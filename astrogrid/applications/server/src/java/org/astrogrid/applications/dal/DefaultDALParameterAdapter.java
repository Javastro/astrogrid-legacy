/*
 * $Id: DefaultDALParameterAdapter.java,v 1.2 2011/09/02 21:55:50 pah Exp $
 * 
 * Created on 10 Jul 2009 by Paul Harrison (paul.harrison@manchester.ac.uk)
 * Copyright 2009 Astrogrid. All rights reserved.
 *
 * This software is published under the terms of the Astrogrid 
 * Software License, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.dal;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.description.impl.DBParameterDefinition;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.DefaultInternalValue;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.FileBasedInternalValue;
import org.astrogrid.applications.parameter.MutableInternalValue;
import org.astrogrid.applications.parameter.ParameterDirection;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

/**
 * Parameter Adapter for DAL. This parameter adapter attempts to use the DAL PQL rules.
 * @author Paul Harrison (paul.harrison@manchester.ac.uk) 10 Jul 2009
 * @version $Name:  $
 * @since AIDA Stage 1
 */
public class DefaultDALParameterAdapter extends DefaultParameterAdapter
        implements DALParameterAdapter {

    /** logger for this class */
    private static final org.apache.commons.logging.Log logger = org.apache.commons.logging.LogFactory
            .getLog(DefaultDALParameterAdapter.class);
    
    /** convenience local for the actual type of the parameter description.
     */
    private DBParameterDefinition dbpardef;

    private FileBasedInternalValue internalValasFile;
    public DefaultDALParameterAdapter(ParameterValue val,
            ParameterDescription description, ParameterDirection dir, ApplicationEnvironment env) {
        super(val, description, dir, env);
        dbpardef = (DBParameterDefinition) description; // IMPL - this cast might not always be possible - could have an application with a mixture of parameter types.
    }

    @Override
    public MutableInternalValue getInternalValue() throws CeaException {
        
        if (internalVal == null) {

            if (!val.isIndirect()) {
                internalVal = new DefaultInternalValue(val.getValue());
               
            } else {
                try {

                    internalValasFile = new FileBasedInternalValue(
                            env.getTempFile(val.getId()));
                    ExternalValue externalVal = getProtocolLib()
                            .getExternalValue(val, env.getSecGuard());
                    internalValasFile.setValue(externalVal.read());
                    internalVal = internalValasFile;
                } catch (Exception e) {
                    throw new CeaException("Could not process parameter "
                            + val.getId(), e);
                }
            }
        }
        return internalVal;
       
    }

    public String whereClausePart() {
        //TODO need to implement the proper DAL PQL
        return dbpardef.getColumn() + "=" + val.getValue();
        
    }

    public String selectClausePart() {
       return dbpardef.getColumn();
    }

}


/*
 * $Log: DefaultDALParameterAdapter.java,v $
 * Revision 1.2  2011/09/02 21:55:50  pah
 * result of merging the 2931 branch
 *
 * Revision 1.1.2.1  2009/07/16 19:53:02  pah
 * NEW - bug 2944: add DAL support
 * http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2944
 *
 */
