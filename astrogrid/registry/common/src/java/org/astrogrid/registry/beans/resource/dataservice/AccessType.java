/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AccessType.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.AccessURLType;
import org.astrogrid.registry.beans.resource.dataservice.types.RightsType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AccessType.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class AccessType extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The physical or digital manifestation of the information
     * supported
     *  by a resource.
     *  
     */
    private java.util.ArrayList _formatList;

    /**
     * Information about rights held in and over the resource.
     *  
     */
    private org.astrogrid.registry.beans.resource.dataservice.types.RightsType _rights;

    /**
     * Specifically, this is a URL that can be used to
     *  download the data contained in the data collection in
     *  question. 
     *  
     */
    private org.astrogrid.registry.beans.resource.AccessURLType _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public AccessType() {
        super();
        _formatList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.AccessType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFormat
     * 
     * @param vFormat
     */
    public void addFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.add(vFormat);
    } //-- void addFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType) 

    /**
     * Method addFormat
     * 
     * @param index
     * @param vFormat
     */
    public void addFormat(int index, org.astrogrid.registry.beans.resource.dataservice.FormatType vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.add(index, vFormat);
    } //-- void addFormat(int, org.astrogrid.registry.beans.resource.dataservice.FormatType) 

    /**
     * Method clearFormat
     */
    public void clearFormat()
    {
        _formatList.clear();
    } //-- void clearFormat() 

    /**
     * Method enumerateFormat
     */
    public java.util.Enumeration enumerateFormat()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_formatList.iterator());
    } //-- java.util.Enumeration enumerateFormat() 

    /**
     * Returns the value of field 'accessURL'. The field
     * 'accessURL' has the following description: Specifically,
     * this is a URL that can be used to
     *  download the data contained in the data collection in
     *  question. 
     *  
     * 
     * @return the value of field 'accessURL'.
     */
    public org.astrogrid.registry.beans.resource.AccessURLType getAccessURL()
    {
        return this._accessURL;
    } //-- org.astrogrid.registry.beans.resource.AccessURLType getAccessURL() 

    /**
     * Method getFormat
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.dataservice.FormatType getFormat(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.dataservice.FormatType) _formatList.get(index);
    } //-- org.astrogrid.registry.beans.resource.dataservice.FormatType getFormat(int) 

    /**
     * Method getFormat
     */
    public org.astrogrid.registry.beans.resource.dataservice.FormatType[] getFormat()
    {
        int size = _formatList.size();
        org.astrogrid.registry.beans.resource.dataservice.FormatType[] mArray = new org.astrogrid.registry.beans.resource.dataservice.FormatType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.dataservice.FormatType) _formatList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.dataservice.FormatType[] getFormat() 

    /**
     * Method getFormatCount
     */
    public int getFormatCount()
    {
        return _formatList.size();
    } //-- int getFormatCount() 

    /**
     * Returns the value of field 'rights'. The field 'rights' has
     * the following description: Information about rights held in
     * and over the resource.
     *  
     * 
     * @return the value of field 'rights'.
     */
    public org.astrogrid.registry.beans.resource.dataservice.types.RightsType getRights()
    {
        return this._rights;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.RightsType getRights() 

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
     * Method removeFormat
     * 
     * @param vFormat
     */
    public boolean removeFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType vFormat)
    {
        boolean removed = _formatList.remove(vFormat);
        return removed;
    } //-- boolean removeFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType) 

    /**
     * Sets the value of field 'accessURL'. The field 'accessURL'
     * has the following description: Specifically, this is a URL
     * that can be used to
     *  download the data contained in the data collection in
     *  question. 
     *  
     * 
     * @param accessURL the value of field 'accessURL'.
     */
    public void setAccessURL(org.astrogrid.registry.beans.resource.AccessURLType accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(org.astrogrid.registry.beans.resource.AccessURLType) 

    /**
     * Method setFormat
     * 
     * @param index
     * @param vFormat
     */
    public void setFormat(int index, org.astrogrid.registry.beans.resource.dataservice.FormatType vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formatList.set(index, vFormat);
    } //-- void setFormat(int, org.astrogrid.registry.beans.resource.dataservice.FormatType) 

    /**
     * Method setFormat
     * 
     * @param formatArray
     */
    public void setFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType[] formatArray)
    {
        //-- copy array
        _formatList.clear();
        for (int i = 0; i < formatArray.length; i++) {
            _formatList.add(formatArray[i]);
        }
    } //-- void setFormat(org.astrogrid.registry.beans.resource.dataservice.FormatType) 

    /**
     * Sets the value of field 'rights'. The field 'rights' has the
     * following description: Information about rights held in and
     * over the resource.
     *  
     * 
     * @param rights the value of field 'rights'.
     */
    public void setRights(org.astrogrid.registry.beans.resource.dataservice.types.RightsType rights)
    {
        this._rights = rights;
    } //-- void setRights(org.astrogrid.registry.beans.resource.dataservice.types.RightsType) 

    /**
     * Method unmarshalAccessType
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.AccessType unmarshalAccessType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.AccessType) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.AccessType.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.AccessType unmarshalAccessType(java.io.Reader) 

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
