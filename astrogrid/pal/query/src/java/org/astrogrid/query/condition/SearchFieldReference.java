/*
 * $Id: SearchFieldReference.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.query.condition;


/**
 * For storing a reference to a field that can be searched on.  Might be a UCD
 * or a column name, etc
 *
 * Rather naughtily implements both NumbericExpression and StringExpression,
 * whereas I think there ought to be separate types...
 */

public abstract class SearchFieldReference implements NumericExpression, StringExpression {
   
   protected String datasetName = null;
   
   /**
    * Sets DatasetName
    */
   public void setDatasetName(String datasetName) {
      this.datasetName = datasetName;
   }
   
   /**
    * Returns DatasetName
    */
   public String getDatasetName() {
      return datasetName;
   }

   /** Returns some kind of general reference - this is a bit of a cheat just now
    * to get ADQL 0.7.4 to provide keyword searches */
   public abstract String getField();
   
}

/*
$Log: SearchFieldReference.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.3  2004/11/03 05:14:33  mch
Bringing Vizier back online

Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */

