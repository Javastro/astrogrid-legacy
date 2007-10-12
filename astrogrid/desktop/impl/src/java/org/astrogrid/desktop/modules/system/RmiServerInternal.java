/**
 * 
 */
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.system.RmiServer;
import org.astrogrid.desktop.modules.util.Selftest;

/** internal interface to rmiserver, that also allows selftesting.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 12, 200710:51:06 AM
 */
public interface RmiServerInternal extends RmiServer, Selftest {

}
