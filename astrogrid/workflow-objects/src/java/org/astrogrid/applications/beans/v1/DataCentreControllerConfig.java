/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCentreControllerConfig.java,v 1.2 2004/03/02 14:09:49 pah Exp $
 */

package org.astrogrid.applications.beans.v1;

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
 * Class DataCentreControllerConfig.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/02 14:09:49 $
 */
public class DataCentreControllerConfig extends org.astrogrid.applications.beans.v1.CommonExecutionConnectorConfigType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _dataCentreList
     */
    private java.util.ArrayList _dataCentreList;


      //----------------/
     //- Constructors -/
    //----------------/

    public DataCentreControllerConfig() {
        super();
        _dataCentreList = new ArrayList();
    } //-- org.astrogrid.applications.beans.v1.DataCentreControllerConfig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDataCentre
     * 
     * @param vDataCentre
     */
    public void addDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataCentreList.add(vDataCentre);
    } //-- void addDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication) 

    /**
     * Method addDataCentre
     * 
     * @param index
     * @param vDataCentre
     */
    public void addDataCentre(int index, org.astrogrid.applications.beans.v1.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataCentreList.add(index, vDataCentre);
    } //-- void addDataCentre(int, org.astrogrid.applications.beans.v1.DataCentreApplication) 

    /**
     * Method clearDataCentre
     */
    public void clearDataCentre()
    {
        _dataCentreList.clear();
    } //-- void clearDataCentre() 

    /**
     * Method enumerateDataCentre
     */
    public java.util.Enumeration enumerateDataCentre()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_dataCentreList.iterator());
    } //-- java.util.Enumeration enumerateDataCentre() 

    /**
     * Method getDataCentre
     * 
     * @param index
     */
    public org.astrogrid.applications.beans.v1.DataCentreApplication getDataCentre(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataCentreList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.applications.beans.v1.DataCentreApplication) _dataCentreList.get(index);
    } //-- org.astrogrid.applications.beans.v1.DataCentreApplication getDataCentre(int) 

    /**
     * Method getDataCentre
     */
    public org.astrogrid.applications.beans.v1.DataCentreApplication[] getDataCentre()
    {
        int size = _dataCentreList.size();
        org.astrogrid.applications.beans.v1.DataCentreApplication[] mArray = new org.astrogrid.applications.beans.v1.DataCentreApplication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.applications.beans.v1.DataCentreApplication) _dataCentreList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.applications.beans.v1.DataCentreApplication[] getDataCentre() 

    /**
     * Method getDataCentreCount
     */
    public int getDataCentreCount()
    {
        return _dataCentreList.size();
    } //-- int getDataCentreCount() 

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
     * Method removeDataCentre
     * 
     * @param vDataCentre
     */
    public boolean removeDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication vDataCentre)
    {
        boolean removed = _dataCentreList.remove(vDataCentre);
        return removed;
    } //-- boolean removeDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication) 

    /**
     * Method setDataCentre
     * 
     * @param index
     * @param vDataCentre
     */
    public void setDataCentre(int index, org.astrogrid.applications.beans.v1.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataCentreList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dataCentreList.set(index, vDataCentre);
    } //-- void setDataCentre(int, org.astrogrid.applications.beans.v1.DataCentreApplication) 

    /**
     * Method setDataCentre
     * 
     * @param dataCentreArray
     */
    public void setDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication[] dataCentreArray)
    {
        //-- copy array
        _dataCentreList.clear();
        for (int i = 0; i < dataCentreArray.length; i++) {
            _dataCentreList.add(dataCentreArray[i]);
        }
    } //-- void setDataCentre(org.astrogrid.applications.beans.v1.DataCentreApplication) 

    /**
     * Method unmarshalDataCentreControllerConfig
     * 
     * @param reader
     */
    public static org.astrogrid.applications.beans.v1.DataCentreControllerConfig unmarshalDataCentreControllerConfig(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.applications.beans.v1.DataCentreControllerConfig) Unmarshaller.unmarshal(org.astrogrid.applications.beans.v1.DataCentreControllerConfig.class, reader);
    } //-- org.astrogrid.applications.beans.v1.DataCentreControllerConfig unmarshalDataCentreControllerConfig(java.io.Reader) 

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
