/*
 * $Id: SRI.java,v 1.1 2005/02/14 20:47:38 mch Exp $
 *
 * (C) Copyright Astrogrid...
 */

package org.astrogrid.slinger;


/**
 * A store resource *identifier* is used to indicate a point that can be resolve
 * to a URI.  I'd rather use/subclass the URI class but rather annoyingly it's final...
 *
 */

public interface SRI {

   /** Returns a URI of the identifier */
   public String toURI();

}
/*
 $Log: SRI.java,v $
 Revision 1.1  2005/02/14 20:47:38  mch
 Split into API and webapp

 Revision 1.2  2005/01/26 17:31:56  mch
 Split slinger out to scapi, swib, etc.

 Revision 1.1.2.1  2005/01/26 14:35:14  mch
 Separating slinger and scapi

 

 */



