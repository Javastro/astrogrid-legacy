/*
 * $Id: Query.java,v 1.3 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.condition.*;

import java.util.StringTokenizer;
import java.util.Vector;
import org.astrogrid.datacenter.returns.TargetIndicator;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;


/**
 * A full in-memory 'modelled' representation of a query.  Consists of a 'scope',
 * indicating what will be searched, a description of where the results
 * will go, and a 'Condition' which describes the search criteria to be used.
 * <p>
 */

public class Query  {

   /** search criteria */
   Condition criteria = null;
   
   /** Defines what the results will be and Where they are to be sent */
   ReturnSpec results = null;
   
   /** Not quite sure how this should be described properly */
   String[] scope;
   
   public Query(String[] givenScope, Condition someCriteria, ReturnSpec aResultsDef) {
      this.scope = givenScope;
      this.criteria = someCriteria;
      this.results = aResultsDef;
   }
   
   public Condition getCriteria()      { return criteria; }

   public TargetIndicator getTarget()  { return results.getTarget(); }
   
   public String[] getScope()          { return scope; }

   public ReturnSpec getResultsDef() { return results; }
   
   /**
    * For humans/debuggign
    */
   public String toString() {
      StringBuffer s = new StringBuffer("{Query: In scope ");
      for (int i = 0; i < scope.length; i++) { s.append(scope[i]+","); }
      if (criteria == null) {
         s.append("find everything, ");
      }
      else {
         s.append("look for where "+criteria+", ");
      }
      s.append(" returning "+results+"}");
      return s.toString();
   }
   
   
   
}

/*
 $Log: Query.java,v $
 Revision 1.3  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.2  2004/08/18 18:43:01  mch
 Better toString for Query

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 */



