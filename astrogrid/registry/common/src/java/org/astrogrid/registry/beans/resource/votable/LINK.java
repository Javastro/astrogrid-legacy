/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: LINK.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class LINK.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class LINK extends org.astrogrid.common.bean.BaseBean 
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
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _contentRole
     */
    private org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType _contentRole;

    /**
     * Field _contentType
     */
    private java.lang.String _contentType;

    /**
     * Field _title
     */
    private java.lang.String _title;

    /**
     * Field _value
     */
    private java.lang.String _value;

    /**
     * Field _href
     */
    private java.lang.String _href;

    /**
     * Field _gref
     */
    private java.lang.String _gref;

    /**
     * Field _action
     */
    private java.lang.String _action;


      //----------------/
     //- Constructors -/
    //----------------/

    public LINK() {
        super();
        setContent("");
    } //-- org.astrogrid.registry.beans.resource.votable.LINK()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'action'.
     * 
     * @return the value of field 'action'.
     */
    public java.lang.String getAction()
    {
        return this._action;
    } //-- java.lang.String getAction() 

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
     * Returns the value of field 'contentRole'.
     * 
     * @return the value of field 'contentRole'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType getContentRole()
    {
        return this._contentRole;
    } //-- org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType getContentRole() 

    /**
     * Returns the value of field 'contentType'.
     * 
     * @return the value of field 'contentType'.
     */
    public java.lang.String getContentType()
    {
        return this._contentType;
    } //-- java.lang.String getContentType() 

    /**
     * Returns the value of field 'gref'.
     * 
     * @return the value of field 'gref'.
     */
    public java.lang.String getGref()
    {
        return this._gref;
    } //-- java.lang.String getGref() 

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
     * Returns the value of field 'ID'.
     * 
     * @return the value of field 'ID'.
     */
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'title'.
     * 
     * @return the value of field 'title'.
     */
    public java.lang.String getTitle()
    {
        return this._title;
    } //-- java.lang.String getTitle() 

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
     * Sets the value of field 'action'.
     * 
     * @param action the value of field 'action'.
     */
    public void setAction(java.lang.String action)
    {
        this._action = action;
    } //-- void setAction(java.lang.String) 

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
     * Sets the value of field 'contentRole'.
     * 
     * @param contentRole the value of field 'contentRole'.
     */
    public void setContentRole(org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType contentRole)
    {
        this._contentRole = contentRole;
    } //-- void setContentRole(org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType) 

    /**
     * Sets the value of field 'contentType'.
     * 
     * @param contentType the value of field 'contentType'.
     */
    public void setContentType(java.lang.String contentType)
    {
        this._contentType = contentType;
    } //-- void setContentType(java.lang.String) 

    /**
     * Sets the value of field 'gref'.
     * 
     * @param gref the value of field 'gref'.
     */
    public void setGref(java.lang.String gref)
    {
        this._gref = gref;
    } //-- void setGref(java.lang.String) 

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
     * Sets the value of field 'ID'.
     * 
     * @param ID the value of field 'ID'.
     */
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'title'.
     * 
     * @param title the value of field 'title'.
     */
    public void setTitle(java.lang.String title)
    {
        this._title = title;
    } //-- void setTitle(java.lang.String) 

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
     * Method unmarshalLINK
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.LINK unmarshalLINK(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.LINK) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.LINK.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.LINK unmarshalLINK(java.io.Reader) 

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
