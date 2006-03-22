/*
 * $Id: StdSqlWriter.java,v 1.9 2006/03/22 15:10:13 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.tableserver.jdbc;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.cfg.ConfigException;
import org.astrogrid.dataservice.metadata.queryable.ConeConfigQueryableResource;
import org.astrogrid.dataservice.metadata.queryable.SearchField;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.geom.Angle;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.adql.AdqlXml074Parser;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.constraint.ConstraintSpec;
import org.astrogrid.query.refine.RefineSpec;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.tableserver.metadata.TableInfo;
import org.astrogrid.tableserver.metadata.TableMetaDocInterpreter;
import org.astrogrid.units.Units;
import org.xml.sax.SAXException;


/**
 * For writing out Querys as SQL statment strings, as close as we can to 'standard SQL'.
 */

public class StdSqlWriter implements QueryVisitor {
   
   

   protected static Log log = LogFactory.getLog(Adql074Writer.class);
   
   private DecimalFormat longDec = new DecimalFormat("#############################0");

   /** SQL String components being built */
   protected StringBuffer select = new StringBuffer();
   protected StringBuffer from = new StringBuffer();
   protected StringBuffer where = new StringBuffer();
   protected StringBuffer orderby = new StringBuffer();
   
   /* Because the methods are stateless, we have to set which stringbuffer we
    * are working on - this is needed for visiting eg column references which
    * appear in seleft, where, order by, etc */
   protected StringBuffer current = null;
   
   /** Query being written */
   Query query = null;
   
   /** Root write call */
   public void visitQuery(Query queryToWrite)  throws IOException  {
      query = queryToWrite;
      log.debug("Making SQL from "+query.toString());
      
      //--- SELECT, ORDER BY ---
      visitReturnSpec(query.getResultsDef());
      visitLimit(query.getLocalLimit());
      
      //-- FROM ---
      visitScope(query.getScope(), query);

      //-- WHERE --
      if (query.getCriteria() != null) {
      
         current = where;
         query.getCriteria().acceptVisitor(this);
      }
      
   }

   public String getSql() {
      String sql = "SELECT "+select.toString()+" FROM "+from.toString();
      if (where.toString().trim().length()>0) {
         sql = sql +" WHERE "+where.toString();
      }
      if (orderby.toString().trim().length()>0) {
         sql = sql + " ORDER BY "+orderby.toString();
      }
      return sql;
   }
   
   public void visitScope(String[] scope, Query query) {

      current = from;

      //if there is no scope given, we default to all the tables in the database
      if ((scope == null) || (scope.length==0)) {
         try {
            TableMetaDocInterpreter interpreter = new TableMetaDocInterpreter();
            TableInfo[] tables = interpreter.getTables(interpreter.getCatalogs()[0]);
            scope = new String[tables.length];
            for (int i = 0; i < tables.length; i++) {
               scope[i] = tables[i].getName();
            }
         }
         catch (IOException ioe) {
            log.error(ioe+" loading metadoc", ioe);
            throw new RuntimeException(ioe);
         }
      }
      
      // list out the table names.  We don't use aliases as this stage - use table names in the query
      for (int i = 0; i < scope.length; i++) {
      /*
         String alias = scope[i];
         if (query.getAlias(alias) != null) {
            alias = query.getAlias(alias);
         }
         sql.append(" "+query.getScope()[i]+" as "+alias+" ");
       */
         from.append(" "+scope[i]+" ");
         if (i<scope.length-1) {
            from.append(",");
         }
      }
   }

   public void visitReturnSpec(ReturnSpec spec) throws IOException  {
 
      current = select; //so called visitors know what we're visiting
      
      if (spec instanceof ReturnTable) {
         if (((ReturnTable) spec).getColDefs()==null)  {
            current.append(" * ");
         }
         else {
            Expression[] colDefs = ((ReturnTable) spec).getColDefs();
   
            for (int i = 0; i < colDefs.length; i++) {
               colDefs[i].acceptVisitor(this);
               if (i<colDefs.length-1) {
                  current.append(", ");
               }
            }
         }
         
         if (((ReturnTable) spec).getSortOrder() != null) {
            current.append(" ORDER BY ");
            Expression[] sortCols = ((ReturnTable) spec).getSortOrder();
   
            for (int i = 0; i < sortCols.length; i++) {
               sortCols[i].acceptVisitor(this);
               if (i<sortCols.length-1) {
                  current.append(", ");
               }
            }
         }
      }
   }

   /** Standard SQL can't handle this, so we leave it blank */
   public void visitLimit(long limit) {
   }
   
   public void visitIntersection(Intersection expression)  throws IOException {
         current.append(" (");
         Condition[] conditions = expression.getConditions();
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
            if (i<conditions.length-1) {
               current.append(" AND ");
            }
         }
         current.append(") ");
    }
    
    public void visitUnion(Union expression)  throws IOException {
         current.append(" (");
         Condition[] conditions = ((Union) expression).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
            if (i<conditions.length-1) {
               current.append(" OR ");
            }
         }
         current.append(") ");
    }

      
   public void visitNumericComparison(NumericComparison expression)  throws IOException {

       expression.getLHS().acceptVisitor(this);
       current.append( expression.getOperator().toString());
       expression.getRHS().acceptVisitor(this);
   }

   public void visitStringComparison(StringComparison expression)  throws IOException {
      
      expression.getLHS().acceptVisitor(this);
      current.append( expression.getOperator().toString());
      expression.getRHS().acceptVisitor(this);
   }
   
   public void visitNumber(LiteralNumber expression)  {
      current.append( " ("+expression.getValue()+") ");  //surround with brackets as negative numbers sometimes don't work very well without
   }

   public void visitAngle(LiteralAngle expression)  {
      current.append( " ("+expression.getAngle().asDegrees()+") ");
   }

   public void visitDate(LiteralDate date) throws IOException {
      current.append( " "+date.getDate()+" ");
   }
   
   public void visitString(LiteralString string) {
      current.append( " '"+string.getValue()+"' ");
   }

   public void visitRawSearchField(RawSearchField field)  {
      current.append( " "+field.getField()+" "); //bit of a botch - should throw an exception?
   }

   public void visitMath(MathExpression math)   throws IOException {
         math.getLHS().acceptVisitor(this);
         current.append( " "+math.getOperator()+" ");
         math.getRHS().acceptVisitor(this);
   }
   
   public void visitFunction(Function function)  throws IOException {
      
      if (function.getName().trim().toUpperCase().equals(CircleCondition.NAME.toUpperCase())) {
         visitCircle(CircleCondition.makeCircle(function));
         return;
      }
      
      current.append(" "+function.getName()+"(");
      
      for (int i = 0; i < function.getArgs().length; i++) {
         ((Expression) function.getArg(i)).acceptVisitor(this);
         if (i<function.getArgs().length-1) {
            current.append(", ");
         }
      }
      current.append(")");
   }
   
   /** Writes out the ADQL tag for the given column as a child of the given parentTag with
    * the given elementName */
   public void visitColumnReference(ColumnReference colRef) {

      current.append(" ");
      String tableName = colRef.getTableName();
      //replace with alias is there is one
//no don't - just use full table name      if (query.getAlias(tableName) != null) {
//         tableName = query.getAlias(tableName);
//      }

      
      if ((colRef.getDatasetName() != null) && (colRef.getDatasetName().trim().length()>0)) {
         current.append(colRef.getDatasetName()+":");
      }
      if ((tableName != null) && (tableName.trim().length()>0)) {
         current.append(tableName+".");
      }

      current.append(colRef.getColName()+" ");
   }

   /** @TOFIX-KEA ADD CONTENTS HERE! */
   public void visitRefineSpec(RefineSpec refineSpec)
   {
   }

   /** @TOFIX-KEA ADD CONTENTS HERE! */
   public void visitConstraintSpec(ConstraintSpec constraintSpec)
   {
   }
   
   /** might be overridden? **/
   public void visitCircle(CircleCondition circleFunc) throws IOException {

      Angle ra;
      Angle dec;
      Angle radius;
      
      try {
         ra = ((LiteralAngle) circleFunc.getArg(1)).getAngle();
         dec = ((LiteralAngle) circleFunc.getArg(2)).getAngle();
   
         radius = ((LiteralAngle) circleFunc.getArg(3)).getAngle();
      }
      catch (ClassCastException cce) {
         //assume it's the circleFunc args
         throw new UnsupportedOperationException("CIRCLE arguments must be LiteralAngles ("+cce+")");
      }

      ConeConfigQueryableResource queryable = new ConeConfigQueryableResource();

      String[] circleScope = query.getScope();
      
      //if no scope has been given, it's a 'blanket' cone search, so we set the
      //scope to all the spatial group table names.
      if ((circleScope == null) || (circleScope.length == 0)) {
         SearchGroup[] tables = queryable.getSpatialGroups();
         String[] names = new String[tables.length];
         for (int i = 0; i < tables.length; i++) {
            names[i] = tables[i].getName();
         }
         circleScope = names;
      }
      
      //go through spatial groups checking against scope looking to see if any of them have any spatial fields
      SearchGroup[] tables = queryable.getSpatialGroups();
      boolean spatialFound = false; //marker that we've found at least one to search on
      for (int i = 0; i < tables.length; i++)
      {
         //scope given, so look through only those for the ones that may be spatial
         for (int j = 0; j < circleScope.length; j++)
         {
            if (tables[i].getId().equals(circleScope[j])) {
               SearchField[] cols = queryable.getSpatialFields(tables[i]);
               if (cols != null) {
                  if (spatialFound) { //a previous one was found, so add an OR to search this one too
                     current.append(" OR ");
                  }
                  spatialFound = true; // found one
   
                  //assume, for the moment, that we get two columns, the first is RA and the
                  //second is DEC
                  current.append(makeSqlCircleCondition(cols[0], cols[1], ra, dec, radius));
               }
            }
         }
      }
   }
   
   /** Returns the SQL condition expression for a circle.  This circle is
    * 'flat' on the sphere, when vieweing the sphere from the center.  ie, the
    * radius is a declination angle from the given RA & DEC point.  This means
    * the circle is distorted in coordinate space
    */
   public String makeSqlCircleCondition(SearchField raCol, SearchField decCol, Angle ra, Angle dec, Angle radius) {
      //Some db functions take radians, some degrees.  Fail if the config is not
      //specified, so we force people to get it right...
      boolean funcsInRads = true; //ConfigFactory.getCommonConfig().getBoolean(SqlMaker.DB_TRIGFUNCS_IN_RADIANS);
      
      String raColRad = makeColumnRadiansId(raCol);
      String decColRad = makeColumnRadiansId(decCol);
      
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
            "POWER( SIN( ("+decColRad+" - ("+ dec.asRadians()+") ) / 2 ) ,2) + "+
            "COS("+dec.asRadians()+") * COS("+decColRad+") * "+
            "POWER( SIN( ("+raColRad+" - "+ ra.asRadians()+") / 2 ), 2) "+
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
    public String makeSqlBoundsCondition(SearchField raCol, SearchField decCol, Angle ra, Angle dec, Angle radius) {

      String decColSqlId = decCol.getGroup()+"."+decCol.getName();
      
      if (dec.asDegrees() - radius.asDegrees() < -90) {
         //circle includes north pole.  Add bounds as just a dec limit.
         return "("+decColSqlId+"<"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()+radius.asRadians()), decCol.getUnits())+")";
      }
   
      if (dec.asDegrees() + radius.asDegrees() > +90) {
         //circle includes south pole. Add bounds as just a dec limit
         return "("+decColSqlId+">"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()-radius.asRadians()), decCol.getUnits())+")";
      }

      //make upper and lower limit
      return "( ("+decColSqlId+"<"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()+radius.asRadians()), decCol.getUnits())+") "+
            " and "+
              " ("+decColSqlId+">"+getAngleInColUnits(Angle.fromRadians(dec.asRadians()-radius.asRadians()), decCol.getUnits())+") )";
    
    }
   
   
   /**
    * Returns the right SQL to translate a conesearch column to radians
    */
   public String makeColumnRadiansId(SearchField col) {
      Units colUnits = col.getUnits();
       String sqlColId = col.getGroup()+"."+col.getName();
      
      if (colUnits.equals("rad")) {
         return sqlColId;
      }
      else if (colUnits.equals("deg")) {
         return "RADIANS("+sqlColId+")";
      }
      else if (colUnits.equals("marcsec")) {
         return "RADIANS("+sqlColId+"/360000000)";
      }
      else {
         throw new ConfigException("Unknown units '"+colUnits+"' for conesearch columns, only 'rad', 'deg' or 'marcsec' supported");
      }
   }
   
   /** Returns the given angle in the column's units.   Useful for making SQL a bit
    * simpler - convert the angles to the column units rather than vice versa with functions */
   public String getAngleInColUnits(Angle givenAngle, Units colUnits) {
      /* @todo - note that the (float) typecasts are for postgres that can't cope with subtracting double precisions from single */
      if (colUnits.equals("rad")) {
         return ""+(float) givenAngle.asRadians();
      }
      else if (colUnits.equals("deg")) {
         return ""+(float) givenAngle.asDegrees();
      }
      else if (colUnits.equals("marcsec")) {
         //this gives 2.3E8 etc which can cause confusion return ""+givenAngle.asArcSecs()*1000);
         //return givenAngle.asArcSecs()+"000"; //...and this is highly dubious, but means we will usually get 230000000 instead
         return longDec.format(givenAngle.asDegrees()*1000);
         
      }
      else {
         throw new ConfigException("Unknown units '"+colUnits+"' for conesearch columns, only 'rad', 'deg' or 'marcsec' supported");
      }
   }
   
   /**
    * Easy test/debug
    */
   public static void main(String[] args) throws IOException, IOException, ParserConfigurationException, SAXException {

      /*
      String sql =
         "SELECT CrossNeighboursEDR.sdssID, ReliableStars.objID, \n"+
         "       ReliableStars.ra, ReliableStars.dec, \n"+
         "       ReliableStars.sCorMagR2, ReliableStars.sCorMagI,\n"+
         "       ReliableStars.sCorMagB, ReliableStars.sigMuD,\n"+
         "       ReliableStars.sigMuAcosD, ReliableStars.muD,\n"+
         "       ReliableStars.muAcosD \n"+
         "FROM ReliableStars, CrossNeighboursEDR \n"+
         "WHERE (CrossNeighboursEDR.ssaID = ReliableStars.objID) AND \n"+
         "      (CrossNeighboursEDR.SdssPrimary = 1) AND \n"+
         "      (CrossNeighboursEDR.sdsstype = 6) AND \n"+
         "      (ReliableStars.ra >= 0) AND \n"+
         "      (ReliableStars.ra <= 1) AND \n"+
         "      (ReliableStars.dec >= 2) AND \n"+
         "      (ReliableStars.dec <= 3) AND \n"+
         "      (ReliableStars.sCorMagR2 > -99) AND \n"+
         "      (ReliableStars.sCorMagI > -99) AND \n"+
         "      (POWER(muAcosD,2) + POWER(muD,2) > 4 * \nSQRT(POWER(muAcosD * sigMuAcosD,2) + POWER(muD * sigMuD,2)))   \n";
       */
      
      String sql = "select t1.decdeg from dqc as t1 where t1.decdeg > 60.0";
      
      Query q = SqlParser.makeQuery(sql);
      
      String adql = Adql074Writer.makeAdql(q);
      
      System.out.println(adql);
      
      q = AdqlXml074Parser.makeQuery(adql);

      System.out.println(q);

      StdSqlWriter sqlMaker = new StdSqlWriter();
      q.acceptVisitor(sqlMaker);
      
      String sql2 = sqlMaker.getSql();
      
      System.out.println(sql2);
      
      //make sure it's reparsable
      SqlParser.makeQuery(sql2);
   }
}

/*
 $Log: StdSqlWriter.java,v $
 Revision 1.9  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.8.62.2  2006/02/20 19:42:08  kea
 Changes to add GROUP-BY support.  Required adding table alias field
 to ColumnReferences, because otherwise the whole Visitor pattern
 falls apart horribly - no way to get at the table aliases which
 are defined in a separate node.

 Revision 1.8.62.1  2006/02/16 17:13:05  kea
 Various ADQL/XML parsing-related fixes, including:
  - adding xsi:type attributes to various tags
  - repairing/adding proper column alias support (aliases compulsory
     in adql 0.7.4)
  - started adding missing bits (like "Allow") - not finished yet
  - added some extra ADQL sample queries - more to come
  - added proper testing of ADQL round-trip conversions using xmlunit
    (existing test was not checking whole DOM tree, only topmost node)
  - tweaked test queries to include xsi:type attributes to help with
    unit-testing checks

 Revision 1.8  2005/05/27 16:21:04  clq2
 mchv_1

 Revision 1.7.6.2  2005/05/04 10:24:33  mch
 fixes to tests

 Revision 1.7.6.1  2005/04/29 16:55:47  mch
 prep for type-fix for postgres

 Revision 1.7  2005/04/01 10:33:52  mch
 more temporary fixes for postgres

 Revision 1.6  2005/03/31 23:16:32  mch
 temp fix for postgres

 Revision 1.5  2005/03/31 16:09:40  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.4  2005/03/31 15:06:16  mch
 Fixes and workarounds for null values, misisng metadoc columns

 Revision 1.3  2005/03/24 17:50:48  mch
 Fixed various resource bits

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1  2005/03/10 16:42:55  mch
 Split fits, sql and xdb

 Revision 1.2  2005/03/10 13:49:52  mch
 Updating metadata

 Revision 1.1.1.1  2005/02/17 18:37:35  mch
 Initial checkin

 Revision 1.1  2005/02/17 18:17:46  mch
 Moved SqlWriters back into server as they need metadata information

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.3  2004/12/20 17:14:15  mch
 Fixes to making cone searches form Queryable

 Revision 1.1.2.2  2004/12/10 12:37:13  mch
 Cone searches to look in metadata, lots of metadata interpreterrs

 Revision 1.1.2.1  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults


 */




