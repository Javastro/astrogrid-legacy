/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: QueryType.java,v 1.1 2003/11/21 17:30:19 nw Exp $
 */

package org.astrogrid.datacenter.axisdataserver.types.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class QueryType.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/11/21 17:30:19 $
 */
public class QueryType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The adql-it04 type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the adql-it04 type
     */
    public static final QueryType VALUE_0 = new QueryType(VALUE_0_TYPE, "adql-it04");

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

    private QueryType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.axisdataserver.types.types.QueryType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of QueryType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this QueryType
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
        members.put("adql-it04", VALUE_0);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * QueryType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new QueryType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.axisdataserver.types.types.QueryType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid QueryType";
            throw new IllegalArgumentException(err);
        }
        return (QueryType) obj;
    } //-- org.astrogrid.datacenter.axisdataserver.types.types.QueryType valueOf(java.lang.String) 

}
