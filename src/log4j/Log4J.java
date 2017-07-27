package log4j;

import org.apache.log4j.Logger;

public class Log4J {
	public final static Logger logger = Logger.getLogger("test demo logger");
	
	public static void main(String[] args) {
		logger.info("create log success");
		logger.warn("test warning");
		logger.error("error",new Exception("test exception"));
		System.out.println("run");
	}
}
