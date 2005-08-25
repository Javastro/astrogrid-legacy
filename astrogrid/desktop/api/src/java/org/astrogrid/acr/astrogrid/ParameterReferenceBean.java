/*$Id: ParameterReferenceBean.java,v 1.1 2005/08/25 16:59:44 nw Exp $
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

/** Description of a parameter occuring in an interface to a cea application
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2005
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
    /** name of the parameter - a reference name of the parameterBean */
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
}


/* 
$Log: ParameterReferenceBean.java,v $
Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/