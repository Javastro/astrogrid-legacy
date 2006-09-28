package uk.ac.roe.astrogrid.tests;

import java.io.File;
import java.io.IOException;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.Finder;
import org.astrogrid.acr.builtin.ACR;

import edu.stanford.cs.ejalbert.BrowserLauncher;

/**
 * Responsible for settings up a workbench instance through which other tests can access the system. By default, it will start a workbench inline.
 * @author jdt
 */
public abstract class RuntimeRequiringTestCase extends LoggingTestCase {

    /**
     * Class to deal with the knotty problem of creating a Workbench instance in different ways.
     * @author jdt
     */
    public interface AcrGetter {

        ACR create() throws Exception;

    }

    /**
     * The DEFAULT AcrGetter will use any running workbench instance, but will in the absence of
     * one will use one in-process, if the workbench is on the classpath.  In other words,
     * it uses the Workbench Finder class as-is.
     * 
     */
    public static final AcrGetter DEFAULT = new AcrGetter() {

        public ACR create() throws Exception {
            // The built in finder should do the trick, if the Acr is on the classpath.
            return new Finder().find();
        }

    };

    /** 
     * Will always attempt to start the workbench via webstart.
     */
    public static final AcrGetter WORKBENCH_JNLP = new AcrGetter() {
        public static final String ACR_JNLP_URL = "http://software.astrogrid.org/jnlp/workbench/workbench.jnlp";

        private void fireUpWorkbench() throws IOException {
            BrowserLauncher.openURL(ACR_JNLP_URL);
        }

        private ACR getWorkbenchIfRunning() throws ACRException {
            File homeDir = new File(System.getProperty("user.home"));
            File conf = new File(homeDir, ".acr-rmi-port");

            if (!conf.exists()) {
                return null; // Not ready yet
            }
            // must be running so use finder
            return new Finder().find();
        }

        public ACR create() throws Exception {
            fireUpWorkbench();
            // need to wait some time to allow external to bootup (and maybe download).

            while (getWorkbenchIfRunning() == null) {
                Thread.sleep(5000); // 5 seconds.
            }
            return getWorkbenchIfRunning();
        }

    };

    private ACR acr;

    private static AcrGetter acrGetter = DEFAULT; //by default create workbench in process

    protected synchronized ACR getAcr() throws Exception {
        if (acr == null) {
            acr = acrGetter.create();
        }
        return acr;
    }

      

    public static void setWorkbenchStyle(AcrGetter workbench_jnlp2) {
       acrGetter = workbench_jnlp2;
        
    }
}
