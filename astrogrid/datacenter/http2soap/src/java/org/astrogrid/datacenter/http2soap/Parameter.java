/*$Id: Parameter.java,v 1.1 2003/10/12 21:39:34 nw Exp $
 * Created on 30-Sep-2003
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.datacenter.http2soap;

/** representation of one parameter to the legacy web service.
 * @author Noel Winstanley nw@jb.man.ac.uk 30-Sep-2003
 *
 */
public class Parameter {

    /** parameter name */
    protected String name;
    /** parameter type - one of constants in {@link ParameterType} */
    protected String type;
    /** true is this is a 'fixed' parameter - i.e. is always passed as same value */
    protected boolean fixed;
    /** value to use when a parameter is fixed */
    protected String value;
    

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     * @param string
     */
    public void setName(String string) {
        name = string;
    }

    /**
     * @param string
     */
    public void setType(String string) {
        type = string;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    public boolean equals(Object obj) {
        Parameter other = (Parameter)obj;
        return this.name.equals(other.name) && this.type.equals(other.type);
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    public int hashCode() {
        return (this.name + this.type).hashCode();
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
       return this.getName() + "::" + this.getType();
    }

    /**
     * @return
     */
    public boolean isFixed() {
        return fixed;
    }

    /**
     * @return
     */
    public String getValue() {
        return value;
    }

    /**
     * @param b
     */
    public void setFixed(boolean b) {
        fixed = b;
    }

    /**
     * @param string
     */
    public void setValue(String string) {
        value = string;
    }

}


/* 
$Log: Parameter.java,v $
Revision 1.1  2003/10/12 21:39:34  nw
first import
 
*/