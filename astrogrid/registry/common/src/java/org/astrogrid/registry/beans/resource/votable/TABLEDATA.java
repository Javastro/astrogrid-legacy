/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TABLEDATA.java,v 1.6 2004/03/19 08:16:47 KevinBenson Exp $
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
 * Class TABLEDATA.
 * 
 * @version $Revision: 1.6 $ $Date: 2004/03/19 08:16:47 $
 */
public class TABLEDATA extends org.astrogrid.registry.beans.resource.votable.TABLEFORMATType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _TRList
     */
    private java.util.ArrayList _TRList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TABLEDATA() {
        super();
        _TRList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.TABLEDATA()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTR
     * 
     * @param vTR
     */
    public void addTR(org.astrogrid.registry.beans.resource.votable.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        _TRList.add(vTR);
    } //-- void addTR(org.astrogrid.registry.beans.resource.votable.TR) 

    /**
     * Method addTR
     * 
     * @param index
     * @param vTR
     */
    public void addTR(int index, org.astrogrid.registry.beans.resource.votable.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        _TRList.add(index, vTR);
    } //-- void addTR(int, org.astrogrid.registry.beans.resource.votable.TR) 

    /**
     * Method clearTR
     */
    public void clearTR()
    {
        _TRList.clear();
    } //-- void clearTR() 

    /**
     * Method enumerateTR
     */
    public java.util.Enumeration enumerateTR()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_TRList.iterator());
    } //-- java.util.Enumeration enumerateTR() 

    /**
     * Method getTR
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.TR getTR(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TRList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.TR) _TRList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.TR getTR(int) 

    /**
     * Method getTR
     */
    public org.astrogrid.registry.beans.resource.votable.TR[] getTR()
    {
        int size = _TRList.size();
        org.astrogrid.registry.beans.resource.votable.TR[] mArray = new org.astrogrid.registry.beans.resource.votable.TR[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.TR) _TRList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.TR[] getTR() 

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
     * Method removeTR
     * 
     * @param vTR
     */
    public boolean removeTR(org.astrogrid.registry.beans.resource.votable.TR vTR)
    {
        boolean removed = _TRList.remove(vTR);
        return removed;
    } //-- boolean removeTR(org.astrogrid.registry.beans.resource.votable.TR) 

    /**
     * Method setTR
     * 
     * @param index
     * @param vTR
     */
    public void setTR(int index, org.astrogrid.registry.beans.resource.votable.TR vTR)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TRList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TRList.set(index, vTR);
    } //-- void setTR(int, org.astrogrid.registry.beans.resource.votable.TR) 

    /**
     * Method setTR
     * 
     * @param TRArray
     */
    public void setTR(org.astrogrid.registry.beans.resource.votable.TR[] TRArray)
    {
        //-- copy array
        _TRList.clear();
        for (int i = 0; i < TRArray.length; i++) {
            _TRList.add(TRArray[i]);
        }
    } //-- void setTR(org.astrogrid.registry.beans.resource.votable.TR) 

    /**
     * Method unmarshalTABLEDATA
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.TABLEDATA unmarshalTABLEDATA(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.TABLEDATA) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.TABLEDATA.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.TABLEDATA unmarshalTABLEDATA(java.io.Reader) 

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
