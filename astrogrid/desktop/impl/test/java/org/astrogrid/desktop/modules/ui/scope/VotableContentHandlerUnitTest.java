/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import java.io.InputStream;
import java.util.Arrays;

import javax.xml.parsers.SAXParserFactory;

import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;
import org.astrogrid.desktop.modules.util.TablesImplUnitTest;
import org.easymock.ArgumentsMatcher;
import org.easymock.MockControl;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.StarTable;
import junit.framework.TestCase;

/** Unit test for the extended table handler.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 28, 200712:51:53 PM
 */
public class VotableContentHandlerUnitTest extends TestCase {

    /**
     * @author Noel.Winstanley@manchester.ac.uk
     * @since Nov 28, 20071:17:56 PM
     */
    private final class NonNullArgument implements ArgumentsMatcher {
        public boolean matches(Object[] expected, Object[] actual) {
            return actual.length == 1 && actual[0] != null;
        }

        public String toString(Object[] arg0) {
          return  arg0[0].toString();
        }
    }

    private VotableHandler h;
    private MockControl m;
    private XMLReader parser;

    protected void setUp() throws Exception {
        super.setUp();
        m = MockControl.createStrictControl(VotableContentHandler.VotableHandler.class);
        h = (VotableContentHandler.VotableHandler)m.getMock();
        
        parser = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
        VotableContentHandler votHandler = new VotableContentHandler(false);
        votHandler.setReadHrefTables(false);
        votHandler.setVotableHandler(h);
        parser.setContentHandler(votHandler);
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        m = null;
        h = null;
        parser = null;
    }
    
    /** straightforward siap response, checking that the data still gets parsed by superclass */
    public void testParseBasic() throws Exception {
        // set up expectations.
        // one resource
        h.resource(null,null,"results");
        
        expectDataTable();
                       
        m.replay();
        InputStream is = TablesImplUnitTest.class.getResourceAsStream("siap.vot");
        assertNotNull("siap.vot not found",is);
        InputSource src = new InputSource(is);
        parser.parse(src);
        m.verify();
    }
    
    /** example with no data, checking that nested resources get detected */
    public void testParseMultipleNestedResource() throws Exception {
        // set up expectations.                
        h.resource(null,null,"results");
        h.resource("fred","42","results");
        h.resource("nested",null,"meta");
        
                       
        m.replay();
        InputStream is = this.getClass().getResourceAsStream("resources.vot");
        assertNotNull("resources.vot not found",is);
        InputSource src = new InputSource(is);
        parser.parse(src);
        m.verify();
    }
    
    /** full complex example - nesting, data
     * testing that infos and params at the top level get detected.
     * 
     * srictly speaking, according to DTD, INFO must occur only before first 
     * resource - but we'll test whether we can see all INFO.
     * */
    public void testNestedResourcesInfoParams() throws Exception {
       
        // set up expectations.   
        h.info("QUERY_STATUS","OK","content");
        h.param("p1","v1","content");
        h.param("p2","v2","content2");
        h.resource(null,null,"results");
        h.info("n","v","c");
        h.resource("fred","42","results");
        h.resource("nested",null,"meta");
        h.info("na","va","co");
        h.resource(null,null,"results");
        
        expectDataTable();
        h.info("a","b","c");        
                       
        m.replay();
        InputStream is = this.getClass().getResourceAsStream("infos.vot");
        assertNotNull("resources.vot not found",is);
        InputSource src = new InputSource(is);
        parser.parse(src);
        m.verify();        
    }
    
    /** test that we're getting the desired results when parsing 
     * service that triggered bugzilla 1729
     * @throws Exception
     */
    public void testParseBz1729() throws Exception {
        // set up expectations.
        h.resource(null,null,"results");
        
        expectDataTable();
        
        h.resource("FOV Extensions","FOV","meta");
        h.resource("HST WFPC2 Field of View","HST_WFPC2_FOV","results");
        anotherTable();
        anotherTable();
        anotherTable();
        anotherTable();        
        m.replay();
        
        InputStream is =this.getClass().getResourceAsStream("bz1729.votable");
        assertNotNull("siap.vot not found",is);
        InputSource src = new InputSource(is);
        parser.parse(src);
        m.verify();
    }

    /**
     * @throws SAXException
     */
    private void expectDataTable() throws SAXException {
        // once call to start table - don't care about args
        h.startTable(null);
        m.setMatcher(new NonNullArgument());        
        // at least one row - don't care about args
        h.rowData(null);
        m.setMatcher(new NonNullArgument());
        m.setVoidCallable(  MockControl.ONE_OR_MORE);        
        // one call to end table
        h.endTable();
    }
    
    //necessaery as yoou can't set a matcher more than once for a method, it seems.
    private void anotherTable() throws SAXException {
        // once call to start table - don't care about args
        h.startTable(null);     
        // at least one row - don't care about args
        h.rowData(null);
        m.setVoidCallable(  MockControl.ONE_OR_MORE);        
        // one call to end table
        h.endTable();
    }

    
}
