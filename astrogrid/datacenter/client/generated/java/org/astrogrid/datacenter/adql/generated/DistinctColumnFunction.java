/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DistinctColumnFunction.java,v 1.5 2003/11/19 18:44:51 nw Exp $
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
 * Class DistinctColumnFunction.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class DistinctColumnFunction extends org.astrogrid.datacenter.adql.generated.Function 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _allColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.AllColumnReference _allColumnReference;

    /**
     * Field _singleColumnReference
     */
    private org.astrogrid.datacenter.adql.generated.SingleColumnReference _singleColumnReference;


      //----------------/
     //- Constructors -/
    //----------------/

    public DistinctColumnFunction() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.DistinctColumnFunction()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'allColumnReference'.
     * 
     * @return the value of field 'allColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.AllColumnReference getAllColumnReference()
    {
        return this._allColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.AllColumnReference getAllColumnReference() 

    /**
     * Returns the value of field 'singleColumnReference'.
     * 
     * @return the value of field 'singleColumnReference'.
     */
    public org.astrogrid.datacenter.adql.generated.SingleColumnReference getSingleColumnReference()
    {
        return this._singleColumnReference;
    } //-- org.astrogrid.datacenter.adql.generated.SingleColumnReference getSingleColumnReference() 

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
     * Sets the value of field 'allColumnReference'.
     * 
     * @param allColumnReference the value of field
     * 'allColumnReference'.
     */
    public void setAllColumnReference(org.astrogrid.datacenter.adql.generated.AllColumnReference allColumnReference)
    {
        this._allColumnReference = allColumnReference;
    } //-- void setAllColumnReference(org.astrogrid.datacenter.adql.generated.AllColumnReference) 

    /**
     * Sets the value of field 'singleColumnReference'.
     * 
     * @param singleColumnReference the value of field
     * 'singleColumnReference'.
     */
    public void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.SingleColumnReference singleColumnReference)
    {
        this._singleColumnReference = singleColumnReference;
    } //-- void setSingleColumnReference(org.astrogrid.datacenter.adql.generated.SingleColumnReference) 

    /**
     * Method unmarshalDistinctColumnFunction
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.DistinctColumnFunction unmarshalDistinctColumnFunction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.DistinctColumnFunction) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.DistinctColumnFunction.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.DistinctColumnFunction unmarshalDistinctColumnFunction(java.io.Reader) 

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
