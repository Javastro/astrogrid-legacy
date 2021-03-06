/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ScalarDataType.java,v 1.2 2007/01/04 16:26:19 clq2 Exp $
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
 * Class ScalarDataType.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:19 $
 */
public class ScalarDataType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The boolean type
     */
    public static final int BOOLEAN_TYPE = 0;

    /**
     * The instance of the boolean type
     */
    public static final ScalarDataType BOOLEAN = new ScalarDataType(BOOLEAN_TYPE, "boolean");

    /**
     * The bit type
     */
    public static final int BIT_TYPE = 1;

    /**
     * The instance of the bit type
     */
    public static final ScalarDataType BIT = new ScalarDataType(BIT_TYPE, "bit");

    /**
     * The unsignedByte type
     */
    public static final int UNSIGNEDBYTE_TYPE = 2;

    /**
     * The instance of the unsignedByte type
     */
    public static final ScalarDataType UNSIGNEDBYTE = new ScalarDataType(UNSIGNEDBYTE_TYPE, "unsignedByte");

    /**
     * The short type
     */
    public static final int SHORT_TYPE = 3;

    /**
     * The instance of the short type
     */
    public static final ScalarDataType SHORT = new ScalarDataType(SHORT_TYPE, "short");

    /**
     * The int type
     */
    public static final int INT_TYPE = 4;

    /**
     * The instance of the int type
     */
    public static final ScalarDataType INT = new ScalarDataType(INT_TYPE, "int");

    /**
     * The long type
     */
    public static final int LONG_TYPE = 5;

    /**
     * The instance of the long type
     */
    public static final ScalarDataType LONG = new ScalarDataType(LONG_TYPE, "long");

    /**
     * The char type
     */
    public static final int CHAR_TYPE = 6;

    /**
     * The instance of the char type
     */
    public static final ScalarDataType CHAR = new ScalarDataType(CHAR_TYPE, "char");

    /**
     * The unicodeChar type
     */
    public static final int UNICODECHAR_TYPE = 7;

    /**
     * The instance of the unicodeChar type
     */
    public static final ScalarDataType UNICODECHAR = new ScalarDataType(UNICODECHAR_TYPE, "unicodeChar");

    /**
     * The float type
     */
    public static final int FLOAT_TYPE = 8;

    /**
     * The instance of the float type
     */
    public static final ScalarDataType FLOAT = new ScalarDataType(FLOAT_TYPE, "float");

    /**
     * The double type
     */
    public static final int DOUBLE_TYPE = 9;

    /**
     * The instance of the double type
     */
    public static final ScalarDataType DOUBLE = new ScalarDataType(DOUBLE_TYPE, "double");

    /**
     * The floatComplex type
     */
    public static final int FLOATCOMPLEX_TYPE = 10;

    /**
     * The instance of the floatComplex type
     */
    public static final ScalarDataType FLOATCOMPLEX = new ScalarDataType(FLOATCOMPLEX_TYPE, "floatComplex");

    /**
     * The doubleComplex type
     */
    public static final int DOUBLECOMPLEX_TYPE = 11;

    /**
     * The instance of the doubleComplex type
     */
    public static final ScalarDataType DOUBLECOMPLEX = new ScalarDataType(DOUBLECOMPLEX_TYPE, "doubleComplex");

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

    private ScalarDataType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.ScalarDataType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ScalarDataType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ScalarDataType
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
        members.put("boolean", BOOLEAN);
        members.put("bit", BIT);
        members.put("unsignedByte", UNSIGNEDBYTE);
        members.put("short", SHORT);
        members.put("int", INT);
        members.put("long", LONG);
        members.put("char", CHAR);
        members.put("unicodeChar", UNICODECHAR);
        members.put("float", FLOAT);
        members.put("double", DOUBLE);
        members.put("floatComplex", FLOATCOMPLEX);
        members.put("doubleComplex", DOUBLECOMPLEX);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ScalarDataType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ScalarDataType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.types.ScalarDataType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ScalarDataType";
            throw new IllegalArgumentException(err);
        }
        return (ScalarDataType) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.ScalarDataType valueOf(java.lang.String) 

}
