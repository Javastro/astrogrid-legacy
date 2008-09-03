/*
 * $Id: ApplicationEnvironmentUnavailableException.java,v 1.2 2008/09/03 14:18:55 pah Exp $
 * 
 * Created on 03-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.environment;

import org.astrogrid.applications.CeaException;

/**
 * Exception thrown when the application environment is not retrievable.
 * @author Paul Harrison (pharriso@eso.org) 03-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class ApplicationEnvironmentUnavailableException extends CeaException {

   /**
    * @param message
    */
   public ApplicationEnvironmentUnavailableException(String message) {
      super(message);
   }

   /**
    * @param message
    * @param cause
    */
   public ApplicationEnvironmentUnavailableException(String message,
         Throwable cause) {
      super(message, cause);
   }

}


/*
 * $Log: ApplicationEnvironmentUnavailableException.java,v $
 * Revision 1.2  2008/09/03 14:18:55  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.1  2008/06/11 14:31:43  pah
 * merged the ids into the application execution environment
 *
 * Revision 1.2  2005/07/05 08:27:00  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.4.1  2005/06/09 08:47:32  pah
 * result of merging branch cea_pah_559b into HEAD
 *
 * Revision 1.1.2.1  2005/06/03 16:01:48  pah
 * first try at getting commandline execution log bz#1058
 *
 */
