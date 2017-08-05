package test.excel2xml;


import static org.junit.Assert.assertNotNull;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

public class TestExcelReader {

	private final String xlsPath = "D:/system.xlsx";
	@Test
	public void test() throws InvalidFormatException, IOException {
		InputStream in = new FileInputStream(new File(xlsPath));
		XSSFWorkbook workbook = new XSSFWorkbook(in);
		assertNotNull(workbook);
		
		for(XSSFSheet sheet : workbook) {
			System.out.println("sheet************* : " + sheet.getSheetName());
			for(Row row : sheet) {
				for(Cell cell : row) {
					switch (cell.getCellType()) 
		            {
		               case Cell.CELL_TYPE_NUMERIC:
		               System.out.print( 
		               cell.getNumericCellValue() + " \t\t " );
		               break;
		               case Cell.CELL_TYPE_STRING:
		               System.out.print(
		               cell.getStringCellValue() + " \t\t " );
		               break;
		            }
				}
				System.out.println();
			}
		}
		
		in.close();
	}

}
