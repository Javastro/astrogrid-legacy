/*
 * $Id: SqlParser.java,v 1.9 2004/09/01 12:04:43 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.query.condition.*;

import java.util.Hashtable;
import java.util.StringTokenizer;
import java.util.Vector;
import org.astrogrid.datacenter.returns.ReturnSpec;
import org.astrogrid.datacenter.returns.ReturnTable;


/**
 * A string parser that separates out the SELECT, FROM and WHERE parts of a SQL
 * statement and sends them off to the relevent parser
 * Much of this has been taken from the Kameleon reports program, a bespoke
 * OO reporting system written far too long ago (by mch) but included search
 * condition parsing and evaluation, including substituting Xpath-like access
 * to an OO database.
 */

public class SqlParser  {
   
   Hashtable colAlias = new Hashtable();
   
   Condition whereClause = null;
   
   ReturnTable resultsDef = new ReturnTable(null);
   
   // the 'from' cluase - theupper list of tables to search
   Vector scope = new Vector();

   //the 'order by' clause
   Vector orderBy = new Vector();
   
   /**
    * Parse where clause
    */
   private void parseWhere(String expression) {
      whereClause = parseBoolean(expression);
   }
   
//   public Condition getWhere() { return whereClause; }
//   public ResultsDefinition getResultsDef() { return resultsDef; }
//   public String[] getScope() { return (String[]) scope.toArray( new String[] {} ); }

   public Query getQuery() {
      return new Query((String[]) scope.toArray( new String[] {} ), whereClause, resultsDef);
   }
   
   //contains results of a breakExpression call
   private class BrokenExpression {
      String left;
      String right;
      String operand;
      boolean brackets = false;
      
      public BrokenExpression(String givenLHS, String givenOp, String givenRHS) {
         this.left = givenLHS.trim();
         this.operand = givenOp.trim();
         this.right = givenRHS.trim();
      }
      
   }
   
   /** Locates an operand in the given expression from the list of
    * given operands.  This is done by going through the string from
    * start to end, counting brackets & quotes on the way, so that we find
    * the operand closest to the 'top' of the brackets and outside the
    * quotes.
    * The operands will be searched for in order - this is important if some
    * operands are substrings of other ones.  Have the longer ones (eg '>='
    * before '>') */
   //lifted and converted directly from report.pas splitStatement
   private BrokenExpression breakExpression(String expression, String[] operands) {

      assert (operands != null) && (operands.length >0) : "No operands given";
      
      int bracketDepth = 0;  //BCount
      int pos = 0;   //P
      boolean inDQuotes = false; // {marker for being in quotes mode}
      boolean inSQuotes = false; // {marker for being in single quotes }

      expression = expression.trim();

      //find operand
      while (pos<expression.length()) //&& (broken.operand.length()==0))
      {

         char C = expression.charAt(pos); //{shorthand}
   
         //{-- check if reached operand ---}
         if (!inDQuotes && !inSQuotes && (bracketDepth==0)) { // {take quotes as literal and split with brackets}

            for (int i = 0; i < operands.length; i++) {
               if ((pos+operands[i].length() < expression.length()) && //is there room for it?
                     (expression.substring(pos,pos+operands[i].length()).toUpperCase().equals(operands[i]))) {
                  
                  BrokenExpression broken = new BrokenExpression(
                     expression.substring(0,pos),
                     operands[i],
                     expression.substring(pos+operands[i].length())
                  );

                  if (((broken.left.length()==0) || (broken.right.length()==0))) {
                     throw new IllegalArgumentException("Hanging Operand (Missing argument) "+broken.operand+" in "+expression);
                  }
                  return broken;

               }
            }
         }
   
         //{check "mode"}
         switch (C) {
            case '"' : inDQuotes = !inDQuotes; break;
            case '\'' : inSQuotes = !inSQuotes; break;
            case '(' : bracketDepth++; break;
            case ')' : if (bracketDepth==0) {
                           throw new IllegalArgumentException("Brackets inconsistent - too many closing ones at "+pos+" in '"+expression+"'");
                        }
                        bracketDepth--;
         }
         pos++; //next character
      } //end while

      //no operand found - could be superflous brackets or an atomic expression
      // (eg colref or number) or syntax errors...

      //reached the end
      if (bracketDepth>0) {
         throw new IllegalArgumentException("Too many opening brackets in "+expression);
      }
      
      if (inDQuotes) {
         throw new IllegalArgumentException("No closing double quotes in "+expression);
      }
      
      if (inSQuotes) {
         throw new IllegalArgumentException("No closing single quotes in "+expression);
      }

      //see if surrounding bracket, and remove - can do this safely now that
      //we know there's no operand found, but can only remove one pair between
      //breaking expressions, so that
      // ((A=B) AND (C=D)) doens't become 'A=B) AND (C=D'
      if (expression.startsWith("(") && expression.endsWith(")")) {
         expression = expression.substring(1, expression.length()-1);
         BrokenExpression broken = breakExpression(expression, operands);
         broken.brackets = true;
         return broken;
      }

      //if there are no spaces, it's some atomic expression
      if (expression.indexOf(' ')==-1) {
         return new BrokenExpression(expression, "", "");
      }

      //the caller might not care if the operands aren't found - might try
      //with other operands.
      //String opList = operands[0];
      //for (int i = 1; i < operands.length; i++) { opList = ", "+opList; }
      
      //throw new IllegalArgumentException("Syntax error? Could not find Operands "+opList+" in expression "+expression);
      return new BrokenExpression(expression, "", "");

   }
   
   
   /** Parses an expression that *results* in a boolean true/false answer */
   private Condition parseBoolean(String expression) {

      BrokenExpression broken = breakExpression(expression.trim(), new String[] {" AND ", " OR " }  );
      if (broken.operand.length() == 0) {
         //the operands are searhced in order of finding the first character, so make sure the >=, <= etc are before the single chars
         broken = breakExpression(expression.trim(), new String[] { ">=", "<=", "<>", "=", "<", ">"} );
      }

      //atomic, so might be a boolean function
      if (broken.operand.length() == 0) {
//         throw new IllegalArgumentException("'"+expression+"' is not a boolean expression (no space-separated logical/comparison operator AND, OR, <, > or =)");
          return parseFunction(expression);
      }
      
      if (broken.operand.trim().equals("AND") || broken.operand.trim().equals("OR")) {
         return new LogicalExpression(
            parseBoolean(broken.left),
            broken.operand,
            parseBoolean(broken.right) );
      }
      else
      {
         return new NumericComparison(
            parseNumeric(broken.left),
            broken.operand,
            parseNumeric(broken.right) );
      }
      
   }
   
   /** Parses an expression that gives a numeric result at evaluation time - eg
    * a column reference or a number.
    */
   private NumericExpression parseNumeric(String expression) {

      expression = expression.trim();
      
      BrokenExpression broken = breakExpression(expression.trim(), new String[] {" - ", " + ", " / ", " * "} );

      if (broken.operand.length() == 0) {

         //atomic expression
         try {
            Double.parseDouble(expression.trim());
            
            return new LiteralNumber(expression);
         }
         catch (NumberFormatException nfe) {
            
            //wasn't a number, must be a column reference or a function
            if ((expression.indexOf('(') > -1) &&
                (expression.indexOf('(') < new String(expression+" ").indexOf(' '))) {
               return parseFunction(expression);
            }
            
            return parseColumnRef(expression);
         }
      }
      else {
         return new MathExpression(
            parseNumeric(broken.left),
            broken.operand,
            parseNumeric(broken.right)
         );
      }
   }
   
   /** Parses a column reference, eg <alias>.<column> or <tablename>.<columnname>
    */
   private ColumnReference parseColumnRef(String colRef) {
      int stop = colRef.indexOf(".");
      String tableName = "";
      if (stop == -1) {
         if (scope.size() == 1) {
            //if there is only one FROM then we can assume the table name is that
            tableName = scope.get(0).toString();
         }
         else {
            throw new IllegalArgumentException("There is no '.' in expected column reference '"+colRef+"'");
         }
      }
      else {
         tableName = colRef.substring(0,stop);
      }
      String colName = colRef.substring(stop+1);
      if (colAlias.get(tableName) != null) {
         tableName = (String) colAlias.get(tableName);
      }
      return new ColumnReference(tableName, colName);
   }

   /**
    * Makes an attempt to guess whether an expression is a string or a number
    * (or an expression of either) and returns the appropriate expression.
    * Simple guess by seeing if the first token can be parsed by a Double, if it
    * starts with a ' or " it's a string, if it's got a single '.' in it with one
    * token it's a column, if it's none of these it's a string. This means that
    * to get it to work properly, the first token must represent the type - ie
    * do 4 + T.A rather than T.A + 4
    *
    * Note this doens't work in all situations - it should only be used in
    * function arguments where the type of argument is unknown.
    */
   private Expression parseExpression(String expression) {
      StringTokenizer tokenizer = new StringTokenizer(expression, " ");
      String first = tokenizer.nextToken();
      try {
         Double.parseDouble(first);
         return parseNumeric(expression);
      }
      catch (NumberFormatException nfe) {
         if (first.startsWith("'") || first.startsWith("\"")) {
            return parseString(expression);
         }
         if (first.indexOf(".")>-1) {
            if (tokenizer.hasMoreTokens()) {
               throw new UnsupportedOperationException("Thicky Parser: Can't tell type (numeric/string) from '"+first+"' of '"+expression+"'.  Please rearrange expression to start with a constant (or '' or 0)");
            }
            //single column
            return parseColumnRef(first);
         }
         //probably a string
         return parseString(expression);
      }
   }

   /**
    * Parses a string expression. Actually it doesn't, it just returns the
    * expression for the moment...
    */
   private StringExpression parseString(String expression) {
      return new LiteralString(expression);
   }
   
   /**
    * Parses a function, ie a function name and a list of comma separated
    * arguments within following brackets
    */
   private Function parseFunction(String expression) {
      String funcName = expression.substring(0,expression.indexOf('(')).trim();

      String bracketed = expression.substring(expression.indexOf('(')+1).trim();
      
      assert bracketed.endsWith(")") : "Closing brackets not at end of function '"+expression+"'";

      StringTokenizer argList = new StringTokenizer(bracketed.substring(0,bracketed.length()-1), ",");
      Vector args = new Vector();
      
      while (argList.hasMoreTokens()) {
         Expression arg = parseExpression(argList.nextToken());
         args.add(arg);
      }
      
      return new Function(funcName, (Expression[]) args.toArray(new Expression[] {}));
      
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
      
      //go through list separated by commas
      StringTokenizer tokenizer = new StringTokenizer(list, ",");
      
      Vector cols = new Vector();
      
      while (tokenizer.hasMoreTokens()) {
         //each token is a column definition for the results
         String colDef = tokenizer.nextToken().trim();

         /*
         if (colRef.indexOf(" ")>-1) {
            throw new UnsupportedOperationException(
               "Give only comma-separated column references in the SELECT statement; "+
               "Token '"+colRef+"' is not a single column reference");
         }
          */
         
         cols.add(parseNumeric((colDef)));
         
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
         resultsDef.setColDefs(null);
      }
      else {
         resultsDef.setColDefs(parseSelectList(select));
      }
   }
   
   /**
    * Creates the list of aliases from the FROM statement which should be
    * comma separated
    */
   private void parseFrom(String from) {
      //go through list separated by commas
      StringTokenizer tokenizer = new StringTokenizer(from, ",");

      while (tokenizer.hasMoreTokens()) {
         //assume each token is just a reference to a column
         String token = tokenizer.nextToken().trim();
         String origToken = token; //for errors
         
         //chop out first word
         String tableName = "";
         if (token.indexOf(' ') == -1) {
            tableName = token;
            token = "";
         }
         else {
            tableName = token.substring(0,token.indexOf(' ')).trim();
            token = token.substring(tableName.length()).trim();
         }
   
         scope.add(tableName);
   
         if (token.toUpperCase().startsWith("AS")) {
            //find alias - chop out AS
            String tableAlias = token.substring(2).trim();
            if (tableAlias.indexOf(' ')>-1) {
               throw new IllegalArgumentException(
                  "FROM statement should consist of comma separated <table>[ as <alias], " +
                  "Alias '"+tableAlias+"' has spaces in, in token '"+origToken+"' in '"+from+"'");
            }
            addAlias(tableName, tableAlias);
         }
         else
         if (token.trim().length() >0) {
            throw new IllegalArgumentException(
               "FROM statement should consist of comma separated <table>[ as <alias], " +
               "Invalid comma-separated token '"+origToken+"' in '"+from+"'");
         }
            
      }
   }
   
   /*
    * The values that define the sort order are defined by NumericExpressions (so you can return
    * LOG(Table.MAG) ) but for the moment we are going to assume that they are
    * all columns.
    */
   public void parseOrderBy(String orderBySql) {
      
      //go through list separated by commas
      StringTokenizer tokenizer = new StringTokenizer(orderBySql, ",");
      
      while (tokenizer.hasMoreTokens()) {
         //each token is a column definition for the results
         String colDef = tokenizer.nextToken().trim();
    
         orderBy.add(parseNumeric((colDef)));
      }

      resultsDef.setSortOrder( (NumericExpression[]) orderBy.toArray(new NumericExpression[] {} ));
   }

   public void parseLimit(String limit) {
         //find first token after LIMIT keyword as the number limit
         StringTokenizer tokenizer = new StringTokenizer(limit.trim()," ");
         resultsDef.setLimit(Integer.parseInt(tokenizer.nextToken()));
   }
      
   /**
    * Parses a full SQL statement, building up the alias list from the FROM
    * and/or SELECT statements, then creating a table results definition from the
    * SELECT part and a BooleanExpression from the WHERE part
    */
   public void parseStatement(String sql) {

      //prepare string
      sql = sql.replace('\n', ' ').trim();
      
      scope = new Vector(); //clear scope

      //break down into SELECT ...  LIMIT ... FROM ... WHERE ... ORDER BY in that order
      if (!sql.startsWith("SELECT")) {
         throw new IllegalArgumentException("SQL doesn't start with SELECT - Can't cope");
      }
      
      int limitIdx = sql.indexOf("LIMIT");
      int fromIdx = sql.indexOf("FROM");
      int whereIdx = sql.indexOf("WHERE");
      int orderByIdx = sql.indexOf("ORDER BY");

      //start with FROM so we get the scope and aliases
      if (fromIdx == -1) {
         throw new IllegalArgumentException("No FROM in SQL statement");
      }
      else {
         int end = whereIdx;
         if (end == -1) end = orderByIdx;
         if (end == -1) {
            parseFrom(sql.substring(fromIdx+4));
         }
         else {
            parseFrom(sql.substring(fromIdx+4, end-1));
         }
      }
      
      
      if (orderByIdx > -1) {
         parseOrderBy(sql.substring(orderByIdx+8));
         sql = sql.substring(0, orderByIdx-1);
      }

      if (whereIdx > -1) {
         parseWhere(sql.substring(whereIdx+5));
         sql = sql.substring(0, whereIdx-1);
      }

      //remove from
      sql = sql.substring(0, fromIdx-1);
      
      if (limitIdx > -1) {
         parseLimit(sql.substring(limitIdx+5));
         sql = sql.substring(0, limitIdx-1);
      }
      
      parseSelect(sql.substring(6));//remove 'SELECT'
      
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
      String s = "S.RA,    T.WIBBLE , UNDIE.PANTS, ETC.ETC";
      
      parser.parseSelect(s);
      
      System.out.println(parser.resultsDef);
      
      //-- where --
      s = "S.RA > 2 AND S.RA = G.RA AND S.DEC <= G.DEC AND WIBBLE.WOBBLE > BANANA.TREE OR X.Y>A.B AND Y.Z<=B.A";
      
      parser.parseWhere(s);
      
      System.out.println(parser.whereClause);
      
      //-- circle --
      s = "CIRCLE(J2000, 12, 30, 6)";
      
      parser.parseWhere(s);
      
      System.out.println(parser.whereClause);

      //-- from --
      s = "STARS AS S, WIBBLES AS WI ,GALAXIES as g";
      parser.parseFrom(s);
      System.out.println(parser.scope);
      
      //-- proper SQL --
      s = "SELECT S.RA  ,  T.WIBBLE, UNDIE.PANTS, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA - C.A";

      parser.parseStatement(s);
  
      System.out.println(parser);

      //-- proper SQL --
      s = "SELECT S.RA  ,  T.WIBBLE, UNDIE.PANTS * 4, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
      
      parser.parseStatement(s);
  
      System.out.println(parser);

      //-- proper SQL --
      s = "SELECT * FROM TABLE WHERE CIRCLE(J2000, 12, 30, 6)";
      
      parser.parseStatement(s);
  
      System.out.println(parser);

      //-- proper SQL --
      s = "SELECT * FROM TABLE WHERE AVG(TABLE.RA) > 12";
      
      parser.parseStatement(s);
  
      System.out.println(parser);

      //-- proper SQL --
      s = "SELECT * FROM TABLE WHERE SUM(TABLE.RA * 2) > 12 AND (TABLE.T * 4 > LOG(TABLE.V) )";
      
      parser.parseStatement(s);
  
      System.out.println(parser);
   }
   
}

/*
 $Log: SqlParser.java,v $
 Revision 1.9  2004/09/01 12:04:43  mch
 Removed upper case conversions

 Revision 1.8  2004/08/27 09:31:16  mch
 Added limit, order by, some page tidying, etc

 Revision 1.7  2004/08/26 11:47:16  mch
 Added tests based on Patricios errors and other SQl statements, and subsequent fixes...

 Revision 1.6  2004/08/25 23:38:34  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.5  2004/08/18 22:56:18  mch
 Added Function parsing

 Revision 1.4  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 Revision 1.3  2004/08/18 09:17:36  mch
 Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

 Revision 1.2  2004/08/13 09:47:57  mch
 Extended parser/builder to handle more WHERE conditins

 Revision 1.1  2004/08/13 08:52:24  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */



