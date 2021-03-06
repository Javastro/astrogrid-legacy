/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationDefinition.java,v 1.2 2007/01/04 16:26:31 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.cea;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.InterfacesType;
import org.astrogrid.applications.beans.v1.types.ApplicationKindType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ApplicationDefinition.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:31 $
 */
public class ApplicationDefinition extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationKind
     */
    private org.astrogrid.applications.beans.v1.types.ApplicationKindType _applicationKind;

    /**
     * Field _parameters
     */
    private org.astrogrid.registry.beans.v10.cea.Parameters _parameters;

    /**
     * Field _interfaces
     */
    private org.astrogrid.applications.beans.v1.InterfacesType _interfaces;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationDefinition() {
        super();
    } //-- org.astrogrid.registry.beans.v10.cea.ApplicationDefinition()


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
        
        if (obj instanceof ApplicationDefinition) {
        
            ApplicationDefinition temp = (ApplicationDefinition)obj;
            if (this._applicationKind != null) {
                if (temp._applicationKind == null) return false;
                else if (!(this._applicationKind.equals(temp._applicationKind))) 
                    return false;
            }
            else if (temp._applicationKind != null)
                return false;
            if (this._parameters != null) {
                if (temp._parameters == null) return false;
                else if (!(this._parameters.equals(temp._parameters))) 
                    return false;
            }
            else if (temp._parameters != null)
                return false;
            if (this._interfaces != null) {
                if (temp._interfaces == null) return false;
                else if (!(this._interfaces.equals(temp._interfaces))) 
                    return false;
            }
            else if (temp._interfaces != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'applicationKind'.
     * 
     * @return the value of field 'applicationKind'.
     */
    public org.astrogrid.applications.beans.v1.types.ApplicationKindType getApplicationKind()
    {
        return this._applicationKind;
    } //-- org.astrogrid.applications.beans.v1.types.ApplicationKindType getApplicationKind() 

    /**
     * Returns the value of field 'interfaces'.
     * 
     * @return the value of field 'interfaces'.
     */
    public org.astrogrid.applications.beans.v1.InterfacesType getInterfaces()
    {
        return this._interfaces;
    } //-- org.astrogrid.applications.beans.v1.InterfacesType getInterfaces() 

    /**
     * Returns the value of field 'parameters'.
     * 
     * @return the value of field 'parameters'.
     */
    public org.astrogrid.registry.beans.v10.cea.Parameters getParameters()
    {
        return this._parameters;
    } //-- org.astrogrid.registry.beans.v10.cea.Parameters getParameters() 

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
     * Sets the value of field 'applicationKind'.
     * 
     * @param applicationKind the value of field 'applicationKind'.
     */
    public void setApplicationKind(org.astrogrid.applications.beans.v1.types.ApplicationKindType applicationKind)
    {
        this._applicationKind = applicationKind;
    } //-- void setApplicationKind(org.astrogrid.applications.beans.v1.types.ApplicationKindType) 

    /**
     * Sets the value of field 'interfaces'.
     * 
     * @param interfaces the value of field 'interfaces'.
     */
    public void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType interfaces)
    {
        this._interfaces = interfaces;
    } //-- void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType) 

    /**
     * Sets the value of field 'parameters'.
     * 
     * @param parameters the value of field 'parameters'.
     */
    public void setParameters(org.astrogrid.registry.beans.v10.cea.Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(org.astrogrid.registry.beans.v10.cea.Parameters) 

    /**
     * Method unmarshalApplicationDefinition
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.cea.ApplicationDefinition unmarshalApplicationDefinition(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.cea.ApplicationDefinition) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.cea.ApplicationDefinition.class, reader);
    } //-- org.astrogrid.registry.beans.v10.cea.ApplicationDefinition unmarshalApplicationDefinition(java.io.Reader) 

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
