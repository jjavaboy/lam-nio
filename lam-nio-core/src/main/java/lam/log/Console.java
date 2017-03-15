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

	private static Gson gson = new Gson();
	
	public static void println(String log){
		out.println(MyLog.timeAppend(log));
	}
	
	public static void println(String format, Object...args){
		println(String.format(format, args));
	}
	
	public static void println(Object object){
		println(nvl(object));
	}
	
	public static void print(String log){
		out.print(MyLog.timeAppend(log));
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

}
