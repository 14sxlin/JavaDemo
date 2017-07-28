package excel;

import java.io.IOException;

import org.apache.log4j.Logger;


public class Main {
	public static final Logger LOGGER = Logger.getLogger("simba");
	
	public static final String XML = "xml";
	public static final String XLSX = "xlsx";
	
	public static void main(String[] args) {
		if(argsWrong(args)) {
			System.out.println("please run : "
					+ "java -jar src.xlsx target.xml");
		}else {
			process(args);
		}

	}

	private static void process(String[] args) {
		String firstFile = args[0];
		String secondFile = args[1];
		if(isType(firstFile, XLSX) && isType(secondFile, XML))
		{
			transform(firstFile,secondFile);
		}
		else {
			System.out.println("Note that : "
					+ "first file should be xlsx file"
					+ "and second file should be xml file");
		}
	}

	private static boolean argsWrong(String[] args) {
		return args == null || args.length != 2;
	}
	
	public static void transform(String xlsx, String xml) {
		try {
			Excel2XmlTransformer transformer = 
					new Excel2XmlTransformer(xlsx, xml);
			transformer.doParse();
		} catch (IOException e) {
			LOGGER.error(e);
		}
	}
	
	private static boolean isType(String file,String type) {
		if(file.contains("."))
		{
			if(type.equals(
					file.substring(file.indexOf(".")+1).toLowerCase()))
				return true;
		}
		return false;
	}

}
