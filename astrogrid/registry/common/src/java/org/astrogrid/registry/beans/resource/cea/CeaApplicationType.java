/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CeaApplicationType.java,v 1.4 2004/04/05 14:36:12 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.cea;

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
 * Class CeaApplicationType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/04/05 14:36:12 $
 */
public class CeaApplicationType extends org.astrogrid.registry.beans.resource.ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _applicationDefinition
     */
    private org.astrogrid.registry.beans.resource.cea.ApplicationDefinition _applicationDefinition;


      //----------------/
     //- Constructors -/
    //----------------/

    public CeaApplicationType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.cea.CeaApplicationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'applicationDefinition'.
     * 
     * @return the value of field 'applicationDefinition'.
     */
    public org.astrogrid.registry.beans.resource.cea.ApplicationDefinition getApplicationDefinition()
    {
        return this._applicationDefinition;
    } //-- org.astrogrid.registry.beans.resource.cea.ApplicationDefinition getApplicationDefinition() 

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
     * Sets the value of field 'applicationDefinition'.
     * 
     * @param applicationDefinition the value of field
     * 'applicationDefinition'.
     */
    public void setApplicationDefinition(org.astrogrid.registry.beans.resource.cea.ApplicationDefinition applicationDefinition)
    {
        this._applicationDefinition = applicationDefinition;
    } //-- void setApplicationDefinition(org.astrogrid.registry.beans.resource.cea.ApplicationDefinition) 

    /**
     * Method unmarshalCeaApplicationType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.cea.CeaApplicationType unmarshalCeaApplicationType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.cea.CeaApplicationType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.cea.CeaApplicationType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.cea.CeaApplicationType unmarshalCeaApplicationType(java.io.Reader) 

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
