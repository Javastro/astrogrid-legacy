/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AliasSelectionItemChoice.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class AliasSelectionItemChoice.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AliasSelectionItemChoice extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _unaryExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr _unaryExpr;

    /**
     * Field _atomExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr _atomExpr;

    /**
     * Field _binaryExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr _binaryExpr;

    /**
     * Field _columnExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr _columnExpr;

    /**
     * Field _functionExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr _functionExpr;

    /**
     * Field _closedExpr
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr _closedExpr;


      //----------------/
     //- Constructors -/
    //----------------/

    public AliasSelectionItemChoice() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'atomExpr'.
     * 
     * @return the value of field 'atomExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr getAtomExpr()
    {
        return this._atomExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr getAtomExpr() 

    /**
     * Returns the value of field 'binaryExpr'.
     * 
     * @return the value of field 'binaryExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr getBinaryExpr()
    {
        return this._binaryExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr getBinaryExpr() 

    /**
     * Returns the value of field 'closedExpr'.
     * 
     * @return the value of field 'closedExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr getClosedExpr()
    {
        return this._closedExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr getClosedExpr() 

    /**
     * Returns the value of field 'columnExpr'.
     * 
     * @return the value of field 'columnExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr getColumnExpr()
    {
        return this._columnExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr getColumnExpr() 

    /**
     * Returns the value of field 'functionExpr'.
     * 
     * @return the value of field 'functionExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr getFunctionExpr()
    {
        return this._functionExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr getFunctionExpr() 

    /**
     * Returns the value of field 'unaryExpr'.
     * 
     * @return the value of field 'unaryExpr'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr getUnaryExpr()
    {
        return this._unaryExpr;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr getUnaryExpr() 

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
     * Sets the value of field 'atomExpr'.
     * 
     * @param atomExpr the value of field 'atomExpr'.
     */
    public void setAtomExpr(org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr atomExpr)
    {
        this._atomExpr = atomExpr;
    } //-- void setAtomExpr(org.astrogrid.datacenter.adql.generated.ogsadai.AtomExpr) 

    /**
     * Sets the value of field 'binaryExpr'.
     * 
     * @param binaryExpr the value of field 'binaryExpr'.
     */
    public void setBinaryExpr(org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr binaryExpr)
    {
        this._binaryExpr = binaryExpr;
    } //-- void setBinaryExpr(org.astrogrid.datacenter.adql.generated.ogsadai.BinaryExpr) 

    /**
     * Sets the value of field 'closedExpr'.
     * 
     * @param closedExpr the value of field 'closedExpr'.
     */
    public void setClosedExpr(org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr closedExpr)
    {
        this._closedExpr = closedExpr;
    } //-- void setClosedExpr(org.astrogrid.datacenter.adql.generated.ogsadai.ClosedExpr) 

    /**
     * Sets the value of field 'columnExpr'.
     * 
     * @param columnExpr the value of field 'columnExpr'.
     */
    public void setColumnExpr(org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr columnExpr)
    {
        this._columnExpr = columnExpr;
    } //-- void setColumnExpr(org.astrogrid.datacenter.adql.generated.ogsadai.ColumnExpr) 

    /**
     * Sets the value of field 'functionExpr'.
     * 
     * @param functionExpr the value of field 'functionExpr'.
     */
    public void setFunctionExpr(org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr functionExpr)
    {
        this._functionExpr = functionExpr;
    } //-- void setFunctionExpr(org.astrogrid.datacenter.adql.generated.ogsadai.FunctionExpr) 

    /**
     * Sets the value of field 'unaryExpr'.
     * 
     * @param unaryExpr the value of field 'unaryExpr'.
     */
    public void setUnaryExpr(org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr unaryExpr)
    {
        this._unaryExpr = unaryExpr;
    } //-- void setUnaryExpr(org.astrogrid.datacenter.adql.generated.ogsadai.UnaryExpr) 

    /**
     * Method unmarshalAliasSelectionItemChoice
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice unmarshalAliasSelectionItemChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice unmarshalAliasSelectionItemChoice(java.io.Reader) 

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
