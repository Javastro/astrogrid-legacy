/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CoverageType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class CoverageType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the spatial coverage of a resource
     */
    private org.astrogrid.registry.generated.package.Spatial _spatial;

    /**
     * the spectral coverage of a resource
     */
    private org.astrogrid.registry.generated.package.Spectral _spectral;

    /**
     * the temporal coverage of a resource
     */
    private org.astrogrid.registry.generated.package.Temporal _temporal;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoverageType() {
        super();
    } //-- org.astrogrid.registry.generated.package.CoverageType()


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
    public org.astrogrid.registry.generated.package.Spatial getSpatial()
    {
        return this._spatial;
    } //-- org.astrogrid.registry.generated.package.Spatial getSpatial() 

    /**
     * Returns the value of field 'spectral'. The field 'spectral'
     * has the following description: the spectral coverage of a
     * resource
     * 
     * @return the value of field 'spectral'.
     */
    public org.astrogrid.registry.generated.package.Spectral getSpectral()
    {
        return this._spectral;
    } //-- org.astrogrid.registry.generated.package.Spectral getSpectral() 

    /**
     * Returns the value of field 'temporal'. The field 'temporal'
     * has the following description: the temporal coverage of a
     * resource
     * 
     * @return the value of field 'temporal'.
     */
    public org.astrogrid.registry.generated.package.Temporal getTemporal()
    {
        return this._temporal;
    } //-- org.astrogrid.registry.generated.package.Temporal getTemporal() 

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
    public void setSpatial(org.astrogrid.registry.generated.package.Spatial spatial)
    {
        this._spatial = spatial;
    } //-- void setSpatial(org.astrogrid.registry.generated.package.Spatial) 

    /**
     * Sets the value of field 'spectral'. The field 'spectral' has
     * the following description: the spectral coverage of a
     * resource
     * 
     * @param spectral the value of field 'spectral'.
     */
    public void setSpectral(org.astrogrid.registry.generated.package.Spectral spectral)
    {
        this._spectral = spectral;
    } //-- void setSpectral(org.astrogrid.registry.generated.package.Spectral) 

    /**
     * Sets the value of field 'temporal'. The field 'temporal' has
     * the following description: the temporal coverage of a
     * resource
     * 
     * @param temporal the value of field 'temporal'.
     */
    public void setTemporal(org.astrogrid.registry.generated.package.Temporal temporal)
    {
        this._temporal = temporal;
    } //-- void setTemporal(org.astrogrid.registry.generated.package.Temporal) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.CoverageType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.CoverageType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
