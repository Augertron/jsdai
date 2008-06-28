import jsdai.lang.*;

public class HelloSDAI {
	public static void main(String argv[]) throws SdaiException {
		SdaiSession session = SdaiSession.openSession();
		Implementation imp = session.getSdaiImplementation();
		System.out.println("Hello " + imp.getName() );
		session.closeSession();
	}
}
