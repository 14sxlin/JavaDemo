package excel;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.w3c.dom.Document;

public interface ExcelDoc {
	void addRootElement(String elemName);
	void parseSheet(Sheet sheet);
	void parseRow(Row row);
	void parseHeader(Row headerRow);
	void parseElemData(Row valueRow);
	Document generateDOM();
}
