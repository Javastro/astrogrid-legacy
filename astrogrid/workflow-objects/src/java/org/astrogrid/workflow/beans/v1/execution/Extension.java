/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Extension.java,v 1.13 2007/01/04 16:26:08 clq2 Exp $
 */

package org.astrogrid.workflow.beans.v1.execution;

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
 * A string 'buffer' for holding further information, keyed by
 * attribute - so execution record becomes a map.
 * 
 * @version $Revision: 1.13 $ $Date: 2007/01/04 16:26:08 $
 */
public class Extension extends org.astrogrid.common.bean.BaseBean 
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
     * key to be used to retreive this extension buffer
     */
    private java.lang.String _key;


      //----------------/
     //- Constructors -/
    //----------------/

    public Extension() {
        super();
        setContent("");
    } //-- org.astrogrid.workflow.beans.v1.execution.Extension()


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
        
        if (obj instanceof Extension) {
        
            Extension temp = (Extension)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._key != null) {
                if (temp._key == null) return false;
                else if (!(this._key.equals(temp._key))) 
                    return false;
            }
            else if (temp._key != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Returns the value of field 'key'. The field 'key' has the
     * following description: key to be used to retreive this
     * extension buffer
     * 
     * @return the value of field 'key'.
     */
    public java.lang.String getKey()
    {
        return this._key;
    } //-- java.lang.String getKey() 

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
     * Sets the value of field 'key'. The field 'key' has the
     * following description: key to be used to retreive this
     * extension buffer
     * 
     * @param key the value of field 'key'.
     */
    public void setKey(java.lang.String key)
    {
        this._key = key;
    } //-- void setKey(java.lang.String) 

    /**
     * Method unmarshalExtension
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.execution.Extension unmarshalExtension(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.execution.Extension) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.execution.Extension.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.execution.Extension unmarshalExtension(java.io.Reader) 

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
