/*
 * $Id: StatusLogger.java,v 1.1 2009/05/13 13:20:27 gtr Exp $
 */

package org.astrogrid.dataservice.queriers.status;

import java.io.*;

import java.net.URL;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.Vector;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.io.account.LoginAccount;
import org.astrogrid.cfg.PropertyNotFoundException;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.service.ServletHelper;
import org.astrogrid.io.Piper;
import org.astrogrid.status.DefaultTaskStatus;
import org.astrogrid.status.TaskStatus;

/**
 * Used to log status information to disk so we can look at it later. It basically
 * records the output from the server
 */


public class StatusLogger {
   
   public final static String STATUS_PAGE_DIRECTORY_KEY = "datacenter.status.page.directory";
   
   File statusPageDirectory = null;
   File statusLog = null;

   TaskStatus[] persistedCache = null;
   
   Log log = LogFactory.getLog(StatusLogger.class);
   
   public StatusLogger() {
      statusPageDirectory = new File(ConfigFactory.getCommonConfig().getString(STATUS_PAGE_DIRECTORY_KEY, "/tmp/"));

      String authID = ConfigFactory.getCommonConfig().getString(
         "datacenter.authorityId", "TestAuthority");
      String resKey = ConfigFactory.getCommonConfig().getString(
         "datacenter.resourceKey", "TestResource");

      authID = authID.replace('/','_');
      resKey = resKey.replace('/','_');
      // Just in case below - shouldn't have spaces
      authID = authID.replace(' ','_');
      resKey = resKey.replace(' ','_');

      statusLog = new File(statusPageDirectory, authID+"_"+resKey+"_status.log");
   }
   
   /** Stores copy of status page on disk, and adds to summary list file */
   public void log(TaskStatus status) {

      /** Store summary details in log */
      try {
         Writer w = new FileWriter(statusLog, true); //append
         
         //get time taken
//         Date timeTaken = new Date(status.getTimestamp().getTime() - status.getFirst().getStartTime().getTime());
         
         w.write(status.getId()+", "+status.getStage().toString()+", "+status.getFirst().getTimestamp().getTime()+", "+status.getTimestamp().getTime()+", "+status.getOwner()+", "+status.getSource()+", "+status.getProgressMax());
         w.write("\n");
         w.close();
      }
      catch (IOException e) {
         log.error("Writing query '"+status.getId()+"' status to "+statusPageDirectory,e);
      }

      /** Store status page if it can be reached */
      // Removed by KEA - don't think these logs are very useful, and they
      // were timing out very slowly in unit tests (when no page is 
      // present to open), in some contexts. 
      /*
      String statusUrl = null;
      try {
         statusUrl = ServletHelper.getUrlStem()+"admin/queryStatus.jsp?ID="+status.getId();
   
         log.debug("Storing status page "+statusUrl);
         
         InputStream in = new URL(statusUrl).openStream();
         OutputStream out = new FileOutputStream(new File(statusPageDirectory, "QueryStatus_"+status.getId()+".html"));
         Piper.bufferedPipe(in, out);
         in.close();
         out.close();
      }
      catch (IOException ioe) {
         //if we can't reach the pages, assume a unit test or similar
         log.warn(ioe+", copying query "+status.getId()+" status from '"+statusUrl+"' to "+statusPageDirectory+" -> could not persist status page");
      }
      catch (PropertyNotFoundException pnfe) {
         //couldn't find stem, never mind
         log.warn(pnfe+", copying query "+status.getId()+" status from '"+statusUrl+"' to "+statusPageDirectory+" -> could not persist status page");
      }
      */
   }
   
   /** Returns TaskStatus representation of past queries from persisted list
      * Only need to load once per application run, as any that get written
       * after loading will be available in the 'live details' list */
   public synchronized TaskStatus[] getPersistedLog() throws IOException {
      
      if (persistedCache == null) {
         
         Vector statuses = new Vector();
         
         LineNumberReader r = new LineNumberReader(new FileReader(statusLog));
               
         String line = r.readLine();
         int lineNum = 1;
         while (line != null) {
            try {
               StringTokenizer splitter = new StringTokenizer(line, ",");
               DefaultTaskStatus status = new DefaultTaskStatus();
               status.setId(splitter.nextToken().trim());
               status.setStage(splitter.nextToken().trim());
               String startTime = splitter.nextToken().trim();
               status.setTimestamp(new Date(Long.parseLong(splitter.nextToken().trim())));
               status.setOwner(new LoginAccount(splitter.nextToken().trim(), null));
               status.setSource(splitter.nextToken().trim());
               status.setProgressMax(Long.parseLong(splitter.nextToken().trim()));
               
               //set start time by using a previous
               DefaultTaskStatus prev = new DefaultTaskStatus(status, status.INITIALISED);
               prev.setTimestamp(new Date(Long.parseLong(startTime)));
               prev.setPrevious(null); //constructor assumes status is previous
               status.setPrevious(prev);
               
               statuses.add(status);
            }
            catch (NumberFormatException nfe) {
               //format has changed perhaps.  Log & continue
               log.debug(nfe+", in line "+lineNum+" of "+statusLog+"; ignoring line");
            }
            lineNum++;
            line = r.readLine();
         }
         r.close();
         persistedCache =  (TaskStatus[]) statuses.toArray(new TaskStatus[] {} );
      }
      return persistedCache;
   }

   /** Removes the status log file */
   public void clearStatusLog() {
      statusLog.delete();
   }
   
   /** Returns the status (if it exists) as loaded from the log file */
   public TaskStatus getPersistedStatus(String id) throws IOException {
      TaskStatus[] persisted = getPersistedLog(); //make sure it's loaded
      for (int i = 0; i < persisted.length; i++)
      {
         if (persisted[i].getId().equals(id)) {
            return persisted[i];
         }
      }
      return null; //not found
   }
   
   /** Returns the page (if it exists) */
   public String getPersistedStatusPage(String id) throws IOException {
      try {
         Reader in = new FileReader(new File(statusPageDirectory, "QueryStatus_"+id+".html"));
         StringWriter out = new StringWriter();
         Piper.bufferedPipe(in, out);
         return out.toString();
      }
      catch (FileNotFoundException fnfe) {
         log.warn(fnfe+" getting persisted status page for id "+id);
         //never mind, it's just not been stored for some reason
         return null;
      }
   }

   /** For quick debug/tests */
   public static void main(String[] args) throws IOException
   {
      TaskStatus[] persisted = new StatusLogger().getPersistedLog();
      for (int i = 0; i < persisted.length; i++)
      {
         System.out.println(persisted[i]);
      }
      System.out.println("---------HTML:");
      
//      new ServerStatusHtmlRenderer().writeHtmlStatus(new OutputStreamWriter(System.out), new DataServer().getStatus().getServiceStatus(), persisted);
   }
}

