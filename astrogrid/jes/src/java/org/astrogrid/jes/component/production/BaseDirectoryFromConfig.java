/*$Id: BaseDirectoryFromConfig.java,v 1.4 2004/03/15 23:45:07 nw Exp $
 * Created on 07-Mar-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.jes.component.production;

import org.astrogrid.config.Config;
import org.astrogrid.jes.component.descriptor.SimpleComponentDescriptor;
import org.astrogrid.jes.impl.workflow.FileJobFactoryImpl.BaseDirectory;

import java.io.File;

/** Configuration object for {@link org.astrogrid.jes.impl.workflow.FileJobFactoryImpl}
 * <p>
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 07-Mar-2004
 */
public class BaseDirectoryFromConfig extends SimpleComponentDescriptor implements BaseDirectory {
    /** key to look in config for base directory */
    public static final String BASE_DIR_KEY = "jes.jobfactory.file.basedir";
     public BaseDirectoryFromConfig(Config conf) {
         String fileLoc = conf.getString(BASE_DIR_KEY,System.getProperty("java.io.tmpdir"));
         baseDir = new File(fileLoc);
         name = "FileJobFactory - Base Directory configuration";
        description = "Loads base-directory configuration parameter for FileJobFactory from Config\n" +
            "key :" + BASE_DIR_KEY            + "\n current value:" + baseDir.getAbsolutePath();
     }
     private final File baseDir;
    /**
     * @see org.astrogrid.jes.impl.workflow.FileJobFactoryImpl.BaseDirectory#getDir()
     */
    public File getDir() {
        return baseDir;
    }

}


/* 
$Log: BaseDirectoryFromConfig.java,v $
Revision 1.4  2004/03/15 23:45:07  nw
improved javadoc

Revision 1.3  2004/03/15 01:30:06  nw
factored component descriptor out into separate package

Revision 1.2  2004/03/07 21:04:38  nw
merged in nww-itn05-pico - adds picocontainer

Revision 1.1.2.1  2004/03/07 20:39:26  nw
added implementation of a self-configuring production set of component
 
*/