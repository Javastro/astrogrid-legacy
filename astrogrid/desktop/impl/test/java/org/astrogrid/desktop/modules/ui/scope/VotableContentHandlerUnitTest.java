/**
 * 
 */
package org.astrogrid.desktop.modules.ui.scope;

import static org.easymock.EasyMock.*;

import java.io.InputStream;

import javax.xml.parsers.SAXParserFactory;

import junit.framework.TestCase;

import org.astrogrid.desktop.modules.ui.scope.VotableContentHandler.VotableHandler;
import org.astrogrid.desktop.modules.util.TablesImplUnitTest;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import uk.ac.starlink.table.StarTable;

/** Unit test for the extended table handler.
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Nov 28, 200712:51:53 PM
 */
public class VotableContentHandlerUnitTest extends TestCase {


    private VotableHandler h;
    private XMLReader parser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        h = createStrictMock(VotableHandler.class);
        
        final SAXParserFactory newInstance = SAXParserFactory.newInstance();
        newInstance.setValidating(false);
        parser = newInstance.newSAXParser().getXMLReader();
        final VotableContentHandler votHandler = new VotableContentHandler(false);
        votHandler.setReadHrefTables(false);
        votHandler.setVotableHandler(h);
        parser.setContentHandler(votHandler);
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        h = null;
        parser = null;
    }
    
    /** straightforward siap response, checking that the data still gets parsed by superclass */
    public void testParseBasic() throws Exception {
        // set up expectations.
        // one resource
        h.resource(null,null,"results");
        
        expectDataTable();
                       
        replay(h);
        final InputStream is = TablesImplUnitTest.class.getResourceAsStream("siap.vot");
        assertNotNull("siap.vot not found",is);
        final InputSource src = new InputSource(is);
        parser.parse(src);
        verify(h);
    }
    
    /** example with no data, checking that nested resources get detected */
    public void testParseMultipleNestedResource() throws Exception {
        // set up expectations.                
        h.resource(null,null,"results");
        h.resource("fred","42","results");
        h.resource("nested",null,"meta");
        
                       
        replay(h);
        final InputStream is = this.getClass().getResourceAsStream("resources.vot");
        assertNotNull("resources.vot not found",is);
        final InputSource src = new InputSource(is);
        parser.parse(src);
        verify(h);
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
                       
        replay(h);
        final InputStream is = this.getClass().getResourceAsStream("infos.vot");
        assertNotNull("resources.vot not found",is);
        final InputSource src = new InputSource(is);
        parser.parse(src);
        verify(h);        
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
        expectDataTable();
        expectDataTable();
        expectDataTable();
        expectDataTable();    
        replay(h);
        
        final InputStream is =this.getClass().getResourceAsStream("bz1729.votable");
        assertNotNull("bz1729.votable not found",is);
        final InputSource src = new InputSource(is);
        parser.parse(src);
        verify(h);
    }

    /**
     * @throws SAXException
     */
    private void expectDataTable() throws SAXException {
        // once call to start table - don't care about args
        h.startTable((StarTable)notNull());     
        // at least one row - don't care about args
        h.rowData((Object[])notNull());
        expectLastCall().atLeastOnce();       
        // one call to end table
        h.endTable();
    }

    
}
