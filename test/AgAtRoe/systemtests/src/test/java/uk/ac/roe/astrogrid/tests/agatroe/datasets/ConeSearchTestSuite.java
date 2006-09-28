package uk.ac.roe.astrogrid.tests.agatroe.datasets;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.log4j.Logger;
import org.astrogrid.acr.builtin.ACR;
import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.Content;
import org.astrogrid.acr.ivoa.resource.Resource;
import org.astrogrid.acr.ivoa.resource.Source;

import uk.ac.roe.astrogrid.tests.RuntimeRequiringTestCase.AcrGetter;

public class ConeSearchTestSuite extends TestSuite {
    /**
     * Logger for this class
     */
    private static final Logger logger = Logger.getLogger(ConeSearchTestSuite.class);

    private static final String SEARCHSTRING = null;

    private static AcrGetter acrGetter;

    public static void setWorkbenchStyle(AcrGetter getter) {
        acrGetter = getter;
    }
    
    public ConeSearchTestSuite() {
        try {
            findConeSearches();
        } catch (Exception e) {
            logger.error("Error locating cone searches",e);
            TestCase.fail("Error locating cone searches");
        }
    }

    private void findConeSearches() throws Exception {
        ACR acr = acrGetter.create();
        Registry reg = (Registry) acr.getService(Registry.class);
        Resource[] resources = reg.adqlsSearch(SEARCHSTRING);
        for (int i = 0; i<resources.length;++i) {
            Resource coneSearchResource = resources[i];
            Content content =  coneSearchResource.getContent();
            Source src = content.getSource();
            
        }
        
        for (int i = 0 ;i<10 ;++i) {
            addTest(new ConeSearchTestCase("test "+i));
        }
        
    }
    
    public class ConeSearchTestCase extends TestCase {
        private String ivorn;
        public ConeSearchTestCase(String ivorn) {
            super("testCone");
            this.ivorn = ivorn;
            logger.info("Creating cone search "+ivorn);
        }
        public void testCone() {
            logger.info("Running cone search "+ivorn);
        }
    }
    
    public static Test suite() {
        return new ConeSearchTestSuite();
    }
}
