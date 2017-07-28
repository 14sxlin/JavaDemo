package test.excel2xml;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.junit.Test;

import excel.Excel2XmlTransformer;
import excel.ExcelDoc;
import excel.SystemExcelDoc;



public class TestExcel2XmlTransformer {

	Excel2XmlTransformer transformer;
	String src = "D:/system.xlsx";
	String target = "D:/system.xml";
	@Test
	public void test() throws IOException, TransformerException, ParserConfigurationException {
		File xmlFile = new File(target);
		if(xmlFile.exists())
			xmlFile.delete();
		ExcelDoc doc = new SystemExcelDoc(src);
		transformer = new Excel2XmlTransformer(doc,target);
		transformer.doParse();
		
		assertTrue(xmlFile.exists());
	}

}
