/*$Id: CommandLineApplicationDescriptionFactory.java,v 1.2 2004/07/01 11:07:59 nw Exp $
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

    /** Construct a new CommandLineApplicationDescriptionFactory
     * 
     */
    public CommandLineApplicationDescriptionFactory(PicoContainer container) {
        super();
        this.factory = container.getComponentAdapterOfType(CommandLineApplicationDescription.class); // or should I generalize here?
    }
    protected final ComponentAdapter factory;

    /**
     * @see org.apache.commons.digester.AbstractObjectCreationFactory#createObject(org.xml.sax.Attributes)
     */
    public Object createObject(Attributes arg0) throws Exception {
        String id = "unknown";
        if (arg0.getValue(CommandLineApplicationDescriptionsConstants.NAME_ATTR) != null) {
            id = arg0.getValue(CommandLineApplicationDescriptionsConstants.NAME_ATTR);
        }
        logger.info("Creating application description for '" + id + "'");
               
       //return new CommandLineApplicationDescription(id,factory);
       CommandLineApplicationDescription descr = (CommandLineApplicationDescription)factory.getComponentInstance();
       descr.setName(id);
       return descr;
      
    }
}


/* 
$Log: CommandLineApplicationDescriptionFactory.java,v $
Revision 1.2  2004/07/01 11:07:59  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/07/01 01:43:39  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:57:47  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/