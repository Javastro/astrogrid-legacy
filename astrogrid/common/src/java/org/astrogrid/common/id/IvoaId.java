/*
 * $Id: IvoaId.java,v 1.2 2008/09/17 08:16:05 pah Exp $
 * 
 * Created on 24-Aug-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.common.id;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.net.URI;
import java.net.URISyntaxException;

/**
 * Class to represent an IVOA identifier as described in <a
 * href="http://www.ivoa.net/Documents/latest/IDs.html">IVOA Resource Identifer
 * Standard</a> In many cases it is better to simply use the URI type instead
 * of this class - especially where web services are concerned.
 * 
 * @author Paul Harrison (pharriso@eso.org) 24-Aug-2005
 * @version $Name:  $
 * @since initial Coding
 */
public final class IvoaId {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory.getLog(IvoaId.class);

   /**
    * the IVOA ID is supposed to be a URI, so it is stored as one.
    */
   private final URI value;

   /**
    * @throws IvoaIdSyntaxException
    * 
    */
   public IvoaId(URI uri) throws IvoaIdSyntaxException {
      this(uri.toString());
   }

   public IvoaId(String id) throws IvoaIdSyntaxException {
      try {
         value = new URI(id);
      }
      catch (URISyntaxException e) {
         throw new IvoaIdSyntaxException("error creating IVOA ID", e);
      }
      checkValid(value);
   }

   /**
    * @param id
    * @throws IvoaIdSyntaxException
    */
   private static boolean checkValid(URI id) throws IvoaIdSyntaxException
   {
      if (!id.getScheme().equals("ivo")) {
         throw new IvoaIdSyntaxException("scheme not ivo: for " + id.toString());
      }
      return true;
   }

   public static boolean checkIfValid(String id)
   {
      boolean retval = true;
      try {
          new IvoaId(id);
      }
      catch (IvoaIdSyntaxException e) {
         retval = false;
      }
      return retval;
   }

   public String getFragment()
   {
      return value.getFragment();
   }
   
   /*
    * (non-Javadoc)
    * 
    * @see java.lang.Object#toString()
    */
   public String toString()
   {
      return value.toString();
   }

   public URI toURI()
   {
      return value;
   }

   
   /**
    * convienence method that will return an instance based on the argument. Catches the exception that the normal constuctor throws.
    * @param ivo
    * @return null if there is a problem
    */
   public static IvoaId createInstance(String ivo){
      IvoaId retval = null;
      try {
         retval = new IvoaId(ivo);
      }
      catch (IvoaIdSyntaxException e) {
         logger.warn("error trying to create instance", e);
      }
      return retval;
   }

/* (non-Javadoc)
 * @see java.lang.Object#hashCode()
 */
public int hashCode() {
	final int PRIME = 31;
	int result = 1;
	result = PRIME * result + ((value == null) ? 0 : value.hashCode());
	return result;
}

/* (non-Javadoc)
 * @see java.lang.Object#equals(java.lang.Object)
 */
public boolean equals(Object obj) {
	if (this == obj)
		return true;
	if (obj == null)
		return false;
	if (getClass() != obj.getClass())
		return false;
	final IvoaId other = (IvoaId) obj;
	if (value == null) {
		if (other.value != null)
			return false;
	} else if (!value.equals(other.value))
		return false;
	return true;
}
}

/*
 * $Log: IvoaId.java,v $
 * Revision 1.2  2008/09/17 08:16:05  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.3  2006/09/15 14:34:48  pharriso
 * make suitable for comparisions in collections
 *
 * Revision 1.2  2005/09/22 15:45:53  pharriso
 * added a static convience method to create instance without thowing exceptions...
 *
 * Revision 1.1  2005/08/24 11:54:20  pharriso
 * an invariant class to represent IVOA IDs Hopefully more sensible to use than the AG IVORN class.
 *
 */
