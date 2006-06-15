/*
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery;

import org.astrogrid.oldquery.condition.*;

import java.io.StringWriter;
import java.util.Enumeration;
import java.util.Hashtable;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.oldquery.returns.ReturnSpec;
import org.astrogrid.oldquery.returns.ReturnTable;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.astrogrid.slinger.targets.WriterTarget;

/**
 * Used to make simple searches, such as cone, keyword lists, etc.
 *
 * @author M Hill
 *
 * @deprecated This is present to support the old query model,
 * OldQuery, which has also been deprecated.
 */


public class SimpleQueryMaker {
   
   /** this routine creates an incomplete query (no return specification), but it's left in for backwards
    * compatibility with the older web/etc interfaces that take a query and separate return spec parameters.
    . Used quite a bit in tests - for the real world, you should really use the constructors with ReturnSpecs included
    * ReturnSpec defaults to string writer
    */
   public static OldQuery makeConeQuery(double givenRa, double givenDec, double givenRadius) {
      return new OldQuery(new CircleCondition("J2000", givenRa, givenDec, givenRadius),
                       new ReturnTable(new WriterTarget(new StringWriter())));
   }

   /** Convenience routine to constructs a query with a condition 'circle' of the given parameters, returning the given returnspec.
    */
   public static OldQuery makeConeQuery(String[] scope, double givenRa, double givenDec, double givenRadius, ReturnSpec returns) {
      return new OldQuery(scope,
                       new CircleCondition("J2000", givenRa, givenDec, givenRadius),
                       returns);
   }

   /** Constructs a query with a condition 'circle' of the given parameters, returning the given returnspec.
    * The scope ('FROM') is loaded from the configuration file, if given
    */
   public static OldQuery makeConeQuery(double givenRa, double givenDec, double givenRadius, ReturnSpec returns) {
      String coneTable = ConfigFactory.getCommonConfig().getString("conesearch.table", null);
      if (coneTable != null) {
         return new OldQuery(new String[] { coneTable },
                           new CircleCondition("J2000", givenRa, givenDec, givenRadius),
                           returns);
      }
      else {
         return new OldQuery(new CircleCondition("J2000", givenRa, givenDec, givenRadius), returns);
      }
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static OldQuery makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIdentifier target, String format) {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target, format));
   }

   /** Convenience method for constructing a cone query returning a VOTable to the given target
    */
   public static OldQuery makeConeQuery(double givenRa, double givenDec, double givenRadius, TargetIdentifier target) {
      return makeConeQuery(givenRa, givenDec, givenRadius, new ReturnTable(target));
   }

   /** Constructs condition from a list of keywords and values which will be ANDed
    */
   public static Condition makeKeywordCondition(Hashtable keywords) {
      Enumeration keys = keywords.keys();
      Intersection intersection = null;
      while (keys.hasMoreElements()) {
         String key = (String) keys.nextElement();
         String value = (String) keywords.get(key);

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
