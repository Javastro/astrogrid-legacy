/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Rights.java,v 1.2 2005/07/05 08:27:01 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.dataservice.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Rights.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:01 $
 */
public class Rights implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The public type
     */
    public static final int PUBLIC_TYPE = 0;

    /**
     * The instance of the public type
     */
    public static final Rights PUBLIC = new Rights(PUBLIC_TYPE, "public");

    /**
     * The secure type
     */
    public static final int SECURE_TYPE = 1;

    /**
     * The instance of the secure type
     */
    public static final Rights SECURE = new Rights(SECURE_TYPE, "secure");

    /**
     * The proprietary type
     */
    public static final int PROPRIETARY_TYPE = 2;

    /**
     * The instance of the proprietary type
     */
    public static final Rights PROPRIETARY = new Rights(PROPRIETARY_TYPE, "proprietary");

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

    private Rights(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of Rights
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this Rights
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
     * Rights
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new Rights based on the given String
     * value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Rights";
            throw new IllegalArgumentException(err);
        }
        return (Rights) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Rights valueOf(java.lang.String) 

}
