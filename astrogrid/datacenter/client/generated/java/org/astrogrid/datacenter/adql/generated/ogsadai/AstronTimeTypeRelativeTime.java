/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeRelativeTime.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AstronTimeTypeRelativeTime.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AstronTimeTypeRelativeTime extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private double _content;

    /**
     * keeps track of state for field: _content
     */
    private boolean _has_content;

    /**
     * Field _unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit _unit = org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit.valueOf("s");


      //----------------/
     //- Constructors -/
    //----------------/

    public AstronTimeTypeRelativeTime() {
        super();
        setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit.valueOf("s"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteContent
     */
    public void deleteContent()
    {
        this._has_content= false;
    } //-- void deleteContent() 

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public double getContent()
    {
        return this._content;
    } //-- double getContent() 

    /**
     * Returns the value of field 'unit'.
     * 
     * @return the value of field 'unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit getUnit()
    {
        return this._unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit getUnit() 

    /**
     * Method hasContent
     */
    public boolean hasContent()
    {
        return this._has_content;
    } //-- boolean hasContent() 

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
     * Sets the value of field 'content'. The field 'content' has
     * the following description: internal content storage
     * 
     * @param content the value of field 'content'.
     */
    public void setContent(double content)
    {
        this._content = content;
        this._has_content = true;
    } //-- void setContent(double) 

    /**
     * Sets the value of field 'unit'.
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit unit)
    {
        this._unit = unit;
    } //-- void setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeRelativeTimeUnit) 

    /**
     * Method unmarshalAstronTimeTypeRelativeTime
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime unmarshalAstronTimeTypeRelativeTime(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeRelativeTime unmarshalAstronTimeTypeRelativeTime(java.io.Reader) 

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
