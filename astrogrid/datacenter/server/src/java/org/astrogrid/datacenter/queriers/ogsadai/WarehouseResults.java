/*
 * $Id: WarehouseResults.java,v 1.3 2004/03/09 21:54:58 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.queriers.ogsadai;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import org.astrogrid.datacenter.queriers.QueryResults;
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

public class WarehouseResults implements QueryResults
{
  protected Document results = null;
   
  /**
   * Construct this wrapper around the given VOTable Document.
   */
  public WarehouseResults(Document results) {
    this.results = results;
  }
   
  /**
   * Return the VOTable document on demand.
   */
  public Document toVotable() {
    return this.results;
  }

  /** Stream version of the writer */
   public void toVotable(OutputStream out) throws IOException {
      toVotable(new OutputStreamWriter(out));
   }
   
   
  /**
   * Return VOTable results to given outputstream.
   */
  public void toVotable(Writer out) throws IOException
  {
    throw new IOException("Not implemented yet");
    /*
    try
    {
       PrintStream printOut = new PrintStream(new BufferedOutputStream(out));
      //DO SOMETHING
       printOut.flush();
    }
    */
  }
}

/*
 $Log: WarehouseResults.java,v $
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
