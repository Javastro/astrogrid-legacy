/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Coverage.java,v 1.2 2007/01/04 16:26:25 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice;

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
 * Class Coverage.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:25 $
 */
public class Coverage extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the spatial coverage of a resource
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.Spatial _spatial;

    /**
     * the spectral coverage of a resource
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.Spectral _spectral;

    /**
     * the temporal coverage of a resource
     *  
     */
    private org.astrogrid.registry.beans.v10.resource.dataservice.Temporal _temporal;


      //----------------/
     //- Constructors -/
    //----------------/

    public Coverage() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Coverage()


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
        
        if (obj instanceof Coverage) {
        
            Coverage temp = (Coverage)obj;
            if (this._spatial != null) {
                if (temp._spatial == null) return false;
                else if (!(this._spatial.equals(temp._spatial))) 
                    return false;
            }
            else if (temp._spatial != null)
                return false;
            if (this._spectral != null) {
                if (temp._spectral == null) return false;
                else if (!(this._spectral.equals(temp._spectral))) 
                    return false;
            }
            else if (temp._spectral != null)
                return false;
            if (this._temporal != null) {
                if (temp._temporal == null) return false;
                else if (!(this._temporal.equals(temp._temporal))) 
                    return false;
            }
            else if (temp._temporal != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'spatial'. The field 'spatial'
     * has the following description: the spatial coverage of a
     * resource
     *  
     * 
     * @return the value of field 'spatial'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Spatial getSpatial()
    {
        return this._spatial;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spatial getSpatial() 

    /**
     * Returns the value of field 'spectral'. The field 'spectral'
     * has the following description: the spectral coverage of a
     * resource
     *  
     * 
     * @return the value of field 'spectral'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Spectral getSpectral()
    {
        return this._spectral;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Spectral getSpectral() 

    /**
     * Returns the value of field 'temporal'. The field 'temporal'
     * has the following description: the temporal coverage of a
     * resource
     *  
     * 
     * @return the value of field 'temporal'.
     */
    public org.astrogrid.registry.beans.v10.resource.dataservice.Temporal getTemporal()
    {
        return this._temporal;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Temporal getTemporal() 

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
     * Sets the value of field 'spatial'. The field 'spatial' has
     * the following description: the spatial coverage of a
     * resource
     *  
     * 
     * @param spatial the value of field 'spatial'.
     */
    public void setSpatial(org.astrogrid.registry.beans.v10.resource.dataservice.Spatial spatial)
    {
        this._spatial = spatial;
    } //-- void setSpatial(org.astrogrid.registry.beans.v10.resource.dataservice.Spatial) 

    /**
     * Sets the value of field 'spectral'. The field 'spectral' has
     * the following description: the spectral coverage of a
     * resource
     *  
     * 
     * @param spectral the value of field 'spectral'.
     */
    public void setSpectral(org.astrogrid.registry.beans.v10.resource.dataservice.Spectral spectral)
    {
        this._spectral = spectral;
    } //-- void setSpectral(org.astrogrid.registry.beans.v10.resource.dataservice.Spectral) 

    /**
     * Sets the value of field 'temporal'. The field 'temporal' has
     * the following description: the temporal coverage of a
     * resource
     *  
     * 
     * @param temporal the value of field 'temporal'.
     */
    public void setTemporal(org.astrogrid.registry.beans.v10.resource.dataservice.Temporal temporal)
    {
        this._temporal = temporal;
    } //-- void setTemporal(org.astrogrid.registry.beans.v10.resource.dataservice.Temporal) 

    /**
     * Method unmarshalCoverage
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.Coverage unmarshalCoverage(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.dataservice.Coverage) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.dataservice.Coverage.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.Coverage unmarshalCoverage(java.io.Reader) 

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
