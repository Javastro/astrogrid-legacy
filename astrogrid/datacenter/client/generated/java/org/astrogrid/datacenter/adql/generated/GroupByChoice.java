/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GroupByChoice.java,v 1.5 2003/11/19 18:44:51 nw Exp $
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
 * Class GroupByChoice.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class GroupByChoice extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field groupByChoiceItem
     */
    private org.astrogrid.datacenter.adql.generated.GroupByChoiceItem groupByChoiceItem;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupByChoice() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoice()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'groupByChoiceItem'.
     * 
     * @return the value of field 'groupByChoiceItem'.
     */
    public org.astrogrid.datacenter.adql.generated.GroupByChoiceItem getGroupByChoiceItem()
    {
        return this.groupByChoiceItem;
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoiceItem getGroupByChoiceItem() 

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
     * Sets the value of field 'groupByChoiceItem'.
     * 
     * @param groupByChoiceItem the value of field
     * 'groupByChoiceItem'.
     */
    public void setGroupByChoiceItem(org.astrogrid.datacenter.adql.generated.GroupByChoiceItem groupByChoiceItem)
    {
        this.groupByChoiceItem = groupByChoiceItem;
    } //-- void setGroupByChoiceItem(org.astrogrid.datacenter.adql.generated.GroupByChoiceItem) 

    /**
     * Method unmarshalGroupByChoice
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.GroupByChoice unmarshalGroupByChoice(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.GroupByChoice) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.GroupByChoice.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoice unmarshalGroupByChoice(java.io.Reader) 

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
