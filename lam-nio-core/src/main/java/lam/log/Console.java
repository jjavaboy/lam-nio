package lam.log;

import java.io.PrintStream;

import com.google.gson.Gson;

/**
* <p>
* 
* </p>
* @author linanmiao
* @date 2017年2月7日
* @versio 1.0
*/
public class Console {
	
	private static PrintStream out = System.out;
	
	private static final String LEVEL_DEBUG = "DEBUG";
	
	private static final String LEVEL_INFO = "INFO";
	
	private static final String LEVEL_ERROR = "ERROR";

	private static Gson gson = new Gson();
	
	private static String getThreadName(){
		return Thread.currentThread().getName();
	}
	
	public static void println(String log){
		StackTraceElement stackTrace = getUpStackTraceElement();
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append(LEVEL_INFO).append("(").append(stackTrace.getLineNumber()).append(") ").append(getThreadName()).append(" ")
			.append(stackTrace.getClassName()).append(".").append(stackTrace.getMethodName()).append(" ").append(log);
		out.println(MyLog.timeAppend(logBuilder.toString()));
	}
	
	public static void println(String format, Object...args){
		println(String.format(format, args));
	}
	
	public static void println(Object object){
		println(nvl(object));
	}
	
	public static void print(String log){
		StackTraceElement stackTrace = getUpStackTraceElement();
		StringBuilder logBuilder = new StringBuilder();
		logBuilder.append(LEVEL_INFO).append("(").append(stackTrace.getLineNumber()).append(") ").append(getThreadName()).append(" ")
			.append(stackTrace.getClassName()).append(".").append(stackTrace.getMethodName()).append(" ").append(log);
		out.print(MyLog.timeAppend(logBuilder.toString()));
	}
	
	public static void print(Object object){
		print(nvl(object));
	}
	
	public static void error(Exception e){
		out.println(e.getMessage());
	}
	
	public static String nvl(Object object){
		return object == null ? "null" : gson.toJson(object);
	}
	
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
