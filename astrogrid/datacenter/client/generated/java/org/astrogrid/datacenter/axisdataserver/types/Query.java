/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Query.java,v 1.3 2003/11/19 18:44:51 nw Exp $
 */

package org.astrogrid.datacenter.axisdataserver.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.Select;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Query.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/11/19 18:44:51 $
 */
public class Query implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _select
     */
    private org.astrogrid.datacenter.adql.generated.Select _select;


      //----------------/
     //- Constructors -/
    //----------------/

    public Query() {
        super();
    } //-- org.astrogrid.datacenter.axisdataserver.types.Query()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'select'.
     * 
     * @return the value of field 'select'.
     */
    public org.astrogrid.datacenter.adql.generated.Select getSelect()
    {
        return this._select;
    } //-- org.astrogrid.datacenter.adql.generated.Select getSelect() 

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
     * Sets the value of field 'select'.
     * 
     * @param select the value of field 'select'.
     */
    public void setSelect(org.astrogrid.datacenter.adql.generated.Select select)
    {
        this._select = select;
    } //-- void setSelect(org.astrogrid.datacenter.adql.generated.Select) 

    /**
     * Method unmarshalQuery
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.axisdataserver.types.Query unmarshalQuery(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.axisdataserver.types.Query) Unmarshaller.unmarshal(org.astrogrid.datacenter.axisdataserver.types.Query.class, reader);
    } //-- org.astrogrid.datacenter.axisdataserver.types.Query unmarshalQuery(java.io.Reader) 

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
