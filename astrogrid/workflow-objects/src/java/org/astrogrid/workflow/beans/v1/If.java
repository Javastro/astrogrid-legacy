/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: If.java,v 1.2 2004/07/09 09:35:54 nw Exp $
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
 * An if statement
 * 
 * @version $Revision: 1.2 $ $Date: 2004/07/09 09:35:54 $
 */
public class If extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _test
     */
    private java.lang.String _test;

    /**
     * Field _scriptingLanguage
     */
    private java.lang.String _scriptingLanguage = "jython-2.1";

    /**
     * Field _then
     */
    private org.astrogrid.workflow.beans.v1.Then _then;

    /**
     * Field _else
     */
    private org.astrogrid.workflow.beans.v1.Else _else;


      //----------------/
     //- Constructors -/
    //----------------/

    public If() {
        super();
        setScriptingLanguage("jython-2.1");
    } //-- org.astrogrid.workflow.beans.v1.If()


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
        
        if (obj instanceof If) {
        
            If temp = (If)obj;
            if (this._test != null) {
                if (temp._test == null) return false;
                else if (!(this._test.equals(temp._test))) 
                    return false;
            }
            else if (temp._test != null)
                return false;
            if (this._scriptingLanguage != null) {
                if (temp._scriptingLanguage == null) return false;
                else if (!(this._scriptingLanguage.equals(temp._scriptingLanguage))) 
                    return false;
            }
            else if (temp._scriptingLanguage != null)
                return false;
            if (this._then != null) {
                if (temp._then == null) return false;
                else if (!(this._then.equals(temp._then))) 
                    return false;
            }
            else if (temp._then != null)
                return false;
            if (this._else != null) {
                if (temp._else == null) return false;
                else if (!(this._else.equals(temp._else))) 
                    return false;
            }
            else if (temp._else != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'else'.
     * 
     * @return the value of field 'else'.
     */
    public org.astrogrid.workflow.beans.v1.Else getElse()
    {
        return this._else;
    } //-- org.astrogrid.workflow.beans.v1.Else getElse() 

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
     * Returns the value of field 'test'.
     * 
     * @return the value of field 'test'.
     */
    public java.lang.String getTest()
    {
        return this._test;
    } //-- java.lang.String getTest() 

    /**
     * Returns the value of field 'then'.
     * 
     * @return the value of field 'then'.
     */
    public org.astrogrid.workflow.beans.v1.Then getThen()
    {
        return this._then;
    } //-- org.astrogrid.workflow.beans.v1.Then getThen() 

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
     * Sets the value of field 'else'.
     * 
     * @param _else
     * @param else the value of field 'else'.
     */
    public void setElse(org.astrogrid.workflow.beans.v1.Else _else)
    {
        this._else = _else;
    } //-- void setElse(org.astrogrid.workflow.beans.v1.Else) 

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
     * Sets the value of field 'test'.
     * 
     * @param test the value of field 'test'.
     */
    public void setTest(java.lang.String test)
    {
        this._test = test;
    } //-- void setTest(java.lang.String) 

    /**
     * Sets the value of field 'then'.
     * 
     * @param then the value of field 'then'.
     */
    public void setThen(org.astrogrid.workflow.beans.v1.Then then)
    {
        this._then = then;
    } //-- void setThen(org.astrogrid.workflow.beans.v1.Then) 

    /**
     * Method unmarshalIf
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.If unmarshalIf(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.If) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.If.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.If unmarshalIf(java.io.Reader) 

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
