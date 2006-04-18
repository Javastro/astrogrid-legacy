/*$Id: ParameterBean.java,v 1.3 2006/04/18 23:25:45 nw Exp $
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
import java.net.URI;

/** description of a single parameter for a remote application.
 * 
 * NB - all fields, apart from <tt>name</tt> and <tt>type</tt> may be null
 * @author Noel Winstanley nw@jb.man.ac.uk 17-Aug-2005
 *
 */
public class ParameterBean  implements Serializable{

    /** Construct a new ParameterInformation
     * @param name
     * @param id
     */
    public ParameterBean(String name, String uiname, String description,
            String ucd,String defaultValue,String units,String type,String subType,String[] options){
       this.name = name;
    this.uiName = uiname;
    this.description =description;
    this.ucd = ucd;
    this.defaultValue = defaultValue;
    this.units = units;
    this.type = type;
    this.subType = subType;
    this.options = options;
    
    }
    static final long serialVersionUID = -6502972617913043329L;
    protected final String name;
    protected final String description;
    protected final String uiName;
    protected final String ucd;
    protected final String defaultValue;
    protected final String units;
    protected final String type;
    protected final String subType;
    protected final String[] options;
    

    /** default value for the parameter */
    public String getDefaultValue() {
        return this.defaultValue;
    }
    /** descriptin of the parameter */
    public String getDescription() {
        return this.description;
    }
    /** name of the parameter- as used in the generated invocation document */
    public String getName() {
        return this.name;
    }
    /** an enumeration of possible values for the parameter - may be null. */
    public String[] getOptions() {
        return this.options;
    }
    /** some further description of the type of this parameter - e.g. acceptable ranges */
    public String getSubType() {
        return this.subType;
    }
    /** type of ths parameter */
    public String getType() {
        return this.type;
    }
    /** the UCD associated with this parameter type */
    public String getUcd() {
        return this.ucd;
    }
    /** user-readable name for this parameter */
    public String getUiName() {
        return this.uiName;
    }
    /** descirption of the units for this parameter */
    public String getUnits() {
        return this.units;
    }
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("[ParameterInformation:");
        buffer.append(" name: ");
        buffer.append(name);
        buffer.append(" description: ");
        buffer.append(description);
        buffer.append(" uiName: ");
        buffer.append(uiName);
        buffer.append(" ucd: ");
        buffer.append(ucd);
        buffer.append(" defaultValue: ");
        buffer.append(defaultValue);
        buffer.append(" units: ");
        buffer.append(units);
        buffer.append(" type: ");
        buffer.append(type);
        buffer.append(" subType: ");
        buffer.append(subType);
        buffer.append(" { ");
        for (int i0 = 0; options != null && i0 < options.length; i0++) {
            buffer.append(" options[" + i0 + "]: ");
            buffer.append(options[i0]);
        }
        buffer.append(" } ");
        buffer.append("]");
        return buffer.toString();
    }
    /**
     * Equality based on value of {@link #name} only
     */
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null) {
            return false;
        }
        if (o.getClass() != getClass()) {
            return false;
        }
        ParameterBean castedObj = (ParameterBean) o;
        return ((this.name == null ? castedObj.name == null : this.name.equals(castedObj.name)));
    }
}


/* 
$Log: ParameterBean.java,v $
Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/