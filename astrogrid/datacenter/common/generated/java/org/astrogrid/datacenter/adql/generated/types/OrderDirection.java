/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: OrderDirection.java,v 1.1 2003/10/14 12:36:54 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class OrderDirection.
 * 
 * @version $Revision: 1.1 $ $Date: 2003/10/14 12:36:54 $
 */
public class OrderDirection implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ASC type
     */
    public static final int ASC_TYPE = 0;

    /**
     * The instance of the ASC type
     */
    public static final OrderDirection ASC = new OrderDirection(ASC_TYPE, "ASC");

    /**
     * The DESC type
     */
    public static final int DESC_TYPE = 1;

    /**
     * The instance of the DESC type
     */
    public static final OrderDirection DESC = new OrderDirection(DESC_TYPE, "DESC");

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

    private OrderDirection(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.types.OrderDirection(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of OrderDirection
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this OrderDirection
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
        members.put("ASC", ASC);
        members.put("DESC", DESC);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * OrderDirection
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new OrderDirection based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.types.OrderDirection valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid OrderDirection";
            throw new IllegalArgumentException(err);
        }
        return (OrderDirection) obj;
    } //-- org.astrogrid.datacenter.adql.generated.types.OrderDirection valueOf(java.lang.String) 

}
