/*
 * $Id: Sql2Adql074.java,v 1.4 2004/08/18 16:27:15 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.query.criteria.Condition;
import org.astrogrid.datacenter.query.criteria.ColumnReference;
import org.astrogrid.datacenter.query.results.TableResultsDefinition;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Translates from SQL to ADQL 0.7.4
 */

public class Sql2Adql074  {
   
   public static String translate(String sql) throws IOException {
      SqlParser parser = new SqlParser();
      parser.parseStatement(sql);

      return Query2Adql074.makeAdql(parser.getQuery(), "ADQL generated from SQL: "+sql);
   }
   
  
   /**
    * Test harness
    */
   public static void main(String[] args) throws IOException {
      
      if ((args != null) && (args.length>0) && (args[0] != null) ) {
         
         System.out.println(Sql2Adql05.translate(args[0]));
         
      }
      else {
         System.out.println("Usage: "+Sql2Adql05.class+" <sql> ");
         System.out.println("");
         System.out.println("eg: ");
         
         //-- proper SQL --
         String s = "SELECT * FROM CHARLIE";
         
         String adql = Sql2Adql05.translate(s);
     
         System.out.println(adql);
         
         //-- proper SQL --
         s = "SELECT S.RA,    T.WIBBLE, UNDIE.PANTS, ETC.ETC FROM A, B,  CHARLIE AS C WHERE C.X > 3 AND C.Y < 4 OR A.RA > B.RA";
         
         adql = Sql2Adql05.translate(s);
     
         System.out.println(adql);
      }
   }
}

/*
 $Log: Sql2Adql074.java,v $
 Revision 1.4  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder


 */


