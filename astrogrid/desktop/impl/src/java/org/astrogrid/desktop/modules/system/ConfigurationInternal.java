/*$Id: ConfigurationInternal.java,v 1.1 2005/08/25 16:59:58 nw Exp $
 * Created on 23-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.system.Configuration;

/** internal interface to the configuration component - adds a 'reset' method 
 * - too dangerous to expose on the public interface.
 * @author Noel Winstanley nw@jb.man.ac.uk 23-Aug-2005
 *
 */
public interface ConfigurationInternal extends Configuration {
    /** reset the configuration back to factory settings. any user history or configuraiton will be lost 
     * @throws ServiceException*/
    public void reset() throws ServiceException;
}


/* 
$Log: ConfigurationInternal.java,v $
Revision 1.1  2005/08/25 16:59:58  nw
1.1-beta-3
 
*/