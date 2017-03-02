package isac.core.log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Logger implements ILogger {

	private static Logger logger = new Logger();
	private SimpleDateFormat dateFormat;

	private Logger() {

		String pattern = "h:mm a";
		dateFormat = new SimpleDateFormat(pattern, new Locale("it", "IT"));

	}

	public static Logger getInstance() {
		return logger;
	}

	@Override
	public void info(String message) {
		String className = this.retrieveClassName();
		String date = this.getDate();
		System.out.println("["+ date + "] [" + className + "] " + message);
	}

	private String getDate() {
		return dateFormat.format(new Date());
	}

	private String retrieveClassName() {
		StackTraceElement[] stackTraceElements = Thread.currentThread().getStackTrace();
		return stackTraceElements[3].getClassName();
	}

	@Override
	public void error(String message) {
		String className = this.retrieveClassName();
		String date = this.getDate();
		System.err.println("["+ date + "] [" + className + "] " + message);

	}

}
