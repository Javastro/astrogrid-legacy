/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Select.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class Select.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class Select extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _optionalAllOrDistinct
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption _optionalAllOrDistinct;

    /**
     * Field _optionalTop
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.SelectionLimit _optionalTop;

    /**
     * Field _selection
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList _selection;

    /**
     * Field _tableClause
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression _tableClause;

    /**
     * Field _orderBy
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression _orderBy;


      //----------------/
     //- Constructors -/
    //----------------/

    public Select() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Select()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'optionalAllOrDistinct'.
     * 
     * @return the value of field 'optionalAllOrDistinct'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption getOptionalAllOrDistinct()
    {
        return this._optionalAllOrDistinct;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption getOptionalAllOrDistinct() 

    /**
     * Returns the value of field 'optionalTop'.
     * 
     * @return the value of field 'optionalTop'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.SelectionLimit getOptionalTop()
    {
        return this._optionalTop;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SelectionLimit getOptionalTop() 

    /**
     * Returns the value of field 'orderBy'.
     * 
     * @return the value of field 'orderBy'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression getOrderBy()
    {
        return this._orderBy;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression getOrderBy() 

    /**
     * Returns the value of field 'selection'.
     * 
     * @return the value of field 'selection'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList getSelection()
    {
        return this._selection;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList getSelection() 

    /**
     * Returns the value of field 'tableClause'.
     * 
     * @return the value of field 'tableClause'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression getTableClause()
    {
        return this._tableClause;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression getTableClause() 

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
    public void setOptionalAllOrDistinct(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption optionalAllOrDistinct)
    {
        this._optionalAllOrDistinct = optionalAllOrDistinct;
    } //-- void setOptionalAllOrDistinct(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionOption) 

    /**
     * Sets the value of field 'optionalTop'.
     * 
     * @param optionalTop the value of field 'optionalTop'.
     */
    public void setOptionalTop(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionLimit optionalTop)
    {
        this._optionalTop = optionalTop;
    } //-- void setOptionalTop(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionLimit) 

    /**
     * Sets the value of field 'orderBy'.
     * 
     * @param orderBy the value of field 'orderBy'.
     */
    public void setOrderBy(org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression orderBy)
    {
        this._orderBy = orderBy;
    } //-- void setOrderBy(org.astrogrid.datacenter.adql.generated.ogsadai.OrderExpression) 

    /**
     * Sets the value of field 'selection'.
     * 
     * @param selection the value of field 'selection'.
     */
    public void setSelection(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList selection)
    {
        this._selection = selection;
    } //-- void setSelection(org.astrogrid.datacenter.adql.generated.ogsadai.SelectionList) 

    /**
     * Sets the value of field 'tableClause'.
     * 
     * @param tableClause the value of field 'tableClause'.
     */
    public void setTableClause(org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression tableClause)
    {
        this._tableClause = tableClause;
    } //-- void setTableClause(org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression) 

    /**
     * Method unmarshalSelect
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.Select unmarshalSelect(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Select) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.Select.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Select unmarshalSelect(java.io.Reader) 

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
