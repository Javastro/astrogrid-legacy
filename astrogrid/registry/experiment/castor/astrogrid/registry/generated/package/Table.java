/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: Table.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TABLE.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class TABLE implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _ID
     */
    private java.lang.String _ID;

    /**
     * Field _name
     */
    private java.lang.String _name;

    /**
     * Field _ref
     */
    private java.lang.Object _ref;

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.generated.package.DESCRIPTION _DESCRIPTION;

    /**
     * Field _FIELDList
     */
    private java.util.Vector _FIELDList;

    /**
     * Field _LINKList
     */
    private java.util.Vector _LINKList;

    /**
     * Field _DATA
     */
    private org.astrogrid.registry.generated.package.DATA _DATA;


      //----------------/
     //- Constructors -/
    //----------------/

    public TABLE() {
        super();
        _FIELDList = new Vector();
        _LINKList = new Vector();
    } //-- org.astrogrid.registry.generated.package.TABLE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFIELD
     * 
     * @param vFIELD
     */
    public void addFIELD(org.astrogrid.registry.generated.package.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.addElement(vFIELD);
    } //-- void addFIELD(org.astrogrid.registry.generated.package.FIELD) 

    /**
     * Method addFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void addFIELD(int index, org.astrogrid.registry.generated.package.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.insertElementAt(vFIELD, index);
    } //-- void addFIELD(int, org.astrogrid.registry.generated.package.FIELD) 

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
     * Method enumerateFIELD
     */
    public java.util.Enumeration enumerateFIELD()
    {
        return _FIELDList.elements();
    } //-- java.util.Enumeration enumerateFIELD() 

    /**
     * Method enumerateLINK
     */
    public java.util.Enumeration enumerateLINK()
    {
        return _LINKList.elements();
    } //-- java.util.Enumeration enumerateLINK() 

    /**
     * Returns the value of field 'DATA'.
     * 
     * @return the value of field 'DATA'.
     */
    public org.astrogrid.registry.generated.package.DATA getDATA()
    {
        return this._DATA;
    } //-- org.astrogrid.registry.generated.package.DATA getDATA() 

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
     * Method getFIELD
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.FIELD getFIELD(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.FIELD) _FIELDList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.FIELD getFIELD(int) 

    /**
     * Method getFIELD
     */
    public org.astrogrid.registry.generated.package.FIELD[] getFIELD()
    {
        int size = _FIELDList.size();
        org.astrogrid.registry.generated.package.FIELD[] mArray = new org.astrogrid.registry.generated.package.FIELD[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.FIELD) _FIELDList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.FIELD[] getFIELD() 

    /**
     * Method getFIELDCount
     */
    public int getFIELDCount()
    {
        return _FIELDList.size();
    } //-- int getFIELDCount() 

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
     * Returns the value of field 'ref'.
     * 
     * @return the value of field 'ref'.
     */
    public java.lang.Object getRef()
    {
        return this._ref;
    } //-- java.lang.Object getRef() 

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
     * Method removeAllFIELD
     */
    public void removeAllFIELD()
    {
        _FIELDList.removeAllElements();
    } //-- void removeAllFIELD() 

    /**
     * Method removeAllLINK
     */
    public void removeAllLINK()
    {
        _LINKList.removeAllElements();
    } //-- void removeAllLINK() 

    /**
     * Method removeFIELD
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.FIELD removeFIELD(int index)
    {
        java.lang.Object obj = _FIELDList.elementAt(index);
        _FIELDList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.FIELD) obj;
    } //-- org.astrogrid.registry.generated.package.FIELD removeFIELD(int) 

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
     * Sets the value of field 'DATA'.
     * 
     * @param DATA the value of field 'DATA'.
     */
    public void setDATA(org.astrogrid.registry.generated.package.DATA DATA)
    {
        this._DATA = DATA;
    } //-- void setDATA(org.astrogrid.registry.generated.package.DATA) 

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
     * Method setFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void setFIELD(int index, org.astrogrid.registry.generated.package.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _FIELDList.setElementAt(vFIELD, index);
    } //-- void setFIELD(int, org.astrogrid.registry.generated.package.FIELD) 

    /**
     * Method setFIELD
     * 
     * @param FIELDArray
     */
    public void setFIELD(org.astrogrid.registry.generated.package.FIELD[] FIELDArray)
    {
        //-- copy array
        _FIELDList.removeAllElements();
        for (int i = 0; i < FIELDArray.length; i++) {
            _FIELDList.addElement(FIELDArray[i]);
        }
    } //-- void setFIELD(org.astrogrid.registry.generated.package.FIELD) 

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
     * Sets the value of field 'ref'.
     * 
     * @param ref the value of field 'ref'.
     */
    public void setRef(java.lang.Object ref)
    {
        this._ref = ref;
    } //-- void setRef(java.lang.Object) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.TABLE) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.TABLE.class, reader);
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
