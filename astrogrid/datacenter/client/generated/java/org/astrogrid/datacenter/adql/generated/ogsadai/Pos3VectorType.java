/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Pos3VectorType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Pos3VectorType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Pos3VectorType extends org.astrogrid.datacenter.adql.generated.ogsadai.PosCoordType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pos_ang_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType _pos_ang_unit = org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType.valueOf("deg");

    /**
     * Field _coordValue
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType _coordValue;

    /**
     * Field _coordError
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType _coordError;

    /**
     * Field _coordResolution
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType _coordResolution;

    /**
     * Field _coordSize
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType _coordSize;

    /**
     * Field _coordPixsize
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType _coordPixsize;


      //----------------/
     //- Constructors -/
    //----------------/

    public Pos3VectorType() {
        super();
        setPos_ang_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType.valueOf("deg"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'coordError'.
     * 
     * @return the value of field 'coordError'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordError()
    {
        return this._coordError;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordError() 

    /**
     * Returns the value of field 'coordPixsize'.
     * 
     * @return the value of field 'coordPixsize'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordPixsize()
    {
        return this._coordPixsize;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordPixsize() 

    /**
     * Returns the value of field 'coordResolution'.
     * 
     * @return the value of field 'coordResolution'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordResolution()
    {
        return this._coordResolution;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordResolution() 

    /**
     * Returns the value of field 'coordSize'.
     * 
     * @return the value of field 'coordSize'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordSize()
    {
        return this._coordSize;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType getCoordSize() 

    /**
     * Returns the value of field 'coordValue'.
     * 
     * @return the value of field 'coordValue'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType getCoordValue()
    {
        return this._coordValue;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType getCoordValue() 

    /**
     * Returns the value of field 'pos_ang_unit'.
     * 
     * @return the value of field 'pos_ang_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType getPos_ang_unit()
    {
        return this._pos_ang_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType getPos_ang_unit() 

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
    public void setCoordError(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType coordError)
    {
        this._coordError = coordError;
    } //-- void setCoordError(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType) 

    /**
     * Sets the value of field 'coordPixsize'.
     * 
     * @param coordPixsize the value of field 'coordPixsize'.
     */
    public void setCoordPixsize(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType coordPixsize)
    {
        this._coordPixsize = coordPixsize;
    } //-- void setCoordPixsize(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType) 

    /**
     * Sets the value of field 'coordResolution'.
     * 
     * @param coordResolution the value of field 'coordResolution'.
     */
    public void setCoordResolution(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType coordResolution)
    {
        this._coordResolution = coordResolution;
    } //-- void setCoordResolution(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType) 

    /**
     * Sets the value of field 'coordSize'.
     * 
     * @param coordSize the value of field 'coordSize'.
     */
    public void setCoordSize(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType coordSize)
    {
        this._coordSize = coordSize;
    } //-- void setCoordSize(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType) 

    /**
     * Sets the value of field 'coordValue'.
     * 
     * @param coordValue the value of field 'coordValue'.
     */
    public void setCoordValue(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType coordValue)
    {
        this._coordValue = coordValue;
    } //-- void setCoordValue(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType) 

    /**
     * Sets the value of field 'pos_ang_unit'.
     * 
     * @param pos_ang_unit the value of field 'pos_ang_unit'.
     */
    public void setPos_ang_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType pos_ang_unit)
    {
        this._pos_ang_unit = pos_ang_unit;
    } //-- void setPos_ang_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType) 

    /**
     * Method unmarshalPos3VectorType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType unmarshalPos3VectorType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType unmarshalPos3VectorType(java.io.Reader) 

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
