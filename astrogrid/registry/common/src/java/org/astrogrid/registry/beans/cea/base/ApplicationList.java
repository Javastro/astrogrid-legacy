/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationList.java,v 1.4 2004/04/05 14:36:12 KevinBenson Exp $
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
 * Class ApplicationList.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/04/05 14:36:12 $
 */
public class ApplicationList extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * This is a generic application definition that does not take
     * in all of the specializations for web/commandline etc
     */
    private java.util.ArrayList _applicationDefnList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationList() {
        super();
        _applicationDefnList = new ArrayList();
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationDefn
     * 
     * @param vApplicationDefn
     */
    public void addApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationDefnList.add(vApplicationDefn);
    } //-- void addApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase) 

    /**
     * Method addApplicationDefn
     * 
     * @param index
     * @param vApplicationDefn
     */
    public void addApplicationDefn(int index, org.astrogrid.registry.beans.cea.base.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationDefnList.add(index, vApplicationDefn);
    } //-- void addApplicationDefn(int, org.astrogrid.registry.beans.cea.base.ApplicationBase) 

    /**
     * Method clearApplicationDefn
     */
    public void clearApplicationDefn()
    {
        _applicationDefnList.clear();
    } //-- void clearApplicationDefn() 

    /**
     * Method enumerateApplicationDefn
     */
    public java.util.Enumeration enumerateApplicationDefn()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_applicationDefnList.iterator());
    } //-- java.util.Enumeration enumerateApplicationDefn() 

    /**
     * Method getApplicationDefn
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.cea.base.ApplicationBase getApplicationDefn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationDefnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.cea.base.ApplicationBase) _applicationDefnList.get(index);
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationBase getApplicationDefn(int) 

    /**
     * Method getApplicationDefn
     */
    public org.astrogrid.registry.beans.cea.base.ApplicationBase[] getApplicationDefn()
    {
        int size = _applicationDefnList.size();
        org.astrogrid.registry.beans.cea.base.ApplicationBase[] mArray = new org.astrogrid.registry.beans.cea.base.ApplicationBase[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.cea.base.ApplicationBase) _applicationDefnList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationBase[] getApplicationDefn() 

    /**
     * Method getApplicationDefnCount
     */
    public int getApplicationDefnCount()
    {
        return _applicationDefnList.size();
    } //-- int getApplicationDefnCount() 

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
     * Method removeApplicationDefn
     * 
     * @param vApplicationDefn
     */
    public boolean removeApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase vApplicationDefn)
    {
        boolean removed = _applicationDefnList.remove(vApplicationDefn);
        return removed;
    } //-- boolean removeApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase) 

    /**
     * Method setApplicationDefn
     * 
     * @param index
     * @param vApplicationDefn
     */
    public void setApplicationDefn(int index, org.astrogrid.registry.beans.cea.base.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationDefnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _applicationDefnList.set(index, vApplicationDefn);
    } //-- void setApplicationDefn(int, org.astrogrid.registry.beans.cea.base.ApplicationBase) 

    /**
     * Method setApplicationDefn
     * 
     * @param applicationDefnArray
     */
    public void setApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase[] applicationDefnArray)
    {
        //-- copy array
        _applicationDefnList.clear();
        for (int i = 0; i < applicationDefnArray.length; i++) {
            _applicationDefnList.add(applicationDefnArray[i]);
        }
    } //-- void setApplicationDefn(org.astrogrid.registry.beans.cea.base.ApplicationBase) 

    /**
     * Method unmarshalApplicationList
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.ApplicationList unmarshalApplicationList(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.ApplicationList) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.ApplicationList.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.ApplicationList unmarshalApplicationList(java.io.Reader) 

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
