/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: NumberLiteral.java,v 1.8 2003/11/26 17:01:37 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class NumberLiteral.
 * 
 * @version $Revision: 1.8 $ $Date: 2003/11/26 17:01:37 $
 */
public class NumberLiteral extends org.astrogrid.datacenter.adql.generated.Literal 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _intNum
     */
    private org.astrogrid.datacenter.adql.generated.IntNum _intNum;

    /**
     * Field _approxNum
     */
    private org.astrogrid.datacenter.adql.generated.ApproxNum _approxNum;


      //----------------/
     //- Constructors -/
    //----------------/

    public NumberLiteral() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.NumberLiteral()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'approxNum'.
     * 
     * @return the value of field 'approxNum'.
     */
    public org.astrogrid.datacenter.adql.generated.ApproxNum getApproxNum()
    {
        return this._approxNum;
    } //-- org.astrogrid.datacenter.adql.generated.ApproxNum getApproxNum() 

    /**
     * Returns the value of field 'intNum'.
     * 
     * @return the value of field 'intNum'.
     */
    public org.astrogrid.datacenter.adql.generated.IntNum getIntNum()
    {
        return this._intNum;
    } //-- org.astrogrid.datacenter.adql.generated.IntNum getIntNum() 

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
     * Sets the value of field 'approxNum'.
     * 
     * @param approxNum the value of field 'approxNum'.
     */
    public void setApproxNum(org.astrogrid.datacenter.adql.generated.ApproxNum approxNum)
    {
        this._approxNum = approxNum;
    } //-- void setApproxNum(org.astrogrid.datacenter.adql.generated.ApproxNum) 

    /**
     * Sets the value of field 'intNum'.
     * 
     * @param intNum the value of field 'intNum'.
     */
    public void setIntNum(org.astrogrid.datacenter.adql.generated.IntNum intNum)
    {
        this._intNum = intNum;
    } //-- void setIntNum(org.astrogrid.datacenter.adql.generated.IntNum) 

    /**
     * Method unmarshalNumberLiteral
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.NumberLiteral unmarshalNumberLiteral(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.NumberLiteral) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.NumberLiteral.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.NumberLiteral unmarshalNumberLiteral(java.io.Reader) 

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
