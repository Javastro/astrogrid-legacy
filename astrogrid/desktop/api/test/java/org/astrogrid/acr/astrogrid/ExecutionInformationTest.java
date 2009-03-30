/**
 * 
 */
package org.astrogrid.acr.astrogrid;

import java.util.Date;


/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200811:53:36 AM
 */
public class ExecutionInformationTest extends AbstractInformationTestAbstract {

    /**
     * 
     */
    private static final String STATUS = "a status";
    /**
     * 
     */
    private static final String DESCRIPTION = "a description";
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        start = new Date();
        end = new Date();
        ei = new ExecutionInformation(ID,NAME,DESCRIPTION,STATUS,start,end);
        ai = ei;
    }
    ExecutionInformation ei;
    Date start;
    Date end;
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testDescription() throws Exception {
        assertEquals(DESCRIPTION,ei.getDescription());
    }
    
    public void testStatus() throws Exception {
        assertEquals(STATUS,ei.getStatus());
    }
    
    public void testStart() throws Exception {
     assertEquals(start,ei.getStartTime());   
    }
    
    public void testEnd() throws Exception {
        assertEquals(end,ei.getFinishTime());
    }

}
