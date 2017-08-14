/**
 * Copyright(C) 2017 MassBot Co. Ltd. All rights reserved.
 *
 */
package cc.aicreate.jni;

/**
 * @since 2017年7月27日 下午2:51:53
 * @version $Id$
 * @author JiangJibo
 *
 */
public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Starting JavaMain...");

		JniExample jniExample = new JniExample();
		jniExample.runExample1();
		jniExample.runExample2();
		jniExample.runExample3();
		jniExample.runExample4();
	}

}
