/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationList.java,v 1.2 2004/03/09 14:35:58 pah Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * list of applications
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/09 14:35:58 $
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
    private org.astrogrid.applications.beans.v1.ApplicationBase _applicationDefn;


      //----------------/
     //- Constructors -/
    //----------------/

    public ApplicationList() {
        super();
    } //-- org.astrogrid.applications.beans.v1.ApplicationList()


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
        
        if (obj instanceof ApplicationList) {
        
            ApplicationList temp = (ApplicationList)obj;
            if (this._applicationDefn != null) {
                if (temp._applicationDefn == null) return false;
                else if (!(this._applicationDefn.equals(temp._applicationDefn))) 
                    return false;
            }
            else if (temp._applicationDefn != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'applicationDefn'. The field
     * 'applicationDefn' has the following description: This is a
     * generic application definition that does not take in all of
     * the specializations for web/commandline etc
     * 
     * @return the value of field 'applicationDefn'.
     */
    public org.astrogrid.applications.beans.v1.ApplicationBase getApplicationDefn()
    {
        return this._applicationDefn;
    } //-- org.astrogrid.applications.beans.v1.ApplicationBase getApplicationDefn() 

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
     * Sets the value of field 'applicationDefn'. The field
     * 'applicationDefn' has the following description: This is a
     * generic application definition that does not take in all of
     * the specializations for web/commandline etc
     * 
     * @param applicationDefn the value of field 'applicationDefn'.
     */
    public void setApplicationDefn(org.astrogrid.applications.beans.v1.ApplicationBase applicationDefn)
    {
        this._applicationDefn = applicationDefn;
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
