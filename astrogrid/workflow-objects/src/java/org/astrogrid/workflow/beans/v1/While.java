/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: While.java,v 1.13 2007/01/04 16:26:26 clq2 Exp $
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
 * A while loop construct
 * 
 * @version $Revision: 1.13 $ $Date: 2007/01/04 16:26:26 $
 */
public class While extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the condition for the loop: an expression (in ${..}) that
     * evaluates to a boolean
     */
    private java.lang.String _test;

    /**
     * Head of the substitution group
     */
    private org.astrogrid.workflow.beans.v1.AbstractActivity _activity;


      //----------------/
     //- Constructors -/
    //----------------/

    public While() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.While()


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
        
        if (obj instanceof While) {
        
            While temp = (While)obj;
            if (this._test != null) {
                if (temp._test == null) return false;
                else if (!(this._test.equals(temp._test))) 
                    return false;
            }
            else if (temp._test != null)
                return false;
            if (this._activity != null) {
                if (temp._activity == null) return false;
                else if (!(this._activity.equals(temp._activity))) 
                    return false;
            }
            else if (temp._activity != null)
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
     * Returns the value of field 'test'. The field 'test' has the
     * following description: the condition for the loop: an
     * expression (in ${..}) that evaluates to a boolean
     * 
     * @return the value of field 'test'.
     */
    public java.lang.String getTest()
    {
        return this._test;
    } //-- java.lang.String getTest() 

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
     * Sets the value of field 'test'. The field 'test' has the
     * following description: the condition for the loop: an
     * expression (in ${..}) that evaluates to a boolean
     * 
     * @param test the value of field 'test'.
     */
    public void setTest(java.lang.String test)
    {
        this._test = test;
    } //-- void setTest(java.lang.String) 

    /**
     * Method unmarshalWhile
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.While unmarshalWhile(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.While) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.While.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.While unmarshalWhile(java.io.Reader) 

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
