/*$Id: FunctionBean.java,v 1.6 2008/09/25 16:02:04 nw Exp $
 * Created on 22-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.acr.ivoa;

import java.io.Serializable;
import java.util.Arrays;

import org.astrogrid.acr.astrogrid.ParameterBean;

/** @exclude
 * doesn't seem to be used anywhere. - thinkn it's a hang over from attempt at skynode support.
 * description of one ADQL function

 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 22-Feb-2006
 *
 */
public class FunctionBean implements Serializable{

    private static int hashCode(final Object[] array) {
		final int PRIME = 31;
		if (array == null) {
            return 0;
        }
		int result = 1;
		for (int index = 0; index < array.length; index++) {
			result = PRIME * result + (array[index] == null ? 0 : array[index].hashCode());
		}
		return result;
	}
	/** Construct a new FunctionBean
     * @param name
     * @param description
     * @param parameters
     */
    public FunctionBean(final String name, final String description, final ParameterBean[] parameters) {
        super();
        this.name = name;
        this.description = description;
        this.parameters = parameters;
    }

    
    static final long serialVersionUID = -7423110308499775880L;
    private final String name;
    private final String description;
    private final ParameterBean[] parameters;
    /** description of this function */
    public String getDescription() {
        return this.description;
    }
    /** name of this function */
    public String getName() {
        return this.name;
    }
    /** descriptin of parameters for this function 
     * @return an array of {@link ParameterBean}, where only name, description and type will be specified
     * */
    public ParameterBean[] getParameters() {
        return this.parameters;
    }
    
    public String toString() {
        final StringBuffer sb = new StringBuffer("FunctionBean[");
        sb.append("name: ");
        sb.append(name);
        sb.append(", description: ");
        sb.append(description);
        sb.append(", parameters: [");
        for (int i = 0; i < parameters.length; i++) {
            sb.append(parameters[i]);
        }
        sb.append("]]");
        return sb.toString();
    }
	public int hashCode() {
		final int PRIME = 31;
		int result = 1;
		result = PRIME * result + ((this.description == null) ? 0 : this.description.hashCode());
		result = PRIME * result + ((this.name == null) ? 0 : this.name.hashCode());
		result = PRIME * result + FunctionBean.hashCode(this.parameters);
		return result;
	}
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
		final FunctionBean other = (FunctionBean) obj;
		if (this.description == null) {
			if (other.description != null) {
                return false;
            }
		} else if (!this.description.equals(other.description)) {
            return false;
        }
		if (this.name == null) {
			if (other.name != null) {
                return false;
            }
		} else if (!this.name.equals(other.name)) {
            return false;
        }
		if (!Arrays.equals(this.parameters, other.parameters)) {
            return false;
        }
		return true;
	}

}


/* 
$Log: FunctionBean.java,v $
Revision 1.6  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.5  2007/03/08 17:48:06  nw
tidied.

Revision 1.4  2007/01/24 14:04:45  nw
updated my email address

Revision 1.3  2006/06/15 09:01:42  nw
provided implementations of equals()

Revision 1.2  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.1.2.2  2006/04/04 10:31:26  nw
preparing to move to mac.

Revision 1.1.2.1  2006/03/22 17:27:20  nw
first development snapshot

Revision 1.1  2006/02/24 12:17:52  nw
added interfaces for skynode
 
*/