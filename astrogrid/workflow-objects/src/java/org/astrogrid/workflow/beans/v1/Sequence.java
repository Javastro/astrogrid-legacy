/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Sequence.java,v 1.6 2004/03/03 19:05:19 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

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
 * a sequential sequence of activities
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/03 19:05:19 $
 */
public class Sequence extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The head of the substitution group
     */
    private java.util.ArrayList _activityList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sequence() {
        super();
        _activityList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.Sequence()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addActivity
     * 
     * @param vActivity
     */
    public void addActivity(org.astrogrid.workflow.beans.v1.AbstractActivity vActivity)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityList.add(vActivity);
    } //-- void addActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Method addActivity
     * 
     * @param index
     * @param vActivity
     */
    public void addActivity(int index, org.astrogrid.workflow.beans.v1.AbstractActivity vActivity)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityList.add(index, vActivity);
    } //-- void addActivity(int, org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Method clearActivity
     */
    public void clearActivity()
    {
        _activityList.clear();
    } //-- void clearActivity() 

    /**
     * Method enumerateActivity
     */
    public java.util.Enumeration enumerateActivity()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_activityList.iterator());
    } //-- java.util.Enumeration enumerateActivity() 

    /**
     * Method getActivity
     * 
     * @param index
     */
    public org.astrogrid.workflow.beans.v1.AbstractActivity getActivity(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.workflow.beans.v1.AbstractActivity) _activityList.get(index);
    } //-- org.astrogrid.workflow.beans.v1.AbstractActivity getActivity(int) 

    /**
     * Method getActivity
     */
    public org.astrogrid.workflow.beans.v1.AbstractActivity[] getActivity()
    {
        int size = _activityList.size();
        org.astrogrid.workflow.beans.v1.AbstractActivity[] mArray = new org.astrogrid.workflow.beans.v1.AbstractActivity[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.workflow.beans.v1.AbstractActivity) _activityList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.workflow.beans.v1.AbstractActivity[] getActivity() 

    /**
     * Method getActivityCount
     */
    public int getActivityCount()
    {
        return _activityList.size();
    } //-- int getActivityCount() 

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
     * Method removeActivity
     * 
     * @param vActivity
     */
    public boolean removeActivity(org.astrogrid.workflow.beans.v1.AbstractActivity vActivity)
    {
        boolean removed = _activityList.remove(vActivity);
        return removed;
    } //-- boolean removeActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Method setActivity
     * 
     * @param index
     * @param vActivity
     */
    public void setActivity(int index, org.astrogrid.workflow.beans.v1.AbstractActivity vActivity)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _activityList.set(index, vActivity);
    } //-- void setActivity(int, org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Method setActivity
     * 
     * @param activityArray
     */
    public void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity[] activityArray)
    {
        //-- copy array
        _activityList.clear();
        for (int i = 0; i < activityArray.length; i++) {
            _activityList.add(activityArray[i]);
        }
    } //-- void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Method unmarshalSequence
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Sequence unmarshalSequence(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Sequence) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Sequence.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Sequence unmarshalSequence(java.io.Reader) 

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
