/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WebServiceExecutionControllerConfig.java,v 1.4 2004/04/05 14:36:12 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * @version $Revision: 1.4 $ $Date: 2004/04/05 14:36:12 $
 */
public class WebServiceExecutionControllerConfig extends org.astrogrid.registry.beans.cea.base.CommonExecutionConnectorConfigType 
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
    } //-- org.astrogrid.registry.beans.cea.base.WebServiceExecutionControllerConfig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addWebService
     * 
     * @param vWebService
     */
    public void addWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        _webServiceList.add(vWebService);
    } //-- void addWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication) 

    /**
     * Method addWebService
     * 
     * @param index
     * @param vWebService
     */
    public void addWebService(int index, org.astrogrid.registry.beans.cea.base.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        _webServiceList.add(index, vWebService);
    } //-- void addWebService(int, org.astrogrid.registry.beans.cea.base.WebServiceApplication) 

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
     * Method getWebService
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.cea.base.WebServiceApplication getWebService(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _webServiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.cea.base.WebServiceApplication) _webServiceList.get(index);
    } //-- org.astrogrid.registry.beans.cea.base.WebServiceApplication getWebService(int) 

    /**
     * Method getWebService
     */
    public org.astrogrid.registry.beans.cea.base.WebServiceApplication[] getWebService()
    {
        int size = _webServiceList.size();
        org.astrogrid.registry.beans.cea.base.WebServiceApplication[] mArray = new org.astrogrid.registry.beans.cea.base.WebServiceApplication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.cea.base.WebServiceApplication) _webServiceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.cea.base.WebServiceApplication[] getWebService() 

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
    public boolean removeWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication vWebService)
    {
        boolean removed = _webServiceList.remove(vWebService);
        return removed;
    } //-- boolean removeWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication) 

    /**
     * Method setWebService
     * 
     * @param index
     * @param vWebService
     */
    public void setWebService(int index, org.astrogrid.registry.beans.cea.base.WebServiceApplication vWebService)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _webServiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _webServiceList.set(index, vWebService);
    } //-- void setWebService(int, org.astrogrid.registry.beans.cea.base.WebServiceApplication) 

    /**
     * Method setWebService
     * 
     * @param webServiceArray
     */
    public void setWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication[] webServiceArray)
    {
        //-- copy array
        _webServiceList.clear();
        for (int i = 0; i < webServiceArray.length; i++) {
            _webServiceList.add(webServiceArray[i]);
        }
    } //-- void setWebService(org.astrogrid.registry.beans.cea.base.WebServiceApplication) 

    /**
     * Method unmarshalWebServiceExecutionControllerConfig
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.WebServiceExecutionControllerConfig unmarshalWebServiceExecutionControllerConfig(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.WebServiceExecutionControllerConfig) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.WebServiceExecutionControllerConfig.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.WebServiceExecutionControllerConfig unmarshalWebServiceExecutionControllerConfig(java.io.Reader) 

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
