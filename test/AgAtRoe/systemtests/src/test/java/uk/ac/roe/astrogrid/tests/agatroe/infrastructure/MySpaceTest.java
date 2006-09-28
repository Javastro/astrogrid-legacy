package uk.ac.roe.astrogrid.tests.agatroe.infrastructure;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

import org.astrogrid.acr.ACRException;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotApplicableException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.astrogrid.Community;
import org.astrogrid.acr.astrogrid.Myspace;

import uk.ac.roe.astrogrid.tests.RuntimeRequiringTestCase;
import uk.ac.roe.astrogrid.tests.agatroe.TestUser;
/**
 * Is MySpace functioning correctly?
 * Many of the tests duplicate each other, indeed, much of the MySpace functionality it tested in
 * setting up and tearing down the fixtures.  Nevertheless, they've been left as separate tests
 * to make it easier to pinpoint any problems.
 * @author jdt
 *
 */
public class MySpaceTest extends RuntimeRequiringTestCase {
    private Community comm;
    private Myspace myspace;
    private final URI testFolder = URI.create("#testfolder");
    private final URI testFile = URI.create("#testfolder/testfile");
    private final URI testFile2 = URI.create("#testfolder/testfile2");
    private final String testTxt = "fee fii fo fum";
    private URI homespace;
    public void setUp() throws InvalidArgumentException, NotFoundException, ACRException, Exception {
        comm = (Community) getAcr().getService(Community.class);
        myspace = (Myspace) getAcr().getService(Myspace.class);
        login();
        homespace = myspace.getHome();
        getLog().debug("HomeSpace is "+homespace);
       
        eraseTestFiles();
    }
    
    /**
     * Can get home directory
     *
     */
    public void testGetHome() throws Exception {
        URI home = myspace.getHome();
        getLog().info("Home directory is "+home);
        URI expectedHome = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+"#");
        assertEquals(expectedHome, home);
    }
    
    public void testCreateFolder() throws ServiceException, SecurityException, InvalidArgumentException, NotFoundException, URISyntaxException {
        myspace.createFolder(testFolder);
        URI[] children = myspace.listIvorns(homespace);
        URI folder_full = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+testFolder);
        assertTrue(Arrays.asList(children).contains(folder_full));
        
    }
    
    public void testCreateFile() throws ServiceException, SecurityException, InvalidArgumentException, NotFoundException, NotApplicableException, URISyntaxException {
        testCreateFolder();
        myspace.createFile(testFile);
        URI[] children = myspace.listIvorns(testFolder);
        URI file_full = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+testFile);
        assertTrue(Arrays.asList(children).contains(file_full));
        myspace.write(testFile, testTxt);
        String readback = myspace.read(testFile);
        assertEquals(testTxt, readback);
    }
    
    public void testDeleteFile() throws ServiceException, SecurityException, InvalidArgumentException, NotFoundException, NotApplicableException, URISyntaxException {
        testCreateFile();
        myspace.delete(testFile);
        URI[] children = myspace.listIvorns(testFolder);
        URI file_full = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+testFile);
        assertFalse(Arrays.asList(children).contains(file_full));
    }
    
    public void testRenameFile() throws ServiceException, SecurityException, InvalidArgumentException, NotFoundException, NotApplicableException, URISyntaxException {
        testCreateFile();
        myspace.rename(testFile, "testfile2");
        URI[] children = myspace.listIvorns(testFolder);
        URI file_full = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+testFile);
        URI file_full2 = new URI("ivo://"+TestUser.COMMUNITY+"/"+TestUser.USER+testFile2);
        assertFalse(Arrays.asList(children).contains(file_full));
        assertTrue(Arrays.asList(children).contains(file_full2));
    }
    
    private void eraseTestFiles() throws SecurityException, ServiceException,  InvalidArgumentException, NotFoundException{
        if (!myspace.exists(testFolder)) return;
        URI[] children = myspace.listIvorns(testFolder);
        
        for (int i =0; i<children.length;++i) {
            final URI file = children[i];
            if (myspace.exists(file)) {
                getLog().info("Deleting file "+file);
                try {
                    myspace.delete(file);
                } catch (NotFoundException e) {
                    fail("That's odd.  MySpace said the file "+file+" existed, now it won't let me delete it.");
                } 
            }
        }
        try {
            myspace.delete(testFolder);
        } catch (NotFoundException e) {
            fail("That's odd.  MySpace said the file "+testFolder+" existed, now it won't let me delete it.");
        } 
    }
    public void tearDown() throws Exception {
        eraseTestFiles();
        comm.logout();
    }
    public void login() throws SecurityException, ServiceException {
        comm.login(TestUser.USER, TestUser.PASS, TestUser.COMMUNITY);
    }
    
    
    
    

}
