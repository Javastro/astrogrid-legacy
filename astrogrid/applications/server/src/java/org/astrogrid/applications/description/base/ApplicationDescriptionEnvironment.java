/*$Id: ApplicationDescriptionEnvironment.java,v 1.8 2006/01/10 11:26:52 clq2 Exp $
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


import org.astrogrid.applications.description.BaseApplicationDescriptionLibrary;
import org.astrogrid.applications.manager.idgen.IdGen;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import junit.framework.Test;

/** A container for some components generally used within {@link org.astrogrid.applications.description.ApplicationDescription} implementations.
 * <p>
 * The components are bundled into this container to make it simpler to add further ones later
 * <p />
 * At present, this class containts a unique-id-generator, and library of indirection handlers.
 * @todo could add hash map, so providers can stuff their own things in here? -becomes a kind of context object. unsure whether this is a good idea - extension may be better.
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
public class ApplicationDescriptionEnvironment implements ComponentDescriptor {
    /** Construct a new ApplicationDescriptionEnvironment
     * 
     */
    public ApplicationDescriptionEnvironment(IdGen id,ProtocolLibrary lib, BaseApplicationDescriptionLibrary.AppAuthorityIDResolver appAuthorityIDResolver) {        
        super();
        this.id =id;
        this.lib = lib;
        this.authIDresolver = appAuthorityIDResolver;
    }
    protected final IdGen id;
    protected final ProtocolLibrary lib;
    protected final BaseApplicationDescriptionLibrary.AppAuthorityIDResolver authIDresolver;
   
    
    /**Access the unique id generator
     * @return the IdGen for this server
     * @see org.astrogrid.applications.manager.idgen
     */
    public IdGen getIdGen() {
        return this.id;
    }
    
    /** Access the library of protcols that paramters can be indirected through
     * @return the protocol library.
     * @see org.astrogrid.applications.param.indirect
     */
    public ProtocolLibrary getProtocolLib() {
        return this.lib;
    }
    
    /**Access the resolver that will return the appropriate authorityID that applications can be registered under.
    * @return the resolver
    */
   public BaseApplicationDescriptionLibrary.AppAuthorityIDResolver getAuthIDResolver()
    {
       return this.authIDresolver;
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
Revision 1.8  2006/01/10 11:26:52  clq2
rolling back to before gtr_1489

Revision 1.6  2005/07/05 08:27:01  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.5.68.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.5.54.1  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.5  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.4.70.1  2004/11/09 09:21:16  pah
initial attempt to rationalise authorityID use & self registering

Revision 1.4  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.3  2004/07/26 00:58:22  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/