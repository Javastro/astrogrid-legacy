/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Script.java,v 1.2 2005/07/05 08:26:57 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.applications.beans.v1.types.ScriptingLanguage;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * a snippet of code to massage the inputs and outputs
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:57 $
 */
public class Script extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _lang
     */
    private org.astrogrid.applications.beans.v1.types.ScriptingLanguage _lang;

    /**
     * Field _code
     */
    private java.lang.String _code;


      //----------------/
     //- Constructors -/
    //----------------/

    public Script() {
        super();
    } //-- org.astrogrid.applications.beans.v1.Script()


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
        
        if (obj instanceof Script) {
        
            Script temp = (Script)obj;
            if (this._lang != null) {
                if (temp._lang == null) return false;
                else if (!(this._lang.equals(temp._lang))) 
                    return false;
            }
            else if (temp._lang != null)
                return false;
            if (this._code != null) {
                if (temp._code == null) return false;
                else if (!(this._code.equals(temp._code))) 
                    return false;
            }
            else if (temp._code != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'code'.
     * 
     * @return the value of field 'code'.
     */
    public java.lang.String getCode()
    {
        return this._code;
    } //-- java.lang.String getCode() 

    /**
     * Returns the value of field 'lang'.
     * 
     * @return the value of field 'lang'.
     */
    public org.astrogrid.applications.beans.v1.types.ScriptingLanguage getLang()
    {
        return this._lang;
    } //-- org.astrogrid.applications.beans.v1.types.ScriptingLanguage getLang() 

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
     * Sets the value of field 'code'.
     * 
     * @param code the value of field 'code'.
     */
    public void setCode(java.lang.String code)
    {
        this._code = code;
    } //-- void setCode(java.lang.String) 

    /**
     * Sets the value of field 'lang'.
     * 
     * @param lang the value of field 'lang'.
     */
    public void setLang(org.astrogrid.applications.beans.v1.types.ScriptingLanguage lang)
    {
        this._lang = lang;
    } //-- void setLang(org.astrogrid.applications.beans.v1.types.ScriptingLanguage) 

    /**
     * Method unmarshalScript
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.Script unmarshalScript(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.Script) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.Script.class, reader);
    } //-- org.astrogrid.applications.beans.v1.Script unmarshalScript(java.io.Reader) 

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
