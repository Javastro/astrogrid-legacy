/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ScalarExpression.java,v 1.5 2003/11/19 18:44:51 nw Exp $
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
 * Class ScalarExpression.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class ScalarExpression extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //----------------/
     //- Constructors -/
    //----------------/

    public ScalarExpression() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression()


      //-----------/
     //- Methods -/
    //-----------/

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
     * Method unmarshalScalarExpression
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ScalarExpression unmarshalScalarExpression(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ScalarExpression) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ScalarExpression.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ScalarExpression unmarshalScalarExpression(java.io.Reader) 

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
