/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: AnyTEXT.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class AnyTEXT.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class AnyTEXT implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _anyObject
     */
    private java.util.Vector _anyObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public AnyTEXT() {
        super();
        setContent("");
        _anyObject = new Vector();
    } //-- org.astrogrid.registry.generated.package.AnyTEXT()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAnyObject
     * 
     * @param vAnyObject
     */
    public void addAnyObject(java.lang.Object vAnyObject)
        throws java.lang.IndexOutOfBoundsException
    {
        _anyObject.addElement(vAnyObject);
    } //-- void addAnyObject(java.lang.Object) 

    /**
     * Method addAnyObject
     * 
     * @param index
     * @param vAnyObject
     */
    public void addAnyObject(int index, java.lang.Object vAnyObject)
        throws java.lang.IndexOutOfBoundsException
    {
        _anyObject.insertElementAt(vAnyObject, index);
    } //-- void addAnyObject(int, java.lang.Object) 

    /**
     * Method enumerateAnyObject
     */
    public java.util.Enumeration enumerateAnyObject()
    {
        return _anyObject.elements();
    } //-- java.util.Enumeration enumerateAnyObject() 

    /**
     * Method getAnyObject
     * 
     * @param index
     */
    public java.lang.Object getAnyObject(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _anyObject.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (java.lang.Object) _anyObject.elementAt(index);
    } //-- java.lang.Object getAnyObject(int) 

    /**
     * Method getAnyObject
     */
    public java.lang.Object[] getAnyObject()
    {
        int size = _anyObject.size();
        java.lang.Object[] mArray = new java.lang.Object[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (java.lang.Object) _anyObject.elementAt(index);
        }
        return mArray;
    } //-- java.lang.Object[] getAnyObject() 

    /**
     * Method getAnyObjectCount
     */
    public int getAnyObjectCount()
    {
        return _anyObject.size();
    } //-- int getAnyObjectCount() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.String getContent()
    {
        return this._content;
    } //-- java.lang.String getContent() 

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
     * Method removeAllAnyObject
     */
    public void removeAllAnyObject()
    {
        _anyObject.removeAllElements();
    } //-- void removeAllAnyObject() 

    /**
     * Method removeAnyObject
     * 
     * @param index
     */
    public java.lang.Object removeAnyObject(int index)
    {
        java.lang.Object obj = _anyObject.elementAt(index);
        _anyObject.removeElementAt(index);
        return (java.lang.Object) obj;
    } //-- java.lang.Object removeAnyObject(int) 

    /**
     * Method setAnyObject
     * 
     * @param index
     * @param vAnyObject
     */
    public void setAnyObject(int index, java.lang.Object vAnyObject)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _anyObject.size())) {
            throw new IndexOutOfBoundsException();
        }
        _anyObject.setElementAt(vAnyObject, index);
    } //-- void setAnyObject(int, java.lang.Object) 

    /**
     * Method setAnyObject
     * 
     * @param anyObjectArray
     */
    public void setAnyObject(java.lang.Object[] anyObjectArray)
    {
        //-- copy array
        _anyObject.removeAllElements();
        for (int i = 0; i < anyObjectArray.length; i++) {
            _anyObject.addElement(anyObjectArray[i]);
        }
    } //-- void setAnyObject(java.lang.Object) 

    /**
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(java.lang.String content)
    {
        this._content = content;
    } //-- void setContent(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.AnyTEXT) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.AnyTEXT.class, reader);
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
