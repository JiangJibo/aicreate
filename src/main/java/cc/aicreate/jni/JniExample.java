
package cc.aicreate.jni;

public class JniExample {

	static {
		System.loadLibrary("niExample");
	}

	// native methods
	/**
	 * this method calls printSomething
	 */
	public native void callJavaMethod();

	/**
	 * this method creates an instance of Bean and returns it
	 */
	public native Bean createAndReturnBean();

	/**
	 * this method takes an instance of Bean as parameter and changes the value of its members
	 */
	public native void modifyBean(Bean bean);

	/**
	 * this method will the JVM when invoked
	 */
	public native void crashTheJvm();

	public void printSomething() {
		System.out.println("Thanks for watching this video");
	}

	public void runExample1() {
		System.out.println("starting runExample1...");
		callJavaMethod();
	}

	public void runExample2() {
		System.out.println("starting runExample2...");

		Bean bean = createAndReturnBean();
		System.out.println("returned= " + bean.toString());
	}

	public void runExample3() {
		System.out.println("starting runExample3...");

		Bean bean = new Bean();
		bean.setDataString("hello");

		byte[] byteArray = new byte[] { 0x01, 0x02, 0x03 };
		bean.setDataByteArray(byteArray);

		System.out.println("before: " + bean.toString());
		modifyBean(bean);
		System.out.println("after: " + bean.toString());
	}

	public void runExample4() {
		System.out.println("starting runExample4...");
		crashTheJvm();
	}

}
