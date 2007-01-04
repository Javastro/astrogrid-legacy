/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AccessURLUseType.java,v 1.2 2007/01/04 16:26:30 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AccessURLUseType.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:30 $
 */
public class AccessURLUseType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The full type
     */
    public static final int FULL_TYPE = 0;

    /**
     * The instance of the full type
     */
    public static final AccessURLUseType FULL = new AccessURLUseType(FULL_TYPE, "full");

    /**
     * The base type
     */
    public static final int BASE_TYPE = 1;

    /**
     * The instance of the base type
     */
    public static final AccessURLUseType BASE = new AccessURLUseType(BASE_TYPE, "base");

    /**
     * The dir type
     */
    public static final int DIR_TYPE = 2;

    /**
     * The instance of the dir type
     */
    public static final AccessURLUseType DIR = new AccessURLUseType(DIR_TYPE, "dir");

    /**
     * Field _memberTable
     */
    private static java.util.Hashtable _memberTable = init();

    /**
     * Field type
     */
    private int type = -1;

    /**
     * Field stringValue
     */
    private java.lang.String stringValue = null;


      //----------------/
     //- Constructors -/
    //----------------/

    private AccessURLUseType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.types.AccessURLUseType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AccessURLUseType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AccessURLUseType
     */
    public int getType()
    {
        return this.type;
    } //-- int getType() 

    /**
     * Method init
     */
    private static java.util.Hashtable init()
    {
        Hashtable members = new Hashtable();
        members.put("full", FULL);
        members.put("base", BASE);
        members.put("dir", DIR);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AccessURLUseType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AccessURLUseType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.types.AccessURLUseType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AccessURLUseType";
            throw new IllegalArgumentException(err);
        }
        return (AccessURLUseType) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.types.AccessURLUseType valueOf(java.lang.String) 

}
