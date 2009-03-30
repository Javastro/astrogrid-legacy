/**
 * 
 */
package org.astrogrid.acr.astrogrid;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:04:58 PM
 */
public class UserInformationTest extends AbstractInformationTestAbstract {

    /**
     * 
     */
    private static final String PASS = "pass";
    /**
     * 
     */
    private static final String COMM = "comm";

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        ui = new UserInformation(ID,NAME,PASS,COMM);
        ai = ui;
    }
    private UserInformation ui;

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testCommunity() throws Exception {
        assertEquals(COMM,ui.getCommunity());
        
    }
    
    public void testPassword() throws Exception {
        assertEquals(PASS,ui.getPassword());
    }

}
