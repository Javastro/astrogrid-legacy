/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ActivitySequence.java,v 1.5 2004/03/03 01:16:54 nw Exp $
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
 * Class ActivitySequence.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/03 01:16:54 $
 */
public class ActivitySequence extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _activityChoiceList
     */
    private java.util.ArrayList _activityChoiceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ActivitySequence() {
        super();
        _activityChoiceList = new ArrayList();
    } //-- org.astrogrid.workflow.beans.v1.ActivitySequence()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addActivityChoice
     * 
     * @param vActivityChoice
     */
    public void addActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice vActivityChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityChoiceList.add(vActivityChoice);
    } //-- void addActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice) 

    /**
     * Method addActivityChoice
     * 
     * @param index
     * @param vActivityChoice
     */
    public void addActivityChoice(int index, org.astrogrid.workflow.beans.v1.ActivityChoice vActivityChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _activityChoiceList.add(index, vActivityChoice);
    } //-- void addActivityChoice(int, org.astrogrid.workflow.beans.v1.ActivityChoice) 

    /**
     * Method clearActivityChoice
     */
    public void clearActivityChoice()
    {
        _activityChoiceList.clear();
    } //-- void clearActivityChoice() 

    /**
     * Method enumerateActivityChoice
     */
    public java.util.Enumeration enumerateActivityChoice()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_activityChoiceList.iterator());
    } //-- java.util.Enumeration enumerateActivityChoice() 

    /**
     * Method getActivityChoice
     * 
     * @param index
     */
    public org.astrogrid.workflow.beans.v1.ActivityChoice getActivityChoice(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.workflow.beans.v1.ActivityChoice) _activityChoiceList.get(index);
    } //-- org.astrogrid.workflow.beans.v1.ActivityChoice getActivityChoice(int) 

    /**
     * Method getActivityChoice
     */
    public org.astrogrid.workflow.beans.v1.ActivityChoice[] getActivityChoice()
    {
        int size = _activityChoiceList.size();
        org.astrogrid.workflow.beans.v1.ActivityChoice[] mArray = new org.astrogrid.workflow.beans.v1.ActivityChoice[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.workflow.beans.v1.ActivityChoice) _activityChoiceList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.workflow.beans.v1.ActivityChoice[] getActivityChoice() 

    /**
     * Method getActivityChoiceCount
     */
    public int getActivityChoiceCount()
    {
        return _activityChoiceList.size();
    } //-- int getActivityChoiceCount() 

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
     * Method removeActivityChoice
     * 
     * @param vActivityChoice
     */
    public boolean removeActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice vActivityChoice)
    {
        boolean removed = _activityChoiceList.remove(vActivityChoice);
        return removed;
    } //-- boolean removeActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice) 

    /**
     * Method setActivityChoice
     * 
     * @param index
     * @param vActivityChoice
     */
    public void setActivityChoice(int index, org.astrogrid.workflow.beans.v1.ActivityChoice vActivityChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _activityChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _activityChoiceList.set(index, vActivityChoice);
    } //-- void setActivityChoice(int, org.astrogrid.workflow.beans.v1.ActivityChoice) 

    /**
     * Method setActivityChoice
     * 
     * @param activityChoiceArray
     */
    public void setActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice[] activityChoiceArray)
    {
        //-- copy array
        _activityChoiceList.clear();
        for (int i = 0; i < activityChoiceArray.length; i++) {
            _activityChoiceList.add(activityChoiceArray[i]);
        }
    } //-- void setActivityChoice(org.astrogrid.workflow.beans.v1.ActivityChoice) 

    /**
     * Method unmarshalActivitySequence
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.ActivitySequence unmarshalActivitySequence(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.ActivitySequence) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.ActivitySequence.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.ActivitySequence unmarshalActivitySequence(java.io.Reader) 

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
