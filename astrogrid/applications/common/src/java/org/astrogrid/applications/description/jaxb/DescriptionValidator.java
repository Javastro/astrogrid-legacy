/*
 * $Id: DescriptionValidator.java,v 1.2 2009/07/01 13:29:49 pah Exp $
 * 
 * Created on 18 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.jaxb;

import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.namespace.QName;

import net.ivoa.resource.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.description.execution.ExecutionSummaryType;
import org.astrogrid.contracts.Namespaces;

/**
 * Utility class to assist with validating the jaxb based descriptions.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 * @TODO make the validator use schema as well...
 */
public class DescriptionValidator {
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(DescriptionValidator.class);

    public static class Validation {
        public boolean valid;
        public String message;
        public Validation(boolean v, String m){
            valid = v;
            message = m;
        }
    }
    /**
     * Validates a resource object.
     * @param appdesc a java object that has suitable jaxb annotations
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Validation validate(final Resource appdesc)
    {
	JAXBElement jaxobj = new JAXBElement(new QName(Namespaces.RI.getNamespace(),"Resource"),Resource.class,appdesc);
        return validate(jaxobj);
    }
    
    @SuppressWarnings("unchecked")
    public static Validation validate(final ExecutionSummaryType execution){
	JAXBElement jaxobj = new JAXBElement(new QName(Namespaces.CEAT.getNamespace(),"ExecutionSummary"),ExecutionSummaryType.class,execution);
        return validate(jaxobj);
 
    }
    


 

    /**
     * Validates a generic object
     * @param appdesc a java object that has suitable jaxb annotations
     * 
     * @return
     */
    public static Validation validate(final Object appdesc)
    {
	  String name = appdesc.getClass().getCanonicalName();
	  try {
	    JAXBContext jc = CEAJAXBContextFactory.newInstance();
	         Marshaller m = jc.createMarshaller();
	          m.setProperty( Marshaller.JAXB_FORMATTED_OUTPUT, 
	                Boolean.TRUE );
	          ValidationEventCollector handler  = new ValidationEventCollector();
		  m.setEventHandler(handler );
		  StringWriter sw = new StringWriter();
	          m.marshal( appdesc, sw);
	          StringBuffer message = new StringBuffer();
	          if(handler.hasEvents())
	          {
	            logger.error("invalid - "+name);
	              for (int i = 0; i < handler.getEvents().length; i++) {
			ValidationEvent array_element = handler.getEvents()[i];
		    logger.error("validation error - "
			    + array_element.toString(), null);
		    message.append(array_element.toString());
		    message.append("\n");
		    }
	             logger.debug(sw.toString());        
	          }
	          
                  return new Validation(!handler.hasEvents(), message.toString());
	} catch (JAXBException e) {
	    logger.error("validation errror - "+name, e);
	    return new Validation(false, "validation error - "+name+" "+ e.getMessage());
	}
         
    }
    
}


/*
 * $Log: DescriptionValidator.java,v $
 * Revision 1.2  2009/07/01 13:29:49  pah
 * validator improvement - returns the validation error string
 *
 * Revision 1.1  2009/02/26 12:25:48  pah
 * separate more out into cea-common for both client and server
 *
 * Revision 1.3  2008/09/18 09:13:39  pah
 * improved javadoc
 *
 * Revision 1.2  2008/09/03 14:18:56  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.1.2.5  2008/06/10 20:16:43  pah
 * annot do such strict schema validation as resources often have capabilities with types from several schema - it seems that jaxb can only cope with validating against a single schema.
 *
 * Revision 1.1.2.4  2008/04/23 14:14:30  pah
 * ASIGNED - bug 2749: make sure all CECs use the  ThreadPoolExecutionController
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2749
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
