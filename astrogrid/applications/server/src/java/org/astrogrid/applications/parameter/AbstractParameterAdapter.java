/*
 * $Id: AbstractParameterAdapter.java,v 1.3 2008/09/03 14:18:57 pah Exp $
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
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.description.execution.ParameterValue;
import org.astrogrid.applications.parameter.protocol.ExternalValue;

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
    * @param externalVal wrapper around the external location that contains the true value of the parameter (in case of direct parameters, is null) 
    */
   public AbstractParameterAdapter(ParameterValue val,ParameterDescription description,ExternalValue externalVal) {
       this.val = val;
       this.description = description;
       this.externalVal = externalVal;
   }

   /* (non-Javadoc)
    * @see org.astrogrid.applications.parameter.ParameterAdapter#process()
    */
   public abstract Object process() throws CeaException;
   /* (non-Javadoc)
    * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
    */
   public abstract void writeBack(Object o) throws CeaException ;
   /** the parameter value */
   protected final ParameterValue val;
   /** the parameter descritpion */
   protected final ParameterDescription description;
   /** indirection to the external location containing the true value of an indirect parameter */
   protected final ExternalValue externalVal;
   
   public ParameterValue getWrappedParameter() {
        return val;
    }

 

}
