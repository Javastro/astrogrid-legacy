/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DateType.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource;

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
 * Class DateType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class DateType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private org.exolab.castor.types.Date _content;

    /**
     * A string indicating what the date refers to. 
     *  
     */
    private java.lang.String _role = "representative";


      //----------------/
     //- Constructors -/
    //----------------/

    public DateType() {
        super();
        setRole("representative");
    } //-- org.astrogrid.registry.beans.resource.DateType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public org.exolab.castor.types.Date getContent()
    {
        return this._content;
    } //-- org.exolab.castor.types.Date getContent() 

    /**
     * Returns the value of field 'role'. The field 'role' has the
     * following description: A string indicating what the date
     * refers to. 
     *  
     * 
     * @return the value of field 'role'.
     */
    public java.lang.String getRole()
    {
        return this._role;
    } //-- java.lang.String getRole() 

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
    public void setContent(org.exolab.castor.types.Date content)
    {
        this._content = content;
    } //-- void setContent(org.exolab.castor.types.Date) 

    /**
     * Sets the value of field 'role'. The field 'role' has the
     * following description: A string indicating what the date
     * refers to. 
     *  
     * 
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role)
    {
        this._role = role;
    } //-- void setRole(java.lang.String) 

    /**
     * Method unmarshalDateType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.DateType unmarshalDateType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.DateType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.DateType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.DateType unmarshalDateType(java.io.Reader) 

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
