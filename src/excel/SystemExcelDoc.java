package excel;

import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;

import org.apache.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
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
	private DocumentBuilder builder;
	private Document doc;
	private Element rootElem;
	private Element sheetElem;
	
	private String itemName;
	private ArrayList<String> headers ;
	
	@Override
	public void addRootElement(String elemName) {
		doc = builder.newDocument();
		rootElem = doc.createElement(elemName);
		doc.appendChild(rootElem);
	}

	@Override
	public void parseSheet(Sheet sheet) {
		LOGGER.debug("parse sheet: '"+ sheet.getSheetName()+"'");
		
		sheetElem = doc.createElement(sheet.getSheetName()+"_sheet");
		
		for(Row row : sheet) {
			parseRow(row);
		}
		
		rootElem.appendChild(sheetElem);
	}

	@Override
	public void parseRow(Row row) {
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

	@Override
	public void parseHeader(Row headerRow) {
		LOGGER.debug("begin header parse" );
		
		headers = new ArrayList<String>();
		for(int i = headerRow.getFirstCellNum();
				i <= headerRow.getLastCellNum(); i++) {
			Cell cell = headerRow.getCell(i);
			if( cell == null)
				break;
			String value = removeInvalidCharacters(
					cell.getStringCellValue()).trim();
			if(! "".equals(value))
				headers.add(value);
			
			LOGGER.debug(""+ i + " head : "+cell.getStringCellValue());
		}
	}
	
	private String removeInvalidCharacters(String value) {
		if(value == null)
			return "";
		return value.replace("$", "")
				.replace("*", "");
	}

	@Override
	public void parseElemData(Row valueRow) {
		LOGGER.debug("begin data parse" );
		Element itemElem = doc.createElement(itemName);
		for(int i = 0; i<headers.size(); i++) {
			String value = removeInvalidCharacters(
					cellValueAt(valueRow,i));
			LOGGER.debug("" + i + " : " + headers.get(i) + "  :  " + value);
			itemElem.setAttribute(headers.get(i),value);
		}
		sheetElem.appendChild(itemElem);
	}

}
