/*
 * $Id: Sql2Adql05.java,v 1.5 2004/08/25 23:38:33 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.sqlparser;
import java.io.IOException;
import java.io.StringWriter;
import org.astrogrid.datacenter.query.condition.Condition;
import org.astrogrid.datacenter.query.condition.ColumnReference;
import org.astrogrid.datacenter.returns.ReturnTable;
import org.astrogrid.io.xml.XmlPrinter;
import org.astrogrid.io.xml.XmlTagPrinter;


/**
 * Translates from SQL to ADQL 0.5
 */

public class Sql2Adql05  {
   
   public static String translate(String sql) throws IOException {
      SqlParser parser = new SqlParser();
      parser.parseStatement(sql);

      return Query2Adql05.makeAdql(parser.getQuery(), "ADQL generated from SQL: "+sql);
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
         
         //example
         s = "SELECT t.a, g.d FROM Tab as a, Tab as d WHERE a.d < d.e AND a.f < d.f";
         System.out.println(Sql2Adql05.translate(s));
      }
   }
}

/*
 $Log: Sql2Adql05.java,v $
 Revision 1.5  2004/08/25 23:38:33  mch
 (Days changes) moved many query- and results- related classes, renamed packages, added tests, added CIRCLE to sql/adql parsers

 Revision 1.4  2004/08/18 16:27:15  mch
 Combining ADQL generators from SQL parser and query builder

 Revision 1.3  2004/08/18 09:17:36  mch
 Improvement: split literals to strings vs numerics, added functions, better class/interface structure, brackets, etc

 Revision 1.2  2004/08/13 09:47:57  mch
 Extended parser/builder to handle more WHERE conditins

 Revision 1.1  2004/08/13 08:52:23  mch
 Added SQL Parser and suitable JSP pages

 Revision 1.1  2004/07/07 15:42:39  mch
 Added skeleton to recursive parser

 */


