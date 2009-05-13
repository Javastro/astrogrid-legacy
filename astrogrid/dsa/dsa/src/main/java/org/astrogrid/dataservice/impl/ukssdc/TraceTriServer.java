/*
 * $Id: TraceTriServer.java,v 1.1 2009/05/13 13:20:23 gtr Exp $
 */

package org.astrogrid.dataservice.impl.ukssdc;

import java.io.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.dataservice.queriers.DatabaseAccessException;
import org.astrogrid.io.Piper;
import org.astrogrid.tableserver.jdbc.JdbcConnections;
import org.astrogrid.tableserver.jdbc.SqlResults;
import org.astrogrid.webapp.DefaultServlet;

/**
 * Serves the contents of 'TRI' combined files given a tape ID and file ID.
 *
 * The 'tri' files are collections of FITS files in some special-to-SolarSoft form,
 * that are kept on tapes served by a tape juke box
 *
 * @author mch
 */
public class TraceTriServer extends DefaultServlet {
 
   JdbcConnections connectionManager = null;
   
   public static final String HD_CACHE_KEY = "trace.filecache";

   protected static final Log log = org.apache.commons.logging.LogFactory.getLog(SqlResults.class);
   
   public void doGet(HttpServletRequest request, HttpServletResponse response)  throws IOException {
      
//      String tapeId = request.getParameter("tape");
      String fileId = request.getParameter("trifile");

      File file = getFileFromTape(fileId);
      
      InputStream in = new FileInputStream(file);
      Piper.bufferedPipe(in, response.getOutputStream());

   }
   
   public File getFileFromTape(String fileId) throws IOException {
      
      //look up which tape to access
      String[] resp = getTapeDetails(fileId);
      String tapeId = resp[0];
      String fileNum = resp[1];
      String date = resp[2];
      
      File dir = new File( ConfigFactory.getCommonConfig().getString(HD_CACHE_KEY, "/data/ukssdc/TRACE/")+
                             date.substring(0,4)+"/"+
                             date.substring(5,6)+"/"+
                             date.substring(7,8)+"/"
                         );
      
      //check and make if they don't exist
      if (!dir.exists()) {
         dir.mkdirs();
      }
      File unloadedFile = new File(dir, fileId);

      log.trace("Unloading file to "+unloadedFile);

      String command = "tape -r -m "+fileNum+" -f "+unloadedFile+" SG "+tapeId;

      log.trace("Command: "+command);
      
      Process proc = Runtime.getRuntime().exec(command);

      if (!unloadedFile.exists()) {
         
         InputStream in = proc.getInputStream();
         StringWriter sw = new StringWriter();
         Piper.pipe(new InputStreamReader(in), sw);
         throw new IOException("Failed to unload file "+fileId+" to "+unloadedFile+" from tape "+tapeId+" ("+fileNum+"): "+sw.toString());
      }
      
      return unloadedFile;
   }


   /** Returns the tape number and file number on that tape for the given
    * tri file name by querying the database table trace_cat,
    */
   public String[] getTapeDetails(String triFile) throws IOException {

      String sql = "SELECT trace_cat.tape_id, trace_cat.file_number, trace_cat.datetime FROM trace_cat WHERE trace_cat.filename = '"+triFile+"'";
      log.debug("TraceTriServer: "+sql);
      
      Connection jdbcConnection = null;
      
      try {
      
         //connect to database
        if (connectionManager == null) {
            connectionManager = JdbcConnections.makeFromConfig();
         }
         jdbcConnection = connectionManager.createConnection();
         Statement statement = jdbcConnection.createStatement();

         //execute query
         statement.execute(sql);

         ResultSet results = statement.getResultSet();

         if (!results.next()) {
            throw new DatabaseAccessException("No match found in database for TRI file "+triFile);
         }

         String tapeId = results.getString(1); //first column
         String fileNum = results.getString(2); //second column
         String datetime = results.getString(3);

         log.debug("TraceTriServer: returned "+tapeId+", "+fileNum+", "+datetime);

         if (results.next()) {
            throw new DatabaseAccessException("More than one match found in database for TRI file "+triFile);
         }
         
         return new String[] { tapeId, fileNum, datetime };
      }
      catch (SQLException e) {
         //we don't really need to store stack info for the SQL exception, which saves logging...
         throw new DatabaseAccessException(e+" using '" + sql + "': ",e);
      }
      finally {
         //try to tidy up now
         try {
            if (jdbcConnection != null) { jdbcConnection.close(); }
         } catch (SQLException e) { } //ignore
      }

   }


}

