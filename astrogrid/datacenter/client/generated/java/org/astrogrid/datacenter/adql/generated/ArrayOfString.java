/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArrayOfString.java,v 1.8 2003/11/26 17:01:37 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class ArrayOfString.
 * 
 * @version $Revision: 1.8 $ $Date: 2003/11/26 17:01:37 $
 */
public class ArrayOfString extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _stringList
     */
    private java.util.Vector _stringList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayOfString() {
        super();
        _stringList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfString()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addString
     * 
     * @param vString
     */
    public void addString(java.lang.String vString)
        throws java.lang.IndexOutOfBoundsException
    {
        _stringList.addElement(vString);
    } //-- void addString(java.lang.String) 

    /**
     * Method addString
     * 
     * @param index
     * @param vString
     */
    public void addString(int index, java.lang.String vString)
        throws java.lang.IndexOutOfBoundsException
    {
        _stringList.insertElementAt(vString, index);
    } //-- void addString(int, java.lang.String) 

    /**
     * Method enumerateString
     */
    public java.util.Enumeration enumerateString()
    {
        return _stringList.elements();
    } //-- java.util.Enumeration enumerateString() 

    /**
     * Method getString
     * 
     * @param index
     */
    public java.lang.String getString(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stringList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_stringList.elementAt(index);
    } //-- java.lang.String getString(int) 

    /**
     * Method getString
     */
    public java.lang.String[] getString()
    {
        int size = _stringList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_stringList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getString() 

    /**
     * Method getStringCount
     */
    public int getStringCount()
    {
        return _stringList.size();
    } //-- int getStringCount() 

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
     * Method removeAllString
     */
    public void removeAllString()
    {
        _stringList.removeAllElements();
    } //-- void removeAllString() 

    /**
     * Method removeString
     * 
     * @param index
     */
    public java.lang.String removeString(int index)
    {
        java.lang.Object obj = _stringList.elementAt(index);
        _stringList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeString(int) 

    /**
     * Method setString
     * 
     * @param index
     * @param vString
     */
    public void setString(int index, java.lang.String vString)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _stringList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _stringList.setElementAt(vString, index);
    } //-- void setString(int, java.lang.String) 

    /**
     * Method setString
     * 
     * @param stringArray
     */
    public void setString(java.lang.String[] stringArray)
    {
        //-- copy array
        _stringList.removeAllElements();
        for (int i = 0; i < stringArray.length; i++) {
            _stringList.addElement(stringArray[i]);
        }
    } //-- void setString(java.lang.String) 

    /**
     * Method unmarshalArrayOfString
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ArrayOfString unmarshalArrayOfString(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ArrayOfString) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ArrayOfString.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfString unmarshalArrayOfString(java.io.Reader) 

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
