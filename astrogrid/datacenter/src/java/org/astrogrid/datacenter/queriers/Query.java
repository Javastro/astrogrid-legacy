/*
 * $Id Query.java $
 *
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.log.Log;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;


/**
 * A wrapper class for holding whatever XML (or otherwise) query we have
 * marshalled from the input soap document, and organising whatever translators
 * we will need to get the appropriate output for the database we are
 * accessing.
 *
 * @author M Hill
 */



public class Query
{
   /** Original XML query... might be useful for debug */
   Element queryXml = null;
   
   public Query(Element domContainingQuery)
   {
      NodeList queries = domContainingQuery.getElementsByTagName(DocMessageHelper.QUERY_TAG);
      
      Log.affirm(queries.getLength() ==1, "Too many query tags in dom to make one query from");
      
      queryXml = (Element) queries.item(0);
      
      String queryType = queryXml.getAttribute("type");

      //should now check type to see if it's one we know about, and marshall
      //into the correct object model
   }

   /**
    * Temporary for compatibiliyt
    */
   public Element getXml()
   {
      return queryXml;
   }
}

/*
$Log: Query.java,v $
Revision 1.1  2003/09/10 18:58:44  mch
Preparing to generalise Query

*/
