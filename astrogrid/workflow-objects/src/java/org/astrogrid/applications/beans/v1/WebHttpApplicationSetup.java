/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WebHttpApplicationSetup.java,v 1.8 2007/01/04 16:26:34 clq2 Exp $
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
 * Description of an HTTP Application
 * 
 * @version $Revision: 1.8 $ $Date: 2007/01/04 16:26:34 $
 */
public class WebHttpApplicationSetup extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _URL
     */
    private org.astrogrid.applications.beans.v1.HttpURLType _URL;

    /**
     * Field _preProcessScript
     */
    private org.astrogrid.applications.beans.v1.Script _preProcessScript;

    /**
     * Field _postProcessScript
     */
    private org.astrogrid.applications.beans.v1.Script _postProcessScript;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebHttpApplicationSetup() {
        super();
    } //-- org.astrogrid.applications.beans.v1.WebHttpApplicationSetup()


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
        
        if (obj instanceof WebHttpApplicationSetup) {
        
            WebHttpApplicationSetup temp = (WebHttpApplicationSetup)obj;
            if (this._URL != null) {
                if (temp._URL == null) return false;
                else if (!(this._URL.equals(temp._URL))) 
                    return false;
            }
            else if (temp._URL != null)
                return false;
            if (this._preProcessScript != null) {
                if (temp._preProcessScript == null) return false;
                else if (!(this._preProcessScript.equals(temp._preProcessScript))) 
                    return false;
            }
            else if (temp._preProcessScript != null)
                return false;
            if (this._postProcessScript != null) {
                if (temp._postProcessScript == null) return false;
                else if (!(this._postProcessScript.equals(temp._postProcessScript))) 
                    return false;
            }
            else if (temp._postProcessScript != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'postProcessScript'.
     * 
     * @return the value of field 'postProcessScript'.
     */
    public org.astrogrid.applications.beans.v1.Script getPostProcessScript()
    {
        return this._postProcessScript;
    } //-- org.astrogrid.applications.beans.v1.Script getPostProcessScript() 

    /**
     * Returns the value of field 'preProcessScript'.
     * 
     * @return the value of field 'preProcessScript'.
     */
    public org.astrogrid.applications.beans.v1.Script getPreProcessScript()
    {
        return this._preProcessScript;
    } //-- org.astrogrid.applications.beans.v1.Script getPreProcessScript() 

    /**
     * Returns the value of field 'URL'.
     * 
     * @return the value of field 'URL'.
     */
    public org.astrogrid.applications.beans.v1.HttpURLType getURL()
    {
        return this._URL;
    } //-- org.astrogrid.applications.beans.v1.HttpURLType getURL() 

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
     * Sets the value of field 'postProcessScript'.
     * 
     * @param postProcessScript the value of field
     * 'postProcessScript'.
     */
    public void setPostProcessScript(org.astrogrid.applications.beans.v1.Script postProcessScript)
    {
        this._postProcessScript = postProcessScript;
    } //-- void setPostProcessScript(org.astrogrid.applications.beans.v1.Script) 

    /**
     * Sets the value of field 'preProcessScript'.
     * 
     * @param preProcessScript the value of field 'preProcessScript'
     */
    public void setPreProcessScript(org.astrogrid.applications.beans.v1.Script preProcessScript)
    {
        this._preProcessScript = preProcessScript;
    } //-- void setPreProcessScript(org.astrogrid.applications.beans.v1.Script) 

    /**
     * Sets the value of field 'URL'.
     * 
     * @param URL the value of field 'URL'.
     */
    public void setURL(org.astrogrid.applications.beans.v1.HttpURLType URL)
    {
        this._URL = URL;
    } //-- void setURL(org.astrogrid.applications.beans.v1.HttpURLType) 

    /**
     * Method unmarshalWebHttpApplicationSetup
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.WebHttpApplicationSetup unmarshalWebHttpApplicationSetup(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.WebHttpApplicationSetup) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.WebHttpApplicationSetup.class, reader);
    } //-- org.astrogrid.applications.beans.v1.WebHttpApplicationSetup unmarshalWebHttpApplicationSetup(java.io.Reader) 

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
