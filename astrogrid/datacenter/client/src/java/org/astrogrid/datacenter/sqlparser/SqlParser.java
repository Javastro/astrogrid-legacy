/*
 * $Id: SqlParser.java,v 1.2 2004/08/13 09:47:57 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.criteria.*;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.astrogrid.datacenter.query.results.ResultsDefinition;
import org.astrogrid.datacenter.query.results.TableResultsDefinition;


/**
 * A string parser that separates out the SELECT, FROM and WHERE parts of a SQL
 * statement and sends them off to the relevent parser
 */

public class SqlParser  {
   
   Hashtable colAlias = new Hashtable();
   
   BooleanExpression whereClause = null;
   
   ResultsDefinition resultsDef = null;
   
   Vector scope = new Vector(); // the 'from' cluase - the list of tables to search
   
   /**
    * Parse where clause
    */
   private void parseWhere(String expression) {
      whereClause = parseBoolean(expression);
   }
   
   public BooleanExpression getWhere() { return whereClause; }
   public ResultsDefinition getResultsDef() { return resultsDef; }
   public String[] getScope() { return (String[]) scope.toArray( new String[] {} ); }
   
   /** Parses an expression that *results* in a boolean true/false answer */
   private BooleanExpression parseBoolean(String expression) {
      
      expression = expression.trim();
      
      int andIndex = expression.toUpperCase().indexOf(" AND "); //surround by spaces so we don't find it in other words
      
      if (andIndex > -1) {
         //found an AND
         BooleanExpression lhs = parseBoolean(expression.substring(0,andIndex).trim());
         BooleanExpression rhs = parseBoolean(expression.substring(andIndex+4).trim());
         return new LogicalExpression(lhs, "AND", rhs);
      }
      
      int orIndex = expression.toUpperCase().indexOf(" OR "); //surround by spaces so we don't find it in other words
      
      if (orIndex > -1) {
         //found an AND
         BooleanExpression lhs = parseBoolean(expression.substring(0,orIndex).trim());
         BooleanExpression rhs = parseBoolean(expression.substring(orIndex+3).trim());
         return new LogicalExpression(lhs, "OR", rhs);
      }
      
      int opIndex = expression.indexOf(">");
      if (opIndex == -1) {
         opIndex =  expression.indexOf("<");
      }
      if (opIndex == -1) {
         opIndex =  expression.indexOf("=");
      }
      int opIndexEnd = opIndex+1;
      if (expression.charAt(opIndexEnd) == '=') { opIndexEnd ++; } //add one if it's <= or >=
      
      
      if (opIndex > -1) {
         //found an operator
         NumericExpression lhs = parseNumeric(expression.substring(0,opIndex).trim());
         NumericExpression rhs = parseNumeric(expression.substring(opIndexEnd).trim());
         return new ComparisonExpression(lhs, expression.substring(opIndex,opIndexEnd).trim(), rhs);
      }
      
      throw new IllegalArgumentException("'"+expression+"' is not a boolean expression (no space-separated logical/comparison operator AND, OR, <, > or =)");
   }
   
   /** Parses an expression that gives a numeric result at evaluation time - eg
    * a column reference or a number.
    * @todo Only handles integers or columns at the moment. Not really numeric - could be a string...
    */
   private NumericExpression parseNumeric(String expression) {
      try {
         int value = Integer.parseInt(expression.trim());
         
         return new Constant(""+value);
      }
      catch (NumberFormatException nfe) {
         //wasn't a number, must be a column reference
         return parseColumnRef(expression);
         
      }
   }
   
   /** Parses a column reference, eg <alias>.<column> or <tablename>.<columnname>
    */
   private ColumnReference parseColumnRef(String colRef) {
      int stop = colRef.indexOf(".");
      if (stop == -1) {
         throw new IllegalArgumentException("There is no '.' in expected column reference '"+colRef+"'");
      }
      String tableName = colRef.substring(0,stop);
      String colName = colRef.substring(stop+1);
      if (colAlias.get(tableName) != null) {
         tableName = (String) colAlias.get(tableName);
      }
      return new ColumnReference(tableName, colName);
   }
   
   /** Adds a table alias - these will be resolved to table names for the
    * internal representation */
   public void addAlias(String tableName, String tableAlias) {
      colAlias.put(tableAlias, tableName);
   }
   
   
   /*
    * The values that define the results columns are defined by NumericExpressions (so you can return
    * LOG(Table.MAG) ) but for the moment we are going to assume that they are
    * all columns.
    */
   private NumericExpression[] parseSelectList(String list) {
      
      //go through list separated by spaces
      StringTokenizer tokenizer = new StringTokenizer(list, " ");
      
      Vector cols = new Vector();
      
      while (tokenizer.hasMoreTokens()) {
         //assume each token is just a reference to a column
         String colRef = tokenizer.nextToken();
         if (colRef.endsWith(",")) {
            colRef = colRef.substring(0,colRef.length()-1);
         }
         
         NumericExpression colDef = parseColumnRef((colRef));
         
         cols.add(colDef);
      }
      
      return (NumericExpression[]) cols.toArray(new NumericExpression[] {} );
   }
   
   /**
    * Returns a results definition - ie a description of what the results will
    * look like.
    */
   private void parseSelect(String select) {
      
      //special case of select all
      if (select.trim().equals("*")) {
         resultsDef = new TableResultsDefinition(null);
      }
      else {
         resultsDef = new TableResultsDefinition(null, parseSelectList(select));
      }
   }
   
   /**
    * Creates the list of aliases from the FROM statement
    */
   private void parseFrom(String from) {
      //trim and remove commas
      from = from.toUpperCase().replace(',', ' ').trim();

      while (from.trim().length() >0) {
         //chop out first word
         String tableName = "";
         if (from.indexOf(' ') == -1) {
            tableName = from;
            from = "";
         }
         else {
            tableName = from.substring(0,from.indexOf(' ')).trim();
            from = from.substring(tableName.length()).trim();
         }
   
         scope.add(tableName);
   
         if (from.startsWith("AS")) {
            //find alias - chop out AS
            from = from.substring(2).trim()+' ';
            int space=from.indexOf(' ');
            String tableAlias = from.substring(0, space).trim();
            from = from.substring(tableAlias.length()).trim();
            addAlias(tableName, tableAlias);
         }
      }
   }
   
   /**
    * Parses a full SQL statement, building up the alias list from the FROM
    * and/or SELECT statements, then creating a table results definition from the
    * SELECT part and a BooleanExpression from the WHERE part
    */
   public void parseStatement(String sql) {
      sql = sql.trim().toUpperCase();
      
      if (!sql.startsWith("SELECT")) {
         throw new IllegalArgumentException("SQL doesn't start with SELECT - Can't cope");
      }
      
      int fromIdx = sql.indexOf("FROM");
      int whereIdx = sql.indexOf("WHERE");

      //parse the from bit if it exists
      if (fromIdx > -1) {
         if (whereIdx > -1) {
            parseFrom(sql.substring(fromIdx+4, whereIdx-1));
         }
         else {
            parseFrom(sql.substring(fromIdx+4));
         }
      }
      
      //parse the select bit
      String select;
      if (fromIdx == -1) {
         select = sql.substring(6, whereIdx);
      }
      else {
         select = sql.substring(6, fromIdx);
      }
      parseSelect(select);
      
      //parse the where clause
      if (whereIdx >-1) {
         String whereSql = sql.substring(whereIdx+5);
      
         parseWhere(whereSql);
      }
   }
   
   /**
    * For humans/debuggign
    */
   public String toString() {
      return "{SQL: In scope "+scope+" look for where "+whereClause+" -> "+resultsDef+"}";
   }
   
   
   /**
    * Test harness
    */
   public static void main(String[] args) {
      
      SqlParser parser = new SqlParser();
      
      parser.addAlias("STAR", "S");
      parser.addAlias("GALAXY", "G");
      
      //test select
      String s = "S.RA    T.WIBBLE UNDIE.PANTS, ETC.ETC";
      
      parser.parseSelect(s);
      
      System.out.println(parser.resultsDef);
      
      //-- where --
      s = "S.RA > 2 AND S.RA = G.RA AND S.DEC <= G.DEC AND WIBBLE.WOBBLE > BANANA.TREE OR X.Y>A.B AND Y.Z<=B.A";
      
      parser.parseWhere(s);
      
      System.out.println(parser.whereClause);
      
      //-- from --
      s = "STARS AS S, WIBBLES AS WI GALAXIES as g";
      parser.parseFrom(s);
      System.out.println(parser.scope);
      
      //-- proper SQL --
      s = "SELECT S.RA    T.WIBBLE UNDIE.PANTS, ETC.ETC FROM A, B  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      parser.parseStatement(s);
  
      System.out.println(parser);
   }
   
}

/*
 $Log: SqlParser.java,v $
 Revision 1.2  2004/08/13 09:47:57  mch
 Extended parser/builder to handle more WHERE conditins

 Revision 1.1  2004/08/13 08:52:24  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */

