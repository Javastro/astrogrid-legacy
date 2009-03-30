/**
 * 
 */
package org.astrogrid.acr.cds;

import junit.framework.TestCase;

/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Sep 26, 200812:07:51 PM
 */
public class SesamePositionBeanTest extends TestCase {

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        sb = new SesamePositionBean();
    }
    SesamePositionBean sb;
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }
    
    public void testAliases() throws Exception {
        final String[] ali = new String[] {
                "foo","bar"
        };
        sb.setAliases(ali);
        assertEquals(ali,sb.getAliases());
        
    }
    public void testDec() throws Exception {
        final double dec = 2.2;
        sb.setDec(dec);
        assertEquals(dec,sb.getDec());         
    }
    
    public void testDecErr() throws Exception {
        final double dece = 2.2;
        sb.setDecErr(dece);
        assertEquals(dece,sb.getDecErr());        
    }
    
    public void testOName() throws Exception {
        final String qn = "qnasd";
        sb.setOName(qn);
        assertEquals(qn,sb.getOName());
    }
    
    public void testPosStr() throws Exception {
        final String pos = "fdsfsd";
        sb.setPosStr(pos);
        assertEquals(pos,sb.getPosStr());
    }
    
    public void testRa() throws Exception {
        final double ra = 2.2;
        sb.setRa(ra);
        assertEquals(ra,sb.getRa());        
    }
    
    public void testRaErr() throws Exception {
        final double rae = 2.2;
        sb.setRaErr(rae);
        assertEquals(rae,sb.getRaErr());
    }
    
    public void testService() throws Exception {
        final String service = "foo";
        sb.setService(service);
        assertEquals(service,sb.getService());
    }
    
    public void testTarget() throws Exception {
        final String target = "t";
        sb.setTarget(target);
        assertEquals(target,sb.getTarget());
        
    }
    
    public void testOType() throws Exception {
        final String ot = "otype";
        sb.setOType(ot);
        assertEquals(ot,sb.getOType());
    }
    
    
    public void testEquals() throws Exception {
        assertEquals(sb,sb);
    }
    public void testHashCode() throws Exception {
        sb.hashCode();
    }
    
    public void testToString() throws Exception {
        assertNotNull(sb.toString());
    }
    
    
    
    
    

}
