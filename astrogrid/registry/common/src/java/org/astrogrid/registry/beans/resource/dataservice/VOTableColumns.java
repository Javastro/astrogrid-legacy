/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VOTableColumns.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.IOException;
import java.io.Reader;
import java.io.Serializable;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import org.astrogrid.registry.beans.resource.votable.FIELD;
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
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class VOTableColumns extends org.astrogrid.common.bean.BaseBean 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _FIELDList
     */
    private java.util.ArrayList _FIELDList;


      //----------------/
     //- Constructors -/
    //----------------/

    public VOTableColumns() {
        super();
        _FIELDList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.dataservice.VOTableColumns()


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
     * Method clearFIELD
     */
    public void clearFIELD()
    {
        _FIELDList.clear();
    } //-- void clearFIELD() 

    /**
     * Method enumerateFIELD
     */
    public java.util.Enumeration enumerateFIELD()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_FIELDList.iterator());
    } //-- java.util.Enumeration enumerateFIELD() 

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
     * Method unmarshalVOTableColumns
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.resource.dataservice.VOTableColumns unmarshalVOTableColumns(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.resource.dataservice.VOTableColumns) Unmarshaller.unmarshal(org.astrogrid.registry.beans.resource.dataservice.VOTableColumns.class, reader);
    } //-- org.astrogrid.registry.beans.resource.dataservice.VOTableColumns unmarshalVOTableColumns(java.io.Reader) 

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
