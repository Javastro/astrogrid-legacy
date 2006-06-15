/*
 * $Id: SqlParser.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.oldquery.sql;

import org.astrogrid.oldquery.condition.*;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.xml.parsers.ParserConfigurationException;
import org.astrogrid.oldquery.FunctionDefinition;
import org.astrogrid.oldquery.OldQuery;
import org.astrogrid.oldquery.QueryException;
import org.astrogrid.oldquery.returns.ReturnTable;
import org.astrogrid.oldquery.constraint.ConstraintSpec;
import org.astrogrid.slinger.targets.TargetIdentifier;
import org.xml.sax.SAXException;


/**
 * A string parser that separates out the SELECT, FROM and WHERE parts of a SQL
 * statement and sends them off to the relevent parser
 * Much of this has been taken from the Kameleon reports program, a bespoke
 * OO reporting system written far too long ago (by mch) but included search
 * condition parsing and evaluation, including substituting Xpath-like access
 * to an OO database.
 *
 * @deprecated This class depends on the old query model, OldQuery, which
 * needs to be removed.
 */

public class SqlParser  {
   
   // Note - this treats the alias as the key and the table name as 
   // the stored value
   Hashtable colAlias = new Hashtable();
   
   Condition whereClause = null;
   
   ReturnTable resultsDef = new ReturnTable(null);
   
   ConstraintSpec constraintSpec = new ConstraintSpec();

   Hashtable funcsAvailable = new Hashtable();
   
   // the 'from' cluase - theupper list of tables to search
   Vector scope = new Vector();

   //the 'order by' clause
   Vector orderBy = new Vector();

   /** Construct parser */
   public SqlParser() {
      //initialise function list
      for (int i = 0; i < FunctionDefinition.STD_ADQL_FUNCS.length; i++) {
         funcsAvailable.put(FunctionDefinition.STD_ADQL_FUNCS[i].getName(),
                            FunctionDefinition.STD_ADQL_FUNCS[i]);
      }
   }
   
   /**
    * Parses a full SQL statement, building up the alias list from the FROM
    * and/or SELECT statements, then creating a table results definition from the
    * SELECT part and a BooleanExpression from the WHERE part
    */
   public void parseStatement(String sql) {

      //prepare/cleanup string
      sql = sql.replace('\n', ' ').replace('\r', ' ').replace('\f', ' ').replace('\t', ' ').trim();
      
      scope = new Vector(); //clear scope

      //break down into SELECT ...  LIMIT ... FROM ... WHERE ... ORDER BY in that order
      if (!sql.toUpperCase().startsWith("SELECT")) {
         throw new IllegalArgumentException("SQL doesn't start with SELECT - Can't cope");
      }
      
      //includes space search, so bear in mind idx is off by minus 1 from actual word
      int limitIdx = sql.toUpperCase().indexOf(" LIMIT ");
      int fromIdx = sql.toUpperCase().indexOf(" FROM ");
      int whereIdx = sql.toUpperCase().indexOf(" WHERE ");
      int orderByIdx = sql.toUpperCase().indexOf(" ORDER BY ");

      //start with FROM so we get the scope and aliases
      if (fromIdx == -1) {
         throw new IllegalArgumentException("No FROM in SQL statement");
      }
      else {
         int end = whereIdx;
         if (end == -1) end = orderByIdx;
         if (end == -1) {
            parseFrom(sql.substring(fromIdx+5));
         }
         else {
            parseFrom(sql.substring(fromIdx+5, end));
         }
      }
      
      
      if (orderByIdx > -1) {
         parseOrderBy(sql.substring(orderByIdx+9));
         sql = sql.substring(0, orderByIdx);
      }

      if (whereIdx > -1) {
         parseWhere(sql.substring(whereIdx+6));
         sql = sql.substring(0, whereIdx);
      }

      //remove from
      sql = sql.substring(0, fromIdx);
      
      if (limitIdx > -1) {
         parseLimit(sql.substring(limitIdx+6));
         sql = sql.substring(0, limitIdx);
      }
      
      parseSelect(sql.substring(6));//remove 'SELECT'
      
   }
   
   /**
    * Parse where clause
    */
   private void parseWhere(String expression) {
      whereClause = parseBoolean(expression);
   }
   
//   public Condition getWhere() { return whereClause; }
//   public ResultsDefinition getResultsDef() { return resultsDef; }
//   public String[] getScope() { return (String[]) scope.toArray( new String[] {} ); }

   public OldQuery getQuery() {
      //return new Query((String[]) scope.toArray( new String[] {} ), whereClause, resultsDef);
      OldQuery query = new OldQuery((String[]) scope.toArray( new String[] {} ), whereClause, resultsDef);
      // Set aliases from colAlias
      Iterator it = colAlias.keySet().iterator();
      while (it.hasNext()) {
         String alias = (String)it.next();
         query.addAlias((String)colAlias.get(alias), alias);
      }
      return query;
   }

   /** Static method for convenience */
   public static OldQuery makeQuery(String sql) {
      try {
         SqlParser parser = new SqlParser();
         parser.parseStatement(sql);
         return parser.getQuery();
      }
      catch (QueryException qe) {
         //add information, but don't wrap as we just get stacks of caused bys
         QueryException nqe = new QueryException(qe.getMessage()+" in SQL '"+sql+"'");
         nqe.setStackTrace(qe.getStackTrace());
         throw nqe;
      }
   }

   /** Another static constructor for convenience */
   public static OldQuery makeQuery(String adql, TargetIdentifier target, String format) throws QueryException, SAXException, IOException, ParserConfigurationException {
      OldQuery query = SqlParser.makeQuery(adql);
      query.getResultsDef().setTarget(target);
      query.getResultsDef().setFormat(format);
      return query;
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
         broken = breakExpression(expression.trim(), new String[] { ">=", "<=", "<>", "=", "<", ">", "LIKE"} );
      }

      if (broken.operand.length() == 0) {
//         throw new IllegalArgumentException("'"+expression+"' is not a boolean expression (no space-separated logical/comparison operator AND, OR, <, > or =)");
         if (expression.indexOf("(")==-1) {
            throw new IllegalArgumentException("Don't recognise any operands in '"+expression+"'; no space-separated logical/comparison operator AND, OR, <, >, =, etc");
         }
         //might be a conditional function - there's a bracket there
         return parseConditionFunction(expression);
      }
      
      if (broken.operand.trim().equals("AND")) {
         return new Intersection(
            parseBoolean(broken.left),
            parseBoolean(broken.right) );
      }
      else if (broken.operand.trim().equals("OR")) {
         return new Union(
            parseBoolean(broken.left),
            parseBoolean(broken.right) );
      }
      else if (broken.left.trim().startsWith("'") || (broken.right.trim().startsWith("'")) ||
               broken.left.trim().startsWith("\"") || (broken.right.trim().startsWith("\""))) {
         //one side is a string
         return new StringComparison(
            parseString(broken.left),
            broken.operand,
            parseString(broken.right) );
      }
      else {
         //assume numeric - NB this might break if we're comparing two string *columns*...
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

      //look for operands, also removes unnecessary brackets etc
      BrokenExpression broken = breakExpression(expression.trim(), new String[] {" - ", " + ", " / ", " * "} );

      if (broken.operand.length() == 0) {

         //atomic expression
         try {
            Double.parseDouble(broken.left);
            
            return new LiteralReal(broken.left);
         }
         catch (NumberFormatException nfe) {
            
            //wasn't a number, must be a column reference or a function
            if ((broken.left.indexOf('(') > -1) &&
                (broken.left.indexOf('(') < new String(broken.left+" ").indexOf(' '))) {
               return parseNumericFunction(broken.left);
            }
            
            return parseFieldRef(broken.left);
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
   
   /** Parses an expression that gives an angle result at evaluation time - at the
    * moment this just copes with LiteralAngle.
    */
   private LiteralAngle parseAngle(String expression) {

      expression = expression.trim();

      return new LiteralAngle(expression);
   }

   /** Parses a reference to a search field - eg a column reference.  At the moment
    * it makes a guess from if it has a full stop in it - but this should really
    * come from info about the queryy defibnition... */
   private SearchFieldReference parseFieldRef(String ref) {
      if (ref.indexOf(".")>-1) {
         return parseColumnRef(ref);
      }
      else {
         return new RawSearchField(ref);
      }
   }
   
   
   /** Parses a column reference, eg <alias>.<column> or <tablename>.<columnname>
    */
   private ColumnReference parseColumnRef(String colRef) {
      int colon = colRef.indexOf(":");
      String datasetName = null;
      if (colon>-1) {
         //extract dataset name & chop it off for next bit of parsing
         datasetName = colRef.substring(0,colon);
         colRef = colRef.substring(colon+1);
      }
      
      int stop = colRef.indexOf(".");
      String tableName = "";
      if (stop == -1) {
         if (scope.size() == 1) {
            //if there is only one FROM then we can assume the table name is that
            tableName = scope.get(0).toString();
         }
         else {
            //we could now look through the rdbms resource and see if there is only one match, but better to get the user to be more explicit
            throw new IllegalArgumentException("There is no '.' in expected column reference '"+colRef+"' and there is more than one table in FROM to apply it to");
         }
      }
      else {
         tableName = colRef.substring(0,stop);
      }
      String colName = colRef.substring(stop+1);
      if (colAlias.get(tableName) != null) {
         String realTableName = (String) colAlias.get(tableName);
         return new ColumnReference(
             datasetName, realTableName, colName, tableName);
      }
      else {
        // No alias in this case
         return new ColumnReference(
             datasetName, tableName, colName, ColumnReference.NO_ALIAS);
      }
   }

   /**
    * Parses a string expression. Actually it doesn't, it just returns the
    * expression for the moment...
    */
   private StringExpression parseString(String expression) {
      String trimmed = expression.trim();
      if (trimmed.startsWith("'")) {
         if (!trimmed.endsWith("'")) {
            throw new QueryException("No closing quote after "+trimmed);
         }
         //remove start and end quotes
         trimmed = trimmed.substring(1, trimmed.length()-1);
         return new LiteralString(trimmed);
      }
      else if (trimmed.startsWith("\"")) {
         if (!trimmed.endsWith("\"")) {
            throw new QueryException("No closing quote after "+trimmed);
         }
         //remove start and end quotes
         trimmed = trimmed.substring(1, trimmed.length()-1);
         return new LiteralString(trimmed);
      }
      else {
         return parseFieldRef(expression);
      }
   }
   
   /**
    * Parses a NumericFunction
    */
   public NumericFunction parseNumericFunction(String expression) {
      Function func = parseFunction(expression);
      if (func instanceof NumericFunction) {
         return (NumericFunction) func;
      }
      throw new QueryException("Numeric "+expression+" parses to function "+func.getName()+" which is not a numeric function");
   }
   
   /**
    * Parses a ConditionalFunction
    */
   public ConditionalFunction parseConditionFunction(String expression) {
      Function func = parseFunction(expression);
      if (func instanceof ConditionalFunction) {
         return (ConditionalFunction) func;
      }
      throw new QueryException("Condition "+expression+" parses to function "+func.getName()+" which is not a conditional function");
   }
   
   
   /**
    * Parses a function, ie a function name and a list of comma separated
    * arguments within following brackets
    */
   public Function parseFunction(String expression) {
      if (expression.indexOf('(')==-1) {
         throw new IllegalArgumentException("SQL Parser error; '"+expression+"' looks like a function but has no opening bracket");
      }
      String funcName = expression.substring(0,expression.indexOf('(')).trim();

      if (funcName.length()==0) {
         throw new RuntimeException("Error parsing query: no Function name in "+expression);
      }

      FunctionDefinition funcdef = (FunctionDefinition) (funcsAvailable.get(funcName.toUpperCase()));
      if (funcdef == null) {
         throw new QueryException("Function "+funcName+" is unknown/unavailable in "+expression);
      }
      
      String bracketed = expression.substring(expression.indexOf('(')+1).trim();
      
      assert bracketed.endsWith(")") : "Closing brackets not at end of function '"+expression+"'";
      bracketed = bracketed.substring(0,bracketed.length()-1);

      Vector argExps = new Vector();
      String[] argTypes = funcdef.getArgTypes();
      
      //find argument
      int bracketLevel = 0;
      boolean inDQuotes = false;
      boolean inSQuotes = false;
      int c = 0; //search char
      int lastC = 0;
      int arg = 0; //argument index
      while (c<bracketed.length()) {

         //parse out comma-separated token
         while ((c<bracketed.length() &&
                    ((bracketed.charAt(c) != ',') || (bracketLevel>0) || (inDQuotes) || (inSQuotes)))) {
            char charAt = bracketed.charAt(c);
            if (charAt == '(') { bracketLevel++; }
            if (charAt == ')') {
               bracketLevel--;
               if (bracketLevel < 0) {
                  throw new QueryException("Too many closing brackets in "+expression);
               }
            }
            if (charAt == '"') {
               inDQuotes = !inDQuotes;
            }
            if (charAt == '\'') {
               inSQuotes = !inSQuotes;
            }
            c++;
         }
         if ((inDQuotes) || (inSQuotes) || (bracketLevel>0)) {
            throw new QueryException("Quotes/bracket problem in "+expression);
         }

         String extractedArg = bracketed.substring(lastC, c).trim();
         if (arg>=argTypes.length) {
            throw new QueryException("Too many arguments in function "+funcdef);
         }
         //parse appropriately
         if (argTypes[arg] == FunctionDefinition.STRING) {
            argExps.add(parseString(extractedArg));
         }
         else if (argTypes[arg] == FunctionDefinition.NUMERIC) {
            argExps.add(parseNumeric(extractedArg));
         }
         else if (argTypes[arg] == FunctionDefinition.ANGLE) {
            argExps.add(parseAngle(extractedArg));
         }
         else {
            throw new RuntimeException("Function definition for "+funcdef.getName()+" returns unknown arg type "+argTypes[arg]);
         }
         arg++;
         c++;
         lastC = c;
      }
      
      if (arg<argTypes.length) {
         throw new QueryException("Not enough arguments in function "+funcdef);
      }

      if (CircleCondition.NAME.toLowerCase().equals(funcName.toLowerCase())) {
         return new CircleCondition((Expression[]) argExps.toArray(new Expression[] {}));
      }
      
      return new NumericFunction(funcName, (Expression[]) argExps.toArray(new Expression[] {}));
      
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
         else {
            // Add a "default" alias that's the same as the table name;
            // this is useful when the constructed query is used for 
            // building valid ADQL/xml, which requires an alias attribute 
            // for each table tag.
           addAlias(tableName, tableName);
           if (token.trim().length() >0) {
              throw new IllegalArgumentException(
                 "FROM statement should consist of comma separated <table>[ as <alias], " +
                 "Invalid comma-separated token '"+origToken+"' in '"+from+"'");
           }
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
         constraintSpec.setLimit(Integer.parseInt(tokenizer.nextToken()));
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
      s = "SELECT * FROM TABLE WHERE CIRCLE(J2000, 12, 30, 6) AND (TABLE.RMAG > 18)";
      
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

      //--- Savas' SQL ---
      s =
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
         "      (ReliableStars.sCorMagR2 > - 99) AND \n"+
         "      (ReliableStars.sCorMagI > - 99) AND \n"+
         "      (POWER(muAcosD,2) + POWER(muD,2) > 4 * \nSQRT(POWER(muAcosD * sigMuAcosD,2) + POWER(muD * sigMuD,2)))   \n";
      
      parser.parseStatement(s);
  
      System.out.println(parser);
      
   }
   
}

/*
 $Log: SqlParser.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/21 10:58:25  kea
 Renaming package.

 Revision 1.1.2.1  2006/04/20 15:18:03  kea
 Adding old query code into oldquery directory (rather than simply
 chucking it away - bits may be useful).

 Revision 1.3.2.1  2006/04/10 16:17:44  kea
 Bits of registry still depending (implicitly) on old Query model, so
 moved this sideways into OldQuery, changed various old-model-related
 classes to use OldQuery and slapped deprecations on them.  Need to
 clean them out eventually, once registry can find another means to
 construct ADQL from SQL, etc.

 Note that PAL build currently broken in this branch.

 Revision 1.3  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.2.82.2  2006/02/20 19:42:08  kea
 Changes to add GROUP-BY support.  Required adding table alias field
 to ColumnReferences, because otherwise the whole Visitor pattern
 falls apart horribly - no way to get at the table aliases which
 are defined in a separate node.

 Revision 1.2.82.1  2006/02/16 17:13:05  kea
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

 Revision 1.2  2005/03/21 18:31:51  mch
 Included dates; made function types more explicit

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.2  2005/02/17 18:19:24  mch
 More rearranging into seperate packages

 Revision 1.1.1.1  2005/02/16 17:11:23  mch
 Initial checkin

 Revision 1.1.2.2  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 Revision 1.4.6.5  2004/12/07 23:46:32  mch
 more doc

 Revision 1.4.6.4  2004/12/07 21:21:09  mch
 Fixes after a days integration testing

 Revision 1.4.6.3  2004/12/03 11:58:57  mch
 various fixes while at data mining

 Revision 1.4.6.2  2004/11/29 21:52:18  mch
 Fixes to skynode, log.error(), getstem, status logger, etc following tests on grendel

 Revision 1.4.6.1  2004/11/17 11:15:46  mch
 Changes for serving images

 Revision 1.4  2004/11/09 18:13:51  mch
 Fix so that double quotes can indicate string constant

 Revision 1.3  2004/11/09 17:42:22  mch
 Fixes to tests after fixes for demos, incl adding closable to targetIndicators

 Revision 1.2  2004/11/03 03:49:41  mch
 Added raw search field parsing

 Revision 1.1  2004/10/12 22:46:42  mch
 Introduced typed function arguments

 Revision 1.3  2004/10/08 09:40:52  mch
 Started proper ADQL parsing

 Revision 1.2  2004/10/07 10:34:44  mch
 Fixes to Cone maker functions and reading/writing String comparisons from Query

 Revision 1.1  2004/10/06 21:12:16  mch
 Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger

 Revision 1.10  2004/09/01 21:37:20  mch
 Fix for lower case keywords

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



