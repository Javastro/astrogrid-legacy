/*$Id: StdSqlMaker.java,v 1.19 2004/08/24 20:08:31 mch Exp $
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

import java.io.*;

import java.util.StringTokenizer;
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
import org.astrogrid.datacenter.queriers.spi.Translator;
import org.astrogrid.datacenter.queriers.sql.deprecated.SqlQuerierSPI;
import org.astrogrid.datacenter.query.AdqlQuery;
import org.astrogrid.datacenter.query.ConeQuery;
import org.astrogrid.datacenter.query.QueryException;
import org.astrogrid.datacenter.sky.Angle;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Element;

/**
 * A 'standard' translator that creates 'standard' SQL
 */
public class StdSqlMaker  extends SqlMaker {

   private static final Log log = LogFactory.getLog(StdSqlMaker.class);

   /**
    * Constructs an SQL statement for the given cone query. Looks for the HTM
    * column first - if it finds it, uses that to do the cone search
    */
   public String fromCone(ConeQuery query) {
      if (SimpleConfig.getSingleton().getString(CONE_SEARCH_HTM_KEY, null) != null) {
         return getHtmSql(query);
      } else {
         return getRaDecSql(query);
      }
   }
   
   /** Returns the SQL suitable for doing a cone query on RA & DEC values */
   public String getRaDecSql(ConeQuery query) {

      String table = SimpleConfig.getSingleton().getString(CONE_SEARCH_TABLE_KEY);
      String alias = table.substring(0,1);
      
      //get which columns given RA & DEC for cone searches
      String raCol  = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_RA_COL_KEY);
      String decCol = alias+"."+SimpleConfig.getSingleton().getString(CONE_SEARCH_DEC_COL_KEY);
      
      Angle ra  = Angle.fromDegrees(query.getRa());
      Angle dec = Angle.fromDegrees(query.getDec());
      Angle radius = Angle.fromDegrees(query.getRadius());
      
      String sql = "SELECT * FROM "+table+" as "+alias+
         " WHERE "+
         //circle
         makeSqlCircleCondition(raCol, decCol, ra, dec, radius);
      
      log.info(query+" -> "+sql);
      
      return sql;
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

      //start with a square - for quicker searches
//      String sql = makeSqlBoundsCondition(raCol, decCol, ra, dec, radius);

      //naively, we could use the 'least squares' distance (pythagoros) to see if
      //the objects are within radius distance. However this doesn't work well
      //except very near the equator, and is useless over the poles. Left here
      //for reference.
      /*
      return sql+"( "+
            "(POWER("+decCol+" - "+dec.asDegrees()+", 2)"+
            "+"+
            "POWER("+raCol+" - "+ra.asDegrees()+", 2))"+
            " < "+
            "POWER("+radius.asDegrees()+", 2) "+
            ")";
       */
      
      //simple great-circle distance formulare.  Doesn't work well for... er... don't know
      //Left here for reference.
      /*
      return sql+" AND "+
         "( "+
           "( SIN("+decColRad+") * SIN("+dec.asRadians()+") + COS("+decColRad+") * COS("+dec.asRadians()+") * COS("+raColRad+" - "+ra.asRadians()+") )"+
           " < COS("+radius.asRadians()+")"+
         " )";
       /**/

      //'haversine' distance formulae.  The correct one to use...
      if (funcsInRads) {
         return
   //       makeSqlBoundsCondition(raCol, decCol, ra, dec, radius) + " AND "+  //not yet correct
            "("+
               "(2 * ASIN( SQRT( "+
                     //surround dec with extra brackets in order to cope with negative decs
                  "POWER( SIN( ("+decColRad+"-("+dec.asRadians()+") )/2 ) ,2) +"+
                     "COS("+dec.asRadians()+") * COS("+decColRad+") * "+
                     "POWER( SIN( ("+raColRad+"-"+ra.asRadians()+")/2 ), 2) "+
               "))) < "+radius.asRadians()+
            ")";
      }
      else {
         throw new UnsupportedOperationException("Not done degree funcs yet - do they exist?");
      }
      /**/
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
      
   
   
   
   /** Returns the SQL condition expression for a rectangle <i>in coordinate space</i>.
    * Use this when doing polygon/circle searches on reasonably small areas, to provide
    * some easy-to-check bounds for the db before it has to get on to the trig.
    * @param raCol, decCol - the column names that contain the RA & DEC values of the objects
    * @TODO Doesn't allow for wraparound RA
    *
   public String makeSqlBoundsCondition(String raCol, String decCol, Angle ra, Angle dec, Angle radius) {

      boolean colsInRadians = SimpleConfig.getSingleton().getBoolean(DB_COLS_IN_RADIANS);
   
      
      if (dec.asDegrees() - radius.asDegrees() < 0) {
         //circle includes north pole.  Add bounds as just a dec limit.
         if (colsInRadians) {
            return "("+decCol+"<"+(dec.asRadians()+radius.asRadians())+")";
         } else {
            return "("+decCol+"<"+(dec.asDegrees()+radius.asDegrees())+")";
         }
      }

      if (dec.asDegrees() + radius.asDegrees() > 180) {
         //circle includes south pole. Add bounds as just a dec limit
         if (colsInRadians) {
            return "("+decCol+">"+(dec.asRadians()-radius.asRadians())+")";
         } else {
            return "("+decCol+">"+(dec.asDegrees()-radius.asDegrees())+")";
         }
      }

      //work out ra angle on sky at center point.
      Angle raWidth = new Angle( radius.asDegrees() / Math.cos(dec.asRadians()));

      if (colsInRadians) {
         return "("+decCol+"<"+(dec.asRadians()+radius.asRadians())+" AND "+decCol+">"+(dec.asRadians()-radius.asRadians())+" AND"+
                  " "+ raCol+"<"+(ra.asRadians()+raWidth.asRadians())+" AND "+ raCol+">"+(ra.asRadians() -raWidth.asRadians())+")";
      } else {
         return "("+decCol+"<"+(dec.asDegrees()+radius.asDegrees())+" AND "+decCol+">"+(dec.asDegrees()-radius.asDegrees())+" AND"+
                  " "+ raCol+"<"+(ra.asDegrees()+raWidth.asDegrees())+" AND "+ raCol+">"+(ra.asDegrees() -raWidth.asDegrees())+")";
      }
      
      
   }
    /**/
   
   
   /** Returns the SQL suitable for doing a cone query on HTM-indexed catalogue */
   public String getHtmSql(ConeQuery query) {
      throw new UnsupportedOperationException();
   }
   
   /**
    * Constructs an SQL statement for the given ADQL
    */
   public String fromAdql(AdqlQuery query) throws QueryException {
      //should use appropriate xslt, but use deprecated stuff for now

      // find the translator
        Element queryBody = query.toDom().getDocumentElement();
        String namespaceURI = queryBody.getNamespaceURI();
        if (namespaceURI == null) {
            // maybe not using namespace aware parser - see if we can find an xmlns attribute instead
            namespaceURI = queryBody.getAttribute("xmlns");
        }
        if (namespaceURI == null) {
            DomHelper.PrettyElementToStream(queryBody,System.out);
            throw new IllegalArgumentException("Query body has no namespace - cannot determine language");
        }
        
        SqlQuerierSPI spi = new SqlQuerierSPI();
        Translator trans = spi.getTranslatorMap().lookup(namespaceURI);
        if (trans != null) {
           String sql = useSpi(queryBody, trans);
           log.debug("Used SPI "+trans+") to translate ADQL ("+namespaceURI+") to '"+sql+"'");
           return sql;
        }
        
        String sql = useXslt(queryBody, namespaceURI);
        
        sql = replaceRegion(sql);
        
        
        return sql;

     }
     
     
     /** Uses the SPI plugin to do the translations */
     public String useSpi(Element queryBody, Translator trans) throws QueryException {
        // do the translation
        Object intermediateRep = null;
        Class expectedType = null;
        try { // don't trust it.
            intermediateRep = trans.translate(queryBody);
        } catch (Exception t) {
            throw new QueryException("Translation phase failed:" + t.getMessage(),t);
        }
        //check return type
        expectedType = trans.getResultType();
        if (! expectedType.isInstance(intermediateRep)) { // checks result is non-null and the right type.
            throw new QueryException("Translation result " + intermediateRep.getClass().getName() + " not of expected type " + expectedType.getName());
        }
        
        return (String) intermediateRep;

   }
   
     /** Uses Xslt to do the translations */
   public String useXslt(Element queryBody, String namespaceURI) throws QueryException {

      if ((namespaceURI==null) || (namespaceURI.length()==0)) {
         throw new QueryException("No namespace specified in query document, so don't know what it is");
      }

      String xsltDoc = null;
      InputStream xsltIn = null;
      String whereIsDoc = null; //used for debug/trace/error messages
      
      //work out which translator sheet to use

      //see if there's a config property set
      String key = "datacenter.sqlmaker.xslt."+namespaceURI.replaceAll(":","_");
      xsltDoc = SimpleConfig.getSingleton().getString(key, null);

      try {
         if (xsltDoc != null) {
            //use config-specified sheet
            xsltIn = new FileInputStream(xsltDoc);
            whereIsDoc = "File "+xsltDoc;
         }
         else {
            //not given in configuration file - look in subdirectory of class as resource
            if (namespaceURI.equals("http://tempuri.org/adql")) { //assume v0.5
               xsltDoc = "adql05-2-sql.xsl";
            }
            else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.7.4")) {
               xsltDoc = "adql074-2-sql.xsl";
            }
            else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.7.3")) {
               xsltDoc = "adql073-2-sql.xsl";
            }
            else if (namespaceURI.equals("http://www.ivoa.net/xml/ADQL/v0.8")) {
               xsltDoc = "adql08-2-sql.xsl";
            }
            else if (namespaceURI.equals("http://astrogrid.org/sadql/v1.1")) {
               xsltDoc = "sadql1.1-2-sql.xsl";
            }
      
            if (xsltDoc == null) {
               throw new RuntimeException("No builtin xslt for ADQL namespace '"+namespaceURI+"'; set configuration key '" + key+"'");
            }
               
            //find specified sheet as resource of this class
            xsltIn = StdSqlMaker.class.getResourceAsStream("./xslt/"+xsltDoc);
            whereIsDoc = StdSqlMaker.class+" resource ./xslt/"+xsltDoc;
            
            if (xsltIn == null) {
               log.warn("Could not find builtin ADQL->SQL transformer doc '"+whereIsDoc+"', looking in classpath...");

               xsltIn = this.getClass().getClassLoader().getResourceAsStream(xsltDoc);
               whereIsDoc = xsltDoc+" in classpath at "+this.getClass().getClassLoader().getResource(xsltDoc);
            }
            
            if (xsltIn == null) {
               throw new QueryException("Could not find ADQL->SQL transformer doc "+xsltDoc);
            }
         }
         
         //create transformer
         log.debug("Transforming ADQL ["+namespaceURI+"] using Xslt doc at '"+whereIsDoc+"'");
         TransformerFactory tFactory = TransformerFactory.newInstance();
         try {
            tFactory.setAttribute("UseNamespaces", Boolean.FALSE);
         }
         catch (IllegalArgumentException iae) {
            //ignore - if UseNamepsaces is unsupported, it will chuck an exception, and
            //we don't want to use namespaces anyway so taht's fine
         }
         Transformer transformer = tFactory.newTransformer(new StreamSource(xsltIn));
         
         //transform
         StringWriter sw = new StringWriter();
         transformer.transform(new DOMSource(queryBody), new StreamResult(sw));
         String sql = sw.toString();
        
         //tidy it up - remove new lines and double spaces
         sql = sql.replaceAll("\n","");
         sql = sql.replaceAll("\r","");
         while (sql.indexOf("  ")>-1) { sql = sql.replaceAll("  ", " "); }
         
         //botch botch botch - for some reason transformers sometimes add <?xml tag to beginning
         if (sql.startsWith("<?")) {
            sql = sql.substring(sql.indexOf("?>")+2);
         }
         //botch botch botch - something funny with ADQL 0.7.3 schema to do with comparisons
         sql = sql.replaceAll("&gt;", ">").replaceAll("&lt;", "<");
         
         log.debug("Used '"+xsltDoc+"' to translate ADQL ("+namespaceURI+") to '"+sql+"'");
         
         return sql;
      }
      catch (TransformerConfigurationException tce) {
         throw new QueryException(tce+" (using xslt sheet "+xsltDoc+")",tce);
      }
      catch (TransformerException te) {
         throw new QueryException(te+" translating ADQL->SQL using "+xsltDoc,te);
      }
      catch (IOException ioe) {
         throw new QueryException(ioe+" Opening XSLT sheet "+xsltDoc,ioe);
      }

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
            double ra = Double.parseDouble(s.nextToken());
            double dec = Double.parseDouble(s.nextToken());
            double radius = Double.parseDouble(s.nextToken());
            
            return sql.substring(0,start-1)+
                     fromCone(new ConeQuery(ra, dec, radius))+
                     sql.substring(end);
            
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



