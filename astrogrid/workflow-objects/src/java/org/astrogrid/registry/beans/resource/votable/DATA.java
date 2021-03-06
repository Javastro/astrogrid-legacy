/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DATA.java,v 1.14 2007/01/04 16:26:14 clq2 Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

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
 * Class DATA.
 * 
 * @version $Revision: 1.14 $ $Date: 2007/01/04 16:26:14 $
 */
public class DATA extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * an abstract representation of the tables storage form. 
     *  
     */
    private org.astrogrid.registry.beans.resource.votable.TABLEFORMATType _TABLEFORMAT;


      //----------------/
     //- Constructors -/
    //----------------/

    public DATA() {
        super();
    } //-- org.astrogrid.registry.beans.resource.votable.DATA()


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
        
        if (obj instanceof DATA) {
        
            DATA temp = (DATA)obj;
            if (this._TABLEFORMAT != null) {
                if (temp._TABLEFORMAT == null) return false;
                else if (!(this._TABLEFORMAT.equals(temp._TABLEFORMAT))) 
                    return false;
            }
            else if (temp._TABLEFORMAT != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'TABLEFORMAT'. The field
     * 'TABLEFORMAT' has the following description: an abstract
     * representation of the tables storage form. 
     *  
     * 
     * @return the value of field 'TABLEFORMAT'.
     */
    public org.astrogrid.registry.beans.resource.votable.TABLEFORMATType getTABLEFORMAT()
    {
        return this._TABLEFORMAT;
    } //-- org.astrogrid.registry.beans.resource.votable.TABLEFORMATType getTABLEFORMAT() 

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
     * Sets the value of field 'TABLEFORMAT'. The field
     * 'TABLEFORMAT' has the following description: an abstract
     * representation of the tables storage form. 
     *  
     * 
     * @param TABLEFORMAT the value of field 'TABLEFORMAT'.
     */
    public void setTABLEFORMAT(org.astrogrid.registry.beans.resource.votable.TABLEFORMATType TABLEFORMAT)
    {
        this._TABLEFORMAT = TABLEFORMAT;
    } //-- void setTABLEFORMAT(org.astrogrid.registry.beans.resource.votable.TABLEFORMATType) 

    /**
     * Method unmarshalDATA
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.DATA unmarshalDATA(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.DATA) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.DATA.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.DATA unmarshalDATA(java.io.Reader) 

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
