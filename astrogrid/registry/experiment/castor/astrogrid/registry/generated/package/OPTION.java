/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: OPTION.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class OPTION.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class OPTION implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _OPTIONList
     */
    private java.util.Vector _OPTIONList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OPTION() {
        super();
        _OPTIONList = new Vector();
    } //-- org.astrogrid.registry.generated.package.OPTION()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addOPTION
     * 
     * @param vOPTION
     */
    public void addOPTION(org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.addElement(vOPTION);
    } //-- void addOPTION(org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method addOPTION
     * 
     * @param index
     * @param vOPTION
     */
    public void addOPTION(int index, org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        _OPTIONList.insertElementAt(vOPTION, index);
    } //-- void addOPTION(int, org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method enumerateOPTION
     */
    public java.util.Enumeration enumerateOPTION()
    {
        return _OPTIONList.elements();
    } //-- java.util.Enumeration enumerateOPTION() 

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
     * Method getOPTION
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.OPTION getOPTION(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.OPTION) _OPTIONList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.OPTION getOPTION(int) 

    /**
     * Method getOPTION
     */
    public org.astrogrid.registry.generated.package.OPTION[] getOPTION()
    {
        int size = _OPTIONList.size();
        org.astrogrid.registry.generated.package.OPTION[] mArray = new org.astrogrid.registry.generated.package.OPTION[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.OPTION) _OPTIONList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.OPTION[] getOPTION() 

    /**
     * Method getOPTIONCount
     */
    public int getOPTIONCount()
    {
        return _OPTIONList.size();
    } //-- int getOPTIONCount() 

    /**
     * Returns the value of field 'value'.
     * 
     * @return the value of field 'value'.
     */
    public java.lang.String getValue()
    {
        return this._value;
    } //-- java.lang.String getValue() 

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
     * Method removeAllOPTION
     */
    public void removeAllOPTION()
    {
        _OPTIONList.removeAllElements();
    } //-- void removeAllOPTION() 

    /**
     * Method removeOPTION
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.OPTION removeOPTION(int index)
    {
        java.lang.Object obj = _OPTIONList.elementAt(index);
        _OPTIONList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.OPTION) obj;
    } //-- org.astrogrid.registry.generated.package.OPTION removeOPTION(int) 

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
     * Method setOPTION
     * 
     * @param index
     * @param vOPTION
     */
    public void setOPTION(int index, org.astrogrid.registry.generated.package.OPTION vOPTION)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _OPTIONList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _OPTIONList.setElementAt(vOPTION, index);
    } //-- void setOPTION(int, org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Method setOPTION
     * 
     * @param OPTIONArray
     */
    public void setOPTION(org.astrogrid.registry.generated.package.OPTION[] OPTIONArray)
    {
        //-- copy array
        _OPTIONList.removeAllElements();
        for (int i = 0; i < OPTIONArray.length; i++) {
            _OPTIONList.addElement(OPTIONArray[i]);
        }
    } //-- void setOPTION(org.astrogrid.registry.generated.package.OPTION) 

    /**
     * Sets the value of field 'value'.
     * 
     * @param value the value of field 'value'.
     */
    public void setValue(java.lang.String value)
    {
        this._value = value;
    } //-- void setValue(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.OPTION) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.OPTION.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
