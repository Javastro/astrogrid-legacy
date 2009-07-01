/*
 * $Id: TemplatedServiceDefinition.java,v 1.3 2009/07/01 14:28:43 pah Exp $
 * 
 * Created on 17 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description;

import java.net.URL;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.helpers.DefaultValidationEventHandler;

import net.ivoa.resource.Service;
import net.ivoa.resource.registry.iface.VOResources;

import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;

/**
 * Obtain the service definition from a template file.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 11 Jun 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class TemplatedServiceDefinition extends AbstractServiceDefinition implements ServiceDefinitionFactory {

    

private URL conf;
public URL getConf() {
    return conf;
}

public void setConf(URL conf) {
    this.conf = conf;
}

public TemplatedServiceDefinition(URL url)
{
    this.conf = url;
}
    
    public Service getCECDescription() throws ServiceDescriptionException {
	URL url = conf;
	  try {
	    JAXBContext jc = CEAJAXBContextFactory.newInstance();
	      Unmarshaller um = jc.createUnmarshaller();
	      um.setEventHandler(new DefaultValidationEventHandler());
	      VOResources retval = (VOResources) um.unmarshal(url);
//TODO set the identifier	      retval.setIdentifier(conf.)
	      return (Service) retval.getResource().get(0);
	} catch (JAXBException e) {
	    throw new ServiceDescriptionException("error reading the service description template", e);
	}  

   }

    public String getName() {
        return "template ServiceDefinition url="+conf.toString();

    }
}
/*
 * $Log: TemplatedServiceDefinition.java,v $
 * Revision 1.3  2009/07/01 14:28:43  pah
 * registration template directly argument of builder object - removed from config
 *
 * Revision 1.2  2008/09/03 14:18:43  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.3  2008/06/16 21:58:58  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.1.2.2  2008/03/26 17:15:38  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:53  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
