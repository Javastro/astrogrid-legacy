/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: VODescription.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * A description of one or more VO Resources
 *  This element is used as a general container for multiple
 * resource
 *  descriptions and can be used as a root element.
 *  
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class VODescription implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Any entity that is describable and identifiable by a IVOA
     * Identifier.
     *  
     */
    private java.util.Vector _resourceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VODescription() {
        super();
        _resourceList = new Vector();
    } //-- org.astrogrid.registry.generated.package.VODescription()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addResource
     * 
     * @param vResource
     */
    public void addResource(org.astrogrid.registry.generated.package.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.addElement(vResource);
    } //-- void addResource(org.astrogrid.registry.generated.package.Resource) 

    /**
     * Method addResource
     * 
     * @param index
     * @param vResource
     */
    public void addResource(int index, org.astrogrid.registry.generated.package.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        _resourceList.insertElementAt(vResource, index);
    } //-- void addResource(int, org.astrogrid.registry.generated.package.Resource) 

    /**
     * Method enumerateResource
     */
    public java.util.Enumeration enumerateResource()
    {
        return _resourceList.elements();
    } //-- java.util.Enumeration enumerateResource() 

    /**
     * Method getResource
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Resource getResource(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Resource) _resourceList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Resource getResource(int) 

    /**
     * Method getResource
     */
    public org.astrogrid.registry.generated.package.Resource[] getResource()
    {
        int size = _resourceList.size();
        org.astrogrid.registry.generated.package.Resource[] mArray = new org.astrogrid.registry.generated.package.Resource[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Resource) _resourceList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Resource[] getResource() 

    /**
     * Method getResourceCount
     */
    public int getResourceCount()
    {
        return _resourceList.size();
    } //-- int getResourceCount() 

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
     * Method removeAllResource
     */
    public void removeAllResource()
    {
        _resourceList.removeAllElements();
    } //-- void removeAllResource() 

    /**
     * Method removeResource
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Resource removeResource(int index)
    {
        java.lang.Object obj = _resourceList.elementAt(index);
        _resourceList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Resource) obj;
    } //-- org.astrogrid.registry.generated.package.Resource removeResource(int) 

    /**
     * Method setResource
     * 
     * @param index
     * @param vResource
     */
    public void setResource(int index, org.astrogrid.registry.generated.package.Resource vResource)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _resourceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _resourceList.setElementAt(vResource, index);
    } //-- void setResource(int, org.astrogrid.registry.generated.package.Resource) 

    /**
     * Method setResource
     * 
     * @param resourceArray
     */
    public void setResource(org.astrogrid.registry.generated.package.Resource[] resourceArray)
    {
        //-- copy array
        _resourceList.removeAllElements();
        for (int i = 0; i < resourceArray.length; i++) {
            _resourceList.addElement(resourceArray[i]);
        }
    } //-- void setResource(org.astrogrid.registry.generated.package.Resource) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.VODescription) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.VODescription.class, reader);
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
