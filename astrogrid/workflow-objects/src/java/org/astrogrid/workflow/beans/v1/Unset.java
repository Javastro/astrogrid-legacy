/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Unset.java,v 1.10 2007/01/04 16:26:27 clq2 Exp $
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
 * forget a previously-declared workflow variable
 * 
 * @version $Revision: 1.10 $ $Date: 2007/01/04 16:26:27 $
 */
public class Unset extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the name of the variabe to unset
     */
    private java.lang.String _var;


      //----------------/
     //- Constructors -/
    //----------------/

    public Unset() {
        super();
    } //-- org.astrogrid.workflow.beans.v1.Unset()


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
        
        if (obj instanceof Unset) {
        
            Unset temp = (Unset)obj;
            if (this._var != null) {
                if (temp._var == null) return false;
                else if (!(this._var.equals(temp._var))) 
                    return false;
            }
            else if (temp._var != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'var'. The field 'var' has the
     * following description: the name of the variabe to unset
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
     * Sets the value of field 'var'. The field 'var' has the
     * following description: the name of the variabe to unset
     * 
     * @param var the value of field 'var'.
     */
    public void setVar(java.lang.String var)
    {
        this._var = var;
    } //-- void setVar(java.lang.String) 

    /**
     * Method unmarshalUnset
     * 
     * @param reader
     */
    public static org.astrogrid.workflow.beans.v1.Unset unmarshalUnset(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.workflow.beans.v1.Unset) Unmarshaller.unmarshal(org.astrogrid.workflow.beans.v1.Unset.class, reader);
    } //-- org.astrogrid.workflow.beans.v1.Unset unmarshalUnset(java.io.Reader) 

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
