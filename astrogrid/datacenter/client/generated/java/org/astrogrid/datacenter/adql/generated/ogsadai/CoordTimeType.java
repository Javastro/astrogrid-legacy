/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordTimeType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class CoordTimeType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class CoordTimeType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _coordValue
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType _coordValue;

    /**
     * Field _coordError
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType _coordError;

    /**
     * Field _coordResolution
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType _coordResolution;

    /**
     * Field _coordSize
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType _coordSize;

    /**
     * Field _coordPixsize
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType _coordPixsize;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordTimeType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'coordError'.
     * 
     * @return the value of field 'coordError'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordError()
    {
        return this._coordError;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordError() 

    /**
     * Returns the value of field 'coordPixsize'.
     * 
     * @return the value of field 'coordPixsize'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordPixsize()
    {
        return this._coordPixsize;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordPixsize() 

    /**
     * Returns the value of field 'coordResolution'.
     * 
     * @return the value of field 'coordResolution'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordResolution()
    {
        return this._coordResolution;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordResolution() 

    /**
     * Returns the value of field 'coordSize'.
     * 
     * @return the value of field 'coordSize'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordSize()
    {
        return this._coordSize;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType getCoordSize() 

    /**
     * Returns the value of field 'coordValue'.
     * 
     * @return the value of field 'coordValue'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType getCoordValue()
    {
        return this._coordValue;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType getCoordValue() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

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
     * Sets the value of field 'coordError'.
     * 
     * @param coordError the value of field 'coordError'.
     */
    public void setCoordError(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType coordError)
    {
        this._coordError = coordError;
    } //-- void setCoordError(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType) 

    /**
     * Sets the value of field 'coordPixsize'.
     * 
     * @param coordPixsize the value of field 'coordPixsize'.
     */
    public void setCoordPixsize(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType coordPixsize)
    {
        this._coordPixsize = coordPixsize;
    } //-- void setCoordPixsize(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType) 

    /**
     * Sets the value of field 'coordResolution'.
     * 
     * @param coordResolution the value of field 'coordResolution'.
     */
    public void setCoordResolution(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType coordResolution)
    {
        this._coordResolution = coordResolution;
    } //-- void setCoordResolution(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType) 

    /**
     * Sets the value of field 'coordSize'.
     * 
     * @param coordSize the value of field 'coordSize'.
     */
    public void setCoordSize(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType coordSize)
    {
        this._coordSize = coordSize;
    } //-- void setCoordSize(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeValueType) 

    /**
     * Sets the value of field 'coordValue'.
     * 
     * @param coordValue the value of field 'coordValue'.
     */
    public void setCoordValue(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType coordValue)
    {
        this._coordValue = coordValue;
    } //-- void setCoordValue(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method unmarshalCoordTimeType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType unmarshalCoordTimeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType unmarshalCoordTimeType(java.io.Reader) 

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
