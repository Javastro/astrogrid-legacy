/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import junit.framework.Test;

/** Interface that provides access to a selftest for a component.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 11, 20071:58:15 PM
 */
public interface Selftest {
    /** access a selftest for the implementor of this interface
     * 
     * @return probably a TestCase or TestSuite
     */
    public Test getSelftest();

}
