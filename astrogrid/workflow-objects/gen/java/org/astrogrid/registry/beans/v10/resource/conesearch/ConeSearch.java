/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConeSearch.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.conesearch;

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
 * a standard service that returns catalog entries with a
 *  position falling within a cone on the sky (given by a position 
 *  and a radius). 
 *  
 *  The standard ConeSearch interface is defined to be ParamHTTP; 
 *  the specifics of this interface (e.g. the supported inputs)
 *  can be given using xsi:type="ParamHTTP" with the Interface 
 *  element. Other alternative (and therefore non-standard) 
 *  interfaces may also be described (e.g. a WebService version).
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:01 $
 */
public class ConeSearch extends org.astrogrid.registry.beans.v10.resource.dataservice.TabularSkyService 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * a description of how this particular ConeSearch 
     *  service behaves. 
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability _capability;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConeSearch() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearch()


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
        
        if (obj instanceof ConeSearch) {
        
            ConeSearch temp = (ConeSearch)obj;
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
     * how this particular ConeSearch 
     *  service behaves. 
     *  
     * 
     * @return the value of field 'capability'.
     */
    public org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability getCapability()
    {
        return this._capability;
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability getCapability() 

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
     * has the following description: a description of how this
     * particular ConeSearch 
     *  service behaves. 
     *  
     * 
     * @param capability the value of field 'capability'.
     */
    public void setCapability(org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability capability)
    {
        this._capability = capability;
    } //-- void setCapability(org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability) 

    /**
     * Method unmarshalConeSearch
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearch unmarshalConeSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearch) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearch.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearch unmarshalConeSearch(java.io.Reader) 

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
