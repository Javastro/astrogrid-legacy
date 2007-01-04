/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SimpleImageAccess.java,v 1.2 2007/01/04 16:26:30 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.sia;

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
 * The capabilities of an SIA implementation. It includes
 *  the listing of the columns that appear in image query
 *  output VOTable and SIA-specific metadata.
 *  
 *  Editor's Notes: 
 *  * This is a prototype definition to
 *  illustrate how to extend the schema to a speicific 
 *  standard interface.
 *  * Staging information is not included yet. 
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:30 $
 */
public class SimpleImageAccess extends org.astrogrid.registry.beans.v10.resource.dataservice.TabularSkyService 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a description of the behavior and limitations
     *  of the SIA implementation. 
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.sia.SIACapability _capability;


      //----------------/
     //- Constructors -/
    //----------------/

    public SimpleImageAccess() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SimpleImageAccess()


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
        
        if (obj instanceof SimpleImageAccess) {
        
            SimpleImageAccess temp = (SimpleImageAccess)obj;
            if (this._capability != null) {
                if (temp._capability == null) return false;
                else if (!(this._capability.equals(temp._capability))) 
                    return false;
            }
            else if (temp._capability != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'capability'. The field
     * 'capability' has the following description: a description of
     * the behavior and limitations
     *  of the SIA implementation. 
     *  
     * 
     * @return the value of field 'capability'.
     */
    public org.astrogrid.registry.beans.v10.resource.sia.SIACapability getCapability()
    {
        return this._capability;
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SIACapability getCapability() 

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
     * Sets the value of field 'capability'. The field 'capability'
     * has the following description: a description of the behavior
     * and limitations
     *  of the SIA implementation. 
     *  
     * 
     * @param capability the value of field 'capability'.
     */
    public void setCapability(org.astrogrid.registry.beans.v10.resource.sia.SIACapability capability)
    {
        this._capability = capability;
    } //-- void setCapability(org.astrogrid.registry.beans.v10.resource.sia.SIACapability) 

    /**
     * Method unmarshalSimpleImageAccess
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.sia.SimpleImageAccess unmarshalSimpleImageAccess(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.sia.SimpleImageAccess) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.sia.SimpleImageAccess.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.sia.SimpleImageAccess unmarshalSimpleImageAccess(java.io.Reader) 

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
