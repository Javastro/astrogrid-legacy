/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AccessURLTypeUseType.java,v 1.2 2004/03/03 16:22:08 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AccessURLTypeUseType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/03 16:22:08 $
 */
public class AccessURLTypeUseType implements java.io.Serializable {


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
    public static final AccessURLTypeUseType FULL = new AccessURLTypeUseType(FULL_TYPE, "full");

    /**
     * The base type
     */
    public static final int BASE_TYPE = 1;

    /**
     * The instance of the base type
     */
    public static final AccessURLTypeUseType BASE = new AccessURLTypeUseType(BASE_TYPE, "base");

    /**
     * The dir type
     */
    public static final int DIR_TYPE = 2;

    /**
     * The instance of the dir type
     */
    public static final AccessURLTypeUseType DIR = new AccessURLTypeUseType(DIR_TYPE, "dir");

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

    private AccessURLTypeUseType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AccessURLTypeUseType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AccessURLTypeUseType
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
     * AccessURLTypeUseType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AccessURLTypeUseType based on
     * the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AccessURLTypeUseType";
            throw new IllegalArgumentException(err);
        }
        return (AccessURLTypeUseType) obj;
    } //-- org.astrogrid.registry.beans.resource.types.AccessURLTypeUseType valueOf(java.lang.String) 

}
