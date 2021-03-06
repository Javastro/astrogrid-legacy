/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WebServiceExecutionControllerConfig.java,v 1.2 2007/01/04 16:26:21 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.cea.implementation;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class WebServiceExecutionControllerConfig.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:21 $
 */
public class WebServiceExecutionControllerConfig extends org.astrogrid.applications.beans.v1.cea.implementation.CommonExecutionConnectorConfigType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _webServiceList
     */
    private java.util.ArrayList _webServiceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebServiceExecutionControllerConfig() {
        super();
        _webServiceList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.WebServiceExecutionControllerConfig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWebService
     * 
     * @param vWebService
     */
    public void addWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        _webServiceList.add(vWebService);
    } //-- void addWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) 

    /**
     * Method addWebService
     * 
     * @param index
     * @param vWebService
     */
    public void addWebService(int index, org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        _webServiceList.add(index, vWebService);
    } //-- void addWebService(int, org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) 

    /**
     * Method clearWebService
     */
    public void clearWebService()
    {
        _webServiceList.clear();
    } //-- void clearWebService() 

    /**
     * Method enumerateWebService
     */
    public java.util.Enumeration enumerateWebService()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_webServiceList.iterator());
    } //-- java.util.Enumeration enumerateWebService() 

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
        
        if (obj instanceof WebServiceExecutionControllerConfig) {
        
            WebServiceExecutionControllerConfig temp = (WebServiceExecutionControllerConfig)obj;
            if (this._webServiceList != null) {
                if (temp._webServiceList == null) return false;
                else if (!(this._webServiceList.equals(temp._webServiceList))) 
                    return false;
            }
            else if (temp._webServiceList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getWebService
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication getWebService(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _webServiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) _webServiceList.get(index);
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication getWebService(int) 

    /**
     * Method getWebService
     */
    public org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication[] getWebService()
    {
        int size = _webServiceList.size();
        org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication[] mArray = new org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) _webServiceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication[] getWebService() 

    /**
     * Method getWebServiceCount
     */
    public int getWebServiceCount()
    {
        return _webServiceList.size();
    } //-- int getWebServiceCount() 

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
     * Method removeWebService
     * 
     * @param vWebService
     */
    public boolean removeWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication vWebService)
    {
        boolean removed = _webServiceList.remove(vWebService);
        return removed;
    } //-- boolean removeWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) 

    /**
     * Method setWebService
     * 
     * @param index
     * @param vWebService
     */
    public void setWebService(int index, org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _webServiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _webServiceList.set(index, vWebService);
    } //-- void setWebService(int, org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) 

    /**
     * Method setWebService
     * 
     * @param webServiceArray
     */
    public void setWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication[] webServiceArray)
    {
        //-- copy array
        _webServiceList.clear();
        for (int i = 0; i < webServiceArray.length; i++) {
            _webServiceList.add(webServiceArray[i]);
        }
    } //-- void setWebService(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceApplication) 

    /**
     * Method unmarshalWebServiceExecutionControllerConfig
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.cea.implementation.WebServiceExecutionControllerConfig unmarshalWebServiceExecutionControllerConfig(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.cea.implementation.WebServiceExecutionControllerConfig) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.cea.implementation.WebServiceExecutionControllerConfig.class, reader);
    } //-- org.astrogrid.applications.beans.v1.cea.implementation.WebServiceExecutionControllerConfig unmarshalWebServiceExecutionControllerConfig(java.io.Reader) 

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
