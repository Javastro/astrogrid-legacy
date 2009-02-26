/**
 * 
 */
package org.astrogrid.desktop.modules.file;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.file.Systems;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;
/**
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Dec 30, 20086:09:46 PM
 */
public class SystemsImplIntegrationTest extends InARTestCase {

    private Systems s;

    protected void setUp() throws Exception {
        super.setUp();
        s = (Systems)assertServiceExists(Systems.class,"file.systems");
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        s = null;
    }
    
    public static Test suite() {
        return new ARTestSetup(new TestSuite(SystemsImplIntegrationTest.class));
    }
    public void testSchemes() throws Exception {
        final String[] fs = s.listSchemes();
        assertNotNull(fs);
        assertThat(fs,allOf(
                hasItemInArray("file")
                ,hasItemInArray("ftp")
                ,hasItemInArray("sftp")
                ,hasItemInArray("ivo")
                ,hasItemInArray("tmp")
                ,hasItemInArray("http")
                ));
        
        
    }

}
