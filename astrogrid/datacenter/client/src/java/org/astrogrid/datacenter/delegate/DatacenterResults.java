/*
 * $Id DatacenterResults.java $
 *
 */

package org.astrogrid.datacenter.delegate;

import org.w3c.dom.Element;

/**
 * For wrapping datacenter results, either votable, fits file or list of
 * urls.
 * Not exactly elegent just now, but it will do...
 *
 * @author M Hill
 */

public class DatacenterResults
{
   private Element votable = null;
   private byte[] fits = null;
   private String[] urls = null;
   
   public DatacenterResults(Element givenVot)
   {
      this.votable = givenVot;
   }

   public DatacenterResults(String[] givenUrls)
   {
      this.urls = givenUrls;
   }
   
   public boolean isVotable()
   {
      return votable != null;
   }
   /** @todo at moment this returns entire response document (containing the votable). maybe needs to be changed to extract VOTABLE from document?
    * 
    * @return
    */
   public Element getVotable()
   {
      return votable;
   }
   
   public boolean isUrls()
   {
      return urls != null;
   }
   
   public String[] getUrls()
   {
      return urls;
   }
   
   public boolean isFits()
   {
      return fits != null;
   }
   
   public byte[] getFits()
   {
      return fits;
   }
}

/*
$Log: DatacenterResults.java,v $
Revision 1.2  2004/01/16 13:25:32  nw
flagged possible bug

Revision 1.1  2003/11/14 00:36:40  mch
Code restructure

Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
