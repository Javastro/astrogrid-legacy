/*
 * $Id: SRL.java,v 1.1 2005/02/14 20:47:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;



import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.net.URI;
import java.net.URISyntaxException;
import java.security.Principal;

/**
 * A store resource *locator* is used to type a class (eg MSRL) as being one that does not need any
 * more high level resolving to be used to reach the store.
 *
 * Perhaps we should work out a way of using the URL class to do this, but I'm 1) not sure
 * how and 2) not sure it would be straightforward to debug.  ho hum.
 *
 * Replaces (in concept) the AGSL...
 *
 */

public interface SRL extends SRI {

}
/*
 $Log: SRL.java,v $
 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.2  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2005/01/26 14:35:14  mch
 Separating slinger and scapi

 Revision 1.1.2.1  2004/12/08 18:37:11  mch
 Introduced SPI and SPL

 */



