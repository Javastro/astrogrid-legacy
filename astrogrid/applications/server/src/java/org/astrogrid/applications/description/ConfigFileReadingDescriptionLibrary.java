/*
 * $Id: ConfigFileReadingDescriptionLibrary.java,v 1.5 2008/09/18 09:02:32 pah Exp $
 * 
 * Created on 18 Mar 2008 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2008 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.description;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBElement;
import javax.xml.bind.Unmarshaller;
import javax.xml.bind.ValidationEvent;
import javax.xml.bind.util.ValidationEventCollector;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;

import junit.framework.Test;
import junit.framework.TestCase;
import net.ivoa.resource.Resource;
import net.ivoa.resource.cea.CeaApplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.impl.CEADALService;
import org.astrogrid.applications.description.impl.CECConfig;
import org.astrogrid.applications.description.impl.CeaCmdLineApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaDBApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.http.HttpApplicationDescription;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;

/**
 * {@link ApplicationDescription} library that can load and contain
 * {@link CeaCmdLineApplicationDefinition}s. Acts as a factory for applications
 * read from a file.
 * 
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 25 Mar 2008
 * @version $Name:  $
 * @since VOTech Stage 7
 */
public class ConfigFileReadingDescriptionLibrary extends
        BaseApplicationDescriptionLibrary {
    /*
     * (non-Javadoc)
     * 
     * @see
     * org.astrogrid.applications.description.BaseApplicationDescriptionLibrary
     * #getName()
     */
    @Override
    public String getName() {
        return "Configuration File Reading Library";
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * org.astrogrid.applications.description.BaseApplicationDescriptionLibrary
     * #getInstallationTest()
     */
    @Override
    public Test getInstallationTest() {
        return new InstallationTest("testLoadApplications");
    }

    /**
     * The installation test case for
     * 
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 16 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public class InstallationTest extends TestCase {
        public InstallationTest(String name) {
            super(name);
        }

        public void testLoadApplications() {
            String error = getErrorMessage();
            if (error == null) {
                error = "ok";
            }
            assertTrue(error, loadok);
        }

    }

    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(ConfigFileReadingDescriptionLibrary.class);
    private ValidationEventCollector handler;
    private boolean loadok = true;

    public ConfigFileReadingDescriptionLibrary(Configuration conf) {
        this(conf, conf.getApplicationDescriptionUrl());
    }

    public ConfigFileReadingDescriptionLibrary(Configuration conf,
            URL applicationDescriptionUrl) {
        super(conf);
        loadok = loadApplications(applicationDescriptionUrl);

    }

    /**
     * load the applications from a particular file. The file must contain
     * 
     * @param applicationDescriptionUrl
     *            =
     * @return TODO
     */
    protected boolean loadApplications(URL applicationDescriptionUrl) {
        loadok = true;
        boolean retval = true;

        if (applicationDescriptionUrl.getProtocol().equals("file")) {
            try {
                File f = new File(applicationDescriptionUrl.toURI());
                if (f.isDirectory()) {
                    for (File dir_memner : f.listFiles()) {
                        try {
                            retval = loadAnApplication(dir_memner.toURL());
                        } catch (MalformedURLException e) {
                            logger.error("problem with configfile URL in dir",
                                    e);
                            retval = false;
                        }
                        if (!retval) { // early exit
                            break;
                        }

                    }
                } else {
                    retval = loadAnApplication(applicationDescriptionUrl);
                }

            } catch (URISyntaxException e) {
                logger.error("problem with application description URI", e);
                retval = false;
            }
        } else {
            retval = loadAnApplication(applicationDescriptionUrl);
        }
        loadok = retval;
        return retval;
    }

    private boolean loadAnApplication(URL applicationDescriptionUrl) {
        handler = new ValidationEventCollector();
        boolean retval = true;

        try {
            JAXBContext jc = CEAJAXBContextFactory.newInstance();
            Unmarshaller um = jc.createUnmarshaller();
            javax.xml.validation.SchemaFactory sf = SchemaFactory
                    .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);

            URL url = SchemaMap.getSchemaURL(Namespaces.CEAIMPL.getNamespace());
            Source schemas = new StreamSource(url.openStream(), url
                    .toExternalForm());
            Schema schema = sf.newSchema(schemas);
            um.setSchema(schema);
            um.setEventHandler(handler);
            // Unmarshall the file into a content object
            Object raw = um.unmarshal(applicationDescriptionUrl);

            if (raw instanceof JAXBElement) {
                // IMPL perhaps there is a way of making sure that JAXB2 can
                // directly instantiate the classes, but this is here as a
                // safetynet
                // if a raw element has been returned
                raw = ((JAXBElement) raw).getValue();
            }

            if (raw instanceof CECConfig) {
                CECConfig c = (CECConfig) raw;

                // commonality here - the metadata definition is actually of a
                // similar kind
                // each can have the same types of CEA applications..
                List<Object> appDefs = c
                        .getCeaApplicationOrCeaDALServiceOrDBDefinition();
                for (Iterator iterator = appDefs.iterator(); iterator.hasNext();) {
                    Object object = (Object) iterator.next();
                    addSingleApplication(object);

                }
            } else if (raw instanceof Resource) {
                addSingleApplication(raw);
            } else {
                logger.fatal("unknown application definition class="
                        + raw.getClass().getCanonicalName());
                retval = false;
            }

        } catch (Exception e) {
            retval = false;
            logger.fatal("error reading application definitions from "
                    + applicationDescriptionUrl, e);
            if (handler.hasEvents()) {
                for (int i = 0; i < handler.getEvents().length; i++) {
                    ValidationEvent event = handler.getEvents()[i];
                    logger.fatal(event.toString());
                }

            }
        }
       return retval;
    }

    private void addSingleApplication(Object object) {
        if (object instanceof CeaApplication) {
            final CeaApplication ceaApp = (CeaApplication) object;
            addApp(ceaApp.getApplicationDefinition(), new AppMetadataAdapter(
                    ceaApp));
        } else if (object instanceof CEADALService) {
            final CEADALService dalService = (CEADALService) object;
            ApplicationBase apptyp = dalService.getApplicationDefinition();
            addApp(apptyp, new ServiceMetadataAdapter(dalService));

        }
    }

    private void addApp(ApplicationBase apptyp, MetadataAdapter ma) {

        if (apptyp instanceof CeaCmdLineApplicationDefinition) {
            this
                    .addApplicationDescription(new CommandLineApplicationDescription(
                            conf, ma));

        } else if (apptyp instanceof CeaDBApplicationDefinition) {
            CeaDBApplicationDefinition cmdDef = (CeaDBApplicationDefinition) apptyp;
            // FIXME need to be the factory for the DB application.
        } else if (apptyp instanceof CeaHttpApplicationDefinition) {
            this.addApplicationDescription(new HttpApplicationDescription(conf,
                    ma));
        }
    }

    /**
     * return if there was an error in the last reading of a config file.
     * 
     * @return true if the config file was loaded ok
     */
    public boolean isLoadok() {
        return loadok;
    }

    /**
     * Return the error message associated with the last reading of a config
     * file.
     * @TODO capture the error message from any exceptions also.
     * @return
     */
    public String getErrorMessage() {
        if (!loadok) {
            if (handler.hasEvents()) {
                StringBuffer error = new StringBuffer();
                for (ValidationEvent event : handler.getEvents()) {
                    error.append(event.toString());
                    error.append("\n");
                }
                return error.toString();
            } else {
                return "unknown error - see log files for more detail";
            }
        } else {
            return null;
        }
    }
}

/*
 * $Log: ConfigFileReadingDescriptionLibrary.java,v $
 * Revision 1.5  2008/09/18 09:02:32  pah
 * add capability to read files from a directory
 * Revision 1.4 2008/09/13
 * 09:49:18 pah make installation test report error
 * 
 * Revision 1.3 2008/09/10 23:27:17 pah moved all of http CEC and most of
 * javaclass CEC code here into common library
 * 
 * Revision 1.2 2008/09/03 14:18:43 pah result of merge of pah_cea_1611 branch
 * 
 * Revision 1.1.2.2 2008/08/29 07:28:26 pah moved most of the commandline CEC
 * into the main server - also new schema for CEAImplementation in preparation
 * for DAL compatible service registration
 * 
 * Revision 1.1.2.1 2008/08/02 13:33:56 pah safety checkin - on vacation
 * 
 * Revision 1.1.2.5 2008/06/11 14:32:48 pah merged the ids into the application
 * execution environment
 * 
 * Revision 1.1.2.4 2008/05/13 16:02:47 pah uws with full app running UI is
 * working
 * 
 * Revision 1.1.2.3 2008/04/04 15:34:52 pah Have got bulk of code working with
 * spring - still need to remove all picocontainer refs ASSIGNED - bug 1611:
 * enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 * 
 * Revision 1.1.2.2 2008/03/26 17:29:51 pah Unit tests pass
 * 
 * Revision 1.1.2.1 2008/03/19 23:15:43 pah First stage of refactoring done -
 * code compiles again - not all unit tests passed
 * 
 * ASSIGNED - bug 1611: enhancements for stdization holding bug
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=1611
 */
