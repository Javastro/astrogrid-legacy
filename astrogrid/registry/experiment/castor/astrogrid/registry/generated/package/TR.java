/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TR.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class TR.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class TR implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _TDList
     */
    private java.util.Vector _TDList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TR() {
        super();
        _TDList = new Vector();
    } //-- org.astrogrid.registry.generated.package.TR()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTD
     * 
     * @param vTD
     */
    public void addTD(org.astrogrid.registry.generated.package.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        _TDList.addElement(vTD);
    } //-- void addTD(org.astrogrid.registry.generated.package.TD) 

    /**
     * Method addTD
     * 
     * @param index
     * @param vTD
     */
    public void addTD(int index, org.astrogrid.registry.generated.package.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        _TDList.insertElementAt(vTD, index);
    } //-- void addTD(int, org.astrogrid.registry.generated.package.TD) 

    /**
     * Method enumerateTD
     */
    public java.util.Enumeration enumerateTD()
    {
        return _TDList.elements();
    } //-- java.util.Enumeration enumerateTD() 

    /**
     * Method getTD
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TD getTD(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.TD) _TDList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.TD getTD(int) 

    /**
     * Method getTD
     */
    public org.astrogrid.registry.generated.package.TD[] getTD()
    {
        int size = _TDList.size();
        org.astrogrid.registry.generated.package.TD[] mArray = new org.astrogrid.registry.generated.package.TD[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.TD) _TDList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.TD[] getTD() 

    /**
     * Method getTDCount
     */
    public int getTDCount()
    {
        return _TDList.size();
    } //-- int getTDCount() 

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
     * Method removeAllTD
     */
    public void removeAllTD()
    {
        _TDList.removeAllElements();
    } //-- void removeAllTD() 

    /**
     * Method removeTD
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TD removeTD(int index)
    {
        java.lang.Object obj = _TDList.elementAt(index);
        _TDList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.TD) obj;
    } //-- org.astrogrid.registry.generated.package.TD removeTD(int) 

    /**
     * Method setTD
     * 
     * @param index
     * @param vTD
     */
    public void setTD(int index, org.astrogrid.registry.generated.package.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TDList.setElementAt(vTD, index);
    } //-- void setTD(int, org.astrogrid.registry.generated.package.TD) 

    /**
     * Method setTD
     * 
     * @param TDArray
     */
    public void setTD(org.astrogrid.registry.generated.package.TD[] TDArray)
    {
        //-- copy array
        _TDList.removeAllElements();
        for (int i = 0; i < TDArray.length; i++) {
            _TDList.addElement(TDArray[i]);
        }
    } //-- void setTD(org.astrogrid.registry.generated.package.TD) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.TR) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.TR.class, reader);
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
