/*
 * $Id: SearchFieldReference.java,v 1.2 2004/10/18 13:11:30 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.query.condition;


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

}

/*
$Log: SearchFieldReference.java,v $
Revision 1.2  2004/10/18 13:11:30  mch
Lumpy Merge

Revision 1.1.2.1  2004/10/15 19:59:05  mch
Lots of changes during trip to CDS to improve int test pass rate

Revision 1.1  2004/10/06 21:12:16  mch
Big Lump of changes to pass Query OM around instead of Query subclasses, and TargetIndicator mixed into Slinger


 */

