/*
 * $Id: FitsHeader.java,v 1.3 2003/11/28 18:20:32 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.datacenter.fits;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

/**
 * A Fits header is the ascii 80-bytes keyword/value pair lines of the primary hdu (header/data unit)
 * or extension unit.  This class represents those values.  Position is useful
 * (esp after use of the 'continue' keyword and multiline comments) but also
 * is quick access given a particular key, so this has both Vector and Hashtable
 *
 * @author M Hill
 */


public class FitsHeader
{
   Hashtable indexed = new Hashtable();
   Vector list = new Vector();

   public FitsHeader()
   {
   }
   
   public void add(FitsKeyword keyword)
   {
      list.add(keyword);
      indexed.put(keyword.getKey(), keyword);
   }
   
   public FitsKeyword get(String key)
   {
      return (FitsKeyword) indexed.get(key);
   }
   
   public Enumeration getKeywords()
   {
      return list.elements();
   }
   
   public FitsKeyword[] getList()
   {
      return (FitsKeyword[]) list.toArray(new FitsKeyword[] {} );
   }

   /**
    * Convenience routine to get the value of the given key.  Equivelent to
    * .get(key).getValue()
    */
   public String getValue(String key)
   {
      return get(key).getValue();
   }
   
   /**
    * returns the number of axis given by the keyword NAXIS
    */
   public int getNumAxis()
   {
      return get(FitsKeyword.NAXIS).toInt();
   }
   
   
   /** Compute size of associated data block in bytes
    *  From P Grosbol
    */
   
   public long getDataSize()
   {
      //number of axis
      int naxis = getNumAxis();
      if (naxis < 1)
      {
         //empty
         return 0;
      }

      //number of bytes per pixel
      int bitsPerPix = get("BITPIX").toInt();
      int bytesPerPix = Math.abs(bitsPerPix)/8;

      //calculate total number of points (ie size of each dimension given by
      //NAXISn for total number of dimensions given by NAXIS)
      long  numPixels = 1;
      for (int n=1; n<=naxis; n++)
      {
         // don't know what this does...if (type==Fits.RGROUP && n==1) continue;
         int numPoints = get("NAXIS" + n).toInt();
         numPixels = numPixels * numPoints;
      }
      long datasize = numPixels;
      
      //add parameter block size (whatever that is)
      FitsKeyword pcount = get("PCOUNT");
      if (pcount != null)
      {
         datasize = datasize + pcount.toInt();
      }
      
      //multiple with group count (?)
      FitsKeyword gcount = get("GCOUNT");
      if (gcount != null)
      {
         datasize = datasize * gcount.toInt();
      }
      
      return datasize * bytesPerPix;
   }
   
}

/*
$Log: FitsHeader.java,v $
Revision 1.3  2003/11/28 18:20:32  mch
Debugged fits readers

Revision 1.2  2003/11/26 18:46:55  mch
First attempt to generate index from FITS files

Revision 1.1  2003/11/25 11:04:11  mch
New FITS io package

 */

