/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceTypeStatusType.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
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
 * Class ResourceTypeStatusType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class ResourceTypeStatusType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The active type
     */
    public static final int ACTIVE_TYPE = 0;

    /**
     * The instance of the active type
     */
    public static final ResourceTypeStatusType ACTIVE = new ResourceTypeStatusType(ACTIVE_TYPE, "active");

    /**
     * The inactive type
     */
    public static final int INACTIVE_TYPE = 1;

    /**
     * The instance of the inactive type
     */
    public static final ResourceTypeStatusType INACTIVE = new ResourceTypeStatusType(INACTIVE_TYPE, "inactive");

    /**
     * The deleted type
     */
    public static final int DELETED_TYPE = 2;

    /**
     * The instance of the deleted type
     */
    public static final ResourceTypeStatusType DELETED = new ResourceTypeStatusType(DELETED_TYPE, "deleted");

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

    private ResourceTypeStatusType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ResourceTypeStatusType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ResourceTypeStatusType
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
        members.put("active", ACTIVE);
        members.put("inactive", INACTIVE);
        members.put("deleted", DELETED);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ResourceTypeStatusType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ResourceTypeStatusType based on
     * the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ResourceTypeStatusType";
            throw new IllegalArgumentException(err);
        }
        return (ResourceTypeStatusType) obj;
    } //-- org.astrogrid.registry.beans.resource.types.ResourceTypeStatusType valueOf(java.lang.String) 

}
