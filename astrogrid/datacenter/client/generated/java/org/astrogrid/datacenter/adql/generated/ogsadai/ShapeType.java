/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ShapeType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class ShapeType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ShapeType extends org.astrogrid.datacenter.adql.generated.ogsadai.RegionType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _coord_system_id
     */
    private java.lang.Object _coord_system_id;


      //----------------/
     //- Constructors -/
    //----------------/

    public ShapeType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'coord_system_id'.
     * 
     * @return the value of field 'coord_system_id'.
     */
    public java.lang.Object getCoord_system_id()
    {
        return this._coord_system_id;
    } //-- java.lang.Object getCoord_system_id() 

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
     * Sets the value of field 'coord_system_id'.
     * 
     * @param coord_system_id the value of field 'coord_system_id'.
     */
    public void setCoord_system_id(java.lang.Object coord_system_id)
    {
        this._coord_system_id = coord_system_id;
    } //-- void setCoord_system_id(java.lang.Object) 

    /**
     * Method unmarshalShapeType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType unmarshalShapeType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ShapeType unmarshalShapeType(java.io.Reader) 

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
