/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DoQuery.java,v 1.3 2003/11/19 18:44:51 nw Exp $
 */

package org.astrogrid.datacenter.axisdataserver.types;

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
 * Class DoQuery.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/11/19 18:44:51 $
 */
public class DoQuery implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _resultsFormat
     */
    private java.lang.String _resultsFormat;

    /**
     * Field _query
     */
    private org.astrogrid.datacenter.axisdataserver.types.Query _query;


      //----------------/
     //- Constructors -/
    //----------------/

    public DoQuery() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.DoQuery()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'query'.
     * 
     * @return the value of field 'query'.
     */
    public org.astrogrid.datacenter.axisdataserver.types.Query getQuery()
    {
        return this._query;
    } //-- org.astrogrid.datacenter.axisdataserver.types.Query getQuery() 

    /**
     * Returns the value of field 'resultsFormat'.
     * 
     * @return the value of field 'resultsFormat'.
     */
    public java.lang.String getResultsFormat()
    {
        return this._resultsFormat;
    } //-- java.lang.String getResultsFormat() 

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
     * Sets the value of field 'query'.
     * 
     * @param query the value of field 'query'.
     */
    public void setQuery(org.astrogrid.datacenter.axisdataserver.types.Query query)
    {
        this._query = query;
    } //-- void setQuery(org.astrogrid.datacenter.axisdataserver.types.Query) 

    /**
     * Sets the value of field 'resultsFormat'.
     * 
     * @param resultsFormat the value of field 'resultsFormat'.
     */
    public void setResultsFormat(java.lang.String resultsFormat)
    {
        this._resultsFormat = resultsFormat;
    } //-- void setResultsFormat(java.lang.String) 

    /**
     * Method unmarshalDoQuery
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.DoQuery unmarshalDoQuery(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.DoQuery) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.DoQuery.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.DoQuery unmarshalDoQuery(java.io.Reader) 

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
