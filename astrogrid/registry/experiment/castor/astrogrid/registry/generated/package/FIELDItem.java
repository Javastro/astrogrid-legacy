/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: FIELDItem.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Vector;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class FIELDItem.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class FIELDItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.generated.package.DESCRIPTION _DESCRIPTION;

    /**
     * Field _VALUESList
     */
    private java.util.Vector _VALUESList;

    /**
     * Field _LINK
     */
    private org.astrogrid.registry.generated.package.LINK _LINK;


      //----------------/
     //- Constructors -/
    //----------------/

    public FIELDItem() {
        super();
        _VALUESList = new Vector();
    } //-- org.astrogrid.registry.generated.package.FIELDItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addVALUES
     * 
     * @param vVALUES
     */
    public void addVALUES(org.astrogrid.registry.generated.package.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_VALUESList.size() < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.addElement(vVALUES);
    } //-- void addVALUES(org.astrogrid.registry.generated.package.VALUES) 

    /**
     * Method addVALUES
     * 
     * @param index
     * @param vVALUES
     */
    public void addVALUES(int index, org.astrogrid.registry.generated.package.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_VALUESList.size() < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.insertElementAt(vVALUES, index);
    } //-- void addVALUES(int, org.astrogrid.registry.generated.package.VALUES) 

    /**
     * Method enumerateVALUES
     */
    public java.util.Enumeration enumerateVALUES()
    {
        return _VALUESList.elements();
    } //-- java.util.Enumeration enumerateVALUES() 

    /**
     * Returns the value of field 'DESCRIPTION'.
     * 
     * @return the value of field 'DESCRIPTION'.
     */
    public org.astrogrid.registry.generated.package.DESCRIPTION getDESCRIPTION()
    {
        return this._DESCRIPTION;
    } //-- org.astrogrid.registry.generated.package.DESCRIPTION getDESCRIPTION() 

    /**
     * Returns the value of field 'LINK'.
     * 
     * @return the value of field 'LINK'.
     */
    public org.astrogrid.registry.generated.package.LINK getLINK()
    {
        return this._LINK;
    } //-- org.astrogrid.registry.generated.package.LINK getLINK() 

    /**
     * Method getVALUES
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.VALUES getVALUES(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _VALUESList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.generated.package.VALUES) _VALUESList.elementAt(index);
    } //-- org.astrogrid.registry.generated.package.VALUES getVALUES(int) 

    /**
     * Method getVALUES
     */
    public org.astrogrid.registry.generated.package.VALUES[] getVALUES()
    {
        int size = _VALUESList.size();
        org.astrogrid.registry.generated.package.VALUES[] mArray = new org.astrogrid.registry.generated.package.VALUES[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.generated.package.VALUES) _VALUESList.elementAt(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.generated.package.VALUES[] getVALUES() 

    /**
     * Method getVALUESCount
     */
    public int getVALUESCount()
    {
        return _VALUESList.size();
    } //-- int getVALUESCount() 

    /**
     * Method removeAllVALUES
     */
    public void removeAllVALUES()
    {
        _VALUESList.removeAllElements();
    } //-- void removeAllVALUES() 

    /**
     * Method removeVALUES
     * 
     * @param index
     */
    public org.astrogrid.registry.generated.package.VALUES removeVALUES(int index)
    {
        java.lang.Object obj = _VALUESList.elementAt(index);
        _VALUESList.removeElementAt(index);
        return (org.astrogrid.registry.generated.package.VALUES) obj;
    } //-- org.astrogrid.registry.generated.package.VALUES removeVALUES(int) 

    /**
     * Sets the value of field 'DESCRIPTION'.
     * 
     * @param DESCRIPTION the value of field 'DESCRIPTION'.
     */
    public void setDESCRIPTION(org.astrogrid.registry.generated.package.DESCRIPTION DESCRIPTION)
    {
        this._DESCRIPTION = DESCRIPTION;
    } //-- void setDESCRIPTION(org.astrogrid.registry.generated.package.DESCRIPTION) 

    /**
     * Sets the value of field 'LINK'.
     * 
     * @param LINK the value of field 'LINK'.
     */
    public void setLINK(org.astrogrid.registry.generated.package.LINK LINK)
    {
        this._LINK = LINK;
    } //-- void setLINK(org.astrogrid.registry.generated.package.LINK) 

    /**
     * Method setVALUES
     * 
     * @param index
     * @param vVALUES
     */
    public void setVALUES(int index, org.astrogrid.registry.generated.package.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _VALUESList.size())) {
            throw new IndexOutOfBoundsException();
        }
        if (!(index < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.setElementAt(vVALUES, index);
    } //-- void setVALUES(int, org.astrogrid.registry.generated.package.VALUES) 

    /**
     * Method setVALUES
     * 
     * @param VALUESArray
     */
    public void setVALUES(org.astrogrid.registry.generated.package.VALUES[] VALUESArray)
    {
        //-- copy array
        _VALUESList.removeAllElements();
        for (int i = 0; i < VALUESArray.length; i++) {
            _VALUESList.addElement(VALUESArray[i]);
        }
    } //-- void setVALUES(org.astrogrid.registry.generated.package.VALUES) 

}
