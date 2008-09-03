/*$Id: RegistryEntryBuilderTest.java,v 1.6 2008/09/03 14:19:00 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.description.registry;

import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.base.TestApplicationDescriptionLibrary;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;

/**
 * Test to see if the registry entry is valid in various ways.
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author pharriso@eso.org May 25, 2005
 *
 */

public class RegistryEntryBuilderTest extends RegistryEntryBuilderTestBase {
   /**
     * Constructor for RegistryEntryBuilderNewTest.
     * @param arg0
     */
    public RegistryEntryBuilderTest(String arg0) {
        super(arg0);
    }
    /**
    * @return
     * @throws ParameterDescriptionNotFoundException 
    */
   protected ApplicationDescriptionLibrary createDesciptionLibrary() throws ParameterDescriptionNotFoundException {
      return new TestApplicationDescriptionLibrary("ivo://astrogrid.org/testapp", "testapp");
   }    
}


/* 
$Log: RegistryEntryBuilderTest.java,v $
Revision 1.6  2008/09/03 14:19:00  pah
result of merge of pah_cea_1611 branch

Revision 1.5.24.4  2008/05/17 16:45:01  pah
tidy tests to make sure more are passing

Revision 1.5.24.3  2008/04/01 13:50:07  pah
http service also passes unit tests with new jaxb metadata config

Revision 1.5.24.2  2008/03/26 17:15:39  pah
Unit tests pass

Revision 1.5.24.1  2008/03/19 23:10:55  pah
First stage of refactoring done - code compiles again - not all unit tests passed

ASSIGNED - bug 1611: enhancements for stdization holding bug
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611

Revision 1.5  2007/01/16 09:53:49  gtr
Many spurious import statements were removed.

Revision 1.4  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.3.68.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.3.54.2  2005/06/02 14:57:28  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.3.54.1  2005/05/31 12:58:26  pah
moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"

Revision 1.3  2004/11/27 13:20:03  pah
result of merge of pah_cea_bz561 branch

Revision 1.2.90.1  2004/11/04 16:49:17  pah
put in the authorityid into the testapp name

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.7.4.1  2004/05/28 10:23:10  nw
checked in early, broken version - but it builds and tests (fail)
 
*/