/*
 * $Id: StdSqlWriter.java,v 1.1 2005/02/17 18:37:35 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.dataservice.queriers.sql;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import java.text.DecimalFormat;
import javax.xml.parsers.ParserConfigurationException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.config.ConfigException;
import org.astrogrid.dataservice.metadata.VoDescriptionServer;
import org.astrogrid.dataservice.metadata.queryable.QueryableResourceReader;
import org.astrogrid.dataservice.metadata.queryable.SearchField;
import org.astrogrid.dataservice.metadata.queryable.SearchGroup;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryVisitor;
import org.astrogrid.query.adql.Adql074Writer;
import org.astrogrid.query.adql.AdqlXml074Parser;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.sql.SqlParser;
import org.astrogrid.sky.Angle;
import org.xml.sax.SAXException;


/**
 * For writing out Querys as SQL statment strings, as close as we can to 'standard SQL'.
 */

public class StdSqlWriter implements QueryVisitor
{

   protected static Log log = LogFactory.getLog(Adql074Writer.class);
   
   private DecimalFormat longDec = new DecimalFormat("#############################0");

   /** SQL String being built */
   protected StringBuffer sql = new StringBuffer();
   
   /** Query being written */
   Query query = null;
   
   /** Root write call */
   public void visitQuery(Query queryToWrite)  throws IOException  {
      sql = new StringBuffer();
      query = queryToWrite;
      log.debug("Making SQL from "+query.toString());
      
      //--- SELECT ---
      visitReturnSpec(query.getResultsDef());
      visitLimit(query.getLocalLimit());
      
      //-- FROM ---
      if (query.getScope() != null) {
         
         visitScope(query.getScope());
      }

      //-- WHERE --
      if (query.getCriteria() != null) {
         sql.append(" WHERE ");
      
         query.getCriteria().acceptVisitor(this);
      }
   }

   public String getSql() {
      return sql.toString();
   }
   
   public void visitScope(String[] scope) {
         sql.append(" FROM ");

         // we just duplicate alias names as table names for now
         for (int i = 0; i < scope.length; i++) {
         /*
            String alias = scope[i];
            if (query.getAlias(alias) != null) {
               alias = query.getAlias(alias);
            }
            sql.append(" "+query.getScope()[i]+" as "+alias+" ");
          */
            sql.append(" "+scope[i]+" ");
            if (i<scope.length-1) {
               sql.append(",");
            }
         }
   }

   public void visitReturnSpec(ReturnSpec spec) throws IOException  {
      sql.append("SELECT ");
      if ( !(spec instanceof ReturnTable) ||
             ( ((ReturnTable) spec).getColDefs()==null)  ) {
         sql.append(" * ");
      }
      else {
         Expression[] colDefs = ((ReturnTable) spec).getColDefs();

         for (int i = 0; i < colDefs.length; i++) {
            colDefs[i].acceptVisitor(this);
            if (i<colDefs.length-1) {
               sql.append(", ");
            }
         }
      }
   }

   /** Standard SQL can't handle this, so we leave it blank */
   public void visitLimit(long limit) {
   }
   
   public void visitIntersection(Intersection expression)  throws IOException {
         sql.append(" (");
         Condition[] conditions = ((Intersection) expression).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
            if (i<conditions.length-1) {
               sql.append(" AND ");
            }
         }
         sql.append(") ");
    }
    
    public void visitUnion(Union expression)  throws IOException {
         sql.append(" (");
         Condition[] conditions = ((Union) expression).getConditions();
         for (int i = 0; i < conditions.length; i++) {
            conditions[i].acceptVisitor(this);
            if (i<conditions.length-1) {
               sql.append(" OR ");
            }
         }
         sql.append(") ");
    }

      
   public void visitNumericComparison(NumericComparison expression)  throws IOException {

       expression.getLHS().acceptVisitor(this);
       sql.append( expression.getOperator().toString());
       expression.getRHS().acceptVisitor(this);
   }

   public void visitStringComparison(StringComparison expression)  throws IOException {
      
      expression.getLHS().acceptVisitor(this);
      sql.append( expression.getOperator().toString());
      expression.getRHS().acceptVisitor(this);
   }
   
   public void visitNumber(LiteralNumber expression)  {
      sql.append( " "+expression.getValue()+" ");
   }

   public void visitAngle(LiteralAngle expression)  {
      sql.append( " "+expression.getAngle().asDegrees()+" ");
   }

   
   public void visitRawSearchField(RawSearchField field)  {
      sql.append( " "+field.getField()+" "); //bit of a botch - should throw an exception?
   }

   public void visitMath(MathExpression math)   throws IOException {
         math.getLHS().acceptVisitor(this);
         sql.append( " "+math.getOperator()+" ");
         math.getRHS().acceptVisitor(this);
   }
   
   public void visitString(LiteralString string) {
      sql.append( " '"+string.getValue()+"' ");
   }

   public void visitFunction(Function function)  throws IOException {
      
      if (function.getName().trim().toUpperCase().equals(CircleCondition.NAME.toUpperCase())) {
         visitCircle(CircleCondition.makeCircle(function));
         return;
      }
      
      sql.append(" "+function.getName()+"(");
      
      for (int i = 0; i < function.getArgs().length; i++) {
         ((Expression) function.getArg(i)).acceptVisitor(this);
         if (i<function.getArgs().length-1) {
            sql.append(", ");
         }
      }
      sql.append(")");
   }
   
   /** Writes out the ADQL tag for the given column as a child of the given parentTag with
    * the given elementName */
   public void visitColumnReference(ColumnReference colRef) {

      sql.append(" ");
      String tableName = colRef.getTableName();
      //replace with alias is there is one
//no don't - just use full table name      if (query.getAlias(tableName) != null) {
//         tableName = query.getAlias(tableName);
//      }

      
      if ((colRef.getDatasetName() != null) && (colRef.getDatasetName().trim().length()>0)) {
         sql.append(colRef.getDatasetName()+":");
      }
      if ((tableName != null) && (tableName.trim().length()>0)) {
         sql.append(tableName+".");
      }

      sql.append(colRef.getColName()+" ");
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

      QueryableResourceReader queryable = VoDescriptionServer.getQueryable();

      //go through spatial groups checking against scope looking to see if any of them have any spatial fields
      SearchGroup[] tables = queryable.getSpatialGroups();
      boolean spatialFound = false; //marker that we've found at least one to search on
      for (int i = 0; i < tables.length; i++)
      {
         //is this table in scope?
         for (int j = 0; j < query.getScope().length; j++)
         {
            if (tables[i].getId().equals(query.getScope()[j])) {
               if (spatialFound) { //a previous one was found, so add an OR to search this one too
                  sql.append(" OR ");
               }
               spatialFound = true; // found one
               SearchField[] cols = queryable.getSpatialFields(tables[i]);

               //assume, for the moment, that we get two columns, the first is RA and the
               //second is DEC
               sql.append(makeSqlCircleCondition(cols[0], cols[1], ra, dec, radius));
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
      boolean funcsInRads = true; //SimpleConfig.getSingleton().getBoolean(SqlMaker.DB_TRIGFUNCS_IN_RADIANS);
      
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
      String colUnits = col.getUnits();
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
   public String getAngleInColUnits(Angle givenAngle, String colUnits) {
      if (colUnits.equals("rad")) {
         return ""+givenAngle.asRadians();
      }
      else if (colUnits.equals("deg")) {
         return ""+givenAngle.asDegrees();
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
 Revision 1.1  2005/02/17 18:37:35  mch
 *** empty log message ***

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




