/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/integrationTests/auto-integration/test/java/org/astrogrid/myspace/integration/Attic/VoSpaceIntegrationTest.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/03/11 13:36:22 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: VoSpaceIntegrationTest.java,v $
 *   Revision 1.4  2005/03/11 13:36:22  clq2
 *   with merges from filemanager
 *
 *   Revision 1.3.86.1  2005/03/10 19:33:16  nw
 *   marked tests for removal once myspace is replaced by filemanager.
 *
 *   Revision 1.3  2004/09/10 19:16:40  pah
 *   test that we can actually write to an output stream
 *
 *   Revision 1.2  2004/09/06 12:50:12  dave
 *   Added VoSpace integration test.
 *
 *   Revision 1.1.2.5  2004/09/03 15:13:12  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.4  2004/09/03 14:29:48  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.3  2004/09/03 14:22:22  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.2  2004/09/03 13:58:01  dave
 *   Added VoSPace test ....
 *
 *   Revision 1.1.2.1  2004/09/03 13:41:12  dave
 *   Adde VoSPace test ....
 *
 * </cvs:log>
 *
 */
package org.astrogrid.myspace.integration;

import java.io.OutputStreamWriter;
import java.io.PrintWriter;

import org.astrogrid.store.Ivorn;
import org.astrogrid.store.VoSpaceClient;

import org.astrogrid.applications.integration.commandline.CommandLineProviderServerInfo;
import org.astrogrid.integration.AbstractTestForIntegration;

/**
 * JUnit test for the VoSpace client interface to MySpace.
 *  *@deprecated remove this package when we ditch myspace
 *  
 */
public class VoSpaceIntegrationTest extends AbstractTestForIntegration {

    /**
     * Reference to our target VoSpace.
     *  
     */
    protected VoSpaceClient vospace;

    /**
     * Public constructor.
     * 
     * @param name
     *  
     */
    public VoSpaceIntegrationTest(String name) {
        super(name);
    }

    /**
     * Set up our test. Creates our target VoSpaceClient.
     *  
     */
    public void setUp() throws Exception {
        super.setUp();

        vospace = new VoSpaceClient(user);
    }

    /**
     * Test that we can create a VoSpaceClient.
     *  
     */
    public void testCreateClient() throws Exception {
        assertNotNull(vospace);
    }

    /**
     * Test that we can write to an output stream.
     *  
     */
    public void testWriteToStream() throws Exception {
        Ivorn target = createIVORN("/frog.txt");
        PrintWriter pw = new PrintWriter(new OutputStreamWriter(vospace.putStream(target)));
        pw.println("content");
        pw.close();     
    }
}