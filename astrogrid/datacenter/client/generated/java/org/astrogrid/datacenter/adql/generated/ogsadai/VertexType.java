/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VertexType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class VertexType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class VertexType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _position
     */
    private CoordsType _position;

    /**
     * Field _smallCircle
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.SmallCircleType _smallCircle;


      //----------------/
     //- Constructors -/
    //----------------/

    public VertexType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VertexType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'position'.
     * 
     * @return the value of field 'position'.
     */
    public CoordsType getPosition()
    {
        return this._position;
    } //-- CoordsType getPosition() 

    /**
     * Returns the value of field 'smallCircle'.
     * 
     * @return the value of field 'smallCircle'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.SmallCircleType getSmallCircle()
    {
        return this._smallCircle;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.SmallCircleType getSmallCircle() 

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
     * Sets the value of field 'position'.
     * 
     * @param position the value of field 'position'.
     */
    public void setPosition(CoordsType position)
    {
        this._position = position;
    } //-- void setPosition(CoordsType) 

    /**
     * Sets the value of field 'smallCircle'.
     * 
     * @param smallCircle the value of field 'smallCircle'.
     */
    public void setSmallCircle(org.astrogrid.datacenter.adql.generated.ogsadai.SmallCircleType smallCircle)
    {
        this._smallCircle = smallCircle;
    } //-- void setSmallCircle(org.astrogrid.datacenter.adql.generated.ogsadai.SmallCircleType) 

    /**
     * Method unmarshalVertexType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.VertexType unmarshalVertexType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.VertexType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.VertexType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VertexType unmarshalVertexType(java.io.Reader) 

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
