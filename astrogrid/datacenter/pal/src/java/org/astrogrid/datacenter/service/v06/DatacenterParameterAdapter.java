/*$Id: DatacenterParameterAdapter.java,v 1.6 2004/11/03 00:17:56 mch Exp $
 * Created on 13-Jul-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid
 * Software License version 1.2, a copy of which has been included
 * with this distribution in the LICENSE.txt file.
 *
**/
package org.astrogrid.datacenter.service.v06;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;

/** parameter adapter that handles redirecting output from dataServer into cea system
 * @author Noel Winstanley nw@jb.man.ac.uk 13-Jul-2004
 *
 */
public class DatacenterParameterAdapter extends DefaultParameterAdapter implements ParameterAdapter {
   /**
    * Logger for this class
    */
   private static final Log logger = LogFactory
         .getLog(DatacenterParameterAdapter.class);

    /** Construct a new DatacenterParameterAdapter
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public DatacenterParameterAdapter(ParameterValue arg0, ParameterDescription arg1, ExternalValue arg2) {
        super(arg0, arg1, arg2);
    }

    public ExternalValue getExternalValue() { return externalVal; }
    
    public void setInternalValue(String value) {
       val.setValue(value);
    }
    
    /** @param arg0 - expects a {@link CEATargetIndicator} from which it will slurp the results.
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object arg0) throws CeaException {
       throw new UnsupportedOperationException("Shouldn't use this in DSAs, see CEATargetMaker");
       /*
       CEATargetIndicator ti = (CEATargetIndicator) arg0;
        logger.debug("writing back "+ti+" to "+externalVal);
        try {
            if (this.externalVal == null) {
                StringWriter sw = new StringWriter();
                Reader r = new InputStreamReader(ti.getStream());
                Piper.pipe(r,sw);
                val.setValue(sw.toString());
            } else {
                InputStream is = ti.getStream();
                OutputStream os = externalVal.write();
                Piper.pipe(is,os);
                os.close();
                
            }
        } catch (IOException e) {
            throw new ParameterWriteBackException("Could not write back parameter",e);
        }
        */
    }

}


/*
$Log: DatacenterParameterAdapter.java,v $
Revision 1.6  2004/11/03 00:17:56  mch
PAL_MCH Candidate 2 merge

Revision 1.1.12.2  2004/11/02 19:48:43  mch
Split TargetIndicator to indicator and maker

Revision 1.1.12.1  2004/10/20 18:12:45  mch
CEA fixes, resource tests and fixes, minor navigation changes

Revision 1.2.2.1  2004/10/20 12:43:28  mch
Fixes to CEA interface to write directly to target

Revision 1.2  2004/10/20 08:13:09  pah
added some logging

Revision 1.1  2004/09/28 15:02:13  mch
Merged PAL and server packages

Revision 1.4  2004/07/27 13:48:33  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package

Revision 1.3  2004/07/22 16:31:22  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/
