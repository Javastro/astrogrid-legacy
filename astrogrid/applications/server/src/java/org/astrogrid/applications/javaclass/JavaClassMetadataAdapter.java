/*
 * $Id: JavaClassMetadataAdapter.java,v 1.1 2009/04/04 20:41:54 pah Exp $
 * 
 * Created on 2 Apr 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.javaclass;

import java.lang.reflect.Method;
import java.util.Map;

import org.astrogrid.applications.description.AbstractMetadataAdapter;
import org.astrogrid.applications.description.MetadataAdapter;
import org.astrogrid.applications.description.ServiceDefinitionFactory;
import org.astrogrid.applications.description.ServiceDescriptionException;
import org.astrogrid.applications.description.base.ApplicationBase;
import org.astrogrid.applications.description.cea.CeaApplication;

public class JavaClassMetadataAdapter extends AbstractMetadataAdapter implements
        MetadataAdapter {
    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(JavaClassMetadataAdapter.class);
    

   private Class<?>  impl;


private JavaClassApplicationMetadataFactory fac;

   JavaClassMetadataAdapter(String implClass, ServiceDefinitionFactory sf) throws ClassNotFoundException{
       super(new CeaApplication());
       this.impl = Class.forName(implClass);
       populate((CeaApplication)this.resource, sf);
    }

    private void populate(CeaApplication app, ServiceDefinitionFactory sf) {
        
        fac = new JavaClassApplicationMetadataFactory(app, impl, sf);
        try {
            fac.createMetadata();
        } catch (ServiceDescriptionException e) {
            logger.fatal("cannot populate metadata", e);
        }
    
}

    public ApplicationBase getApplicationBase() {
      return ((CeaApplication)resource).getApplicationDefinition();
    }

    public boolean isService() {
        return false;
    }

    public boolean needApplication() {
        return false;
    }
    
    public Map<String, Method> getMethodMap(){
        return fac.getMethodMap();
    }

}


/*
 * $Log: JavaClassMetadataAdapter.java,v $
 * Revision 1.1  2009/04/04 20:41:54  pah
 * ASSIGNED - bug 2113: better configuration for java CEC
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2113
 * Introduced annotations
 *
 */
