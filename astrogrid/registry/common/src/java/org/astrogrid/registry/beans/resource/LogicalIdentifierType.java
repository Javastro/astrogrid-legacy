/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: LogicalIdentifierType.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
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
 * Class LogicalIdentifierType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class LogicalIdentifierType extends org.astrogrid.registry.beans.resource.IdentifierType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a name refering to the characteristic that relates resources
     *  having this logical identifier.
     *  
     */
    private java.lang.String _role;


      //----------------/
     //- Constructors -/
    //----------------/

    public LogicalIdentifierType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.LogicalIdentifierType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'role'. The field 'role' has the
     * following description: a name refering to the characteristic
     * that relates resources
     *  having this logical identifier.
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
     * Sets the value of field 'role'. The field 'role' has the
     * following description: a name refering to the characteristic
     * that relates resources
     *  having this logical identifier.
     *  
     * 
     * @param role the value of field 'role'.
     */
    public void setRole(java.lang.String role)
    {
        this._role = role;
    } //-- void setRole(java.lang.String) 

    /**
     * Method unmarshalLogicalIdentifierType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.LogicalIdentifierType unmarshalLogicalIdentifierType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.LogicalIdentifierType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.LogicalIdentifierType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.LogicalIdentifierType unmarshalLogicalIdentifierType(java.io.Reader) 

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
