/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery;

import java.io.IOException;
import java.util.Hashtable;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.oldquery.condition.Condition;
import org.astrogrid.oldquery.returns.ReturnSpec;
import org.astrogrid.oldquery.constraint.ConstraintSpec;
import org.astrogrid.oldquery.refine.RefineSpec;
import org.astrogrid.slinger.targets.TargetIdentifier;


/**
 * A full in-memory 'modelled' representation of a query.  
 * Consists of a 'scope', indicating what will be searched, 
 * a description of where the results will go, and a 'Condition' 
 * which describes the search criteria to be used.
 * 
 * @deprecated  This class is the original (now disused) pal query model; 
 * certain bits of functionality (such as the Sql2Adql class), which are 
 * still in use externally (e.g. in the Registry project),  still depend 
 * on it. When these external dependencies are cleaned up, hopefully
 * this class may be removed.
 */

public class OldQuery  {

   /** search criteria */
   Condition criteria = null;
   
   /** Defines what the results will be and Where they are to be sent */
   ReturnSpec results = null;
   
   /** Not quite sure how this should be described properly */
   String[] scope = null;

   /** Stores toplevel constrainsts such as row limits, DISTINCT filters etc */
   ConstraintSpec constraintSpec = null;

   /** Stores toplevel refinements such as GROUP BY, ORDER BY, HAVING */
   RefineSpec refineSpec = null;

   /** lookup of aliases *by table* if any are given.  This might only be used for human-readable
    * queries or where we want the exact same output as was input.  */
   Hashtable aliases = new Hashtable();
   
   /** Key used to define maximum number of matches allowed - defaults to 200. 0 or less = no limit */
   public final static String MAX_RETURN_KEY = "datacenter.max.return";

   public OldQuery(String[] givenScope, Condition someCriteria, ReturnSpec aResultsDef) {
      this.scope = givenScope;
      this.criteria = someCriteria;
      this.results = aResultsDef;
      this.constraintSpec = new ConstraintSpec();  /* Empty by default */
      this.refineSpec = new RefineSpec();  /* Empty by default */
   }

   public OldQuery(String[] givenScope, Condition someCriteria, ReturnSpec aResultsDef, ConstraintSpec constraintSpec, RefineSpec refineSpec) {
      this.scope = givenScope;
      this.criteria = someCriteria;
      this.results = aResultsDef;
      this.constraintSpec = constraintSpec;  
      this.refineSpec = refineSpec;  
   }
   
   public OldQuery(Condition someCriteria, ReturnSpec aResultsDef) {
      this.criteria = someCriteria;
      this.results = aResultsDef;
      this.constraintSpec = new ConstraintSpec();  /* Empty by default */
   }
   
   public void setScope(String[] givenScope) {
      this.scope = givenScope;
   }
   
   public Condition getCriteria()      { return criteria; }

   public TargetIdentifier getTarget()  { return results.getTarget(); }
   
   public String[] getScope()          { return scope; }

   public ReturnSpec getResultsDef() { return results; }

   public ConstraintSpec getConstraintSpec() { return constraintSpec; }

   public RefineSpec getRefineSpec() { return refineSpec; }
   
   public void setResultsDef(ReturnSpec spec) {
      this.results = spec;
   }

   /**
    * Returns maximum number of results */
   public long getLimit() 
   {      
     if (constraintSpec != null) {
       return constraintSpec.getLimit(); 
     }
     return ConstraintSpec.LIMIT_NOLIMIT;
   }

   /** Sets the row limit constraint */
   public void setLimit(long limit) 
   {      
      if (constraintSpec == null) {
         constraintSpec = new ConstraintSpec();
      }
      constraintSpec.setLimit(limit);
   }
   
   /** Returns the lowest of the query limit (stored in ConstraintSpec) 
    *  or local limit (configured in DSA setup) */
   public long getLocalLimit() {
      long queryLimit = ConstraintSpec.LIMIT_NOLIMIT;  
      long localLimit = ConfigFactory.getCommonConfig().getInt(MAX_RETURN_KEY, 0);
      if (constraintSpec != null) {
        queryLimit = constraintSpec.getLimit();
      }
      if ((queryLimit == ConstraintSpec.LIMIT_NOLIMIT) || 
             ((queryLimit > localLimit) && (localLimit > 0))) {
         queryLimit = localLimit;
      }
      return queryLimit;
   }
   
   /** Sets the allow constraint */
   public void setAllow(String allowVal) 
   {      
      if (constraintSpec == null) {
         constraintSpec = new ConstraintSpec();
      }
      constraintSpec.setAllow(allowVal);
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
      StringBuffer s = new StringBuffer("{OldQuery: ");
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
