/*$Id: SimpleQuerier.java,v 1.1 2003/11/24 21:06:01 nw Exp $
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.rpc.ServiceException;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.datacenter.delegate.AdqlQuerier;
import org.astrogrid.datacenter.delegate.DatacenterDelegateFactory;
import org.astrogrid.datacenter.delegate.DatacenterQuery;
import org.astrogrid.datacenter.delegate.DatacenterResults;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.ValidationException;

/** Simple tool to fire a query at a server, and print out the response.
 * Usage: java org.astrogrid.datacenter.tools.SimpleQuerier <i>service URL</i> <i>adql file</i>
 * @author Noel Winstanley nw@jb.man.ac.uk 24-Nov-2003
 *
 */
public class SimpleQuerier {

    public final static void main(String[] args) throws Exception {
        (new SimpleQuerier(args)).doIt();
        
    }
    
    public SimpleQuerier() {
    }
    public SimpleQuerier(String[] args) throws IllegalArgumentException, MalformedURLException{
        if (args.length != 2) {
            throw new IllegalArgumentException("Usage: java org.astrogrid.datacenter.tools.SimpleQuerier <service URL> <query file>");
        }
        endpoint = new URL(args[0]);
        queryFile = new File(args[1]);
        if (!queryFile.exists()) {
            throw new IllegalArgumentException("Query file does not exist: " + queryFile.getAbsolutePath());
        }
                
    }
    protected URL endpoint;
    protected File queryFile;

    public void doIt() throws MalformedURLException, ServiceException, IOException, MarshalException, ValidationException, FileNotFoundException{
        AdqlQuerier del = DatacenterDelegateFactory.makeAdqlQuerier(endpoint.toString());
        Select select = Select.unmarshalSelect(new InputStreamReader ( new FileInputStream(queryFile)));
        DatacenterResults results = del.doQuery(AdqlQuerier.VOTABLE,select);
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
Revision 1.1  2003/11/24 21:06:01  nw
added little command-line tool to query datacenter
 
*/