/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RESOURCE.java,v 1.8 2004/04/05 14:36:11 KevinBenson Exp $
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
import org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class RESOURCE.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:11 $
 */
public class RESOURCE extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _type
     */
    private org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType _type = org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType.valueOf("results");

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.beans.resource.votable.AnyTEXT _DESCRIPTION;

    /**
     * Field _INFOList
     */
    private java.util.ArrayList _INFOList;

    /**
     * Field _COOSYSList
     */
    private java.util.ArrayList _COOSYSList;

    /**
     * Field _PARAMList
     */
    private java.util.ArrayList _PARAMList;

    /**
     * Field _LINKList
     */
    private java.util.ArrayList _LINKList;

    /**
     * Field _TABLEList
     */
    private java.util.ArrayList _TABLEList;

    /**
     * Field _RESOURCEList
     */
    private java.util.ArrayList _RESOURCEList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RESOURCE() {
        super();
        setType(org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType.valueOf("results"));
        _INFOList = new ArrayList();
        _COOSYSList = new ArrayList();
        _PARAMList = new ArrayList();
        _LINKList = new ArrayList();
        _TABLEList = new ArrayList();
        _RESOURCEList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.RESOURCE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCOOSYS
     * 
     * @param vCOOSYS
     */
    public void addCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        _COOSYSList.add(vCOOSYS);
    } //-- void addCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS) 

    /**
     * Method addCOOSYS
     * 
     * @param index
     * @param vCOOSYS
     */
    public void addCOOSYS(int index, org.astrogrid.registry.beans.resource.votable.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        _COOSYSList.add(index, vCOOSYS);
    } //-- void addCOOSYS(int, org.astrogrid.registry.beans.resource.votable.COOSYS) 

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
     * Method addLINK
     * 
     * @param vLINK
     */
    public void addLINK(org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.add(vLINK);
    } //-- void addLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method addLINK
     * 
     * @param index
     * @param vLINK
     */
    public void addLINK(int index, org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.add(index, vLINK);
    } //-- void addLINK(int, org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method addPARAM
     * 
     * @param vPARAM
     */
    public void addPARAM(org.astrogrid.registry.beans.resource.votable.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        _PARAMList.add(vPARAM);
    } //-- void addPARAM(org.astrogrid.registry.beans.resource.votable.PARAM) 

    /**
     * Method addPARAM
     * 
     * @param index
     * @param vPARAM
     */
    public void addPARAM(int index, org.astrogrid.registry.beans.resource.votable.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        _PARAMList.add(index, vPARAM);
    } //-- void addPARAM(int, org.astrogrid.registry.beans.resource.votable.PARAM) 

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
     * Method addTABLE
     * 
     * @param vTABLE
     */
    public void addTABLE(org.astrogrid.registry.beans.resource.votable.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        _TABLEList.add(vTABLE);
    } //-- void addTABLE(org.astrogrid.registry.beans.resource.votable.TABLE) 

    /**
     * Method addTABLE
     * 
     * @param index
     * @param vTABLE
     */
    public void addTABLE(int index, org.astrogrid.registry.beans.resource.votable.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        _TABLEList.add(index, vTABLE);
    } //-- void addTABLE(int, org.astrogrid.registry.beans.resource.votable.TABLE) 

    /**
     * Method clearCOOSYS
     */
    public void clearCOOSYS()
    {
        _COOSYSList.clear();
    } //-- void clearCOOSYS() 

    /**
     * Method clearINFO
     */
    public void clearINFO()
    {
        _INFOList.clear();
    } //-- void clearINFO() 

    /**
     * Method clearLINK
     */
    public void clearLINK()
    {
        _LINKList.clear();
    } //-- void clearLINK() 

    /**
     * Method clearPARAM
     */
    public void clearPARAM()
    {
        _PARAMList.clear();
    } //-- void clearPARAM() 

    /**
     * Method clearRESOURCE
     */
    public void clearRESOURCE()
    {
        _RESOURCEList.clear();
    } //-- void clearRESOURCE() 

    /**
     * Method clearTABLE
     */
    public void clearTABLE()
    {
        _TABLEList.clear();
    } //-- void clearTABLE() 

    /**
     * Method enumerateCOOSYS
     */
    public java.util.Enumeration enumerateCOOSYS()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_COOSYSList.iterator());
    } //-- java.util.Enumeration enumerateCOOSYS() 

    /**
     * Method enumerateINFO
     */
    public java.util.Enumeration enumerateINFO()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_INFOList.iterator());
    } //-- java.util.Enumeration enumerateINFO() 

    /**
     * Method enumerateLINK
     */
    public java.util.Enumeration enumerateLINK()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_LINKList.iterator());
    } //-- java.util.Enumeration enumerateLINK() 

    /**
     * Method enumeratePARAM
     */
    public java.util.Enumeration enumeratePARAM()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_PARAMList.iterator());
    } //-- java.util.Enumeration enumeratePARAM() 

    /**
     * Method enumerateRESOURCE
     */
    public java.util.Enumeration enumerateRESOURCE()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_RESOURCEList.iterator());
    } //-- java.util.Enumeration enumerateRESOURCE() 

    /**
     * Method enumerateTABLE
     */
    public java.util.Enumeration enumerateTABLE()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_TABLEList.iterator());
    } //-- java.util.Enumeration enumerateTABLE() 

    /**
     * Method getCOOSYS
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.COOSYS getCOOSYS(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _COOSYSList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.COOSYS) _COOSYSList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.COOSYS getCOOSYS(int) 

    /**
     * Method getCOOSYS
     */
    public org.astrogrid.registry.beans.resource.votable.COOSYS[] getCOOSYS()
    {
        int size = _COOSYSList.size();
        org.astrogrid.registry.beans.resource.votable.COOSYS[] mArray = new org.astrogrid.registry.beans.resource.votable.COOSYS[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.COOSYS) _COOSYSList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.COOSYS[] getCOOSYS() 

    /**
     * Method getCOOSYSCount
     */
    public int getCOOSYSCount()
    {
        return _COOSYSList.size();
    } //-- int getCOOSYSCount() 

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
     * Method getLINK
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.LINK getLINK(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.LINK) _LINKList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.LINK getLINK(int) 

    /**
     * Method getLINK
     */
    public org.astrogrid.registry.beans.resource.votable.LINK[] getLINK()
    {
        int size = _LINKList.size();
        org.astrogrid.registry.beans.resource.votable.LINK[] mArray = new org.astrogrid.registry.beans.resource.votable.LINK[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.LINK) _LINKList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.LINK[] getLINK() 

    /**
     * Method getLINKCount
     */
    public int getLINKCount()
    {
        return _LINKList.size();
    } //-- int getLINKCount() 

    /**
     * Returns the value of field 'name'.
     * 
     * @return the value of field 'name'.
     */
    public java.lang.String getName()
    {
        return this._name;
    } //-- java.lang.String getName() 

    /**
     * Method getPARAM
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.PARAM getPARAM(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _PARAMList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.PARAM) _PARAMList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.PARAM getPARAM(int) 

    /**
     * Method getPARAM
     */
    public org.astrogrid.registry.beans.resource.votable.PARAM[] getPARAM()
    {
        int size = _PARAMList.size();
        org.astrogrid.registry.beans.resource.votable.PARAM[] mArray = new org.astrogrid.registry.beans.resource.votable.PARAM[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.PARAM) _PARAMList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.PARAM[] getPARAM() 

    /**
     * Method getPARAMCount
     */
    public int getPARAMCount()
    {
        return _PARAMList.size();
    } //-- int getPARAMCount() 

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
     * Method getTABLE
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.TABLE getTABLE(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TABLEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.TABLE) _TABLEList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.TABLE getTABLE(int) 

    /**
     * Method getTABLE
     */
    public org.astrogrid.registry.beans.resource.votable.TABLE[] getTABLE()
    {
        int size = _TABLEList.size();
        org.astrogrid.registry.beans.resource.votable.TABLE[] mArray = new org.astrogrid.registry.beans.resource.votable.TABLE[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.TABLE) _TABLEList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.TABLE[] getTABLE() 

    /**
     * Method getTABLECount
     */
    public int getTABLECount()
    {
        return _TABLEList.size();
    } //-- int getTABLECount() 

    /**
     * Returns the value of field 'type'.
     * 
     * @return the value of field 'type'.
     */
    public org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType getType() 

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
     * Method removeCOOSYS
     * 
     * @param vCOOSYS
     */
    public boolean removeCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS vCOOSYS)
    {
        boolean removed = _COOSYSList.remove(vCOOSYS);
        return removed;
    } //-- boolean removeCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS) 

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
     * Method removeLINK
     * 
     * @param vLINK
     */
    public boolean removeLINK(org.astrogrid.registry.beans.resource.votable.LINK vLINK)
    {
        boolean removed = _LINKList.remove(vLINK);
        return removed;
    } //-- boolean removeLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method removePARAM
     * 
     * @param vPARAM
     */
    public boolean removePARAM(org.astrogrid.registry.beans.resource.votable.PARAM vPARAM)
    {
        boolean removed = _PARAMList.remove(vPARAM);
        return removed;
    } //-- boolean removePARAM(org.astrogrid.registry.beans.resource.votable.PARAM) 

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
     * Method removeTABLE
     * 
     * @param vTABLE
     */
    public boolean removeTABLE(org.astrogrid.registry.beans.resource.votable.TABLE vTABLE)
    {
        boolean removed = _TABLEList.remove(vTABLE);
        return removed;
    } //-- boolean removeTABLE(org.astrogrid.registry.beans.resource.votable.TABLE) 

    /**
     * Method setCOOSYS
     * 
     * @param index
     * @param vCOOSYS
     */
    public void setCOOSYS(int index, org.astrogrid.registry.beans.resource.votable.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _COOSYSList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _COOSYSList.set(index, vCOOSYS);
    } //-- void setCOOSYS(int, org.astrogrid.registry.beans.resource.votable.COOSYS) 

    /**
     * Method setCOOSYS
     * 
     * @param COOSYSArray
     */
    public void setCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS[] COOSYSArray)
    {
        //-- copy array
        _COOSYSList.clear();
        for (int i = 0; i < COOSYSArray.length; i++) {
            _COOSYSList.add(COOSYSArray[i]);
        }
    } //-- void setCOOSYS(org.astrogrid.registry.beans.resource.votable.COOSYS) 

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
     * Method setLINK
     * 
     * @param index
     * @param vLINK
     */
    public void setLINK(int index, org.astrogrid.registry.beans.resource.votable.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _LINKList.set(index, vLINK);
    } //-- void setLINK(int, org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method setLINK
     * 
     * @param LINKArray
     */
    public void setLINK(org.astrogrid.registry.beans.resource.votable.LINK[] LINKArray)
    {
        //-- copy array
        _LINKList.clear();
        for (int i = 0; i < LINKArray.length; i++) {
            _LINKList.add(LINKArray[i]);
        }
    } //-- void setLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Sets the value of field 'name'.
     * 
     * @param name the value of field 'name'.
     */
    public void setName(java.lang.String name)
    {
        this._name = name;
    } //-- void setName(java.lang.String) 

    /**
     * Method setPARAM
     * 
     * @param index
     * @param vPARAM
     */
    public void setPARAM(int index, org.astrogrid.registry.beans.resource.votable.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _PARAMList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _PARAMList.set(index, vPARAM);
    } //-- void setPARAM(int, org.astrogrid.registry.beans.resource.votable.PARAM) 

    /**
     * Method setPARAM
     * 
     * @param PARAMArray
     */
    public void setPARAM(org.astrogrid.registry.beans.resource.votable.PARAM[] PARAMArray)
    {
        //-- copy array
        _PARAMList.clear();
        for (int i = 0; i < PARAMArray.length; i++) {
            _PARAMList.add(PARAMArray[i]);
        }
    } //-- void setPARAM(org.astrogrid.registry.beans.resource.votable.PARAM) 

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
     * Method setTABLE
     * 
     * @param index
     * @param vTABLE
     */
    public void setTABLE(int index, org.astrogrid.registry.beans.resource.votable.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TABLEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TABLEList.set(index, vTABLE);
    } //-- void setTABLE(int, org.astrogrid.registry.beans.resource.votable.TABLE) 

    /**
     * Method setTABLE
     * 
     * @param TABLEArray
     */
    public void setTABLE(org.astrogrid.registry.beans.resource.votable.TABLE[] TABLEArray)
    {
        //-- copy array
        _TABLEList.clear();
        for (int i = 0; i < TABLEArray.length; i++) {
            _TABLEList.add(TABLEArray[i]);
        }
    } //-- void setTABLE(org.astrogrid.registry.beans.resource.votable.TABLE) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType) 

    /**
     * Method unmarshalRESOURCE
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.RESOURCE unmarshalRESOURCE(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.RESOURCE) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.RESOURCE.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.RESOURCE unmarshalRESOURCE(java.io.Reader) 

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
