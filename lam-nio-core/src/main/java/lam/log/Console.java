package lam.log;

import java.io.PrintStream;

import com.google.gson.Gson;

/**
* <p>
* Set system property "lam.log.isAsyn" to be "true", then log worker will be asyn mode.
* </p>
* @author linanmiao
* @date 2017年2月7日
* @versio 1.0
*/
public class Console {
	
	//private static PrintStream out = System.out;
	
	private static final String LEVEL_DEBUG = "DEBUG";
	
	private static final String LEVEL_INFO = "INFO";
	
	private static final String LEVEL_ERROR = "ERROR";

	private static Gson gson = new Gson();
	
	private LAppender appender;
	
	private Console(){
		appender = new LamLAppender();
	}
	
	private static class ConsoleHolder {
		private static Console INSTANCE = new Console();
	}
	
	public static Console getInstance() {
		return ConsoleHolder.INSTANCE;
	}
	
	private static String getThreadName(){
		return Thread.currentThread().getName();
	}
	
	public static void println(String log){
		StackTraceElement stackTrace = getUpStackTraceElement();
		log = formLogWithStackInfo(log, stackTrace);
		getInstance().appender.append(appendLineSeparator(MyLog.timeBefore(log)));
	}
	
	public static void println(String format, Object...args){
		StackTraceElement stackTrace = getUpStackTraceElement();
		String log = formLogWithStackInfo(String.format(format, args), stackTrace);
		getInstance().appender.append(appendLineSeparator(MyLog.timeBefore(log)));
	}
	
	public static void println(Object object){
		StackTraceElement stackTrace = getUpStackTraceElement();
		String log = formLogWithStackInfo(nvl(object), stackTrace);
		getInstance().appender.append(appendLineSeparator(MyLog.timeBefore(log)));
	}
	
	public static void print(String log){
		StackTraceElement stackTrace = getUpStackTraceElement();
		log = formLogWithStackInfo(log, stackTrace);
		getInstance().appender.append(MyLog.timeBefore(log));
	}
	
	public static void print(Object object){
		StackTraceElement stackTrace = getUpStackTraceElement();
		String log = formLogWithStackInfo(nvl(object), stackTrace);
		getInstance().appender.append(MyLog.timeBefore(log));
	}
	
	public static void error(Exception e){
		StackTraceElement stackTrace = getUpStackTraceElement();
		String log = formLogWithStackInfo(e.getMessage(), stackTrace);
		getInstance().appender.append(appendLineSeparator(MyLog.timeBefore(log)));
	}
	
	private static String nvl(Object object){
		return object == null ? "null" : gson.toJson(object);
	}
	
	private static String formLogWithStackInfo(String log, StackTraceElement stackTrace) {
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append(LEVEL_INFO).append("(").append(stackTrace.getLineNumber()).append(") ").append(getThreadName()).append(" ")
			.append(stackTrace.getClassName()).append(".").append(stackTrace.getMethodName()).append(" ").append(log);
		return logBuilder.toString();
	}
	
	private static String appendLineSeparator(String str) {
		return str + System.lineSeparator();
	}
	
	/*private static void outPrintln(String log) {
		out.println(log);
	}
	
	private static void outPrint(String log) {
		out.print(log);
	}*/
	
	public static StackTraceElement getCurrentStackTraceElement() {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		return stackTraces[2];
	}
	
	public static StackTraceElement getUpStackTraceElement() {
		StackTraceElement[] stackTraces = Thread.currentThread().getStackTrace();
		return stackTraces[3];
	}
	
	public void test(int i, String s, int[] ints, String[] strs) {
		//MyLog.getMethodName();
		Console.print("日志");
	}
	
	public static void main(String[] args) {
		new Console().test(1, "ab", new int[]{2, 3}, new String[]{"bc", "def"});
	}

}
