/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationBase.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.types.ApplicationKindType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Base Application Description
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class ApplicationBase extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * This is the name of an application - it consists of the
     * authorityId and the resourceID. e.g. authority.id/name -
     * This should probably really be the ivorn proper, but all the
     * implementations at the moment leave the ivo:// part off the
     * internal name of the application
     */
    private java.lang.String _name;

    /**
     * A Java class that implements
     * org.astrogrid.applications.Application
     */
    private java.lang.String _instanceClass;

    /**
     * The CEA type of the application - this allows the system to
     * determine which CEC it should be talking to.
     */
    private org.astrogrid.applications.beans.v1.types.ApplicationKindType _applicationType;

    /**
     * The complete list of parameters that might occur in any of
     * the apllication interfaces
     */
    private org.astrogrid.applications.beans.v1.Parameters _parameters;

    /**
     * The list of interfaces that an application might have
     */
    private org.astrogrid.applications.beans.v1.InterfacesType _interfaces;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationBase() {
        super();
    } //-- org.astrogrid.applications.beans.v1.ApplicationBase()


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
        
        if (obj instanceof ApplicationBase) {
        
            ApplicationBase temp = (ApplicationBase)obj;
            if (this._name != null) {
                if (temp._name == null) return false;
                else if (!(this._name.equals(temp._name))) 
                    return false;
            }
            else if (temp._name != null)
                return false;
            if (this._instanceClass != null) {
                if (temp._instanceClass == null) return false;
                else if (!(this._instanceClass.equals(temp._instanceClass))) 
                    return false;
            }
            else if (temp._instanceClass != null)
                return false;
            if (this._applicationType != null) {
                if (temp._applicationType == null) return false;
                else if (!(this._applicationType.equals(temp._applicationType))) 
                    return false;
            }
            else if (temp._applicationType != null)
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
     * Returns the value of field 'applicationType'. The field
     * 'applicationType' has the following description: The CEA
     * type of the application - this allows the system to
     * determine which CEC it should be talking to.
     * 
     * @return the value of field 'applicationType'.
     */
    public org.astrogrid.applications.beans.v1.types.ApplicationKindType getApplicationType()
    {
        return this._applicationType;
    } //-- org.astrogrid.applications.beans.v1.types.ApplicationKindType getApplicationType() 

    /**
     * Returns the value of field 'instanceClass'. The field
     * 'instanceClass' has the following description: A Java class
     * that implements org.astrogrid.applications.Application
     * 
     * @return the value of field 'instanceClass'.
     */
    public java.lang.String getInstanceClass()
    {
        return this._instanceClass;
    } //-- java.lang.String getInstanceClass() 

    /**
     * Returns the value of field 'interfaces'. The field
     * 'interfaces' has the following description: The list of
     * interfaces that an application might have
     * 
     * @return the value of field 'interfaces'.
     */
    public org.astrogrid.applications.beans.v1.InterfacesType getInterfaces()
    {
        return this._interfaces;
    } //-- org.astrogrid.applications.beans.v1.InterfacesType getInterfaces() 

    /**
     * Returns the value of field 'name'. The field 'name' has the
     * following description: This is the name of an application -
     * it consists of the authorityId and the resourceID. e.g.
     * authority.id/name - This should probably really be the ivorn
     * proper, but all the implementations at the moment leave the
     * ivo:// part off the internal name of the application
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Returns the value of field 'parameters'. The field
     * 'parameters' has the following description: The complete
     * list of parameters that might occur in any of the
     * apllication interfaces
     * 
     * @return the value of field 'parameters'.
     */
    public org.astrogrid.applications.beans.v1.Parameters getParameters()
    {
        return this._parameters;
    } //-- org.astrogrid.applications.beans.v1.Parameters getParameters() 

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
     * Sets the value of field 'applicationType'. The field
     * 'applicationType' has the following description: The CEA
     * type of the application - this allows the system to
     * determine which CEC it should be talking to.
     * 
     * @param applicationType the value of field 'applicationType'.
     */
    public void setApplicationType(org.astrogrid.applications.beans.v1.types.ApplicationKindType applicationType)
    {
        this._applicationType = applicationType;
    } //-- void setApplicationType(org.astrogrid.applications.beans.v1.types.ApplicationKindType) 

    /**
     * Sets the value of field 'instanceClass'. The field
     * 'instanceClass' has the following description: A Java class
     * that implements org.astrogrid.applications.Application
     * 
     * @param instanceClass the value of field 'instanceClass'.
     */
    public void setInstanceClass(java.lang.String instanceClass)
    {
        this._instanceClass = instanceClass;
    } //-- void setInstanceClass(java.lang.String) 

    /**
     * Sets the value of field 'interfaces'. The field 'interfaces'
     * has the following description: The list of interfaces that
     * an application might have
     * 
     * @param interfaces the value of field 'interfaces'.
     */
    public void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType interfaces)
    {
        this._interfaces = interfaces;
    } //-- void setInterfaces(org.astrogrid.applications.beans.v1.InterfacesType) 

    /**
     * Sets the value of field 'name'. The field 'name' has the
     * following description: This is the name of an application -
     * it consists of the authorityId and the resourceID. e.g.
     * authority.id/name - This should probably really be the ivorn
     * proper, but all the implementations at the moment leave the
     * ivo:// part off the internal name of the application
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Sets the value of field 'parameters'. The field 'parameters'
     * has the following description: The complete list of
     * parameters that might occur in any of the apllication
     * interfaces
     * 
     * @param parameters the value of field 'parameters'.
     */
    public void setParameters(org.astrogrid.applications.beans.v1.Parameters parameters)
    {
        this._parameters = parameters;
    } //-- void setParameters(org.astrogrid.applications.beans.v1.Parameters) 

    /**
     * Method unmarshalApplicationBase
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.ApplicationBase unmarshalApplicationBase(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.ApplicationBase) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.ApplicationBase.class, reader);
    } //-- org.astrogrid.applications.beans.v1.ApplicationBase unmarshalApplicationBase(java.io.Reader) 

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
