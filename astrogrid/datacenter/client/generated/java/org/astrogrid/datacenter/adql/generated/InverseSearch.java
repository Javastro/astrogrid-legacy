/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: InverseSearch.java,v 1.5 2003/11/19 18:44:51 nw Exp $
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
 * Class InverseSearch.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class InverseSearch extends org.astrogrid.datacenter.adql.generated.Search 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _condition
     */
    private org.astrogrid.datacenter.adql.generated.Search _condition;


      //----------------/
     //- Constructors -/
    //----------------/

    public InverseSearch() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.InverseSearch()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'condition'.
     * 
     * @return the value of field 'condition'.
     */
    public org.astrogrid.datacenter.adql.generated.Search getCondition()
    {
        return this._condition;
    } //-- org.astrogrid.datacenter.adql.generated.Search getCondition() 

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
     * Sets the value of field 'condition'.
     * 
     * @param condition the value of field 'condition'.
     */
    public void setCondition(org.astrogrid.datacenter.adql.generated.Search condition)
    {
        this._condition = condition;
    } //-- void setCondition(org.astrogrid.datacenter.adql.generated.Search) 

    /**
     * Method unmarshalInverseSearch
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.InverseSearch unmarshalInverseSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.InverseSearch) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.InverseSearch.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.InverseSearch unmarshalInverseSearch(java.io.Reader) 

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
