/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ArrayOfAlias.java,v 1.2 2004/03/26 16:03:33 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai;

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
 * Class ArrayOfAlias.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:33 $
 */
public class ArrayOfAlias extends org.astrogrid.datacenter.adql.AbstractQOM 
implements java.io.Serializable
{


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _aliasList
     */
    private java.util.Vector _aliasList;


      //----------------/
     //- Constructors -/
    //----------------/

    public ArrayOfAlias() {
        super();
        _aliasList = new Vector();
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addAlias
     * 
     * @param vAlias
     */
    public void addAlias(org.astrogrid.datacenter.adql.generated.ogsadai.Alias vAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        _aliasList.addElement(vAlias);
    } //-- void addAlias(org.astrogrid.datacenter.adql.generated.ogsadai.Alias) 

    /**
     * Method addAlias
     * 
     * @param index
     * @param vAlias
     */
    public void addAlias(int index, org.astrogrid.datacenter.adql.generated.ogsadai.Alias vAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        _aliasList.insertElementAt(vAlias, index);
    } //-- void addAlias(int, org.astrogrid.datacenter.adql.generated.ogsadai.Alias) 

    /**
     * Method enumerateAlias
     */
    public java.util.Enumeration enumerateAlias()
    {
        return _aliasList.elements();
    } //-- java.util.Enumeration enumerateAlias() 

    /**
     * Method getAlias
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Alias getAlias(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _aliasList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Alias) _aliasList.elementAt(index);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Alias getAlias(int) 

    /**
     * Method getAlias
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Alias[] getAlias()
    {
        int size = _aliasList.size();
        org.astrogrid.datacenter.adql.generated.ogsadai.Alias[] mArray = new org.astrogrid.datacenter.adql.generated.ogsadai.Alias[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.datacenter.adql.generated.ogsadai.Alias) _aliasList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Alias[] getAlias() 

    /**
     * Method getAliasCount
     */
    public int getAliasCount()
    {
        return _aliasList.size();
    } //-- int getAliasCount() 

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
     * Method removeAlias
     * 
     * @param index
     */
    public org.astrogrid.datacenter.adql.generated.ogsadai.Alias removeAlias(int index)
    {
        java.lang.Object obj = _aliasList.elementAt(index);
        _aliasList.removeElementAt(index);
        return (org.astrogrid.datacenter.adql.generated.ogsadai.Alias) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.Alias removeAlias(int) 

    /**
     * Method removeAllAlias
     */
    public void removeAllAlias()
    {
        _aliasList.removeAllElements();
    } //-- void removeAllAlias() 

    /**
     * Method setAlias
     * 
     * @param index
     * @param vAlias
     */
    public void setAlias(int index, org.astrogrid.datacenter.adql.generated.ogsadai.Alias vAlias)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _aliasList.size())) {
            throw new IndexOutOfBoundsException();
        }
        _aliasList.setElementAt(vAlias, index);
    } //-- void setAlias(int, org.astrogrid.datacenter.adql.generated.ogsadai.Alias) 

    /**
     * Method setAlias
     * 
     * @param aliasArray
     */
    public void setAlias(org.astrogrid.datacenter.adql.generated.ogsadai.Alias[] aliasArray)
    {
        //-- copy array
        _aliasList.removeAllElements();
        for (int i = 0; i < aliasArray.length; i++) {
            _aliasList.addElement(aliasArray[i]);
        }
    } //-- void setAlias(org.astrogrid.datacenter.adql.generated.ogsadai.Alias) 

    /**
     * Method unmarshalArrayOfAlias
     * 
     * @param reader
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias unmarshalArrayOfAlias(java.io.Reader reader)
        throws org.exolab.castor.xml.MarshalException, org.exolab.castor.xml.ValidationException
    {
        return (org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias) Unmarshaller.unmarshal(org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias.class, reader);
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.ArrayOfAlias unmarshalArrayOfAlias(java.io.Reader) 

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
