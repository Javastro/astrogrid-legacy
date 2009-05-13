/*
 * $Id: Piper.java,v 1.1.1.1 2009/05/13 13:20:35 gtr Exp $
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid Software License,
 * a copy of which has been included with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.io;

import java.io.*;

import org.astrogrid.io.piper.SizeWarningListener;
import org.astrogrid.io.piper.StreamPiper;

/**
 * Helper class for connecting streams
 *
 * @author M Hill
 */

public class Piper
{
   
   /** Utility class - should not be instantiated */
   private Piper() {}
   
   /**
    * Helper method for reading all the bytes from the given input stream
    * and sending them to the given output stream.  Remember that it is much
    * more efficient to use Buffered streams. @see bufferedPipe.
    */
   public static void pipe(InputStream in, OutputStream out) throws IOException
   {
      StreamPiper piper = new StreamPiper();
      piper.pipe(in, out, new SizeWarningListener());
   }
   
   /** Convenience routine to wrap given streams in Buffered*putStreams and
    * pipe
    */
   public static void bufferedPipe(InputStream in, OutputStream out) throws IOException
   {
      out = new BufferedOutputStream(out);
      pipe(new BufferedInputStream(in), out);
      out.flush();
      
   }

   
   /**
    * Helper method for reading all the bytes from the given reader
    * and sending them to the given writer.
    */
   public static void pipe(Reader in, Writer out) throws IOException
   {
      char[] block = new char[StreamPiper.DEFAULT_BLOCK_SIZE];
      int total = 0;
      int read = in.read(block);
      total += read;
      SizeWarningListener listener = new SizeWarningListener();
      
      while (read > -1)
      {
         out.write(block,0, read);
         read = in.read(block);
         total += read;
         listener.blockPiped(total);
      }
   }

   /**
    * Convenience routine to wrap given readers/writers in Buffereds
    */
   public static void bufferedPipe(Reader in, Writer out) throws IOException
   {
      out = new BufferedWriter(out);
      pipe(new BufferedReader(in), out);
      out.flush();
   }
   
}

/* $Log: Piper.java,v $
 * Revision 1.1.1.1  2009/05/13 13:20:35  gtr
 *
 *
/* Revision 1.2  2006/09/26 15:34:42  clq2
/* SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal
/*
/* Revision 1.1.2.1  2006/09/11 11:40:46  kea
/* Moving slinger functionality back into DSA (but preserving separate
/* org.astrogrid.slinger namespace for now, for easier replacement of
/* slinger functionality later).
/*
/* Revision 1.2  2005/03/22 16:17:33  mch
/* Extended Piper to a package that has listening, thread spawning, etc
/*
/* Revision 1.1  2005/02/15 18:33:20  mch
/* multiproject-ed
/*
/* Revision 1.5  2004/09/24 14:17:40  pah
/* added excessive transfer size warning at 30M
/*
/* Revision 1.4  2004/03/02 11:58:00  mch
/* Fixed buffer not flushing
/*
/* Revision 1.3  2004/02/17 14:31:49  mch
/* Minor changes to please checkstyle
/*
/* Revision 1.2  2004/02/15 23:17:59  mch
/* minor doc changes
/*
/* Revision 1.1  2003/12/09 13:01:33  mch
/* Moved Piper to common
/* */
