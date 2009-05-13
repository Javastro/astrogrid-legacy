/*
 * $Id: ConeConverter.java,v 1.1 2009/05/13 13:20:37 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.InputStreamReader;
import org.astrogrid.io.Piper;
import org.astrogrid.cfg.ConfigFactory;
import org.astrogrid.cfg.PropertyNotFoundException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Utility class for taking conesearch parameters and converting them
 * into an ADQL/sql query format.  Note that this class assumes that
 * the database table stores RA and DEC columns in decimal degrees.
 *
 * See notes about conesearch maths appended at end.
 *
 * @author K. Andrews, J. Lusted
 */

public class ConeConverter  {

     protected static Log log = LogFactory.getLog(ConeConverter.class);

     static final String CATALOG_NAME = "INSERT_NAME_CATALOG";
     static final String TABLE_NAME = "INSERT_NAME_TABLE";
     static final String RA_NAME = "INSERT_NAME_RA";
     static final String DEC_NAME = "INSERT_NAME_DEC";
     static final String RA_VALUE_RAD = "INSERT_VALUE_RA_RAD";
     static final String DEC_VALUE_RAD = "INSERT_VALUE_DEC_RAD";
     static final String RADIUS_VALUE_RAD = "INSERT_VALUE_CIRCRADIUS_RAD";
     static final String RA_VALUE_DEG = "INSERT_VALUE_RA_DEG";
     static final String DEC_VALUE_DEG = "INSERT_VALUE_DEC_DEG";
     static final String RADIUS_VALUE_DEG = "INSERT_VALUE_CIRCRADIUS_DEG";
     static final String DECMIN_VALUE_DEG = "INSERT_VALUE_MIN_DEC_DEG";
     static final String DECMAX_VALUE_DEG = "INSERT_VALUE_MAX_DEC_DEG";
     static final String DECMIN_VALUE_RAD = "INSERT_VALUE_MIN_DEC_RAD";
     static final String DECMAX_VALUE_RAD = "INSERT_VALUE_MAX_DEC_RAD";
     static final String RA_CLIP_CONDITION = "INSERT_RA_CLIP_CONDITION";

     static final double CONVERT_FACTOR = Math.PI/180.0;
         
   /** 
    * Creates an ADQL conesearch query string reflecting specified 
    * table and column names, and specified search RA, Dec and radius.
    * This function expects its numeric inputs in decimal degrees.
    */
   public static String getAdql(
       String catalogName, String tableName, String colUnits,
       String raColName, String decColName,
       double coneRA, double coneDec, double coneRadius) throws QueryException 
   {
      String adqlString;
      boolean funcsInRads;
      boolean colsInDegs;

      // Check all inputs thoroughly
      if (colUnits == null || "".equals(colUnits)) {
        throw new QueryException(
            "Column units may not be empty, must be one of 'deg' or 'rad'");
      }
      if (
            !"deg".equals(colUnits) && !"rad".equals(colUnits) &&
            !"degrees".equals(colUnits) && !"radians".equals(colUnits)
      ) {
        throw new QueryException(
            "Column units must be one of 'deg' or 'rad'");
      }
      if ((coneRA < 0.0) || (coneRA > 360.0)) {
        throw new QueryException(
            "Illegal value for conesearch right ascension: " +
            Double.toString(coneRA)
        );
      }
      if ((coneDec < -90.0) || (coneDec > 90.0)) {
         throw new QueryException(
           "Illegal value for conesearch declination: " +
           Double.toString(coneDec)
        );
      }
      if (coneRadius < 0.0) {
        throw new QueryException(
            "Illegal value for conesearch radius (must be positive): " + 
            Double.toString(coneRadius)
        );
      }
      if ((catalogName == null) || (catalogName.equals(""))) {
        throw new QueryException(
            "Illegal null or empty string for conesearch catalog name");
      }
      if ((tableName == null) || (tableName.equals(""))) {
        throw new QueryException(
            "Illegal null or empty string for conesearch table name");
      }
      if ((raColName == null) || (raColName.equals(""))) {
        throw new QueryException(
            "Illegal null or empty string for RA column name");
      }
      if ((decColName == null) || (decColName.equals(""))) {
        throw new QueryException(
            "Illegal null or empty string for RA column name");
      }

      try {
         funcsInRads = ConfigFactory.getCommonConfig().getBoolean(
            "db.trigfuncs.in.radians");
      }
      catch (PropertyNotFoundException nfe) {
         throw new QueryException(
            "DSA local property 'db.trigfuncs.in.radians' is not set, " +
            "please check your configuration");
      }
      if ( colUnits.equals("deg") || colUnits.equals("degrees") ) {
         colsInDegs = true;
      }
      else if ( colUnits.equals("rad") || colUnits.equals("radians") ) {
         colsInDegs = false;
      }
      else {
         // Shouldn't get here
         throw new QueryException(
            "Unrecognised value " + colUnits + 
            " for conesearch column units', " +
            "should be 'deg' or 'rad'");
      }
      // Now check for input radius limit
      String radiusLimitString;
      double radiusLimit;
      try {
         radiusLimitString = ConfigFactory.getCommonConfig().getString("conesearch.radius.limit");
      }
      catch (PropertyNotFoundException nfe) {
         throw new QueryException(
            "DSA local property 'conesearch.radius.limit' is not set, " +
            "please check your configuration");
      }
      if ("0".equals(radiusLimitString)) {
         radiusLimit = 360.0; // No limit, effectively
      }
      else {
        try {
          radiusLimit = Double.parseDouble(radiusLimitString); 
        }
        catch (NumberFormatException nfe) {
          throw new QueryException(
            "Unrecognised value " + radiusLimitString + 
            " for property 'conesearch.radius.limit', " +
            "should be a valid decimal number (expecting decimal degrees).");
        }
      }
      if (coneRadius > radiusLimit) {
          throw new QueryException(
            "Input conesearch radius " + Double.toString(coneRadius) + 
            " is too large;  maximum allowed radius is "+ Double.toString(radiusLimit));
      }

      // These are for use in an initial box-cut filter in the ADQL
      // (to make for more efficient calculation) - NB IN DEGREES HERE
      double decMin = coneDec - coneRadius;
      double decMax = coneDec + coneRadius;

      // This lifted from uk.ac.starlink.ttools.cone.JdbcConer
      double deltaRa = 
          Math.toDegrees( calculateDeltaRa( 
                   Math.toRadians( coneRA ),
                   Math.toRadians( coneDec ),
                   Math.toRadians( coneRadius ) ) );

      double raMin = coneRA - deltaRa;
      double raMax = coneRA + deltaRa;

      double colsRaMin = raMin;
      double colsRaMax = raMax;
      if (colsInDegs == false) {
        // Convert RA clip bounds to radians for actual substitution
        // into formula
        colsRaMin = colsRaMin * CONVERT_FACTOR;
        colsRaMax = colsRaMax * CONVERT_FACTOR;
      }

      String raClipCondition = "";
      if (deltaRa >= 180) {
        // Forget about RA clipping in this case, might as well do
        // the lot.
        log.debug("Ignoring RA boxcut for huge search radius");
        raClipCondition = "";
      }
      else if ((raMin >= 0.0) && (raMax < 360.0)) {
        log.debug("Got simple RA boxcut");
        // Ahh, nice simple boxcut here!
        raClipCondition = " AND ( (a.INSERT_NAME_RA >= " + colsRaMin + ") AND " +
            "(a.INSERT_NAME_RA <= " + colsRaMax + ") )";
      }
      else if (raMin < 0.0) {
        // Wrapping leftwards :  <   |0|   *        >
        // Two cases here: 
        //   (ra > 360+raMin) OR (ra <= raMax)
        log.debug("Got RA boxcut left wrap");
        double realMin = 360.0 + raMin;   // A value less than 360.0
        if (colsInDegs == false) {
           realMin = realMin * CONVERT_FACTOR;
        }
        raClipCondition = " AND ( " +
          "(a.INSERT_NAME_RA >= "+ Double.toString(realMin) + ") OR " +
            "(a.INSERT_NAME_RA <= " + colsRaMax + ") )";
      }
      else if (raMax >= 360.0) {
        // Wrapping rightwards :  <       *    |0|   >
        // Two cases here: 
        //   (ra >= raMin) OR (ra < raMax-360) 
        log.debug("Got RA boxcut right wrap");
        double realMax = raMax-360.0;   // A value greater than 0
        if (colsInDegs == false) {
          realMax = realMax * CONVERT_FACTOR;
        }
        raClipCondition = " AND ( " +
          "(a.INSERT_NAME_RA >= "+ Double.toString(colsRaMin) + ") OR " +
          "(a.INSERT_NAME_RA <= " + Double.toString(realMax) + ") )";
      }
      else {
        //SHOULDN'T GET HERE!
         throw new QueryException(
            "Got unexpected conditions in conesearch - this is a bug, please advise of it at http://www.astrogrid.org/bugzilla");
      }
 
      if (coneRadius < 0.167) { // < roughly 10 arcmin
          // Haversine is more accurate for small circles
          // (avoids rounding-related errors)
          if (funcsInRads) {
             if (colsInDegs) {
                adqlString = getConeTemplate("adql/templates/cone_haversine_rad_deg.sql");
             }
             else {
               adqlString = getConeTemplate("adql/templates/cone_haversine_rad_rad.sql");
            }
          } 
          else {
             if (colsInDegs) {
                adqlString = getConeTemplate("adql/templates/cone_haversine_deg_deg.sql");
             }
             else {
                adqlString = getConeTemplate("adql/templates/cone_haversine_deg_rad.sql");
            }
          }
      }
      else {
         // Great circle ok for larger circles
         if (funcsInRads) {
            if (colsInDegs) {
               adqlString = getConeTemplate("adql/templates/cone_greatcircle_rad_deg.sql");
            }
            else {
               adqlString = getConeTemplate("adql/templates/cone_greatcircle_rad_rad.sql");
            }
         }
         else {
            if (colsInDegs) {
               adqlString = getConeTemplate("adql/templates/cone_greatcircle_deg_deg.sql");
            }
            else {
               adqlString = getConeTemplate("adql/templates/cone_greatcircle_deg_rad.sql");
            }
         }
      }

      //System.out.println("ADQL BEFORE SUBSTITUTION: ");
      //System.out.println(adqlString);

      // Perform necessary substitutions
      
      // First of all, insert RA clip condition
      adqlString = adqlString.replaceAll(RA_CLIP_CONDITION,raClipCondition);
      adqlString = adqlString.replaceAll(CATALOG_NAME,catalogName);
      adqlString = adqlString.replaceAll(TABLE_NAME,tableName);
      adqlString = adqlString.replaceAll(RA_NAME,raColName);
      adqlString = adqlString.replaceAll(DEC_NAME,decColName);

      // These go in as whatever the trig functions require (degrees or rads)
      if (funcsInRads) {
        // Convert input values into radians for trig
        double coneRARad = coneRA * CONVERT_FACTOR;
        double coneDecRad = coneDec * CONVERT_FACTOR;
        double coneRadiusRad = coneRadius * CONVERT_FACTOR;
        adqlString = adqlString.replaceAll(
            RA_VALUE_RAD, Double.toString(coneRARad));
        adqlString = adqlString.replaceAll(
            DEC_VALUE_RAD, Double.toString(coneDecRad));
        adqlString = adqlString.replaceAll(
            RADIUS_VALUE_RAD,Double.toString(coneRadiusRad));
      }
      else {
        adqlString = adqlString.replaceAll(
            RA_VALUE_DEG, Double.toString(coneRA));
        adqlString = adqlString.replaceAll(
            DEC_VALUE_DEG, Double.toString(coneDec));
        adqlString = adqlString.replaceAll(
            RADIUS_VALUE_DEG, Double.toString(coneRadius));
      }

      if (colsInDegs) {
        adqlString = adqlString.replaceAll(
            DECMIN_VALUE_DEG,Double.toString(decMin));
        adqlString = adqlString.replaceAll(
            DECMAX_VALUE_DEG,Double.toString(decMax));
      }
      else {
        double decMinRad = decMin * CONVERT_FACTOR;
        double decMaxRad = decMax * CONVERT_FACTOR;
        adqlString = adqlString.replaceAll(
            DECMIN_VALUE_RAD,Double.toString(decMinRad));
        adqlString = adqlString.replaceAll(
            DECMAX_VALUE_RAD,Double.toString(decMaxRad));
      }
      //System.out.println("ADQL AFTER SUBSTITUTION: ");
      //System.out.println(adqlString);

      return adqlString;
   }

   /** 
    * Extracts the XML template file resources for use in constructing
    * conesearch queries.
    */
   protected static String getConeTemplate(String filename) throws QueryException
   {
      InputStream templateIn = null;
      if ((filename == null) || (filename.trim().equals(""))) {
         throw new QueryException(
             "Null/empty name specified for test query to run");
      }
      //find specified sheet as resource of this class
      templateIn = ConeConverter.class.getResourceAsStream(
          "./" + filename);
      String whereIsDoc = ConeConverter.class+" resource " +
          "./" + filename;
      //System.out.println("Trying to load test query as resource of class: " +whereIsDoc);

      //if above doesn't work, try doing by hand for Tomcat ClassLoader
      if (templateIn == null) {
         // Extract the package name, turn it into a filesystem path by 
         // replacing .'s with /'s, and append the path to the xslt.
         // Hopefully this will mean tomcat can locate the file within
         // the bundled jar file.  
         String path = "java/" + 
           ConeConverter.class.getPackage().getName().replace('.', '/')
               + "/" + filename;
         templateIn = ConeConverter.class.getClassLoader().getResourceAsStream(path);
         //System.out.println("Trying to load test query via classloader : " +path);
      }

      //Last ditch, look in class path.  
      if (templateIn == null) {
         //System.out.println("Could not find test query '"
          //   +whereIsDoc+"', looking in classpath...");
         templateIn = 
           ConeConverter.class.getClassLoader().getResourceAsStream(filename);
         whereIsDoc = filename+" in classpath at "+
           ConeConverter.class.getClassLoader().getResource(filename);
         //System.out.println("Trying to load test query  via classpath : " +whereIsDoc);
      }
      if (templateIn == null) {
          throw new QueryException(
              "Could not find conesearch template "+filename);
      }
      // Now we have the query, let's get it as a string.
      String adqlString = null;
      try {
         StringWriter sw = new StringWriter();
         Piper.bufferedPipe(new InputStreamReader(templateIn), sw);
         adqlString = sw.toString();
      }
      catch (IOException e) {
        throw new QueryException(
            "Couldn't load conesearch template " + filename +
            ": " + e.getMessage(), e);
      }
      return adqlString;
   }


    /**
     * Works out the minimum change in Right Ascension which will encompass
     * all points within a given search radius at a given central ra, dec.
     * This is lifted verbatim from the STIL class
     * uk.ac.starlink.ttools.cone.JdbcConer.
     *
     * @param   ra   right ascension of the centre of the search region
     *               in radians
     * @param   dec  declination of the centre of the search region 
     *               in radians
     * @param   sr   radius of the search region in radians
     * @return  minimum change in radians of RA from the central value
     *          which will contain the entire search region
     */
    public static double calculateDeltaRa( double ra, double dec, double sr ) {
 
        /* Get the arc angle between the pole and the cone centre. */
        double hypArc = Math.PI / 2 - Math.abs( dec );

        /* If the search radius is greater than this, then all right 
         * ascensions must be included. */
        if ( sr >= hypArc ) {
            return Math.PI;
        }
        /* In the more general case, we need a bit of spherical trigonometry.
         * Consider a right spherical triangle with one vertex at the pole,
         * one vertex at the centre of the search circle, and the right angle
         * vertex at the tangent between the search circle and a line of
         * longitude; then apply Napier's Pentagon.  The vertex angle at the
         * pole is the desired change in RA. */
        return Math.asin( Math.cos( Math.PI / 2 - sr ) / Math.sin( hypArc ) );
    }


}

/* NOTES ABOUT CONESEARCH MATHS
 *
 * SEE http://www2.sjsu.edu/faculty/watkins/sphere.htm
 * ---------------------------------------------------
 *
 *   Standard great circle angular separation formula :
 *
 *     cos(A) = sin(DEC_C)*sin(DEC) + (cos(DEC_c)*cos(Dec)*cos(RA_c-RA));
 *
 *   Sinnott's haversign formula (for where A is small) :
 *
 *    sin(A/2) = sqrt ( sin^2((DEC_C-DEC)/2) + (COS(DEC_C)*COS(DEC)*
 *       (sin^2((RA_C-RA)/2))) );
 *
 *
 * SEE http://www.cs.nyu.edu/visual/home/proj/tiger/gisfaq.html)
 * -------------------------------------------------------------
 *
 *   Law of Cosines for Spherical Trigonometry
 *
 *   a = sin(lat1) * sin(lat2)
 *   b = cos(lat1) * cos(lat2) * cos(lon2 - lon1)
 *   c = arccos(a + b)
 *   d = R * c
 *
 *   Although this formula is mathematically exact, it is unreliable for
 *   small distances because the inverse cosine is ill-conditioned. Sinnott
 *   (in the article cited above) offers the following table to illustrate
 *   the point:
 *   cos (5 degrees) = 0.996194698
 *   cos (1 degree) = 0.999847695
 *   cos (1 minute) = 0.9999999577
 *   cos (1 second) = 0.9999999999882
 *   cos (0.05 sec) = 0.999999999999971
 *   A computer carrying seven significant figures cannot distinguish the
 *   cosines of any distances smaller than about one minute of arc.
 *   The function min(1,(a + b)) could replace (a + b) as the argument for
 *   the inverse cosine to guard against possible round-off errors, but
 *   doing so would be to "polish a cannonball". 
 *
 *   Haversine formula again:
 *    dlon = lon2 - lon1
 *    dlat = lat2 - lat1
 *    a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
 *    c = 2 * arcsin(min(1,sqrt(a)))
 *    d = R * c 
 * (the min here prevents arcsin crashing due to sqrt(a) >1 due to rounding
 *
 *  The Haversine Formula can be expressed in terms of a two-argument
 *  inverse tangent function, atan2(y,x), instead of an inverse sine
 *  as follows (no bulletproofing is needed for an inverse tangent):
 *
 *  dlon = lon2 - lon1
 *  dlat = lat2 - lat1
 *  a = (sin(dlat/2))^2 + cos(lat1) * cos(lat2) * (sin(dlon/2))^2
 *  c = 2 * atan2( sqrt(a), sqrt(1-a) )
 *  d = R * c
 *
 *  In this context, a one-argument inverse tangent function would also
 *  work: replace "atan2( sqrt(a), sqrt(1-a) )" by "arctan( sqrt(a) /
 *  sqrt(1-a) )", but insert a test to ensure you will not divide by
 *  zero if a = 1. (In the case a = 1, c = pi radians = 180 degrees,
 *  and d is halfway around the Earth = 3.14159265... * R.) 
 *
 * NOTE THAT mysql (and other DBMSs presumably) EXPECT TRIG FUNCTION
 * ARGUMENTS IN RADIANS, NOT DEGREES!! 
 *
  // THESE ARE EXAMPLES FROM THE OLD DSA
SELECT *
 FROM RASS_PHOTONS
 WHERE 2 * ASIN( SQRT(SIN(($DEC_C-DEC)/2) 
    * SIN(($DEC_C-DEC)/2) + COS($DEC_C) * COS(DEC) 
    * SIN(($RA_C - RA)/2) * SIN(($RA_C - RA)/2))) <= $SR_C  
SELECT  *  FROM  catalogue  WHERE (
    (catalogue.POS_EQ_DEC<2.5)  and
    (catalogue.  POS_EQ_DEC>1.5) ) AND
  ((2 * ASIN( SQRT( POWER( SIN( (RADIANS(catalogue.POS_EQ_DEC) -
  (0.03490658503988659) ) / 2 ) ,2) +
  COS(0.03490658503988659) * COS(RADIANS(catalogue.POS_EQ_DEC)) *
  POWER( SIN( (RADIANS(catalogue.POS_EQ_RA) - 0.017453292519943295) / 2 ), 2)
  )))< 0.008726646259971648)
*/
