/*
 * $Id: AbstractApplicationDescription.java,v 1.7 2008/09/04 19:10:52 pah Exp $
 * 
 * Created on 14-Nov-2003 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2003 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.description.base;

import net.ivoa.resource.Resource;
import net.ivoa.resource.cea.CeaApplication;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.astrogrid.applications.component.InternalCEAComponents;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.description.ApplicationDescription;
import org.astrogrid.applications.description.ApplicationInterface;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.exception.InterfaceDescriptionNotFoundException;
import org.astrogrid.applications.description.exception.ParameterDescriptionNotFoundException;
import org.astrogrid.applications.description.execution.Tool;
import org.astrogrid.security.SecurityGuard;

/**
 * Abstract base implementation of {@link org.astrogrid.applications.description.ApplicationDescription}.
 * <p />
 * Implements all methods in the <tt>ApplicationDescription</tt> interface apart from {@link org.astrogrid.applications.description.ApplicationDescription#initializeApplication(String, SecurityGuard, Tool)},
 * which is left abstract
 * <p />
 * Implementation is an adapter for {@link CeaApplication} with extra methods for adding/retrieving interfaces and parameters. In addition to the methods of the <tt>ApplicationDescription</tt> interface, this class
 * provides methods for adding items to these maps ({@link #addInterface(ApplicationInterface)}, {@link #addParameterDescription(ParameterDescription)}), and
 * an implementation of {@link #toString()} that produces a formatted dump of the application description. 
 * @author Noel WInstanley
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4
 */
public abstract class AbstractApplicationDescription implements ApplicationDescription{
    /**
     * Logger for this class
     */
    private static final Log logger = LogFactory
	    .getLog(AbstractApplicationDescription.class);

   /** underlying description of application - this is in terms of the standard registry description*/
   protected final ApplicationBase appBase;

   /* the registry description of the application */
   protected Resource resource;

   protected final MetadataAdapter metadataAdapter ;

private  InternalCEAComponents internalComponentFactory;

 
   
   /** 
    *  Construct a new AbstractApplicationDescription from an externally defined
    * @param env environment object (useful shared objects for application descriptions)
    */
   public AbstractApplicationDescription(MetadataAdapter ma)
   {
      this.metadataAdapter = ma;
      this.appBase = ma.getApplicationBase();
      this.resource = ma.getResource();

      assert appBase.isValid() : "ApplicationDescriptions should be passed  CeaApplications with valid BaseApplicationDescriptions";
      //make sure that the back reference to the application definition is set 
      for (InterfaceDefinition intf : appBase.interfaces.interfaceDefinition) {
	      //make sure that the back reference to the application definition is set 
	intf.setApplicationDescription(this); //IMPL would be better to do this at creation time...
	
   }
   }
   
       
   
 
 



protected InternalCEAComponents getInternalComponentFactory() {
    //lazily initialize the internal component factory as construction time is too soon in many cases.
    if(internalComponentFactory == null){
	      internalComponentFactory = InternalCeaComponentFactory.getInstance();	
    }
    return internalComponentFactory;
}

public String getId() {
      return resource.getIdentifier();
   }

   
   public String getDescription() {
       return resource.getContent().getDescription();
 }


    public String getName() {
     return resource.getShortName();//IMPL is it better to do getTitle?
}


public String getReferenceURL() {
    return resource.getContent().getReferenceURL();
}


   

   public ParameterDescription[] getParameterDescriptions()
   {

       return appBase.getParameters().parameterDefinition.toArray(new ParameterDescription[0]);       
   }

   public ParameterDescription getParameterDescription(String parameterName) throws ParameterDescriptionNotFoundException
   {
       return appBase.getParameters().getParameterDefinitionbyId(parameterName);
   }
   
   

   public ApplicationInterface getInterface(String interfaceName) throws InterfaceDescriptionNotFoundException
   {
       return appBase.getInterfaces().getInterfaceById(interfaceName);
    }
   
   public ApplicationInterface[] getInterfaces() {
       return appBase.getInterfaces().getInterfaceDefinition().toArray(new ApplicationInterface[0]);
   }



    /** produce a nicely-formatted dump of the metadata for this application 
     * @see java.lang.Object#toString()
     */
    public String toString() {
        StringBuffer paramBuff = new StringBuffer();
        StringBuffer interfaceBuff = new StringBuffer();
        ParameterDescription[] paramDescs = getParameterDescriptions();
        for (int i = 0 ; i < paramDescs.length; i++) {
            paramBuff.append("\n");
            paramBuff.append(paramDescs[i].toString());
        }
        ApplicationInterface[] ifaces = getInterfaces();
        for (int i = 0; i < ifaces.length; i++) {
            interfaceBuff.append("\n");
            interfaceBuff.append(ifaces[i].toString());
        }
        return "Application Description: " + this.getId()
        + "\n" + paramBuff.toString()
        + "\n" +  interfaceBuff.toString()
        + "\n-------------------------------------------------------------";
    }



    public MetadataAdapter getMetadataAdapter() {
	assert metadataAdapter != null : "metadataAdapter should have been set for the application at creation time";
	return metadataAdapter;
    }


    


 
}


