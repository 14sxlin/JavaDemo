package linsixin.demo.test;

import java.io.FileOutputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.junit.Test;

public class TestExcelWriter {

	Workbook workbook;
	private Sheet []sheets;
	
	public void createXlsDoc(String xlsName,int sheetNum) throws IOException {
		assert sheetNum>=0 && sheetNum<Integer.MAX_VALUE;
		
		workbook = new HSSFWorkbook();
		sheets = new Sheet[sheetNum];
		for(int i = 0; i < sheetNum; i++) {
			sheets[i] = workbook.createSheet("sheet"+i);
		}
		FileOutputStream out = new FileOutputStream(xlsName);
		workbook.write(out);
		out.close();
	}
	
	public void createCell(Sheet sheet,short rowIndex,short columnIndex) {
		assert rowIndex>=0 && rowIndex<Integer.MAX_VALUE;
		assert columnIndex>=0 && columnIndex<Integer.MAX_VALUE;
		
		Row row = sheet.createRow(rowIndex);
		Cell cell = row.createCell(columnIndex);
		cell.setCellValue("" + rowIndex + " " + columnIndex);
		// TODO
	}

	
	@Test
	public void testCreateExcel() throws IOException {
		createXlsDoc("test.xls", 2);
	}

}
