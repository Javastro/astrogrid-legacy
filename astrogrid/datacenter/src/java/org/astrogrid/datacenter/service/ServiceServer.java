/*
 * $Id: ServiceServer.java,v 1.13 2003/09/23 18:09:09 mch Exp $
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
import org.apache.xpath.XPathAPI;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sun.security.krb5.internal.crypto.e;

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

   /**
    * Returns the whole metadata file as a DOM document
    * @todo implement better error reporting in case of failure
    *
    */
   public Element getMetadata()
   {
      // File metaFile = new File(Configuration.getProperty(METADATA_FILE_LOC_KEY, "metadata.xml"));
      // search for file, then for resource on classpath - fits better with appservers / servlet containers
      String location = Configuration.getProperty(METADATA_FILE_LOC_KEY,"metadata.xml");
      String trying = location; //for  error reporting
      try
      {

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
           if (is == null) { // try making the resource absolute.
//this will throw errors if the property is already absolute
//trying = this.getClass().getResource("/"+location).toString();
//              is = this.getClass().getResourceAsStream("/" + location);
              throw new IOException("metadata file '"+location+"' or '"+trying+" not found");
           }
        }

         return XMLUtils.newDocument(is).getDocumentElement();
      }
      catch (ParserConfigurationException e)
      {
         Log.logError("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (SAXException e)
      {
         Log.logError("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata in '"+trying+"'",e);
      }
      catch (IOException e)
      {
         Log.logError("Metadata file error",e);
         throw new RuntimeException("Server not configured properly - metadata i/o error for '"+trying+"'",e);
      }
   }

   /**
    * Returns the list of nodes of the metadata corresponding to the given XPath
    *
    * @todo implement
    */
   public NodeList getMetadata(String xpathExpression) throws IOException
   {
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
   public Element getQuerierStatus(String queryId) throws Throwable
   {
      return ResponseHelper.makeStatusResponse(getQuerier(queryId)).getDocumentElement();
   }

   /**
    * Returns the service corresponding to the given ID
    */
   public DatabaseQuerier getQuerier(String queryId)
   {
      return DatabaseQuerier.getQuerier(queryId);
   }


}


