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
Revision 1.1  2003/10/06 18:55:21  mch
Naughtily large set of changes converting to SOAPy bean/interface-based delegates

*/
