/*
 * $Id: SocketTarget.java,v 1.1 2009/05/13 13:20:42 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;

import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.Socket;
import java.security.Principal;

/**
 * Used to indicate the target where the results are to be sent when it's been
 * given as a tcp/ip socket.
 *
 */

public class SocketTarget implements TargetIdentifier {
   
   Socket socket;

   public SocketTarget(Socket targetSocket)  {

      this.socket = targetSocket;
   }

   /** opens output stream to the URL */
   public OutputStream openOutputStream() throws IOException {
      return socket.getOutputStream();
   }

   /** Used to set the mime type of the data about to be sent to the target. . */
   public void setMimeType(String mimeType) throws IOException {
   }

   public String toString() {
      return "[Socket at '"+socket.getLocalAddress()+":"+socket.getLocalPort()+"']";
   }

   /** Resolves writer as a wrapper around resolved outputstream */
   public Writer openWriter() throws IOException {
      return new OutputStreamWriter(openOutputStream());
   }
   
   
}
/*
 $Log: SocketTarget.java,v $
 Revision 1.1  2009/05/13 13:20:42  gtr
 *** empty log message ***

 Revision 1.2  2006/09/26 15:34:42  clq2
 SLI_KEA_1794 for slinger and PAL_KEA_1974 for pal and xml, deleted slinger jar from repo, merged with pal

 Revision 1.1.2.1  2006/09/11 11:40:47  kea
 Moving slinger functionality back into DSA (but preserving separate
 org.astrogrid.slinger namespace for now, for easier replacement of
 slinger functionality later).

 Revision 1.2  2005/05/27 16:21:01  clq2
 mchv_1

 Revision 1.1.10.1  2005/04/21 17:09:03  mch
 incorporated homespace etc into URLs

 Revision 1.1  2005/03/28 01:48:09  mch
 Added socket source/target, and makeFile instead of outputChild


 */



