/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CommonExecutionConnectorConfigType.java,v 1.13 2004/03/10 13:58:29 pah Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * The base for a common execution connector configuration
 * 
 * @version $Revision: 1.13 $ $Date: 2004/03/10 13:58:29 $
 */
public class CommonExecutionConnectorConfigType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //----------------/
     //- Constructors -/
    //----------------/

    public CommonExecutionConnectorConfigType() {
        super();
    } //-- org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType()


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
        
        if (obj instanceof CommonExecutionConnectorConfigType) {
        
            CommonExecutionConnectorConfigType temp = (CommonExecutionConnectorConfigType)obj;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

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
     * Method unmarshalCommonExecutionConnectorConfigType
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType unmarshalCommonExecutionConnectorConfigType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType.class, reader);
    } //-- org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType unmarshalCommonExecutionConnectorConfigType(java.io.Reader) 

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
