/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Sequence.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.workflow.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Sequence.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class Sequence extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _activitySequence
     */
    private org.astrogrid.workflow.beans.v1.ActivitySequence _activitySequence;


      //----------------/
     //- Constructors -/
    //----------------/

    public Sequence() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Sequence()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'activitySequence'.
     * 
     * @return the value of field 'activitySequence'.
     */
    public org.astrogrid.workflow.beans.v1.ActivitySequence getActivitySequence()
    {
        return this._activitySequence;
    } //-- org.astrogrid.workflow.beans.v1.ActivitySequence getActivitySequence() 

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
     * Sets the value of field 'activitySequence'.
     * 
     * @param activitySequence the value of field 'activitySequence'
     */
    public void setActivitySequence(org.astrogrid.workflow.beans.v1.ActivitySequence activitySequence)
    {
        this._activitySequence = activitySequence;
    } //-- void setActivitySequence(org.astrogrid.workflow.beans.v1.ActivitySequence) 

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
