/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Coord3ValueType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Coord3ValueType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Coord3ValueType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _pos1_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType _pos1_unit;

    /**
     * Field _pos2_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType _pos2_unit;

    /**
     * Field _pos3_unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType _pos3_unit;

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble _value;

    /**
     * Field _reference
     */
    private java.util.Vector _reference;


      //----------------/
     //- Constructors -/
    //----------------/

    public Coord3ValueType() {
        super();
        _reference = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addReference
     * 
     * @param vReference
     */
    public void addReference(java.lang.Object vReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _reference.addElement(vReference);
    } //-- void addReference(java.lang.Object) 

    /**
     * Method addReference
     * 
     * @param index
     * @param vReference
     */
    public void addReference(int index, java.lang.Object vReference)
        throws java.lang.IndexOutOfBoundsException
    {
        _reference.insertElementAt(vReference, index);
    } //-- void addReference(int, java.lang.Object) 

    /**
     * Method enumerateReference
     */
    public java.util.Enumeration enumerateReference()
    {
        return _reference.elements();
    } //-- java.util.Enumeration enumerateReference() 

    /**
     * Returns the value of field 'pos1_unit'.
     * 
     * @return the value of field 'pos1_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos1_unit()
    {
        return this._pos1_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos1_unit() 

    /**
     * Returns the value of field 'pos2_unit'.
     * 
     * @return the value of field 'pos2_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos2_unit()
    {
        return this._pos2_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos2_unit() 

    /**
     * Returns the value of field 'pos3_unit'.
     * 
     * @return the value of field 'pos3_unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos3_unit()
    {
        return this._pos3_unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType getPos3_unit() 

    /**
     * Method getReference
     * 
     * @param index
     */
    public java.lang.Object getReference(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _reference.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (java.lang.Object) _reference.elementAt(index);
    } //-- java.lang.Object getReference(int) 

    /**
     * Method getReference
     */
    public java.lang.Object[] getReference()
    {
        int size = _reference.size();
        java.lang.Object[] mArray = new java.lang.Object[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (java.lang.Object) _reference.elementAt(index);
        }
        return mArray;
    } //-- java.lang.Object[] getReference() 

    /**
     * Method getReferenceCount
     */
    public int getReferenceCount()
    {
        return _reference.size();
    } //-- int getReferenceCount() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getValue()
    {
        return this._value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getValue() 

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
     * Method removeAllReference
     */
    public void removeAllReference()
    {
        _reference.removeAllElements();
    } //-- void removeAllReference() 

    /**
     * Method removeReference
     * 
     * @param index
     */
    public java.lang.Object removeReference(int index)
    {
        java.lang.Object obj = _reference.elementAt(index);
        _reference.removeElementAt(index);
        return (java.lang.Object) obj;
    } //-- java.lang.Object removeReference(int) 

    /**
     * Sets the value of field 'pos1_unit'.
     * 
     * @param pos1_unit the value of field 'pos1_unit'.
     */
    public void setPos1_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType pos1_unit)
    {
        this._pos1_unit = pos1_unit;
    } //-- void setPos1_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType) 

    /**
     * Sets the value of field 'pos2_unit'.
     * 
     * @param pos2_unit the value of field 'pos2_unit'.
     */
    public void setPos2_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType pos2_unit)
    {
        this._pos2_unit = pos2_unit;
    } //-- void setPos2_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType) 

    /**
     * Sets the value of field 'pos3_unit'.
     * 
     * @param pos3_unit the value of field 'pos3_unit'.
     */
    public void setPos3_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType pos3_unit)
    {
        this._pos3_unit = pos3_unit;
    } //-- void setPos3_unit(org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType) 

    /**
     * Method setReference
     * 
     * @param index
     * @param vReference
     */
    public void setReference(int index, java.lang.Object vReference)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _reference.size())) {
            throw new IndexOutOfBoundsException();
        }
        _reference.setElementAt(vReference, index);
    } //-- void setReference(int, java.lang.Object) 

    /**
     * Method setReference
     * 
     * @param referenceArray
     */
    public void setReference(java.lang.Object[] referenceArray)
    {
        //-- copy array
        _reference.removeAllElements();
        for (int i = 0; i < referenceArray.length; i++) {
            _reference.addElement(referenceArray[i]);
        }
    } //-- void setReference(java.lang.Object) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble value)
    {
        this._value = value;
    } //-- void setValue(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble) 

    /**
     * Method unmarshalCoord3ValueType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType unmarshalCoord3ValueType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3ValueType unmarshalCoord3ValueType(java.io.Reader) 

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
