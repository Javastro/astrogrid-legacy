/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Function.java,v 1.2 2003/09/16 12:47:59 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.types.AggregateFunction;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class Function.
 * 
 * @version $Revision: 1.2 $ $Date: 2003/09/16 12:47:59 $
 */
public class Function extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _functionReference
     */
    private org.astrogrid.datacenter.adql.generated.types.AggregateFunction _functionReference;


      //----------------/
     //- Constructors -/
    //----------------/

    public Function() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.Function()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'functionReference'.
     * 
     * @return the value of field 'functionReference'.
     */
    public org.astrogrid.datacenter.adql.generated.types.AggregateFunction getFunctionReference()
    {
        return this._functionReference;
    } //-- org.astrogrid.datacenter.adql.generated.types.AggregateFunction getFunctionReference() 

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
     * Sets the value of field 'functionReference'.
     * 
     * @param functionReference the value of field
     * 'functionReference'.
     */
    public void setFunctionReference(org.astrogrid.datacenter.adql.generated.types.AggregateFunction functionReference)
    {
        this._functionReference = functionReference;
    } //-- void setFunctionReference(org.astrogrid.datacenter.adql.generated.types.AggregateFunction) 

    /**
     * Method unmarshalFunction
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.Function unmarshalFunction(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.Function) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.Function.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.Function unmarshalFunction(java.io.Reader) 

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
