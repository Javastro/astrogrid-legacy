/*
 * $Id: SimpleQueryMaker.java,v 1.7 2004/10/25 13:14:19 jdt Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query;


/**
 * Used to make simple searches, such as cone, keyword lists, etc.
 *
 * @author M Hill
 */

import org.astrogrid.datacenter.query.condition.*;

import java.util.Enumeration;
import java.util.Properties;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.TargetIndicator;

public class SimpleQueryMaker  {
   
   /** Constructs query from given values in decimal degrees for coord sys J2000
    */
   protected static Condition makeConeCondition(double givenRa, double givenDec, double givenRadius) {
      Expression coordSys = new LiteralString("J2000");
      Expression ra = new LiteralNumber(""+givenRa);
      Expression dec = new LiteralNumber(""+givenDec);
      Expression radius = new LiteralNumber(""+givenRadius);
      Function circle = new Function("CIRCLE", new Expression[] { coordSys, ra, dec, radius });
      
      return circle;
   }

   /** this routine creates an incomplete query (no return specification), but it's left in for backwards
    * compatibility with the older web/etc interfaces that take a query and separate return spec parameters
    . @deprecated
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius) {
      return new Query(makeConeCondition(givenRa, givenDec, givenRadius), new ReturnTable(null));
   }

   /** Convenience routine to constructs a query with a condition 'circle' of the given parameters, returning the given returnspec.
    */
   public static Query makeConeQuery(String[] scope, double givenRa, double givenDec, double givenRadius, ReturnSpec returns) {
      return new Query(scope, makeConeCondition(givenRa, givenDec, givenRadius), returns);
   }

   /** Constructs a query with a condition 'circle' of the given parameters, returning the given returnspec.
    * The scope ('FROM') is loaded from the configuration file, if given
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, ReturnSpec returns) {
      String coneTable = SimpleConfig.getSingleton().getString("conesearch.table", null);
      if (coneTable != null) {
         return new Query(new String[] { coneTable }, makeConeCondition(givenRa, givenDec, givenRadius), returns);
      }
      else {
         return new Query(makeConeCondition(givenRa, givenDec, givenRadius), returns);
      }
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIndicator target, String format) {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target, format));
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIndicator target) {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target));
   }

   /** Constructs condition from a list of keywords and values which will be ANDed
    */
   public static Condition makeKeywordCondition(Properties properties) {
      Enumeration keys = properties.keys();
      Intersection intersection = null;
      while (keys.hasMoreElements()) {
         String key = (String) keys.nextElement();
         String value = (String) properties.get(key);

         Condition comparison = new StringComparison(new RawSearchField(key), StringCompareOperator.LIKE, new LiteralString(value));
         
         //add condition to previous
         if (intersection == null) {
            intersection = new Intersection(comparison);
         }
         else {
            intersection.addCondition(comparison);
         }
      }
      return intersection;
   }
}
/*
 $Log: SimpleQueryMaker.java,v $
 Revision 1.7  2004/10/25 13:14:19  jdt
 Merges from branch PAL_MCH - another attempt

 Revision 1.4.6.1  2004/10/21 19:10:24  mch
 Removed deprecated translators, moved SqlMaker back to server,

 Revision 1.4  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.3.2.2  2004/10/16 14:28:36  mch
 Fixed recursion problem for some cone generation

 Revision 1.3.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 22:13:45  mch
 Added convenience method

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 
 */



