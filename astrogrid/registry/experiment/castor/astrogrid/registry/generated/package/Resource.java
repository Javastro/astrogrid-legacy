/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: Resource.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
import org.astrogrid.registry.generated.package.types.RESOURCETypeType;
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class RESOURCE.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class RESOURCE implements java.io.Serializable {


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
    private org.astrogrid.registry.generated.package.types.RESOURCETypeType _type = org.astrogrid.registry.generated.package.types.RESOURCETypeType.valueOf("results");

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.generated.package.DESCRIPTION _DESCRIPTION;

    /**
     * Field _INFOList
     */
    private java.util.Vector _INFOList;

    /**
     * Field _COOSYSList
     */
    private java.util.Vector _COOSYSList;

    /**
     * Field _PARAMList
     */
    private java.util.Vector _PARAMList;

    /**
     * Field _LINKList
     */
    private java.util.Vector _LINKList;

    /**
     * Field _TABLEList
     */
    private java.util.Vector _TABLEList;

    /**
     * Field _RESOURCEList
     */
    private java.util.Vector _RESOURCEList;


      //----------------/
     //- Constructors -/
    //----------------/

    public RESOURCE() {
        super();
        setType(org.astrogrid.registry.generated.package.types.RESOURCETypeType.valueOf("results"));
        _INFOList = new Vector();
        _COOSYSList = new Vector();
        _PARAMList = new Vector();
        _LINKList = new Vector();
        _TABLEList = new Vector();
        _RESOURCEList = new Vector();
    } //-- org.astrogrid.registry.generated.package.RESOURCE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addCOOSYS
     * 
     * @param vCOOSYS
     */
    public void addCOOSYS(org.astrogrid.registry.generated.package.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        _COOSYSList.addElement(vCOOSYS);
    } //-- void addCOOSYS(org.astrogrid.registry.generated.package.COOSYS) 

    /**
     * Method addCOOSYS
     * 
     * @param index
     * @param vCOOSYS
     */
    public void addCOOSYS(int index, org.astrogrid.registry.generated.package.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        _COOSYSList.insertElementAt(vCOOSYS, index);
    } //-- void addCOOSYS(int, org.astrogrid.registry.generated.package.COOSYS) 

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
     * Method addLINK
     * 
     * @param vLINK
     */
    public void addLINK(org.astrogrid.registry.generated.package.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.addElement(vLINK);
    } //-- void addLINK(org.astrogrid.registry.generated.package.LINK) 

    /**
     * Method addLINK
     * 
     * @param index
     * @param vLINK
     */
    public void addLINK(int index, org.astrogrid.registry.generated.package.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        _LINKList.insertElementAt(vLINK, index);
    } //-- void addLINK(int, org.astrogrid.registry.generated.package.LINK) 

    /**
     * Method addPARAM
     * 
     * @param vPARAM
     */
    public void addPARAM(org.astrogrid.registry.generated.package.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        _PARAMList.addElement(vPARAM);
    } //-- void addPARAM(org.astrogrid.registry.generated.package.PARAM) 

    /**
     * Method addPARAM
     * 
     * @param index
     * @param vPARAM
     */
    public void addPARAM(int index, org.astrogrid.registry.generated.package.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        _PARAMList.insertElementAt(vPARAM, index);
    } //-- void addPARAM(int, org.astrogrid.registry.generated.package.PARAM) 

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
     * Method addTABLE
     * 
     * @param vTABLE
     */
    public void addTABLE(org.astrogrid.registry.generated.package.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        _TABLEList.addElement(vTABLE);
    } //-- void addTABLE(org.astrogrid.registry.generated.package.TABLE) 

    /**
     * Method addTABLE
     * 
     * @param index
     * @param vTABLE
     */
    public void addTABLE(int index, org.astrogrid.registry.generated.package.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        _TABLEList.insertElementAt(vTABLE, index);
    } //-- void addTABLE(int, org.astrogrid.registry.generated.package.TABLE) 

    /**
     * Method enumerateCOOSYS
     */
    public java.util.Enumeration enumerateCOOSYS()
    {
        return _COOSYSList.elements();
    } //-- java.util.Enumeration enumerateCOOSYS() 

    /**
     * Method enumerateINFO
     */
    public java.util.Enumeration enumerateINFO()
    {
        return _INFOList.elements();
    } //-- java.util.Enumeration enumerateINFO() 

    /**
     * Method enumerateLINK
     */
    public java.util.Enumeration enumerateLINK()
    {
        return _LINKList.elements();
    } //-- java.util.Enumeration enumerateLINK() 

    /**
     * Method enumeratePARAM
     */
    public java.util.Enumeration enumeratePARAM()
    {
        return _PARAMList.elements();
    } //-- java.util.Enumeration enumeratePARAM() 

    /**
     * Method enumerateRESOURCE
     */
    public java.util.Enumeration enumerateRESOURCE()
    {
        return _RESOURCEList.elements();
    } //-- java.util.Enumeration enumerateRESOURCE() 

    /**
     * Method enumerateTABLE
     */
    public java.util.Enumeration enumerateTABLE()
    {
        return _TABLEList.elements();
    } //-- java.util.Enumeration enumerateTABLE() 

    /**
     * Method getCOOSYS
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.COOSYS getCOOSYS(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _COOSYSList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.COOSYS) _COOSYSList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.COOSYS getCOOSYS(int) 

    /**
     * Method getCOOSYS
     */
    public org.astrogrid.registry.generated.package.COOSYS[] getCOOSYS()
    {
        int size = _COOSYSList.size();
        org.astrogrid.registry.generated.package.COOSYS[] mArray = new org.astrogrid.registry.generated.package.COOSYS[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.COOSYS) _COOSYSList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.COOSYS[] getCOOSYS() 

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
     * Method getLINK
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.LINK getLINK(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.LINK) _LINKList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.LINK getLINK(int) 

    /**
     * Method getLINK
     */
    public org.astrogrid.registry.generated.package.LINK[] getLINK()
    {
        int size = _LINKList.size();
        org.astrogrid.registry.generated.package.LINK[] mArray = new org.astrogrid.registry.generated.package.LINK[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.LINK) _LINKList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.LINK[] getLINK() 

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
    public org.astrogrid.registry.generated.package.PARAM getPARAM(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _PARAMList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.PARAM) _PARAMList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.PARAM getPARAM(int) 

    /**
     * Method getPARAM
     */
    public org.astrogrid.registry.generated.package.PARAM[] getPARAM()
    {
        int size = _PARAMList.size();
        org.astrogrid.registry.generated.package.PARAM[] mArray = new org.astrogrid.registry.generated.package.PARAM[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.PARAM) _PARAMList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.PARAM[] getPARAM() 

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
     * Method getTABLE
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TABLE getTABLE(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TABLEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.TABLE) _TABLEList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.TABLE getTABLE(int) 

    /**
     * Method getTABLE
     */
    public org.astrogrid.registry.generated.package.TABLE[] getTABLE()
    {
        int size = _TABLEList.size();
        org.astrogrid.registry.generated.package.TABLE[] mArray = new org.astrogrid.registry.generated.package.TABLE[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.TABLE) _TABLEList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.TABLE[] getTABLE() 

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
    public org.astrogrid.registry.generated.package.types.RESOURCETypeType getType()
    {
        return this._type;
    } //-- org.astrogrid.registry.generated.package.types.RESOURCETypeType getType() 

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
     * Method removeAllCOOSYS
     */
    public void removeAllCOOSYS()
    {
        _COOSYSList.removeAllElements();
    } //-- void removeAllCOOSYS() 

    /**
     * Method removeAllINFO
     */
    public void removeAllINFO()
    {
        _INFOList.removeAllElements();
    } //-- void removeAllINFO() 

    /**
     * Method removeAllLINK
     */
    public void removeAllLINK()
    {
        _LINKList.removeAllElements();
    } //-- void removeAllLINK() 

    /**
     * Method removeAllPARAM
     */
    public void removeAllPARAM()
    {
        _PARAMList.removeAllElements();
    } //-- void removeAllPARAM() 

    /**
     * Method removeAllRESOURCE
     */
    public void removeAllRESOURCE()
    {
        _RESOURCEList.removeAllElements();
    } //-- void removeAllRESOURCE() 

    /**
     * Method removeAllTABLE
     */
    public void removeAllTABLE()
    {
        _TABLEList.removeAllElements();
    } //-- void removeAllTABLE() 

    /**
     * Method removeCOOSYS
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.COOSYS removeCOOSYS(int index)
    {
        java.lang.Object obj = _COOSYSList.elementAt(index);
        _COOSYSList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.COOSYS) obj;
    } //-- org.astrogrid.registry.generated.package.COOSYS removeCOOSYS(int) 

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
     * Method removeLINK
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.LINK removeLINK(int index)
    {
        java.lang.Object obj = _LINKList.elementAt(index);
        _LINKList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.LINK) obj;
    } //-- org.astrogrid.registry.generated.package.LINK removeLINK(int) 

    /**
     * Method removePARAM
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.PARAM removePARAM(int index)
    {
        java.lang.Object obj = _PARAMList.elementAt(index);
        _PARAMList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.PARAM) obj;
    } //-- org.astrogrid.registry.generated.package.PARAM removePARAM(int) 

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
     * Method removeTABLE
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TABLE removeTABLE(int index)
    {
        java.lang.Object obj = _TABLEList.elementAt(index);
        _TABLEList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.TABLE) obj;
    } //-- org.astrogrid.registry.generated.package.TABLE removeTABLE(int) 

    /**
     * Method setCOOSYS
     * 
     * @param index
     * @param vCOOSYS
     */
    public void setCOOSYS(int index, org.astrogrid.registry.generated.package.COOSYS vCOOSYS)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _COOSYSList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _COOSYSList.setElementAt(vCOOSYS, index);
    } //-- void setCOOSYS(int, org.astrogrid.registry.generated.package.COOSYS) 

    /**
     * Method setCOOSYS
     * 
     * @param COOSYSArray
     */
    public void setCOOSYS(org.astrogrid.registry.generated.package.COOSYS[] COOSYSArray)
    {
        //-- copy array
        _COOSYSList.removeAllElements();
        for (int i = 0; i < COOSYSArray.length; i++) {
            _COOSYSList.addElement(COOSYSArray[i]);
        }
    } //-- void setCOOSYS(org.astrogrid.registry.generated.package.COOSYS) 

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
     * Method setLINK
     * 
     * @param index
     * @param vLINK
     */
    public void setLINK(int index, org.astrogrid.registry.generated.package.LINK vLINK)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _LINKList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _LINKList.setElementAt(vLINK, index);
    } //-- void setLINK(int, org.astrogrid.registry.generated.package.LINK) 

    /**
     * Method setLINK
     * 
     * @param LINKArray
     */
    public void setLINK(org.astrogrid.registry.generated.package.LINK[] LINKArray)
    {
        //-- copy array
        _LINKList.removeAllElements();
        for (int i = 0; i < LINKArray.length; i++) {
            _LINKList.addElement(LINKArray[i]);
        }
    } //-- void setLINK(org.astrogrid.registry.generated.package.LINK) 

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
    public void setPARAM(int index, org.astrogrid.registry.generated.package.PARAM vPARAM)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _PARAMList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _PARAMList.setElementAt(vPARAM, index);
    } //-- void setPARAM(int, org.astrogrid.registry.generated.package.PARAM) 

    /**
     * Method setPARAM
     * 
     * @param PARAMArray
     */
    public void setPARAM(org.astrogrid.registry.generated.package.PARAM[] PARAMArray)
    {
        //-- copy array
        _PARAMList.removeAllElements();
        for (int i = 0; i < PARAMArray.length; i++) {
            _PARAMList.addElement(PARAMArray[i]);
        }
    } //-- void setPARAM(org.astrogrid.registry.generated.package.PARAM) 

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
     * Method setTABLE
     * 
     * @param index
     * @param vTABLE
     */
    public void setTABLE(int index, org.astrogrid.registry.generated.package.TABLE vTABLE)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TABLEList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TABLEList.setElementAt(vTABLE, index);
    } //-- void setTABLE(int, org.astrogrid.registry.generated.package.TABLE) 

    /**
     * Method setTABLE
     * 
     * @param TABLEArray
     */
    public void setTABLE(org.astrogrid.registry.generated.package.TABLE[] TABLEArray)
    {
        //-- copy array
        _TABLEList.removeAllElements();
        for (int i = 0; i < TABLEArray.length; i++) {
            _TABLEList.addElement(TABLEArray[i]);
        }
    } //-- void setTABLE(org.astrogrid.registry.generated.package.TABLE) 

    /**
     * Sets the value of field 'type'.
     * 
     * @param type the value of field 'type'.
     */
    public void setType(org.astrogrid.registry.generated.package.types.RESOURCETypeType type)
    {
        this._type = type;
    } //-- void setType(org.astrogrid.registry.generated.package.types.RESOURCETypeType) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.RESOURCE) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.RESOURCE.class, reader);
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
