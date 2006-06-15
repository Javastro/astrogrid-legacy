package org.astrogrid.acr.test;

import org.astrogrid.acr.NotFoundException;
/**
 * 
 */

/** Contains methods with interesting parameters and return types.
 * Used to test correctness of the transport between client and AR.
 * @author Noel Winstanley
 * @since Jun 15, 20061:26:38 AM
 * @service test.transporttest
 */
public interface TransportTest {

	/** method always throws a 'notfound' exception */
	public void throwCheckedException() throws NotFoundException;
	
	/** method always throws a 'NullPointer' exception */
	public void throwUncheckedException() ;
	
	
	/** method always throws a new subtype of RuntimeException */
	public void throwUncheckedExceptionOfUnknownType();
	
	
	/** method simply echos it's parameter byte array back again */
	public byte[] echoByteArray(byte[] arr) ;
}
