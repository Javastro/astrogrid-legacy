/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: STREAMEncodingType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.votable.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class STREAMEncodingType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class STREAMEncodingType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The gzip type
     */
    public static final int GZIP_TYPE = 0;

    /**
     * The instance of the gzip type
     */
    public static final STREAMEncodingType GZIP = new STREAMEncodingType(GZIP_TYPE, "gzip");

    /**
     * The base64 type
     */
    public static final int BASE64_TYPE = 1;

    /**
     * The instance of the base64 type
     */
    public static final STREAMEncodingType BASE64 = new STREAMEncodingType(BASE64_TYPE, "base64");

    /**
     * The dynamic type
     */
    public static final int DYNAMIC_TYPE = 2;

    /**
     * The instance of the dynamic type
     */
    public static final STREAMEncodingType DYNAMIC = new STREAMEncodingType(DYNAMIC_TYPE, "dynamic");

    /**
     * The none type
     */
    public static final int NONE_TYPE = 3;

    /**
     * The instance of the none type
     */
    public static final STREAMEncodingType NONE = new STREAMEncodingType(NONE_TYPE, "none");

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

    private STREAMEncodingType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of STREAMEncodingType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this STREAMEncodingType
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
        members.put("gzip", GZIP);
        members.put("base64", BASE64);
        members.put("dynamic", DYNAMIC);
        members.put("none", NONE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * STREAMEncodingType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new STREAMEncodingType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid STREAMEncodingType";
            throw new IllegalArgumentException(err);
        }
        return (STREAMEncodingType) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.STREAMEncodingType valueOf(java.lang.String) 

}
