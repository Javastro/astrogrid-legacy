/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RightsType.java,v 1.4 2004/03/09 09:45:24 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.dataservice.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class RightsType.
 * 
 * @version $Revision: 1.4 $ $Date: 2004/03/09 09:45:24 $
 */
public class RightsType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The public type
     */
    public static final int PUBLIC_TYPE = 1;

    /**
     * The instance of the public type
     */
    public static final RightsType PUBLIC = new RightsType(PUBLIC_TYPE, "public");

    /**
     * The secure type
     */
    public static final int SECURE_TYPE = 2;

    /**
     * The instance of the secure type
     */
    public static final RightsType SECURE = new RightsType(SECURE_TYPE, "secure");

    /**
     * The proprietary type
     */
    public static final int PROPRIETARY_TYPE = 3;

    /**
     * The instance of the proprietary type
     */
    public static final RightsType PROPRIETARY = new RightsType(PROPRIETARY_TYPE, "proprietary");

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

    private RightsType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.RightsType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of RightsType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this RightsType
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
        members.put("public", PUBLIC);
        members.put("secure", SECURE);
        members.put("proprietary", PROPRIETARY);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * RightsType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new RightsType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.dataservice.types.RightsType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid RightsType";
            throw new IllegalArgumentException(err);
        }
        return (RightsType) obj;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.RightsType valueOf(java.lang.String) 

}
