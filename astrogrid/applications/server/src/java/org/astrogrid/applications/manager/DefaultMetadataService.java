/*$Id: DefaultMetadataService.java,v 1.2 2004/07/01 11:16:22 nw Exp $
 * Created on 21-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.ProvidesVODescription;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.registry.beans.resource.VODescription;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.StringReader;
import java.io.StringWriter;

import junit.framework.Test;
import junit.framework.TestCase;

/** Standard implementation of the CEA metadata component.
 * @author Noel Winstanley nw@jb.man.ac.uk 21-May-2004
 *
 */
public class DefaultMetadataService implements MetadataService, ComponentDescriptor {
    private static final Log logger = LogFactory.getLog(DefaultMetadataService.class);
       
    /** Construct a new StandardCEAMetaData
     * 
     */
    public DefaultMetadataService( ProvidesVODescription provider) {
       this.provider = provider;
    }
    //protected final ApplicationDescriptionLibrary library;
    protected final ProvidesVODescription provider;
  
    

    public String returnRegistryEntry() throws CeaException {
 
       StringWriter sw;
       try {
          sw = new StringWriter();
          provider.getVODescription().marshal(sw);
          sw.close();                
          return sw.toString();
         
       }
       catch (Exception e) {
         throw new CeaException("problem returning registry entry", e);
       }
    }    

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Standard CEA Server Description";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return "Standard implementation of the service description component";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        return new InstallationTest("testGetRegistryEntry");
    }
    
    public class InstallationTest extends TestCase {

        public InstallationTest(String arg0) {
            super(arg0);
        }
        public void testGetRegistryEntry() throws Exception {
            String entry = returnRegistryEntry();
            assertNotNull(entry);
            //@todo need more here really... but can't parse back into VODescripton, as castor fails to round-trip
        }

}
}


/* 
$Log: DefaultMetadataService.java,v $
Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/