/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: DEFINITIONS.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
 * Class DEFINITIONS.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class DEFINITIONS implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _items
     */
    private java.util.Vector _items;


      //----------------/
     //- Constructors -/
    //----------------/

    public DEFINITIONS() {
        super();
        _items = new Vector();
    } //-- org.astrogrid.registry.generated.package.DEFINITIONS()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDEFINITIONSItem
     * 
     * @param vDEFINITIONSItem
     */
    public void addDEFINITIONSItem(org.astrogrid.registry.generated.package.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.addElement(vDEFINITIONSItem);
    } //-- void addDEFINITIONSItem(org.astrogrid.registry.generated.package.DEFINITIONSItem) 

    /**
     * Method addDEFINITIONSItem
     * 
     * @param index
     * @param vDEFINITIONSItem
     */
    public void addDEFINITIONSItem(int index, org.astrogrid.registry.generated.package.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        _items.insertElementAt(vDEFINITIONSItem, index);
    } //-- void addDEFINITIONSItem(int, org.astrogrid.registry.generated.package.DEFINITIONSItem) 

    /**
     * Method enumerateDEFINITIONSItem
     */
    public java.util.Enumeration enumerateDEFINITIONSItem()
    {
        return _items.elements();
    } //-- java.util.Enumeration enumerateDEFINITIONSItem() 

    /**
     * Method getDEFINITIONSItem
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.DEFINITIONSItem getDEFINITIONSItem(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.DEFINITIONSItem) _items.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.DEFINITIONSItem getDEFINITIONSItem(int) 

    /**
     * Method getDEFINITIONSItem
     */
    public org.astrogrid.registry.generated.package.DEFINITIONSItem[] getDEFINITIONSItem()
    {
        int size = _items.size();
        org.astrogrid.registry.generated.package.DEFINITIONSItem[] mArray = new org.astrogrid.registry.generated.package.DEFINITIONSItem[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.DEFINITIONSItem) _items.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.DEFINITIONSItem[] getDEFINITIONSItem() 

    /**
     * Method getDEFINITIONSItemCount
     */
    public int getDEFINITIONSItemCount()
    {
        return _items.size();
    } //-- int getDEFINITIONSItemCount() 

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
     * Method removeAllDEFINITIONSItem
     */
    public void removeAllDEFINITIONSItem()
    {
        _items.removeAllElements();
    } //-- void removeAllDEFINITIONSItem() 

    /**
     * Method removeDEFINITIONSItem
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.DEFINITIONSItem removeDEFINITIONSItem(int index)
    {
        java.lang.Object obj = _items.elementAt(index);
        _items.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.DEFINITIONSItem) obj;
    } //-- org.astrogrid.registry.generated.package.DEFINITIONSItem removeDEFINITIONSItem(int) 

    /**
     * Method setDEFINITIONSItem
     * 
     * @param index
     * @param vDEFINITIONSItem
     */
    public void setDEFINITIONSItem(int index, org.astrogrid.registry.generated.package.DEFINITIONSItem vDEFINITIONSItem)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _items.size())) {
            throw new IndexOutOfBoundsException();
        }
        _items.setElementAt(vDEFINITIONSItem, index);
    } //-- void setDEFINITIONSItem(int, org.astrogrid.registry.generated.package.DEFINITIONSItem) 

    /**
     * Method setDEFINITIONSItem
     * 
     * @param DEFINITIONSItemArray
     */
    public void setDEFINITIONSItem(org.astrogrid.registry.generated.package.DEFINITIONSItem[] DEFINITIONSItemArray)
    {
        //-- copy array
        _items.removeAllElements();
        for (int i = 0; i < DEFINITIONSItemArray.length; i++) {
            _items.addElement(DEFINITIONSItemArray[i]);
        }
    } //-- void setDEFINITIONSItem(org.astrogrid.registry.generated.package.DEFINITIONSItem) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.DEFINITIONS) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.DEFINITIONS.class, reader);
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
