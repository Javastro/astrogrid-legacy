/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Job.java,v 1.2 2004/02/09 11:41:43 nw Exp $
 */

package org.astrogrid.jes.beans.v1;

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
 * Class Job.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/02/09 11:41:43 $
 */
public class Job implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _userid
     */
    private java.lang.String _userid;

    /**
     * Field _community
     */
    private java.lang.String _community;

    /**
     * Field _jobURN
     */
    private java.lang.String _jobURN;

    /**
     * Field _time
     */
    private java.lang.String _time;

    /**
     * Field _description
     */
    private java.lang.String _description;

    /**
     * Field _jobstepList
     */
    private java.util.Vector _jobstepList;


      //----------------/
     //- Constructors -/
    //----------------/

    public Job() {
        super();
        _jobstepList = new Vector();
    } //-- org.astrogrid.jes.beans.v1.Job()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addJobstep
     * 
     * @param vJobstep
     */
    public void addJobstep(org.astrogrid.jes.beans.v1.Jobstep vJobstep)
        throws java.lang.IndexOutOfBoundsException
    {
        _jobstepList.addElement(vJobstep);
    } //-- void addJobstep(org.astrogrid.jes.beans.v1.Jobstep) 

    /**
     * Method addJobstep
     * 
     * @param index
     * @param vJobstep
     */
    public void addJobstep(int index, org.astrogrid.jes.beans.v1.Jobstep vJobstep)
        throws java.lang.IndexOutOfBoundsException
    {
        _jobstepList.insertElementAt(vJobstep, index);
    } //-- void addJobstep(int, org.astrogrid.jes.beans.v1.Jobstep) 

    /**
     * Method enumerateJobstep
     */
    public java.util.Enumeration enumerateJobstep()
    {
        return _jobstepList.elements();
    } //-- java.util.Enumeration enumerateJobstep() 

    /**
     * Method getCommunityReturns the value of field 'community'.
     * 
     * @return the value of field 'community'.
     */
    public java.lang.String getCommunity()
    {
        return this._community;
    } //-- java.lang.String getCommunity() 

    /**
     * Method getDescriptionReturns the value of field
     * 'description'.
     * 
     * @return the value of field 'description'.
     */
    public java.lang.String getDescription()
    {
        return this._description;
    } //-- java.lang.String getDescription() 

    /**
     * Method getJobURNReturns the value of field 'jobURN'.
     * 
     * @return the value of field 'jobURN'.
     */
    public java.lang.String getJobURN()
    {
        return this._jobURN;
    } //-- java.lang.String getJobURN() 

    /**
     * Method getJobstep
     * 
     * @param index
     */
    public org.astrogrid.jes.beans.v1.Jobstep getJobstep(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _jobstepList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.jes.beans.v1.Jobstep) _jobstepList.elementAt(index);
    } //-- org.astrogrid.jes.beans.v1.Jobstep getJobstep(int) 

    /**
     * Method getJobstep
     */
    public org.astrogrid.jes.beans.v1.Jobstep[] getJobstep()
    {
        int size = _jobstepList.size();
        org.astrogrid.jes.beans.v1.Jobstep[] mArray = new org.astrogrid.jes.beans.v1.Jobstep[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.jes.beans.v1.Jobstep) _jobstepList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.jes.beans.v1.Jobstep[] getJobstep() 

    /**
     * Method getJobstepCount
     */
    public int getJobstepCount()
    {
        return _jobstepList.size();
    } //-- int getJobstepCount() 

    /**
     * Method getNameReturns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getTimeReturns the value of field 'time'.
     * 
     * @return the value of field 'time'.
     */
    public java.lang.String getTime()
    {
        return this._time;
    } //-- java.lang.String getTime() 

    /**
     * Method getUseridReturns the value of field 'userid'.
     * 
     * @return the value of field 'userid'.
     */
    public java.lang.String getUserid()
    {
        return this._userid;
    } //-- java.lang.String getUserid() 

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
     * Method removeAllJobstep
     */
    public void removeAllJobstep()
    {
        _jobstepList.removeAllElements();
    } //-- void removeAllJobstep() 

    /**
     * Method removeJobstep
     * 
     * @param index
     */
    public org.astrogrid.jes.beans.v1.Jobstep removeJobstep(int index)
    {
        java.lang.Object obj = _jobstepList.elementAt(index);
        _jobstepList.removeElementAt(index);
        return (org.astrogrid.jes.beans.v1.Jobstep) obj;
    } //-- org.astrogrid.jes.beans.v1.Jobstep removeJobstep(int) 

    /**
     * Method setCommunitySets the value of field 'community'.
     * 
     * @param community the value of field 'community'.
     */
    public void setCommunity(java.lang.String community)
    {
        this._community = community;
    } //-- void setCommunity(java.lang.String) 

    /**
     * Method setDescriptionSets the value of field 'description'.
     * 
     * @param description the value of field 'description'.
     */
    public void setDescription(java.lang.String description)
    {
        this._description = description;
    } //-- void setDescription(java.lang.String) 

    /**
     * Method setJobURNSets the value of field 'jobURN'.
     * 
     * @param jobURN the value of field 'jobURN'.
     */
    public void setJobURN(java.lang.String jobURN)
    {
        this._jobURN = jobURN;
    } //-- void setJobURN(java.lang.String) 

    /**
     * Method setJobstep
     * 
     * @param index
     * @param vJobstep
     */
    public void setJobstep(int index, org.astrogrid.jes.beans.v1.Jobstep vJobstep)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _jobstepList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _jobstepList.setElementAt(vJobstep, index);
    } //-- void setJobstep(int, org.astrogrid.jes.beans.v1.Jobstep) 

    /**
     * Method setJobstep
     * 
     * @param jobstepArray
     */
    public void setJobstep(org.astrogrid.jes.beans.v1.Jobstep[] jobstepArray)
    {
        //-- copy array
        _jobstepList.removeAllElements();
        for (int i = 0; i < jobstepArray.length; i++) {
            _jobstepList.addElement(jobstepArray[i]);
        }
    } //-- void setJobstep(org.astrogrid.jes.beans.v1.Jobstep) 

    /**
     * Method setNameSets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setTimeSets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(java.lang.String time)
    {
        this._time = time;
    } //-- void setTime(java.lang.String) 

    /**
     * Method setUseridSets the value of field 'userid'.
     * 
     * @param userid the value of field 'userid'.
     */
    public void setUserid(java.lang.String userid)
    {
        this._userid = userid;
    } //-- void setUserid(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static org.astrogrid.jes.beans.v1.Job unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.jes.beans.v1.Job) Unmarshaller.unmarshal(org.astrogrid.jes.beans.v1.Job.class, reader);
    } //-- org.astrogrid.jes.beans.v1.Job unmarshal(java.io.Reader) 

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
