/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Select.java,v 1.3 2003/11/18 14:21:03 nw Exp $
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
 * Class Select.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/11/18 14:21:03 $
 */
public class Select extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _optionalAllOrDistinct
     */
    private org.astrogrid.datacenter.adql.generated.SelectionOption _optionalAllOrDistinct;

    /**
     * Field _selectChoice
     */
    private org.astrogrid.datacenter.adql.generated.SelectChoice _selectChoice;

    /**
     * Field _tableClause
     */
    private org.astrogrid.datacenter.adql.generated.TableExpression _tableClause;

    /**
     * Field _orderBy
     */
    private org.astrogrid.datacenter.adql.generated.OrderExpression _orderBy;


      //----------------/
     //- Constructors -/
    //----------------/

    public Select() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.Select()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'optionalAllOrDistinct'.
     * 
     * @return the value of field 'optionalAllOrDistinct'.
     */
    public org.astrogrid.datacenter.adql.generated.SelectionOption getOptionalAllOrDistinct()
    {
        return this._optionalAllOrDistinct;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionOption getOptionalAllOrDistinct() 

    /**
     * Returns the value of field 'orderBy'.
     * 
     * @return the value of field 'orderBy'.
     */
    public org.astrogrid.datacenter.adql.generated.OrderExpression getOrderBy()
    {
        return this._orderBy;
    } //-- org.astrogrid.datacenter.adql.generated.OrderExpression getOrderBy() 

    /**
     * Returns the value of field 'selectChoice'.
     * 
     * @return the value of field 'selectChoice'.
     */
    public org.astrogrid.datacenter.adql.generated.SelectChoice getSelectChoice()
    {
        return this._selectChoice;
    } //-- org.astrogrid.datacenter.adql.generated.SelectChoice getSelectChoice() 

    /**
     * Returns the value of field 'tableClause'.
     * 
     * @return the value of field 'tableClause'.
     */
    public org.astrogrid.datacenter.adql.generated.TableExpression getTableClause()
    {
        return this._tableClause;
    } //-- org.astrogrid.datacenter.adql.generated.TableExpression getTableClause() 

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
     * Sets the value of field 'optionalAllOrDistinct'.
     * 
     * @param optionalAllOrDistinct the value of field
     * 'optionalAllOrDistinct'.
     */
    public void setOptionalAllOrDistinct(org.astrogrid.datacenter.adql.generated.SelectionOption optionalAllOrDistinct)
    {
        this._optionalAllOrDistinct = optionalAllOrDistinct;
    } //-- void setOptionalAllOrDistinct(org.astrogrid.datacenter.adql.generated.SelectionOption) 

    /**
     * Sets the value of field 'orderBy'.
     * 
     * @param orderBy the value of field 'orderBy'.
     */
    public void setOrderBy(org.astrogrid.datacenter.adql.generated.OrderExpression orderBy)
    {
        this._orderBy = orderBy;
    } //-- void setOrderBy(org.astrogrid.datacenter.adql.generated.OrderExpression) 

    /**
     * Sets the value of field 'selectChoice'.
     * 
     * @param selectChoice the value of field 'selectChoice'.
     */
    public void setSelectChoice(org.astrogrid.datacenter.adql.generated.SelectChoice selectChoice)
    {
        this._selectChoice = selectChoice;
    } //-- void setSelectChoice(org.astrogrid.datacenter.adql.generated.SelectChoice) 

    /**
     * Sets the value of field 'tableClause'.
     * 
     * @param tableClause the value of field 'tableClause'.
     */
    public void setTableClause(org.astrogrid.datacenter.adql.generated.TableExpression tableClause)
    {
        this._tableClause = tableClause;
    } //-- void setTableClause(org.astrogrid.datacenter.adql.generated.TableExpression) 

    /**
     * Method unmarshalSelect
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.Select unmarshalSelect(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.Select) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.Select.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.Select unmarshalSelect(java.io.Reader) 

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
