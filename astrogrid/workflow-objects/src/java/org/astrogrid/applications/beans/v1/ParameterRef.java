/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterRef.java,v 1.6 2004/03/03 19:05:19 pah Exp $
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
 * reference to an existing parameter
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/03 19:05:19 $
 */
public class ParameterRef extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ref
     */
    private java.lang.String _ref;

    /**
     * Field _repeatable
     */
    private boolean _repeatable = false;

    /**
     * keeps track of state for field: _repeatable
     */
    private boolean _has_repeatable;

    /**
     * Field _optional
     */
    private boolean _optional = false;

    /**
     * keeps track of state for field: _optional
     */
    private boolean _has_optional;


      //----------------/
     //- Constructors -/
    //----------------/

    public ParameterRef() {
        super();
    } //-- org.astrogrid.applications.beans.v1.ParameterRef()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method deleteOptional
     */
    public void deleteOptional()
    {
        this._has_optional= false;
    } //-- void deleteOptional() 

    /**
     * Method deleteRepeatable
     */
    public void deleteRepeatable()
    {
        this._has_repeatable= false;
    } //-- void deleteRepeatable() 

    /**
     * Returns the value of field 'optional'.
     * 
     * @return the value of field 'optional'.
     */
    public boolean getOptional()
    {
        return this._optional;
    } //-- boolean getOptional() 

    /**
     * Returns the value of field 'ref'.
     * 
     * @return the value of field 'ref'.
     */
    public java.lang.String getRef()
    {
        return this._ref;
    } //-- java.lang.String getRef() 

    /**
     * Returns the value of field 'repeatable'.
     * 
     * @return the value of field 'repeatable'.
     */
    public boolean getRepeatable()
    {
        return this._repeatable;
    } //-- boolean getRepeatable() 

    /**
     * Method hasOptional
     */
    public boolean hasOptional()
    {
        return this._has_optional;
    } //-- boolean hasOptional() 

    /**
     * Method hasRepeatable
     */
    public boolean hasRepeatable()
    {
        return this._has_repeatable;
    } //-- boolean hasRepeatable() 

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
     * Sets the value of field 'optional'.
     * 
     * @param optional the value of field 'optional'.
     */
    public void setOptional(boolean optional)
    {
        this._optional = optional;
        this._has_optional = true;
    } //-- void setOptional(boolean) 

    /**
     * Sets the value of field 'ref'.
     * 
     * @param ref the value of field 'ref'.
     */
    public void setRef(java.lang.String ref)
    {
        this._ref = ref;
    } //-- void setRef(java.lang.String) 

    /**
     * Sets the value of field 'repeatable'.
     * 
     * @param repeatable the value of field 'repeatable'.
     */
    public void setRepeatable(boolean repeatable)
    {
        this._repeatable = repeatable;
        this._has_repeatable = true;
    } //-- void setRepeatable(boolean) 

    /**
     * Method unmarshalParameterRef
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.ParameterRef unmarshalParameterRef(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.ParameterRef) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.ParameterRef.class, reader);
    } //-- org.astrogrid.applications.beans.v1.ParameterRef unmarshalParameterRef(java.io.Reader) 

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
