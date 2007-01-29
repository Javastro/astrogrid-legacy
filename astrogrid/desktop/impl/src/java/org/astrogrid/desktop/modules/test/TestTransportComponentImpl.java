/**
 * 
 */
package org.astrogrid.desktop.modules.test;

import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.test.TransportTest;

/** implementation of transport test.
 * not named 'TransportTestImpl', as this conflicts with naming convention for tests, 
 * and gets everyting messed up.
 * @author Noel Winstanley
 * @since Jun 15, 20061:34:13 AM
 */
public class TestTransportComponentImpl implements TransportTest {
/**
 * 
 */
public TestTransportComponentImpl() {
}
	public byte[] echoByteArray(byte[] arg0) {
		return arg0;
	}

	public void throwCheckedException() throws NotFoundException {
		throw new NotFoundException("from throwCheckedException");
	}

	public void throwUncheckedException() {
		throw new NullPointerException("from throwUncheckedException");
	}

	public void throwUncheckedExceptionOfUnknownType() {
		throw new AnUnknownRuntimeException("from throwUncheckedExceptionOfUnknownType");
	}
	

}
