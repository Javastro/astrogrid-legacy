/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AliasSelectionItem.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class AliasSelectionItem.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class AliasSelectionItem extends org.astrogrid.datacenter.adql.generated.ogsadai.SelectionItem 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aliasSelectionItemChoice
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice _aliasSelectionItemChoice;

    /**
     * Field _aliasName
     */
    private java.lang.String _aliasName;


      //----------------/
     //- Constructors -/
    //----------------/

    public AliasSelectionItem() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'aliasName'.
     * 
     * @return the value of field 'aliasName'.
     */
    public java.lang.String getAliasName()
    {
        return this._aliasName;
    } //-- java.lang.String getAliasName() 

    /**
     * Returns the value of field 'aliasSelectionItemChoice'.
     * 
     * @return the value of field 'aliasSelectionItemChoice'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice getAliasSelectionItemChoice()
    {
        return this._aliasSelectionItemChoice;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice getAliasSelectionItemChoice() 

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
     * Sets the value of field 'aliasName'.
     * 
     * @param aliasName the value of field 'aliasName'.
     */
    public void setAliasName(java.lang.String aliasName)
    {
        this._aliasName = aliasName;
    } //-- void setAliasName(java.lang.String) 

    /**
     * Sets the value of field 'aliasSelectionItemChoice'.
     * 
     * @param aliasSelectionItemChoice the value of field
     * 'aliasSelectionItemChoice'.
     */
    public void setAliasSelectionItemChoice(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice aliasSelectionItemChoice)
    {
        this._aliasSelectionItemChoice = aliasSelectionItemChoice;
    } //-- void setAliasSelectionItemChoice(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItemChoice) 

    /**
     * Method unmarshalAliasSelectionItem
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem unmarshalAliasSelectionItem(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.AliasSelectionItem unmarshalAliasSelectionItem(java.io.Reader) 

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
