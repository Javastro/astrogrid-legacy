/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: TABLEDATA.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class TABLEDATA.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class TABLEDATA extends org.astrogrid.registry.generated.package.TABLEFORMATType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _TRList
     */
    private java.util.Vector _TRList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TABLEDATA() {
        super();
        _TRList = new Vector();
    } //-- org.astrogrid.registry.generated.package.TABLEDATA()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTR
     * 
     * @param vTR
     */
    public void addTR(org.astrogrid.registry.generated.package.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        _TRList.addElement(vTR);
    } //-- void addTR(org.astrogrid.registry.generated.package.TR) 

    /**
     * Method addTR
     * 
     * @param index
     * @param vTR
     */
    public void addTR(int index, org.astrogrid.registry.generated.package.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        _TRList.insertElementAt(vTR, index);
    } //-- void addTR(int, org.astrogrid.registry.generated.package.TR) 

    /**
     * Method enumerateTR
     */
    public java.util.Enumeration enumerateTR()
    {
        return _TRList.elements();
    } //-- java.util.Enumeration enumerateTR() 

    /**
     * Method getTR
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TR getTR(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TRList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.TR) _TRList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.TR getTR(int) 

    /**
     * Method getTR
     */
    public org.astrogrid.registry.generated.package.TR[] getTR()
    {
        int size = _TRList.size();
        org.astrogrid.registry.generated.package.TR[] mArray = new org.astrogrid.registry.generated.package.TR[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.TR) _TRList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.TR[] getTR() 

    /**
     * Method getTRCount
     */
    public int getTRCount()
    {
        return _TRList.size();
    } //-- int getTRCount() 

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
     * Method removeAllTR
     */
    public void removeAllTR()
    {
        _TRList.removeAllElements();
    } //-- void removeAllTR() 

    /**
     * Method removeTR
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.TR removeTR(int index)
    {
        java.lang.Object obj = _TRList.elementAt(index);
        _TRList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.TR) obj;
    } //-- org.astrogrid.registry.generated.package.TR removeTR(int) 

    /**
     * Method setTR
     * 
     * @param index
     * @param vTR
     */
    public void setTR(int index, org.astrogrid.registry.generated.package.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TRList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TRList.setElementAt(vTR, index);
    } //-- void setTR(int, org.astrogrid.registry.generated.package.TR) 

    /**
     * Method setTR
     * 
     * @param TRArray
     */
    public void setTR(org.astrogrid.registry.generated.package.TR[] TRArray)
    {
        //-- copy array
        _TRList.removeAllElements();
        for (int i = 0; i < TRArray.length; i++) {
            _TRList.addElement(TRArray[i]);
        }
    } //-- void setTR(org.astrogrid.registry.generated.package.TR) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.TABLEDATA) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.TABLEDATA.class, reader);
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
