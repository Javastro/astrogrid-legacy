/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: VOTABLE.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Vector;
import org.astrogrid.registry.generated.package.types.VOTABLEVersionType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class VOTABLE.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class VOTABLE implements java.io.Serializable {


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
    private org.astrogrid.registry.generated.package.types.VOTABLEVersionType _version;

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.generated.package.DESCRIPTION _DESCRIPTION;

    /**
     * Field _DEFINITIONS
     */
    private org.astrogrid.registry.generated.package.DEFINITIONS _DEFINITIONS;

    /**
     * Field _INFOList
     */
    private java.util.Vector _INFOList;

    /**
     * Field _RESOURCEList
     */
    private java.util.Vector _RESOURCEList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOTABLE() {
        super();
        _INFOList = new Vector();
        _RESOURCEList = new Vector();
    } //-- org.astrogrid.registry.generated.package.VOTABLE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addINFO
     * 
     * @param vINFO
     */
    public void addINFO(org.astrogrid.registry.generated.package.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        _INFOList.addElement(vINFO);
    } //-- void addINFO(org.astrogrid.registry.generated.package.INFO) 

    /**
     * Method addINFO
     * 
     * @param index
     * @param vINFO
     */
    public void addINFO(int index, org.astrogrid.registry.generated.package.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        _INFOList.insertElementAt(vINFO, index);
    } //-- void addINFO(int, org.astrogrid.registry.generated.package.INFO) 

    /**
     * Method addRESOURCE
     * 
     * @param vRESOURCE
     */
    public void addRESOURCE(org.astrogrid.registry.generated.package.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        _RESOURCEList.addElement(vRESOURCE);
    } //-- void addRESOURCE(org.astrogrid.registry.generated.package.RESOURCE) 

    /**
     * Method addRESOURCE
     * 
     * @param index
     * @param vRESOURCE
     */
    public void addRESOURCE(int index, org.astrogrid.registry.generated.package.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        _RESOURCEList.insertElementAt(vRESOURCE, index);
    } //-- void addRESOURCE(int, org.astrogrid.registry.generated.package.RESOURCE) 

    /**
     * Method enumerateINFO
     */
    public java.util.Enumeration enumerateINFO()
    {
        return _INFOList.elements();
    } //-- java.util.Enumeration enumerateINFO() 

    /**
     * Method enumerateRESOURCE
     */
    public java.util.Enumeration enumerateRESOURCE()
    {
        return _RESOURCEList.elements();
    } //-- java.util.Enumeration enumerateRESOURCE() 

    /**
     * Returns the value of field 'DEFINITIONS'.
     * 
     * @return the value of field 'DEFINITIONS'.
     */
    public org.astrogrid.registry.generated.package.DEFINITIONS getDEFINITIONS()
    {
        return this._DEFINITIONS;
    } //-- org.astrogrid.registry.generated.package.DEFINITIONS getDEFINITIONS() 

    /**
     * Returns the value of field 'DESCRIPTION'.
     * 
     * @return the value of field 'DESCRIPTION'.
     */
    public org.astrogrid.registry.generated.package.DESCRIPTION getDESCRIPTION()
    {
        return this._DESCRIPTION;
    } //-- org.astrogrid.registry.generated.package.DESCRIPTION getDESCRIPTION() 

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
    public org.astrogrid.registry.generated.package.INFO getINFO(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _INFOList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.INFO) _INFOList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.INFO getINFO(int) 

    /**
     * Method getINFO
     */
    public org.astrogrid.registry.generated.package.INFO[] getINFO()
    {
        int size = _INFOList.size();
        org.astrogrid.registry.generated.package.INFO[] mArray = new org.astrogrid.registry.generated.package.INFO[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.INFO) _INFOList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.INFO[] getINFO() 

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
    public org.astrogrid.registry.generated.package.RESOURCE getRESOURCE(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _RESOURCEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.RESOURCE) _RESOURCEList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.RESOURCE getRESOURCE(int) 

    /**
     * Method getRESOURCE
     */
    public org.astrogrid.registry.generated.package.RESOURCE[] getRESOURCE()
    {
        int size = _RESOURCEList.size();
        org.astrogrid.registry.generated.package.RESOURCE[] mArray = new org.astrogrid.registry.generated.package.RESOURCE[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.RESOURCE) _RESOURCEList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.RESOURCE[] getRESOURCE() 

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
    public org.astrogrid.registry.generated.package.types.VOTABLEVersionType getVersion()
    {
        return this._version;
    } //-- org.astrogrid.registry.generated.package.types.VOTABLEVersionType getVersion() 

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
     * Method removeAllINFO
     */
    public void removeAllINFO()
    {
        _INFOList.removeAllElements();
    } //-- void removeAllINFO() 

    /**
     * Method removeAllRESOURCE
     */
    public void removeAllRESOURCE()
    {
        _RESOURCEList.removeAllElements();
    } //-- void removeAllRESOURCE() 

    /**
     * Method removeINFO
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.INFO removeINFO(int index)
    {
        java.lang.Object obj = _INFOList.elementAt(index);
        _INFOList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.INFO) obj;
    } //-- org.astrogrid.registry.generated.package.INFO removeINFO(int) 

    /**
     * Method removeRESOURCE
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.RESOURCE removeRESOURCE(int index)
    {
        java.lang.Object obj = _RESOURCEList.elementAt(index);
        _RESOURCEList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.RESOURCE) obj;
    } //-- org.astrogrid.registry.generated.package.RESOURCE removeRESOURCE(int) 

    /**
     * Sets the value of field 'DEFINITIONS'.
     * 
     * @param DEFINITIONS the value of field 'DEFINITIONS'.
     */
    public void setDEFINITIONS(org.astrogrid.registry.generated.package.DEFINITIONS DEFINITIONS)
    {
        this._DEFINITIONS = DEFINITIONS;
    } //-- void setDEFINITIONS(org.astrogrid.registry.generated.package.DEFINITIONS) 

    /**
     * Sets the value of field 'DESCRIPTION'.
     * 
     * @param DESCRIPTION the value of field 'DESCRIPTION'.
     */
    public void setDESCRIPTION(org.astrogrid.registry.generated.package.DESCRIPTION DESCRIPTION)
    {
        this._DESCRIPTION = DESCRIPTION;
    } //-- void setDESCRIPTION(org.astrogrid.registry.generated.package.DESCRIPTION) 

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
    public void setINFO(int index, org.astrogrid.registry.generated.package.INFO vINFO)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _INFOList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _INFOList.setElementAt(vINFO, index);
    } //-- void setINFO(int, org.astrogrid.registry.generated.package.INFO) 

    /**
     * Method setINFO
     * 
     * @param INFOArray
     */
    public void setINFO(org.astrogrid.registry.generated.package.INFO[] INFOArray)
    {
        //-- copy array
        _INFOList.removeAllElements();
        for (int i = 0; i < INFOArray.length; i++) {
            _INFOList.addElement(INFOArray[i]);
        }
    } //-- void setINFO(org.astrogrid.registry.generated.package.INFO) 

    /**
     * Method setRESOURCE
     * 
     * @param index
     * @param vRESOURCE
     */
    public void setRESOURCE(int index, org.astrogrid.registry.generated.package.RESOURCE vRESOURCE)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _RESOURCEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _RESOURCEList.setElementAt(vRESOURCE, index);
    } //-- void setRESOURCE(int, org.astrogrid.registry.generated.package.RESOURCE) 

    /**
     * Method setRESOURCE
     * 
     * @param RESOURCEArray
     */
    public void setRESOURCE(org.astrogrid.registry.generated.package.RESOURCE[] RESOURCEArray)
    {
        //-- copy array
        _RESOURCEList.removeAllElements();
        for (int i = 0; i < RESOURCEArray.length; i++) {
            _RESOURCEList.addElement(RESOURCEArray[i]);
        }
    } //-- void setRESOURCE(org.astrogrid.registry.generated.package.RESOURCE) 

    /**
     * Sets the value of field 'version'.
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(org.astrogrid.registry.generated.package.types.VOTABLEVersionType version)
    {
        this._version = version;
    } //-- void setVersion(org.astrogrid.registry.generated.package.types.VOTABLEVersionType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.VOTABLE) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.VOTABLE.class, reader);
    } //-- java.lang.Object unmarshal(java.io.Reader) 

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
