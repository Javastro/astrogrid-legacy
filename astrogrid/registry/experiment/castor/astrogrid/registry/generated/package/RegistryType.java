/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: RegistryType.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
 * Class RegistryType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class RegistryType extends ServiceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * an authority identifier managed by a registry.
     *  
     */
    private java.util.Vector _managedAuthorityList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RegistryType() {
        super();
        _managedAuthorityList = new Vector();
    } //-- org.astrogrid.registry.generated.package.RegistryType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addManagedAuthority
     * 
     * @param vManagedAuthority
     */
    public void addManagedAuthority(java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedAuthorityList.addElement(vManagedAuthority);
    } //-- void addManagedAuthority(java.lang.String) 

    /**
     * Method addManagedAuthority
     * 
     * @param index
     * @param vManagedAuthority
     */
    public void addManagedAuthority(int index, java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        _managedAuthorityList.insertElementAt(vManagedAuthority, index);
    } //-- void addManagedAuthority(int, java.lang.String) 

    /**
     * Method enumerateManagedAuthority
     */
    public java.util.Enumeration enumerateManagedAuthority()
    {
        return _managedAuthorityList.elements();
    } //-- java.util.Enumeration enumerateManagedAuthority() 

    /**
     * Method getManagedAuthority
     * 
     * @param index
     */
    public java.lang.String getManagedAuthority(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedAuthorityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (String)_managedAuthorityList.elementAt(index);
    } //-- java.lang.String getManagedAuthority(int) 

    /**
     * Method getManagedAuthority
     */
    public java.lang.String[] getManagedAuthority()
    {
        int size = _managedAuthorityList.size();
        java.lang.String[] mArray = new java.lang.String[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (String)_managedAuthorityList.elementAt(index);
        }
        return mArray;
    } //-- java.lang.String[] getManagedAuthority() 

    /**
     * Method getManagedAuthorityCount
     */
    public int getManagedAuthorityCount()
    {
        return _managedAuthorityList.size();
    } //-- int getManagedAuthorityCount() 

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
     * Method removeAllManagedAuthority
     */
    public void removeAllManagedAuthority()
    {
        _managedAuthorityList.removeAllElements();
    } //-- void removeAllManagedAuthority() 

    /**
     * Method removeManagedAuthority
     * 
     * @param index
     */
    public java.lang.String removeManagedAuthority(int index)
    {
        java.lang.Object obj = _managedAuthorityList.elementAt(index);
        _managedAuthorityList.removeElementAt(index);
        return (String)obj;
    } //-- java.lang.String removeManagedAuthority(int) 

    /**
     * Method setManagedAuthority
     * 
     * @param index
     * @param vManagedAuthority
     */
    public void setManagedAuthority(int index, java.lang.String vManagedAuthority)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _managedAuthorityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _managedAuthorityList.setElementAt(vManagedAuthority, index);
    } //-- void setManagedAuthority(int, java.lang.String) 

    /**
     * Method setManagedAuthority
     * 
     * @param managedAuthorityArray
     */
    public void setManagedAuthority(java.lang.String[] managedAuthorityArray)
    {
        //-- copy array
        _managedAuthorityList.removeAllElements();
        for (int i = 0; i < managedAuthorityArray.length; i++) {
            _managedAuthorityList.addElement(managedAuthorityArray[i]);
        }
    } //-- void setManagedAuthority(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.RegistryType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.RegistryType.class, reader);
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
