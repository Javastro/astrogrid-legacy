/*$Id: AsuTwigMaker.java,v 1.2 2006/06/15 16:50:09 clq2 Exp $
 * Created on 13-Nov-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 **/
package org.astrogrid.dataservice.impl.cds;

import org.astrogrid.query.condition.*;

import java.io.IOException;
import java.net.URLEncoder;
import org.astrogrid.io.mime.MimeNames;
import org.astrogrid.query.Query;
import org.astrogrid.query.QueryTraverser;
import org.astrogrid.query.returns.ReturnSpec;
import org.astrogrid.query.returns.ReturnTable;
import org.astrogrid.query.refine.RefineSpec;
import org.astrogrid.query.constraint.ConstraintSpec;
import org.astrogrid.query.sql.SqlParser;

/** Makes an ASU URL 'twig' that can be attached to a URL stem to query
 * an ASU-compatible server.  Actually I think there is only one ASU-compatible
 * server, ie VizieR
 * <p>
 * @see http://vizier.u-strasbg.fr/doc/asu.html
 * <p>
 * @author M Hill
 */
public class AsuTwigMaker extends QueryTraverser  {

   StringBuffer asuTwig = new StringBuffer();

   /** Case is important */
   public void visitColumnReference(ColumnReference colRef) throws IOException {
      //could perhaps examine resource to check case and get it right..
      asuTwig.append(colRef.getColName());
   }

   public void visitAngle(LiteralAngle angle) throws IOException {
      asuTwig.append(angle.getAngle().asDegrees());
   }
   
   public void visitLimit(long limit) throws IOException {
      if (limit>0) {
         asuTwig.append("-out.max="+limit);
      }
   }
   
   public void visitNumber(LiteralNumber number) throws IOException {
      asuTwig.append(number.getValue());
   }
   
   public void visitString(LiteralString string) throws IOException {
      asuTwig.append(string.getValue());
   }

   public void visitDate(LiteralDate date) throws IOException {
      asuTwig.append(URLEncoder.encode(date.getDate().toString()));
   }

   public void visitRawSearchField(RawSearchField field) throws IOException {
      asuTwig.append(field.getField());
   }
   
   
   public void visitCircle(CircleCondition circle) {
      asuTwig.append("&-c.eq="+circle.getEquinox()+
                     "&-c.ra="+circle.getRa().asDegrees()+
                     "&-c.dec="+circle.getDec().asDegrees()+
                     "&-c.rd="+circle.getRadius().asDegrees());
   }
   
   public void visitReturnSpec(ReturnSpec spec) {
      if (!spec.getFormat().equals(ReturnSpec.DEFAULT)) {
         String format = spec.getFormat();
         //better off using human ones for these - the 'mime types' aren't really mime types...
         format = MimeNames.humanFriendly(format).toLowerCase();
         asuTwig.append("&-mime="+format);
      }
      
      String outfields = "";
      if (spec instanceof ReturnTable) {
         Expression[] cols = ((ReturnTable) spec).getColDefs();
         if (cols != null) {
            for (int i = 0; i < cols.length; i++) {
               if (cols[i] instanceof ColumnReference) {
                  outfields=outfields+  ((ColumnReference) cols[i]).getColName()+",";
               }
               else if (cols[i] instanceof RawSearchField) {
                  outfields=outfields+ ((RawSearchField) cols[i]).getField()+",";
               }
               else {
                  throw new IllegalArgumentException("Specify only column/field names in the Return Table Specification (eg SELECT) for ASU queries");
               }
            }
         }
      }
      if (outfields.length()>0) {
         asuTwig.append("&-out="+outfields.substring(0,outfields.length()-1)); //chop off last ','
      }
      else {
         asuTwig.append("&-out.all=1"); //ask for all
      }
   }
   
   
   public void visitFunction(Function function) {
      throw new IllegalArgumentException("Only CIRCLE functions can be used");
   }

   /** ASU can only handle flat comparisons (I believe) so check that the comparison is not nested */
   public void visitNumericComparison(NumericComparison comparison) throws IOException {
      if (!(comparison.getLHS() instanceof SearchFieldReference) || !(comparison.getRHS() instanceof Literal)) {
         throw new IllegalArgumentException("Comparisons are restricted to {field}{condition}{literal} and should not be nested, but they are in "+comparison);
      }
      asuTwig.append("&");
      comparison.getLHS().acceptVisitor(this);
      if (comparison.getOperator()==NumericCompareOperator.NE) {
         asuTwig.append("="+URLEncoder.encode("!="));
      }
      else {
         asuTwig.append("="+URLEncoder.encode(comparison.getOperator().toString()));
      }
      comparison.getRHS().acceptVisitor(this);
   }

   public void visitStringComparison(StringComparison comparison) throws IOException {
      if (!(comparison.getLHS() instanceof SearchFieldReference) || !(comparison.getRHS() instanceof Literal)) {
         throw new IllegalArgumentException("Comparisons are restricted to {field}{condition}{literal} and should not be nested, but they are in "+comparison);
      }
      asuTwig.append("&");
      comparison.getLHS().acceptVisitor(this);
      if (comparison.getOperator()==StringCompareOperator.EQ) {
         asuTwig.append("="+URLEncoder.encode("=="));
      }
      if (comparison.getOperator()==StringCompareOperator.NE) {
         asuTwig.append("="+URLEncoder.encode("!="));
      }
      else if (comparison.getOperator()==StringCompareOperator.LIKE) {
         asuTwig.append("="+URLEncoder.encode("="));
      }
      else {
         asuTwig.append("="+URLEncoder.encode(comparison.getOperator().toString()));
      }
      comparison.getRHS().acceptVisitor(this);
   }
   
   public void visitScope(String[] scope, Query query) {
      if ((scope ==null) || (scope.length==0)) {
         return;
      }
      asuTwig.append("&-source="+scope[0]);
      for (int i = 1; i < scope.length; i++)
      {
         asuTwig.append(","+scope[i]);
      }
   }

   /** @TOFIX-KEA ADD THIS ONE!! */
   public void visitRefineSpec(RefineSpec refineSpec)
   {
   }
   /** @TOFIX-KEA ADD THIS ONE!! */
   public void visitConstraintSpec(ConstraintSpec constraintSpec)
   {
   }
   
   /** returns twig, prepending ? and removing initial & and  */
   public String getAsuTwig() {
      return "?"+asuTwig.toString().substring(1);
   }
   
   /**
    *
    */
   public static void main(String[] args)  throws IOException
   {
      /*
      {
     Query query = SimpleQueryMaker.makeConeQuery(20,30,6);
      AsuTwigMaker t = new AsuTwigMaker();
      
      query.acceptVisitor(t);

      System.out.println(t.getAsuTwig());
      }
       */
      Query query = SqlParser.makeQuery("SELECT ELIAS.RMAG, ELIAS.BMAG FROM ELAIS WHERE CIRCLE('J2000',20,30,6) AND ((ELIAS.RMAG > 5 OR ELIAS.BMAG >= 5) OR ELIAS.COOKED LIKE 'TRUE')");
      
      AsuTwigMaker t = new AsuTwigMaker();
      
      query.acceptVisitor(t);

      System.out.println(t.getAsuTwig());
      
      
   }
}


/*
 $Log: AsuTwigMaker.java,v $
 Revision 1.2  2006/06/15 16:50:09  clq2
 PAL_KEA_1612

 Revision 1.1.2.1  2006/04/20 15:23:08  kea
 Checking old sources in in oldserver directory (rather than just
 deleting them, might still be useful).

 Revision 1.4  2006/03/22 15:10:13  clq2
 KEA_PAL-1534

 Revision 1.3.62.2  2006/02/20 19:42:08  kea
 Changes to add GROUP-BY support.  Required adding table alias field
 to ColumnReferences, because otherwise the whole Visitor pattern
 falls apart horribly - no way to get at the table aliases which
 are defined in a separate node.

 Revision 1.3.62.1  2006/02/16 17:13:05  kea
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

 Revision 1.3  2005/05/27 16:21:02  clq2
 mchv_1

 Revision 1.2.16.1  2005/04/21 17:20:51  mch
 Fixes to output types

 Revision 1.2  2005/03/21 18:45:55  mch
 Naughty big lump of changes

 Revision 1.1.1.1  2005/02/17 18:37:34  mch
 Initial checkin

 Revision 1.1.1.1  2005/02/16 17:11:24  mch
 Initial checkin

 Revision 1.1.2.4  2005/01/24 12:14:26  mch
 Fixes to VizieR proxy and resource stuff

 Revision 1.1.2.3  2004/12/09 10:21:16  mch
 added count asu maker and asu conditions

 Revision 1.1.2.2  2004/12/08 23:23:37  mch
 Made SqlWriter and AdqlWriter implement QueryVisitor

 Revision 1.1.2.1  2004/12/08 18:36:40  mch
 Added Vizier, rationalised SqlWriters etc, separated out TableResults from QueryResults

 */





