/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ClosedSearch.java,v 1.1 2003/08/28 15:27:54 nw Exp $
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
 * Class ClosedSearch.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/08/28 15:27:54 $
 */
public class ClosedSearch extends org.astrogrid.datacenter.adql.generated.Search 
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

    public ClosedSearch() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ClosedSearch()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Note: hashCode() has not been overriden
     * 
     * @param obj
     */
    public boolean equals(java.lang.Object obj)
    {
        if ( this == obj )
            return true;
        
        if (super.equals(obj)==false)
            return false;
        
        if (obj instanceof ClosedSearch) {
        
            ClosedSearch temp = (ClosedSearch)obj;
            if (this._condition != null) {
                if (temp._condition == null) return false;
                else if (!(this._condition.equals(temp._condition))) 
                    return false;
            }
            else if (temp._condition != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Method unmarshalClosedSearch
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ClosedSearch unmarshalClosedSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ClosedSearch) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ClosedSearch.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ClosedSearch unmarshalClosedSearch(java.io.Reader) 

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
