/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WebHttpApplication.java,v 1.2 2004/08/03 10:08:34 jdt Exp $
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
 * @version $Revision: 1.2 $ $Date: 2004/08/03 10:08:34 $
 */
public class WebHttpApplication extends org.astrogrid.applications.beans.v1.ApplicationBase 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _URL
     */
    private java.lang.String _URL;

    /**
     * Field _processInputsXSLT
     */
    private java.lang.String _processInputsXSLT;

    /**
     * Field _postprocessScript
     */
    private java.lang.String _postprocessScript;


      //----------------/
     //- Constructors -/
    //----------------/

    public WebHttpApplication() {
        super();
    } //-- org.astrogrid.applications.beans.v1.WebHttpApplication()


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
        
        if (obj instanceof WebHttpApplication) {
        
            WebHttpApplication temp = (WebHttpApplication)obj;
            if (this._URL != null) {
                if (temp._URL == null) return false;
                else if (!(this._URL.equals(temp._URL))) 
                    return false;
            }
            else if (temp._URL != null)
                return false;
            if (this._processInputsXSLT != null) {
                if (temp._processInputsXSLT == null) return false;
                else if (!(this._processInputsXSLT.equals(temp._processInputsXSLT))) 
                    return false;
            }
            else if (temp._processInputsXSLT != null)
                return false;
            if (this._postprocessScript != null) {
                if (temp._postprocessScript == null) return false;
                else if (!(this._postprocessScript.equals(temp._postprocessScript))) 
                    return false;
            }
            else if (temp._postprocessScript != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'postprocessScript'.
     * 
     * @return the value of field 'postprocessScript'.
     */
    public java.lang.String getPostprocessScript()
    {
        return this._postprocessScript;
    } //-- java.lang.String getPostprocessScript() 

    /**
     * Returns the value of field 'processInputsXSLT'.
     * 
     * @return the value of field 'processInputsXSLT'.
     */
    public java.lang.String getProcessInputsXSLT()
    {
        return this._processInputsXSLT;
    } //-- java.lang.String getProcessInputsXSLT() 

    /**
     * Returns the value of field 'URL'.
     * 
     * @return the value of field 'URL'.
     */
    public java.lang.String getURL()
    {
        return this._URL;
    } //-- java.lang.String getURL() 

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
     * Sets the value of field 'postprocessScript'.
     * 
     * @param postprocessScript the value of field
     * 'postprocessScript'.
     */
    public void setPostprocessScript(java.lang.String postprocessScript)
    {
        this._postprocessScript = postprocessScript;
    } //-- void setPostprocessScript(java.lang.String) 

    /**
     * Sets the value of field 'processInputsXSLT'.
     * 
     * @param processInputsXSLT the value of field
     * 'processInputsXSLT'.
     */
    public void setProcessInputsXSLT(java.lang.String processInputsXSLT)
    {
        this._processInputsXSLT = processInputsXSLT;
    } //-- void setProcessInputsXSLT(java.lang.String) 

    /**
     * Sets the value of field 'URL'.
     * 
     * @param URL the value of field 'URL'.
     */
    public void setURL(java.lang.String URL)
    {
        this._URL = URL;
    } //-- void setURL(java.lang.String) 

    /**
     * Method unmarshalWebHttpApplication
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.WebHttpApplication unmarshalWebHttpApplication(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.WebHttpApplication) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.WebHttpApplication.class, reader);
    } //-- org.astrogrid.applications.beans.v1.WebHttpApplication unmarshalWebHttpApplication(java.io.Reader) 

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
