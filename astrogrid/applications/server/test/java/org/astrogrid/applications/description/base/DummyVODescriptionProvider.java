/*
 * $Id: DummyVODescriptionProvider.java,v 1.13 2008/09/24 13:40:50 pah Exp $
 * Created on 02-Jun-2004
 * 
 * Copyright (C) AstroGrid. All rights reserved.
 * 
 * This software is published under the terms of the AstroGrid Software License
 * version 1.2, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 *  
 */
package org.astrogrid.applications.description.base;

import java.math.BigInteger;
import java.net.URL;
import javax.xml.transform.TransformerConfigurationException;

import net.ivoa.resource.AccessURL;
import net.ivoa.resource.Contact;
import net.ivoa.resource.Content;
import net.ivoa.resource.ContentLevel;
import net.ivoa.resource.Creator;
import net.ivoa.resource.Curation;
import net.ivoa.resource.Interface;
import net.ivoa.resource.Resource;
import net.ivoa.resource.ResourceName;
import net.ivoa.resource.Service;
import net.ivoa.resource.Source;
import net.ivoa.resource.Type;
import net.ivoa.resource.WebService;
import net.ivoa.resource.registry.iface.VOResources;

import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.cea.CeaCapability;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.manager.AbstractMetadataService;
import org.astrogrid.applications.manager.MetadataService;
import org.joda.time.DateTime;
import org.w3c.dom.Document;

// TODO would probably be better just to read in a dummy instance - this really is testing the object model of the resources
// document....easier next time the schema changes...
public class DummyVODescriptionProvider extends AbstractMetadataService
	implements MetadataService {
    private final String AUTH_ID;

    private final String RES_KEY;

    public DummyVODescriptionProvider(String AUTH_ID, String RES_KEY) throws TransformerConfigurationException {
	super();
	this.AUTH_ID = AUTH_ID;
	this.RES_KEY = RES_KEY;
    }

    public Document getServerDescription() throws MetadataException {
	// right, this is the minimal valid vodescription. hope the schema
	// doesn't
	// change again soon! It did!!
	Resource resource = new Resource();

	resource.setIdentifier("ivo://" + AUTH_ID + "/" + RES_KEY);
	setDublinCore(resource);

	assert isValid(resource) : "Dummy VODescription is not valid";
	try {
	    return marshall(resource);
	} catch (Exception e) {
	   throw new MetadataException("error creating Dummy Service Description", e);
	}
    }

    private void setDublinCore(Resource resource) {
	resource.setTitle("test resource");
	Content content = new Content();
	content.getSubject();
	content.getContentLevel().add(ContentLevel.RESEARCH);
	content.setDescription("test application description");
	content.setReferenceURL("");
	Source source = new Source();
	source.setValue("");
	content.setSource(source);
        Type o = Type.EDUCATION;
	content.getType().add(o );
	
	content.addSubject("subject");
	content.getType().add(Type.BASIC_DATA);
	content.getSource().setValue("Source");// TODO this is not very natural
						// way of setting source,
						// because source is a multiple
						// token....
	resource.setContent(content);
	Curation curation = new Curation();
	Contact contact = new Contact();
	ResourceName name = new ResourceName();
	name.setValue("name");
	contact.setName(name);
	contact.setAddress("address");
	contact.setEmail("email");
	curation.getContact().add(contact);
	Creator creator = new Creator();
	creator.setName(name);
	curation.getCreator().add(creator);

	curation.getContributor().add(name);
	curation.setPublisher(name);
	curation.setVersion("dummy");
	resource.setCuration(curation);
    }

    public URL getRegistrationTemplate() {
	return null;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.applications.component.ProvidesVOResources#getAuthorityID()
     */
    public String getAuthorityID() {
	return AUTH_ID;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.astrogrid.applications.component.ProvidesVODescription#setServerID(java.lang.String)
     */
    public String setServerID(String id) {
	// TODO implement this and refactor the test that uses it...
	throw new UnsupportedOperationException(
		"DummyVODescriptionProvider.setServerID() not implemented");
    }

    /**
     * Reveals the IVORNs for the supported applications.
     */
    public String[] getApplicationIvorns() {
	String[] names = new String[1];
	names[0] = "ivo://" + AUTH_ID + "/" + RES_KEY + "APP";
	return names;
    }

    public Document getApplicationDescription(String id) throws ApplicationDescriptionNotFoundException {
	CeaApplication resource = new CeaApplication();
 //TODO this is not really finding an application
	resource.setIdentifier("ivo://" + AUTH_ID + "/" + RES_KEY+ "APP");
	setDublinCore(resource);

	assert isValid(resource) : "Dummy Application description is not valid";
	try {
	    Document doc = marshall(resource);
	    return doc;
	} catch (Exception e) {
	    throw new ApplicationDescriptionNotFoundException("cannot find the dummy app description", e);
	}
    }

    public Document returnRegistryEntry() throws MetadataException {
	Service service = new Service();
	setDublinCore(service);
	service.setIdentifier("ivo://" + AUTH_ID + "/" + RES_KEY);
	setDates(service);
	service.setStatus("active");
	CeaCapability cap = new CeaCapability();
	Interface intf = new WebService();
	AccessURL accessurl = new AccessURL();
	accessurl.setValue("http://dummy.org/service");
	intf.getAccessURL().add(accessurl );
	cap.getInterface().add(intf );
	cap.getManagedApplications().getApplicationReference().add("ivo://" + AUTH_ID + "/" + RES_KEY+ "APP");
	assert isValid(service) : "Dummy Service description is not valid";
	VOResources resources = new VOResources();
	resources.setFrom(new BigInteger("1"));
	resources.setNumberReturned(new BigInteger("1"));
	resources.setMore(false);
	resources.getResource().add(service);
	try {
	    Document doc = marshall(resources);
	    return doc;
	} catch (Exception e) {
	    throw new MetadataException("cannot create dummy service description", e);
	}
	
	
   }
    private void setDates(Resource r){
	DateTime value = new DateTime();
	r.setCreated(value );
	r.setUpdated(value);
    }
}

/*
 * $Log: DummyVODescriptionProvider.java,v $
 * Revision 1.13  2008/09/24 13:40:50  pah
 * package naming changes
 *
 * Revision 1.12  2008/09/13 09:51:04  pah
 * code cleanup
 *
 * Revision 1.11  2008/09/03 14:18:52  pah
 * result of merge of pah_cea_1611 branch
 *
 * Revision 1.10.2.6  2008/06/16 21:58:59  pah
 * altered how the description libraries fit together  - introduced the SimpleApplicationDescriptionLibrary to just plonk app descriptions into.
 *
 * Revision 1.10.2.5  2008/06/10 20:01:39  pah
 * moved ParameterValue and friends to CEATypes.xsd
 *
 * Revision 1.10.2.4  2008/05/17 16:45:01  pah
 * tidy tests to make sure more are passing
 *
 * Revision 1.10.2.3  2008/03/27 13:34:36  pah
 * now producing correct registry documents
 *
 * Revision 1.10.2.2  2008/03/26 17:15:39  pah
 * Unit tests pass
 *
 * Revision 1.10.2.1  2008/03/19 23:10:55  pah
 * First stage of refactoring done - code compiles again - not all unit tests passed
 *
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * Revision 1.10 2007/09/28 18:03:36
 * clq2 apps_gtr_2303
 * 
 * Revision 1.9.52.1 2007/08/29 14:31:03 gtr VOSI support.
 * 
 * Revision 1.9.44.1 2007/06/07 13:20:00 gtr I added the getApplicationIvorns()
 * method.
 * 
 * Revision 1.9 2006/03/17 17:50:58 clq2 gtr_1489_cea correted version
 * 
 * Revision 1.7 2006/03/07 21:45:27 clq2 gtr_1489_cea
 * 
 * Revision 1.4.38.1 2005/12/21 14:44:35 gtr Changed to make the registration
 * template available through the InitServlet.
 * 
 * Revision 1.4 2005/07/05 08:27:01 clq2 paul's 559b and 559c for wo/apps and
 * jes
 * 
 * Revision 1.3.120.1 2005/06/09 08:47:33 pah result of merging branch
 * cea_pah_559b into HEAD
 * 
 * Revision 1.3.106.2 2005/06/02 14:57:29 pah merge the ProvidesVODescription
 * interface into the MetadataService interface
 * 
 * Revision 1.3.106.1 2005/05/31 12:58:26 pah moved to v10 schema interpretation -
 * this means that the authorityID is read directly with the applicaion "name"
 * Revision 1.3 2004/08/28 07:17:34 pah commandline parameter passing - unit
 * tests ok
 * 
 * Revision 1.2 2004/07/01 11:16:22 nw merged in branch
 * nww-itn06-componentization
 * 
 * Revision 1.1.2.1 2004/06/14 08:56:58 nw factored applications into
 * sub-projects, got packaging of wars to work again
 * 
 */