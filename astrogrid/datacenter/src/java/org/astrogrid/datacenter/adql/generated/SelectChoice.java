/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SelectChoice.java,v 1.1 2003/08/28 15:27:54 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class SelectChoice.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/08/28 15:27:54 $
 */
public class SelectChoice extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _selectionList
     */
    private org.astrogrid.datacenter.adql.generated.SelectionList _selectionList;

    /**
     * Field _selectionAll
     */
    private org.astrogrid.datacenter.adql.generated.SelectionAll _selectionAll;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectChoice() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.SelectChoice()


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
        
        if (obj instanceof SelectChoice) {
        
            SelectChoice temp = (SelectChoice)obj;
            if (this._selectionList != null) {
                if (temp._selectionList == null) return false;
                else if (!(this._selectionList.equals(temp._selectionList))) 
                    return false;
            }
            else if (temp._selectionList != null)
                return false;
            if (this._selectionAll != null) {
                if (temp._selectionAll == null) return false;
                else if (!(this._selectionAll.equals(temp._selectionAll))) 
                    return false;
            }
            else if (temp._selectionAll != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'selectionAll'.
     * 
     * @return the value of field 'selectionAll'.
     */
    public org.astrogrid.datacenter.adql.generated.SelectionAll getSelectionAll()
    {
        return this._selectionAll;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionAll getSelectionAll() 

    /**
     * Returns the value of field 'selectionList'.
     * 
     * @return the value of field 'selectionList'.
     */
    public org.astrogrid.datacenter.adql.generated.SelectionList getSelectionList()
    {
        return this._selectionList;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionList getSelectionList() 

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
     * Sets the value of field 'selectionAll'.
     * 
     * @param selectionAll the value of field 'selectionAll'.
     */
    public void setSelectionAll(org.astrogrid.datacenter.adql.generated.SelectionAll selectionAll)
    {
        this._selectionAll = selectionAll;
    } //-- void setSelectionAll(org.astrogrid.datacenter.adql.generated.SelectionAll) 

    /**
     * Sets the value of field 'selectionList'.
     * 
     * @param selectionList the value of field 'selectionList'.
     */
    public void setSelectionList(org.astrogrid.datacenter.adql.generated.SelectionList selectionList)
    {
        this._selectionList = selectionList;
    } //-- void setSelectionList(org.astrogrid.datacenter.adql.generated.SelectionList) 

    /**
     * Method unmarshalSelectChoice
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.SelectChoice unmarshalSelectChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.SelectChoice) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.SelectChoice.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.SelectChoice unmarshalSelectChoice(java.io.Reader) 

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
