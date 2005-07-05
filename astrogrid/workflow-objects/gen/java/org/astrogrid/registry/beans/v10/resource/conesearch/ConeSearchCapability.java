/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ConeSearchCapability.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
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
 * The capabilities of a Cone Search implementation. It includes
 *  the listing of the columns that appear in image query
 *  output VOTable and Cone Search-specific metadata.
 *  
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:01 $
 */
public class ConeSearchCapability extends org.astrogrid.registry.beans.v10.resource.conesearch.CSCapRestriction 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The largest search radius, in degrees, that will be
     *  accepted by the service without returning an error 
     *  condition.
     *  
     */
    private float _maxSR;

    /**
     * keeps track of state for field: _maxSR
     */
    private boolean _has_maxSR;

    /**
     * The largest number of records that the service will 
     *  return. 
     *  
     */
    private int _maxRecords;

    /**
     * keeps track of state for field: _maxRecords
     */
    private boolean _has_maxRecords;

    /**
     * True if the service supports the VERB keyword; 
     *  false, otherwise.
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

    public ConeSearchCapability() {
        super();
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability()


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
        
        if (obj instanceof ConeSearchCapability) {
        
            ConeSearchCapability temp = (ConeSearchCapability)obj;
            if (this._maxSR != temp._maxSR)
                return false;
            if (this._has_maxSR != temp._has_maxSR)
                return false;
            if (this._maxRecords != temp._maxRecords)
                return false;
            if (this._has_maxRecords != temp._has_maxRecords)
                return false;
            if (this._verbosity != temp._verbosity)
                return false;
            if (this._has_verbosity != temp._has_verbosity)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'maxRecords'. The field
     * 'maxRecords' has the following description: The largest
     * number of records that the service will 
     *  return. 
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
     *  condition.
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
     * service supports the VERB keyword; 
     *  false, otherwise.
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
     * that the service will 
     *  return. 
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
     *  condition.
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
     * the VERB keyword; 
     *  false, otherwise.
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
     * Method unmarshalConeSearchCapability
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability unmarshalConeSearchCapability(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability) Unmarshaller.unmarshal(org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability.class, reader);
    } //-- org.astrogrid.registry.beans.v10.resource.conesearch.ConeSearchCapability unmarshalConeSearchCapability(java.io.Reader) 

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
