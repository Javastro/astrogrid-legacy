/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: STREAM.java,v 1.4 2004/03/09 09:45:23 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Date;
import org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType;
import org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType;
import org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class STREAM.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:23 $
 */
public class STREAM extends org.astrogrid.common.bean.BaseBean 
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
     * Field _type
     */
    private org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType _type = org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType.valueOf("locator");

    /**
     * Field _href
     */
    private java.lang.String _href;

    /**
     * Field _actuate
     */
    private org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType _actuate = org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType.valueOf("onRequest");

    /**
     * Field _encoding
     */
    private org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType _encoding = org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType.valueOf("none");

    /**
     * Field _expires
     */
    private java.util.Date _expires;

    /**
     * Field _rights
     */
    private java.lang.String _rights;


      //----------------/
     //- Constructors -/
    //----------------/

    public STREAM() {
        super();
        setContent("");
        setType(org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType.valueOf("locator"));
        setActuate(org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType.valueOf("onRequest"));
        setEncoding(org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType.valueOf("none"));
    } //-- org.astrogrid.registry.beans.resource.votable.STREAM()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'actuate'.
     * 
     * @return the value of field 'actuate'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType getActuate()
    {
        return this._actuate;
    } //-- org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType getActuate() 

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
     * Returns the value of field 'encoding'.
     * 
     * @return the value of field 'encoding'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType getEncoding()
    {
        return this._encoding;
    } //-- org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType getEncoding() 

    /**
     * Returns the value of field 'expires'.
     * 
     * @return the value of field 'expires'.
     */
    public java.util.Date getExpires()
    {
        return this._expires;
    } //-- java.util.Date getExpires() 

    /**
     * Returns the value of field 'href'.
     * 
     * @return the value of field 'href'.
     */
    public java.lang.String getHref()
    {
        return this._href;
    } //-- java.lang.String getHref() 

    /**
     * Returns the value of field 'rights'.
     * 
     * @return the value of field 'rights'.
     */
    public java.lang.String getRights()
    {
        return this._rights;
    } //-- java.lang.String getRights() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType getType() 

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
     * Sets the value of field 'actuate'.
     * 
     * @param actuate the value of field 'actuate'.
     */
    public void setActuate(org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType actuate)
    {
        this._actuate = actuate;
    } //-- void setActuate(org.astrogrid.registry.beans.resource.votable.types.STREAMActuateType) 

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
     * Sets the value of field 'encoding'.
     * 
     * @param encoding the value of field 'encoding'.
     */
    public void setEncoding(org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType encoding)
    {
        this._encoding = encoding;
    } //-- void setEncoding(org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType) 

    /**
     * Sets the value of field 'expires'.
     * 
     * @param expires the value of field 'expires'.
     */
    public void setExpires(java.util.Date expires)
    {
        this._expires = expires;
    } //-- void setExpires(java.util.Date) 

    /**
     * Sets the value of field 'href'.
     * 
     * @param href the value of field 'href'.
     */
    public void setHref(java.lang.String href)
    {
        this._href = href;
    } //-- void setHref(java.lang.String) 

    /**
     * Sets the value of field 'rights'.
     * 
     * @param rights the value of field 'rights'.
     */
    public void setRights(java.lang.String rights)
    {
        this._rights = rights;
    } //-- void setRights(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.beans.resource.votable.types.STREAMTypeType) 

    /**
     * Method unmarshalSTREAM
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.STREAM unmarshalSTREAM(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.STREAM) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.STREAM.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.STREAM unmarshalSTREAM(java.io.Reader) 

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
