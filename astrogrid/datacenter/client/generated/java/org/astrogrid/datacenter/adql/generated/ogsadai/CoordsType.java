/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordsType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
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
 * Class CoordsType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class CoordsType extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _coord_system_id
     */
    private java.lang.Object _coord_system_id;

    /**
     * Field _velScalar
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType _velScalar;

    /**
     * Field _vel3Vector
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Vel3VectorType _vel3Vector;

    /**
     * Field _spectrum
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralType _spectrum;

    /**
     * Field _coordFile
     */
    private java.lang.String _coordFile;

    /**
     * Field _time
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType _time;

    /**
     * Field _vel2Vector
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType _vel2Vector;

    /**
     * Field _pos2Vector
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Pos2VectorType _pos2Vector;

    /**
     * Field _posScalar
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.PosScalarType _posScalar;

    /**
     * Field _pos3Vector
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType _pos3Vector;

    /**
     * Field _redshift
     */
    private org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType _redshift;


      //----------------/
     //- Constructors -/
    //----------------/

    public CoordsType() {
        super();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Returns the value of field 'coordFile'.
     * 
     * @return the value of field 'coordFile'.
     */
    public java.lang.String getCoordFile()
    {
        return this._coordFile;
    } //-- java.lang.String getCoordFile() 

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
     * Returns the value of field 'ID'.
     * 
     * @return the value of field 'ID'.
     */
    public java.lang.String getID()
    {
        return this._ID;
    } //-- java.lang.String getID() 

    /**
     * Returns the value of field 'pos2Vector'.
     * 
     * @return the value of field 'pos2Vector'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Pos2VectorType getPos2Vector()
    {
        return this._pos2Vector;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Pos2VectorType getPos2Vector() 

    /**
     * Returns the value of field 'pos3Vector'.
     * 
     * @return the value of field 'pos3Vector'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType getPos3Vector()
    {
        return this._pos3Vector;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType getPos3Vector() 

    /**
     * Returns the value of field 'posScalar'.
     * 
     * @return the value of field 'posScalar'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.PosScalarType getPosScalar()
    {
        return this._posScalar;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.PosScalarType getPosScalar() 

    /**
     * Returns the value of field 'redshift'.
     * 
     * @return the value of field 'redshift'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType getRedshift()
    {
        return this._redshift;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType getRedshift() 

    /**
     * Returns the value of field 'spectrum'.
     * 
     * @return the value of field 'spectrum'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralType getSpectrum()
    {
        return this._spectrum;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralType getSpectrum() 

    /**
     * Returns the value of field 'time'.
     * 
     * @return the value of field 'time'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType getTime()
    {
        return this._time;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType getTime() 

    /**
     * Returns the value of field 'vel2Vector'.
     * 
     * @return the value of field 'vel2Vector'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType getVel2Vector()
    {
        return this._vel2Vector;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType getVel2Vector() 

    /**
     * Returns the value of field 'vel3Vector'.
     * 
     * @return the value of field 'vel3Vector'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Vel3VectorType getVel3Vector()
    {
        return this._vel3Vector;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Vel3VectorType getVel3Vector() 

    /**
     * Returns the value of field 'velScalar'.
     * 
     * @return the value of field 'velScalar'.
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType getVelScalar()
    {
        return this._velScalar;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType getVelScalar() 

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
     * Sets the value of field 'coordFile'.
     * 
     * @param coordFile the value of field 'coordFile'.
     */
    public void setCoordFile(java.lang.String coordFile)
    {
        this._coordFile = coordFile;
    } //-- void setCoordFile(java.lang.String) 

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
     * Sets the value of field 'ID'.
     * 
     * @param ID the value of field 'ID'.
     */
    public void setID(java.lang.String ID)
    {
        this._ID = ID;
    } //-- void setID(java.lang.String) 

    /**
     * Sets the value of field 'pos2Vector'.
     * 
     * @param pos2Vector the value of field 'pos2Vector'.
     */
    public void setPos2Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Pos2VectorType pos2Vector)
    {
        this._pos2Vector = pos2Vector;
    } //-- void setPos2Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Pos2VectorType) 

    /**
     * Sets the value of field 'pos3Vector'.
     * 
     * @param pos3Vector the value of field 'pos3Vector'.
     */
    public void setPos3Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType pos3Vector)
    {
        this._pos3Vector = pos3Vector;
    } //-- void setPos3Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Pos3VectorType) 

    /**
     * Sets the value of field 'posScalar'.
     * 
     * @param posScalar the value of field 'posScalar'.
     */
    public void setPosScalar(org.astrogrid.datacenter.adql.generated.ogsadai.PosScalarType posScalar)
    {
        this._posScalar = posScalar;
    } //-- void setPosScalar(org.astrogrid.datacenter.adql.generated.ogsadai.PosScalarType) 

    /**
     * Sets the value of field 'redshift'.
     * 
     * @param redshift the value of field 'redshift'.
     */
    public void setRedshift(org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType redshift)
    {
        this._redshift = redshift;
    } //-- void setRedshift(org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType) 

    /**
     * Sets the value of field 'spectrum'.
     * 
     * @param spectrum the value of field 'spectrum'.
     */
    public void setSpectrum(org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralType spectrum)
    {
        this._spectrum = spectrum;
    } //-- void setSpectrum(org.astrogrid.datacenter.adql.generated.ogsadai.CoordSpectralType) 

    /**
     * Sets the value of field 'time'.
     * 
     * @param time the value of field 'time'.
     */
    public void setTime(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType time)
    {
        this._time = time;
    } //-- void setTime(org.astrogrid.datacenter.adql.generated.ogsadai.CoordTimeType) 

    /**
     * Sets the value of field 'vel2Vector'.
     * 
     * @param vel2Vector the value of field 'vel2Vector'.
     */
    public void setVel2Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType vel2Vector)
    {
        this._vel2Vector = vel2Vector;
    } //-- void setVel2Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Vel2VectorType) 

    /**
     * Sets the value of field 'vel3Vector'.
     * 
     * @param vel3Vector the value of field 'vel3Vector'.
     */
    public void setVel3Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Vel3VectorType vel3Vector)
    {
        this._vel3Vector = vel3Vector;
    } //-- void setVel3Vector(org.astrogrid.datacenter.adql.generated.ogsadai.Vel3VectorType) 

    /**
     * Sets the value of field 'velScalar'.
     * 
     * @param velScalar the value of field 'velScalar'.
     */
    public void setVelScalar(org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType velScalar)
    {
        this._velScalar = velScalar;
    } //-- void setVelScalar(org.astrogrid.datacenter.adql.generated.ogsadai.VelScalarType) 

    /**
     * Method unmarshalCoordsType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType unmarshalCoordsType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.CoordsType unmarshalCoordsType(java.io.Reader) 

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
