/*$Id: HttpApplicationCEAServerTest.java,v 1.14 2008/09/03 14:18:33 pah Exp $
 * Created on 30-July-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.http;
import org.astrogrid.applications.test.AbstractComponentManagerTestCase;
import org.astrogrid.registry.client.RegistryDelegateFactory;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/** test of a cea server configured with the javaclass backend.
 * @author jdt/Noel Winstanley nw@jb.man.ac.uk 21-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 9 Jun 2008
 * @todo exercise other components of server here..
 *
 */
@RunWith(SpringJUnit4ClassRunner.class) 
@ContextConfiguration(locations={"/cecspringTest.xml"}) 
public class HttpApplicationCEAServerTest extends AbstractComponentManagerTestCase {
   
    public void setUp() throws Exception {
    	super.setUp();
        System.setProperty(RegistryDelegateFactory.QUERY_URL_PROPERTY, "http://dummy");//FIXME should this really be necessary - but in the registry delegate to NPE?
   }
    
    
 //TODO need more tests to ensure that this is set up properly 
    
}


/* 
$Log: HttpApplicationCEAServerTest.java,v $
Revision 1.14  2008/09/03 14:18:33  pah
result of merge of pah_cea_1611 branch

Revision 1.13.42.2  2008/08/02 13:32:32  pah
safety checkin - on vacation

Revision 1.13.42.1  2008/04/01 13:50:07  pah
http service also passes unit tests with new jaxb metadata config

Revision 1.13  2006/03/17 17:50:58  clq2
gtr_1489_cea correted version

Revision 1.11  2006/03/07 21:45:26  clq2
gtr_1489_cea

Revision 1.8.34.1  2005/12/22 13:56:03  gtr
Refactored to match the other kinds of CEC.

Revision 1.8  2005/07/05 08:26:56  clq2
paul's 559b and 559c for wo/apps and jes

Revision 1.7.100.1  2005/06/09 08:47:32  pah
result of merging branch cea_pah_559b into HEAD

Revision 1.7.86.1  2005/06/08 22:10:45  pah
make http applications v10 compliant

Revision 1.7  2004/09/10 11:04:22  jdt
Organised imports.

Revision 1.6  2004/09/07 08:13:20  pah
remove incorrect import

Revision 1.5  2004/09/01 16:36:50  jdt
Was failing....perhaps a change in the server code since we branched?

Revision 1.4  2004/09/01 15:42:26  jdt
Merged in Case 3


 
*/