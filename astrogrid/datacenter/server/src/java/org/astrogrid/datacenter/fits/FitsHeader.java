/*
 * $Id: FitsHeader.java,v 1.2 2003/11/26 18:46:55 mch Exp $
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
}

/*
$Log: FitsHeader.java,v $
Revision 1.2  2003/11/26 18:46:55  mch
First attempt to generate index from FITS files

Revision 1.1  2003/11/25 11:04:11  mch
New FITS io package

 */

