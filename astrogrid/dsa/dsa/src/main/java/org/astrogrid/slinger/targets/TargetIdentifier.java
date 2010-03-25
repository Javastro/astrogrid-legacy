/*
 * $Id: TargetIdentifier.java,v 1.2 2010/03/25 10:25:51 gtr Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.targets;


import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;

/**
 * Indicates the target where the results are to be sent.  TargetIndicators
 * represent anything that can be resolved into a stream or writer.  Thus they
 * might be URLsMay be an AGSL, or an email address, or
 * some IVO based thingamy that is still to be resolved.
 * <p>
 * Essentially TargetIndicators provide a suitably typed and validated way of
 * passing around where results are going to go, rather than having to pass around
 * Strings to indicate Identifiers, and separate methods to handle Writers/Streams.
 *
 */

public interface TargetIdentifier  {


   /** All targets must be able to resolve to a writer. The user is required
    * for permissions */
   public Writer openWriter() throws IOException;
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public OutputStream openOutputStream() throws IOException;
   
   /** Used to set the mime type of the data about to be sent to the target. *Must*
    * be set before any data is set, as some targets cannot cope with it afterwards.
    * Note that many target implementations (such as disk files?) do not have this
    * capability, and so often this will
    * do nothing. */
   public void setMimeType(String mimeType) throws IOException;
}