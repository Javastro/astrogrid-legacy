/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: UnaryOperator.java,v 1.3 2003/09/16 13:23:24 nw Exp $
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
 * Class UnaryOperator.
 * 
 * @version $Revision: 1.3 $ $Date: 2003/09/16 13:23:24 $
 */
public class UnaryOperator implements java.io.Serializable {


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
    public static final UnaryOperator VALUE_0 = new UnaryOperator(VALUE_0_TYPE, "+");

    /**
     * The - type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the - type
     */
    public static final UnaryOperator VALUE_1 = new UnaryOperator(VALUE_1_TYPE, "-");

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

    private UnaryOperator(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.types.UnaryOperator(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of UnaryOperator
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this UnaryOperator
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
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * UnaryOperator
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new UnaryOperator based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.types.UnaryOperator valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid UnaryOperator";
            throw new IllegalArgumentException(err);
        }
        return (UnaryOperator) obj;
    } //-- org.astrogrid.datacenter.adql.generated.types.UnaryOperator valueOf(java.lang.String) 

}
