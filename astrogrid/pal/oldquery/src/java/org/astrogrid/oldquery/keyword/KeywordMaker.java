/*$Id: KeywordMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.oldquery.keyword;

import org.astrogrid.oldquery.condition.*;

import java.util.Enumeration;
import java.util.Hashtable;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.QueryException;

/**
 * Takes a 'flat' query - one that consists of no nested equals conditions and only AND
 * intersection conjunctions - and creates a list of keyword/value pairs.
 * <p>
 * Used for things like proxies to simple services or bespoke apps that only take a few keyword parameters
 * *
 * Circle functions translate to three keyword/value pairs; RA, DEC and RADIUS
 * <p>
 * Note that all keys are stored as upper case to make it case insensitive.
 *
 * @deprecated This class supports the old query model, OldQuery, which has
 * been deprecated and needs to be removed.
 */
public class KeywordMaker  {

   private static final Log log = LogFactory.getLog(KeywordMaker.class);

   public static final String COORD_KEYWORD = "COORDSYS";
   public static final String RA_KEYWORD = "RA";
   public static final String DEC_KEYWORD = "DEC";
   public static final String RADIUS_KEYWORD = "RADIUS";
   public static final String UNIT_KEYWORD = "UNIT";
   
   Hashtable keywords;
   
   /**
    * Sets the keywords from the given query.  Also returns the hashtable  */
   public KeywordMaker(OldQuery query) throws QueryException {
      Condition c = query.getCriteria();
      keywords = new Hashtable();
      addKeywordPair(c);
   }

   /** Returns the list of keywords */
   public Enumeration getKeywords() {
      return keywords.keys();
   }

   /** case insensitive lookup */
   public String getValue(String key) {
      Object value = keywords.get(key.toUpperCase());
      if (value == null) {
         return null;
      }
      return value.toString();
   }
   
   /** case insensitive lookup, throwing a meaningful exception if the parameter
    * doesn't exist */
   public String getRequiredValue(String key) {
      String value = getValue(key);
      if (value == null) {
         throw new IllegalArgumentException("Must specify "+key+" in the query");
      }
      return value;
   }

   /**
    * Takes a single condition and extracts keyword pair, inserst it into the
    * hashtable.  Converts all keys to upper case
    */
   private void addKeywordPair(Condition c) {
      if (c instanceof Intersection) {
         Condition[] conditions = ((Intersection) c).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            addKeywordPair(conditions[i]);
         }
      }
      else if (c instanceof Union) {
         throw new IllegalArgumentException("Keyword Searches cannot handle union (OR) conditions");
      }
      else if (c instanceof Function) {
         Function f = (Function) c;
         if (f.getName().toLowerCase().equals("circle")) {
            keywords.put(COORD_KEYWORD, ((LiteralString) f.getArg(0)).getValue());
            keywords.put(RA_KEYWORD, ((LiteralAngle) f.getArg(1)).getAngle());
            keywords.put(DEC_KEYWORD, ((LiteralAngle) f.getArg(2)).getAngle());
            keywords.put(RADIUS_KEYWORD, ((LiteralAngle) f.getArg(3)).getAngle());
            keywords.put(UNIT_KEYWORD, "deg");
         }
         else {
            throw new IllegalArgumentException("Keyword Searches cannot handle "+f.getName()+" functions");
         }
      }
      else if (c instanceof StringComparison) {
         StringComparison s = (StringComparison) c;
         if ( (s.getOperator() == StringCompareOperator.EQ) || (s.getOperator() == StringCompareOperator.LIKE) ) {
            if ( (s.getLHS() instanceof SearchFieldReference) && (s.getRHS() instanceof LiteralString) ) {
               keywords.put( ((SearchFieldReference) s.getLHS()).getField().toUpperCase(), ((LiteralString) s.getRHS()).getValue());
            }
            else {
               throw new IllegalArgumentException("In a Keyword Search, String Comparisons must be {SearchField} = {LiteralString}, not "+s.getLHS() +" = "+s.getRHS());
            }
         }
         else {
            throw new IllegalArgumentException("In a Keyword Search, String Comparisons must be {SearchField} = {LiteralString}, not "+s.getLHS() +" = "+s.getRHS());
         }
      }
      else if (c instanceof NumericComparison) {
         NumericComparison n = (NumericComparison) c;
         if ( (n.getOperator() == NumericCompareOperator.EQ) ) {
            if ( (n.getLHS() instanceof SearchFieldReference) && (n.getRHS() instanceof LiteralNumber) ) {
               keywords.put( ((SearchFieldReference) n.getLHS()).getField().toUpperCase(), ((LiteralNumber) n.getRHS()).getValue());
            }
            else {
               throw new IllegalArgumentException("In a Keyword Search, Numeric Comparisons must be {SearchField} = {LiteralNumber}, not "+n.getLHS() +" = "+n.getRHS());
            }
         }
         else {
            throw new IllegalArgumentException("In a Keyword Search, Numeric Comparisons must be {SearchField} = {LiteralNumber}, not "+n.getLHS() +" = "+n.getRHS());
         }
      }
   }
   
   /** String representation */
   public String toString() {
      String s = "KeywordQuery: ";
      Enumeration e=keywords.keys();
      while (e.hasMoreElements()) {
         String key = (String) e.nextElement();
         s = s+key+"="+keywords.get(key)+", ";
      }
      return s.substring(0,s.length()-2);
   }

}


/*
$Log: KeywordMaker.java,v $
Revision 1.2  2006/06/15 16:50:09  clq2
PAL_KEA_1612

Revision 1.1.2.1  2006/04/21 10:58:25  kea
Renaming package.

Revision 1.1.2.1  2006/04/20 15:18:03  kea
Adding old query code into oldquery directory (rather than simply
chucking it away - bits may be useful).

Revision 1.2.64.1  2006/04/10 16:17:44  kea
Bits of registry still depending (implicitly) on old Query model, so
moved this sideways into OldQuery, changed various old-model-related
classes to use OldQuery and slapped deprecations on them.  Need to
clean them out eventually, once registry can find another means to
construct ADQL from SQL, etc.

Note that PAL build currently broken in this branch.

Revision 1.2  2005/05/27 16:21:20  clq2
mchv_1

Revision 1.1.1.1.24.1  2005/05/13 16:56:29  mch
'some changes'

Revision 1.1.1.1  2005/02/17 18:37:34  mch
Initial checkin

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.5.2.3  2004/11/29 21:52:18  mch
Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

Revision 1.5.2.2  2004/11/24 20:59:37  mch
doc fixes and added slinger browser

Revision 1.5.2.1  2004/11/17 11:15:46  mch
Changes for serving images

Revision 1.5  2004/11/12 13:49:12  mch
Fix where keyword maker might not have had keywords made

Revision 1.4  2004/11/11 20:42:50  mch
Fixes to Vizier plugin, introduced SkyNode, started SssImagePlugin

Revision 1.3  2004/11/09 18:14:59  mch
Better error text

Revision 1.2  2004/11/03 12:13:26  mch
Fixes to branch cockup, plus katatjuta Register and get cone (for examples)

Revision 1.1  2004/11/03 05:14:33  mch
Bringing Vizier back online

Revision 1.2  2004/10/06 21:12:17  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

Revision 1.1  2004/10/05 19:19:18  mch
Merged CDS implementation into PAL

Revision 1.5  2004/09/29 18:45:55  mch
Bringing Vizier into line with new(er) metadata stuff

Revision 1.4  2004/09/28 15:05:27  mch
Temporary compile-only fix for removing obsolete ADQL 0.5

Revision 1.3  2004/09/10 10:31:17  mch
Added cause to thrown error

Revision 1.2  2004/09/07 00:54:20  mch
Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

Revision 1.1  2004/03/13 23:40:59  mch
Changes to adapt to It05 refactor

Revision 1.4  2004/03/12 20:04:57  mch
It05 Refactor (Client)

Revision 1.3  2004/03/12 05:03:23  mch
Removed unused code

Revision 1.2  2004/03/12 05:01:22  mch
Changed doc

Revision 1.1  2004/03/12 04:45:26  mch
It05 MCH Refactor

 
*/



