/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConeSearchType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.conesearch;

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
 * Class ConeSearchType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class ConeSearchType extends org.astrogrid.registry.beans.resource.CapabilityType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The largest search radius, in degrees, that will be
     *  accepted by the service without returning an error
     * condition.
     *  
     */
    private float _maxSR;

    /**
     * keeps track of state for field: _maxSR
     */
    private boolean _has_maxSR;

    /**
     * The largest number of records that the service will return. 
     *  
     */
    private int _maxRecords;

    /**
     * keeps track of state for field: _maxRecords
     */
    private boolean _has_maxRecords;

    /**
     * True if the service supports the VERB keyword; false,
     * otherwise.
     *  
     */
    private boolean _verbosity;

    /**
     * keeps track of state for field: _verbosity
     */
    private boolean _has_verbosity;


      //----------------/
     //- Constructors -/
    //----------------/

    public ConeSearchType() {
        super();
    } //-- org.astrogrid.registry.beans.resource.conesearch.ConeSearchType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'maxRecords'. The field
     * 'maxRecords' has the following description: The largest
     * number of records that the service will return. 
     *  
     * 
     * @return the value of field 'maxRecords'.
     */
    public int getMaxRecords()
    {
        return this._maxRecords;
    } //-- int getMaxRecords() 

    /**
     * Returns the value of field 'maxSR'. The field 'maxSR' has
     * the following description: The largest search radius, in
     * degrees, that will be
     *  accepted by the service without returning an error
     * condition.
     *  
     * 
     * @return the value of field 'maxSR'.
     */
    public float getMaxSR()
    {
        return this._maxSR;
    } //-- float getMaxSR() 

    /**
     * Returns the value of field 'verbosity'. The field
     * 'verbosity' has the following description: True if the
     * service supports the VERB keyword; false, otherwise.
     *  
     * 
     * @return the value of field 'verbosity'.
     */
    public boolean getVerbosity()
    {
        return this._verbosity;
    } //-- boolean getVerbosity() 

    /**
     * Method hasMaxRecords
     */
    public boolean hasMaxRecords()
    {
        return this._has_maxRecords;
    } //-- boolean hasMaxRecords() 

    /**
     * Method hasMaxSR
     */
    public boolean hasMaxSR()
    {
        return this._has_maxSR;
    } //-- boolean hasMaxSR() 

    /**
     * Method hasVerbosity
     */
    public boolean hasVerbosity()
    {
        return this._has_verbosity;
    } //-- boolean hasVerbosity() 

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
     * Sets the value of field 'maxRecords'. The field 'maxRecords'
     * has the following description: The largest number of records
     * that the service will return. 
     *  
     * 
     * @param maxRecords the value of field 'maxRecords'.
     */
    public void setMaxRecords(int maxRecords)
    {
        this._maxRecords = maxRecords;
        this._has_maxRecords = true;
    } //-- void setMaxRecords(int) 

    /**
     * Sets the value of field 'maxSR'. The field 'maxSR' has the
     * following description: The largest search radius, in
     * degrees, that will be
     *  accepted by the service without returning an error
     * condition.
     *  
     * 
     * @param maxSR the value of field 'maxSR'.
     */
    public void setMaxSR(float maxSR)
    {
        this._maxSR = maxSR;
        this._has_maxSR = true;
    } //-- void setMaxSR(float) 

    /**
     * Sets the value of field 'verbosity'. The field 'verbosity'
     * has the following description: True if the service supports
     * the VERB keyword; false, otherwise.
     *  
     * 
     * @param verbosity the value of field 'verbosity'.
     */
    public void setVerbosity(boolean verbosity)
    {
        this._verbosity = verbosity;
        this._has_verbosity = true;
    } //-- void setVerbosity(boolean) 

    /**
     * Method unmarshalConeSearchType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.conesearch.ConeSearchType unmarshalConeSearchType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.conesearch.ConeSearchType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.conesearch.ConeSearchType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.conesearch.ConeSearchType unmarshalConeSearchType(java.io.Reader) 

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
