/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VOTABLE.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class VOTABLE.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class VOTABLE extends org.astrogrid.common.bean.BaseBean 
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
     * Field _version
     */
    private org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType _version;

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.beans.resource.votable.AnyTEXT _DESCRIPTION;

    /**
     * Field _DEFINITIONS
     */
    private org.astrogrid.registry.beans.resource.votable.DEFINITIONS _DEFINITIONS;

    /**
     * Field _INFOList
     */
    private java.util.ArrayList _INFOList;

    /**
     * Field _RESOURCEList
     */
    private java.util.ArrayList _RESOURCEList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOTABLE() {
        super();
        _INFOList = new ArrayList();
        _RESOURCEList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.VOTABLE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addINFO
     * 
     * @param vINFO
     */
    public void addINFO(org.astrogrid.registry.beans.resource.votable.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        _INFOList.add(vINFO);
    } //-- void addINFO(org.astrogrid.registry.beans.resource.votable.INFO) 

    /**
     * Method addINFO
     * 
     * @param index
     * @param vINFO
     */
    public void addINFO(int index, org.astrogrid.registry.beans.resource.votable.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        _INFOList.add(index, vINFO);
    } //-- void addINFO(int, org.astrogrid.registry.beans.resource.votable.INFO) 

    /**
     * Method addRESOURCE
     * 
     * @param vRESOURCE
     */
    public void addRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        _RESOURCEList.add(vRESOURCE);
    } //-- void addRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE) 

    /**
     * Method addRESOURCE
     * 
     * @param index
     * @param vRESOURCE
     */
    public void addRESOURCE(int index, org.astrogrid.registry.beans.resource.votable.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        _RESOURCEList.add(index, vRESOURCE);
    } //-- void addRESOURCE(int, org.astrogrid.registry.beans.resource.votable.RESOURCE) 

    /**
     * Method clearINFO
     */
    public void clearINFO()
    {
        _INFOList.clear();
    } //-- void clearINFO() 

    /**
     * Method clearRESOURCE
     */
    public void clearRESOURCE()
    {
        _RESOURCEList.clear();
    } //-- void clearRESOURCE() 

    /**
     * Method enumerateINFO
     */
    public java.util.Enumeration enumerateINFO()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_INFOList.iterator());
    } //-- java.util.Enumeration enumerateINFO() 

    /**
     * Method enumerateRESOURCE
     */
    public java.util.Enumeration enumerateRESOURCE()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_RESOURCEList.iterator());
    } //-- java.util.Enumeration enumerateRESOURCE() 

    /**
     * Returns the value of field 'DEFINITIONS'.
     * 
     * @return the value of field 'DEFINITIONS'.
     */
    public org.astrogrid.registry.beans.resource.votable.DEFINITIONS getDEFINITIONS()
    {
        return this._DEFINITIONS;
    } //-- org.astrogrid.registry.beans.resource.votable.DEFINITIONS getDEFINITIONS() 

    /**
     * Returns the value of field 'DESCRIPTION'.
     * 
     * @return the value of field 'DESCRIPTION'.
     */
    public org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION()
    {
        return this._DESCRIPTION;
    } //-- org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION() 

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
     * Method getINFO
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.INFO getINFO(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _INFOList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.INFO) _INFOList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.INFO getINFO(int) 

    /**
     * Method getINFO
     */
    public org.astrogrid.registry.beans.resource.votable.INFO[] getINFO()
    {
        int size = _INFOList.size();
        org.astrogrid.registry.beans.resource.votable.INFO[] mArray = new org.astrogrid.registry.beans.resource.votable.INFO[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.INFO) _INFOList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.INFO[] getINFO() 

    /**
     * Method getINFOCount
     */
    public int getINFOCount()
    {
        return _INFOList.size();
    } //-- int getINFOCount() 

    /**
     * Method getRESOURCE
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.RESOURCE getRESOURCE(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _RESOURCEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.RESOURCE) _RESOURCEList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.RESOURCE getRESOURCE(int) 

    /**
     * Method getRESOURCE
     */
    public org.astrogrid.registry.beans.resource.votable.RESOURCE[] getRESOURCE()
    {
        int size = _RESOURCEList.size();
        org.astrogrid.registry.beans.resource.votable.RESOURCE[] mArray = new org.astrogrid.registry.beans.resource.votable.RESOURCE[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.RESOURCE) _RESOURCEList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.RESOURCE[] getRESOURCE() 

    /**
     * Method getRESOURCECount
     */
    public int getRESOURCECount()
    {
        return _RESOURCEList.size();
    } //-- int getRESOURCECount() 

    /**
     * Returns the value of field 'version'.
     * 
     * @return the value of field 'version'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType getVersion()
    {
        return this._version;
    } //-- org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType getVersion() 

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
     * Method removeINFO
     * 
     * @param vINFO
     */
    public boolean removeINFO(org.astrogrid.registry.beans.resource.votable.INFO vINFO)
    {
        boolean removed = _INFOList.remove(vINFO);
        return removed;
    } //-- boolean removeINFO(org.astrogrid.registry.beans.resource.votable.INFO) 

    /**
     * Method removeRESOURCE
     * 
     * @param vRESOURCE
     */
    public boolean removeRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE vRESOURCE)
    {
        boolean removed = _RESOURCEList.remove(vRESOURCE);
        return removed;
    } //-- boolean removeRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE) 

    /**
     * Sets the value of field 'DEFINITIONS'.
     * 
     * @param DEFINITIONS the value of field 'DEFINITIONS'.
     */
    public void setDEFINITIONS(org.astrogrid.registry.beans.resource.votable.DEFINITIONS DEFINITIONS)
    {
        this._DEFINITIONS = DEFINITIONS;
    } //-- void setDEFINITIONS(org.astrogrid.registry.beans.resource.votable.DEFINITIONS) 

    /**
     * Sets the value of field 'DESCRIPTION'.
     * 
     * @param DESCRIPTION the value of field 'DESCRIPTION'.
     */
    public void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT DESCRIPTION)
    {
        this._DESCRIPTION = DESCRIPTION;
    } //-- void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT) 

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
     * Method setINFO
     * 
     * @param index
     * @param vINFO
     */
    public void setINFO(int index, org.astrogrid.registry.beans.resource.votable.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _INFOList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _INFOList.set(index, vINFO);
    } //-- void setINFO(int, org.astrogrid.registry.beans.resource.votable.INFO) 

    /**
     * Method setINFO
     * 
     * @param INFOArray
     */
    public void setINFO(org.astrogrid.registry.beans.resource.votable.INFO[] INFOArray)
    {
        //-- copy array
        _INFOList.clear();
        for (int i = 0; i < INFOArray.length; i++) {
            _INFOList.add(INFOArray[i]);
        }
    } //-- void setINFO(org.astrogrid.registry.beans.resource.votable.INFO) 

    /**
     * Method setRESOURCE
     * 
     * @param index
     * @param vRESOURCE
     */
    public void setRESOURCE(int index, org.astrogrid.registry.beans.resource.votable.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _RESOURCEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _RESOURCEList.set(index, vRESOURCE);
    } //-- void setRESOURCE(int, org.astrogrid.registry.beans.resource.votable.RESOURCE) 

    /**
     * Method setRESOURCE
     * 
     * @param RESOURCEArray
     */
    public void setRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE[] RESOURCEArray)
    {
        //-- copy array
        _RESOURCEList.clear();
        for (int i = 0; i < RESOURCEArray.length; i++) {
            _RESOURCEList.add(RESOURCEArray[i]);
        }
    } //-- void setRESOURCE(org.astrogrid.registry.beans.resource.votable.RESOURCE) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType version)
    {
        this._version = version;
    } //-- void setVersion(org.astrogrid.registry.beans.resource.votable.types.VOTABLEVersionType) 

    /**
     * Method unmarshalVOTABLE
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.VOTABLE unmarshalVOTABLE(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.VOTABLE) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.VOTABLE.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.VOTABLE unmarshalVOTABLE(java.io.Reader) 

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
