/*
 * $Id: DatacenterDelegate.java,v 1.1 2003/08/25 15:19:28 mch Exp $
 *
 * (C) Copyright AstroGrid...
 */

package org.astrogrid.datacenter.delegate;

import java.util.Hashtable;
import org.w3c.dom.Element;

/**
 * A convenience class for java clients of datacenters.  They can create and
 * use this class to manage all the connections/calls/etc without having to
 * mess around with all the SOAP messages directly.
 *
 * An instance of one of these corresponds to one connection to one database
 *
 * @author M Hill
 */

public class DatacenterDelegate
{
   public DatacenterDelegate()
   {
   }

   /**
    * Convenience method for making a simple query taking key/value pairs and
    * returning all elements
    * that match all of them.  Tests the cgi-like query.
    */
   public Element pairsQueryDatacenter(Hashtable keyValuePairs)
   {
      return null;
   }

   /**
    * General purpose query database; pass in an XML document with the query
    * described in ADQL (Astronomical Data Query Language).  Returns the
    * results part of the returned document, which may be VOTable or otherwise
    * depending on the results format specified in the ADQL
    */
   public Element adqlQueryDataceneter(Element adql)
   {
      return null;
   }

   /**
    * Returns the number of matches of the given query
    */
   public int adqlCountDatacenter(Element adql)
   {
      return 0;
   }

   /**
    * returns metadata (an XML document describing the data the
    * center serves) in the form required by registries. See the VOResource
    * schema; I think that is what this should return...
    */
   public Element getRegistryMetadata()
   {
      return null;
   }

   /**
    * Polls the service and asks for the current status
    */
   public String getStatus()
   {
      return DatacenterStatusListener.UNKNOWN;
   }

   /**
    * Register a status listener
    */
   public void registerStatusListener(DatacenterStatusListener aListener)
   {
   }

}

/*
$Log: DatacenterDelegate.java,v $
Revision 1.1  2003/08/25 15:19:28  mch
initial checkin


*/

