/*
 * $Id: Query.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import java.util.Hashtable;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.query.condition.Condition;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.slinger.targets.TargetIdentifier;


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

   /** lookup of aliases *by table* if any are given.  This might only be used for human-readable
    * queries or where we want the exact same output as was input.  */
   Hashtable aliases = new Hashtable();
   
   /** Key used to define maximum number of matches allowed - defaults to 200. 0 or less = no limit */
   public final static String MAX_RETURN_KEY = "datacenter.max.return";

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

   public TargetIdentifier getTarget()  { return results.getTarget(); }
   
   public String[] getScope()          { return scope; }

   public ReturnSpec getResultsDef() { return results; }
   
   public void setResultsDef(ReturnSpec spec) {
      this.results = spec;
   }

   /** Returns the lowest of the query or local limit */
   public long getLocalLimit() {
      long localLimit = SimpleConfig.getSingleton().getInt(MAX_RETURN_KEY, 0);
      long queryLimit = getLimit();
      if ((queryLimit <= 0) || ((queryLimit > localLimit) && (localLimit > 0))) {
         queryLimit = localLimit;
      }
      return queryLimit;
   }
   

   
   public void addAlias(String table, String alias)
   {
      //note that this is indexed *by table*, so that we can look up which alias
      //to use on writing out
      aliases.put(table, alias);
   }
   
   /** Returns the alias of the given table, if any */
   public String getAlias(String table) {
      return (String) aliases.get(table);
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
   
   /** Accept Visitor, and pass it down to the appropriate properties */
   public void acceptVisitor(QueryVisitor visitor)  throws IOException {
      visitor.visitQuery(this);
   }
   
}

/*
 $Log: Query.java,v $
 Revision 1.1  2005/02/17 18:37:34  mch
 *** empty log message ***

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.7.6.4  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.7.6.3  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.7.6.2  2004/11/22 00:57:15  mch
 New interfaces for SIAP etc and new slinger package

 Revision 1.7.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.7  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.6  2004/11/03 00:17:56  mch
 PAL_MCH Candidate 2 merge

 Revision 1.5.6.2  2004/11/01 16:01:25  mch
 removed unnecessary getLocalLimit parameter, and added check for abort in sqlResults

 Revision 1.5.6.1  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.5  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.4.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

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



