/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CurationType.java,v 1.1 2004/03/03 03:40:59 pcontrer Exp $
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
 * Class CurationType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:59 $
 */
public class CurationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Entity (e.g. person or organisation) responsible for making
     * the 
     *  resource available
     *  
     */
    private org.astrogrid.registry.generated.package.Publisher _publisher;

    /**
     * Information that can be used for contacting someone with
     *  regard to this resource.
     *  
     */
    private org.astrogrid.registry.generated.package.Contact _contact;

    /**
     * Date associated with an event in the life cycle of the
     *  resource. 
     *  
     */
    private java.util.Vector _dateList;

    /**
     * The entity (e.g. person or organisation) primarily
     * responsible for
     *  creating the content or constitution of the resource
     *  
     */
    private org.astrogrid.registry.generated.package.Creator _creator;

    /**
     * Entity responsible for contributions to the content of the
     * resource
     *  
     */
    private java.util.Vector _contributorList;

    /**
     * Label associated with creation or availablilty of a version
     * of 
     *  a resource.
     *  
     */
    private java.lang.String _version;


      //----------------/
     //- Constructors -/
    //----------------/

    public CurationType() {
        super();
        _dateList = new Vector();
        _contributorList = new Vector();
    } //-- org.astrogrid.registry.generated.package.CurationType()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addContributor
     * 
     * @param vContributor
     */
    public void addContributor(org.astrogrid.registry.generated.package.Contributor vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        _contributorList.addElement(vContributor);
    } //-- void addContributor(org.astrogrid.registry.generated.package.Contributor) 

    /**
     * Method addContributor
     * 
     * @param index
     * @param vContributor
     */
    public void addContributor(int index, org.astrogrid.registry.generated.package.Contributor vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        _contributorList.insertElementAt(vContributor, index);
    } //-- void addContributor(int, org.astrogrid.registry.generated.package.Contributor) 

    /**
     * Method addDate
     * 
     * @param vDate
     */
    public void addDate(org.astrogrid.registry.generated.package.Date vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        _dateList.addElement(vDate);
    } //-- void addDate(org.astrogrid.registry.generated.package.Date) 

    /**
     * Method addDate
     * 
     * @param index
     * @param vDate
     */
    public void addDate(int index, org.astrogrid.registry.generated.package.Date vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        _dateList.insertElementAt(vDate, index);
    } //-- void addDate(int, org.astrogrid.registry.generated.package.Date) 

    /**
     * Method enumerateContributor
     */
    public java.util.Enumeration enumerateContributor()
    {
        return _contributorList.elements();
    } //-- java.util.Enumeration enumerateContributor() 

    /**
     * Method enumerateDate
     */
    public java.util.Enumeration enumerateDate()
    {
        return _dateList.elements();
    } //-- java.util.Enumeration enumerateDate() 

    /**
     * Returns the value of field 'contact'. The field 'contact'
     * has the following description: Information that can be used
     * for contacting someone with
     *  regard to this resource.
     *  
     * 
     * @return the value of field 'contact'.
     */
    public org.astrogrid.registry.generated.package.Contact getContact()
    {
        return this._contact;
    } //-- org.astrogrid.registry.generated.package.Contact getContact() 

    /**
     * Method getContributor
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Contributor getContributor(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contributorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Contributor) _contributorList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Contributor getContributor(int) 

    /**
     * Method getContributor
     */
    public org.astrogrid.registry.generated.package.Contributor[] getContributor()
    {
        int size = _contributorList.size();
        org.astrogrid.registry.generated.package.Contributor[] mArray = new org.astrogrid.registry.generated.package.Contributor[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Contributor) _contributorList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Contributor[] getContributor() 

    /**
     * Method getContributorCount
     */
    public int getContributorCount()
    {
        return _contributorList.size();
    } //-- int getContributorCount() 

    /**
     * Returns the value of field 'creator'. The field 'creator'
     * has the following description: The entity (e.g. person or
     * organisation) primarily responsible for
     *  creating the content or constitution of the resource
     *  
     * 
     * @return the value of field 'creator'.
     */
    public org.astrogrid.registry.generated.package.Creator getCreator()
    {
        return this._creator;
    } //-- org.astrogrid.registry.generated.package.Creator getCreator() 

    /**
     * Method getDate
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Date getDate(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.Date) _dateList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.Date getDate(int) 

    /**
     * Method getDate
     */
    public org.astrogrid.registry.generated.package.Date[] getDate()
    {
        int size = _dateList.size();
        org.astrogrid.registry.generated.package.Date[] mArray = new org.astrogrid.registry.generated.package.Date[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.Date) _dateList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.Date[] getDate() 

    /**
     * Method getDateCount
     */
    public int getDateCount()
    {
        return _dateList.size();
    } //-- int getDateCount() 

    /**
     * Returns the value of field 'publisher'. The field
     * 'publisher' has the following description: Entity (e.g.
     * person or organisation) responsible for making the 
     *  resource available
     *  
     * 
     * @return the value of field 'publisher'.
     */
    public org.astrogrid.registry.generated.package.Publisher getPublisher()
    {
        return this._publisher;
    } //-- org.astrogrid.registry.generated.package.Publisher getPublisher() 

    /**
     * Returns the value of field 'version'. The field 'version'
     * has the following description: Label associated with
     * creation or availablilty of a version of 
     *  a resource.
     *  
     * 
     * @return the value of field 'version'.
     */
    public java.lang.String getVersion()
    {
        return this._version;
    } //-- java.lang.String getVersion() 

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
     * Method removeAllContributor
     */
    public void removeAllContributor()
    {
        _contributorList.removeAllElements();
    } //-- void removeAllContributor() 

    /**
     * Method removeAllDate
     */
    public void removeAllDate()
    {
        _dateList.removeAllElements();
    } //-- void removeAllDate() 

    /**
     * Method removeContributor
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Contributor removeContributor(int index)
    {
        java.lang.Object obj = _contributorList.elementAt(index);
        _contributorList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Contributor) obj;
    } //-- org.astrogrid.registry.generated.package.Contributor removeContributor(int) 

    /**
     * Method removeDate
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.Date removeDate(int index)
    {
        java.lang.Object obj = _dateList.elementAt(index);
        _dateList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.Date) obj;
    } //-- org.astrogrid.registry.generated.package.Date removeDate(int) 

    /**
     * Sets the value of field 'contact'. The field 'contact' has
     * the following description: Information that can be used for
     * contacting someone with
     *  regard to this resource.
     *  
     * 
     * @param contact the value of field 'contact'.
     */
    public void setContact(org.astrogrid.registry.generated.package.Contact contact)
    {
        this._contact = contact;
    } //-- void setContact(org.astrogrid.registry.generated.package.Contact) 

    /**
     * Method setContributor
     * 
     * @param index
     * @param vContributor
     */
    public void setContributor(int index, org.astrogrid.registry.generated.package.Contributor vContributor)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _contributorList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _contributorList.setElementAt(vContributor, index);
    } //-- void setContributor(int, org.astrogrid.registry.generated.package.Contributor) 

    /**
     * Method setContributor
     * 
     * @param contributorArray
     */
    public void setContributor(org.astrogrid.registry.generated.package.Contributor[] contributorArray)
    {
        //-- copy array
        _contributorList.removeAllElements();
        for (int i = 0; i < contributorArray.length; i++) {
            _contributorList.addElement(contributorArray[i]);
        }
    } //-- void setContributor(org.astrogrid.registry.generated.package.Contributor) 

    /**
     * Sets the value of field 'creator'. The field 'creator' has
     * the following description: The entity (e.g. person or
     * organisation) primarily responsible for
     *  creating the content or constitution of the resource
     *  
     * 
     * @param creator the value of field 'creator'.
     */
    public void setCreator(org.astrogrid.registry.generated.package.Creator creator)
    {
        this._creator = creator;
    } //-- void setCreator(org.astrogrid.registry.generated.package.Creator) 

    /**
     * Method setDate
     * 
     * @param index
     * @param vDate
     */
    public void setDate(int index, org.astrogrid.registry.generated.package.Date vDate)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _dateList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _dateList.setElementAt(vDate, index);
    } //-- void setDate(int, org.astrogrid.registry.generated.package.Date) 

    /**
     * Method setDate
     * 
     * @param dateArray
     */
    public void setDate(org.astrogrid.registry.generated.package.Date[] dateArray)
    {
        //-- copy array
        _dateList.removeAllElements();
        for (int i = 0; i < dateArray.length; i++) {
            _dateList.addElement(dateArray[i]);
        }
    } //-- void setDate(org.astrogrid.registry.generated.package.Date) 

    /**
     * Sets the value of field 'publisher'. The field 'publisher'
     * has the following description: Entity (e.g. person or
     * organisation) responsible for making the 
     *  resource available
     *  
     * 
     * @param publisher the value of field 'publisher'.
     */
    public void setPublisher(org.astrogrid.registry.generated.package.Publisher publisher)
    {
        this._publisher = publisher;
    } //-- void setPublisher(org.astrogrid.registry.generated.package.Publisher) 

    /**
     * Sets the value of field 'version'. The field 'version' has
     * the following description: Label associated with creation or
     * availablilty of a version of 
     *  a resource.
     *  
     * 
     * @param version the value of field 'version'.
     */
    public void setVersion(java.lang.String version)
    {
        this._version = version;
    } //-- void setVersion(java.lang.String) 

    /**
     * Method unmarshal
     * 
     * @param reader
     */
    public static java.lang.Object unmarshal(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.registry.generated.package.CurationType) Unmarshaller.unmarshal(org.astrogrid.registry.generated.package.CurationType.class, reader);
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
