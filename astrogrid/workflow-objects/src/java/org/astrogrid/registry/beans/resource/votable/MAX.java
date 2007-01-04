/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MAX.java,v 1.14 2007/01/04 16:26:13 clq2 Exp $
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
 * Class MAX.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:13 $
 */
public class MAX extends org.astrogrid.common.bean.BaseBean 
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

    public MAX() {
        super();
        setContent("");
        setInclusive(org.astrogrid.registry.beans.resource.votable.types.Yesno.valueOf("yes"));
    } //-- org.astrogrid.registry.beans.resource.votable.MAX()


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
        
        if (obj instanceof MAX) {
        
            MAX temp = (MAX)obj;
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
     * Method unmarshalMAX
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.MAX unmarshalMAX(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.MAX) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.MAX.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.MAX unmarshalMAX(java.io.Reader) 

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
