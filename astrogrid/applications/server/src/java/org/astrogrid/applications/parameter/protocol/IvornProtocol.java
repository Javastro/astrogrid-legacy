/*$Id: IvornProtocol.java,v 1.5 2008/09/04 19:10:53 pah Exp $
 * Created on 16-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter.protocol;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;

import junit.framework.Test;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.filemanager.common.BundlePreferences;
import org.astrogrid.security.SecurityGuard;
import org.astrogrid.store.Ivorn;
import org.springframework.stereotype.Component;

/** protocol implementation for ivo:/
 * @author Noel Winstanley nw@jb.man.ac.uk 16-Jun-2004
 *
 */
@Component
public class IvornProtocol implements Protocol , ComponentDescriptor{
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(IvornProtocol.class);

    /** Construct a new IvornProtocol
     * 
     */
    public IvornProtocol() {
        super();
        factory = new FileManagerClientFactory(NO_PREFETCH_POLICY);
    }
    
    protected final FileManagerClientFactory factory;
    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#getProtocolName()
     */
    public String getProtocolName() {
        return Ivorn.SCHEME;
    }
    
    //
    protected static final BundlePreferences NO_PREFETCH_POLICY  = new BundlePreferences();
    static {
        NO_PREFETCH_POLICY.setFetchParents(false);
        NO_PREFETCH_POLICY.setMaxExtraNodes(new Integer(0));
        NO_PREFETCH_POLICY.setPrefetchDepth(new Integer(0));
    }
    /**
     * @see org.astrogrid.applications.parameter.protocol.Protocol#createIndirectValue(java.net.URI, SecurityGuard)
     * @todo find nice way to pass correct user value in here.
     * @todo cache FileManagerClientFactory?
     */
    public ExternalValue createIndirectValue(final URI reference, SecurityGuard secGuard) throws InaccessibleExternalValueException {
        final Ivorn ivorn;
        try {
            ivorn = new Ivorn(reference.toString());
        }
        catch (URISyntaxException e) {
            throw new InaccessibleExternalValueException(reference.toString(),e);
        }      
        return new ExternalValue() {
            FileManagerClient client= factory.login();
           
            public InputStream read() throws InaccessibleExternalValueException {
                try {
                    FileManagerNode n = client.node(ivorn);
                    return n.readContent();
                } catch (Exception e) {
                    logger.debug("Could not read ivorn" + ivorn,e);
                    throw new InaccessibleExternalValueException(ivorn.toString(),e);
                } 
            }

            public OutputStream write() throws InaccessibleExternalValueException {
                try {
                    FileManagerNode n = null;
                    if (client.exists(ivorn) != null) {
                        n = client.node(ivorn);
                    } else {
                        n = client.createFile(ivorn);
                    }
                return n.writeContent();
                } catch (Exception e) {
                    logger.debug("Could not write ivorn " + ivorn,e);
                    throw new InaccessibleExternalValueException(ivorn.toString(),e);
                }                
            }
        };
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "IvornProtocol";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "protocol adapter for ivo: urns";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return null;
    }
}


/* 
$Log: IvornProtocol.java,v $
Revision 1.5  2008/09/04 19:10:53  pah
ASSIGNED - bug 2825: support VOSpace
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2825
Added the basic implementation to support VOSpace  - however essentially untested on real deployement

Revision 1.4  2008/09/03 14:18:57  pah
result of merge of pah_cea_1611 branch

Revision 1.3.146.1  2008/04/04 15:46:08  pah
Have got bulk of code working with spring - still need to remove all picocontainer refs
ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.3  2005/03/31 08:34:17  nw
fixed problem of writing to non-existent ivorns

Revision 1.2  2005/03/13 07:13:39  clq2
merging jes-nww-686 common-nww-686 workflow-nww-996 scripting-nww-995 cea-nww-994

Revision 1.1.110.1  2005/03/11 11:20:58  nw
replaced VoSpaceClient with FileManagerClient

Revision 1.1  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core
 
*/