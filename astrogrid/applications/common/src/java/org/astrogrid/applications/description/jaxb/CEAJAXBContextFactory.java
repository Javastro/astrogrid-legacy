/*
 * $Id: CEAJAXBContextFactory.java,v 1.2 2009/06/05 13:06:20 pah Exp $
 * 
 * Created on 12 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Utility Factory to create JAXB contexts which know about all the classes necessary for CEA.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 6
 */
public class CEAJAXBContextFactory {

    
    private static JAXBContext jc = null;

    public static JAXBContext newInstance() throws JAXBException
    {
	if(jc == null)
	  jc = JAXBContext.newInstance("org.astrogrid.applications.description.impl:" +
		        "org.astrogrid.applications.description.execution:" +
	  		"org.astrogrid.applications.description.base:"+
	  		"org.astrogrid.applications.description.cea:"+
	  		"net.ivoa.uws:"+
	  		"net.ivoa.resource.registry.iface:"+
	  		"net.ivoa.resource.dataservice");
	return jc;

    }
}


/*
 * $Log: CEAJAXBContextFactory.java,v $
 * Revision 1.2  2009/06/05 13:06:20  pah
 * RESOLVED - bug 2921: add capabilities to the automatic registration
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2921
 * marshalling of capabilities and namespace changes
 *
 * Revision 1.1  2008/10/06 12:12:37  pah
 * factor out classes common to server and client
 *
 * Revision 1.3  2008/09/24 13:39:42  pah
 * include the uws types in the context
 *
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.4  2008/05/08 22:40:53  pah
 * basic UWS working
 *
 * Revision 1.1.2.3  2008/04/17 16:08:33  pah
 * removed all castor marshalling - even in the web service layer - unit tests passing
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * ASSIGNED - bug 2708: Use Spring as the container
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2708
 * ASSIGNED - bug 2739: remove dependence on castor/workflow objects
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2739
 *
 * Revision 1.1.2.2  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:54  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
