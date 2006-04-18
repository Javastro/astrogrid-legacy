/*$Id: InterfaceBean.java,v 1.3 2006/04/18 23:25:45 nw Exp $
 * Created on 17-Aug-2005
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.astrogrid;

import java.io.Serializable;

/** Description of  an interface to a remote application.
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2005
 *@see org.astrogrid.acr.astrogrid.ParameterBean
 *@see org.astrogrid.acr.astrogrid.ParameterReferenceBean
 */
public class InterfaceBean implements Serializable {

    /** Construct a new InterfaceInformation
     * 
     */
    public InterfaceBean(String name,ParameterReferenceBean[] inputs, ParameterReferenceBean[] outputs) {
        super();
        this.name =name;
        this.inputs = inputs;
        this.outputs = outputs;
    }
    
    static final long serialVersionUID = -7711917087192288451L;
    protected final String name;
    protected ParameterReferenceBean[] inputs;
    protected ParameterReferenceBean[] outputs;
    

    /**  list of input parameters */
    public ParameterReferenceBean[] getInputs() {
        return this.inputs;
    }
    /** the name of the interface */
    public String getName() {
        return this.name;
    }
    /** list of output parameters */
    public ParameterReferenceBean[] getOutputs() {
        return this.outputs;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[InterfaceBean:");
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" { ");
        for (int i0 = 0; inputs != null && i0 < inputs.length; i0++) {
            buffer.append(" inputs[" + i0 + "]: ");
            buffer.append(inputs[i0]);
        }
        buffer.append(" } ");
        buffer.append(" { ");
        for (int i0 = 0; outputs != null && i0 < outputs.length; i0++) {
            buffer.append(" outputs[" + i0 + "]: ");
            buffer.append(outputs[i0]);
        }
        buffer.append(" } ");
        buffer.append("]");
        return buffer.toString();
    }
}


/* 
$Log: InterfaceBean.java,v $
Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/