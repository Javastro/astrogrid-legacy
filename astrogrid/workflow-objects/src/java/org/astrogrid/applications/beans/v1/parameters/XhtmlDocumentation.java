/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: XhtmlDocumentation.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.applications.beans.v1.parameters;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * should really reference the XHTML shema, but just did this for
 * convienience
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class XhtmlDocumentation extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


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
    private java.util.ArrayList _anyObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public XhtmlDocumentation() {
        super();
        setContent("");
        _anyObject = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation()


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
        _anyObject.add(vAnyObject);
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
        _anyObject.add(index, vAnyObject);
    } //-- void addAnyObject(int, java.lang.Object) 

    /**
     * Method clearAnyObject
     */
    public void clearAnyObject()
    {
        _anyObject.clear();
    } //-- void clearAnyObject() 

    /**
     * Method enumerateAnyObject
     */
    public java.util.Enumeration enumerateAnyObject()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_anyObject.iterator());
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
        
        return (java.lang.Object) _anyObject.get(index);
    } //-- java.lang.Object getAnyObject(int) 

    /**
     * Method getAnyObject
     */
    public java.lang.Object[] getAnyObject()
    {
        int size = _anyObject.size();
        java.lang.Object[] mArray = new java.lang.Object[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (java.lang.Object) _anyObject.get(index);
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
     * Method removeAnyObject
     * 
     * @param vAnyObject
     */
    public boolean removeAnyObject(java.lang.Object vAnyObject)
    {
        boolean removed = _anyObject.remove(vAnyObject);
        return removed;
    } //-- boolean removeAnyObject(java.lang.Object) 

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
        _anyObject.set(index, vAnyObject);
    } //-- void setAnyObject(int, java.lang.Object) 

    /**
     * Method setAnyObject
     * 
     * @param anyObjectArray
     */
    public void setAnyObject(java.lang.Object[] anyObjectArray)
    {
        //-- copy array
        _anyObject.clear();
        for (int i = 0; i < anyObjectArray.length; i++) {
            _anyObject.add(anyObjectArray[i]);
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
     * Method unmarshalXhtmlDocumentation
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation unmarshalXhtmlDocumentation(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation.class, reader);
    } //-- org.astrogrid.applications.beans.v1.parameters.XhtmlDocumentation unmarshalXhtmlDocumentation(java.io.Reader) 

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
