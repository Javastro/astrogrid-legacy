/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: IntersectionSearch.java,v 1.1 2003/09/10 13:01:26 nw Exp $
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
 * Class IntersectionSearch.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/09/10 13:01:26 $
 */
public class IntersectionSearch extends org.astrogrid.datacenter.adql.generated.Search 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _firstCondition
     */
    private org.astrogrid.datacenter.adql.generated.Search _firstCondition;

    /**
     * Field _secondCondition
     */
    private org.astrogrid.datacenter.adql.generated.Search _secondCondition;


      //----------------/
     //- Constructors -/
    //----------------/

    public IntersectionSearch() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.IntersectionSearch()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'firstCondition'.
     * 
     * @return the value of field 'firstCondition'.
     */
    public org.astrogrid.datacenter.adql.generated.Search getFirstCondition()
    {
        return this._firstCondition;
    } //-- org.astrogrid.datacenter.adql.generated.Search getFirstCondition() 

    /**
     * Returns the value of field 'secondCondition'.
     * 
     * @return the value of field 'secondCondition'.
     */
    public org.astrogrid.datacenter.adql.generated.Search getSecondCondition()
    {
        return this._secondCondition;
    } //-- org.astrogrid.datacenter.adql.generated.Search getSecondCondition() 

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
     * Sets the value of field 'firstCondition'.
     * 
     * @param firstCondition the value of field 'firstCondition'.
     */
    public void setFirstCondition(org.astrogrid.datacenter.adql.generated.Search firstCondition)
    {
        this._firstCondition = firstCondition;
    } //-- void setFirstCondition(org.astrogrid.datacenter.adql.generated.Search) 

    /**
     * Sets the value of field 'secondCondition'.
     * 
     * @param secondCondition the value of field 'secondCondition'.
     */
    public void setSecondCondition(org.astrogrid.datacenter.adql.generated.Search secondCondition)
    {
        this._secondCondition = secondCondition;
    } //-- void setSecondCondition(org.astrogrid.datacenter.adql.generated.Search) 

    /**
     * Method unmarshalIntersectionSearch
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.IntersectionSearch unmarshalIntersectionSearch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.IntersectionSearch) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.IntersectionSearch.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.IntersectionSearch unmarshalIntersectionSearch(java.io.Reader) 

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
