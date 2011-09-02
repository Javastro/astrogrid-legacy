/*
 * $Id: AbstractParameterAdapter.java,v 1.4 2011/09/02 21:55:52 pah Exp $
 * 
 * Created on 27-Oct-2004 by Paul Harrison (pah@jb.man.ac.uk)
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */ 

package org.astrogrid.applications.parameter;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.component.InternalCeaComponentFactory;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.environment.ApplicationEnvironment;
import org.astrogrid.applications.parameter.protocol.ProtocolLibrary;

/**
 * Base class for {@link org.astrogrid.applications.parameter.ParameterAdapter}. It contains the necessary fileds that the adapter wraps. 
 * @author Paul Harrison (pah@jb.man.ac.uk) 27-Oct-2004
 * @version $Name:  $
 * @since iteration6
 */
public abstract class AbstractParameterAdapter implements ParameterAdapter {

/** Construct a new AbstractParameterAdapter
    * @param val the parameter value to adapt.
    * @param description the description associated with this value.
    * @param env wrapper around the external location that contains the true value of the parameter (in case of direct parameters, is null) 
    */
   public AbstractParameterAdapter(ParameterValue val,ParameterDescription description,ParameterDirection direction, ApplicationEnvironment env) {
       this.val = val;
       this.description = description;
       this.env = env;
       this.direction = direction;
   }

   /** the parameter value */
   protected final ParameterValue val;
   /** the parameter descritpion */
   protected final ParameterDescription description;
   /** indirection to the external location containing the true value of an indirect parameter */
   protected final ApplicationEnvironment env;
   /** the parameter direction - whether input or output */
protected ParameterDirection direction;
  
   protected MutableInternalValue internalVal = null;
   
   public ParameterValue getWrappedParameter() {
        return val;
    }

   protected ProtocolLibrary getProtocolLib() {
       return InternalCeaComponentFactory.getInstance().getProtocolLibrary();
   }

public abstract MutableInternalValue getInternalValue() throws CeaException;

public abstract void writeBack() throws CeaException;


}
