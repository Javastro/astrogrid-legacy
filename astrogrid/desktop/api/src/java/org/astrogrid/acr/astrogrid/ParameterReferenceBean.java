/*$Id: ParameterReferenceBean.java,v 1.6 2008/09/25 16:02:04 nw Exp $
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

/** A reference to parameter of a remote application (CEA).
 * 
 * This class is used to to allow an {@link InterfaceBean interface} of a CEA application
 * to refer to one of it's {@link ParameterBean parameters}.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *@see org.astrogrid.acr.astrogrid.InterfaceBean
 *@see org.astrogrid.acr.astrogrid.ParameterBean
 *@see CeaApplication
 * @see Applications Executing remote applications
 *@bean
 */
public class ParameterReferenceBean implements Serializable {

    /** Construct a new ParameterReferenceBean
     * @exclude
     */
    public ParameterReferenceBean(final String ref, final int max,final int min) {
        this.ref = ref;
        this.max = max;
        this.min = min;                
    }
    
    static final long serialVersionUID = -4214425657473163441L;
    protected final String ref;
    protected final int max;
    protected final int min;
    
    
    /** maximum number of times this parameter is permitted to occur
     * @note {@code 0} denotes unlimited number of times. */
    public int getMax() {
        return this.max;
    }
    /** minimum number of times this parameter is permitted to occur */
    public int getMin() {
        return this.min;
    }
    /** name of the parameter.
     * 
     *  Can be used as a key to find the corresponding parameterBean */
    public String getRef() {
        return this.ref;
    }
    /** @exclude */
    public String toString() {
        final StringBuffer buffer = new StringBuffer();
        buffer.append("[ParameterReferenceBean:");
        buffer.append(" ref: ");
        buffer.append(ref);
        buffer.append(" max: ");
        buffer.append(max);
        buffer.append(" min: ");
        buffer.append(min);
        buffer.append("]");
        return buffer.toString();
    }
    /**@exclude */
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.max;
		result = PRIME * result + this.min;
		result = PRIME * result + ((this.ref == null) ? 0 : this.ref.hashCode());
		return result;
	}
	/** @exclude */
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
		final ParameterReferenceBean other = (ParameterReferenceBean) obj;
		if (this.max != other.max) {
            return false;
        }
		if (this.min != other.min) {
            return false;
        }
		if (this.ref == null) {
			if (other.ref != null) {
                return false;
            }
		} else if (!this.ref.equals(other.ref)) {
            return false;
        }
		return true;
	}
}


/* 
$Log: ParameterReferenceBean.java,v $
Revision 1.6  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.5  2007/01/24 14:04:45  nw
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