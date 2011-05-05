/*$Id: DatacenterParameterAdapter.java,v 1.2 2011/05/05 14:49:37 gtr Exp $
 * Created on 04-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.dataservice.service.cea;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;


/** The default implementation of {@link org.astrogrid.applications.parameter.ParameterAdapter}
 * <p/>
 * Handles both direct and indirect parameters, returing them as an in-memory string.
 * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(ParameterValue)
 * @author Noel Winstanley (nw@jb.man.ac.uk)
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @todo should really do different things according to what the type of the parameter is.
 * @todo check whether it is really appropriate use Readers and Writers to access the indirect parameters
 */
public class DatacenterParameterAdapter extends DefaultParameterAdapter {
    /**
     * Constructor.
    * @param val
    * @param description
    * @param externalVal
    */
   public DatacenterParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue externalVal) {
      super(val, description, externalVal);
   }

   /**
     * Commons Logger for this class
     */
    private static final Log logger = 
       LogFactory.getLog(DatacenterParameterAdapter.class);

     /**
     * retreives the value for this paramter
     * if the parameter is direct, just return the value of the parameter 
     * value itself, if indirect, decides what to do depending on what the
     * parameter is.
     * @return always returns the string value of this parameter
     *  
     */
    @Override
    public Object process() throws CeaException {
        String name = val.getName();
        if (DatacenterApplicationDescription.INPUT_VOTABLE.equals(name)) {
           // Is it a direct parameter?
           if (externalVal == null) {
              // We have literal VOTable, just return it
              return val.getValue();
           } 
           else {
              // This is a special case - the default parameter adapter
              // fetches the *content* of the resource at the indirect URI, 
              // but we want the actual URI,
              return val.getValue();
           }
        }
        else {
           // Otherwise default behaviour is fine
           return super.process();
        }
    }

    public boolean isIndirect() {
       if (externalVal == null) {
          return false;
       }
       return true;
    }
}
