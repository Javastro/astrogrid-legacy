/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeChoice.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.math.BigDecimal;
import java.util.Date;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AstronTimeTypeChoice.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AstronTimeTypeChoice extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _MJDRefTime
     */
    private java.math.BigDecimal _MJDRefTime;

    /**
     * Field _reference
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference _reference;

    /**
     * Field _ISORefTime
     */
    private java.util.Date _ISORefTime;

    /**
     * Field _ISOTime
     */
    private java.util.Date _ISOTime;

    /**
     * Field _relativeTime
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime _relativeTime;

    /**
     * Field _JDTime
     */
    private java.math.BigDecimal _JDTime;

    /**
     * Field _MJDTime
     */
    private java.math.BigDecimal _MJDTime;

    /**
     * Field _JDRefTime
     */
    private java.math.BigDecimal _JDRefTime;


      //----------------/
     //- Constructors -/
    //----------------/

    public AstronTimeTypeChoice() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'ISORefTime'.
     * 
     * @return the value of field 'ISORefTime'.
     */
    public java.util.Date getISORefTime()
    {
        return this._ISORefTime;
    } //-- java.util.Date getISORefTime() 

    /**
     * Returns the value of field 'ISOTime'.
     * 
     * @return the value of field 'ISOTime'.
     */
    public java.util.Date getISOTime()
    {
        return this._ISOTime;
    } //-- java.util.Date getISOTime() 

    /**
     * Returns the value of field 'JDRefTime'.
     * 
     * @return the value of field 'JDRefTime'.
     */
    public java.math.BigDecimal getJDRefTime()
    {
        return this._JDRefTime;
    } //-- java.math.BigDecimal getJDRefTime() 

    /**
     * Returns the value of field 'JDTime'.
     * 
     * @return the value of field 'JDTime'.
     */
    public java.math.BigDecimal getJDTime()
    {
        return this._JDTime;
    } //-- java.math.BigDecimal getJDTime() 

    /**
     * Returns the value of field 'MJDRefTime'.
     * 
     * @return the value of field 'MJDRefTime'.
     */
    public java.math.BigDecimal getMJDRefTime()
    {
        return this._MJDRefTime;
    } //-- java.math.BigDecimal getMJDRefTime() 

    /**
     * Returns the value of field 'MJDTime'.
     * 
     * @return the value of field 'MJDTime'.
     */
    public java.math.BigDecimal getMJDTime()
    {
        return this._MJDTime;
    } //-- java.math.BigDecimal getMJDTime() 

    /**
     * Returns the value of field 'reference'.
     * 
     * @return the value of field 'reference'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference getReference()
    {
        return this._reference;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference getReference() 

    /**
     * Returns the value of field 'relativeTime'.
     * 
     * @return the value of field 'relativeTime'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime getRelativeTime()
    {
        return this._relativeTime;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime getRelativeTime() 

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
     * Sets the value of field 'ISORefTime'.
     * 
     * @param ISORefTime the value of field 'ISORefTime'.
     */
    public void setISORefTime(java.util.Date ISORefTime)
    {
        this._ISORefTime = ISORefTime;
    } //-- void setISORefTime(java.util.Date) 

    /**
     * Sets the value of field 'ISOTime'.
     * 
     * @param ISOTime the value of field 'ISOTime'.
     */
    public void setISOTime(java.util.Date ISOTime)
    {
        this._ISOTime = ISOTime;
    } //-- void setISOTime(java.util.Date) 

    /**
     * Sets the value of field 'JDRefTime'.
     * 
     * @param JDRefTime the value of field 'JDRefTime'.
     */
    public void setJDRefTime(java.math.BigDecimal JDRefTime)
    {
        this._JDRefTime = JDRefTime;
    } //-- void setJDRefTime(java.math.BigDecimal) 

    /**
     * Sets the value of field 'JDTime'.
     * 
     * @param JDTime the value of field 'JDTime'.
     */
    public void setJDTime(java.math.BigDecimal JDTime)
    {
        this._JDTime = JDTime;
    } //-- void setJDTime(java.math.BigDecimal) 

    /**
     * Sets the value of field 'MJDRefTime'.
     * 
     * @param MJDRefTime the value of field 'MJDRefTime'.
     */
    public void setMJDRefTime(java.math.BigDecimal MJDRefTime)
    {
        this._MJDRefTime = MJDRefTime;
    } //-- void setMJDRefTime(java.math.BigDecimal) 

    /**
     * Sets the value of field 'MJDTime'.
     * 
     * @param MJDTime the value of field 'MJDTime'.
     */
    public void setMJDTime(java.math.BigDecimal MJDTime)
    {
        this._MJDTime = MJDTime;
    } //-- void setMJDTime(java.math.BigDecimal) 

    /**
     * Sets the value of field 'reference'.
     * 
     * @param reference the value of field 'reference'.
     */
    public void setReference(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference reference)
    {
        this._reference = reference;
    } //-- void setReference(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference) 

    /**
     * Sets the value of field 'relativeTime'.
     * 
     * @param relativeTime the value of field 'relativeTime'.
     */
    public void setRelativeTime(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime relativeTime)
    {
        this._relativeTime = relativeTime;
    } //-- void setRelativeTime(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime) 

    /**
     * Method unmarshalAstronTimeTypeChoice
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice unmarshalAstronTimeTypeChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice unmarshalAstronTimeTypeChoice(java.io.Reader) 

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
