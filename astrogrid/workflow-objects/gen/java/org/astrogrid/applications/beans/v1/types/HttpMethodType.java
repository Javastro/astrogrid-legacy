/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: HttpMethodType.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * http method type: get or post
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:00 $
 */
public class HttpMethodType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The get type
     */
    public static final int GET_TYPE = 0;

    /**
     * The instance of the get type
     */
    public static final HttpMethodType GET = new HttpMethodType(GET_TYPE, "get");

    /**
     * The post type
     */
    public static final int POST_TYPE = 1;

    /**
     * The instance of the post type
     */
    public static final HttpMethodType POST = new HttpMethodType(POST_TYPE, "post");

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

    private HttpMethodType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.types.HttpMethodType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of HttpMethodType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this HttpMethodType
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
        members.put("get", GET);
        members.put("post", POST);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * HttpMethodType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new HttpMethodType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.types.HttpMethodType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid HttpMethodType";
            throw new IllegalArgumentException(err);
        }
        return (HttpMethodType) obj;
    } //-- org.astrogrid.applications.beans.v1.types.HttpMethodType valueOf(java.lang.String) 

}
