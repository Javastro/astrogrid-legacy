/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Long.java,v 1.3 2004/03/05 09:51:59 KevinBenson Exp $
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
 * The range in longitude
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:51:59 $
 */
public class Long extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The minimum longitude
     */
    private double _min;

    /**
     * keeps track of state for field: _min
     */
    private boolean _has_min;

    /**
     * The maximum longitude
     */
    private double _max;

    /**
     * keeps track of state for field: _max
     */
    private boolean _has_max;


      //----------------/
     //- Constructors -/
    //----------------/

    public Long() {
        super();
    } //-- org.astrogrid.registry.beans.resource.dataservice.Long()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'max'. The field 'max' has the
     * following description: The maximum longitude
     * 
     * @return the value of field 'max'.
     */
    public double getMax()
    {
        return this._max;
    } //-- double getMax() 

    /**
     * Returns the value of field 'min'. The field 'min' has the
     * following description: The minimum longitude
     * 
     * @return the value of field 'min'.
     */
    public double getMin()
    {
        return this._min;
    } //-- double getMin() 

    /**
     * Method hasMax
     */
    public boolean hasMax()
    {
        return this._has_max;
    } //-- boolean hasMax() 

    /**
     * Method hasMin
     */
    public boolean hasMin()
    {
        return this._has_min;
    } //-- boolean hasMin() 

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
     * Sets the value of field 'max'. The field 'max' has the
     * following description: The maximum longitude
     * 
     * @param max the value of field 'max'.
     */
    public void setMax(double max)
    {
        this._max = max;
        this._has_max = true;
    } //-- void setMax(double) 

    /**
     * Sets the value of field 'min'. The field 'min' has the
     * following description: The minimum longitude
     * 
     * @param min the value of field 'min'.
     */
    public void setMin(double min)
    {
        this._min = min;
        this._has_min = true;
    } //-- void setMin(double) 

    /**
     * Method unmarshalLong
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.Long unmarshalLong(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.Long) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.Long.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.Long unmarshalLong(java.io.Reader) 

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
