/*$Id: InterfaceBean.java,v 1.9 2009/03/30 15:02:55 nw Exp $
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
import java.util.Arrays;

/** Registry description of a parameter-set supported by a remote (CEA) application.
 *@see CeaApplication
 *@bean
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 */
public class InterfaceBean implements Serializable {

    /* <br />
    * omits InterfaceConstants at the moment - as these don't seem to be settled in the specification yet.
    * <br/>
    * likewise, parameter groups and conditional groups are omitted - need to see a real-world example of their use before spending effort implementing this.
    */
	/**
     * 
     */
    private static final long serialVersionUID = -2556943626640025802L;


    private static int hashCode(final Object[] array) {
        final int prime = 31;
        if (array == null) {
            return 0;
        }
        int result = 1;
        for (int index = 0; index < array.length; index++) {
            result = prime * result
                    + (array[index] == null ? 0 : array[index].hashCode());
        }
        return result;
    }
/** @exclude */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime
                * result
                + ((this.description == null) ? 0 : this.description.hashCode());
        result = prime * result + InterfaceBean.hashCode(this.inputs);
        result = prime * result
                + ((this.name == null) ? 0 : this.name.hashCode());
        result = prime * result + InterfaceBean.hashCode(this.outputs);
        return result;
    }
/** @exclude */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final InterfaceBean other = (InterfaceBean) obj;
        if (this.description == null) {
            if (other.description != null) {
                return false;
            }
        } else if (!this.description.equals(other.description)) {
            return false;
        }
        if (!Arrays.equals(this.inputs, other.inputs)) {
            return false;
        }
        if (this.name == null) {
            if (other.name != null) {
                return false;
            }
        } else if (!this.name.equals(other.name)) {
            return false;
        }
        if (!Arrays.equals(this.outputs, other.outputs)) {
            return false;
        }
        return true;
    }

    /** Construct a new InterfaceInformation
     * @exclude 
     * @deprecated
     */
    @Deprecated
    public InterfaceBean(final String name,final ParameterReferenceBean[] inputs, final ParameterReferenceBean[] outputs) {
        super();
        this.name =name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.description = null;
    }
    /** @exclude */
    public InterfaceBean(final String name,final String description,final ParameterReferenceBean[] inputs, final ParameterReferenceBean[] outputs) {
        super();
        this.name =name;
        this.inputs = inputs;
        this.outputs = outputs;
        this.description = description;
    }
    
    protected final String name;
    protected final String description;
    protected ParameterReferenceBean[] inputs = new ParameterReferenceBean[0];
    protected ParameterReferenceBean[] outputs = new ParameterReferenceBean[0];
    

    /**  list of input parameters 
     * */
    public ParameterReferenceBean[] getInputs() {
        return this.inputs;
    }
    /** the name of this interface */
    public String getName() {
        return this.name;
    }
    /** list of output parameters */
    public ParameterReferenceBean[] getOutputs() {
        return this.outputs;
    }
    /** @exclude */
    @Override
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
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
    /**A human-readable description of this parameter set.*/
    public final String getDescription() {
        return this.description;
    }
	
}


/* 
$Log: InterfaceBean.java,v $
Revision 1.9  2009/03/30 15:02:55  nw
Added override annotations.

Revision 1.8  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.7  2008/01/21 09:47:26  nw
Incomplete - task 134: Upgrade to reg v1.0

Revision 1.6  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.5  2007/01/24 14:04:44  nw
updated my email address

Revision 1.4  2006/06/15 09:01:27  nw
provided implementations of equals()

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/