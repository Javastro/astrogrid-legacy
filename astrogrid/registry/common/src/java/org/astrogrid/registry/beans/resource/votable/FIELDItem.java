/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: FIELDItem.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Enumeration;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class FIELDItem.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class FIELDItem implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * Field _DESCRIPTION
     */
    private org.astrogrid.registry.beans.resource.votable.AnyTEXT _DESCRIPTION;

    /**
     * Field _VALUESList
     */
    private java.util.ArrayList _VALUESList;

    /**
     * Field _LINK
     */
    private org.astrogrid.registry.beans.resource.votable.LINK _LINK;


      //----------------/
     //- Constructors -/
    //----------------/

    public FIELDItem() {
        super();
        _VALUESList = new ArrayList();
    } //-- org.astrogrid.registry.beans.resource.votable.FIELDItem()


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method addVALUES
     * 
     * @param vVALUES
     */
    public void addVALUES(org.astrogrid.registry.beans.resource.votable.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_VALUESList.size() < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.add(vVALUES);
    } //-- void addVALUES(org.astrogrid.registry.beans.resource.votable.VALUES) 

    /**
     * Method addVALUES
     * 
     * @param index
     * @param vVALUES
     */
    public void addVALUES(int index, org.astrogrid.registry.beans.resource.votable.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        if (!(_VALUESList.size() < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.add(index, vVALUES);
    } //-- void addVALUES(int, org.astrogrid.registry.beans.resource.votable.VALUES) 

    /**
     * Method clearVALUES
     */
    public void clearVALUES()
    {
        _VALUESList.clear();
    } //-- void clearVALUES() 

    /**
     * Method enumerateVALUES
     */
    public java.util.Enumeration enumerateVALUES()
    {
        return new org.exolab.castor.util.IteratorEnumeration(_VALUESList.iterator());
    } //-- java.util.Enumeration enumerateVALUES() 

    /**
     * Returns the value of field 'DESCRIPTION'.
     * 
     * @return the value of field 'DESCRIPTION'.
     */
    public org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION()
    {
        return this._DESCRIPTION;
    } //-- org.astrogrid.registry.beans.resource.votable.AnyTEXT getDESCRIPTION() 

    /**
     * Returns the value of field 'LINK'.
     * 
     * @return the value of field 'LINK'.
     */
    public org.astrogrid.registry.beans.resource.votable.LINK getLINK()
    {
        return this._LINK;
    } //-- org.astrogrid.registry.beans.resource.votable.LINK getLINK() 

    /**
     * Method getVALUES
     * 
     * @param index
     */
    public org.astrogrid.registry.beans.resource.votable.VALUES getVALUES(int index)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _VALUESList.size())) {
            throw new IndexOutOfBoundsException();
        }
        
        return (org.astrogrid.registry.beans.resource.votable.VALUES) _VALUESList.get(index);
    } //-- org.astrogrid.registry.beans.resource.votable.VALUES getVALUES(int) 

    /**
     * Method getVALUES
     */
    public org.astrogrid.registry.beans.resource.votable.VALUES[] getVALUES()
    {
        int size = _VALUESList.size();
        org.astrogrid.registry.beans.resource.votable.VALUES[] mArray = new org.astrogrid.registry.beans.resource.votable.VALUES[size];
        for (int index = 0; index < size; index++) {
            mArray[index] = (org.astrogrid.registry.beans.resource.votable.VALUES) _VALUESList.get(index);
        }
        return mArray;
    } //-- org.astrogrid.registry.beans.resource.votable.VALUES[] getVALUES() 

    /**
     * Method getVALUESCount
     */
    public int getVALUESCount()
    {
        return _VALUESList.size();
    } //-- int getVALUESCount() 

    /**
     * Method removeVALUES
     * 
     * @param vVALUES
     */
    public boolean removeVALUES(org.astrogrid.registry.beans.resource.votable.VALUES vVALUES)
    {
        boolean removed = _VALUESList.remove(vVALUES);
        return removed;
    } //-- boolean removeVALUES(org.astrogrid.registry.beans.resource.votable.VALUES) 

    /**
     * Sets the value of field 'DESCRIPTION'.
     * 
     * @param DESCRIPTION the value of field 'DESCRIPTION'.
     */
    public void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT DESCRIPTION)
    {
        this._DESCRIPTION = DESCRIPTION;
    } //-- void setDESCRIPTION(org.astrogrid.registry.beans.resource.votable.AnyTEXT) 

    /**
     * Sets the value of field 'LINK'.
     * 
     * @param LINK the value of field 'LINK'.
     */
    public void setLINK(org.astrogrid.registry.beans.resource.votable.LINK LINK)
    {
        this._LINK = LINK;
    } //-- void setLINK(org.astrogrid.registry.beans.resource.votable.LINK) 

    /**
     * Method setVALUES
     * 
     * @param index
     * @param vVALUES
     */
    public void setVALUES(int index, org.astrogrid.registry.beans.resource.votable.VALUES vVALUES)
        throws java.lang.IndexOutOfBoundsException
    {
        //-- check bounds for index
        if ((index < 0) || (index > _VALUESList.size())) {
            throw new IndexOutOfBoundsException();
        }
        if (!(index < 2)) {
            throw new IndexOutOfBoundsException();
        }
        _VALUESList.set(index, vVALUES);
    } //-- void setVALUES(int, org.astrogrid.registry.beans.resource.votable.VALUES) 

    /**
     * Method setVALUES
     * 
     * @param VALUESArray
     */
    public void setVALUES(org.astrogrid.registry.beans.resource.votable.VALUES[] VALUESArray)
    {
        //-- copy array
        _VALUESList.clear();
        for (int i = 0; i < VALUESArray.length; i++) {
            _VALUESList.add(VALUESArray[i]);
        }
    } //-- void setVALUES(org.astrogrid.registry.beans.resource.votable.VALUES) 

}
