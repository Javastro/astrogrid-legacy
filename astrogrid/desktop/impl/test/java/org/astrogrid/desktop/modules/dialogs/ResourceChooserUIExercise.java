/**
 * 
 */
package org.astrogrid.desktop.modules.dialogs;

import java.net.URI;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.astrogrid.acr.dialogs.ResourceChooser;
import org.astrogrid.desktop.ARTestSetup;
import org.astrogrid.desktop.InARTestCase;

/** bring up a file explorer dialogue for interactive testing.
 * uses the Junit fixtures and stuff for bringing up the AR, but doesn't do any assertions itself.
 * 
 * @author Noel.Winstanley@manchester.ac.uk
 * @since Oct 1, 200710:01:06 AM
 */
public class ResourceChooserUIExercise extends InARTestCase {
    private ResourceChooser chooser;

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.chooser = assertServiceExists(ResourceChooser.class,"dialogs.resourceChooser");
    }
    
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
        chooser = null;
    }
    
    public void testRunChooser() throws Exception {
        URI res = chooser.fullChooseResource("exercise this dialogue",true,true,true);
        System.out.println(res);
    }
    
    public void testRunChooserAgain() throws Exception {
        testRunChooser(); // verify it gets back to the starting position each time it's called.
        
    }
    
    public void testLocalFilesOnly() throws Exception {
        URI res = chooser.fullChooseResource("local files only",false,true,false);
        System.out.println(res);        
        
    }
    
    public void testRunFullChooserAgain() throws Exception {
        testRunChooser();        
    }

    public void testURLsOnly() throws Exception {
        URI res = chooser.fullChooseResource("urls only",false,false,true);
        System.out.println(res);        
        
    }
    
    public void testRunFullChooserAgainAgain() throws Exception {
        testRunChooser();        
    }    
    
   public void testChooseFolder() throws Exception {
       URI res = chooser.chooseFolder("choose a folder",true);
       System.out.println(res);
}

   public void testRunFullChooserYetAgain() throws Exception {
       testRunChooser();        
   }
   
    public static Test suite() {
        return new ARTestSetup(new TestSuite(ResourceChooserUIExercise.class));
    }
    
}
