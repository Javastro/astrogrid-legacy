/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: OrganisationType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class OrganisationType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class OrganisationType extends ResourceType 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * the observatory or facility used to collect the data 
     *  contained or managed by this resource. 
     *  
     */
    private java.util.Vector _facilityList;

    /**
     * the Instrument used to collect the data contain or 
     *  managed by a resource. 
     *  
     */
    private java.util.Vector _instrumentList;


      //----------------/
     //- Constructors -/
    //----------------/

    public OrganisationType() {
        super();
        _facilityList = new Vector();
        _instrumentList = new Vector();
    } //-- org.astrogrid.registry.generated.package.OrganisationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addFacility
     * 
     * @param vFacility
     */
    public void addFacility(Facility vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.addElement(vFacility);
    } //-- void addFacility(Facility) 

    /**
     * Method addFacility
     * 
     * @param index
     * @param vFacility
     */
    public void addFacility(int index, Facility vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        _facilityList.insertElementAt(vFacility, index);
    } //-- void addFacility(int, Facility) 

    /**
     * Method addInstrument
     * 
     * @param vInstrument
     */
    public void addInstrument(Instrument vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.addElement(vInstrument);
    } //-- void addInstrument(Instrument) 

    /**
     * Method addInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void addInstrument(int index, Instrument vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        _instrumentList.insertElementAt(vInstrument, index);
    } //-- void addInstrument(int, Instrument) 

    /**
     * Method enumerateFacility
     */
    public java.util.Enumeration enumerateFacility()
    {
        return _facilityList.elements();
    } //-- java.util.Enumeration enumerateFacility() 

    /**
     * Method enumerateInstrument
     */
    public java.util.Enumeration enumerateInstrument()
    {
        return _instrumentList.elements();
    } //-- java.util.Enumeration enumerateInstrument() 

    /**
     * Method getFacility
     * 
     * @param index
     */
    public Facility getFacility(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Facility) _facilityList.elementAt(index);
    } //-- Facility getFacility(int) 

    /**
     * Method getFacility
     */
    public Facility[] getFacility()
    {
        int size = _facilityList.size();
        Facility[] mArray = new Facility[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Facility) _facilityList.elementAt(index);
        }
        return mArray;
    } //-- Facility[] getFacility() 

    /**
     * Method getFacilityCount
     */
    public int getFacilityCount()
    {
        return _facilityList.size();
    } //-- int getFacilityCount() 

    /**
     * Method getInstrument
     * 
     * @param index
     */
    public Instrument getInstrument(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (Instrument) _instrumentList.elementAt(index);
    } //-- Instrument getInstrument(int) 

    /**
     * Method getInstrument
     */
    public Instrument[] getInstrument()
    {
        int size = _instrumentList.size();
        Instrument[] mArray = new Instrument[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (Instrument) _instrumentList.elementAt(index);
        }
        return mArray;
    } //-- Instrument[] getInstrument() 

    /**
     * Method getInstrumentCount
     */
    public int getInstrumentCount()
    {
        return _instrumentList.size();
    } //-- int getInstrumentCount() 

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
     * Method removeAllFacility
     */
    public void removeAllFacility()
    {
        _facilityList.removeAllElements();
    } //-- void removeAllFacility() 

    /**
     * Method removeAllInstrument
     */
    public void removeAllInstrument()
    {
        _instrumentList.removeAllElements();
    } //-- void removeAllInstrument() 

    /**
     * Method removeFacility
     * 
     * @param index
     */
    public Facility removeFacility(int index)
    {
        java.lang.Object obj = _facilityList.elementAt(index);
        _facilityList.removeElementAt(index);
        return (Facility) obj;
    } //-- Facility removeFacility(int) 

    /**
     * Method removeInstrument
     * 
     * @param index
     */
    public Instrument removeInstrument(int index)
    {
        java.lang.Object obj = _instrumentList.elementAt(index);
        _instrumentList.removeElementAt(index);
        return (Instrument) obj;
    } //-- Instrument removeInstrument(int) 

    /**
     * Method setFacility
     * 
     * @param index
     * @param vFacility
     */
    public void setFacility(int index, Facility vFacility)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _facilityList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _facilityList.setElementAt(vFacility, index);
    } //-- void setFacility(int, Facility) 

    /**
     * Method setFacility
     * 
     * @param facilityArray
     */
    public void setFacility(Facility[] facilityArray)
    {
        //-- copy array
        _facilityList.removeAllElements();
        for (int i = 0; i < facilityArray.length; i++) {
            _facilityList.addElement(facilityArray[i]);
        }
    } //-- void setFacility(Facility) 

    /**
     * Method setInstrument
     * 
     * @param index
     * @param vInstrument
     */
    public void setInstrument(int index, Instrument vInstrument)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _instrumentList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _instrumentList.setElementAt(vInstrument, index);
    } //-- void setInstrument(int, Instrument) 

    /**
     * Method setInstrument
     * 
     * @param instrumentArray
     */
    public void setInstrument(Instrument[] instrumentArray)
    {
        //-- copy array
        _instrumentList.removeAllElements();
        for (int i = 0; i < instrumentArray.length; i++) {
            _instrumentList.addElement(instrumentArray[i]);
        }
    } //-- void setInstrument(Instrument) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.OrganisationType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.OrganisationType.class, reader);
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
