package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerConfigurationException;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class SystemExcelDoc implements ExcelDoc {

    private final static String TITLE = "##";
//	private final static String BEGIN_ATTRIBUTE = "#begin_attr";
//	private final static String END_ATTRIBUTE = "#end_attr";
    private final static String BEGIN_ = "#begin_";
    private final static String END_ = "#end_";

    private final Logger LOGGER = Logger.getLogger(getClass());

    private boolean rowHasHeaders = false;
    private XSSFWorkbook wb;
    private DocumentBuilder builder;
    private Document doc;
    private Element rootElem;
    private Element sheetElem;

    private String itemName;
    private ArrayList<String> headers ;

    private File xlsxFile;
    private InputStream in;

    public SystemExcelDoc(final String xlsxFile) {
        this.xlsxFile = new File(xlsxFile);
    }

    public Document generateDOM() {
        try {
            setUp();
            loadXlsxFile(xlsxFile.getAbsolutePath());
            addRootElement(prefix(xlsxFile.getName(), "root"));
            startParse();
            finishParse();
        } catch (final IOException e) {
            LOGGER.error(e);
        } catch (final TransformerConfigurationException e) {
            LOGGER.error(e);
        } catch (final ParserConfigurationException e) {
            LOGGER.error(e);
        }
        return doc;
    }



    private void setUp() throws ParserConfigurationException, TransformerConfigurationException {
        final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        builder = factory.newDocumentBuilder();
    }

    private void loadXlsxFile(final String xlsxPath) throws IOException {
        LOGGER.info("begin to load : " + xlsxPath
                + " .. please wait");

        xlsxFile = new File(xlsxPath);
        in = new FileInputStream(xlsxPath);
        wb = new XSSFWorkbook(in);

        LOGGER.info("load success .. begin to parse ");
    }

    public void addRootElement(final String elemName) {
        doc = builder.newDocument();
        rootElem = doc.createElement(elemName);
        doc.appendChild(rootElem);
    }

    private String prefix(final String fileName,final String _default) {
        if(fileName == null || !fileName.contains(".")) {
            return _default;
        }
        return fileName.substring(0, fileName.indexOf("."));
    }

    private void startParse() {
        LOGGER.info("parsing .. please wait");
        for(final XSSFSheet sheet : wb) {
            parseSheet(sheet);
        }
        LOGGER.info("parse success");
    }

    public void parseSheet(final Sheet sheet) {
        LOGGER.debug("parse sheet: '"+ sheet.getSheetName()+"'");

        sheetElem = doc.createElement(sheet.getSheetName()+"_sheet");

        for(final Row row : sheet) {
            parseRow(row);
        }

        rootElem.appendChild(sheetElem);
    }

    public void parseRow(final Row row) {
        LOGGER.debug("parse row from : "+row.getFirstCellNum());

        if(is(row,TITLE)) {
            itemName = firstCellValue(row).replaceAll("#", "");
            LOGGER.debug("fetch title : <" +itemName +">");
        }else if(is(row, BEGIN_)) {
            rowHasHeaders = true;
            LOGGER.debug("skip begin_elem(attr) , next row begin parse header " );
            return;

        }else if(is(row, END_)){
            LOGGER.debug("skip end_elem(attr) " );
            return;

        }else{
            if(rowHasHeaders) {
                parseHeader(row);
                rowHasHeaders = false;

            }else {
                parseElemData(row);
            }
        }
    }

    private boolean is(final Row row, final String rowType) {
        if(row.getCell(row.getFirstCellNum())
                .getStringCellValue()
                .contains(rowType)) {
            return true;
        }
        return false;
    }

    private String firstCellValue(final Row row) {
        return row.getCell(row.getFirstCellNum())
                .getStringCellValue();
    }

    private String cellValueAt(final Row row, final int i) {
        final Cell cell = row.getCell(i);
        if(cell == null) {
            return "";
        }
        switch (cell.getCellType())
        {
           case Cell.CELL_TYPE_NUMERIC:
               return ""+cell.getNumericCellValue();
           case Cell.CELL_TYPE_STRING:
               return cell.getStringCellValue();
           case Cell.CELL_TYPE_BOOLEAN:
               if(cell.getBooleanCellValue()) {
                return "true";
            } else {
                return "false";
            }
           default:
               return cell.getStringCellValue();
        }
    }

    public void parseHeader(final Row headerRow) {
        LOGGER.debug("begin header parse" );

        headers = new ArrayList<String>();
        for(int i = headerRow.getFirstCellNum();
                i <= headerRow.getLastCellNum(); i++) {
            final Cell cell = headerRow.getCell(i);
            if( cell == null) {
                break;
            }
            final String value = removeInvalidCharacters(
                    cell.getStringCellValue()).trim();
            if(! "".equals(value)) {
                headers.add(value);
            }

            LOGGER.debug(""+ i + " head : "+cell.getStringCellValue());
        }
    }

    private String removeInvalidCharacters(final String value) {
        if(value == null) {
            return "";
        }
        return value.replace("$", "")
                .replace("*", "");
    }

    public void parseElemData(final Row valueRow) {
        LOGGER.debug("begin data parse" );
        final Element itemElem = doc.createElement(itemName);
        for(int i = 0; i<headers.size(); i++) {
            final String value = removeInvalidCharacters(
                    cellValueAt(valueRow,i));
            LOGGER.debug("" + i + " : " + headers.get(i) + "  :  " + value);
            itemElem.setAttribute(headers.get(i),value);
        }
        sheetElem.appendChild(itemElem);
    }

    private void finishParse() {
        try {
            in.close();
        } catch (final IOException e) {
            LOGGER.error(e);
        }
    }

}
