package lam.offheap;

import java.nio.ByteBuffer;

import lam.util.Threads;

/**
* <p>
* TODO
* </p>
* @author linanmiao
* @date 2018年1月5日
* @version 1.0
*/
public class DirectByteBufferTest {

	public static void main(String[] args) {
		//堆外内存，不在JVM的管理下
		ByteBuffer byteBuffer = ByteBuffer.allocateDirect(1024 * 1024 * 10);
		Threads.sleepWithUninterrupt(5000L);
		//释放内存
		((sun.nio.ch.DirectBuffer)byteBuffer).cleaner().clean();
	}

}
