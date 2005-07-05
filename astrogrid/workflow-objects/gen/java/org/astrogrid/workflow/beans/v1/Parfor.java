/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Parfor.java,v 1.2 2005/07/05 08:26:55 clq2 Exp $
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
 * A parallel-for loop construct - executes the loop body on each
 * element of items in parallel
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:26:55 $
 */
public class Parfor extends org.astrogrid.workflow.beans.v1.AbstractActivity 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the name of the loop variable
     */
    private java.lang.String _var;

    /**
     * A sequence or iterator of items- the loop variable will be
     * assigned to each in turn, and then loop body executed
     */
    private java.lang.String _items;

    /**
     * Head of the substitution group
     */
    private org.astrogrid.workflow.beans.v1.AbstractActivity _activity;


      //----------------/
     //- Constructors -/
    //----------------/

    public Parfor() {
        super();
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
            if (this._items != null) {
                if (temp._items == null) return false;
                else if (!(this._items.equals(temp._items))) 
                    return false;
            }
            else if (temp._items != null)
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
     * has the following description: Head of the substitution
     * group
     * 
     * @return the value of field 'activity'.
     */
    public org.astrogrid.workflow.beans.v1.AbstractActivity getActivity()
    {
        return this._activity;
    } //-- org.astrogrid.workflow.beans.v1.AbstractActivity getActivity() 

    /**
     * Returns the value of field 'items'. The field 'items' has
     * the following description: A sequence or iterator of items-
     * the loop variable will be assigned to each in turn, and then
     * loop body executed
     * 
     * @return the value of field 'items'.
     */
    public java.lang.String getItems()
    {
        return this._items;
    } //-- java.lang.String getItems() 

    /**
     * Returns the value of field 'var'. The field 'var' has the
     * following description: the name of the loop variable
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
     * the following description: Head of the substitution group
     * 
     * @param activity the value of field 'activity'.
     */
    public void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity activity)
    {
        this._activity = activity;
    } //-- void setActivity(org.astrogrid.workflow.beans.v1.AbstractActivity) 

    /**
     * Sets the value of field 'items'. The field 'items' has the
     * following description: A sequence or iterator of items- the
     * loop variable will be assigned to each in turn, and then
     * loop body executed
     * 
     * @param items the value of field 'items'.
     */
    public void setItems(java.lang.String items)
    {
        this._items = items;
    } //-- void setItems(java.lang.String) 

    /**
     * Sets the value of field 'var'. The field 'var' has the
     * following description: the name of the loop variable
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
