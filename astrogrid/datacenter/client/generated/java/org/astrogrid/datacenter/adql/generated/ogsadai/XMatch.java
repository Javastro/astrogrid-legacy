/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: XMatch.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class XMatch.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class XMatch extends org.astrogrid.datacenter.adql.generated.ogsadai.Search 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _args
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias _args;

    /**
     * Field _compare
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison _compare;

    /**
     * Field _XMatchChoice
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.XMatchChoice _XMatchChoice;


      //----------------/
     //- Constructors -/
    //----------------/

    public XMatch() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.XMatch()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'args'.
     * 
     * @return the value of field 'args'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias getArgs()
    {
        return this._args;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias getArgs() 

    /**
     * Returns the value of field 'compare'.
     * 
     * @return the value of field 'compare'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison getCompare()
    {
        return this._compare;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison getCompare() 

    /**
     * Returns the value of field 'XMatchChoice'.
     * 
     * @return the value of field 'XMatchChoice'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.XMatchChoice getXMatchChoice()
    {
        return this._XMatchChoice;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.XMatchChoice getXMatchChoice() 

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
     * Sets the value of field 'args'.
     * 
     * @param args the value of field 'args'.
     */
    public void setArgs(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias args)
    {
        this._args = args;
    } //-- void setArgs(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias) 

    /**
     * Sets the value of field 'compare'.
     * 
     * @param compare the value of field 'compare'.
     */
    public void setCompare(org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison compare)
    {
        this._compare = compare;
    } //-- void setCompare(org.astrogrid.datacenter.adql.generated.ogsadai.types.Comparison) 

    /**
     * Sets the value of field 'XMatchChoice'.
     * 
     * @param XMatchChoice the value of field 'XMatchChoice'.
     */
    public void setXMatchChoice(org.astrogrid.datacenter.adql.generated.ogsadai.XMatchChoice XMatchChoice)
    {
        this._XMatchChoice = XMatchChoice;
    } //-- void setXMatchChoice(org.astrogrid.datacenter.adql.generated.ogsadai.XMatchChoice) 

    /**
     * Method unmarshalXMatch
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.XMatch unmarshalXMatch(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.XMatch) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.XMatch.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.XMatch unmarshalXMatch(java.io.Reader) 

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
