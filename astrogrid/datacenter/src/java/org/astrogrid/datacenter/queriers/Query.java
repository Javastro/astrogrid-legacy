/*
 * $Id Query.java $
 *
 */

package org.astrogrid.datacenter.queriers;

import org.astrogrid.datacenter.adql.ADQLException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.QOM;
import org.astrogrid.datacenter.common.DocMessageHelper;
import org.astrogrid.datacenter.query.AstroGridQuery;
import org.astrogrid.log.Log;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


/**
 * Queries may be submitted in various versions of ADQL, and at the moment
 * in an Iteration 02 AstroGrid XML Query language.
 *
 * Eventually, we can assume that all queries will be translated into
 * a particular version of ADQL, before being translated into whatever
 * the local database needs, otherwise we end up with a many-to-many
 * relationship between submitted query languages and vast hordes of
 * translators.  At that tiem this class may be subclassed from some
 * ADQL object model.
 *
 * In the meantime, for Iteration 03, this class is used to wrap the
 * whole messy business of dealing with ADQL or Astrogrid XML queries
 * that need to be translated to the local SQL.
 *
 * @author M Hill
 */



public class Query
{
   /** Original XML query... might be useful for debug */
   Element queryXml = null;

   /** AstroGrid It02 query */
   AstroGridQuery agQuery = null;

   /** ADQL object model */
   QOM adql = null;

   public Query(Element domContainingQuery) throws SAXException
   {
      NodeList queries = domContainingQuery.getElementsByTagName(DocMessageHelper.QUERY_TAG);

      Log.affirm(queries.getLength() ==1, "Too many query tags in dom to make one query from");

      queryXml = (Element) queries.item(0);

      String queryType = queryXml.getAttribute("type");

      //should now check type to see if it's one we know about, and marshall
      //into the correct object model
      if (queryType.toLowerCase().equals("adql"))
      {
         try
         {
            adql = ADQLUtils.unmarshalSelect(queryXml);
         }
         catch (org.exolab.castor.xml.ValidationException e)
         {
            throw new SAXException("ADQL invalid",e);
         }
         catch (org.exolab.castor.xml.MarshalException e)
         {
            throw new SAXException("Failed to load adql object model",e);
         }
      }
      else
      {
         //astrogrid it02 query
         try
         {
            agQuery = new AstroGridQuery(queryXml);
         }
         catch (org.astrogrid.datacenter.query.QueryException e)
         {
            throw new SAXException("Error loading astrogrid query",e);
         }
      }
   }

   /**
    * Temporary for compatibiliyt
    *
   public Element getXml()
   {
      return queryXml;
   }
    /**/

   /**
    * Temporary for it 03
    */
   public  String toSql(QueryTranslator translator) throws ADQLException
   {
      if (agQuery != null)
      {
         return agQuery.toSQLString();
      }
      if (adql != null)
      {
         return translator.translate(adql);
      }
      //should never happen
      throw new IllegalStateException("Neither astrogrid nor adql query are set");
   }
}

/*
$Log: Query.java,v $
Revision 1.2  2003/09/11 09:28:20  mch
Added backwards compatibility for old queries

Revision 1.1  2003/09/10 18:58:44  mch
Preparing to generalise Query

*/
