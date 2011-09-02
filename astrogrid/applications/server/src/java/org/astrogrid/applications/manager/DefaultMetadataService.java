/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.manager;

import java.io.StringWriter;
import java.math.BigInteger;
import java.net.URL;
import java.util.List;

import javax.xml.transform.TransformerConfigurationException;

import junit.framework.Test;
import junit.framework.TestCase;
import net.ivoa.resource.AccessURL;
import net.ivoa.resource.Capability;
import net.ivoa.resource.Interface;
import net.ivoa.resource.Resource;
import net.ivoa.resource.Service;
import net.ivoa.resource.WebService;
import net.ivoa.resource.dataservice.HTTPQueryType;
import net.ivoa.resource.dataservice.ParamHTTP;
import net.ivoa.resource.registry.iface.VOResources;

import org.apache.axis.utils.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.ApplicationDescriptionLibrary;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.MetadataException;
import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.cea.CeaCapability;
import org.astrogrid.applications.description.cea.ManagedApplications;
import org.astrogrid.applications.description.cea.UWSPA;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.astrogrid.contracts.StandardIds;
import org.astrogrid.test.AstrogridAssert;
import org.w3c.dom.Document;

/**
 * Standard implementation of the {@link MetadataService}
 * {@link org.astrogrid.applications.manager.MetadataService}component.
 * 
 * @author Noel Winstanley nw@jb.man.ac.uk 21-May-2004
 * @author pharriso@eso.org 02-Jun-2005
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 18 Mar 2008
 * @TODO - might want some more thought wrt overriding in other cards.
 * @TODO - perhaps this is no longer worthy of separate "service" - now the CECs
 *       are actually configured directly in terms of a resource entries.
 */
public class DefaultMetadataService extends AbstractMetadataService implements MetadataService,
	ComponentDescriptor {
    private static final Log logger = LogFactory
	    .getLog(DefaultMetadataService.class);


    /** configuration settings */
    private Configuration configuration;

    /** library to generate description for */
    private final ApplicationDescriptionLibrary lib;

    private ServiceDefinitionFactory serviceFactory;

    /**
     * Construct a new DefaultMetadataService
     * 
     * @param lib
     *                The library of descriptions for which to build a registry
     *                entry.
     * @param urls
     *                URLs needed for configuration.
     *                @TODO should get rid of the exception if possible.
     */
    public DefaultMetadataService(ApplicationDescriptionLibrary lib,
	    Configuration configuration, ServiceDefinitionFactory serviceFac) throws TransformerConfigurationException{
	this.lib = lib;
	this.configuration = configuration;
	this.serviceFactory = serviceFac;
    }

    /**
     * Reveals the IVORNs for the supported applications.
     */
    public String[] getApplicationIvorns() {
	String[] names = this.lib.getApplicationNames();
	for (int i = 0; i < names.length; i++) {
	    if (!names[i].startsWith("ivo://")) {
		names[i] = "ivo://" + names[i];
	    }
	}
	return names;
    }

    /**
     * This should potentially be overriden by subclasses.
     * 
     * @see org.astrogrid.applications.component.ProvidesVODescription#getDescription()
     * @todo could cache the result.
     */
    protected Service getServer() throws Exception {

	Service service = serviceFactory.getCECDescription();
	String endpoint = this.configuration.getServiceEndpoint().toString();
	if(! endpoint.endsWith("/")) endpoint = endpoint+"/";
	CeaCapability ceaCap = new CeaCapability();
	ManagedApplications managedApplications = ceaCap
		.getManagedApplications();
	managedApplications.getApplicationReference().clear();

	String ids[] = lib.getApplicationNames();
	// add each of the application definitions.
	for (int i = 0; i < ids.length; i++) {
	    String applicationName = ids[i];

	    // Applications without names break the container, so don't use
	    // them.
	    if (applicationName == null)
		continue;

	    // Applications in the org.astrogrid.unregistered authority are
	    // for special tests and shouldn't go in the registry. Don't use
	    // them.
	    if (applicationName.startsWith("ivo://org.astrogrid.unregistered"))
		continue;

	    // add this application to the list of managed applications.
	    managedApplications.getApplicationReference().add(applicationName);

	}

	ceaCap.setStandardID(StandardIds.CEA_1_0);
	WebService intf = new WebService();
	intf.setVersion("1.0");
	AccessURL accessURL = new AccessURL();
	
        accessURL.setValue(endpoint+"services/CommonExecutionConnectorService");
	intf.getAccessURL().add(accessURL);
	ceaCap.getInterface().add(intf);
	Interface uws = new UWSPA();
	uws.setVersion("0.9");
	AccessURL uwsURL = new AccessURL();
	uwsURL.setValue(endpoint+"uws/jobs");
	uws.getAccessURL().add(uwsURL);
	ceaCap.getInterface().add(uws);
	
	// add the service description
	service.getCapability().add(ceaCap);
	
	//put in the VOSI capability itself - to allow vosi harvesting
	Capability vosiCap = new Capability();
	vosiCap.setStandardID("ivo://org.astrogrid/std/VOSI/v0.4#capabilities");
	ParamHTTP vosiIntf = new ParamHTTP();
	vosiIntf.setVersion("1.0");
	AccessURL vosiURL = new AccessURL();
	vosiURL.setValue(endpoint+"/VOSI/capabilities");
	vosiURL.setUse("full");
	vosiIntf.getQueryType().add(HTTPQueryType.GET);
	vosiIntf.setResultType("application/xml");
	vosiIntf.getAccessURL().add(vosiURL);
	vosiCap.getInterface().add(vosiIntf);
	service.getCapability().add(vosiCap);
	
	
	//put in the VOSI availability capability itself - to allow vosi harvesting
        Capability availCap = new Capability();
        availCap.setStandardID("ivo://org.astrogrid/std/VOSI/v0.4#availability");
        ParamHTTP availInf = new ParamHTTP();
        availInf.setVersion("1.0");
        AccessURL availURL = new AccessURL();
        availURL.setValue(endpoint+"VOSI/availability");
        availURL.setUse("full");
        availInf.getQueryType().add(HTTPQueryType.GET);
        availInf.setResultType("application/xml");
        availInf.getAccessURL().add(availURL);
        availCap.getInterface().add(availInf);
        service.getCapability().add(availCap);

        //put in the VOSI availability capability itself - to allow vosi harvesting
        Capability appCap = new Capability();
        appCap.setStandardID("ivo://org.astrogrid/std/VOSI/v0.4#applications");
        ParamHTTP appIntf = new ParamHTTP();
        appIntf.setVersion("1.0");
        AccessURL appURL = new AccessURL();
        appURL.setValue(endpoint+"uws/reg/app");
        appURL.setUse("full");
        appIntf.getQueryType().add(HTTPQueryType.GET);
        appIntf.setResultType("application/xml");
        appIntf.getAccessURL().add(appURL);
        appCap.getInterface().add(appIntf);
        service.getCapability().add(appCap);

        //put in the delegation capability itself 
        Capability deleCap = new Capability();
        deleCap.setStandardID("ivo://ivoa.net/std/Delegation");
        ParamHTTP deleIntf = new ParamHTTP();
        deleIntf.setVersion("1.0");
        AccessURL deleURL = new AccessURL();
        deleURL.setValue(endpoint+"delegation");
        deleURL.setUse("full");
        deleIntf.getQueryType().add(HTTPQueryType.GET);
        deleIntf.setResultType("application/xml");
        deleIntf.getAccessURL().add(deleURL);
        deleCap.getInterface().add(deleIntf);
        service.getCapability().add(deleCap);

        
        return service;

    }

 
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
	return "Standard CEA Server Description";
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
	StringBuffer sb = new StringBuffer(
		"Standard implementation of the service description component`n");
	try {
	    Document doc = this.getServerDescription();
	    StringWriter sw = new StringWriter();
	    XMLUtils.PrettyDocumentToWriter(doc, sw);
	    sb.append("VODescription \n"
		    + XMLUtils.xmlEncodeString(sw.toString()));
	} catch (Exception e) {
	    sb.append("Could not display description: " + e.getMessage());
	}

	return sb.toString();
    }

    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
	return new InstallationTest("testGetRegistryEntry");
    }

    /**
     * The installation test for the Metadata service.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public class InstallationTest extends TestCase {

	public InstallationTest(String arg0) {
	    super(arg0);
	}

	public void testGetRegistryEntry() throws Exception {
	    System.out.println(Namespaces.CEA);
	    System.out.println(Namespaces.CEAIMPL);
	    Document server = getServerDescription();
	    assertNotNull(server);
	    AstrogridAssert.assertSchemaValid(server, "Resource",
		    SchemaMap.ALL);
	}

    }

    public Document getApplicationDescription(String id)
	    throws ApplicationDescriptionNotFoundException {
/*
 * get the application description - this can be either just a CEAApplication or a DataService with an application definition. - In the case
 * that it is a DataService, then the "application definition" may be in-line, or may in fact be a separate application.
 */
	
	MetadataAdapter adapter = lib.getDescription(id).getMetadataAdapter();

	try {
	    
	    if(adapter.isService()){
		//FIXME - deal with CEA service
		return marshall(adapter.getResource());
	    }
	    else {
		return marshall(adapter.getResource());
	    }
	    
	} catch (Exception e) {
	    throw new ApplicationDescriptionNotFoundException(
		    "error creating the application description for " + id, e);
	}

    }

    public Document getServerDescription() throws MetadataException {
	
	try {
	    return marshall(getServer());
	} catch (Exception e) {
	   throw new MetadataException("problem getting service description", e);
	}
    }

    public Document returnRegistryEntry() throws MetadataException {
	VOResources res = new VOResources();
	List<Resource> reslist = res.getResource();
	try {
	    reslist.add(getServer());
	} catch (Exception e) {
	  throw new MetadataException("error getting server description",e);
	}
	
	String[] applicationIvorns = getApplicationIvorns();
	int j = 0;
	for (int i = 0; i < applicationIvorns.length; i++) {
	    if(!applicationIvorns[i].equals("ivo://org.astrogrid.unregistered/default")){
	    reslist.add(lib.getDescription(applicationIvorns[i]).getMetadataAdapter().getResource());
	    j++;
	    }
	}
	res.setFrom(BigInteger.valueOf(1));
	res.setNumberReturned(BigInteger.valueOf(j+1));
	try {
	    return marshall(res);
	} catch (Exception e) {
	    throw new MetadataException("error marshalling descriptions",e);
	}
    }

    public Document getServerCapabilities() throws MetadataException {
        try{
       return marshall(getServer().getCapability());
       
    } catch (Exception e) {
        throw new MetadataException("error marshalling capabilities",e);
    }

    }

    private Document marshall(List<Capability> capability) throws MetadataException {
        return CEAJAXBUtils.marshall(capability);
    }

}
