/*
 * $Id: ServiceServer.java,v 1.6 2003/11/21 17:37:56 nw Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.xpath.XPathAPI;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.axisdataserver.types.QueryId;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.datacenter.queriers.DatabaseQuerierManager;
import org.astrogrid.datacenter.snippet.ResponseHelper;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

/**
 * This abstract class provides the framework for managing the datacenter.  It
 * manages the running service instances.  Subclasses from this might implement
 * an axis/http server, or a socket-server, or a grid/ogsa server, etc.
 * @see org.astrogrid.datacenter.servers package for implementations
 * <p>
 * @author M Hill
 */

public abstract class ServiceServer
{
    private static Log log = LogFactory.getLog(ServiceServer.class);
   /** Configuration key to where the metadata file is located */
   public static final String METADATA_FILE_LOC_KEY = "MetadataFile";

   /**
    * Returns the metadata in the registry form (VOResource)
    * @todo implement
    */
   public Element getVOResource()
   {
      Element fullMetadata = getMetadata();

      //do some transformation thing
      Element voResource = fullMetadata; //for now...

      //return transformed document
      return voResource;
   }

/** primitive method to access stream containing metadata */
    protected InputStream getMetadataStream() throws IOException {
        // File metaFile = new File(Configuration.getProperty(METADATA_FILE_LOC_KEY, "metadata.xml"));
        // search for file, then for resource on classpath - fits better with appservers / servlet containers
        String location = SimpleConfig.getProperty(METADATA_FILE_LOC_KEY,"metadata.xml");
        String trying = location; //for  error reporting
        File metaFile = new File(location);
        InputStream is = null;
        if (metaFile.exists() && metaFile.isFile()) {
           is = new FileInputStream(metaFile);
        } else {
           URL url = this.getClass().getResource(location);
           if (url != null)  {
              trying = this.getClass().getResource(location).toString();
              is = url.openStream();
           }
           if (is == null && ! location.startsWith("/")) { // try making the resource absolute.
//this will throw errors if the property is already absolute
// and don't want to change the default location for this, in case it breaks socket-server based datacenters.
// NWW - well we can check for this. commenting this out breaks datacenters running in an app server.
// MCH - hopefully this will work under both :-)
              url = this.getClass().getResource("/"+location);
              if (url != null)
              {
                 trying = url.toString();
                 is = this.getClass().getResourceAsStream("/" + location);
              }
           }
        }
        if (is == null) {
            throw new IOException("metadata file '"+location+"' or '"+trying+" not found");
        }
        return is;
    }


   /**
    * Returns the whole metadata file as a DOM document
    * @todo implement better error reporting in case of failure
    *
    */
   public Element getMetadata()
   {
    InputStream is = null;
      try
      {
        is = getMetadataStream();
         return XMLUtils.newDocument(is).getDocumentElement();
      }
      catch (ParserConfigurationException e)
      {
         log.error("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e)
      {
         log.error("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata",e);
      }
      catch (IOException e)
      {
         log.error("Metadata file error",e);
         throw new RuntimeException("Server not configured properly - metadata i/o error",e);
      }
      finally {
          if (is != null) {
              try {
                is.close();
              } catch (IOException e) {
                  // not bothered
              }
          }
      }
   }

   /**
    * Returns the list of nodes of the metadata corresponding to the given XPath
    *
    * @todo implement
    * @todo - is it possible to run XPATH over an input stream (SAX-like) rather than build a DOM first?
    */
   public NodeList getMetadata(String xpathExpression) throws IOException
   {
       if (xpathExpression == null || xpathExpression.length() ==0) {
           throw new IllegalArgumentException("Empty xpathExpression");
       } 
      Element metadata = getMetadata();

      //do some xpathing
      try
      {          
         NodeList nodes = XPathAPI.selectNodeList(metadata, xpathExpression);

         return nodes;
      }
      catch (javax.xml.transform.TransformerException e)
      {
         throw new IOException("Could not transform metadata using xpath '"+xpathExpression+"'");
      }

   }
    /**/


   /** For non-blocking queries, you might want to get the query status
    * if the querier has an error (status = errro) throws the exception
    * (dont liek this too general)
    */
   public Element getQuerierStatus(QueryId queryId) throws Throwable
   {
      return ResponseHelper.makeStatusResponse(getQuerier(queryId)).getDocumentElement();
   }

   /**
    * Returns the service corresponding to the given ID
    */
   protected DatabaseQuerier getQuerier(QueryId queryId)
   {
      return DatabaseQuerierManager.getQuerier(queryId.getId());
   }



}


