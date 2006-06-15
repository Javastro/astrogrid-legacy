/*$Id: XmlResults.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.xdbserver.xql;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
//import java.io.PrintWriter;
import java.io.ByteArrayInputStream;
import java.io.BufferedInputStream;

import java.security.Principal;
import org.astrogrid.dataservice.queriers.Querier;
import org.astrogrid.dataservice.queriers.RawPipeResults;
import org.astrogrid.io.mime.MimeTypes;
import org.astrogrid.slinger.sourcetargets.UrlSourceTarget;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.dataservice.queriers.status.QuerierProcessingResults;
import org.astrogrid.dataservice.queriers.status.QuerierStatus;
import org.astrogrid.tableserver.out.TableWriter;
import org.astrogrid.tableserver.out.VoTableWriter;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.dataservice.DatacenterException;
import org.xmldb.api.base.XMLDBException;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import org.xmldb.api.base.ResourceSet;
import org.xmldb.api.base.Resource;
import org.xmldb.api.modules.XMLResource;
import org.xmldb.api.base.ResourceIterator;

import org.astrogrid.dataservice.queriers.QueryResults;

/**
 * A results wrapper around an XML document stream
 *
 */
public class XmlResults implements QueryResults  {
   
    
    private static final Log log = LogFactory.getLog(XmlResults.class);    
    private Querier querier = null;
    private ResourceSet rs = null;
    private InputStream is = null;
    private String xql = null;
    /**  Std Constructor. xmlIn is a stream containing the xml document
     */
    public XmlResults(Querier querier, ResourceSet rs, String xql) {
      this.querier = querier;
      this.rs = rs;
      this.xql = xql;
    }

    /**  Constructor where the xml doc is in the given File
     */
    public XmlResults(Querier querier, File xmlFile) throws IOException {
      //this(querier, new FileInputStream(xmlFile));
      this.is = new FileInputStream(xmlFile);
      this.querier = querier;
    }
    
    /** This is a helper method for plugins; it is meant to be called
     * from the askQuery method.  It transforms the results and sends them
     * as required, updating the querier status appropriately.
     */
    public void send(ReturnSpec returns, Principal user) throws IOException {
       if (returns instanceof ReturnTable) {
          sendTable( (ReturnTable) returns, user);
       }
       else {
          try {
              if(rs != null) {
                  is = new BufferedInputStream(new ByteArrayInputStream(rs.getMembersAsResource().getContent().toString().getBytes()));
              }
              RawPipeResults rpr = new RawPipeResults(querier,is,MimeTypes.XML);
              rpr.send(returns,user);
          }catch(XMLDBException xdbe) {
              log.error(xdbe);
          }
       }
    }    
    
    
    /** Sends a table */
    public void sendTable(ReturnTable returns, Principal user) throws IOException {
       
       QuerierProcessingResults status = new QuerierProcessingResults(querier.getStatus());
       querier.setStatus(status);

       log.info(querier+", sending results to "+returns);

       TargetIdentifier target = returns.getTarget();
       String format = returns.getFormat();

       if (target == null) {
          throw new DatacenterException("No Target given for results");
       }

       status.setMessage("Sending results to "+target.toString()+" as "+format);

       TableWriter tableWriter = makeTableWriter(target, format, user);
       
       //call overridden method that will know what form the data is in
       writeTable(tableWriter, status);

       if (querier.isAborted()) {
          return;
       }
       
       String s = "Results sent as table ("+format+") to ";

       if (target instanceof UrlSourceTarget) {
          s =s+"<a href='ViewFile?"+((UrlSourceTarget) target).toURI()+"'>"+target+"</a>";
       }
       else {
          s =s+target;
       }

       status.addDetail(s);
       status.setMessage("");
         
       log.info(querier+" results sent");
    }
    
    /** Subclasses override to make spocial table writers.  requested format is given
     * as a mime type*/
    public TableWriter makeTableWriter(TargetIdentifier target, String requestedFormat, Principal user) throws IOException {

       if (requestedFormat.equals(ReturnTable.VOTABLE)) {
          return new VoTableWriter(target, "Query Results", user);
       } else if (requestedFormat.equals(ReturnTable.DEFAULT)) {
          return new VoTableWriter(target, "Query Results", user);
       }
       else {
          throw new IllegalArgumentException("Unknown results format "+requestedFormat+" given");
       }
    }    
    
    /**
     * Don't know how to write this out in table form...
     */
   public void writeTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {
       if (tableWriter instanceof VoTableWriter) {
           writeVOTable(tableWriter,statusToUpdate);
        }
        else {
           throw new UnsupportedOperationException("Can only write to votables");
        }       
   }    
   
   private void writeVOTable(TableWriter tableWriter, QuerierStatus statusToUpdate) throws IOException {
       
       try
       {
          tableWriter.open();
          ResourceIterator ri = rs.getIterator();
          while(ri.hasMoreResources()) {
              Resource res = ri.nextResource();
              ((XMLResource)res).getContentAsSAX(new XMLDBHandler(tableWriter,statusToUpdate,xql));
          }
       }catch(XMLDBException xdbe) {
           log.error(xdbe);
           
       }finally {
           tableWriter.close(); 
       }
       
       
   }

    public static String[] listFormats() {
       return new String[] { MimeTypes.XML };
    }
    
    
    
}


/*
$Log: XmlResults.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.2  2006/04/25 08:48:44  kea
Moving Exist functionality from Kev's branch into oldserver for the
time being.

Revision 1.2.64.2  2006/04/24 13:49:59  clq2
merged with Pal_KMB_XMLDB

Revision 1.2.60.2  2006/02/27 16:41:51  KevinBenson
removed a xqlmaker class that is already being used in the query package/component
added to the xmldb plugin

Revision 1.2.60.1  2006/02/09 13:30:25  KevinBenson
adding xmldb type war/service for the dsa

Revision 1.2  2005/05/27 16:21:18  clq2
mchv_1

Revision 1.1.24.1  2005/04/21 17:20:51  mch
Fixes to output types

Revision 1.1  2005/03/10 16:42:55  mch
Split fits, sql and xdb

Revision 1.1.1.1  2005/02/17 18:37:35  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:24  mch
Initial checkin

Revision 1.2.24.4  2004/12/08 18:36:40  mch
Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 
*/
