/*$Id: ApplicationDescriptionEnvironment.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.base;

import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.parameter.indirect.IndirectionProtocolLibrary;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import junit.framework.Test;

/** Class containing components generally-used within appDescriptions and apps.
 * at present, just the unique-id-generator, and library of indirection handlers. byt may add more here later.
 * @todo add hash map, so providers can stuff their own things in here?
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class ApplicationDescriptionEnvironment implements ComponentDescriptor {
    /** Construct a new ApplicationDescriptionEnvironment
     * 
     */
    public ApplicationDescriptionEnvironment(IdGen id,IndirectionProtocolLibrary lib) {        
        super();
        this.id =id;
        this.lib = lib;
    }
    protected final IdGen id;
    protected final IndirectionProtocolLibrary lib;
    
    public IdGen getIdGen() {
        return this.id;
    }
    
    public IndirectionProtocolLibrary getProtocolLib() {
        return this.lib;
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "ApplicationDescriptionEnvironment";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Container for components used by applications";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
    
  
    
}


/* 
$Log: ApplicationDescriptionEnvironment.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/