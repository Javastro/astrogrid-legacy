/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ActivityContainer.java,v 1.1 2004/08/10 21:09:29 pah Exp $
 */

package jes;

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
import org.astrogrid.workflow.beans.v1.AbstractActivity;

/**
 * Abstract base class of activities that contain other activities
 * 
 * @version $Revision: 1.1 $ $Date: 2004/08/10 21:09:29 $
 */
public class ActivityContainer extends jes.AbstractActivity 
implements Serializable
{
      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

//-- org.astrogrid.workflow.beans.v1.ActivityContainer()


      //-----------/
     //- Methods -/
    //-----------/


    /**
     * Method addActivity
     * 
     * @param vActivity
     */
    public void addActivity(jes.AbstractActivity vActivity)
        throws IndexOutOfBoundsException
    {
        _activityList.add(vActivity);
    }

 //-- void addActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

//-- void addActivity(int, org.astrogrid.workflow.beans.v1.AbstractActivity) 

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
    public Enumeration enumerateActivity()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_activityList.iterator());
    }

 //-- java.util.Enumeration enumerateActivity() 

//-- boolean equals(java.lang.Object) 

    /**
     * Method getActivity
     * 
     * @param index
     */
    public jes.AbstractActivity getActivity(int index)
        throws IndexOutOfBoundsException
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
    public jes.AbstractActivity[] getActivity()
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
    }

 //-- boolean isValid() 


//-- void marshal(java.io.Writer) 

//-- void marshal(org.xml.sax.ContentHandler) 

    /**
     * Method removeActivity
     * 
     * @param vActivity
     */
    public boolean removeActivity(jes.AbstractActivity vActivity)
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
    public void setActivity(int index, jes.AbstractActivity vActivity)
        throws IndexOutOfBoundsException
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
    public void setActivity(jes.AbstractActivity[] activityArray)
    {
        //-- copy array
        _activityList.clear();
        for (int i = 0; i < activityArray.length; i++) {
            _activityList.add(activityArray[i]);
        }
    }

 //-- void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 


//-- org.astrogrid.workflow.beans.v1.ActivityContainer unmarshalActivityContainer(java.io.Reader) 

//-- void validate() 

}
