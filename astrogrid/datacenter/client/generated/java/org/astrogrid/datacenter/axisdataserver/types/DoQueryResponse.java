/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DoQueryResponse.java,v 1.1 2003/11/18 14:22:14 nw Exp $
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
 * Class DoQueryResponse.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/11/18 14:22:14 $
 */
public class DoQueryResponse implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _doQueryReturn
     */
    private java.lang.String _doQueryReturn;


      //----------------/
     //- Constructors -/
    //----------------/

    public DoQueryResponse() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'doQueryReturn'.
     * 
     * @return the value of field 'doQueryReturn'.
     */
    public java.lang.String getDoQueryReturn()
    {
        return this._doQueryReturn;
    } //-- java.lang.String getDoQueryReturn() 

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
     * Sets the value of field 'doQueryReturn'.
     * 
     * @param doQueryReturn the value of field 'doQueryReturn'.
     */
    public void setDoQueryReturn(java.lang.String doQueryReturn)
    {
        this._doQueryReturn = doQueryReturn;
    } //-- void setDoQueryReturn(java.lang.String) 

    /**
     * Method unmarshalDoQueryResponse
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse unmarshalDoQueryResponse(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.DoQueryResponse unmarshalDoQueryResponse(java.io.Reader) 

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
