/*$Id: DefaultParameterAdapter.java,v 1.4 2004/07/22 16:33:48 nw Exp $
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

import org.astrogrid.applications.CeaException;
import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.applications.description.ParameterDescription;
import org.astrogrid.applications.parameter.indirect.IndirectParameterValue;
import org.astrogrid.io.Piper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.StringWriter;


/** default parameter adapter implementation
 * <p/>
 * handles both direct and indirect parameters
 */
public class DefaultParameterAdapter implements ParameterAdapter {
    public DefaultParameterAdapter(ParameterValue val,ParameterDescription description,IndirectParameterValue indirectVal) {
        this.val = val;
        this.description = description;
        this.indirectVal = indirectVal;
    }
    protected final ParameterValue val;
    protected final ParameterDescription description;
    protected final IndirectParameterValue indirectVal;
    /**
     * retreives the value for this paramter
     * if the parameter is direct, just return the value of the parameter value itself, 
     * if indirect, retreive the value first
     * @returrn always retirns the string value of this parameter
     *  */
    public Object process() throws CeaException {
        if (indirectVal == null) {
            return val.getValue();
        } else {
            try {
                StringWriter sw = new StringWriter();
                Reader r = new InputStreamReader(indirectVal.read());                
                Piper.pipe(r, sw);
                r.close();
                sw.close();
                return sw.toString();                
            }
            catch (IOException e) {
                throw new CeaException("Could not prociess parameter " + val.getName());
            }

        }
    }

    /** stores the value for this parameter back again
     * <p />
     * if the parameter is direct, stores the result directly in the parameter.
     * if the parameter is indirect, writes the result out to the remote resouce
     * @param o result object (will be stringified using {@link String.toString}
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
        if (indirectVal == null) {
            val.setValue(value);
        } else {
            PrintWriter pw = new PrintWriter(new OutputStreamWriter( indirectVal.write() ));
            pw.println(value);
            pw.close();
        }
    }

    public ParameterValue getWrappedParameter() {
        return val;
    }
}

/* 
$Log: DefaultParameterAdapter.java,v $
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