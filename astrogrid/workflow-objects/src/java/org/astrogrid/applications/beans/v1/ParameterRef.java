/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterRef.java,v 1.14 2004/03/10 17:13:59 pah Exp $
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
 * reference to an existing parameter. Used in the interface
 * 
 * @version $Revision: 1.14 $ $Date: 2004/03/10 17:13:59 $
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
     * Field _minoccurs
     */
    private int _minoccurs = 1;

    /**
     * keeps track of state for field: _minoccurs
     */
    private boolean _has_minoccurs;

    /**
     * a value of 0 implies unbounded
     */
    private int _maxoccurs = 1;

    /**
     * keeps track of state for field: _maxoccurs
     */
    private boolean _has_maxoccurs;


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
     * Method deleteMaxoccurs
     */
    public void deleteMaxoccurs()
    {
        this._has_maxoccurs= false;
    } //-- void deleteMaxoccurs() 

    /**
     * Method deleteMinoccurs
     */
    public void deleteMinoccurs()
    {
        this._has_minoccurs= false;
    } //-- void deleteMinoccurs() 

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
        
        if (obj instanceof ParameterRef) {
        
            ParameterRef temp = (ParameterRef)obj;
            if (this._ref != null) {
                if (temp._ref == null) return false;
                else if (!(this._ref.equals(temp._ref))) 
                    return false;
            }
            else if (temp._ref != null)
                return false;
            if (this._minoccurs != temp._minoccurs)
                return false;
            if (this._has_minoccurs != temp._has_minoccurs)
                return false;
            if (this._maxoccurs != temp._maxoccurs)
                return false;
            if (this._has_maxoccurs != temp._has_maxoccurs)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'maxoccurs'. The field
     * 'maxoccurs' has the following description: a value of 0
     * implies unbounded
     * 
     * @return the value of field 'maxoccurs'.
     */
    public int getMaxoccurs()
    {
        return this._maxoccurs;
    } //-- int getMaxoccurs() 

    /**
     * Returns the value of field 'minoccurs'.
     * 
     * @return the value of field 'minoccurs'.
     */
    public int getMinoccurs()
    {
        return this._minoccurs;
    } //-- int getMinoccurs() 

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
     * Method hasMaxoccurs
     */
    public boolean hasMaxoccurs()
    {
        return this._has_maxoccurs;
    } //-- boolean hasMaxoccurs() 

    /**
     * Method hasMinoccurs
     */
    public boolean hasMinoccurs()
    {
        return this._has_minoccurs;
    } //-- boolean hasMinoccurs() 

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
     * Sets the value of field 'maxoccurs'. The field 'maxoccurs'
     * has the following description: a value of 0 implies
     * unbounded
     * 
     * @param maxoccurs the value of field 'maxoccurs'.
     */
    public void setMaxoccurs(int maxoccurs)
    {
        this._maxoccurs = maxoccurs;
        this._has_maxoccurs = true;
    } //-- void setMaxoccurs(int) 

    /**
     * Sets the value of field 'minoccurs'.
     * 
     * @param minoccurs the value of field 'minoccurs'.
     */
    public void setMinoccurs(int minoccurs)
    {
        this._minoccurs = minoccurs;
        this._has_minoccurs = true;
    } //-- void setMinoccurs(int) 

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
