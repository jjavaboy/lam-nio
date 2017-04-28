package lam.serialization;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2017年4月28日
* @version 1.0
*/
public class HessianTest {

	public static void main(String[] args) {
		try {
			OutputStream outputStream = new FileOutputStream("./hessian.xml");
			Hessian2Output hessian2Output = new Hessian2Output(outputStream);
			try {
				Detector d = new Detector();
				d.bool = Boolean.TRUE.booleanValue();
				d.by = 1;
				d.sh = 2;
				d.ch = 'a';
				d.id = 3;
				d.lo = 4L;
				d.fl = 1.2F;
				d.doub = 2.4D;
				d.dat = new Date();
				d.name = "lam";
				hessian2Output.writeObject(d);
				hessian2Output.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			InputStream inputStream = new FileInputStream("./hessian.xml");
			Hessian2Input hessian2Input = new Hessian2Input(inputStream);
			try {
				Detector detector = (Detector) hessian2Input.readObject();
				System.out.println(detector);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	private static class Detector implements java.io.Serializable{
		
		private static final long serialVersionUID = -1048768614666446593L;
		
		boolean bool;
		
		byte by;
		
		short sh;
		
		char ch;
		
		int id;
		
		long lo;
		
		float fl;
		
		double doub;
		
		String name;
		
		java.util.Date dat;
		
		Detector(){}
		
		@Override
		public String toString() {
			StringBuilder sb = new StringBuilder();
			sb.append("{")
			.append("bool").append(":").append(bool).append(", ")
			.append("by").append(":").append(by).append(", ")
			.append("sh").append(":").append(sh).append(", ")
			.append("ch").append(":").append(ch).append(", ")
			.append("id").append(":").append(id).append(", ")
			.append("lo").append(":").append(lo).append(", ")
			.append("fl").append(":").append(fl).append(", ")
			.append("doub").append(":").append(doub).append(", ")
			.append("name").append(":").append(name).append(", ")
			.append("dat").append(":").append(dat)
			.append("}");
			return sb.toString();
		}
	}

}
