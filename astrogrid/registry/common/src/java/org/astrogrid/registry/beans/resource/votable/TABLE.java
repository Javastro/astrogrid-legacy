/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TABLE.java,v 1.6 2004/03/19 08:16:47 KevinBenson Exp $
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
import org.exolab.castor.xml.MarshalException;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;
import org.exolab.castor.xml.ValidationException;
import org.xml.sax.ContentHandler;

/**
 * Class TABLE.
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/19 08:16:47 $
 */
public class TABLE extends org.astrogrid.common.bean.BaseBean 
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
    private org.astrogrid.registry.beans.resource.votable.AnyTEXT _DESCRIPTION;

    /**
     * Field _FIELDList
     */
    private java.util.ArrayList _FIELDList;

    /**
     * Field _LINKList
     */
    private java.util.ArrayList _LINKList;

    /**
     * Field _DATA
     */
    private org.astrogrid.registry.beans.resource.votable.DATA _DATA;


      //----------------/
     //- Constructors -/
    //----------------/

    public TABLE() {
        super();
        _FIELDList = new ArrayList();
        _LINKList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.TABLE()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFIELD
     * 
     * @param vFIELD
     */
    public void addFIELD(org.astrogrid.registry.beans.resource.votable.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.add(vFIELD);
    } //-- void addFIELD(org.astrogrid.registry.beans.resource.votable.FIELD) 

    /**
     * Method addFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void addFIELD(int index, org.astrogrid.registry.beans.resource.votable.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.add(index, vFIELD);
    } //-- void addFIELD(int, org.astrogrid.registry.beans.resource.votable.FIELD) 

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
     * Method clearFIELD
     */
    public void clearFIELD()
    {
        _FIELDList.clear();
    } //-- void clearFIELD() 

    /**
     * Method clearLINK
     */
    public void clearLINK()
    {
        _LINKList.clear();
    } //-- void clearLINK() 

    /**
     * Method enumerateFIELD
     */
    public java.util.Enumeration enumerateFIELD()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_FIELDList.iterator());
    } //-- java.util.Enumeration enumerateFIELD() 

    /**
     * Method enumerateLINK
     */
    public java.util.Enumeration enumerateLINK()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_LINKList.iterator());
    } //-- java.util.Enumeration enumerateLINK() 

    /**
     * Returns the value of field 'DATA'.
     * 
     * @return the value of field 'DATA'.
     */
    public org.astrogrid.registry.beans.resource.votable.DATA getDATA()
    {
        return this._DATA;
    } //-- org.astrogrid.registry.beans.resource.votable.DATA getDATA() 

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
     * Method getFIELD
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.FIELD getFIELD(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.FIELD) _FIELDList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.FIELD getFIELD(int) 

    /**
     * Method getFIELD
     */
    public org.astrogrid.registry.beans.resource.votable.FIELD[] getFIELD()
    {
        int size = _FIELDList.size();
        org.astrogrid.registry.beans.resource.votable.FIELD[] mArray = new org.astrogrid.registry.beans.resource.votable.FIELD[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.FIELD) _FIELDList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.FIELD[] getFIELD() 

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
     * Method removeFIELD
     * 
     * @param vFIELD
     */
    public boolean removeFIELD(org.astrogrid.registry.beans.resource.votable.FIELD vFIELD)
    {
        boolean removed = _FIELDList.remove(vFIELD);
        return removed;
    } //-- boolean removeFIELD(org.astrogrid.registry.beans.resource.votable.FIELD) 

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
     * Sets the value of field 'DATA'.
     * 
     * @param DATA the value of field 'DATA'.
     */
    public void setDATA(org.astrogrid.registry.beans.resource.votable.DATA DATA)
    {
        this._DATA = DATA;
    } //-- void setDATA(org.astrogrid.registry.beans.resource.votable.DATA) 

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
     * Method setFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void setFIELD(int index, org.astrogrid.registry.beans.resource.votable.FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _FIELDList.set(index, vFIELD);
    } //-- void setFIELD(int, org.astrogrid.registry.beans.resource.votable.FIELD) 

    /**
     * Method setFIELD
     * 
     * @param FIELDArray
     */
    public void setFIELD(org.astrogrid.registry.beans.resource.votable.FIELD[] FIELDArray)
    {
        //-- copy array
        _FIELDList.clear();
        for (int i = 0; i < FIELDArray.length; i++) {
            _FIELDList.add(FIELDArray[i]);
        }
    } //-- void setFIELD(org.astrogrid.registry.beans.resource.votable.FIELD) 

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
     * Sets the value of field 'ref'.
     * 
     * @param ref the value of field 'ref'.
     */
    public void setRef(java.lang.Object ref)
    {
        this._ref = ref;
    } //-- void setRef(java.lang.Object) 

    /**
     * Method unmarshalTABLE
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.TABLE unmarshalTABLE(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.TABLE) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.TABLE.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.TABLE unmarshalTABLE(java.io.Reader) 

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
