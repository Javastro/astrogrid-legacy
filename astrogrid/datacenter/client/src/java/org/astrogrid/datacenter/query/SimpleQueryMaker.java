/*
 * $Id: SimpleQueryMaker.java,v 1.2 2004/10/06 22:13:45 mch Exp $
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
import org.astrogrid.datacenter.queriers.sql.SqlMaker;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.slinger.TargetIndicator;

public class SimpleQueryMaker  {
   
   /** Constructs query from given values in decimal degrees for coord sys J2000
    */
   public static Condition makeConeCondition(double givenRa, double givenDec, double givenRadius) {
      Expression coordSys = new LiteralString("J2000");
      Expression ra = new LiteralNumber(""+givenRa);
      Expression dec = new LiteralNumber(""+givenDec);
      Expression radius = new LiteralNumber(""+givenRadius);
      Function circle = new Function("CIRCLE", new Expression[] { coordSys, ra, dec, radius });
      
      return circle;
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIndicator target, String format) {
      Query query = makeConeQuery(givenRa, givenDec, givenRadius, target);
      query.getResultsDef().setFormat(format);
      return query;
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIndicator target) {
      String coneTable = SimpleConfig.getSingleton().getString(SqlMaker.CONE_SEARCH_TABLE_KEY);
      return new Query(new String[] { coneTable }, makeConeCondition(givenRa, givenDec, givenRadius), new ReturnTable(target));
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static Query makeConeQuery(double givenRa, double givenDec, double givenRadius) {
      return makeConeQuery(givenRa, givenDec, givenRadius, null);
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
 Revision 1.2  2004/10/06 22:13:45  mch
 Added convenience method

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 
 */



