/*
 * $Id: ConstraintSpec.java,v 1.2 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.constraint;

import org.astrogrid.query.QueryException;

/**
 * Stores various toplevel constraints applying to a query, such
 * as a numeric row limit or 'DISTINCT' filter.
 *
 * Created by KEA as a holder for constraint information, some of
 * which was previously duplicated in different classes and some of
 * which was missing from the query model.
 *
 * @author K Andrews
 */
public class ConstraintSpec  {

   long limit = LIMIT_NOLIMIT;
   String allow = ALLOW_EMPTY;

   /** Value indicating limit is not set */
   public static final long LIMIT_NOLIMIT  = -1;
   
   /** Allowed values for this.allow */
   public static final String ALLOW_EMPTY  = "";
   public static final String ALLOW_ALL  = "All";
   public static final String ALLOW_DISTINCT = "DISTINCT";
   
   /** Sets Limit to number of result items to be returned */
   public void setLimit(long limit) 
   {  
      this.limit = limit; 
   }
   public long getLimit()
   {
      return limit;
   }

   /** Set allowed results ("all" or "DISTINCT") */
   public void setAllow(String allowVal) throws QueryException
   {
      if (allowVal != null) {
         if ( !(allowVal.equals(ALLOW_ALL)) && 
                !(allowVal.equals(ALLOW_DISTINCT)) ) {
            throw new QueryException(
               "Allow must have 'Option' attribute of '" +
               ALLOW_ALL + "' or '" + ALLOW_DISTINCT + "'");
         }
      }
      this.allow = allowVal;
   }
   public String getAllow()
   {
      return allow;
   }

   /** Returns Limit to number of result items to be returned */
   public String toString() 
   {
     String s = null;
     if (limit>0) {
        s = s +" limit "+limit+", ";
     }
     if ( ! allow.equals(ALLOW_EMPTY) ) {
        s = s +" allow "+allow+" ";
     }
     return s;
   }
}
