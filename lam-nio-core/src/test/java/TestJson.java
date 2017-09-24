import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import com.google.gson.Gson;

import lam.log.Console;
import lam.netty.http.json.factory.OrderFactory;
import lam.netty.http.json.model.Order;
import lam.queue.LQueue;
import lam.queue.blocking.LBlockingQueue;
import lam.queue.blocking.impl.LLinkedBlockingQueue;
import lam.queue.impl.LLinkedQueue;
import lam.util.concurrent.ThreadFactoryBuilder;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2016年10月9日
* @versio 1.0
*/
public class TestJson {

	public static void main(String[] args) {
		String str = "1a 03 08 96 01";
		int i = Integer.parseInt("1a", 16);
		System.out.println(Integer.toBinaryString(i));
	}
	
	static ThreadLocal<String> threadName = new ThreadLocal<String>();
	
	private static class User{
		private int age;
		private String name;
		
		@Override
		public boolean equals(Object obj) {
			if(obj == null || !(obj instanceof User)){
				return false;
			}
			User u = (User)obj;
			return age == u.age && (name == u.name || (name != null && name.equals(u.name)));
		}
	}

}
