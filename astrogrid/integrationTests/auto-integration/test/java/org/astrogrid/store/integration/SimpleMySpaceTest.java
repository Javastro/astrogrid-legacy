package org.astrogrid.store.integration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.util.ArrayList;
import java.util.Vector;
import junit.framework.Assert;
import junit.framework.TestCase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.community.User;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.mySpace.delegate.MySpaceClient;
import org.astrogrid.mySpace.delegate.MySpaceDelegateFactory;
import org.astrogrid.store.Agsl;
import org.astrogrid.store.delegate.StoreFile;
import org.astrogrid.store.delegate.myspaceItn05.MySpaceIt05Delegate;
import org.astrogrid.store.delegate.myspaceItn05.StatusCodes;
import org.astrogrid.store.delegate.myspaceItn05.StatusMessage;
/**
 * Junit tests for a deployed MySpace Manager.
 * 
 * @author A C Davenhall (Edinburgh)
 * @since Iteration 5.
 * @version Iteration 5.
 */
public class SimpleMySpaceTest extends TestCase {
    /**
     * Commons logger
     */
    private static final org.apache.commons.logging.Log log =
        org.apache.commons.logging.LogFactory.getLog(SimpleMySpaceTest.class);
    /**
     * Standard constructor for JUnit test classes.
     */
    public SimpleMySpaceTest(String name) {
        super(name);
    }
    //
    // Test class.
    //
    // This JUnit test has a single test class, which performs a sequence
    // of operations on the remote Manager.
    public void testDeployedManager() {/*
        try {
            //
            //      Create the contents which will subsequently be imported as a
            //      file. Note that the contents deliberately includes XML-like
            //      tags and new-line characters.
            StringBuffer contentsBuffer = new StringBuffer("<outer>\n");
            int rows = 2000;
            for (int loop = 0; loop < rows; loop++) {
                String thisRow = "  <tag>some content " + loop + "</tag>\n";
                contentsBuffer.append(thisRow);
            }
            contentsBuffer.append("</outer>\n");
            String contents = contentsBuffer.toString();
            //         System.out.println(contents);
            //
            //      Create a delegate to access the remote Manager.
            //
            final String endPoint =
                SimpleConfig.getSingleton().getString(
                    "org.astrogrid.myspace.endpoint");
            log.info("Running test against endpoint " + endPoint);
            User operator = new User("someuser", "group", "token");
            MySpaceIt05Delegate middle =
                new MySpaceIt05Delegate(operator, endPoint);
            //
            //      Configure the delegate to (i) not generate test responses and
            //      (ii) not to throw exceptions if errors are returned.
            middle.setTest(false);
            middle.setThrow(false);
            //
            //      Create an account.
            User testAccount = new User("testxyz", "", "", "");
            middle.createUser(testAccount);
            System.out.println("Account created...");
            //
            //      Import a file into this account.
            middle.putString(contents, "/testxyz/file1", false);
            System.out.println("File imported...");
            //
            //      Copy this file.
            Agsl someAgsl =
                new Agsl("http://blue.nowhere.org", "/testxyz/file2");
            middle.copy("/testxyz/file1", someAgsl);
            System.out.println("File copied...");
            //
            //      Move this file.
            someAgsl = new Agsl("http://blue.nowhere.org", "/testxyz/file3");
            middle.move("/testxyz/file1", someAgsl);
            System.out.println("File moved...");
            //
            //      Export the contents of the moved file.
            String retrievedContents = middle.getString("/testxyz/file2");
            Assert.assertEquals(contents, retrievedContents);
            System.out.println("File exported...");
            //
            //      Import and export the file as an array of bytes rather than
            //      a String.
            byte[] byteContents = contents.getBytes();
            int lengthByteContents = Array.getLength(byteContents);
            middle.putBytes(
                byteContents,
                0,
                lengthByteContents,
                "/testxyz/file4",
                false);
            System.out.println("File imported as bytes...");
            byte[] retrievedBytes = middle.getBytes("/testxyz/file4");
            int lengthRetrievedBytes = Array.getLength(retrievedBytes);
            Assert.assertEquals(lengthByteContents, lengthRetrievedBytes);
            String restoredContents = new String(retrievedBytes);
            Assert.assertEquals(contents, restoredContents);
            System.out.println("File exported as bytes...");
            //
            //      Import and export the file as an array of bytes using the
            //      streaming methods.
            OutputStream outStream = middle.putStream("/testxyz/file5", true);
            outStream.write(byteContents);
            outStream.close();
            System.out.println("File imported as bytes using streaming...");
            InputStream inStream = middle.getStream("/testxyz/file5");
            int current;
            int retrievedStreamSize = 0;
            StringBuffer buffer = new StringBuffer();
            byte[] byteBuffer = new byte[1];
            while ((current = inStream.read()) != -1) {
                byteBuffer[0] = (byte) current;
                String currentChar = new String(byteBuffer);
                buffer.append(currentChar);
                retrievedStreamSize = retrievedStreamSize + 1;
            }
            Assert.assertEquals(lengthByteContents, retrievedStreamSize);
            String streamedContents = buffer.toString();
            Assert.assertEquals(contents, streamedContents);
            System.out.println("File exported as bytes using streaming...");
            //
            //      Delete the files.
            middle.delete("/testxyz/file2");
            middle.delete("/testxyz/file3");
            middle.delete("/testxyz/file4");
            middle.delete("/testxyz/file5");
            System.out.println("Files deleted...");
            //
            //      Delete the test account.
            //
            //      Note that the account can only be deleted if the attempts to
            //      delete the files above succeeded. Thus the assertions here are
            //      also checking that the file deletions succeeded.
            middle.resetStatusList();
            middle.deleteUser(testAccount);
            System.out.println("Account deleted...");
            ArrayList statusList = middle.getStatusList();
            int numMessages = statusList.size();
            Assert.assertEquals(numMessages, 1);
            if (numMessages > 0) {
                StatusMessage message = (StatusMessage) statusList.get(0);
                int severity = message.getSeverity();
                Assert.assertEquals(message.getSeverity(), StatusCodes.INFO);
            }
        } catch (Exception e) {
            System.out.println("Exception thrown.");
            e.printStackTrace();
        }*/
    }
    /*
     * @param args ignored
     */
    public static void main(final String[] args) {
        junit.textui.TestRunner.run(SimpleMySpaceTest.class);
    }
    /**
     * Location of MySpace webservice property file
     */
    private String mySpaceEndPoint;
    /**
     * most tests will require a user
     */
    private String defaultUser ;
    /**
     * mosts tests will also require a community
     */
    private String defaultCommunity = "pentonville"; //@TODO - this doesn't seem to be used??
    /**
     * similarly a credential, though this is empty at the mo.
     */
    private String defaultCredential = "any";
    /**
     * default server
     */
    private String defaultServer = "serv1";
    private MySpaceIt05Delegate myspace;
    /**
     * Called before each test. Sets up a default user
     * 
     * @throws IOException on failure to load the config file, or a problem
     *             accessing the web service
     */
    public void setUp() throws IOException {
        log.info("\n\n\n\nSetting up...");
        final String endPoint =
            SimpleConfig.getSingleton().getString(
                "org.astrogrid.myspace.endpoint");
        log.info("Running test against endpoint " + endPoint);
        final User operator = new User("someuser", "group", "token");
        myspace = new MySpaceIt05Delegate(operator, endPoint);
        //
        //      Configure the delegate to (i) not generate test responses and
        //      (ii) to throw exceptions if errors are returned.
        myspace.setTest(false);
        myspace.setThrow(true);
        
        defaultUser = "NeilHamilton"+Long.toString(System.currentTimeMillis());
        createUser(defaultUser, defaultCommunity);
    }
    /**
     * Tidy up following test. Deletes the default user
     * 
     * @throws Exception on failure to load the config file, or a problem
     *             accessing the web service
     */
    public void tearDown() throws Exception {
        log.info("Tearing down...");
        deleteUser(defaultUser, defaultCommunity);
    }
    /**
     * Delete the user with the given params
     * 
     * @param userID userId
     * @param communityID communityID
     * @param endPoint end point of myspace server
     * @return returns false if there was an exception thrown by the mySpace
     */
    private void deleteUser(final String userID, final String communityID)
        throws IOException {
        // @TODO - change this, it's deprecated
        log.info("Deleting User "+userID);
        User testAccount = new User(userID, communityID, "", "");
        final StoreFile[] files = myspace.listFiles("/"+userID+"/*");
        if (files==null) {
            log.debug("Found no files");
        } else {
            log.info("Found "+files.length+" files");
            for (int i =0;i<files.length;++i) {
                StoreFile file = files[i];
                //if (file.isFolder()) {
                    log.info("found file " + files[i].getName());
                    myspace.delete(file.getName());
               // }
            }
        }
        
        myspace.deleteUser(testAccount);
        /*
         * // MySpaceWiper wiper; try { // wiper = new MySpaceWiper(endPoint); //
         * wiper.clear(userID, communityID); } catch (IOException e) {
         * log.error("Exception: ", e); return false; } catch
         * (MySpaceDelegateException e1) { log.error("Exception: ", e1); return
         * false;
         */
    }
    /**
     * create a user with the given params
     * 
     * @param userID userId
     * @param communityID communityId
     * @param endPoint myspace webservice end point
     * @throws IOException if the mySpace delegate chucks one
     * @return the value from the delegate method. Bad mix of exceptions and
     *         return codes.
     */
    private void createUser(final String userID, final String communityID)
        throws IOException {
        log.info("Attempting to createUser with the following params:");
        log.info("userID: " + userID);
        log.info("communityId: " + communityID);
        // @TODO - change this, it's deprecated
        User testAccount = new User(userID, communityID, "", "");
        myspace.createUser(testAccount);
        log.info("Account created...");
    }
    public void testUpAndRunning() throws IOException {
        assertTrue("Expect heartbeat true", myspace.heartBeat());
    }
    /**
     * Let's start with something trivial - can we save and return text?
     * 
     * @throws Exception no idea
     */
    public void testImportExportDeleteSimpleText() throws IOException  {
        for (int i = 0; i < 10; ++i) {
            final String name =
                "foo_"
                    + Integer.toString(i)
                    + "_"
                    + Long.toString(System.currentTimeMillis());
            final String string = "argle" + Integer.toString(i);
            importExportDelete(
                getFullPath(defaultUser, defaultCommunity, name),
                string);
        }
    }
    /**
     * Now a bit harder - can we save and return xml?
     * 
     * @throws Exception no idea
     */
    public void testImportExportDeleteXMLTextAgain() throws IOException  {
        final String name = "bar" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version=\"1.0\"?><title>Integration Tests</title>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Again, with single quotes
     * 
     * @throws Exception no idea
     */
    public void testImportExportDeleteXMLText() throws IOException {
        final String name = "foo" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version='1.0'?><properties><author email='jdt@roe.ac.uk'>John Taylor</author></properties>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Now lets really go mad - multiline text. Note this has just been done by
     * the addition of a few \n chars....maybe need to think about this a bit
     * more
     * 
     * @throws Exception nfi
     */
    public void testImportExportDeleteMultilineText() throws IOException {
        final String name = "foo" + Long.toString(System.currentTimeMillis());
        final String xml =
            "<?xml version=\"1.0\"?>\n<properties>\n<title>Integration Tests</title>\n"
                + "<author email=\"jdt@roe.ac.uk\">John Taylor</author>\n</properties>";
        importExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            xml);
    }
    /**
     * Construct the full path name of a myspace artifact
     * 
     * @param user user
     * @param community community
     * @param file filename
     * @return full path
     */
    private String getFullPath(
        final String user,
        final String community,
        final String file) {
        return "/" + user +  "/" + file;
    }
    /**
     * Utility method extracting the commonality of the saveDataHolding tests
     * 
     * @param name full path name to save into myspace
     * @param text the text to store
     * @throws Exception who knows
     */
    private void importExportDelete(final String name, final String text) throws IOException
         {

        myspace.putString(text, name, false);

        String result = myspace.getString(name);
        assertNotNull("Returned result from getDataHolding was null", result);
        log.info("Attempted to save '" + text + "' under name " + name);
        log.info("Received back '" + result + "'");
        final String testText = text + "\n"; //@TODO see bug 119
        assertEquals(
            "data returned from myspace not same as saved",
            result,
            testText);

        log.info("deleting...");
        myspace.delete(name);
    }
    /**
     * Utility method extracting the commonality of the saveDataHolding tests
     * 
     * @param name under which you wish to save the text
     * @param urlString the url of the data you wish to save
     * @throws Exception who knows
     */
    private void importURLExportDelete(
        final String name,
        final String urlString)
        throws Exception {
        URL url = new URL(urlString);
        myspace.putUrl(url, name, false);
        String result = myspace.getString(name);
        assertNotNull("Returned result from getDataHolding was null", result);
        InputStream is = url.openStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuffer textBuff = new StringBuffer();
        int ch;
        while ((ch = reader.read()) != -1) {
            textBuff.append((char) ch);
        }
        String text = textBuff.toString();
        log.info("Attempted to save from'" + url + "' under name " + name);
        log.info("Received back '" + result + "'");
        log.info("Expected: '" + text + "'");
        assertEquals(
            "data returned from myspace not same as saved",
            result,
            text);
        myspace.delete(name);
    }
    /**
     * test reading from url
     * 
     * @throws Exception no idea
     */
    public void testImportExportDeleteURL() throws Exception {
        String name = "foo" + Long.toString(System.currentTimeMillis());
        String url =
            "http://wiki.astrogrid.org/pub/Main/JohnTaylor/urlTestConfig.xml";
        importURLExportDelete(
            getFullPath(defaultUser, defaultCommunity, name),
            url);
    }
}
