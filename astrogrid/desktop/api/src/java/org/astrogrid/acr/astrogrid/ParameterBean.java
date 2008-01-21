/*$Id: ParameterBean.java,v 1.6 2008/01/21 09:47:26 nw Exp $
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

import org.astrogrid.acr.ivoa.resource.BaseParam;

/** description of a single parameter for a remote application.
 * 
 * NB - all fields, apart from <tt>name</tt> and <tt>type</tt> may be null
 * <p />
 * <b>Design Note</b> - in previous schema, the element now called <i>name</i>
 * was called <i>UiName</i>, while the element now called <i>id</i> was called <i>name</i>.
 * Because of this overlapping name shift, it's impossble to just deprecate the previous methods.
 * So, to ensure backwards compatability, this bean provides access to elements in the new schema as follows
 * <table>
 * <tr>
 * <th>element</th><th>getter</th><th>setter</th></tr>
 * <tr>
 * <td><tt>id</tt></td><td>{@link #getId()}, <br/>{@link #getName()} (deprecated)</td><td>{@link #setId()}</td>
 * </tr>
 * <tr>
 * <td><tt>name</tt></td><td>{@link #getUiName()}</td><td>{@link #setName()}</td>
 * </tr>
 * </table>
 * <br/>
 * unimplemented - Range.
 * @author Noel Winstanley noel.winstanley@manchester.ac.uk 17-Aug-2005
 *
 */
public class ParameterBean extends BaseParam{

    /**
     * 
     */
    private static final long serialVersionUID = 2983698574593000414L;


    /** Construct a new ParameterInformation
     * @param name
     * @param id
     * @deprecated use setters instead
     * 
     */
    public ParameterBean(String name, String uiname, String description,
            String ucd,String defaultValue,String units,String type,String subType,String[] options){
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
     * @deprecated use {@link #getDefaultValues()} */
    public String getDefaultValue() {
        if (defaultValues == null || defaultValues.length == 0) {
            return null;
        } 
        return this.defaultValues[0];
    }
 
    /** the identifier for the parameter
     * @deprecated use {@link #getId()} */
    public String getName() {
        return this.getId();
    }
    /** @deprecated - use getUnit() */
    public String getUnits() {
        return super.getUnit();
    }
    /** the identifier for the parameter */
    public String getId() {
        return id;
    }
    /** an enumeration of possible values for the parameter - may be null. */
    public String[] getOptions() {
        return this.options;
    }
    /** some further description of the type of this parameter - e.g. acceptable ranges 
     * @deprecated unused.
     * @return null*/
    public String getSubType() {
        return null;
    }
    /** type of ths parameter
     * @return 'integer','real','complex','text','boolean','anyURI','VOTable','RA','Dec','MJD','DateTime','ADQL','ADQL-S','STC-S','binary','FITS','XML' */
    public String getType() {
        return this.type;
    }

    /** user-readable name for this parameter
     * <br />
     * <b>warning</b> Once the deprecated method {@link #getName()} has been retired
     * this method will be deprecated in favour of getName */
    public String getUiName() {
        return super.getName();
    }

    

    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((this.id == null) ? 0 : this.id.hashCode());
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (getClass() != obj.getClass())
            return false;
        final ParameterBean other = (ParameterBean) obj;
        if (this.id == null) {
            if (other.id != null)
                return false;
        } else if (!this.id.equals(other.id))
            return false;
        return true;
    }

    /** a possible default for this type of parameter
                        this is repeateable for the case of array
                        parameters.*/
    public final String[] getDefaultValues() {
        return this.defaultValues;
    }

    public final void setDefaultValues(String[] defaultValues) {
        this.defaultValues = defaultValues;
    }

    public final void setId(String id) {
        this.id = id;
    }

    public final void setType(String type) {
        this.type = type;
    }

    public final void setOptions(String[] options) {
        this.options = options;
    }
/** possibly a uType for this parameter */
    public final String getUType() {
        return this.uType;
    }

    public final void setUType(String type) {
        this.uType = type;
    }
/** possibly a mime type for this parameter */
    public final String getMimeType() {
        return this.mimeType;
    }

    public final void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
    /**                     the shape of the array that constitutes the value
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

    public final void setArraysize(String array) {
        this.arraysize = array;
    }
}


/* 
$Log: ParameterBean.java,v $
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