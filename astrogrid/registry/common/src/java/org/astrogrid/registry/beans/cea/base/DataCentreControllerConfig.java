/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataCentreControllerConfig.java,v 1.4 2004/04/05 14:36:12 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.cea.base;

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
 * Configuration for a set of datacentre descriptions
 * 
 * @version $Revision: 1.4 $ $Date: 2004/04/05 14:36:12 $
 */
public class DataCentreControllerConfig extends org.astrogrid.registry.beans.cea.base.CommonExecutionConnectorConfigType 
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
    } //-- org.astrogrid.registry.beans.cea.base.DataCentreControllerConfig()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addDataCentre
     * 
     * @param vDataCentre
     */
    public void addDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataCentreList.add(vDataCentre);
    } //-- void addDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication) 

    /**
     * Method addDataCentre
     * 
     * @param index
     * @param vDataCentre
     */
    public void addDataCentre(int index, org.astrogrid.registry.beans.cea.base.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        _dataCentreList.add(index, vDataCentre);
    } //-- void addDataCentre(int, org.astrogrid.registry.beans.cea.base.DataCentreApplication) 

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
    public org.astrogrid.registry.beans.cea.base.DataCentreApplication getDataCentre(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataCentreList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.cea.base.DataCentreApplication) _dataCentreList.get(index);
    } //-- org.astrogrid.registry.beans.cea.base.DataCentreApplication getDataCentre(int) 

    /**
     * Method getDataCentre
     */
    public org.astrogrid.registry.beans.cea.base.DataCentreApplication[] getDataCentre()
    {
        int size = _dataCentreList.size();
        org.astrogrid.registry.beans.cea.base.DataCentreApplication[] mArray = new org.astrogrid.registry.beans.cea.base.DataCentreApplication[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.cea.base.DataCentreApplication) _dataCentreList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.cea.base.DataCentreApplication[] getDataCentre() 

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
    public boolean removeDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication vDataCentre)
    {
        boolean removed = _dataCentreList.remove(vDataCentre);
        return removed;
    } //-- boolean removeDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication) 

    /**
     * Method setDataCentre
     * 
     * @param index
     * @param vDataCentre
     */
    public void setDataCentre(int index, org.astrogrid.registry.beans.cea.base.DataCentreApplication vDataCentre)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dataCentreList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dataCentreList.set(index, vDataCentre);
    } //-- void setDataCentre(int, org.astrogrid.registry.beans.cea.base.DataCentreApplication) 

    /**
     * Method setDataCentre
     * 
     * @param dataCentreArray
     */
    public void setDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication[] dataCentreArray)
    {
        //-- copy array
        _dataCentreList.clear();
        for (int i = 0; i < dataCentreArray.length; i++) {
            _dataCentreList.add(dataCentreArray[i]);
        }
    } //-- void setDataCentre(org.astrogrid.registry.beans.cea.base.DataCentreApplication) 

    /**
     * Method unmarshalDataCentreControllerConfig
     * 
     * @param reader
     */
    public static org.astrogrid.registry.beans.cea.base.DataCentreControllerConfig unmarshalDataCentreControllerConfig(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.beans.cea.base.DataCentreControllerConfig) Unmarshaller.unmarshal(org.astrogrid.registry.beans.cea.base.DataCentreControllerConfig.class, reader);
    } //-- org.astrogrid.registry.beans.cea.base.DataCentreControllerConfig unmarshalDataCentreControllerConfig(java.io.Reader) 

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
