/*
 * $Id: Query.java,v 1.2 2004/08/18 18:43:01 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.criteria.*;

import java.util.StringTokenizer;
import java.util.Vector;
import org.astrogrid.datacenter.TargetIndicator;
import org.astrogrid.datacenter.query.results.ResultsDefinition;
import org.astrogrid.datacenter.query.results.TableResultsDefinition;


/**
 * A full in-memory 'modelled' representation of a query.  Consists of a 'scope',
 * indicating what will be searched, a TargetIndicator indicating where the results
 * will go, and a 'Condition' which describes the search criteria to be used.
 * <p>
 */

public class Query  {

   /** search criteria */
   Condition criteria = null;
   
   /** Defines what the results will be and Where they are to be sent */
   ResultsDefinition results = null;
   
   /** Not quite sure how this should be described properly */
   String[] scope;
   
   public Query(String[] givenScope, Condition someCriteria, ResultsDefinition aResultsDef) {
      this.scope = givenScope;
      this.criteria = someCriteria;
      this.results = aResultsDef;
   }
   
   public Condition getCriteria()      { return criteria; }

   public TargetIndicator getTarget()  { return results.getTarget(); }
   
   public String[] getScope()          { return scope; }

   public ResultsDefinition getResultsDef() { return results; }
   
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
 Revision 1.2  2004/08/18 18:43:01  mch
 Better toString for Query

 Revision 1.1  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 */



