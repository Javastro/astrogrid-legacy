/*
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */
package org.astrogrid.applications.description;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.commandline.CommandLineApplicationDescription;
import org.astrogrid.applications.contracts.Configuration;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.cea.CeaApplication;
import org.astrogrid.applications.description.exception.ApplicationDescriptionNotFoundException;
import org.astrogrid.applications.description.impl.CEADALService;
import org.astrogrid.applications.description.impl.CECConfig;
import org.astrogrid.applications.description.impl.CeaCmdLineApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaDBApplicationDefinition;
import org.astrogrid.applications.description.impl.CeaHttpApplicationDefinition;
import org.astrogrid.applications.description.jaxb.CEAJAXBContextFactory;
import org.astrogrid.applications.http.HttpApplicationDescription;
import org.astrogrid.component.descriptor.ComponentDescriptor;
import org.astrogrid.contracts.Namespaces;
import org.astrogrid.contracts.SchemaMap;
import org.xml.sax.InputSource;

/** Basic implementation of an {@link org.astrogrid.applications.description.ApplicationDescriptionLibrary}
 * <p />
 * Unsurprisingly, based on a map. Provides methods to add descriptions to the library. 
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Jun-2004
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Sep 2008
 *
 */
public class BaseApplicationDescriptionLibrary implements ApplicationDescriptionLibrary, ComponentDescriptor {
    /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory.getLog(BaseApplicationDescriptionLibrary.class);

    protected Configuration conf;
    
  /**
   * Constructs a new BaseApplicationDescriptionLibrary
   * @param env2
 * @param conf 
   */
  public BaseApplicationDescriptionLibrary(Configuration conf) {
    this.descMap =  new HashMap<String, ApplicationDescription >();
    this.conf = conf;
   }

   /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getDescription(java.lang.String)
         */
    public ApplicationDescription getDescription(String name) throws ApplicationDescriptionNotFoundException {
        ApplicationDescription ad = (ApplicationDescription) descMap.get(name);
        if (ad == null) {
            throw new ApplicationDescriptionNotFoundException(name);
        }
        return ad;
    }
    /**
         * @see org.astrogrid.applications.description.ApplicationDescriptionLibrary#getApplicationNames()
         */
    public String[] getApplicationNames() {
    return (String[])descMap.keySet().toArray(new String[0]);
      }
    private final Map<String, ApplicationDescription> descMap;
    
    /** map keyed on the short name with all spaces removed - this is because the short name is used in some URIs in the UWS interfaces
     */
    private final Map<String, ApplicationDescription> shortMap = new HashMap<String, ApplicationDescription >();

    protected boolean loadok = true;

    protected ValidationEventCollector handler;;
    
    /** add an application description to the library
     * <p> if an application with the same name already exists, it will be overridden. 
     * @param desc the application description, which will be stored under key <tt>desc.getName()</tt>*/
    public void addApplicationDescription(ApplicationDescription desc) {
       
        String shortname = desc.getName().replaceAll("\\s", "");
        logger.info("Adding description for " + desc.getId() + " with shortname="+shortname+" type="+desc.getClass().getCanonicalName());
        descMap.put(desc.getId(),desc);
        shortMap.put(shortname, desc);
        
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getName()
     */
    public String getName() {
        return "Basic Application Description Library";
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getDescription()
     */
    public String getDescription() {
        return this.toString();
    }

   @Override
public String toString() {     
        StringBuffer appList = new StringBuffer();
        for (Iterator i = descMap.values().iterator(); i.hasNext(); ) {
            ApplicationDescription desc = (ApplicationDescription)i.next();
            appList.append("\n");
            appList.append(desc.toString());
            appList.append("\n");
        }
        return "Applications in Library:'"  + appList.toString();
    }
    /**
     * @see org.astrogrid.component.descriptor.ComponentDescriptor#getInstallationTest()
     */
    public Test getInstallationTest() {
          
          return new InstallationTest("testApplicationsDefined");
    }
    
    /**
     * Installation test for the Description library. Tests that at least one application is defined.
     * @author Paul Harrison (paul.harrison@manchester.ac.uk) 17 Sep 2008
     * @version $Name:  $
     * @since VOTech Stage 7
     */
    public class InstallationTest extends TestCase {
       
       /**
       * @param arg0
       */
      public InstallationTest(String arg0) {
         super(arg0);
         
      }
      public InstallationTest(){super();}
      
      public void testApplicationsDefined()
      {
         if(descMap.isEmpty())
         {
            fail("there are no applications defined in library "+ BaseApplicationDescriptionLibrary.this.getName() );
         }
      }
    }
    
   public ApplicationDescription getDescriptionByShortName(String name) throws ApplicationDescriptionNotFoundException{
       ApplicationDescription ad = (ApplicationDescription) shortMap.get(name);
       if (ad == null) {
	   throw new ApplicationDescriptionNotFoundException(name);
       }
       return ad;
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
    try {
        InputSource src = new InputSource(applicationDescriptionUrl.openStream());
        return loadApplications(src);
    } catch (IOException e) {
       logger.fatal("failed to load application from "+applicationDescriptionUrl.toString(), e);
       return false;
    }
}

protected boolean loadApplications(InputStream is) {
        InputSource src = new InputSource(is);
        return loadApplications(src);
}

protected boolean loadApplications(InputSource appdescSource) {
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
        Object raw = um.unmarshal(appdescSource);

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
                + appdescSource, e);
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
