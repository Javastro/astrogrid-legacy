/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoverageType.java,v 1.6 2004/03/19 08:16:47 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

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
 * Class CoverageType.
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/19 08:16:47 $
 */
public class CoverageType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the spatial coverage of a resource
     */
    private org.astrogrid.registry.beans.resource.dataservice.SpatialType _spatial;

    /**
     * the spectral coverage of a resource
     */
    private org.astrogrid.registry.beans.resource.dataservice.SpectralType _spectral;

    /**
     * the temporal coverage of a resource
     */
    private org.astrogrid.registry.beans.resource.dataservice.TemporalType _temporal;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoverageType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.CoverageType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'spatial'. The field 'spatial'
     * has the following description: the spatial coverage of a
     * resource
     * 
     * @return the value of field 'spatial'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.SpatialType getSpatial()
    {
        return this._spatial;
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpatialType getSpatial() 

    /**
     * Returns the value of field 'spectral'. The field 'spectral'
     * has the following description: the spectral coverage of a
     * resource
     * 
     * @return the value of field 'spectral'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.SpectralType getSpectral()
    {
        return this._spectral;
    } //-- org.astrogrid.registry.beans.resource.dataservice.SpectralType getSpectral() 

    /**
     * Returns the value of field 'temporal'. The field 'temporal'
     * has the following description: the temporal coverage of a
     * resource
     * 
     * @return the value of field 'temporal'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.TemporalType getTemporal()
    {
        return this._temporal;
    } //-- org.astrogrid.registry.beans.resource.dataservice.TemporalType getTemporal() 

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
     * @param spatial the value of field 'spatial'.
     */
    public void setSpatial(org.astrogrid.registry.beans.resource.dataservice.SpatialType spatial)
    {
        this._spatial = spatial;
    } //-- void setSpatial(org.astrogrid.registry.beans.resource.dataservice.SpatialType) 

    /**
     * Sets the value of field 'spectral'. The field 'spectral' has
     * the following description: the spectral coverage of a
     * resource
     * 
     * @param spectral the value of field 'spectral'.
     */
    public void setSpectral(org.astrogrid.registry.beans.resource.dataservice.SpectralType spectral)
    {
        this._spectral = spectral;
    } //-- void setSpectral(org.astrogrid.registry.beans.resource.dataservice.SpectralType) 

    /**
     * Sets the value of field 'temporal'. The field 'temporal' has
     * the following description: the temporal coverage of a
     * resource
     * 
     * @param temporal the value of field 'temporal'.
     */
    public void setTemporal(org.astrogrid.registry.beans.resource.dataservice.TemporalType temporal)
    {
        this._temporal = temporal;
    } //-- void setTemporal(org.astrogrid.registry.beans.resource.dataservice.TemporalType) 

    /**
     * Method unmarshalCoverageType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.CoverageType unmarshalCoverageType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.CoverageType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.CoverageType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.CoverageType unmarshalCoverageType(java.io.Reader) 

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
