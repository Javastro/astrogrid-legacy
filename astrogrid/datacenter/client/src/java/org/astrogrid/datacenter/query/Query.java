/*
 * $Id: Query.java,v 1.4 2004/10/12 22:46:42 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;
import org.astrogrid.datacenter.query.condition.*;

import java.util.StringTokenizer;
import java.util.Vector;
import org.astrogrid.slinger.TargetIndicator;
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
   String[] scope = null;

   /** maximum number of results. -1 = unlimited */
   long limit = -1;

   
   public Query(String[] givenScope, Condition someCriteria, ReturnSpec aResultsDef) {
      this.scope = givenScope;
      this.criteria = someCriteria;
      this.results = aResultsDef;
   }
   
   public Query(Condition someCriteria, ReturnSpec aResultsDef) {
      this.criteria = someCriteria;
      this.results = aResultsDef;
   }
   
   public void setScope(String[] givenScope) {
      this.scope = givenScope;
   }
   
   /** Sets maximum number of results   */
   public void setLimit(long limit) {     this.limit = limit;  }
   
   /**
    * Returns maximum number of results */
   public long getLimit() {      return limit; }
   
   public Condition getCriteria()      { return criteria; }

   public TargetIndicator getTarget()  { return results.getTarget(); }
   
   public String[] getScope()          { return scope; }

   public ReturnSpec getResultsDef() { return results; }
   
   public void setResultsDef(ReturnSpec spec) {
      this.results = spec;
   }
   
   /**
    * For humans/debuggign
    */
   public String toString() {
      StringBuffer s = new StringBuffer("{Query: ");
      if (scope != null) {
         s.append("In scope ");
         for (int i = 0; i < scope.length; i++) { s.append(scope[i]+","); }
      }
      if (criteria == null) {
         s.append("find everything, ");
      }
      else {
         s.append("find where "+criteria+", ");
      }
      s.append(" returning "+results+"}");
      return s.toString();
   }
   
   
   
}

/*
 $Log: Query.java,v $
 Revision 1.4  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.3  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.2  2004/08/18 18:43:01  mch
 Better toString for Query

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 */



