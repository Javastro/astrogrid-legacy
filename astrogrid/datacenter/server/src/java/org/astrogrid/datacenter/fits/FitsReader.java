/*
 $Id: FitsReader.java,v 1.4 2003/11/28 18:20:32 mch Exp $

 Copyright (c) etc
 */

package org.astrogrid.datacenter.fits;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * An interface so that appilcations that want to read FITS files can do so
 * no matter where they are. For example, it may be that a FITS file is remote
 * and can only be accessed by a URL - which means it must be streamed only.
 * Whereas a local file can be random accessed much more efficiently.  But simple
 * applications that want to read them may not care about where they are, so
 * both accessors define this reader interface.
 * @see FitsStreamReader FitsFile
 */
public interface FitsReader
{
   //using these helps avoid typos...
   public final static int CARD_SIZE = 80;
   public final static int BLOCK_SIZE = 2880;
   
   /**
    * Loads the header into a set of key/value pairs.  NB this reads up to the
    * END marker, or the optionally given stop keyword.  This means that, if
    * we know something of the file and only want to read useful information, we
    * can stop before having to read the whole thing.
    * <p>
    * For example, some of the JBO fits files have huge HISTORY blocks, so we
    * can specify "HISTORY AIP" as an 'endWord' and when they are encountered,
    * the reader will stop reading.
    */
   public void readHeaderKeywords(FitsHeader header, String endWord) throws IOException;
   
   public void readData(FitsHdu hdu) throws IOException;

}

/*
 $Log: FitsReader.java,v $
 Revision 1.4  2003/11/28 18:20:32  mch
 Debugged fits readers

 Revision 1.3  2003/11/28 16:10:30  nw
 finished plugin-rewrite.
 added tests to cover plugin system.
 cleaned up querier & queriermanager. tested

 Revision 1.2  2003/11/26 18:46:55  mch
 First attempt to generate index from FITS files

 Revision 1.1  2003/11/25 11:04:11  mch
 New FITS io package

 Revision 1.1.1.1  2003/08/25 18:36:27  mch
 Reimported to fit It02 source structure

 Revision 1.1  2003/07/03 18:14:51  mch
 Fits file handling

 */
