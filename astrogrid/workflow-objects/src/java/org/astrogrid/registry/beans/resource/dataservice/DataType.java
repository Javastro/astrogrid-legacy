/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataType.java,v 1.14 2007/01/04 16:26:07 clq2 Exp $
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
 * a (VOTable-supported) data type
 *  
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:07 $
 */
public class DataType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.String _content;

    /**
     * Field _arraysize
     */
    private java.lang.String _arraysize;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataType()


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
        
        if (obj instanceof DataType) {
        
            DataType temp = (DataType)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._arraysize != null) {
                if (temp._arraysize == null) return false;
                else if (!(this._arraysize.equals(temp._arraysize))) 
                    return false;
            }
            else if (temp._arraysize != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'arraysize'.
     * 
     * @return the value of field 'arraysize'.
     */
    public java.lang.String getArraysize()
    {
        return this._arraysize;
    } //-- java.lang.String getArraysize() 

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
     * Sets the value of field 'arraysize'.
     * 
     * @param arraysize the value of field 'arraysize'.
     */
    public void setArraysize(java.lang.String arraysize)
    {
        this._arraysize = arraysize;
    } //-- void setArraysize(java.lang.String) 

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
     * Method unmarshalDataType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.DataType unmarshalDataType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.DataType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.DataType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.DataType unmarshalDataType(java.io.Reader) 

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
