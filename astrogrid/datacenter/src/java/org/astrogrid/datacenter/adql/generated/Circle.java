/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Circle.java,v 1.1 2003/08/28 15:27:54 nw Exp $
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
 * Class Circle.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/08/28 15:27:54 $
 */
public class Circle extends org.astrogrid.datacenter.adql.generated.Area 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ra
     */
    private org.astrogrid.datacenter.adql.generated.ApproxNum _ra;

    /**
     * Field _dec
     */
    private org.astrogrid.datacenter.adql.generated.ApproxNum _dec;

    /**
     * Field _radius
     */
    private org.astrogrid.datacenter.adql.generated.ApproxNum _radius;


      //----------------/
     //- Constructors -/
    //----------------/

    public Circle() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.Circle()


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
        
        if (obj instanceof Circle) {
        
            Circle temp = (Circle)obj;
            if (this._ra != null) {
                if (temp._ra == null) return false;
                else if (!(this._ra.equals(temp._ra))) 
                    return false;
            }
            else if (temp._ra != null)
                return false;
            if (this._dec != null) {
                if (temp._dec == null) return false;
                else if (!(this._dec.equals(temp._dec))) 
                    return false;
            }
            else if (temp._dec != null)
                return false;
            if (this._radius != null) {
                if (temp._radius == null) return false;
                else if (!(this._radius.equals(temp._radius))) 
                    return false;
            }
            else if (temp._radius != null)
                return false;
            return true;
        }
        return false;
    } //-- boolean equals(java.lang.Object) 

    /**
     * Returns the value of field 'dec'.
     * 
     * @return the value of field 'dec'.
     */
    public org.astrogrid.datacenter.adql.generated.ApproxNum getDec()
    {
        return this._dec;
    } //-- org.astrogrid.datacenter.adql.generated.ApproxNum getDec() 

    /**
     * Returns the value of field 'ra'.
     * 
     * @return the value of field 'ra'.
     */
    public org.astrogrid.datacenter.adql.generated.ApproxNum getRa()
    {
        return this._ra;
    } //-- org.astrogrid.datacenter.adql.generated.ApproxNum getRa() 

    /**
     * Returns the value of field 'radius'.
     * 
     * @return the value of field 'radius'.
     */
    public org.astrogrid.datacenter.adql.generated.ApproxNum getRadius()
    {
        return this._radius;
    } //-- org.astrogrid.datacenter.adql.generated.ApproxNum getRadius() 

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
     * Sets the value of field 'dec'.
     * 
     * @param dec the value of field 'dec'.
     */
    public void setDec(org.astrogrid.datacenter.adql.generated.ApproxNum dec)
    {
        this._dec = dec;
    } //-- void setDec(org.astrogrid.datacenter.adql.generated.ApproxNum) 

    /**
     * Sets the value of field 'ra'.
     * 
     * @param ra the value of field 'ra'.
     */
    public void setRa(org.astrogrid.datacenter.adql.generated.ApproxNum ra)
    {
        this._ra = ra;
    } //-- void setRa(org.astrogrid.datacenter.adql.generated.ApproxNum) 

    /**
     * Sets the value of field 'radius'.
     * 
     * @param radius the value of field 'radius'.
     */
    public void setRadius(org.astrogrid.datacenter.adql.generated.ApproxNum radius)
    {
        this._radius = radius;
    } //-- void setRadius(org.astrogrid.datacenter.adql.generated.ApproxNum) 

    /**
     * Method unmarshalCircle
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.Circle unmarshalCircle(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.Circle) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.Circle.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.Circle unmarshalCircle(java.io.Reader) 

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
