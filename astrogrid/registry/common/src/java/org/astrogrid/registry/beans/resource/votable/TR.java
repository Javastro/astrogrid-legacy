/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TR.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
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
 * Class TR.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class TR extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _TDList
     */
    private java.util.ArrayList _TDList;


      //----------------/
     //- Constructors -/
    //----------------/

    public TR() {
        super();
        _TDList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.TR()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addTD
     * 
     * @param vTD
     */
    public void addTD(org.astrogrid.registry.beans.resource.votable.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        _TDList.add(vTD);
    } //-- void addTD(org.astrogrid.registry.beans.resource.votable.TD) 

    /**
     * Method addTD
     * 
     * @param index
     * @param vTD
     */
    public void addTD(int index, org.astrogrid.registry.beans.resource.votable.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        _TDList.add(index, vTD);
    } //-- void addTD(int, org.astrogrid.registry.beans.resource.votable.TD) 

    /**
     * Method clearTD
     */
    public void clearTD()
    {
        _TDList.clear();
    } //-- void clearTD() 

    /**
     * Method enumerateTD
     */
    public java.util.Enumeration enumerateTD()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_TDList.iterator());
    } //-- java.util.Enumeration enumerateTD() 

    /**
     * Method getTD
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.TD getTD(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.TD) _TDList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.TD getTD(int) 

    /**
     * Method getTD
     */
    public org.astrogrid.registry.beans.resource.votable.TD[] getTD()
    {
        int size = _TDList.size();
        org.astrogrid.registry.beans.resource.votable.TD[] mArray = new org.astrogrid.registry.beans.resource.votable.TD[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.TD) _TDList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.TD[] getTD() 

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
     * Method removeTD
     * 
     * @param vTD
     */
    public boolean removeTD(org.astrogrid.registry.beans.resource.votable.TD vTD)
    {
        boolean removed = _TDList.remove(vTD);
        return removed;
    } //-- boolean removeTD(org.astrogrid.registry.beans.resource.votable.TD) 

    /**
     * Method setTD
     * 
     * @param index
     * @param vTD
     */
    public void setTD(int index, org.astrogrid.registry.beans.resource.votable.TD vTD)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _TDList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _TDList.set(index, vTD);
    } //-- void setTD(int, org.astrogrid.registry.beans.resource.votable.TD) 

    /**
     * Method setTD
     * 
     * @param TDArray
     */
    public void setTD(org.astrogrid.registry.beans.resource.votable.TD[] TDArray)
    {
        //-- copy array
        _TDList.clear();
        for (int i = 0; i < TDArray.length; i++) {
            _TDList.add(TDArray[i]);
        }
    } //-- void setTD(org.astrogrid.registry.beans.resource.votable.TD) 

    /**
     * Method unmarshalTR
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.votable.TR unmarshalTR(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.votable.TR) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.votable.TR.class, reader);
    } //-- org.astrogrid.registry.beans.resource.votable.TR unmarshalTR(java.io.Reader) 

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
