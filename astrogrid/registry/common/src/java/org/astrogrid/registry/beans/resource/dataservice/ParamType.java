/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParamType.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

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
 * Class ParamType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class ParamType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the name of someone or something
     */
    private java.lang.String _name;

    /**
     * An account of the nature of the resource
     *  
     */
    private java.lang.String _description;

    /**
     * a (VOTable-supported) data type
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.DataType _dataType;

    /**
     * the measurement unit associated with a measurement.
     *  
     */
    private java.lang.String _unit;

    /**
     * the name of a unified content descriptor. See ....
     *  
     */
    private java.lang.String _UCD;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParamType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'dataType'. The field 'dataType'
     * has the following description: a (VOTable-supported) data
     * type
     *  
     * 
     * @return the value of field 'dataType'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.DataType getDataType()
    {
        return this._dataType;
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataType getDataType() 

    /**
     * Returns the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
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
     * following description: the name of someone or something
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'UCD'. The field 'UCD' has the
     * following description: the name of a unified content
     * descriptor. See ....
     *  
     * 
     * @return the value of field 'UCD'.
     */
    public java.lang.String getUCD()
    {
        return this._UCD;
    } //-- java.lang.String getUCD() 

    /**
     * Returns the value of field 'unit'. The field 'unit' has the
     * following description: the measurement unit associated with
     * a measurement.
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
     * the following description: a (VOTable-supported) data type
     *  
     * 
     * @param dataType the value of field 'dataType'.
     */
    public void setDataType(org.astrogrid.registry.beans.resource.dataservice.DataType dataType)
    {
        this._dataType = dataType;
    } //-- void setDataType(org.astrogrid.registry.beans.resource.dataservice.DataType) 

    /**
     * Sets the value of field 'description'. The field
     * 'description' has the following description: An account of
     * the nature of the resource
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
     * following description: the name of someone or something
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'UCD'. The field 'UCD' has the
     * following description: the name of a unified content
     * descriptor. See ....
     *  
     * 
     * @param UCD the value of field 'UCD'.
     */
    public void setUCD(java.lang.String UCD)
    {
        this._UCD = UCD;
    } //-- void setUCD(java.lang.String) 

    /**
     * Sets the value of field 'unit'. The field 'unit' has the
     * following description: the measurement unit associated with
     * a measurement.
     *  
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(java.lang.String unit)
    {
        this._unit = unit;
    } //-- void setUnit(java.lang.String) 

    /**
     * Method unmarshalParamType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.ParamType unmarshalParamType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.ParamType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.ParamType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.ParamType unmarshalParamType(java.io.Reader) 

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
