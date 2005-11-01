/*$Id: ConfigurationKeys.java,v 1.1 2005/11/01 09:19:46 nw Exp $
 * Created on 25-Oct-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.system;

/** Central place to store names of common config keys.
 * @author Noel Winstanley nw@jb.man.ac.uk 25-Oct-2005
 *
 */
public interface ConfigurationKeys {
    /** config key that points to the working directory */
    public static final String WORK_DIR_KEY = "working.dir";
    /** list of keys that must be present in the configuration system
     */
    public static final String[] REQUIRED_KEYS = { "org.astrogrid.registry.query.endpoint",
            "org.astrogrid.registry.admin.endpoint",
            "org.astrogrid.registry.query.altendpoint",
            "jes.job.controller.endpoint",
            WORK_DIR_KEY
            };

}


/* 
$Log: ConfigurationKeys.java,v $
Revision 1.1  2005/11/01 09:19:46  nw
messsaging for applicaitons.
 
*/