/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Try.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
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
 * Error-handling construct. if an error occurs during execution of
 * the wrapped activity,
 *  the activity in the 'catch' block is executed.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class Try extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Head of the substitution group
     */
    private org.astrogrid.workflow.beans.v1.AbstractActivity _activity;

    /**
     * Action to take when an error occurs in the wrapped try block
     *  variable specified by 'var' attribute will contain details
     * of the error.
     *  
     */
    private org.astrogrid.workflow.beans.v1.Catch _catch;


      //----------------/
     //- Constructors -/
    //----------------/

    public Try() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Try()


      //-----------/
     //- Methods -/
    //-----------/

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
        
        if (obj instanceof Try) {
        
            Try temp = (Try)obj;
            if (this._activity != null) {
                if (temp._activity == null) return false;
                else if (!(this._activity.equals(temp._activity))) 
                    return false;
            }
            else if (temp._activity != null)
                return false;
            if (this._catch != null) {
                if (temp._catch == null) return false;
                else if (!(this._catch.equals(temp._catch))) 
                    return false;
            }
            else if (temp._catch != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'activity'. The field 'activity'
     * has the following description: Head of the substitution
     * group
     * 
     * @return the value of field 'activity'.
     */
    public org.astrogrid.workflow.beans.v1.AbstractActivity getActivity()
    {
        return this._activity;
    } //-- org.astrogrid.workflow.beans.v1.AbstractActivity getActivity() 

    /**
     * Returns the value of field 'catch'. The field 'catch' has
     * the following description: Action to take when an error
     * occurs in the wrapped try block
     *  variable specified by 'var' attribute will contain details
     * of the error.
     *  
     * 
     * @return the value of field 'catch'.
     */
    public org.astrogrid.workflow.beans.v1.Catch getCatch()
    {
        return this._catch;
    } //-- org.astrogrid.workflow.beans.v1.Catch getCatch() 

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
     * Sets the value of field 'activity'. The field 'activity' has
     * the following description: Head of the substitution group
     * 
     * @param activity the value of field 'activity'.
     */
    public void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity activity)
    {
        this._activity = activity;
    } //-- void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Sets the value of field 'catch'. The field 'catch' has the
     * following description: Action to take when an error occurs
     * in the wrapped try block
     *  variable specified by 'var' attribute will contain details
     * of the error.
     *  
     * 
     * @param _catch
     * @param catch the value of field 'catch'.
     */
    public void setCatch(org.astrogrid.workflow.beans.v1.Catch _catch)
    {
        this._catch = _catch;
    } //-- void setCatch(org.astrogrid.workflow.beans.v1.Catch) 

    /**
     * Method unmarshalTry
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Try unmarshalTry(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Try) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Try.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Try unmarshalTry(java.io.Reader) 

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
