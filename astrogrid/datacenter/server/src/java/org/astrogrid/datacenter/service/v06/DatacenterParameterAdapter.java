/*$Id: DatacenterParameterAdapter.java,v 1.3 2004/07/22 16:31:22 nw Exp $
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

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.DefaultParameterAdapter;
import org.astrogrid.applications.parameter.ParameterAdapter;
import org.astrogrid.applications.parameter.ParameterWriteBackException;
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;
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
    /** Construct a new DatacenterParameterAdapter
     * @param arg0
     * @param arg1
     * @param arg2
     */
    public DatacenterParameterAdapter(ParameterValue arg0, ParameterDescription arg1, IndirectParameterValue arg2) {
        super(arg0, arg1, arg2);
    }
        
    /** @param arg0 - expects a {@link CEATargetIndicator} from which it will slurp the results.
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object arg0) throws CeaException {
        CEATargetIndicator ti = (CEATargetIndicator) arg0;
        try {
            if (this.indirectVal == null) {
                StringWriter sw = new StringWriter();
                Reader r = new InputStreamReader(ti.getStream());
                Piper.pipe(r,sw);
                val.setValue(sw.toString());
            } else {
                InputStream is = ti.getStream();
                OutputStream os = indirectVal.write();
                Piper.pipe(is,os);
                os.close();
                
            }
        } catch (IOException e) {
            throw new ParameterWriteBackException("Could not write back parameter",e);
        }
    }

}


/* 
$Log: DatacenterParameterAdapter.java,v $
Revision 1.3  2004/07/22 16:31:22  nw
cleaned up application / parameter adapter interface.

Revision 1.2  2004/07/20 02:14:48  nw
final implementaiton of itn06 Datacenter CEA interface

Revision 1.1  2004/07/13 17:11:09  nw
first draft of an itn06 CEA implementation for datacenter
 
*/