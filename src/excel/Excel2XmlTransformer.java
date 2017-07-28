package excel;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class Excel2XmlTransformer {

	
	private final Logger LOGGER = Logger.getLogger(getClass());
	
	private XSSFWorkbook wb;

	private DocumentBuilder builder;
	private Document doc;
	private Element rootElem;
	private Element sheetElem;
	
	private final static String TITLE = "##";
//	private final static String BEGIN_ATTRIBUTE = "#begin_attr";
//	private final static String END_ATTRIBUTE = "#end_attr";
	private final static String BEGIN_ = "#begin_";
	private final static String END_ = "#end_";
	
	private boolean rowHasHeaders = false;
	
	private String itemName;
	private ArrayList<String> headers ;
	
	private Transformer transformer;
	private File xlsxFile;
	private InputStream in;
	private String output;
	
	public Excel2XmlTransformer(String xlsxFile, String output) {
		this.xlsxFile = new File(xlsxFile);
		this.output = output;
	}
	
	
	public void doParse() throws IOException{
		try {
			
			setUp();
			loadXlsxFile(xlsxFile.getAbsolutePath());
			addRootElement(prefix(xlsxFile.getName(), "root"));
			startParse();
			finishParse();	
			
		} catch (TransformerConfigurationException | ParserConfigurationException e) {
			LOGGER.error(e);
		} catch (TransformerException e) {
			LOGGER.error(e);
		}
			
	}

	private void setUp() throws ParserConfigurationException, TransformerConfigurationException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		builder = factory.newDocumentBuilder();
		
		TransformerFactory tansFactory = TransformerFactory.newInstance();
		transformer = tansFactory.newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");  
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		
	}

	private void loadXlsxFile(String xlsxPath) throws IOException {
		LOGGER.info("begin to load : " + xlsxPath
				+ " .. please wait");
		
		xlsxFile = new File(xlsxPath);
		in = new FileInputStream(xlsxPath);
		wb = new XSSFWorkbook(in);
		
		LOGGER.info("load success .. begin to parse ");
	}
	
	private void addRootElement(String elemName) {
		doc = builder.newDocument();
		rootElem = doc.createElement(elemName);
		doc.appendChild(rootElem);
	}
	
	private String prefix(String fileName,String _default) {
		if(fileName == null || !fileName.contains("."))
			return _default;
		return fileName.substring(0, fileName.indexOf("."));
	}

	private void startParse() {
		LOGGER.info("parsing .. please wait");
		for(XSSFSheet sheet : wb) {
			parseXSSFSheet(sheet);
		}
		LOGGER.info("parse success");
	}
	
	private void parseXSSFSheet(XSSFSheet sheet) {
		LOGGER.debug("parse sheet: '"+ sheet.getSheetName()+"'");
		
		sheetElem = doc.createElement(sheet.getSheetName()+"_sheet");
		
		for(Row row : sheet) {
			parseRow(row);
		}
		
		rootElem.appendChild(sheetElem);
		
	}
	
	private void parseRow(Row row) {
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
	
	private boolean is(Row row, String rowType) {
		if(row.getCell(row.getFirstCellNum())
				.getStringCellValue()
				.contains(rowType))
			return true;
		return false;
	}
	
	private void parseHeader(Row row) {
		LOGGER.debug("begin header parse" );
		
		headers = new ArrayList<String>();
		for(int i = row.getFirstCellNum();
				i <= row.getLastCellNum(); i++) {
			Cell cell = row.getCell(i);
			if( cell == null)
				break;
			String value = removeInvalidCharacters(
					cell.getStringCellValue()).trim();
			if(! "".equals(value))
				headers.add(value);
			
			LOGGER.debug(""+ i + " head : "+cell.getStringCellValue());
		}
	}
	
	private void parseElemData(Row row) {
		LOGGER.debug("begin data parse" );
		Element itemElem = doc.createElement(itemName);
		for(int i = 0; i<headers.size(); i++) {
			String value = removeInvalidCharacters(
					cellValueAt(row,i));
			LOGGER.debug("" + i + " : " + headers.get(i) + "  :  " + value);
			itemElem.setAttribute(headers.get(i),value);
		}
		sheetElem.appendChild(itemElem);
	}
	
	private String firstCellValue(Row row) {
		return row.getCell(row.getFirstCellNum())
				.getStringCellValue();
	}
	
	private String cellValueAt(Row row, int i) {
		Cell cell = row.getCell(i);
		if(cell == null)
			return "";
		switch (cell.getCellType()) 
        {
           case Cell.CELL_TYPE_NUMERIC:
        	   return ""+cell.getNumericCellValue();
           case Cell.CELL_TYPE_STRING:
        	   return cell.getStringCellValue();
           case Cell.CELL_TYPE_BOOLEAN:
        	   if(cell.getBooleanCellValue())
        		   return "true";
        	   else return "false";
           default:
        	   return cell.getStringCellValue();
        }
	}
	
	private String removeInvalidCharacters(String value) {
		if(value == null)
			return "";
		return value.replace("$", "")
				.replace("*", "");
	}
	
	private void finishParse() throws TransformerException, IOException {
		transformer.transform(new DOMSource(doc),
				new StreamResult(new File(output)));
		in.close();
		LOGGER.info("output: " + output);
	}
	
	

}
