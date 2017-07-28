package excel;

import java.io.IOException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;

public class Excel2XmlTransformer {

	
	private final Logger LOGGER = Logger.getLogger(getClass());
	
	private Transformer transformer;
	private String output;
	
	private ExcelDoc excelDoc;
	
	public Excel2XmlTransformer(ExcelDoc doc,String output) {
		this.excelDoc = doc;
		this.output = output;
	}
	
	
	public void doParse() throws IOException{
		
		try {
			setUp();
			Document doc = excelDoc.generateDOM();
			if(doc == null)
				return;
			transformer.transform(new DOMSource(doc), new StreamResult(output));
		} catch (TransformerException e) {
			LOGGER.error(e);
		} catch (ParserConfigurationException e) {
			LOGGER.error(e);
		}
	}

	private void setUp() throws ParserConfigurationException, TransformerConfigurationException {
		TransformerFactory tansFactory = TransformerFactory.newInstance();
		transformer = tansFactory.newTransformer();
		transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");  
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
	}
	

}
