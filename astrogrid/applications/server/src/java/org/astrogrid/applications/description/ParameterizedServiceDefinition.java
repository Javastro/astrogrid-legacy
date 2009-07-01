/*
 * $Id: ParameterizedServiceDefinition.java,v 1.1 2009/07/01 14:28:42 pah Exp $
 * 
 * Created on 26 Jun 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.io.IOException;

import javax.xml.bind.JAXBException;

import org.astrogrid.applications.description.jaxb.CEAJAXBUtils;
import org.xml.sax.SAXException;

import net.ivoa.resource.Contact;
import net.ivoa.resource.Creator;
import net.ivoa.resource.Curation;
import net.ivoa.resource.Resource;
import net.ivoa.resource.ResourceName;
import net.ivoa.resource.Service;

public class ParameterizedServiceDefinition extends AbstractServiceDefinition
        implements ServiceDefinitionFactory {

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(ParameterizedServiceDefinition.class);
    
    private String title;
    private String shortName;
    private String id;
    private String publisher;
    private String creator;
    private String contact;
    private String contactEmail;
    private String subject;
    private String description;
    private String url;

    private Service desc;

    public ParameterizedServiceDefinition() {
        try {
            desc = CEAJAXBUtils.unmarshall(this.getClass().getResourceAsStream(
                    "/CEARegistryTemplate.xml"), Service.class);
        } catch (Exception e) {
           logger.fatal("cannot read service description template", e);
        } 
    }

    public Service getCECDescription() throws ServiceDescriptionException {

        desc.setTitle(title);
        desc.setShortName(shortName);
        desc.setIdentifier(id);
        Curation curation = desc.getCuration();
        ResourceName pub = new ResourceName();
        pub.setValue(publisher);
        curation.setPublisher(pub);
        Creator crea = new Creator();
        pub = new ResourceName();
        pub.setValue(creator);
        crea.setName(pub);
        curation.getCreator().add(crea);
        Contact ctc = new Contact();
        pub = new ResourceName();
        pub.setValue(contact);
        ctc.setName(pub);
        ctc.setEmail(contactEmail);
        curation.getContact().add(ctc);
        return desc;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
        
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return "Parameterized service description";
    }

}

/*
 * $Log: ParameterizedServiceDefinition.java,v $
 * Revision 1.1  2009/07/01 14:28:42  pah
 * registration template directly argument of builder object - removed from config
 *
 */
