/*$Id: CompositeApplicationDescriptionLibrary.java,v 1.5 2005/01/23 12:52:26 jdt Exp $
 * Created on 09-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.component.descriptor.ComponentDescriptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import junit.framework.Test;
import junit.framework.TestSuite;

/** An {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary} that composes together other <tt>ApplicationDescriptionLibrary</tt>s 
 * @author Noel Winstanley nw@jb.man.ac.uk 09-Jun-2004
 *
 */
public class CompositeApplicationDescriptionLibrary implements ApplicationDescriptionLibrary, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(CompositeApplicationDescriptionLibrary.class);

    /** Construct a new CompositeApplicationDescriptionLibrary
     * 
     */
    public CompositeApplicationDescriptionLibrary() {
        super();
    }
    protected final Set libs = new HashSet();
    /**
     * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getDescription(java.lang.String)
     */
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        logger.debug("Getting description for "+name+" from composite library");
        logger.debug("Composite library contains "+libs.size()+" libraries");
        for (Iterator i = libs.iterator(); i.hasNext(); ) {
            ApplicationDescriptionLibrary lib = (ApplicationDescriptionLibrary)i.next();
            logger.debug("Checking library "+lib+".  This library has "+lib.getApplicationNames().length+" entries");
            try {
                ApplicationDescription desc = lib.getDescription(name);
                logger.debug("Found description");
                // shouldn't return null, but sanity check.
                if (desc != null) {
                    return desc;
                }
            } catch (ApplicationDescriptionNotFoundException e) {
                // oh well, not there then.
            }
        }
        // not found
        throw new ApplicationDescriptionNotFoundException(name);
    }
    /**
     * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getApplicationNames()
     */
    public String[] getApplicationNames() {
        List results = new ArrayList();
        for (Iterator i = libs.iterator(); i.hasNext(); ) {
            ApplicationDescriptionLibrary lib = (ApplicationDescriptionLibrary)i.next();
            String[] names = lib.getApplicationNames();
            results.addAll(Arrays.asList(names));
        }
        return (String[])results.toArray(new String[]{});
    }
    
    /** add another library to the composite */
    public void addLibrary(ApplicationDescriptionLibrary lib) {
        libs.add(lib);
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Composite Application Description Library";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        StringBuffer libDescs = new StringBuffer();
        for (Iterator i = libs.iterator(); i.hasNext(); ) {
            ApplicationDescriptionLibrary lib = (ApplicationDescriptionLibrary)i.next();
            libDescs.append("\n");
            if (lib instanceof ComponentDescriptor) {
                 libDescs.append(((ComponentDescriptor)lib).getDescription());
            } else {
                libDescs.append(lib.getClass().getName());
                libDescs.append("\n");
                libDescs.append(Arrays.asList(lib.getApplicationNames()));                
            }
        }
        return "Composes a set of description libraries into one. Currently contains " + libDescs.toString();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
        // join together container installationi tests.
        TestSuite suite = new TestSuite("Tests for " + this.getName());
        for (Iterator i = libs.iterator(); i.hasNext(); ) {
            Object o = i.next();
            if (o instanceof ComponentDescriptor) {
                Test t= ((ComponentDescriptor)o).getInstallationTest();
                if (t != null) {
                    suite.addTest(t);
                }
            }
        }
        return suite;
    }
}


/* 
$Log: CompositeApplicationDescriptionLibrary.java,v $
Revision 1.5  2005/01/23 12:52:26  jdt
merge from cea_jdt_902

Revision 1.4.88.1  2005/01/22 13:57:01  jdt
added some logging.

Revision 1.4  2004/08/11 16:52:11  nw
added logger

Revision 1.3  2004/07/26 00:57:46  nw
javadoc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/