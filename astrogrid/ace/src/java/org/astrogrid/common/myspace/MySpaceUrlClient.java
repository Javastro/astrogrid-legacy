/*
   .java

   Date         Author      Changes
   11 Nov 2002  M Hill      Created

   (c) Copyright...
*/

package org.astrogrid.common.myspace;

import java.io.*;
import java.net.*;

import org.astrogrid.log.Log;
import org.astrogrid.tools.util.TimeStamp;

/**
 * An update-only version of myspace client, for http or anonymous ftp
 * or perhaps gopher access to files
 */

public class MySpaceUrlClient implements MySpaceClient
{

   /**
    * Connect to server, get authorised and be ready to move files
    */
   public void connect() throws IOException
   {
      //nothing to do for http
   }


   /**
    * Disconnects from server, translating all faults into IOExceptions
    */
   public void disconnect() throws IOException
   {
      //nothing to do for http
   }

   /**
    * Cannot publicise to http/anon ftp; all the same, we do nothing here
    * on the basis that the file is already public
    */
   public String publicise(String uniqueId, String filename) throws IOException
   {
      //throw new UnsupportedOperationException("Cannot publicise to url (http/anon ftp) myspaces");
      return filename;
   }


   /**
    * Gets the file identified by the remote string (this might be a url)
    * and copy it to the local filename.  If possible, it should only do this
    * if required - ie if poss it should do some sort of time/date or
    * checksum comparison. Retruns full path to local file
    */
   public String update(String remoteUrl, String localDir) throws IOException
   {
      URL url = new URL(remoteUrl);

      Log.affirm(url.getFile() != null, "No file given in location '"+remoteUrl+"'");

      //get name of remote file
      String localFilename = url.getFile(); //includes path

      //create name for local copy, based on remote
      int fni = localFilename.lastIndexOf('/');
      localFilename = localFilename.substring(fni+1);

      //remove any query characters that might interfere with the local filename
      //localFilename = localFilename.replaceAll("[=&!\\?\\+]",""); //java 1.4
      localFilename = replaceAll(localFilename,"[","");  //java 1.3
      localFilename = replaceAll(localFilename,"]","");  //java 1.3
      localFilename = replaceAll(localFilename,"=","");  //java 1.3
      localFilename = replaceAll(localFilename,"&","");  //java 1.3
      localFilename = replaceAll(localFilename,"!","");  //java 1.3
      localFilename = replaceAll(localFilename,"\\","");  //java 1.3
      localFilename = replaceAll(localFilename,"?","");  //java 1.3
      localFilename = replaceAll(localFilename,"/","");  //java 1.3
      localFilename = replaceAll(localFilename,"+","");  //java 1.3

      localFilename = localDir+localFilename;
      File localFile = new File(localFilename);

      //get characteristics of remote file
      URLConnection connection = url.openConnection();
      int remoteFileSize = connection.getContentLength();
      long remoteLastMod = connection.getLastModified();
      Log.trace("(MySpaceUrlClient) Examining '"+remoteUrl+"'...");
      Log.trace("...(size = "+remoteFileSize+", lastmod="+remoteLastMod+")...");

      //check against local file - for now, timestamp will do.  0 means remote timestamp not known
      if ((remoteLastMod != 0) && (remoteLastMod == localFile.lastModified()))
      {
         Log.trace("...Local file has same timestamp as remote; not updating");
         return localFilename;
      }

      Log.trace("Copying '"+remoteUrl+"' to '"+localFile+"'...");
      TimeStamp startTime = new TimeStamp();

      InputStream in = new BufferedInputStream(connection.getInputStream());
      OutputStream local = new BufferedOutputStream(new FileOutputStream(localFile));
      int bytesRead=0;
      int copied = 0;
      byte[] block = new byte[1024];

      //update user at time intervals
      long lastUpdateTime = startTime.getSecsSince();
      while ( (bytesRead = in.read(block)) != -1)
      {
         local.write(block,0,bytesRead);
         copied = copied+bytesRead;
//         if ( (copied % 1024) == 0) // ooo optimisation - don't load time every byte
         {
            if ( startTime.getSecsSince() > (lastUpdateTime+4))   // do every 5 secs
            {
               Log.trace("...(copied "+copied+"b of "+remoteFileSize+" ["+(long) copied*100/remoteFileSize+"%] in "+startTime.getSecsSince()+"s)...");
               lastUpdateTime = startTime.getSecsSince();
            }
         }
      }
      in.close();
      local.close();
      if (remoteLastMod !=0)
      {
         localFile.setLastModified(remoteLastMod); //set timestamp to remote one
      }
      Log.trace("...'"+localFilename+"' ("+copied+" bytes) copied in "+startTime.getSecsSince()+"s");

      return localFilename;
   }

   /**
    * Java 1.3 does not have a String.replaceAll() method, so we have to do
    * it by hand
    */
   public static String replaceAll(String s, String find, String replace)
   {
      int pos =0;
      while ((pos = s.indexOf(find,pos+1)) != -1)
      {
         s = s.substring(0,pos-1)+replace+s.substring(pos+1);
      }
      return s;
   }

   
   /**
    * Test harness to update from a given URL.  (Specify the url on the command
    * line)
    */
    public static void main(String[] args)
    {
      String imageUrl = "http://www.ast.cam.ac.uk/~rgm/mirrors/archive.stsci.edu/pub/hlsp/goods/h_s1b02sciv05_img.fits";
      if (args.length>0)
      {
         imageUrl = args[0];
      }


      try
      {
         MySpaceUrlClient myspace = new MySpaceUrlClient();
         myspace.connect();
         String localFilename = myspace.update(imageUrl, "");
         myspace.disconnect();
      }
      catch (IOException e)
      {
         org.astrogrid.log.Log.logError("Could not update '"+imageUrl+"'",e);
      }
   }
}
