/*
 * $Id: FitsHdu.java,v 1.1 2005/02/17 18:37:34 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.fits;


/**
 * Represents a 'header/data unit' which may be the original 'primary' hdu
 * at the beginning of a fits file, or an extension that also consists of a
 * header/data pairing.
 *
 * @author M Hill
 */

public class FitsHdu
{
   private FitsHeader header = null;
   private FitsData data = null;

   public FitsHdu() {}
   
   public FitsHdu(FitsHeader aHeader)
   {
      this.header = aHeader;
   }

   public FitsHdu(FitsHeader aHeader, FitsData aDataBlock)
   {
      this.header = aHeader;
      this.data = aDataBlock;
   }
   
   public FitsHeader getHeader() { return header; }
   public FitsData   getData()   { return data;   }
   
   public void setHeader(FitsHeader aHeader) { this.header = aHeader; }
   public void setData(FitsData aDataBlock) { this.data = aDataBlock; }
   

}

/*
$Log: FitsHdu.java,v $
Revision 1.1  2005/02/17 18:37:34  mch
*** empty log message ***

Revision 1.1.1.1  2005/02/16 17:11:23  mch
Initial checkin

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.2  2003/11/28 18:20:32  mch
Debugged fits readers

Revision 1.1  2003/11/25 11:04:11  mch
New FITS io package

 */

