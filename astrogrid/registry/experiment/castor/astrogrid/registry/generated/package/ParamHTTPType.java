/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: ParamHTTPType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.registry.generated.package.types.HTTPQueryType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class ParamHTTPType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class ParamHTTPType extends org.astrogrid.registry.generated.package.ExtendedInterfaceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _qtype
     */
    private org.astrogrid.registry.generated.package.types.HTTPQueryType _qtype;

    /**
     * The MIME type of a document returned by an HTTP Get.
     *  
     */
    private java.lang.String _HTTPResults;

    /**
     * an input parameter. This should be rendered
     *  as name=value in the query URL's arguements. 
     *  
     */
    private java.util.Vector _paramList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParamHTTPType() {
        super();
        _paramList = new Vector();
    } //-- org.astrogrid.registry.generated.package.ParamHTTPType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addParam
     * 
     * @param vParam
     */
    public void addParam(org.astrogrid.registry.generated.package.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.addElement(vParam);
    } //-- void addParam(org.astrogrid.registry.generated.package.Param) 

    /**
     * Method addParam
     * 
     * @param index
     * @param vParam
     */
    public void addParam(int index, org.astrogrid.registry.generated.package.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        _paramList.insertElementAt(vParam, index);
    } //-- void addParam(int, org.astrogrid.registry.generated.package.Param) 

    /**
     * Method enumerateParam
     */
    public java.util.Enumeration enumerateParam()
    {
        return _paramList.elements();
    } //-- java.util.Enumeration enumerateParam() 

    /**
     * Returns the value of field 'HTTPResults'. The field
     * 'HTTPResults' has the following description: The MIME type
     * of a document returned by an HTTP Get.
     *  
     * 
     * @return the value of field 'HTTPResults'.
     */
    public java.lang.String getHTTPResults()
    {
        return this._HTTPResults;
    } //-- java.lang.String getHTTPResults() 

    /**
     * Method getParam
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Param getParam(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Param) _paramList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Param getParam(int) 

    /**
     * Method getParam
     */
    public org.astrogrid.registry.generated.package.Param[] getParam()
    {
        int size = _paramList.size();
        org.astrogrid.registry.generated.package.Param[] mArray = new org.astrogrid.registry.generated.package.Param[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Param) _paramList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Param[] getParam() 

    /**
     * Method getParamCount
     */
    public int getParamCount()
    {
        return _paramList.size();
    } //-- int getParamCount() 

    /**
     * Returns the value of field 'qtype'.
     * 
     * @return the value of field 'qtype'.
     */
    public org.astrogrid.registry.generated.package.types.HTTPQueryType getQtype()
    {
        return this._qtype;
    } //-- org.astrogrid.registry.generated.package.types.HTTPQueryType getQtype() 

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
     * Method removeAllParam
     */
    public void removeAllParam()
    {
        _paramList.removeAllElements();
    } //-- void removeAllParam() 

    /**
     * Method removeParam
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Param removeParam(int index)
    {
        java.lang.Object obj = _paramList.elementAt(index);
        _paramList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Param) obj;
    } //-- org.astrogrid.registry.generated.package.Param removeParam(int) 

    /**
     * Sets the value of field 'HTTPResults'. The field
     * 'HTTPResults' has the following description: The MIME type
     * of a document returned by an HTTP Get.
     *  
     * 
     * @param HTTPResults the value of field 'HTTPResults'.
     */
    public void setHTTPResults(java.lang.String HTTPResults)
    {
        this._HTTPResults = HTTPResults;
    } //-- void setHTTPResults(java.lang.String) 

    /**
     * Method setParam
     * 
     * @param index
     * @param vParam
     */
    public void setParam(int index, org.astrogrid.registry.generated.package.Param vParam)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _paramList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _paramList.setElementAt(vParam, index);
    } //-- void setParam(int, org.astrogrid.registry.generated.package.Param) 

    /**
     * Method setParam
     * 
     * @param paramArray
     */
    public void setParam(org.astrogrid.registry.generated.package.Param[] paramArray)
    {
        //-- copy array
        _paramList.removeAllElements();
        for (int i = 0; i < paramArray.length; i++) {
            _paramList.addElement(paramArray[i]);
        }
    } //-- void setParam(org.astrogrid.registry.generated.package.Param) 

    /**
     * Sets the value of field 'qtype'.
     * 
     * @param qtype the value of field 'qtype'.
     */
    public void setQtype(org.astrogrid.registry.generated.package.types.HTTPQueryType qtype)
    {
        this._qtype = qtype;
    } //-- void setQtype(org.astrogrid.registry.generated.package.types.HTTPQueryType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.ParamHTTPType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.ParamHTTPType.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
