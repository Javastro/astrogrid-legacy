/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RelationshipType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
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
 * Class RelationshipType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class RelationshipType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The mirror-of type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the mirror-of type
     */
    public static final RelationshipType VALUE_0 = new RelationshipType(VALUE_0_TYPE, "mirror-of");

    /**
     * The service-for type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the service-for type
     */
    public static final RelationshipType VALUE_1 = new RelationshipType(VALUE_1_TYPE, "service-for");

    /**
     * The derived-from type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the derived-from type
     */
    public static final RelationshipType VALUE_2 = new RelationshipType(VALUE_2_TYPE, "derived-from");

    /**
     * The related-to type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the related-to type
     */
    public static final RelationshipType VALUE_3 = new RelationshipType(VALUE_3_TYPE, "related-to");

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

    private RelationshipType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.types.RelationshipType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of RelationshipType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this RelationshipType
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
        members.put("mirror-of", VALUE_0);
        members.put("service-for", VALUE_1);
        members.put("derived-from", VALUE_2);
        members.put("related-to", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * RelationshipType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new RelationshipType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.types.RelationshipType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid RelationshipType";
            throw new IllegalArgumentException(err);
        }
        return (RelationshipType) obj;
    } //-- org.astrogrid.registry.beans.resource.types.RelationshipType valueOf(java.lang.String) 

}
