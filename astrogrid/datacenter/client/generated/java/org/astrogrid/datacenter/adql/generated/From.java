/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: From.java,v 1.12 2004/01/13 00:31:23 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class From.
 * 
 * @version $Revision: 1.12 $ $Date: 2004/01/13 00:31:23 $
 */
public class From extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _tableReference
     */
    private org.astrogrid.datacenter.adql.generated.ArrayOfTable _tableReference;


      //----------------/
     //- Constructors -/
    //----------------/

    public From() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.From()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'tableReference'.
     * 
     * @return the value of field 'tableReference'.
     */
    public org.astrogrid.datacenter.adql.generated.ArrayOfTable getTableReference()
    {
        return this._tableReference;
    } //-- org.astrogrid.datacenter.adql.generated.ArrayOfTable getTableReference() 

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
     * Sets the value of field 'tableReference'.
     * 
     * @param tableReference the value of field 'tableReference'.
     */
    public void setTableReference(org.astrogrid.datacenter.adql.generated.ArrayOfTable tableReference)
    {
        this._tableReference = tableReference;
    } //-- void setTableReference(org.astrogrid.datacenter.adql.generated.ArrayOfTable) 

    /**
     * Method unmarshalFrom
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.From unmarshalFrom(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.From) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.From.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.From unmarshalFrom(java.io.Reader) 

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
