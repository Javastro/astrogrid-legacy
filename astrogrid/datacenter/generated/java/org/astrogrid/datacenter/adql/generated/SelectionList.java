/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SelectionList.java,v 1.1 2003/09/10 13:01:27 nw Exp $
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
 * Class SelectionList.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/09/10 13:01:27 $
 */
public class SelectionList extends org.astrogrid.datacenter.adql.generated.Selection 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _selectionListChoiceList
     */
    private java.util.Vector _selectionListChoiceList;


      //----------------/
     //- Constructors -/
    //----------------/

    public SelectionList() {
        super();
        _selectionListChoiceList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.SelectionList()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addSelectionListChoice
     * 
     * @param vSelectionListChoice
     */
    public void addSelectionListChoice(org.astrogrid.datacenter.adql.generated.SelectionListChoice vSelectionListChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _selectionListChoiceList.addElement(vSelectionListChoice);
    } //-- void addSelectionListChoice(org.astrogrid.datacenter.adql.generated.SelectionListChoice) 

    /**
     * Method addSelectionListChoice
     * 
     * @param index
     * @param vSelectionListChoice
     */
    public void addSelectionListChoice(int index, org.astrogrid.datacenter.adql.generated.SelectionListChoice vSelectionListChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        _selectionListChoiceList.insertElementAt(vSelectionListChoice, index);
    } //-- void addSelectionListChoice(int, org.astrogrid.datacenter.adql.generated.SelectionListChoice) 

    /**
     * Method enumerateSelectionListChoice
     */
    public java.util.Enumeration enumerateSelectionListChoice()
    {
        return _selectionListChoiceList.elements();
    } //-- java.util.Enumeration enumerateSelectionListChoice() 

    /**
     * Method getSelectionListChoice
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.SelectionListChoice getSelectionListChoice(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _selectionListChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.SelectionListChoice) _selectionListChoiceList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoice getSelectionListChoice(int) 

    /**
     * Method getSelectionListChoice
     */
    public org.astrogrid.datacenter.adql.generated.SelectionListChoice[] getSelectionListChoice()
    {
        int size = _selectionListChoiceList.size();
        org.astrogrid.datacenter.adql.generated.SelectionListChoice[] mArray = new org.astrogrid.datacenter.adql.generated.SelectionListChoice[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.SelectionListChoice) _selectionListChoiceList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoice[] getSelectionListChoice() 

    /**
     * Method getSelectionListChoiceCount
     */
    public int getSelectionListChoiceCount()
    {
        return _selectionListChoiceList.size();
    } //-- int getSelectionListChoiceCount() 

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
     * Method removeAllSelectionListChoice
     */
    public void removeAllSelectionListChoice()
    {
        _selectionListChoiceList.removeAllElements();
    } //-- void removeAllSelectionListChoice() 

    /**
     * Method removeSelectionListChoice
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.SelectionListChoice removeSelectionListChoice(int index)
    {
        java.lang.Object obj = _selectionListChoiceList.elementAt(index);
        _selectionListChoiceList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.SelectionListChoice) obj;
    } //-- org.astrogrid.datacenter.adql.generated.SelectionListChoice removeSelectionListChoice(int) 

    /**
     * Method setSelectionListChoice
     * 
     * @param index
     * @param vSelectionListChoice
     */
    public void setSelectionListChoice(int index, org.astrogrid.datacenter.adql.generated.SelectionListChoice vSelectionListChoice)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _selectionListChoiceList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _selectionListChoiceList.setElementAt(vSelectionListChoice, index);
    } //-- void setSelectionListChoice(int, org.astrogrid.datacenter.adql.generated.SelectionListChoice) 

    /**
     * Method setSelectionListChoice
     * 
     * @param selectionListChoiceArray
     */
    public void setSelectionListChoice(org.astrogrid.datacenter.adql.generated.SelectionListChoice[] selectionListChoiceArray)
    {
        //-- copy array
        _selectionListChoiceList.removeAllElements();
        for (int i = 0; i < selectionListChoiceArray.length; i++) {
            _selectionListChoiceList.addElement(selectionListChoiceArray[i]);
        }
    } //-- void setSelectionListChoice(org.astrogrid.datacenter.adql.generated.SelectionListChoice) 

    /**
     * Method unmarshalSelectionList
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.SelectionList unmarshalSelectionList(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.SelectionList) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.SelectionList.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.SelectionList unmarshalSelectionList(java.io.Reader) 

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
