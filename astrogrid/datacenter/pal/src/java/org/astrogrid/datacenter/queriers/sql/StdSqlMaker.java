/*$Id: StdSqlMaker.java,v 1.6 2004/11/03 01:35:18 mch Exp $
 * Created on 27-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.datacenter.queriers.sql;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.StringTokenizer;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.ConfigException;
import org.astrogrid.config.SimpleConfig;
import org.astrogrid.datacenter.query.Adql074Writer;
import org.astrogrid.datacenter.query.Query;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.query.condition.Function;
import org.astrogrid.datacenter.sky.Angle;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

/**
 * A 'standard' translator that creates 'standard' SQL
 */
public class StdSqlMaker  extends AdqlSqlMaker {

   
   private static final Log log = LogFactory.getLog(StdSqlMaker.class);
   
    /** Returns the SQL condition for a circle based on the columns in the
    * configuration file
    */
   public String makeSqlCircleCondition(Angle ra, Angle dec, Angle radius) {
      String table = SimpleConfig.getSingleton().getString(CONE_SEARCH_TABLE_KEY);
      
      //get which columns given RA & DEC for cone searches
      String raCol  = table+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_RA_COL_KEY);
      String decCol = table+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_DEC_COL_KEY);
      
      return makeSqlCircleCondition(raCol, decCol, ra, dec, radius);
   }
   
   /** Returns the SQL condition expression for a circle.  This circle is
    * 'flat' on the sphere, when vieweing the sphere from the center.  ie, the
    * radius is a declination angle from the given RA & DEC point.  This means
    * the circle is distorted in coordinate space
    */
   public String makeSqlCircleCondition(String raCol, String decCol, Angle ra, Angle dec, Angle radius) {
      //Some db functions take radians, some degrees.  Fail if the config is not
      //specified, so we force people to get it right...
      boolean funcsInRads = SimpleConfig.getSingleton().getBoolean(DB_TRIGFUNCS_IN_RADIANS);
      
      String raColRad = makeColumnRadians(raCol);
      String decColRad = makeColumnRadians(decCol);
      
      //naively, we could use the 'least squares' distance (pythagoros) to see if
      //the objects are within radius distance. However this doesn't work well
      //except very near the equator, and is useless over the poles.
      
      
      //'haversine' distance formulae.  The correct one to use...
      if (funcsInRads) {
         return
            makeSqlBoundsCondition(raCol, decCol, ra, dec, radius) + " AND "+
            "("+
            "(2 * ASIN( SQRT( "+
            //surround dec with extra brackets in order to cope with negative decs
            "POWER( SIN( ("+decColRad+" - ("+dec.asRadians()+") ) / 2 ) ,2) + "+
            "COS("+dec.asRadians()+") * COS("+decColRad+") * "+
            "POWER( SIN( ("+raColRad+" - "+ra.asRadians()+") / 2 ), 2) "+
            "))) < "+radius.asRadians()+
            ")";
      }
      else {
         throw new UnsupportedOperationException("Not done degree funcs yet - do they exist?");
      }
      /**/
   }

   /** Returns a SQL condition expression to 'bound' a given circle. Otherwise circle queries
    * query is likely to take a very very long time as it trawls through all the rows calculating
    * the distance.
    * <p> At the moment this is just a DEC-binding (which is easier than RA :-)
    * @param raCol, decCol - the column names that contain the RA & DEC values of the objects
    */
    public String makeSqlBoundsCondition(String raCol, String decCol, Angle ra, Angle dec, Angle radius) {
   
       String colUnits = SimpleConfig.getSingleton().getString(CONE_SEARCH_COL_UNITS_KEY);
      
      if (dec.asDegrees() - radius.asDegrees() < -90) {
         //circle includes north pole.  Add bounds as just a dec limit.
         return "("+decCol+"<"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()+radius.asRadians()))+")";
      }
   
      if (dec.asDegrees() + radius.asDegrees() > +90) {
         //circle includes south pole. Add bounds as just a dec limit
         return "("+decCol+">"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()-radius.asRadians()))+")";
      }

      //make upper and lower limit
      return "( ("+decCol+"<"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()+radius.asRadians()))+") "+
            " and "+
              " ("+decCol+">"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()-radius.asRadians()))+") )";
    
    }
   
   
   /**
    * Returns the right SQL to translate a conesearch column to radians
    */
   public String makeColumnRadians(String colName) {
      String colUnits = SimpleConfig.getSingleton().getString(CONE_SEARCH_COL_UNITS_KEY).trim().toLowerCase();
      
      if (colUnits.equals("rad")) {
         return colName;
      }
      else if (colUnits.equals("deg")) {
         return "RADIANS("+colName+")";
      }
      else if (colUnits.equals("marcsec")) {
         return "RADIANS("+colName+"*360000)";
      }
      else {
         throw new ConfigException("Unknown units '"+colUnits+"' for conesearch columns, only 'rad', 'deg' or 'marcsec' supported");
      }
   }
   
   /** Returns the given angle in the column's units.   Useful for making SQL a bit
    * simpler - convert the angles to the column units rather than vice versa with functions */
   public String getAngleInColUnits(Angle givenAngle) {
      String colUnits = SimpleConfig.getSingleton().getString(CONE_SEARCH_COL_UNITS_KEY).trim().toLowerCase();
      if (colUnits.equals("rad")) {
         return ""+givenAngle.asRadians();
      }
      else if (colUnits.equals("deg")) {
         return ""+givenAngle.asDegrees();
      }
      else if (colUnits.equals("marcsec")) {
         return ""+(givenAngle.asArcSecs()*1000);
      }
      else {
         throw new ConfigException("Unknown units '"+colUnits+"' for conesearch columns, only 'rad', 'deg' or 'marcsec' supported");
      }
   }
   
   
   /**
    * Constructs an SQL statement for the given ADQL document by getting the
    * (super) ADQL/sql and replacing the region
    */
   public String makeSql(Query query) throws QueryException {
      String sql = super.makeSql(query);
      
      sql = replaceRegion(sql);
      return sql;
   }
   
   
   /**
    * Replaces the REGION() function with the cone search SQL.
    * This is a bit messy as we're reparsing the SQL to replace the function
    * but we need access to the configuration for this...
    */
   public String replaceRegion(String sql) {
      
      int start = sql.toLowerCase().indexOf("region");
      
      if (start==-1) {
         return sql;
      }
      
      int end = sql.indexOf(")", start);
      
      int argStart=sql.indexOf("'", start);
      int argEnd=sql.indexOf("'", argStart+1);
      
      String regionArg = sql.substring(argStart+1, end-1).trim().toLowerCase();
      
      StringTokenizer s = new StringTokenizer(regionArg, " ");
      String shape = s.nextToken();
      
      if (shape.equals("circle")) {
         //parse out arguments
         String type = s.nextToken();
         if (type.equals("j2000")) {
            Angle ra = Angle.fromDegrees(Double.parseDouble(s.nextToken()));
            Angle dec = Angle.fromDegrees(Double.parseDouble(s.nextToken()));
            Angle radius = Angle.fromDegrees(Double.parseDouble(s.nextToken()));
            
            return sql.substring(0,start)+
               makeSqlCircleCondition(ra, dec, radius)+
               sql.substring(end+1);
            
         }
         else if (type.equals("cartesian")) {
            throw new UnsupportedOperationException("Can't yet do cartesian circles");
         }
         else {
            throw new QueryException("Unknown circle type: "+type);
         }
         
      }
      else {
         throw new QueryException("Unknown region shape: "+regionArg);
      }
   }
}


/*
 $Log: StdSqlMaker.java,v $
 Revision 1.6  2004/11/03 01:35:18  mch
 PAL_MCH_Candidate2 merge Part II

 Revision 1.2.2.4  2004/11/01 12:25:29  mch
 Fix for dec -90 to +90

 Revision 1.2.2.3  2004/10/27 00:43:39  mch
 Started adding getCount, some resource fixes, some jsps

 Revision 1.2.2.2  2004/10/22 14:34:56  mch
 fixes for limiting sql on ms sql server

 Revision 1.2.2.1  2004/10/22 09:05:15  mch
 Moved SqlMakers back to server

 Revision 1.7.6.1  2004/10/21 16:14:21  mch
 Changes to take home

 Revision 1.7  2004/10/18 13:11:30  mch
 Lumpy Merge

 Revision 1.6.2.1  2004/10/15 19:59:05  mch
 Lots of changes during trip to CDS to improve int test pass rate

 Revision 1.6  2004/10/13 01:30:58  mch
 Added adqlsql (keeps CIRCLE)

 Revision 1.5  2004/10/12 22:45:45  mch
 Added spaces around operators so SqlParser can work with it

 Revision 1.4  2004/10/08 15:19:36  mch
 Removed unnecessary imports

 Revision 1.3  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.2  2004/10/06 22:24:58  mch
 Fixed wrong brackets in replaceRegion

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.1  2004/09/28 15:02:13  mch
 Merged PAL and server packages

 Revision 1.24  2004/09/08 21:55:14  mch
 Uncommented SQL/ADQL tests

 Revision 1.23  2004/09/07 13:22:26  mch
 Throws better error if ADQL 0.5 is submitted

 Revision 1.22  2004/09/07 02:28:29  mch
 Removed ADQL 0.5 tests

 Revision 1.21  2004/09/07 00:54:20  mch
 Tidied up Querier/Plugin/Results, and removed deprecated SPI-visitor-SQL-translator

 Revision 1.20  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.19  2004/08/24 20:08:31  mch
 Moved xslts to classpaths to fix ADQL-SQL translation problems, slightly improved query builder pages

 Revision 1.18  2004/08/24 19:06:44  mch
 Improvements to JSP pages, lots to query building and translating

 Revision 1.17  2004/08/24 12:55:09  mch
 Minor fixes to xslt translator

 Revision 1.16  2004/08/06 12:04:19  mch
 Added unit description to conesearch columns to cope with ESO milliarcseconds (& others in future)

 Revision 1.6.10.1  2004/08/05 17:57:08  mch
 Merging Itn06 fixes into Itn05

 Revision 1.15  2004/08/05 10:56:56  mch
 Fix for negative dec

 Revision 1.14  2004/08/05 09:52:59  mch
 Removed aql073 and added 08

 Revision 1.13  2004/08/02 11:35:03  mch
 Commented out incorrect square bounds maker

 Revision 1.12  2004/07/14 18:04:25  mch
 Fixed SQL/Angles

 Revision 1.11  2004/07/12 23:26:51  mch
 Fixed (somewhat) SQL for cone searches, added tests to Dummy DB

 Revision 1.10  2004/07/12 14:12:04  mch
 Fixed ADQL 0.7.4 xslt

 Revision 1.9  2004/07/07 19:33:59  mch
 Fixes to get Dummy db working and xslt sheets working both for unit tests and deployed

 Revision 1.8  2004/07/06 18:48:34  mch
 Series of unit test fixes

 Revision 1.7  2004/07/01 23:07:14  mch
 Introduced metadata generator

 Revision 1.6  2004/03/17 21:03:20  mch
 Added SQL transformation tests

 Revision 1.5  2004/03/17 18:03:20  mch
 Added v0.8 ADQL

 Revision 1.4  2004/03/17 01:47:26  mch
 Added v05 Axis web interface

 Revision 1.3  2004/03/14 16:55:48  mch
 Added XSLT ADQL->SQL support

 Revision 1.2  2004/03/12 20:04:57  mch
 It05 Refactor (Client)

 Revision 1.1  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 
 */



