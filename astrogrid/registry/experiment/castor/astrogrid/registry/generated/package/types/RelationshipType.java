/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: RelationshipType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package.types;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class RelationshipType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The mirror-of type
     */
    public static final int MIRROR_OF_TYPE = 0;

    /**
     * The instance of the mirror-of type
     */
    public static final RelationshipType MIRROR_OF = new RelationshipType(MIRROR_OF_TYPE, "mirror-of");

    /**
     * The service-for type
     */
    public static final int SERVICE_FOR_TYPE = 1;

    /**
     * The instance of the service-for type
     */
    public static final RelationshipType SERVICE_FOR = new RelationshipType(SERVICE_FOR_TYPE, "service-for");

    /**
     * The derived-from type
     */
    public static final int DERIVED_FROM_TYPE = 2;

    /**
     * The instance of the derived-from type
     */
    public static final RelationshipType DERIVED_FROM = new RelationshipType(DERIVED_FROM_TYPE, "derived-from");

    /**
     * The related-to type
     */
    public static final int RELATED_TO_TYPE = 3;

    /**
     * The instance of the related-to type
     */
    public static final RelationshipType RELATED_TO = new RelationshipType(RELATED_TO_TYPE, "related-to");

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
    } //-- org.astrogrid.registry.generated.package.types.RelationshipType(int, java.lang.String)


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
        members.put("mirror-of", MIRROR_OF);
        members.put("service-for", SERVICE_FOR);
        members.put("derived-from", DERIVED_FROM);
        members.put("related-to", RELATED_TO);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance. <br/>
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

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
    public static org.astrogrid.registry.generated.package.types.RelationshipType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid RelationshipType";
            throw new IllegalArgumentException(err);
        }
        return (RelationshipType) obj;
    } //-- org.astrogrid.registry.generated.package.types.RelationshipType valueOf(java.lang.String) 

}
