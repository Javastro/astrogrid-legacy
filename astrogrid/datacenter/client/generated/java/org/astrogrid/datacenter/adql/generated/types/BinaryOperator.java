/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: BinaryOperator.java,v 1.5 2003/11/19 18:44:51 nw Exp $
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
 * Class BinaryOperator.
 * 
 * @version $Revision: 1.5 $ $Date: 2003/11/19 18:44:51 $
 */
public class BinaryOperator implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The + type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the + type
     */
    public static final BinaryOperator VALUE_0 = new BinaryOperator(VALUE_0_TYPE, "+");

    /**
     * The - type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the - type
     */
    public static final BinaryOperator VALUE_1 = new BinaryOperator(VALUE_1_TYPE, "-");

    /**
     * The * type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the * type
     */
    public static final BinaryOperator VALUE_2 = new BinaryOperator(VALUE_2_TYPE, "*");

    /**
     * The / type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the / type
     */
    public static final BinaryOperator VALUE_3 = new BinaryOperator(VALUE_3_TYPE, "/");

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

    private BinaryOperator(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.types.BinaryOperator(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of BinaryOperator
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this BinaryOperator
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
        members.put("+", VALUE_0);
        members.put("-", VALUE_1);
        members.put("*", VALUE_2);
        members.put("/", VALUE_3);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * BinaryOperator
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new BinaryOperator based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.types.BinaryOperator valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid BinaryOperator";
            throw new IllegalArgumentException(err);
        }
        return (BinaryOperator) obj;
    } //-- org.astrogrid.datacenter.adql.generated.types.BinaryOperator valueOf(java.lang.String) 

}
