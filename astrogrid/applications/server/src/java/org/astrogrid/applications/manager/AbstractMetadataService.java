/*
 * $Id: AbstractMetadataService.java,v 1.4 2009/07/01 14:27:11 pah Exp $
 * 
 * Created on 18 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.manager;

import javax.xml.bind.JAXBException;
import javax.xml.parsers.ParserConfigurationException;

import net.ivoa.resource.Resource;
import net.ivoa.resource.registry.iface.VOResources;

import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.applications.description.jaxb.DescriptionValidator;
import org.w3c.dom.Document;

/**
 * The common functionality of a {@link MetadataService}
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public abstract class AbstractMetadataService implements MetadataService{

    
 
    public AbstractMetadataService(){
	super();

   }

    protected Document marshall(Resource desc)
    throws ParserConfigurationException, JAXBException, MetadataException {
	Document retval = CEAJAXBUtils.marshall(desc);
        return retval;
    }
    protected Document marshall(VOResources desc)
    throws ParserConfigurationException, JAXBException, MetadataException {
	Document retval = CEAJAXBUtils.marshall(desc);
        return retval;
    }

     
    protected boolean isValid(Resource desc)
    {
	return DescriptionValidator.validate(desc).valid;
    }
}

/*
 * $Log: AbstractMetadataService.java,v $
 * Revision 1.4  2009/07/01 14:27:11  pah
 * registration template directly argument of builder object - removed from config
 *
 * Revision 1.3  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.5  2008/09/03 12:22:55  pah
 * improve unit tests so that they can run in single eclipse gulp
 *
 * Revision 1.1.2.4  2008/05/13 15:57:32  pah
 * uws with full app running UI is working
 *
 * Revision 1.1.2.3  2008/03/27 13:34:36  pah
 * now producing correct registry documents
 *
 * Revision 1.1.2.2  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 * Revision 1.1.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 *
 */
