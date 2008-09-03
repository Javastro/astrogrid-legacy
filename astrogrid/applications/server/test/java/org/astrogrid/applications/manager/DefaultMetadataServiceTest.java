/*$Id: DefaultMetadataServiceTest.java,v 1.4 2008/09/03 14:19:04 pah Exp $
 * Created on 26-May-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.manager;

import javax.xml.transform.TransformerConfigurationException;

import junit.framework.TestCase;

import org.apache.axis.utils.XMLUtils;
import org.astrogrid.applications.description.base.DummyVODescriptionProvider;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.test.AstrogridAssert;
import org.w3c.dom.Document;

/**
 * @author Noel Winstanley nw@jb.man.ac.uk 26-May-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 27 Mar 2008
 *
 */
public class DefaultMetadataServiceTest extends TestCase {
    public static final String RES_KEY = "test-entry";
    public static final String AUTH_ID = "org.astrogrid.test";
    public static final String RES_NAME = AUTH_ID + "/" + RES_KEY;
    /**
     * Constructor for BaseCEAServerDescriptionTest.
     * @param arg0
     * @throws TransformerConfigurationException 
     */
    public DefaultMetadataServiceTest(String arg0) throws TransformerConfigurationException {
        super(arg0);
        provider = new DummyVODescriptionProvider(AUTH_ID, RES_KEY);
    }
    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
    }
    protected final MetadataService provider ;
    /*
     * @see TestCase#tearDown()
     */
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testReturnRegistryEntry() throws Exception{
        Document regString= provider.returnRegistryEntry();
        assertNotNull(regString);
        XMLUtils.PrettyDocumentToStream(regString, System.out);
        AstrogridAssert.assertSchemaValid(regString, "VOResources", SchemaMap.ALL);
 
    }
    


}


/* 
$Log: DefaultMetadataServiceTest.java,v $
Revision 1.4  2008/09/03 14:19:04  pah
result of merge of pah_cea_1611 branch

Revision 1.3.102.3  2008/06/10 20:01:39  pah
moved ParameterValue and friends to CEATypes.xsd

Revision 1.3.102.2  2008/04/01 13:50:07  pah
http service also passes unit tests with new jaxb metadata config

Revision 1.3.102.1  2008/03/27 13:34:36  pah
now producing correct registry documents

Revision 1.3  2005/07/05 08:27:00  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.2.172.1  2005/06/09 08:47:33  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.2.158.2  2005/06/02 14:57:29  pah
merge the ProvidesVODescription interface into the MetadataService interface

Revision 1.2.158.1  2005/05/31 12:58:26  pah
moved to v10 schema interpretation - this means that the authorityID is read directly with the applicaion "name"

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:47  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again

Revision 1.1.2.1  2004/05/28 10:23:11  nw
checked in early, broken version - but it builds and tests (fail)
 
*/