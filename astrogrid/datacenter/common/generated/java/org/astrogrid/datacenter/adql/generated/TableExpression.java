/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TableExpression.java,v 1.1 2003/10/14 12:36:54 nw Exp $
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
 * Class TableExpression.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/10/14 12:36:54 $
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
    private org.astrogrid.datacenter.adql.generated.From _fromClause;

    /**
     * Field _whereClause
     */
    private org.astrogrid.datacenter.adql.generated.Where _whereClause;

    /**
     * Field _groupByClause
     */
    private org.astrogrid.datacenter.adql.generated.GroupBy _groupByClause;

    /**
     * Field _havingClause
     */
    private org.astrogrid.datacenter.adql.generated.Having _havingClause;


      //----------------/
     //- Constructors -/
    //----------------/

    public TableExpression() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.TableExpression()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'fromClause'.
     * 
     * @return the value of field 'fromClause'.
     */
    public org.astrogrid.datacenter.adql.generated.From getFromClause()
    {
        return this._fromClause;
    } //-- org.astrogrid.datacenter.adql.generated.From getFromClause() 

    /**
     * Returns the value of field 'groupByClause'.
     * 
     * @return the value of field 'groupByClause'.
     */
    public org.astrogrid.datacenter.adql.generated.GroupBy getGroupByClause()
    {
        return this._groupByClause;
    } //-- org.astrogrid.datacenter.adql.generated.GroupBy getGroupByClause() 

    /**
     * Returns the value of field 'havingClause'.
     * 
     * @return the value of field 'havingClause'.
     */
    public org.astrogrid.datacenter.adql.generated.Having getHavingClause()
    {
        return this._havingClause;
    } //-- org.astrogrid.datacenter.adql.generated.Having getHavingClause() 

    /**
     * Returns the value of field 'whereClause'.
     * 
     * @return the value of field 'whereClause'.
     */
    public org.astrogrid.datacenter.adql.generated.Where getWhereClause()
    {
        return this._whereClause;
    } //-- org.astrogrid.datacenter.adql.generated.Where getWhereClause() 

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
    public void setFromClause(org.astrogrid.datacenter.adql.generated.From fromClause)
    {
        this._fromClause = fromClause;
    } //-- void setFromClause(org.astrogrid.datacenter.adql.generated.From) 

    /**
     * Sets the value of field 'groupByClause'.
     * 
     * @param groupByClause the value of field 'groupByClause'.
     */
    public void setGroupByClause(org.astrogrid.datacenter.adql.generated.GroupBy groupByClause)
    {
        this._groupByClause = groupByClause;
    } //-- void setGroupByClause(org.astrogrid.datacenter.adql.generated.GroupBy) 

    /**
     * Sets the value of field 'havingClause'.
     * 
     * @param havingClause the value of field 'havingClause'.
     */
    public void setHavingClause(org.astrogrid.datacenter.adql.generated.Having havingClause)
    {
        this._havingClause = havingClause;
    } //-- void setHavingClause(org.astrogrid.datacenter.adql.generated.Having) 

    /**
     * Sets the value of field 'whereClause'.
     * 
     * @param whereClause the value of field 'whereClause'.
     */
    public void setWhereClause(org.astrogrid.datacenter.adql.generated.Where whereClause)
    {
        this._whereClause = whereClause;
    } //-- void setWhereClause(org.astrogrid.datacenter.adql.generated.Where) 

    /**
     * Method unmarshalTableExpression
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.TableExpression unmarshalTableExpression(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.TableExpression) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.TableExpression.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.TableExpression unmarshalTableExpression(java.io.Reader) 

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
