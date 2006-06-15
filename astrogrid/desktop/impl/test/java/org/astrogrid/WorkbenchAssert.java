/**
 * 
 */
package org.astrogrid;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import junit.framework.Assert;

/** class of static helper assertions.
 * @author Noel Winstanley
 * @since Jun 14, 20065:22:53 PM
 */
public class WorkbenchAssert extends Assert {

	/** test an object is serializable by seriazing, deserializing, and then comparing two objects for equality using the comparator 
	 * 
	 * @throws ClassNotFoundException */
	public static void assertSerializable(Object o) throws IOException, ClassNotFoundException {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(os);
		oos.writeObject(o);
		oos.close();
		InputStream is = new ByteArrayInputStream(os.toByteArray());
		ObjectInputStream ois = new ObjectInputStream(is);
		Object nu = ois.readObject();
		assertNotNull(nu);
		assertEquals(o.getClass(),nu.getClass());
		assertNotSame(o,nu);
		assertEquals(o,nu);
		
	}

}
