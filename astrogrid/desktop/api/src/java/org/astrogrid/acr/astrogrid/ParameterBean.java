/*$Id: ParameterBean.java,v 1.9 2009/03/30 15:02:54 nw Exp $
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

import org.astrogrid.acr.ivoa.Registry;
import org.astrogrid.acr.ivoa.resource.BaseParam;

/** An input or output parameter of a remote application (CEA).
 * 
 * @note Most fields are inherited from the parent class - {@link BaseParam}, 
 * @note All fields, apart from <tt>name</tt> and <tt>type</tt> may be null
 * 
 * @note In previous schema, the element now called {@code name}
 * was called {@code UiName}, while the element now called {@code id} was called {@code name}.
 * Because of this overlapping name shift, it's impossible to just deprecate the previous methods.
 * So, to ensure backwards compatibility, this bean provides access to elements in the new schema as follows
 * <table>
 * <tr>
 * <th>element</th><th>getter</th></tr>
 * <tr>
 * <td>{@code id}</td><td>{@link #getId()}, <br/>{@link #getName()} (deprecated)</td>
 * </tr>
 * <tr>
 * <td>{@code name}</td><td>{@link #getUiName()}</td>
 * </tr>
 * </table>
 * <br/>
 * 
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 * @bean
 * @see CeaApplication
 * @see Applications Executing remote applications
 * @see Registry Querying for registry resources
 * @see <a href='http://www.ivoa.net/Documents/latest/CEA.html'>CEA Specification</a>
 */
public class ParameterBean extends BaseParam{
    /*
    * unimplemented - Range.
    */
    /**
     * 
     */
    private static final long serialVersionUID = 2983698574593000414L;

/**
 * @exclude
 */
public ParameterBean() {
}
    /** Construct a new ParameterInformation
     * @exclude
     * @param name
     * @param id
     * @exclude 
     * @deprecated use setters instead
     * 
     */
    @Deprecated
    public ParameterBean(final String name, final String uiname, final String description,
            final String ucd,final String defaultValue,final String units,final String type,final String subType,final String[] options){
       this.id = name;
    this.defaultValues = new String[]{defaultValue};
    this.type = type;
    this.options = options;
    setName(uiname);
    setDescription(description);
    setUcd(ucd);
    setUnit(units);
    
    }
    protected String id;
    protected String[] defaultValues;
    protected String type;
    protected String[] options; // should I initialize this to an array?.
    protected String uType;
    protected String mimeType;
    protected String arraysize = "1";
    

    /** default value for the parameter
     * @exclude 
     * @deprecated use {@link #getDefaultValues()} */
    @Deprecated
    public String getDefaultValue() {
        if (defaultValues == null || defaultValues.length == 0) {
            return null;
        } 
        return this.defaultValues[0];
    }
 
    /** the identifier for the parameter
     * @exclude 
     * @deprecated use {@link #getId()} */
    @Override
    @Deprecated
    public String getName() {
        return this.getId();
    }
    /** @exclude 
     * @deprecated - use getUnit() */
    @Deprecated
    public String getUnits() {
        return super.getUnit();
    }
    /** the unique identifier for the parameter */
    public String getId() {
        return id;
    }
    /** an enumeration of possible values for the parameter - may be null. */
    public String[] getOptions() {
        return this.options;
    }
    /** some further description of the type of this parameter - e.g. acceptable ranges 
     * @exclude 
     * @deprecated unused.
     * @return null*/
    @Deprecated
    public String getSubType() {
        return null;
    }
    /** type of ths parameter
     * @return A type defined in the CEA specification. One of {@code integer}, {@code real}, {@code complex}
     * ,{@code text},{@code boolean}, {@code anyURI}, {@code VOTable},
     * {@code RA}, {@code Dec}, {@code MJD}, {@code DateTime},
     * {@code ADQL}, {@code ADQL-S},{@code STC-S},{@code binary},{@code FITS},{@code XML} */
    
    public String getType() {
        return this.type;
    }

    /** user-readable name for this parameter.
     * 
     * @warning Once the deprecated method {@link #getName()} has been retired
     * this method will be deprecated in favour of getName */
    public String getUiName() {
        return super.getName();
    }

    
    /** @exclude */
    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }
    /** @exclude */
    @Override
    public boolean equals(final Object obj) {
        if (this == obj) {
            return true;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ParameterBean other = (ParameterBean) obj;
        if (this.id == null) {
            if (other.id != null) {
                return false;
            }
        } else if (!this.id.equals(other.id)) {
            return false;
        }
        return true;
    }

    /** a possible default for this type of parameter
                        this is array-valued for the case of array
                        parameters.*/
    public final String[] getDefaultValues() {
        return this.defaultValues;
    }
    /** @exclude */
    public final void setDefaultValues(final String[] defaultValues) {
        this.defaultValues = defaultValues;
    }
    /** @exclude */
    public final void setId(final String id) {
        this.id = id;
    }
    /** @exclude */
    public final void setType(final String type) {
        this.type = type;
    }
    /** @exclude */
    public final void setOptions(final String[] options) {
        this.options = options;
    }
/** possibly access uType for this parameter
 *  @return may be null */
    public final String getUType() {
        return this.uType;
    }
    /** @exclude */
    public final void setUType(final String type) {
        this.uType = type;
    }
/** possibly a mime type for this parameter 
 * @return may be null*/
    public final String getMimeType() {
        return this.mimeType;
    }
    /** @exclude */
    public final void setMimeType(final String mimeType) {
        this.mimeType = mimeType;
    }
    /**                     the shape of the array that constitutes the value.
     * <p/>
     *             An expression of a the shape of a multi-dimensional array
            of the form LxNxM... where each value between gives the
            integer length of the array along a dimension.  An
            asterisk (*) as the last dimension of the shape indicates 
            that the length of the last axis is variable or
            undetermined. 
            <p />
     *                      the default is "1"; i.e. the value is a scalar.
     * */    
    public final String getArraysize() {
        return this.arraysize;
    }
    /** @exclude */
    public final void setArraysize(final String array) {
        this.arraysize = array;
    }
}


/* 
$Log: ParameterBean.java,v $
Revision 1.9  2009/03/30 15:02:54  nw
Added override annotations.

Revision 1.8  2008/09/25 16:02:04  nw
documentation overhaul

Revision 1.7  2008/01/25 07:33:24  nw
final api changes for reg1.0 upgrade

Revision 1.6  2008/01/21 09:47:26  nw
Incomplete - task 134: Upgrade to reg v1.0

Revision 1.5  2007/03/08 17:46:56  nw
removed deprecated interfaces.

Revision 1.4  2007/01/24 14:04:44  nw
updated my email address

Revision 1.3  2006/04/18 23:25:45  nw
merged asr development.

Revision 1.2.6.1  2006/04/04 10:31:25  nw
preparing to move to mac.

Revision 1.2  2006/02/02 14:19:48  nw
fixed up documentation.

Revision 1.1  2005/08/25 16:59:44  nw
1.1-beta-3
 
*/