/*$Id: ParameterReferenceBean.java,v 1.5 2007/01/24 14:04:45 nw Exp $
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

/** Description of a parameter occuring in an interface to a remote application
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *@see org.astrogrid.acr.astrogrid.InterfaceBean;
 *@see org.astrogrid.acr.astrogrid.ParameterBean
 */
public class ParameterReferenceBean implements Serializable {

    /** Construct a new ParameterReferenceBean
     * 
     */
    public ParameterReferenceBean(String ref, int max,int min) {
        this.ref = ref;
        this.max = max;
        this.min = min;                
    }
    
    static final long serialVersionUID = -4214425657473163441L;
    protected final String ref;
    protected final int max;
    protected final int min;
    
    
    /** maximum number of times this parameter is permitted to occur */
    public int getMax() {
        return this.max;
    }
    /** minimum number of times this parameter is permitted to occur */
    public int getMin() {
        return this.min;
    }
    /** name of the parameter - should be used as a reference  of the corresponding parameterBean */
    public String getRef() {
        return this.ref;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
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
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + this.max;
		result = PRIME * result + this.min;
		result = PRIME * result + ((this.ref == null) ? 0 : this.ref.hashCode());
		return result;
	}
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		final ParameterReferenceBean other = (ParameterReferenceBean) obj;
		if (this.max != other.max)
			return false;
		if (this.min != other.min)
			return false;
		if (this.ref == null) {
			if (other.ref != null)
				return false;
		} else if (!this.ref.equals(other.ref))
			return false;
		return true;
	}
}


/* 
$Log: ParameterReferenceBean.java,v $
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