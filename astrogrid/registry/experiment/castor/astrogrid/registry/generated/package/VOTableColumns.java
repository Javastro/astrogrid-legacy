/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: VOTableColumns.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * The columns returned in a VOTable.
 *  
 *  This element is recommended for use within extensions of
 *  the Capability element.
 *  
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class VOTableColumns implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _FIELDList
     */
    private java.util.Vector _FIELDList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOTableColumns() {
        super();
        _FIELDList = new Vector();
    } //-- org.astrogrid.registry.generated.package.VOTableColumns()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFIELD
     * 
     * @param vFIELD
     */
    public void addFIELD(FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.addElement(vFIELD);
    } //-- void addFIELD(FIELD) 

    /**
     * Method addFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void addFIELD(int index, FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        _FIELDList.insertElementAt(vFIELD, index);
    } //-- void addFIELD(int, FIELD) 

    /**
     * Method enumerateFIELD
     */
    public java.util.Enumeration enumerateFIELD()
    {
        return _FIELDList.elements();
    } //-- java.util.Enumeration enumerateFIELD() 

    /**
     * Method getFIELD
     * 
     * @param index
     */
    public FIELD getFIELD(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (FIELD) _FIELDList.elementAt(index);
    } //-- FIELD getFIELD(int) 

    /**
     * Method getFIELD
     */
    public FIELD[] getFIELD()
    {
        int size = _FIELDList.size();
        FIELD[] mArray = new FIELD[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (FIELD) _FIELDList.elementAt(index);
        }
        return mArray;
    } //-- FIELD[] getFIELD() 

    /**
     * Method getFIELDCount
     */
    public int getFIELDCount()
    {
        return _FIELDList.size();
    } //-- int getFIELDCount() 

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
     * Method removeFIELD
     * 
     * @param index
     */
    public FIELD removeFIELD(int index)
    {
        java.lang.Object obj = _FIELDList.elementAt(index);
        _FIELDList.removeElementAt(index);
        return (FIELD) obj;
    } //-- FIELD removeFIELD(int) 

    /**
     * Method setFIELD
     * 
     * @param index
     * @param vFIELD
     */
    public void setFIELD(int index, FIELD vFIELD)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _FIELDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _FIELDList.setElementAt(vFIELD, index);
    } //-- void setFIELD(int, FIELD) 

    /**
     * Method setFIELD
     * 
     * @param FIELDArray
     */
    public void setFIELD(FIELD[] FIELDArray)
    {
        //-- copy array
        _FIELDList.removeAllElements();
        for (int i = 0; i < FIELDArray.length; i++) {
            _FIELDList.addElement(FIELDArray[i]);
        }
    } //-- void setFIELD(FIELD) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.VOTableColumns) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.VOTableColumns.class, reader);
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
