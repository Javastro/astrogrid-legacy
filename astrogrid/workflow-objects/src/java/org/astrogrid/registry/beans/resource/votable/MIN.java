/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MIN.java,v 1.12 2005/04/25 12:09:28 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.votable.types.Yesno;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class MIN.
 * 
 * @version $Revision: 1.12 $ $Date: 2005/04/25 12:09:28 $
 */
public class MIN extends org.astrogrid.common.bean.BaseBean 
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
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _inclusive
     */
    private org.astrogrid.registry.beans.resource.votable.types.Yesno _inclusive = org.astrogrid.registry.beans.resource.votable.types.Yesno.valueOf("yes");


      //----------------/
     //- Constructors -/
    //----------------/

    public MIN() {
        super();
        setContent("");
        setInclusive(org.astrogrid.registry.beans.resource.votable.types.Yesno.valueOf("yes"));
    } //-- org.astrogrid.registry.beans.resource.votable.MIN()


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
        
        if (obj instanceof MIN) {
        
            MIN temp = (MIN)obj;
            if (this._content != null) {
                if (temp._content == null) return false;
                else if (!(this._content.equals(temp._content))) 
                    return false;
            }
            else if (temp._content != null)
                return false;
            if (this._value != null) {
                if (temp._value == null) return false;
                else if (!(this._value.equals(temp._value))) 
                    return false;
            }
            else if (temp._value != null)
                return false;
            if (this._inclusive != null) {
                if (temp._inclusive == null) return false;
                else if (!(this._inclusive.equals(temp._inclusive))) 
                    return false;
            }
            else if (temp._inclusive != null)
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
     * Returns the value of field 'inclusive'.
     * 
     * @return the value of field 'inclusive'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.Yesno getInclusive()
    {
        return this._inclusive;
    } //-- org.astrogrid.registry.beans.resource.votable.types.Yesno getInclusive() 

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
     * Sets the value of field 'inclusive'.
     * 
     * @param inclusive the value of field 'inclusive'.
     */
    public void setInclusive(org.astrogrid.registry.beans.resource.votable.types.Yesno inclusive)
    {
        this._inclusive = inclusive;
    } //-- void setInclusive(org.astrogrid.registry.beans.resource.votable.types.Yesno) 

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
     * Method unmarshalMIN
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.MIN unmarshalMIN(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.MIN) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.MIN.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.MIN unmarshalMIN(java.io.Reader) 

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
