/*$Id: Bootloader.java,v 1.5 2005/11/01 09:19:46 nw Exp $
 * Created on 15-Mar-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.framework;

import org.astrogrid.desktop.framework.descriptors.DescriptorParser;
import org.astrogrid.desktop.framework.descriptors.ModuleDescriptor;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.PicoInitializationException;
import org.picocontainer.Startable;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;

/** Parses a series of module descriptors to populate the registry.
 * @todo replace this with lazy loading of resources, based on lists stored in config.
 * @author Noel Winstanley nw@jb.man.ac.uk 15-Mar-2005
 *
 */
public class Bootloader implements Startable{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(Bootloader.class);

    /** Construct a new Bootloader
     * 
     */
    public Bootloader(ACRImpl reg,DescriptorParser parser) {
        super();
        this.reg = reg;
        this.parser = parser;
    }
    protected final ACRImpl reg;
    protected final DescriptorParser parser;

    /** load in the core modules.
     * @see org.picocontainer.Startable#start()
     */
    public void start() {
        registerModule("system","/org/astrogrid/desktop/modules/system/module.xml");
        registerModule("astrogrid","/org/astrogrid/desktop/modules/ag/module.xml");
        registerModule("ivoa","/org/astrogrid/desktop/modules/ivoa/module.xml");        
        registerModule("cds","/org/astrogrid/desktop/modules/cds/module.xml");
        registerModule("nvo","/org/astrogrid/desktop/modules/nvo/module.xml");
        registerModule("background","/org/astrogrid/desktop/modules/background/module.xml");        
        registerModule("dialogs","/org/astrogrid/desktop/modules/dialogs/module.xml");
        registerModule("ui","/org/astrogrid/desktop/modules/ui/module.xml");            
        registerModule("scripting","/org/astrogrid/desktop/modules/scripting/module.xml");
        registerModule("external","/org/astrogrid/desktop/modules/external/module.xml");  
        
    }
    
/**Parse and process a module descriptor
 * 
 * @param name name of this module - used to generate friendly error and log message
 * @param resourcePath - classpath to this resource
 * @throws IllegalStateException
 */
    private void registerModule(String name, String resourcePath) throws PicoInitializationException {
        logger.info("Looking for " + name + " - " + resourcePath);
        InputStream is = this.getClass().getResourceAsStream(resourcePath);
        if (is == null) {
            logger.fatal("Cannot find module.xml for " + name + " module");
            throw new PicoInitializationException("Cannot find module.xml for " + name + " module");
        }      
        ModuleDescriptor builtin = null;
        try {
            builtin = parser.parse(is);
        } catch (IOException e) {
            logger.fatal("Cannot read module xml for " + name +" module",e);
            throw new PicoInitializationException("Cannot read module xml for " + name +" module",e);
        } catch (SAXException e) {
            logger.fatal("Cannot parse module.xml for " + name +"module",e);
            throw new PicoInitializationException("Cannot parse module xml for " + name +" module",e);
        }
        reg.register(builtin);
        logger.info("Registered " + name);
    }

    /**
     * @see org.picocontainer.Startable#stop()
     */
    public void stop() {
    }

}


/* 
$Log: Bootloader.java,v $
Revision 1.5  2005/11/01 09:19:46  nw
messsaging for applicaitons.

Revision 1.4  2005/10/17 16:02:45  nw
added siap and cone interfaces

Revision 1.3  2005/09/12 15:21:16  nw
reworked application launcher. starting on workflow builder

Revision 1.2  2005/08/25 16:59:58  nw
1.1-beta-3

Revision 1.1  2005/08/11 10:15:00  nw
finished split

Revision 1.7  2005/08/05 11:46:55  nw
reimplemented acr interfaces, added system tests.

Revision 1.6  2005/07/08 11:08:02  nw
bug fixes and polishing for the workshop

Revision 1.5  2005/05/12 15:59:11  clq2
nww 1111 again

Revision 1.3.8.1  2005/05/11 14:25:24  nw
javadoc, improved result transformers for xml

Revision 1.3  2005/04/27 13:42:41  clq2
1082

Revision 1.2.2.2  2005/04/22 15:59:26  nw
made a star documenting desktop.

Revision 1.2.2.1  2005/04/22 10:53:19  nw
added in dialogues module.

Revision 1.2  2005/04/13 12:59:12  nw
checkin from branch desktop-nww-998

Revision 1.1.2.7  2005/04/06 15:04:11  nw
added new front end - more modern, with lots if icons.

Revision 1.1.2.6  2005/04/04 08:49:27  nw
working job monitor, tied into pw launcher.

Revision 1.1.2.5  2005/04/01 19:03:10  nw
beta of job monitor

Revision 1.1.2.4  2005/03/30 12:48:22  nw
added two more lttle modules

Revision 1.1.2.3  2005/03/23 14:36:18  nw
got pw working

Revision 1.1.2.2  2005/03/18 15:47:37  nw
worked in swingworker.
got community login working.

Revision 1.1.2.1  2005/03/18 12:09:31  nw
got framework, builtin and system levels working.
 
*/