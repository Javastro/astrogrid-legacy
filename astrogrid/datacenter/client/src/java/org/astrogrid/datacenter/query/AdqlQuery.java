/*
 * $Id: AdqlQuery.java,v 1.3 2004/03/13 23:38:27 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;

import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.datacenter.adql.ADQLUtils;
import org.astrogrid.datacenter.adql.generated.Select;
import org.astrogrid.util.DomHelper;
import org.exolab.castor.xml.CastorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * Represents an ADQL Query.  Takes a string; so any ADQL version will do; in
 * fact it should take an SQL string as well...
 *
 * @author M Hill
 */


public class AdqlQuery implements Query {

   String adqlXml = null;

   /** Constructs query. Throws exception if raw SQL is not allowed on this
    * server
    */
   public AdqlQuery(String givenAdql) {
      
      this.adqlXml = givenAdql;
   }

   /** Constructs query from Object model representation
    */
   public AdqlQuery(Select givenAdql) throws QueryException {
      try {
         //rather wastfully just converts back to a string...
         this.adqlXml = ADQLUtils.queryToString(givenAdql);
      }
      catch (CastorException e) {
         throw new QueryException("Caster error: ",e);
      }
      catch (IOException e) {
         throw new QueryException("IO error: ",e);
      }
   }
   /** Constructs query from Object model representation
    */
   public AdqlQuery(Element givenAdql) throws QueryException {
      //rather wastfully just converts back to a string...
      this.adqlXml = DomHelper.ElementToString(givenAdql);
   }
   
   /** Returns DOM representation  */
   public Document toDom() throws QueryException {
      try {
            return DomHelper.newDocument(adqlXml);
      }
      catch (IOException e) {
         //shouldn't happen
         throw new RuntimeException("Accessing ADQL/XML", e);
      }
      catch (ParserConfigurationException e) {
         //shouldn't happen
         throw new RuntimeException("Server not configured properly", e);
      }
      catch (SAXException e) {
         throw new QueryException("ADQL is not valid XML",e);
      }

   }
   
   /** Human representation for debugging, trace, etc */
   public String toString() {
      return "AdqlQuery";
   }
   
}
/*
 $Log: AdqlQuery.java,v $
 Revision 1.3  2004/03/13 23:38:27  mch
 Test fixes and better front-end JSP access

 Revision 1.2  2004/03/12 23:58:03  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 20:00:11  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor


 */



