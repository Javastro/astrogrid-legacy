/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterRef.java,v 1.4 2004/04/05 14:36:12 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * @version $Revision: 1.4 $ $Date: 2004/04/05 14:36:12 $
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
    } //-- org.astrogrid.registry.beans.cea.base.ParameterRef()


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
    public static org.astrogrid.registry.beans.cea.base.ParameterRef unmarshalParameterRef(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.ParameterRef) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.ParameterRef.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.ParameterRef unmarshalParameterRef(java.io.Reader) 

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
