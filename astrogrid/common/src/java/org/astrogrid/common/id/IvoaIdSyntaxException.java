/*
 * $Id: IvoaIdSyntaxException.java,v 1.2 2008/09/17 08:16:05 pah Exp $
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

import org.astrogrid.AstroGridException;
import org.astrogrid.common.AGCommonException;

/**
 * An attempt has been made to create an invalid @link org.eso.vos.common.IvoaId.
 * @author Paul Harrison (pharriso@eso.org) 24-Aug-2005
 * @version $Name:  $
 * @since initial Coding
 * @see org.eso.vos.common.IvoaId
 */
public class IvoaIdSyntaxException extends AGCommonException {

   /**
    * @param arg0
    * @param arg1
    */
   public IvoaIdSyntaxException(String arg0, Throwable arg1) {
      super(arg0, arg1);
     
   }

   /**
    * @param arg0
    */
   public IvoaIdSyntaxException(String arg0) {
      super(arg0);
      
   }
   

  
}


/*
 * $Log: IvoaIdSyntaxException.java,v $
 * Revision 1.2  2008/09/17 08:16:05  pah
 * result of merge of pah_community_1611 branch
 *
 * Revision 1.1.2.1  2008/05/17 20:55:13  pah
 * safety checkin before interop
 *
 * Revision 1.1  2005/08/24 11:54:20  pharriso
 * an invariant class to represent IVOA IDs Hopefully more sensible to use than the AG IVORN class.
 *
 */
