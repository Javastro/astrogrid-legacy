/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ResourceStatusType.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
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
 * Class ResourceStatusType.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:00 $
 */
public class ResourceStatusType implements java.io.Serializable {


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
    public static final ResourceStatusType ACTIVE = new ResourceStatusType(ACTIVE_TYPE, "active");

    /**
     * The inactive type
     */
    public static final int INACTIVE_TYPE = 1;

    /**
     * The instance of the inactive type
     */
    public static final ResourceStatusType INACTIVE = new ResourceStatusType(INACTIVE_TYPE, "inactive");

    /**
     * The deleted type
     */
    public static final int DELETED_TYPE = 2;

    /**
     * The instance of the deleted type
     */
    public static final ResourceStatusType DELETED = new ResourceStatusType(DELETED_TYPE, "deleted");

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

    private ResourceStatusType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ResourceStatusType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ResourceStatusType
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
     * ResourceStatusType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ResourceStatusType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ResourceStatusType";
            throw new IllegalArgumentException(err);
        }
        return (ResourceStatusType) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.types.ResourceStatusType valueOf(java.lang.String) 

}
