/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TableExpression.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class TableExpression.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class TableExpression extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _fromClause
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.From _fromClause;

    /**
     * Field _whereClause
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Where _whereClause;

    /**
     * Field _groupByClause
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy _groupByClause;

    /**
     * Field _havingClause
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Having _havingClause;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableExpression() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fromClause'.
     * 
     * @return the value of field 'fromClause'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.From getFromClause()
    {
        return this._fromClause;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.From getFromClause() 

    /**
     * Returns the value of field 'groupByClause'.
     * 
     * @return the value of field 'groupByClause'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy getGroupByClause()
    {
        return this._groupByClause;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy getGroupByClause() 

    /**
     * Returns the value of field 'havingClause'.
     * 
     * @return the value of field 'havingClause'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Having getHavingClause()
    {
        return this._havingClause;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Having getHavingClause() 

    /**
     * Returns the value of field 'whereClause'.
     * 
     * @return the value of field 'whereClause'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Where getWhereClause()
    {
        return this._whereClause;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Where getWhereClause() 

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
     * Sets the value of field 'fromClause'.
     * 
     * @param fromClause the value of field 'fromClause'.
     */
    public void setFromClause(org.astrogrid.datacenter.adql.generated.ogsadai.From fromClause)
    {
        this._fromClause = fromClause;
    } //-- void setFromClause(org.astrogrid.datacenter.adql.generated.ogsadai.From) 

    /**
     * Sets the value of field 'groupByClause'.
     * 
     * @param groupByClause the value of field 'groupByClause'.
     */
    public void setGroupByClause(org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy groupByClause)
    {
        this._groupByClause = groupByClause;
    } //-- void setGroupByClause(org.astrogrid.datacenter.adql.generated.ogsadai.GroupBy) 

    /**
     * Sets the value of field 'havingClause'.
     * 
     * @param havingClause the value of field 'havingClause'.
     */
    public void setHavingClause(org.astrogrid.datacenter.adql.generated.ogsadai.Having havingClause)
    {
        this._havingClause = havingClause;
    } //-- void setHavingClause(org.astrogrid.datacenter.adql.generated.ogsadai.Having) 

    /**
     * Sets the value of field 'whereClause'.
     * 
     * @param whereClause the value of field 'whereClause'.
     */
    public void setWhereClause(org.astrogrid.datacenter.adql.generated.ogsadai.Where whereClause)
    {
        this._whereClause = whereClause;
    } //-- void setWhereClause(org.astrogrid.datacenter.adql.generated.ogsadai.Where) 

    /**
     * Method unmarshalTableExpression
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression unmarshalTableExpression(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.TableExpression unmarshalTableExpression(java.io.Reader) 

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
