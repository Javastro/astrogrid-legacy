/*$Id: DefaultParameterAdapter.java,v 1.12 2006/03/11 05:57:54 clq2 Exp $
 * Created on 04-Jun-2004
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.applications.parameter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.protocol.ExternalValue;
import org.astrogrid.io.Piper;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;


/** The default implementation of {@link org.astrogrid.applications.parameter.ParameterAdapter}
 * <p/>
 * Handles both direct and indirect parameters, returing them as an in-memory string.
 * @see org.astrogrid.applications.parameter.protocol.ProtocolLibrary#getExternalValue(ParameterValue)
 * @author Noel Winstanley (nw@jb.man.ac.uk)
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @todo should really do different things according to what the type of the parameter is.
 * @todo check whether it is really appropriate use Readers and Writers to access the indirect parameters
 */
public class DefaultParameterAdapter extends AbstractParameterAdapter {
    /**
     * Constructor.
    * @param val
    * @param description
    * @param externalVal
    */
   public DefaultParameterAdapter(ParameterValue val, ParameterDescription description, ExternalValue externalVal) {
      super(val, description, externalVal);
      
   }

   /**
     * Commons Logger for this class
     */
    private static final Log logger = LogFactory
            .getLog(DefaultParameterAdapter.class);

     /**
     * retreives the value for this paramter
     * if the parameter is direct, just return the value of the parameter value itself, 
     * if indirect, retreive the value from the {@link #externalVal}
     * @return always returns the string value of this parameter
     *  */
    public Object process() throws CeaException {
        if (externalVal == null) {
            return val.getValue();
        } else {
            Reader r = null;
            StringWriter sw = null;
            try {
                sw = new StringWriter();
                r = new InputStreamReader(externalVal.read());                
                Piper.pipe(r, sw);

                return sw.toString();                
            }
            catch (IOException e) {
                throw new CeaException("Could not process parameter " + val.getName());
            } finally {
                if (r != null) {
                    try {
                        r.close();
                    } catch (IOException e) {
                        logger.warn("could not close reader",e);
                    }
                }
                if (sw != null) {
                    try {
                        sw.close();
                    } catch (IOException e) {
                        logger.warn("could not close writer",e);
                    }
                }
            }

        }
    }

    /** stores the value for this parameter back again
     * <p />
     * if the parameter is direct, stores the result directly in the parameter value
     * if the parameter is indirect, writes the result out to the remote resouce
     * @param o result object (will be stringified using {@link String#toString()}
     * @see org.astrogrid.applications.parameter.ParameterAdapter#writeBack(java.lang.Object)
     */
    public void writeBack(Object o) throws CeaException {
        //don't trust it...
        String value;        
        if (o == null) {
            value = "<null>";
        } else {
            value = o.toString();
        }
        if (externalVal == null) {
            val.setValue(value);
        } else {
            PrintWriter pw = null;
            try {
                pw = new PrintWriter(new OutputStreamWriter( externalVal.write() ));
                pw.println(value);
            } finally {
                if (pw != null) {
                    pw.close();
                }
            }
        }
    }
}

/* 
$Log: DefaultParameterAdapter.java,v $
Revision 1.12  2006/03/11 05:57:54  clq2
roll back to before merged apps_gtr_1489, tagged as rolback_gtr_1489

Revision 1.10  2004/11/27 13:20:02  pah
result of merge of pah_cea_bz561 branch

Revision 1.9.10.1  2004/10/27 16:04:05  pah
pulled up an AbstractParameterAdapter

Revision 1.9  2004/09/22 10:52:51  pah
getting rid of some unused imports

Revision 1.8  2004/09/17 11:39:07  nw
made sure streams are closed

Revision 1.7  2004/08/28 07:17:34  pah
commandline parameter passing - unit tests ok

Revision 1.6  2004/07/30 14:54:47  jdt
merges in from case3 branch

Revision 1.5.2.1  2004/07/30 13:11:59  jdt
typo

Revision 1.5  2004/07/26 12:07:38  nw
renamed indirect package to protocol,
renamed classes and methods within protocol package
javadocs

Revision 1.4  2004/07/22 16:33:48  nw
reads in values fully now.

Revision 1.3  2004/07/20 02:03:23  nw
doc

Revision 1.2  2004/07/01 11:16:22  nw
merged in branch
nww-itn06-componentization

Revision 1.1.2.2  2004/07/01 01:42:46  nw
final version, before merge

Revision 1.1.2.1  2004/06/17 09:21:23  nw
finished all major functionality additions to core

Revision 1.1.2.1  2004/06/14 08:56:58  nw
factored applications into sub-projects,
got packaging of wars to work again
 
*/