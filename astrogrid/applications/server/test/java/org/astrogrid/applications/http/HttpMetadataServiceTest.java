/*
 * $Id: HttpMetadataServiceTest.java,v 1.2 2011/09/02 21:55:54 pah Exp $
 *
 * Created on 14-Jun-2005 by Paul Harrison (pharriso@eso.org)
 * Copyright 2005 ESO. All rights reserved.
 *
 * This software is published under the terms of the ESO
 * Software License, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
 */

package org.astrogrid.applications.http;

import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.contracts.MockNonSpringConfiguredConfig;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.StandardApplicationDescriptionFactory;
import org.astrogrid.applications.description.registry.RegistryEntryBuilderTestBase;
import org.astrogrid.applications.http.test.TestHttpApplicationLibrary;

/**
 * @author Paul Harrison (pharriso@eso.org) 14-Jun-2005
 * @version $Name:  $
 * @since initial Coding
 */
public class HttpMetadataServiceTest extends RegistryEntryBuilderTestBase {


   /*
    * @see TestCase#setUp()
    */
   @Override
protected ApplicationDescriptionLibrary createDesciptionLibrary()
	throws Exception {
    Configuration conf = new MockNonSpringConfiguredConfig();

   return
            new TestHttpApplicationLibrary(conf.getApplicationDescriptionUrl(),new StandardApplicationDescriptionFactory(conf));
}


}

/*
 * $Log: HttpMetadataServiceTest.java,v $
 * Revision 1.2  2011/09/02 21:55:54  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.6.2  2011/09/02 19:42:18  pah
 * change setup of dynamic description library
 *
 * Revision 1.1.6.1  2009/07/15 10:01:00  pah
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2907
 * NEW - bug 2851: generalized DAL applications
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2851
 * NEW - bug 2931: upgrades for 2009.2
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2931
 * NEW - bug 2920: upgrade to uws 1.0
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2920
 *
 * Revision 1.1  2008/09/10 23:27:17  pah
 * moved all of http CEC and most of javaclass CEC code here into common library
 *
 * Revision 1.8  2008/09/03 14:18:33  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.7.42.2  2008/08/02 13:32:32  pah
 * safety checkin - on vacation
 *
 * Revision 1.7.42.1  2008/04/01 13:50:07  pah
 * http service also passes unit tests with new jaxb metadata config
 *
 * Revision 1.7  2006/03/17 17:50:58  clq2
 * gtr_1489_cea correted version
 *
 * Revision 1.5  2006/03/07 21:45:26  clq2
 * gtr_1489_cea
 *
 * Revision 1.2.34.2  2006/01/26 13:18:42  gtr
 * Refactored.
 *
 * Revision 1.2.34.1  2005/12/22 13:56:03  gtr
 * Refactored to match the other kinds of CEC.
 *
 * Revision 1.2  2005/07/05 08:26:56  clq2
 * paul's 559b and 559c for wo/apps and jes
 *
 * Revision 1.1.2.1  2005/06/14 09:49:32  pah
 * make the http cec only register itself as a ceaservice - do not try to reregister any cea applications that it finds
 *
 */
