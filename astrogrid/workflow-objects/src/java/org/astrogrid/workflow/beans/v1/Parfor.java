/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Parfor.java,v 1.2 2004/07/09 09:35:53 nw Exp $
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
 * A parallel-for loop construct
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/09 09:35:53 $
 */
public class Parfor extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _var
     */
    private java.lang.String _var;

    /**
     * Field _range
     */
    private java.lang.String _range;

    /**
     * Field _scriptingLanguage
     */
    private java.lang.String _scriptingLanguage = "jython-2.1";

    /**
     * The head of the substitution group
     */
    private org.astrogrid.workflow.beans.v1.AbstractActivity _activity;


      //----------------/
     //- Constructors -/
    //----------------/

    public Parfor() {
        super();
        setScriptingLanguage("jython-2.1");
    } //-- org.astrogrid.workflow.beans.v1.Parfor()


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
        
        if (obj instanceof Parfor) {
        
            Parfor temp = (Parfor)obj;
            if (this._var != null) {
                if (temp._var == null) return false;
                else if (!(this._var.equals(temp._var))) 
                    return false;
            }
            else if (temp._var != null)
                return false;
            if (this._range != null) {
                if (temp._range == null) return false;
                else if (!(this._range.equals(temp._range))) 
                    return false;
            }
            else if (temp._range != null)
                return false;
            if (this._scriptingLanguage != null) {
                if (temp._scriptingLanguage == null) return false;
                else if (!(this._scriptingLanguage.equals(temp._scriptingLanguage))) 
                    return false;
            }
            else if (temp._scriptingLanguage != null)
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
     * has the following description: The head of the substitution
     * group
     * 
     * @return the value of field 'activity'.
     */
    public org.astrogrid.workflow.beans.v1.AbstractActivity getActivity()
    {
        return this._activity;
    } //-- org.astrogrid.workflow.beans.v1.AbstractActivity getActivity() 

    /**
     * Returns the value of field 'range'.
     * 
     * @return the value of field 'range'.
     */
    public java.lang.String getRange()
    {
        return this._range;
    } //-- java.lang.String getRange() 

    /**
     * Returns the value of field 'scriptingLanguage'.
     * 
     * @return the value of field 'scriptingLanguage'.
     */
    public java.lang.String getScriptingLanguage()
    {
        return this._scriptingLanguage;
    } //-- java.lang.String getScriptingLanguage() 

    /**
     * Returns the value of field 'var'.
     * 
     * @return the value of field 'var'.
     */
    public java.lang.String getVar()
    {
        return this._var;
    } //-- java.lang.String getVar() 

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
     * the following description: The head of the substitution
     * group
     * 
     * @param activity the value of field 'activity'.
     */
    public void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity activity)
    {
        this._activity = activity;
    } //-- void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Sets the value of field 'range'.
     * 
     * @param range the value of field 'range'.
     */
    public void setRange(java.lang.String range)
    {
        this._range = range;
    } //-- void setRange(java.lang.String) 

    /**
     * Sets the value of field 'scriptingLanguage'.
     * 
     * @param scriptingLanguage the value of field
     * 'scriptingLanguage'.
     */
    public void setScriptingLanguage(java.lang.String scriptingLanguage)
    {
        this._scriptingLanguage = scriptingLanguage;
    } //-- void setScriptingLanguage(java.lang.String) 

    /**
     * Sets the value of field 'var'.
     * 
     * @param var the value of field 'var'.
     */
    public void setVar(java.lang.String var)
    {
        this._var = var;
    } //-- void setVar(java.lang.String) 

    /**
     * Method unmarshalParfor
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Parfor unmarshalParfor(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Parfor) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Parfor.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Parfor unmarshalParfor(java.io.Reader) 

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
