/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationList.java,v 1.3 2004/03/10 13:58:28 pah Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * list of applications
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/10 13:58:28 $
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
    } //-- org.astrogrid.applications.beans.v1.ApplicationList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addApplicationDefn
     * 
     * @param vApplicationDefn
     */
    public void addApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationDefnList.add(vApplicationDefn);
    } //-- void addApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase) 

    /**
     * Method addApplicationDefn
     * 
     * @param index
     * @param vApplicationDefn
     */
    public void addApplicationDefn(int index, org.astrogrid.applications.beans.v1.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        _applicationDefnList.add(index, vApplicationDefn);
    } //-- void addApplicationDefn(int, org.astrogrid.applications.beans.v1.ApplicationBase) 

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
        
        if (obj instanceof ApplicationList) {
        
            ApplicationList temp = (ApplicationList)obj;
            if (this._applicationDefnList != null) {
                if (temp._applicationDefnList == null) return false;
                else if (!(this._applicationDefnList.equals(temp._applicationDefnList))) 
                    return false;
            }
            else if (temp._applicationDefnList != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Method getApplicationDefn
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.ApplicationBase getApplicationDefn(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationDefnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.ApplicationBase) _applicationDefnList.get(index);
    } //-- org.astrogrid.applications.beans.v1.ApplicationBase getApplicationDefn(int) 

    /**
     * Method getApplicationDefn
     */
    public org.astrogrid.applications.beans.v1.ApplicationBase[] getApplicationDefn()
    {
        int size = _applicationDefnList.size();
        org.astrogrid.applications.beans.v1.ApplicationBase[] mArray = new org.astrogrid.applications.beans.v1.ApplicationBase[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.ApplicationBase) _applicationDefnList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.ApplicationBase[] getApplicationDefn() 

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
    public boolean removeApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase vApplicationDefn)
    {
        boolean removed = _applicationDefnList.remove(vApplicationDefn);
        return removed;
    } //-- boolean removeApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase) 

    /**
     * Method setApplicationDefn
     * 
     * @param index
     * @param vApplicationDefn
     */
    public void setApplicationDefn(int index, org.astrogrid.applications.beans.v1.ApplicationBase vApplicationDefn)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _applicationDefnList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _applicationDefnList.set(index, vApplicationDefn);
    } //-- void setApplicationDefn(int, org.astrogrid.applications.beans.v1.ApplicationBase) 

    /**
     * Method setApplicationDefn
     * 
     * @param applicationDefnArray
     */
    public void setApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase[] applicationDefnArray)
    {
        //-- copy array
        _applicationDefnList.clear();
        for (int i = 0; i < applicationDefnArray.length; i++) {
            _applicationDefnList.add(applicationDefnArray[i]);
        }
    } //-- void setApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase) 

    /**
     * Method unmarshalApplicationList
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.ApplicationList unmarshalApplicationList(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.ApplicationList) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.ApplicationList.class, reader);
    } //-- org.astrogrid.applications.beans.v1.ApplicationList unmarshalApplicationList(java.io.Reader) 

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
