/*
 * $Id: WarehouseResults.java,v 1.8 2004/03/17 12:25:51 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.ogsadai;

import java.io.IOException;
import java.io.Writer;
import java.io.File;
import org.astrogrid.datacenter.queriers.QueryResults;
import org.astrogrid.datacenter.queriers.status.QuerierProcessingResults;
import org.astrogrid.util.DomHelper;
import org.w3c.dom.Document;

/**
 * A simple wrapper to hold a VOTable results Document produced by
 * a {@link WarehouseQuerier}.
 *
 * @TOFIX This may need extending with more functionality to do with
 * examining metadata, etc.
 *
 * @author K Andrews
 * @version 1.0
 * @see WarehouseQuerier
 */

public class WarehouseResults extends QueryResults
{
  protected File results = null;
   
  /**
   * Construct this wrapper around the given VOTable Document.
   */
  public WarehouseResults(File results) {
    this.results = results;
  }
   
   /** Count */
   public int getCount() {
      return -1;
   }
   
  /**
   * Return VOTable results to given outputstream.
   */
  public void toVotable(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
  {
     throw new UnsupportedOperationException("Not yet implemented - here RSN");
  }
  
  /**
   * Return results as CSV to given outputstream.
   */
  public void toCSV(Writer out, QuerierProcessingResults statusToUpdate) throws IOException
  {
     throw new UnsupportedOperationException("Not yet implemented - use VOTABLE format");
  }
  
}

/*
 $Log: WarehouseResults.java,v $
 Revision 1.8  2004/03/17 12:25:51  kea
 Oops, fixing slips.

 Revision 1.7  2004/03/17 12:20:55  kea
 Removing XSLT rowset->VOTable conversions, now done in OGSA-DAI.
 Interim checkin, end-to-end not working yet.

 Revision 1.6  2004/03/14 02:17:07  mch
 Added CVS format and emailer

 Revision 1.5  2004/03/12 04:45:26  mch
 It05 MCH Refactor

 Revision 1.4  2004/03/10 02:36:25  mch
 Added getCount

 Revision 1.3  2004/03/09 21:54:58  mch
 Added Writer methods to toVotables for JSPs

 Revision 1.2  2004/01/24 20:44:25  gtr
 Merged from GDW-integration branch.

 Revision 1.1.2.1  2004/01/22 14:56:32  gtr
 Transfered from astrogrid/warehouse, with package name changed to be in
 org.astrogrid.datacenter.*.

 Revision 1.2  2003/12/08 20:16:54  kea
 Added JavaDoc.  Changed properties to use Java-style capitalisation.
 Misc. small tidyings.

 Revision 1.1  2003/11/26 19:47:29  kea
 Customised results class to provide VOTable results.

*/
