/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CeaServiceType.java,v 1.2 2007/01/04 16:26:31 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.cea;

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
 * The definition of a service that can offer an instance of a
 * CeaApplication
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:31 $
 */
public class CeaServiceType extends org.astrogrid.registry.beans.v10.resource.Service 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Ths list of applications that a Common Execution Controller
     * Manages
     */
    private org.astrogrid.registry.beans.v10.cea.ManagedApplications _managedApplications;


      //----------------/
     //- Constructors -/
    //----------------/

    public CeaServiceType() {
        super();
    } //-- org.astrogrid.registry.beans.v10.cea.CeaServiceType()


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
        
        if (obj instanceof CeaServiceType) {
        
            CeaServiceType temp = (CeaServiceType)obj;
            if (this._managedApplications != null) {
                if (temp._managedApplications == null) return false;
                else if (!(this._managedApplications.equals(temp._managedApplications))) 
                    return false;
            }
            else if (temp._managedApplications != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'managedApplications'. The field
     * 'managedApplications' has the following description: Ths
     * list of applications that a Common Execution Controller
     * Manages
     * 
     * @return the value of field 'managedApplications'.
     */
    public org.astrogrid.registry.beans.v10.cea.ManagedApplications getManagedApplications()
    {
        return this._managedApplications;
    } //-- org.astrogrid.registry.beans.v10.cea.ManagedApplications getManagedApplications() 

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
     * Sets the value of field 'managedApplications'. The field
     * 'managedApplications' has the following description: Ths
     * list of applications that a Common Execution Controller
     * Manages
     * 
     * @param managedApplications the value of field
     * 'managedApplications'.
     */
    public void setManagedApplications(org.astrogrid.registry.beans.v10.cea.ManagedApplications managedApplications)
    {
        this._managedApplications = managedApplications;
    } //-- void setManagedApplications(org.astrogrid.registry.beans.v10.cea.ManagedApplications) 

    /**
     * Method unmarshalCeaServiceType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.cea.CeaServiceType unmarshalCeaServiceType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.cea.CeaServiceType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.cea.CeaServiceType.class, reader);
    } //-- org.astrogrid.registry.beans.v10.cea.CeaServiceType unmarshalCeaServiceType(java.io.Reader) 

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
