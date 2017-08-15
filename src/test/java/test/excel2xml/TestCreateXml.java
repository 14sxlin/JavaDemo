package test.excel2xml;

import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class TestCreateXml {

	@Test
	public void test() throws ParserConfigurationException, TransformerException {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.newDocument();
		Element elem = doc.createElement("tag1");
		Element elem1 = doc.createElement("tag2");
		elem1.setAttribute("isTest", "true");
		elem1.setTextContent("this is a test tag");
		
		Element childElem = doc.createElement("child");
		childElem.setTextContent("I'm a child");
		
		elem1.appendChild(childElem);
		
		doc.appendChild(elem);
		elem.appendChild(elem1); // root element should have only one
		
		
		TransformerFactory tansFactory = TransformerFactory.newInstance();
		Transformer transformer = tansFactory.newTransformer();
		transformer.setOutputProperty(OutputKeys.INDENT, "yes");
		transformer.transform(new DOMSource(doc),
				new StreamResult(new File("D:/system.xml")));
	}

}
