/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SelectionListChoice.java,v 1.8 2003/11/26 17:01:37 nw Exp $
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
 * Class SelectionListChoice.
 * 
 * @version $Revision: 1.8 $ $Date: 2003/11/26 17:01:37 $
 */
public class SelectionListChoice extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field selectionListChoiceItem
     */
    private org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem selectionListChoiceItem;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectionListChoice() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'selectionListChoiceItem'.
     * 
     * @return the value of field 'selectionListChoiceItem'.
     */
    public org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem getSelectionListChoiceItem()
    {
        return this.selectionListChoiceItem;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem getSelectionListChoiceItem() 

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
     * Sets the value of field 'selectionListChoiceItem'.
     * 
     * @param selectionListChoiceItem the value of field
     * 'selectionListChoiceItem'.
     */
    public void setSelectionListChoiceItem(org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem selectionListChoiceItem)
    {
        this.selectionListChoiceItem = selectionListChoiceItem;
    } //-- void setSelectionListChoiceItem(org.astrogrid.datacenter.adql.generated.SelectionListChoiceItem) 

    /**
     * Method unmarshalSelectionListChoice
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.SelectionListChoice unmarshalSelectionListChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.SelectionListChoice) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.SelectionListChoice.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoice unmarshalSelectionListChoice(java.io.Reader) 

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
