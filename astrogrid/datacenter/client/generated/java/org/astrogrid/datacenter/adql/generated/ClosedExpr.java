/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ClosedExpr.java,v 1.10 2003/11/27 17:27:15 nw Exp $
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
 * Class ClosedExpr.
 * 
 * @version $Revision: 1.10 $ $Date: 2003/11/27 17:27:15 $
 */
public class ClosedExpr extends org.astrogrid.datacenter.adql.generated.ScalarExpression 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _expr
     */
    private org.astrogrid.datacenter.adql.generated.ScalarExpression _expr;


      //----------------/
     //- Constructors -/
    //----------------/

    public ClosedExpr() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ClosedExpr()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'expr'.
     * 
     * @return the value of field 'expr'.
     */
    public org.astrogrid.datacenter.adql.generated.ScalarExpression getExpr()
    {
        return this._expr;
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression getExpr() 

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
     * Sets the value of field 'expr'.
     * 
     * @param expr the value of field 'expr'.
     */
    public void setExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression expr)
    {
        this._expr = expr;
    } //-- void setExpr(org.astrogrid.datacenter.adql.generated.ScalarExpression) 

    /**
     * Method unmarshalClosedExpr
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ClosedExpr unmarshalClosedExpr(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ClosedExpr) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ClosedExpr.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ClosedExpr unmarshalClosedExpr(java.io.Reader) 

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
