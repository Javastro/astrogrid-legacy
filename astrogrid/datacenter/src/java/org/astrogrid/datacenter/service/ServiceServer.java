/*
 * $Id: ServiceServer.java,v 1.9 2003/09/15 22:05:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.service;
import org.astrogrid.datacenter.queriers.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import org.apache.axis.utils.XMLUtils;
import org.apache.xpath.XPathAPI;
import org.astrogrid.datacenter.common.ResponseHelper;
import org.astrogrid.datacenter.config.Configuration;
import org.astrogrid.datacenter.queriers.DatabaseQuerier;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

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
    * @todo implement
    */
   public Element getMetadata()
   {
      try
      {
         File metaFile = new File(Configuration.getProperty(METADATA_FILE_LOC_KEY, "metadata.xml"));

         return XMLUtils.newDocument(new FileInputStream(metaFile)).getDocumentElement();
      }
      catch (javax.xml.parsers.ParserConfigurationException e)
      {
         Log.logError("XML Parser not configured properly",e);
         throw new RuntimeException("Server not configured properly",e);
      }
      catch (org.xml.sax.SAXException e)
      {
         Log.logError("Invalid metadata",e);
         throw new RuntimeException("Server not configured properly - invalid metadata",e);
      }
      catch (IOException e)
      {
         Log.logError("Metadata file error",e);
         throw new RuntimeException("Server not configured properly - metadata i/o error",e);
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
   public Element getServiceStatus(String queryId) throws Throwable
   {
      return ResponseHelper.makeStatusResponse(getService(queryId)).getDocumentElement();
   }

   /**
    * Returns the service corresponding to the given ID
    */
   public DatabaseQuerier getService(String queryId)
   {
      return DatabaseQuerier.getQuerier(queryId);
   }


}

