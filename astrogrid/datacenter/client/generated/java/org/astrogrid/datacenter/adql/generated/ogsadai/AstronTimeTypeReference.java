/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeReference.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AstronTimeTypeReference.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AstronTimeTypeReference extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * internal content storage
     */
    private java.lang.Object _content;

    /**
     * Field _time_base
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base _time_base = org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base.valueOf("ISO8601");

    /**
     * Field _unit
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit _unit = org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit.valueOf("s");


      //----------------/
     //- Constructors -/
    //----------------/

    public AstronTimeTypeReference() {
        super();
        setTime_base(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base.valueOf("ISO8601"));
        setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit.valueOf("s"));
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'content'. The field 'content'
     * has the following description: internal content storage
     * 
     * @return the value of field 'content'.
     */
    public java.lang.Object getContent()
    {
        return this._content;
    } //-- java.lang.Object getContent() 

    /**
     * Returns the value of field 'time_base'.
     * 
     * @return the value of field 'time_base'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base getTime_base()
    {
        return this._time_base;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base getTime_base() 

    /**
     * Returns the value of field 'unit'.
     * 
     * @return the value of field 'unit'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit getUnit()
    {
        return this._unit;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit getUnit() 

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
    public void setContent(java.lang.Object content)
    {
        this._content = content;
    } //-- void setContent(java.lang.Object) 

    /**
     * Sets the value of field 'time_base'.
     * 
     * @param time_base the value of field 'time_base'.
     */
    public void setTime_base(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base time_base)
    {
        this._time_base = time_base;
    } //-- void setTime_base(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base) 

    /**
     * Sets the value of field 'unit'.
     * 
     * @param unit the value of field 'unit'.
     */
    public void setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit unit)
    {
        this._unit = unit;
    } //-- void setUnit(org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceUnit) 

    /**
     * Method unmarshalAstronTimeTypeReference
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference unmarshalAstronTimeTypeReference(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AstronTimeTypeReference unmarshalAstronTimeTypeReference(java.io.Reader) 

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
