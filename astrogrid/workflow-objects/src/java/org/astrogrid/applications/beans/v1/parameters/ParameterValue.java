/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterValue.java,v 1.5 2004/03/03 01:16:54 nw Exp $
 */

package org.astrogrid.applications.beans.v1.parameters;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Defines what a parameterValue can contain - for the instance
 * document
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/03 01:16:54 $
 */
public class ParameterValue extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _type
     */
    private org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes _type;

    /**
     * internal content storage
     */
    private java.lang.String _content = "";

    /**
     * Field _anyObject
     */
    private java.lang.Object _anyObject;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParameterValue() {
        super();
        setContent("");
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'anyObject'.
     * 
     * @return the value of field 'anyObject'.
     */
    public java.lang.Object getAnyObject()
    {
        return this._anyObject;
    } //-- java.lang.Object getAnyObject() 

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
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes getType()
    {
        return this._type;
    } //-- org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes getType() 

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
     * Sets the value of field 'anyObject'.
     * 
     * @param anyObject the value of field 'anyObject'.
     */
    public void setAnyObject(java.lang.Object anyObject)
    {
        this._anyObject = anyObject;
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
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes) 

    /**
     * Method unmarshalParameterValue
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.parameters.ParameterValue unmarshalParameterValue(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.parameters.ParameterValue) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.parameters.ParameterValue.class, reader);
    } //-- org.astrogrid.applications.beans.v1.parameters.ParameterValue unmarshalParameterValue(java.io.Reader) 

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
