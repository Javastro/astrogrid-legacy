/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MakeQueryWithId.java,v 1.1 2003/11/18 14:22:14 nw Exp $
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
 * Class MakeQueryWithId.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/11/18 14:22:14 $
 */
public class MakeQueryWithId implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _query
     */
    private org.astrogrid.datacenter.axisdataserver.types.Query _query;

    /**
     * Field _assignedId
     */
    private java.lang.String _assignedId;


      //----------------/
     //- Constructors -/
    //----------------/

    public MakeQueryWithId() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'assignedId'.
     * 
     * @return the value of field 'assignedId'.
     */
    public java.lang.String getAssignedId()
    {
        return this._assignedId;
    } //-- java.lang.String getAssignedId() 

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
     * Sets the value of field 'assignedId'.
     * 
     * @param assignedId the value of field 'assignedId'.
     */
    public void setAssignedId(java.lang.String assignedId)
    {
        this._assignedId = assignedId;
    } //-- void setAssignedId(java.lang.String) 

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
     * Method unmarshalMakeQueryWithId
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId unmarshalMakeQueryWithId(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.MakeQueryWithId unmarshalMakeQueryWithId(java.io.Reader) 

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
