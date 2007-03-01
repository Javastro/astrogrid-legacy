/*$Id: CommandLineApplicationDescriptionFactory.java,v 1.6 2007/03/01 18:34:12 gtr Exp $
 * Created on 02-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.commandline.digester;

import org.astrogrid.applications.commandline.CommandLineApplicationDescription;

import org.apache.commons.digester.AbstractObjectCreationFactory;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.picocontainer.ComponentAdapter;
import org.picocontainer.PicoContainer;
import org.xml.sax.Attributes;

/** factory - part of digester mechanism for producing an application descruption.
 * @author Noel Winstanley nw@jb.man.ac.uk 02-Jun-2004
 *
 */
public class CommandLineApplicationDescriptionFactory extends AbstractObjectCreationFactory {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(CommandLineApplicationDescriptionFactory.class);

    private static final String NAME_ATTR = "name";
    
    /** Construct a new CommandLineApplicationDescriptionFactory
     * 
     */
    public CommandLineApplicationDescriptionFactory(PicoContainer container) {
        super();
        this.container = container;
        this.factory = container.getComponentAdapterOfType(CommandLineApplicationDescription.class); // or should I generalize here?
    }
    protected final ComponentAdapter factory;
    protected final PicoContainer container;

    /**
     * @see org.apache.commons.digester.AbstractObjectCreationFactory#createObject(org.xml.sax.Attributes)
     */
    public Object createObject(Attributes arg0) throws Exception {
        String id = "unknown";
        if (arg0.getValue(NAME_ATTR) != null) {
            id = arg0.getValue(NAME_ATTR);
        }
        if (id.startsWith("ivo://")) {
          id = id.substring(6);
        }
        logger.debug("Creating application description for '" + id + "'");
               
       CommandLineApplicationDescription descr = (CommandLineApplicationDescription)factory.getComponentInstance(container);
       descr.setName(id);
       return descr;
      
    }
}


/* 
$Log: CommandLineApplicationDescriptionFactory.java,v $
Revision 1.6  2007/03/01 18:34:12  gtr
The constants describing XML structure are now local to this class.

Revision 1.5  2006/06/15 15:26:03  clq2
ea-gtr-1648

Revision 1.4.116.1  2006/06/13 17:08:09  gtr
It now allows the name attribute for the applications to be given with or without the ivo:// prefix.

Revision 1.4  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.3.72.1  2005/03/11 11:22:16  nw
adjusted to fit with pico 1.1

Revision 1.3  2004/08/27 12:45:52  pah
get cardinality working for commandline digester

Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:57:47  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/