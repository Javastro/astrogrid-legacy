/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AstronTimeType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AstronTimeType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _astronTimeTypeChoice
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice _astronTimeTypeChoice;

    /**
     * Field _timeScale
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType _timeScale;


      //----------------/
     //- Constructors -/
    //----------------/

    public AstronTimeType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'astronTimeTypeChoice'.
     * 
     * @return the value of field 'astronTimeTypeChoice'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice getAstronTimeTypeChoice()
    {
        return this._astronTimeTypeChoice;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice getAstronTimeTypeChoice() 

    /**
     * Returns the value of field 'timeScale'.
     * 
     * @return the value of field 'timeScale'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType getTimeScale()
    {
        return this._timeScale;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType getTimeScale() 

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
     * Sets the value of field 'astronTimeTypeChoice'.
     * 
     * @param astronTimeTypeChoice the value of field
     * 'astronTimeTypeChoice'.
     */
    public void setAstronTimeTypeChoice(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice astronTimeTypeChoice)
    {
        this._astronTimeTypeChoice = astronTimeTypeChoice;
    } //-- void setAstronTimeTypeChoice(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeChoice) 

    /**
     * Sets the value of field 'timeScale'.
     * 
     * @param timeScale the value of field 'timeScale'.
     */
    public void setTimeScale(org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType timeScale)
    {
        this._timeScale = timeScale;
    } //-- void setTimeScale(org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType) 

    /**
     * Method unmarshalAstronTimeType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType unmarshalAstronTimeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeType unmarshalAstronTimeType(java.io.Reader) 

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
