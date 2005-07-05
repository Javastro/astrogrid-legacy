/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CeaHttpApplicationType.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.cea;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.WebHttpApplicationSetup;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * The definition of a CEA Http application
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class CeaHttpApplicationType extends org.astrogrid.registry.beans.v10.cea.CeaApplicationType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ceaHttpAdapterSetup
     */
    private org.astrogrid.applications.beans.v1.WebHttpApplicationSetup _ceaHttpAdapterSetup;


      //----------------/
     //- Constructors -/
    //----------------/

    public CeaHttpApplicationType() {
        super();
    } //-- org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType()


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
        
        if (obj instanceof CeaHttpApplicationType) {
        
            CeaHttpApplicationType temp = (CeaHttpApplicationType)obj;
            if (this._ceaHttpAdapterSetup != null) {
                if (temp._ceaHttpAdapterSetup == null) return false;
                else if (!(this._ceaHttpAdapterSetup.equals(temp._ceaHttpAdapterSetup))) 
                    return false;
            }
            else if (temp._ceaHttpAdapterSetup != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'ceaHttpAdapterSetup'.
     * 
     * @return the value of field 'ceaHttpAdapterSetup'.
     */
    public org.astrogrid.applications.beans.v1.WebHttpApplicationSetup getCeaHttpAdapterSetup()
    {
        return this._ceaHttpAdapterSetup;
    } //-- org.astrogrid.applications.beans.v1.WebHttpApplicationSetup getCeaHttpAdapterSetup() 

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
     * Sets the value of field 'ceaHttpAdapterSetup'.
     * 
     * @param ceaHttpAdapterSetup the value of field
     * 'ceaHttpAdapterSetup'.
     */
    public void setCeaHttpAdapterSetup(org.astrogrid.applications.beans.v1.WebHttpApplicationSetup ceaHttpAdapterSetup)
    {
        this._ceaHttpAdapterSetup = ceaHttpAdapterSetup;
    } //-- void setCeaHttpAdapterSetup(org.astrogrid.applications.beans.v1.WebHttpApplicationSetup) 

    /**
     * Method unmarshalCeaHttpApplicationType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType unmarshalCeaHttpApplicationType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType.class, reader);
    } //-- org.astrogrid.registry.beans.v10.cea.CeaHttpApplicationType unmarshalCeaHttpApplicationType(java.io.Reader) 

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
