/*
 * $Id: WarehouseResults.java,v 1.2 2003/12/08 20:16:54 kea Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.warehouse.queriers.ogsadai;

import java.io.IOException;
import java.io.OutputStream;

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
   
  /**
   * Return VOTable results to given outputstream.  
   */
  public void toVotable(OutputStream out) throws IOException
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
 Revision 1.2  2003/12/08 20:16:54  kea
 Added JavaDoc.  Changed properties to use Java-style capitalisation.
 Misc. small tidyings.

 Revision 1.1  2003/11/26 19:47:29  kea
 Customised results class to provide VOTable results.

*/
