/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: UnionType.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class UnionType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class UnionType extends org.astrogrid.datacenter.adql.generated.ogsadai.RegionType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _regionList
     */
    private java.util.Vector _regionList;


      //----------------/
     //- Constructors -/
    //----------------/

    public UnionType() {
        super();
        _regionList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.UnionType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addRegion
     * 
     * @param vRegion
     */
    public void addRegion(org.astrogrid.datacenter.adql.generated.ogsadai.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.addElement(vRegion);
    } //-- void addRegion(org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) 

    /**
     * Method addRegion
     * 
     * @param index
     * @param vRegion
     */
    public void addRegion(int index, org.astrogrid.datacenter.adql.generated.ogsadai.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        _regionList.insertElementAt(vRegion, index);
    } //-- void addRegion(int, org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) 

    /**
     * Method enumerateRegion
     */
    public java.util.Enumeration enumerateRegion()
    {
        return _regionList.elements();
    } //-- java.util.Enumeration enumerateRegion() 

    /**
     * Method getRegion
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.RegionType getRegion(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) _regionList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionType getRegion(int) 

    /**
     * Method getRegion
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.RegionType[] getRegion()
    {
        int size = _regionList.size();
        org.astrogrid.datacenter.adql.generated.ogsadai.RegionType[] mArray = new org.astrogrid.datacenter.adql.generated.ogsadai.RegionType[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) _regionList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionType[] getRegion() 

    /**
     * Method getRegionCount
     */
    public int getRegionCount()
    {
        return _regionList.size();
    } //-- int getRegionCount() 

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
     * Method removeAllRegion
     */
    public void removeAllRegion()
    {
        _regionList.removeAllElements();
    } //-- void removeAllRegion() 

    /**
     * Method removeRegion
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.RegionType removeRegion(int index)
    {
        java.lang.Object obj = _regionList.elementAt(index);
        _regionList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.RegionType removeRegion(int) 

    /**
     * Method setRegion
     * 
     * @param index
     * @param vRegion
     */
    public void setRegion(int index, org.astrogrid.datacenter.adql.generated.ogsadai.RegionType vRegion)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _regionList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _regionList.setElementAt(vRegion, index);
    } //-- void setRegion(int, org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) 

    /**
     * Method setRegion
     * 
     * @param regionArray
     */
    public void setRegion(org.astrogrid.datacenter.adql.generated.ogsadai.RegionType[] regionArray)
    {
        //-- copy array
        _regionList.removeAllElements();
        for (int i = 0; i < regionArray.length; i++) {
            _regionList.addElement(regionArray[i]);
        }
    } //-- void setRegion(org.astrogrid.datacenter.adql.generated.ogsadai.RegionType) 

    /**
     * Method unmarshalUnionType
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.UnionType unmarshalUnionType(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.UnionType) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.UnionType.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.UnionType unmarshalUnionType(java.io.Reader) 

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
