/*$Id: SimpleQuerier.java,v 1.4 2004/01/14 12:49:49 nw Exp $
 * Created on 24-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.astrogrid.datacenter.delegate.FullSearcher;
import org.astrogrid.datacenter.sql.SQLUtils;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;
import org.w3c.dom.Element;

/** Simple tool to fire a query at a server, and print out the response.
 * Currently accepts queries in ADQL and SQL
 * For usage instructions, run with no arguments:
 * <pre>
 * java org.astrogrid.datacenter.tools.SimpleQuerier
 * </pre>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Nov-2003
 *
 */
public class SimpleQuerier {

    public final static void main(String[] args) throws Exception {
       SimpleQuerier q = new SimpleQuerier();
       try {
          q.parse(args);
          q.doIt();
       } catch (Exception e) {
          System.out.println("Error: " + e.getClass().getName() + " - " + e.getMessage());
          q.usage();
       }        
    }

    public SimpleQuerier() {
    }
    
    public void parse(String[] args) throws IllegalArgumentException, MalformedURLException{
        if (args.length < 2 || args.length > 3) {
            throw new IllegalArgumentException("Incorrect number of arguments");
        }
        endpoint = new URL(args[0]);
        if (args[1].toLowerCase().startsWith("--sql")) {
           if (args.length != 3) {
              throw new IllegalArgumentException("Must provide a sql expression");
           }
           sql = args[2].trim();
           if (sql == null || sql.length() == 0) {
              throw new IllegalArgumentException("Must provide a sql expression");
           }           
        } else {
         queryFile = new File(args[1]);
         if (!queryFile.exists()) {
            throw new IllegalArgumentException("Query file does not exist: " + queryFile.getAbsolutePath());
         }
        }                
    }
    
    protected URL endpoint;
    protected File queryFile;
    protected String sql;
   
   public void usage() {
      System.out.println("Usage: java org.astrogrid.datacenter.tools.SimpleQuerier <service URL> [ <query file> | --sql \"<sql expression>\"]");
   }
   
    public void doIt() throws ServiceException, MarshalException, ValidationException, IOException, ADQLException{
        FullSearcher del = DatacenterDelegateFactory.makeFullSearcher(endpoint.toString());
        Element queryBody = null;
        if (queryFile != null) {
            Select select = Select.unmarshalSelect(new InputStreamReader ( new FileInputStream(queryFile)));
            queryBody = ADQLUtils.toQueryBody(select);
        } else {
           queryBody = SQLUtils.toQueryBody(sql);
        }
        DatacenterResults results = del.doQuery(FullSearcher.VOTABLE,queryBody);
        XMLUtils.PrettyElementToStream(results.getVotable(), System.out);        
    }


    /**
     * @return
     */
    public URL getEndpoint() {
        return endpoint;
    }

    /**
     * @return
     */
    public File getQuery() {
        return queryFile;
    }

    /**
     * @param url
     */
    public void setEndpoint(URL url) {
        endpoint = url;
    }

    /**
     * @param file
     */
    public void setQuery(File file) {
        queryFile = file;
    }

}


/* 
$Log: SimpleQuerier.java,v $
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