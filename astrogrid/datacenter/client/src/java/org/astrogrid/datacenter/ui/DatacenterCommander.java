/*$Id: DatacenterCommander.java,v 1.11 2004/10/12 23:09:53 mch Exp $
 * Created on 24-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.ui;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.rmi.MarshalException;
import javax.xml.rpc.ServiceException;
import org.astrogrid.community.Account;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.QuerySearcher;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.SqlQueryMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.io.Piper;

/** Simple tool to fire a query at a server, and print out the response.
 * Currently accepts queries in ADQL and SQL
 * <h2>Examples of use</h2>
 * For usage instructions, run with no arguments:
 * <pre>
 * java org.astrogrid.datacenter.tools.SimpleQuerier
 * </pre>
 * Submit an SQL query
 * <pre>
 * java org.astrogrid.datacenter.tools.SimpleQuerier http://localhost:8080/pal/services/AxisDataServer --sql "Select * from merlinCatalog where DataNo = 657038"
 * </pre>
 * Submit an ADQL query, stored in the file <tt>adql-query.xml</tt>
 * <pre>
 * java org.astrogrid.datacenter.tools.SimpleQuerier http://localhost:8080/pal/services/AxisDataServer adql-query.xml
 * </pre>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Nov-2003
 *
 */
public class DatacenterCommander {
   
   /** Main method interprets inputs */
   public final static void main(String[] args) throws Exception {
      try {
         
         if (args.length<2) {
            usage();
         }
         else {
            String command = args[0].toLowerCase().trim();
            String endpoint = args[1];
            
            if (command.equals("adql")) {
               doAdql(endpoint, args);
            }
            else if (command.equals("sql")) {
               if (args.length<3) {
                  System.out.println("No SQL given");
                  usage();
               }
               else {
                  doSql(endpoint, args[2]);
               }
            }
            else if (command.equals("cone")) {
               doCone(endpoint);
            }
            else {
               System.out.println("Unknown command: "+command);
               usage();
            }
         }
      } catch (Exception e) {
         System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
         usage();
      }
   }
   
   public DatacenterCommander() {
   }
   
   
   public static void usage() {
      System.out.println("Usage: java "+DatacenterCommander.class+" <command> <service URL> [ parameters ]");
      System.out.println();
      System.out.println("commands:");
      System.out.println("   sql: submit string given in parameters as SQL ");
      System.out.println("   adql: submit query at url given in parameter ");
      System.out.println("   cone: do cone search; give serviceurl as base?RA=20;DEC=30;SR=4 as normal NVO");
   }
   
   public static void doAdql(String endpoint, String[] args) throws ServiceException, IOException {
      
      if (args.length<3) {
         System.out.println("No ADQL file given");
         usage();
         return;
      }
      File queryFile = new File(args[2]);
      if (queryFile == null) {
         System.out.println("Cannot find ADQL query file "+args[2]);
         usage();
         return;
      }
      System.out.println("Connecting to server...");
      QuerySearcher del = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS, endpoint.toString(), DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      System.out.println("Reading query...");
      System.out.println("...FAIL: need to update this bit of code");
      /*
      Select select = Select.unmarshalSelect(new InputStreamReader ( new FileInputStream(queryFile)));
      Element queryBody = ADQLUtils.toQueryBody(select);
      System.out.println("Asking query...");
      InputStream results = del.askQuery(new AdqlQuery(queryBody), QuerySearcher.VOTABLE);
      System.out.println("Results:");
      Piper.bufferedPipe(results, System.out);
       */
   }
   
   
   public static void doCone(String endpoint) throws MalformedURLException, IOException {
      URL url = new URL(endpoint);
      
      System.out.println("Asking query...");
      InputStream in = url.openStream();
      OutputStream out = System.out;
      
      System.out.println("Results:");
      Piper.bufferedPipe(in, out);
   }
   
            

   
   public static void doSql(String endpoint, String sql) throws ServiceException, IOException {
      
      System.out.println("Connecting to server...");
      QuerySearcher del = DatacenterDelegateFactory.makeQuerySearcher(Account.ANONYMOUS, endpoint.toString(), DatacenterDelegateFactory.ASTROGRID_WEB_SERVICE);
      System.out.println("Asking query...");
      Query query = SqlQueryMaker.makeQuery(sql);
      query.setResultsDef(new ReturnTable(null, QuerySearcher.VOTABLE));
      InputStream results = del.askQuery(query);
      System.out.println("Results:");
      Piper.bufferedPipe(results, System.out);
   }
   
}


/*
 $Log: DatacenterCommander.java,v $
 Revision 1.11  2004/10/12 23:09:53  mch
 Lots of changes to querying to get proxy querying to SSA, and registry stuff

 Revision 1.10  2004/10/08 15:17:54  mch
 Some updates to try and reach SSA/etc at ROE

 Revision 1.9  2004/10/06 21:12:17  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.8  2004/09/28 15:49:55  mch
 Removed calls to deprecated methods

 Revision 1.7  2004/09/28 14:59:35  mch
 Removed obsolete ADQL 0.5 object model

 Revision 1.6  2004/03/13 16:26:25  mch
 Changed makeFullSearcher to makeQuerySearcher

 Revision 1.5  2004/03/13 16:19:38  mch
 Tidying up after refactor

 Revision 1.4  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.3  2004/03/07 21:13:52  mch
 Changed apache XMLUtils to implementation-independent DomHelper

 Revision 1.2  2004/03/06 19:34:21  mch
 Merged in mostly support code (eg web query form) changes

 Revision 1.1  2004/03/03 10:08:01  mch
 Moved UI and some IO stuff into client

 Revision 1.5  2004/01/14 12:55:28  nw
 improved documentation

 Revision 1.4  2004/01/14 12:49:49  nw
 improved documentation

 Revision 1.3  2004/01/13 00:32:47  nw
 Merged in branch providing
 * sql pass-through
 * replace Certification by User
 * Rename _query as Query

 Revision 1.2.10.3  2004/01/12 17:04:50  nw
 fixed bug with argument parsing

 Revision 1.2.10.2  2004/01/08 09:42:26  nw
 tidied imports

 Revision 1.2.10.1  2004/01/08 09:10:20  nw
 replaced adql front end with a generalized front end that accepts
 a range of query languages (pass-thru sql at the moment)

 Revision 1.2  2003/11/26 16:31:46  nw
 altered transport to accept any query format.
 moved back to axis from castor

 Revision 1.1  2003/11/24 21:06:01  nw
 added little command-line tool to query datacenter
 
 */
