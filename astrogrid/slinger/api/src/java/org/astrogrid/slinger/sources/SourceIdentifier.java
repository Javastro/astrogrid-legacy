/*
 * $Id: SourceIdentifier.java,v 1.1 2005/02/14 20:47:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger.sources;


import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.security.Principal;

/**
 * Indicates anything that can be resolved into an inputstream, or an input reader
 * <p>
 * Essentially TargetIndicators provide a suitably typed and validated way of
 * passing around where things might come from, rather than having to pass around
 * Strings to indicate Identifiers, and separate methods to handle Readers/Streams.
 *
 */

public interface SourceIdentifier  {


   /** All targets must be able to resolve to a reader. The user is required
    * for permissions */
   public Reader resolveReader(Principal user) throws IOException;
   
   /** All targets must be able to resolve to a stream.  The user is required
    * for permissioning. */
   public InputStream resolveInputStream(Principal user) throws IOException;
   
   /** Used to get the mime type of the data about to be read.  Note that many
    * implementations (such as disk files?) do not have this
    * capability, and so often this will
    * do nothing. */
   public String getMimeType(Principal user) throws IOException;
}
/*
 $Log: SourceIdentifier.java,v $
 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.3  2005/01/26 17:31:57  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.2  2004/12/03 11:50:19  mch
 renamed Msrl etc to separate from storeclient ones.  Prepared for SRB

 Revision 1.1.2.1  2004/11/22 00:46:28  mch
 New Slinger Package

 */



