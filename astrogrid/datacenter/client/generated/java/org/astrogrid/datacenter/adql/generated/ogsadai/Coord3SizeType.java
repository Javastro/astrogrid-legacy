/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Coord3SizeType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class Coord3SizeType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Coord3SizeType extends org.astrogrid.datacenter.adql.AbstractQOM 
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
     * Field _posAngleRef
     */
    private java.util.Vector _posAngleRef;

    /**
     * Field _value
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble _value;

    /**
     * Field _matrix
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble _matrix;

    /**
     * Field _reference
     */
    private java.util.Vector _reference;

    /**
     * Field _posAngle
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble _posAngle;


      //----------------/
     //- Constructors -/
    //----------------/

    public Coord3SizeType() {
        super();
        _posAngleRef = new Vector();
        _reference = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addPosAngleRef
     * 
     * @param vPosAngleRef
     */
    public void addPosAngleRef(java.lang.Object vPosAngleRef)
        throws java.lang.IndexOutOfBoundsException
    {
        _posAngleRef.addElement(vPosAngleRef);
    } //-- void addPosAngleRef(java.lang.Object) 

    /**
     * Method addPosAngleRef
     * 
     * @param index
     * @param vPosAngleRef
     */
    public void addPosAngleRef(int index, java.lang.Object vPosAngleRef)
        throws java.lang.IndexOutOfBoundsException
    {
        _posAngleRef.insertElementAt(vPosAngleRef, index);
    } //-- void addPosAngleRef(int, java.lang.Object) 

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
     * Method enumeratePosAngleRef
     */
    public java.util.Enumeration enumeratePosAngleRef()
    {
        return _posAngleRef.elements();
    } //-- java.util.Enumeration enumeratePosAngleRef() 

    /**
     * Method enumerateReference
     */
    public java.util.Enumeration enumerateReference()
    {
        return _reference.elements();
    } //-- java.util.Enumeration enumerateReference() 

    /**
     * Returns the value of field 'matrix'.
     * 
     * @return the value of field 'matrix'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getMatrix()
    {
        return this._matrix;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getMatrix() 

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
     * Returns the value of field 'posAngle'.
     * 
     * @return the value of field 'posAngle'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getPosAngle()
    {
        return this._posAngle;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble getPosAngle() 

    /**
     * Method getPosAngleRef
     * 
     * @param index
     */
    public java.lang.Object getPosAngleRef(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _posAngleRef.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (java.lang.Object) _posAngleRef.elementAt(index);
    } //-- java.lang.Object getPosAngleRef(int) 

    /**
     * Method getPosAngleRef
     */
    public java.lang.Object[] getPosAngleRef()
    {
        int size = _posAngleRef.size();
        java.lang.Object[] mArray = new java.lang.Object[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (java.lang.Object) _posAngleRef.elementAt(index);
        }
        return mArray;
    } //-- java.lang.Object[] getPosAngleRef() 

    /**
     * Method getPosAngleRefCount
     */
    public int getPosAngleRefCount()
    {
        return _posAngleRef.size();
    } //-- int getPosAngleRefCount() 

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
     * Method removeAllPosAngleRef
     */
    public void removeAllPosAngleRef()
    {
        _posAngleRef.removeAllElements();
    } //-- void removeAllPosAngleRef() 

    /**
     * Method removeAllReference
     */
    public void removeAllReference()
    {
        _reference.removeAllElements();
    } //-- void removeAllReference() 

    /**
     * Method removePosAngleRef
     * 
     * @param index
     */
    public java.lang.Object removePosAngleRef(int index)
    {
        java.lang.Object obj = _posAngleRef.elementAt(index);
        _posAngleRef.removeElementAt(index);
        return (java.lang.Object) obj;
    } //-- java.lang.Object removePosAngleRef(int) 

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
     * Sets the value of field 'matrix'.
     * 
     * @param matrix the value of field 'matrix'.
     */
    public void setMatrix(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble matrix)
    {
        this._matrix = matrix;
    } //-- void setMatrix(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble) 

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
     * Sets the value of field 'posAngle'.
     * 
     * @param posAngle the value of field 'posAngle'.
     */
    public void setPosAngle(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble posAngle)
    {
        this._posAngle = posAngle;
    } //-- void setPosAngle(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfDouble) 

    /**
     * Method setPosAngleRef
     * 
     * @param index
     * @param vPosAngleRef
     */
    public void setPosAngleRef(int index, java.lang.Object vPosAngleRef)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _posAngleRef.size())) {
            throw new IndexOutOfBoundsException();
        }
        _posAngleRef.setElementAt(vPosAngleRef, index);
    } //-- void setPosAngleRef(int, java.lang.Object) 

    /**
     * Method setPosAngleRef
     * 
     * @param posAngleRefArray
     */
    public void setPosAngleRef(java.lang.Object[] posAngleRefArray)
    {
        //-- copy array
        _posAngleRef.removeAllElements();
        for (int i = 0; i < posAngleRefArray.length; i++) {
            _posAngleRef.addElement(posAngleRefArray[i]);
        }
    } //-- void setPosAngleRef(java.lang.Object) 

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
     * Method unmarshalCoord3SizeType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType unmarshalCoord3SizeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Coord3SizeType unmarshalCoord3SizeType(java.io.Reader) 

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
