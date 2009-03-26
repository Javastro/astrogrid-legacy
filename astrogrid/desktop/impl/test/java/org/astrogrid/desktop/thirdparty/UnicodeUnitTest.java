/**
 * 
 */
package org.astrogrid.desktop.thirdparty;

import junit.framework.TestCase;

/** Test for creating unicode special characters - dingbats.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Apr 29, 20088:15:32 AM
 */
public class UnicodeUnitTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testConstructDingbat() throws Exception {
        System.out.println(Character.toChars(9212));
    }
    

}
