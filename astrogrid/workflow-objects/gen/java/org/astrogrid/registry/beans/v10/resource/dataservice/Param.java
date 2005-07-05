/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Param.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Param.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class Param extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the name of the column
     *  
     */
    private java.lang.String _name;

    /**
     * a description of the column's contents
     *  
     */
    private java.lang.String _description;

    /**
     * a type of data contained in the column
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.DataType _dataType;

    /**
     * the unit associated with all values in the column
     *  
     */
    private java.lang.String _unit;

    /**
     * the name of a unified content descriptor. See ....
     *  
     */
    private java.lang.String _ucd;


      //----------------/
     //- Constructors -/
    //----------------/

    public Param() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof Param) {
        
            Param temp = (Param)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._description != null) {
                if (temp._description == null) return false;
                else if (!(this._description.equals(temp._description))) 
                    return false;
            }
            else if (temp._description != null)
                return false;
            if (this._dataType != null) {
                if (temp._dataType == null) return false;
                else if (!(this._dataType.equals(temp._dataType))) 
                    return false;
            }
            else if (temp._dataType != null)
                return false;
            if (this._unit != null) {
                if (temp._unit == null) return false;
                else if (!(this._unit.equals(temp._unit))) 
                    return false;
            }
            else if (temp._unit != null)
                return false;
            if (this._ucd != null) {
                if (temp._ucd == null) return false;
                else if (!(this._ucd.equals(temp._ucd))) 
                    return false;
            }
            else if (temp._ucd != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'dataType'. The field 'dataType'
     * has the following description: a type of data contained in
     * the column
     *  
     * 
     * @return the value of field 'dataType'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.DataType getDataType()
    {
        return this._dataType;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.DataType getDataType() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: a description
     * of the column's contents
     *  
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: the name of the column
     *  
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'ucd'. The field 'ucd' has the
     * following description: the name of a unified content
     * descriptor. See ....
     *  
     * 
     * @return the value of field 'ucd'.
     */
    public java.lang.String getUcd()
    {
        return this._ucd;
    } //-- java.lang.String getUcd() 

    /**
     * Returns the value of field 'unit'. The field 'unit' has the
     * following description: the unit associated with all values
     * in the column
     *  
     * 
     * @return the value of field 'unit'.
     */
    public java.lang.String getUnit()
    {
        return this._unit;
    } //-- java.lang.String getUnit() 

    /**
     * Method isValid
     */
    public boolean isValid()
    {
        try {
            validate();
        }
        catch (org.exolab.castor.xml.ValidationException vex) {
            return false;
        }
        return true;
    } //-- boolean isValid() 

    /**
     * Method marshal
     * 
     * @param out
     */
    public void marshal(java.io.Writer out)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, out);
    } //-- void marshal(java.io.Writer) 

    /**
     * Method marshal
     * 
     * @param handler
     */
    public void marshal(org.xml.sax.ContentHandler handler)
        throws java.io.IOException, org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        
        Marshaller.marshal(this, handler);
    } //-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Sets the value of field 'dataType'. The field 'dataType' has
     * the following description: a type of data contained in the
     * column
     *  
     * 
     * @param dataType the value of field 'dataType'.
     */
    public void setDataType(org.astrogrid.registry.beans.v10.resource.dataservice.DataType dataType)
    {
        this._dataType = dataType;
    } //-- void setDataType(org.astrogrid.registry.beans.v10.resource.dataservice.DataType) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: a description
     * of the column's contents
     *  
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: the name of the column
     *  
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'ucd'. The field 'ucd' has the
     * following description: the name of a unified content
     * descriptor. See ....
     *  
     * 
     * @param ucd the value of field 'ucd'.
     */
    public void setUcd(java.lang.String ucd)
    {
        this._ucd = ucd;
    } //-- void setUcd(java.lang.String) 

    /**
     * Sets the value of field 'unit'. The field 'unit' has the
     * following description: the unit associated with all values
     * in the column
     *  
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(java.lang.String unit)
    {
        this._unit = unit;
    } //-- void setUnit(java.lang.String) 

    /**
     * Method unmarshalParam
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Param unmarshalParam(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Param) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Param.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Param unmarshalParam(java.io.Reader) 

    /**
     * Method validate
     */
    public void validate()
        throws org.exolab.castor.xml.ValidationException
    {
        org.exolab.castor.xml.Validator validator = new org.exolab.castor.xml.Validator();
        validator.validate(this);
    } //-- void validate() 

}
