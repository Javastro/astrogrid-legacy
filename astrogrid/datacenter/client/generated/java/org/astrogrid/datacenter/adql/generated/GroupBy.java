/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: GroupBy.java,v 1.7 2003/11/26 16:22:19 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated;

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
 * Class GroupBy.
 * 
 * @version $Revision: 1.7 $ $Date: 2003/11/26 16:22:19 $
 */
public class GroupBy extends org.astrogrid.datacenter.adql.AbstractQOM implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _groupByChoiceList
     */
    private java.util.Vector _groupByChoiceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public GroupBy() {
        super();
        _groupByChoiceList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.GroupBy()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addGroupByChoice
     * 
     * @param vGroupByChoice
     */
    public void addGroupByChoice(org.astrogrid.datacenter.adql.generated.GroupByChoice vGroupByChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupByChoiceList.addElement(vGroupByChoice);
    } //-- void addGroupByChoice(org.astrogrid.datacenter.adql.generated.GroupByChoice) 

    /**
     * Method addGroupByChoice
     * 
     * @param index
     * @param vGroupByChoice
     */
    public void addGroupByChoice(int index, org.astrogrid.datacenter.adql.generated.GroupByChoice vGroupByChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _groupByChoiceList.insertElementAt(vGroupByChoice, index);
    } //-- void addGroupByChoice(int, org.astrogrid.datacenter.adql.generated.GroupByChoice) 

    /**
     * Method enumerateGroupByChoice
     */
    public java.util.Enumeration enumerateGroupByChoice()
    {
        return _groupByChoiceList.elements();
    } //-- java.util.Enumeration enumerateGroupByChoice() 

    /**
     * Method getGroupByChoice
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.GroupByChoice getGroupByChoice(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupByChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.GroupByChoice) _groupByChoiceList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoice getGroupByChoice(int) 

    /**
     * Method getGroupByChoice
     */
    public org.astrogrid.datacenter.adql.generated.GroupByChoice[] getGroupByChoice()
    {
        int size = _groupByChoiceList.size();
        org.astrogrid.datacenter.adql.generated.GroupByChoice[] mArray = new org.astrogrid.datacenter.adql.generated.GroupByChoice[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.GroupByChoice) _groupByChoiceList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoice[] getGroupByChoice() 

    /**
     * Method getGroupByChoiceCount
     */
    public int getGroupByChoiceCount()
    {
        return _groupByChoiceList.size();
    } //-- int getGroupByChoiceCount() 

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
     * Method removeAllGroupByChoice
     */
    public void removeAllGroupByChoice()
    {
        _groupByChoiceList.removeAllElements();
    } //-- void removeAllGroupByChoice() 

    /**
     * Method removeGroupByChoice
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.GroupByChoice removeGroupByChoice(int index)
    {
        java.lang.Object obj = _groupByChoiceList.elementAt(index);
        _groupByChoiceList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.GroupByChoice) obj;
    } //-- org.astrogrid.datacenter.adql.generated.GroupByChoice removeGroupByChoice(int) 

    /**
     * Method setGroupByChoice
     * 
     * @param index
     * @param vGroupByChoice
     */
    public void setGroupByChoice(int index, org.astrogrid.datacenter.adql.generated.GroupByChoice vGroupByChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _groupByChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _groupByChoiceList.setElementAt(vGroupByChoice, index);
    } //-- void setGroupByChoice(int, org.astrogrid.datacenter.adql.generated.GroupByChoice) 

    /**
     * Method setGroupByChoice
     * 
     * @param groupByChoiceArray
     */
    public void setGroupByChoice(org.astrogrid.datacenter.adql.generated.GroupByChoice[] groupByChoiceArray)
    {
        //-- copy array
        _groupByChoiceList.removeAllElements();
        for (int i = 0; i < groupByChoiceArray.length; i++) {
            _groupByChoiceList.addElement(groupByChoiceArray[i]);
        }
    } //-- void setGroupByChoice(org.astrogrid.datacenter.adql.generated.GroupByChoice) 

    /**
     * Method unmarshalGroupBy
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.GroupBy unmarshalGroupBy(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.GroupBy) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.GroupBy.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.GroupBy unmarshalGroupBy(java.io.Reader) 

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
