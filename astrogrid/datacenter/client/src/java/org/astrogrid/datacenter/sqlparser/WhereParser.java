/*
 * $Id: WhereParser.java,v 1.1 2004/07/07 15:42:39 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import org.astrogrid.datacenter.match.*;


/**
 * A string parser that looks at a Logical expression (ie this AND that, this OR that)
 * and creates a LogicalExpression to model that.
 */

public class WhereParser  {
   

   public static BooleanExpression parseBoolean(String expression) {
   
      int andIndex = expression.toUpperCase().indexOf(" AND "); //surround by spaces so we don't find it in other words

      if (andIndex > -1) {
         //found an AND
         BooleanExpression lhs = parseBoolean(expression.substring(0,andIndex));
         BooleanExpression rhs = parseBoolean(expression.substring(andIndex+6));
         return new LogicalExpression(lhs, "AND", rhs);
      }
      
      int orIndex = expression.toUpperCase().indexOf(" OR "); //surround by spaces so we don't find it in other words

      if (orIndex > -1) {
         //found an AND
         BooleanExpression lhs = parseBoolean(expression.substring(0,orIndex));
         BooleanExpression rhs = parseBoolean(expression.substring(orIndex+5));
         return new LogicalExpression(lhs, "OR", rhs);
      }
   
      int opIndex = expression.toUpperCase().indexOf(" > "); //surround by spaces so we don't find it in other words
      if (opIndex == -1) {
         opIndex =  expression.toUpperCase().indexOf(" < ");
      }
      if (opIndex == -1) {
         opIndex =  expression.toUpperCase().indexOf(" = ");
      }

      if (opIndex > -1) {
         //found an operator
         NumericExpression lhs = parseNumeric(expression.substring(0,opIndex));
         NumericExpression rhs = parseNumeric(expression.substring(opIndex+4));
         return new ComparisonExpression(lhs, expression.substring(opIndex+1,1), rhs);
      }
      
      throw new IllegalArgumentException("'"+expression+"' is not a boolean expression (no logical/comparison operator AND, OR, <, > or =)");
    }
   
    public static NumericExpression parseNumeric(String expression) {
       try {
          int value = Integer.parseInt(expression);
          
          return new Constant(""+value);
       }
       catch (NumberFormatException nfe) {
          //wasn't a number, must be a column reference
          return new ColumnReference(expression);
          
       }
    }
    
    public static void main(String[] args) {
       
       String s = "S.RA > 2 AND S.RA = G.RA AND S.DEC = G.DEC";
       
       BooleanExpression whereClause = WhereParser.parseBoolean(s);
       
       if (whereClause instanceof LogicalExpression) {
          BooleanExpression lhs = ((LogicalExpression) whereClause).getLHS();
       }
       
    }
    
}

/*
$Log: WhereParser.java,v $
Revision 1.1  2004/07/07 15:42:39  mch
Added skeleton to recursive parser

 */

