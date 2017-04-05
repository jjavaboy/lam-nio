package lam.reference;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

/**
* <p>
* 强引用、软引用、弱引用和影子引用
* </p>
* @author linanmiao
* @date 2017年4月2日
* @versio 1.0
*/
public class ReferenceDetector {
	
	public static void main(String[] args){
		Cow cow = new Cow("niuniu");
		java.lang.ref.ReferenceQueue<Cow> queue = new ReferenceQueue<Cow>();
		java.lang.ref.SoftReference<Cow> softReference = new SoftReference<Cow>(cow, queue);
		cow = null;
		queue = null;
		
		//softReference.clear();
		//==>>this.referent = null; this:means SoftReference Object
		//softReference.enqueue();
		//softReference.isEnqueued();
		
		Cow cow2 = new Cow("niuniu2");
		ReferenceQueue<Cow> queue2 = new ReferenceQueue<Cow>();
		java.lang.ref.WeakReference<Cow> weakReference = new WeakReference<Cow>(cow2, queue2);
		cow2 = null;
		queue2 = null;
		WeakHashMap<String, String> map = new WeakHashMap<String, String>();
		map.put("a", "b");
		map.get("a");
		
		Cow cow3 = new Cow("niuniu3");
		ReferenceQueue<Cow> queue3 = new ReferenceQueue<Cow>();
		java.lang.ref.PhantomReference<Cow> phantomReference = new PhantomReference<Cow>(cow3, queue3);
		cow3 = null;
		queue3 = null;
	}
	
	private static class Cow{
		private String name;
		
		Cow(String name){
			this.name = name;
		}
		
	}

}
