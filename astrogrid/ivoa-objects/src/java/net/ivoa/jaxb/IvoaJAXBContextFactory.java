/*
 * $Id: IvoaJAXBContextFactory.java,v 1.2 2011/09/13 13:43:33 pah Exp $
 * 
 * Created on 12 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package net.ivoa.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/**
 * Utility Factory to create JAXB contexts which know about all the classes necessary for Registry Data Moel.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 12 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 6
 */
public class IvoaJAXBContextFactory {

    
    private static JAXBContext jc = null;

    public static JAXBContext newInstance() throws JAXBException
    {
	if(jc == null)
	  jc = JAXBContext.newInstance("net.ivoa.resource"
	  		+ ":net.ivoa.resource.dataservice"
	  		+ ":net.ivoa.resource.registry"
	  		+ ":net.ivoa.resource.registry.iface"
	  		+ ":net.ivoa.resource.applications"
	  		+ ":net.ivoa.resource.cone"
	  		+ ":net.ivoa.resource.sia"
	  		+ ":net.ivoa.resource.standard"
	  		);
	return jc;

    }
}


/*
 * $Log: IvoaJAXBContextFactory.java,v $
 * Revision 1.2  2011/09/13 13:43:33  pah
 * result of merge of branch ivoa_pah_2931
 *
 * Revision 1.1.2.4  2011/09/01 09:38:21  pah
 * include more schema
 *
 * Revision 1.1.2.3  2011/06/11 17:49:39  pah
 * sorted out ResourceName mappings - down to the minimum number of tables
 *
 * Revision 1.1.2.2  2011/06/09 22:18:56  pah
 * basic VOResource schema nearly done - but not got save/recall working
 *
 * Revision 1.1.2.1  2011/06/01 15:23:42  pah
 * first pass at adding JPA for rdb as well as xml representation.
 *
 */
