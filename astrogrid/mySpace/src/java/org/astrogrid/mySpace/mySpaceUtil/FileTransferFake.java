package org.astrogrid.mySpace.mySpaceUtil;

import java.io.*;
import java.net.*;


/**
 * A manager for the download of a single data-set from a URL.
 *
 * The client should create a fresh FileTransfer for each transfer.
 * There is no valid way to reuse one of these object for a second
 * transfer.  The source and destination of the data are set when
 * the FileTransfer is created.
 *
 * <p>
 * This version is a simple fake for the real FileTransfer class.  It
 * performs a simple retrieval from a single URL.
 * 
 * <p>
 * Note that this version of the class no longer has the same name as
 * as the real version.
 *
 * @author A C Davenhall
 */
public class FileTransferFake
{  private String  importUrl;  // The URL to be retrieved.
   private String  fileName;   // The name of the file to be written.

//
// Constructor.

/**
 * Creates a FileTransfer with a single source URL and
 * one destination file.
 *
 * @param source      the URL from which to transfer.
 * @param destination the name of the file in which to put the data.
 */

  public FileTransferFake (final String source, final String destination)
  {  importUrl = source;
     fileName = destination;
  }

//
// Methods.

// --------------------------------------------------------------------

/**
 * Executes the transfer using the sources and destination
 * set during construction.
 */

  public final void transfer ()
  {  try
     {  URL url = new URL(importUrl);
        InputStream iStream = url.openStream();

        int b;

        File outFile = new File(fileName);
        PrintWriter out = new PrintWriter(new FileWriter(outFile) );

        while( (b = iStream.read())  !=  -1)
        {  out.write(b);
        }

        out.close();
     }
     catch (Exception e)
     {  importUrl = null;
     }
  }

/**
 * Returns the URL used in the last transfer.  Returns null if
 * no transfer has been made since construction.
 *
 * @return the URL used in the transfer.
 */

  public final String getChosenUrl ()
  {  return importUrl;
  }
}
