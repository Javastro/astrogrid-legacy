/*
 * $Id 6dfDataCenter.java $
 *
 */

package org.astrogrid.mySpace.delegate;
import org.astrogrid.mySpace.delegate.*;

import java.io.*;

import java.net.URL;
import java.util.Arrays;
import java.util.Vector;
import org.astrogrid.mySpace.delegate.MySpaceManagerDelegate;
//import org.astrogrid.log.Log;

/**
 * This is a dummy myspace delegate that can be used to test applications
 * against myspace delegates but without actually needing access to a myspace
 * service. It basically stores files locally, in a directory
 * off the working one, with a name given by the endpoint,
 *
 * @author: M Hill
 */


public class MySpaceDummyDelegate implements MySpaceClient
{
   private File dir = null;
   String endPoint = null;

   public static final String DUMMY = "http://Dummy.address/"; //use this URL to ask for a dummy delegate

   /**
    * Class for filtering filenames based on the given criteria
    */
   private class CriteriaFilenameFilter implements FilenameFilter
   {
      String criteria = null;

      public CriteriaFilenameFilter(String givenCriteria)
      {
         this.criteria = givenCriteria;
      }

      public boolean accept(File dir, String name)
      {
         if (criteria.equals("*"))
         {
            return true;
         }
         else
         {
            throw new UnsupportedOperationException();
         }
      }
   }

   /**
    * Opens (creates if it doesn't exist) a subdirectory off the working one
    * given by the given end point
    */
   public MySpaceDummyDelegate(String givenEndPoint) {

      this.endPoint = givenEndPoint;

      //replace all ":"s and "/"s with "_"s,
      String dirName = givenEndPoint.replace(':', '_');
      dirName = dirName.replace('/', '_');
      
      //use createTemporary file instead - will always be able to create it somewhere.
      try {
        dir = File.createTempFile("MySpaceDummy" + dirName,"");

      // now make a directory here.
      dir.delete();
      dir.mkdir();
      /*
      dir = new File(dirName);

      if (!dir.exists())
      {
         dir.mkdir();
      }
    */
      //Log.affirm(dir.exists(), "Failed to make dir "+dirName);
      //Log.affirm(dir.isDirectory(), "Dir "+dirName+" is not a directory");
      } catch (IOException e) {
          throw new RuntimeException("Couldn't create dummy myspace ",e);
      }
   }

   /**
   * Returns a list of filenames that match the given criteria
   *
   * @param: userid: userid
   * @param: communityid
   * @param: criteria: eg./userid/communityid/workflows/A*
   * @return: Vector of String of fileNames
   */
   public Vector listDataHoldings(String userid, String communityid, String credentials, String criteria) {

      String[] matchingFilenames = dir.list(new CriteriaFilenameFilter(criteria));

      return new Vector(Arrays.asList(matchingFilenames));
   }

   /**
    * A generic method which returns a list of xml strings, each of which
    * holds the full file path as well as other info such as expired date etc.
    * @param userid
    * @param communityid
    * @param criteria
    * @return Vector of strings, each one an XML snippet
    */
   public Vector listDataHoldingsGen(String userid, String communityid, String credentials, String criteria)  {

      Vector matchingFilesXml = new Vector(); //list of xml strings

      File[] matchedFiles = dir.listFiles(new CriteriaFilenameFilter(criteria));

      //make up xml snippet for each one
      for (int f=0;f<matchedFiles.length;f++)
      {
         matchingFilesXml.add(getXmlSnippet(matchedFiles[f]));
      }

      return matchingFilesXml;
   }

   /**
    * Returns the full xml snippet for the given file
    */
   private String getXmlSnippet(File givenFile)
   {
      return "<filename>"+givenFile.getName()+"<filename>";
   }

   /**
    * Returns the path to access the given file for the given user, etc
    */
   private String getPath(String userid, String communityid)
   {
      return dir.getPath()+File.separator;
   }

   /**
    * Returns the full xml description of the given file
    * @param userid
    * @param communityid
    * @param serverFileName: full file name eg: /clq/serv1/File1.xml
    * @return
    */
   public String listDataHolding(String userid, String communityid, String credentials, String serverFileName)  {

      File file = new File(getPath(userid, communityid)+serverFileName);

      //Log.affirm(file.exists(), "File "+serverFileName+" not found");

      return getXmlSnippet(file);
   }

   /**
    * Copies one file to another on the same server
    *
    * @param userid
    * @param communityid
    * @param sourceFileName: full file name copy from
    * @param destFileName: full file name copy to
    * @return
    */
   public String copyDataHolding(String userid, String communityid, String credentials, String sourceFileName, String destFileName)
      throws IOException {

      sourceFileName = makeSafeFilename(sourceFileName);
      destFileName = makeSafeFilename(destFileName);

      File source = new File(getPath(userid, communityid)+sourceFileName);
      File dest = new File(getPath(userid, communityid)+destFileName);

      //Log.affirm(source.exists(), "File "+sourceFileName+" not found");

      InputStream in = new BufferedInputStream(new FileInputStream(source));
      OutputStream out = new BufferedOutputStream(new FileOutputStream(dest));

      byte[] block = new byte[100];
      int bytesRead = 0;

      while (in.available() > 0)
      {
         bytesRead = in.read(block);
         out.write(block, 0, bytesRead);
      }

      in.close();
      out.close();

      return "done";
   }

   /**
    * Renames a file on a server
    *
    * @todo broken at the moment - possibly because the rename shouldn't
    * have the path specified.
    *
    * @param userid
    * @param communityid
    * @param oldFileName: ole file full name
    * @param newFileName: new file full name
    * @return
    */

   public String renameDataHolding(String userid, String communityid, String credentials, String oldFileName, String newFileName)  throws IOException {

      oldFileName = makeSafeFilename(oldFileName);
      newFileName = makeSafeFilename(newFileName);

      
      File file = new File(getPath(userid, communityid)+oldFileName);
      File dest = new File(getPath(userid, communityid)+newFileName);

     // Log.affirm(file.exists(), "File "+oldFileName+" not found");
     // Log.affirm(!dest.exists(), "File "+newFileName+" exists! Cannot rename "+oldFileName);

      boolean success = file.renameTo(dest);

      if (!success)
      {
         throw new IOException("Failed to rename "+file+" to "+dest+" (don't know why)");
      }

      return "done";
   }

   /**
    * Deletes a file on the server
    *
    * @param userid
    * @param communityid
    * @param serverFileName: Full file name which you want to delete
    * @return
    * @throws Exception
    */
   public String deleteDataHolding(String userid, String communityid, String credentials, String serverFileName)  throws IOException {

      serverFileName = makeSafeFilename(serverFileName);

      File file = new File(getPath(userid, communityid)+serverFileName);

   //   Log.affirm(file.exists(), "File "+serverFileName+" not found");

      boolean success = file.delete();

      if (!success) {
         throw new IOException("Could not delete file '"+file+"' (don't know why)");
      }

      return "done";
   }

   /**
    * Saves the given fileContent string to the given filename on the server.
    * Used as a 'push' save
    *
   * @param: userid: userid
   * @param: communityid
   * @param: fileName: unique file name for Workflow or Query you want to store.
   * @param: fileContent: content of workflow or data query
   * @param: category "WF" or "QUERY", if not set, default is "VOTable"
   * @param: action "Overwrite" or "Append", if not set, default is "Overwrite"
   * @return: boolean true if file successfully stored in MySapce false otherwise.
   */

   public boolean saveDataHolding(String userid, String communityid, String credentials, String fileName, String fileContent,
                                  String category, String action) throws IOException
   {
      boolean append = false;

      if (action.equals(OVERWRITE)) {
         append = false;
      }
      else if (action.equals(APPEND)) {
         append = true;
      }
      else {
         throw new IllegalArgumentException("Illegal Action '"+action+"'");
      }

      //remove colons, spaces and slashes from filename
      fileName = makeSafeFilename(fileName);

      File dest = new File(getPath(userid, communityid)+fileName);
      OutputStream out = new FileOutputStream(dest, append);
      out.write(fileContent.getBytes());
      out.close();

      return true;
   }

   /**
    * Saves the file at the given URL to the server filespace
    * Used to 'pull' data
    * @param userid
    * @param communityid
    * @param fileName
    * @param importURI - url that save the dataholding from
    * @param category
    * @param action
    * @return
    * @throws Exception
    */

   public boolean saveDataHoldingURL(String userid, String communityid, String credentials, String fileName, String importURL,
                           String category, String action) throws  IOException {

      boolean append = false;

      if (action.toLowerCase().equals(OVERWRITE)) {
         append = false;
      }
      else if (action.toLowerCase().equals(APPEND)) {
         append = true;
      }
      else {
         throw new IllegalArgumentException("Illegal Action '"+action+"'");
      }

      fileName = makeSafeFilename(fileName);

      URL source = new URL(importURL);
      File dest = new File(getPath(userid, communityid)+fileName);

      InputStream in = new BufferedInputStream(source.openStream());
      OutputStream out = new BufferedOutputStream(new FileOutputStream(dest, append));

      byte[] block = new byte[100];
      int bytesRead = 0;

      while (in.available() > 0)
      {
         bytesRead = in.read(block);
         out.write(block, 0, bytesRead);
      }

      in.close();
      out.close();

      return true;
   }

   /**
    * URLs tend to have characters in them that don't work on file systems - so
    * change these characters to safer ones
    */
   protected String makeSafeFilename(String fileName)
   {
      //remove colons, spaces and slashes from filename
      fileName = fileName.replace(':','_');
      fileName = fileName.replaceAll("\\\\","_");
      fileName = fileName.replace(' ','_');
      fileName = fileName.replace('?','_');
      fileName = fileName.replace('&','_');
      fileName = fileName.replace('=','_');
      
      return fileName;
   }
   
   /**
    * Returns the URL of the given file name
    *
   * @param: userid: userid
    * @param: communityid: communityid
   * @param: fullFileName: full file name in mySpace for Workflow or Query you want to find
   * @return: URL of file
   */
   public String getDataHoldingUrl(String userid, String communityid, String credentials, String fullFileName) throws IOException {

      fullFileName = makeSafeFilename(fullFileName);
      
      File file = new File(getPath(userid, communityid)+fullFileName);

  //    Log.affirm(file.exists(), "File '"+fullFileName+"' not found");

      String fullpath = file.getCanonicalPath();

      return "file://"+fullpath;
   }

   /**
    * Returns an inputstream to the given file name
    *
   * @param: userid: userid
    * @param: communityid: communityid
   * @param: fullFileName: full file name in mySpace for Workflow or Query you want be downloaded.
   * @return: file content in String format
   */
   public InputStream getDataHoldingStream(String userid, String communityid, String credentials, String fullFileName) throws IOException {

      fullFileName = makeSafeFilename(fullFileName);

      URL url = new URL( getDataHoldingUrl(userid, communityid, credentials, fullFileName));

      return url.openStream();
   }

   /**
    * Returns the contents of the given file namen as a string.  Not sure about
    * the encoding methods used to convert between bytes and chars.
    *
   * @param: userid: userid
    * @param: communityid: communityid
   * @param: fullFileName: full file name in mySpace for Workflow or Query you want be downloaded.
   * @return: file content in String format
   */
   public String getDataHoldingContents(String userid, String communityid, String credentials, String fullFileName) throws IOException {

      fullFileName = makeSafeFilename(fullFileName);
      
      InputStream in = new BufferedInputStream(getDataHoldingStream(userid, communityid, credentials, fullFileName));
      ByteArrayOutputStream out = new ByteArrayOutputStream();

      byte[] block = new byte[100];
      int bytesRead = 0;

      while (in.available() > 0)
      {
         bytesRead = in.read(block);
         out.write(block, 0, bytesRead);
      }

      in.close();
      out.close();

      return out.toString();
   }

   /**
    * Release is permanent on dummy.  Does nothing
    */
   public String extendLease(String userid, String communityid, String credentials, String serverFileName, int extentionPeriod)
   {
      return "done";
   }



   /**
    * Create a new user on the current MSS.
    *
    * @param userId User identifier.
    * @param servers Vector of server names on which containers will be
    *   created for the user.
    *
    * @return boolean; true the user was created successfully.
    * @throws Exception
    */
   public boolean createUser(String userId, String communityId, String credential, Vector servers) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Retrieve a copy of a dataHolder on a remote MSS and save it with a
    * specified MySpace name on the current MSS.
    *
    * @param userId User identifier.
    * @param communityId Community identifier.
    * @param remoteMssUrl URL of the remote MSS.
    * @param remoteMySpaceName MySpace name of the dataHolder to be
    *   retrieved from the remote MSS.
    * @param newMySpaceName MySpace name for the copy of the dataHolder on
    *   the current MSS.
    *
    * @return boolean; true if the copy succeeded.
    * @throws Exception
    */
   public boolean copyRemoteDataHolding(String userId, String communityId, String credential, String remoteMssUrl, String remoteMySpaceName, String newMySpaceName) throws Exception {
      // TODO
      return false;
   }
   
   /**
    * Retrieve the contents of a dataHolder and supply them as the String
    * returned by the method.
    *
    * @param userId User identifier.
    * @param communityId community identifier.
    * @param  mySpaceName MySpace name of the dataHolder whose contents are
    *   to be retrieved.
    *
    * @return: A String containing the contents of the specified dataHolder.
    */
   public String getDataHolding(String userId, String communityId, String credential, String mySpaceName) throws Exception {
      // TODO
      return null;
   }
   
   /**
    * Delete a user from the current MSS.
    *
    * @param userId User identifier.
    *
    * @return boolean; true is the user was deleted successfully.
    * @throws Exception
    */
   public boolean deleteUser(String userId, String communityId, String credential) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param dataHolderName: file working on
    * @param newOwnerID: userId changing to
    * @return
    * @throws Exception
    */
   public String changeOwner(String userId, String communityId, String credential, String dataHolderName, String newOwnerID) throws Exception {
      throw new UnsupportedOperationException();
   }
   
   /**
    *
    * @param userId
    * @param communityId
    * @param newContainerName
    * @return
    */
   public String createContainer(String userId, String communityId, String credential, String newContainerName) throws Exception {
      // TODO
      return null;
   }
   
   /**
    *
    * @param jobDetails: use mySpace/configFiles/MSManagerRequestTemplate.xml to create an xml String by filling in userId/communityId/jobID/serverFileName
    * @return
    * @throws Exception
    */
   public String publish(String userId, String communityId, String credential, String mySpaceName) throws Exception {
      // TODO
      return null;
   }
   
}
