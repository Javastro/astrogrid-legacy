/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: HTTPQueryType.java,v 1.11 2005/01/23 12:51:24 jdt Exp $
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
 * The type of HTTP request, either GET or POST.
 *  
 * 
 * @version $Revision: 1.11 $ $Date: 2005/01/23 12:51:24 $
 */
public class HTTPQueryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The GET type
     */
    public static final int GET_TYPE = 0;

    /**
     * The instance of the GET type
     */
    public static final HTTPQueryType GET = new HTTPQueryType(GET_TYPE, "GET");

    /**
     * The POST type
     */
    public static final int POST_TYPE = 1;

    /**
     * The instance of the POST type
     */
    public static final HTTPQueryType POST = new HTTPQueryType(POST_TYPE, "POST");

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

    private HTTPQueryType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of HTTPQueryType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this HTTPQueryType
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
        members.put("GET", GET);
        members.put("POST", POST);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * HTTPQueryType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new HTTPQueryType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid HTTPQueryType";
            throw new IllegalArgumentException(err);
        }
        return (HTTPQueryType) obj;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.HTTPQueryType valueOf(java.lang.String) 

}
