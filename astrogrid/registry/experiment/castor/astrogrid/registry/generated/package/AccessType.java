/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: AccessType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
import org.astrogrid.registry.generated.package.types.RightsType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class AccessType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class AccessType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The physical or digital manifestation of the information
     * supported
     *  by a resource.
     *  
     */
    private java.util.Vector _formatList;

    /**
     * Information about rights held in and over the resource.
     *  
     */
    private org.astrogrid.registry.generated.package.types.RightsType _rights;

    /**
     * Specifically, this is a URL that can be used to
     *  download the data contained in the data collection in
     *  question. 
     *  
     */
    private AccessURL _accessURL;


      //----------------/
     //- Constructors -/
    //----------------/

    public AccessType() {
        super();
        _formatList = new Vector();
    } //-- org.astrogrid.registry.generated.package.AccessType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFormat
     * 
     * @param vFormat
     */
    public void addFormat(org.astrogrid.registry.generated.package.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.addElement(vFormat);
    } //-- void addFormat(org.astrogrid.registry.generated.package.Format) 

    /**
     * Method addFormat
     * 
     * @param index
     * @param vFormat
     */
    public void addFormat(int index, org.astrogrid.registry.generated.package.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        _formatList.insertElementAt(vFormat, index);
    } //-- void addFormat(int, org.astrogrid.registry.generated.package.Format) 

    /**
     * Method enumerateFormat
     */
    public java.util.Enumeration enumerateFormat()
    {
        return _formatList.elements();
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
    public AccessURL getAccessURL()
    {
        return this._accessURL;
    } //-- AccessURL getAccessURL() 

    /**
     * Method getFormat
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Format getFormat(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Format) _formatList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Format getFormat(int) 

    /**
     * Method getFormat
     */
    public org.astrogrid.registry.generated.package.Format[] getFormat()
    {
        int size = _formatList.size();
        org.astrogrid.registry.generated.package.Format[] mArray = new org.astrogrid.registry.generated.package.Format[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Format) _formatList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Format[] getFormat() 

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
    public org.astrogrid.registry.generated.package.types.RightsType getRights()
    {
        return this._rights;
    } //-- org.astrogrid.registry.generated.package.types.RightsType getRights() 

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
     * Method removeAllFormat
     */
    public void removeAllFormat()
    {
        _formatList.removeAllElements();
    } //-- void removeAllFormat() 

    /**
     * Method removeFormat
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Format removeFormat(int index)
    {
        java.lang.Object obj = _formatList.elementAt(index);
        _formatList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Format) obj;
    } //-- org.astrogrid.registry.generated.package.Format removeFormat(int) 

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
    public void setAccessURL(AccessURL accessURL)
    {
        this._accessURL = accessURL;
    } //-- void setAccessURL(AccessURL) 

    /**
     * Method setFormat
     * 
     * @param index
     * @param vFormat
     */
    public void setFormat(int index, org.astrogrid.registry.generated.package.Format vFormat)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _formatList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _formatList.setElementAt(vFormat, index);
    } //-- void setFormat(int, org.astrogrid.registry.generated.package.Format) 

    /**
     * Method setFormat
     * 
     * @param formatArray
     */
    public void setFormat(org.astrogrid.registry.generated.package.Format[] formatArray)
    {
        //-- copy array
        _formatList.removeAllElements();
        for (int i = 0; i < formatArray.length; i++) {
            _formatList.addElement(formatArray[i]);
        }
    } //-- void setFormat(org.astrogrid.registry.generated.package.Format) 

    /**
     * Sets the value of field 'rights'. The field 'rights' has the
     * following description: Information about rights held in and
     * over the resource.
     *  
     * 
     * @param rights the value of field 'rights'.
     */
    public void setRights(org.astrogrid.registry.generated.package.types.RightsType rights)
    {
        this._rights = rights;
    } //-- void setRights(org.astrogrid.registry.generated.package.types.RightsType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.AccessType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.AccessType.class, reader);
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
