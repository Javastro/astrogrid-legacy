/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: DataType.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
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
 * Class DataType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class DataType implements java.io.Serializable {


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
    public static final DataType BOOLEAN = new DataType(BOOLEAN_TYPE, "boolean");

    /**
     * The bit type
     */
    public static final int BIT_TYPE = 1;

    /**
     * The instance of the bit type
     */
    public static final DataType BIT = new DataType(BIT_TYPE, "bit");

    /**
     * The unsignedByte type
     */
    public static final int UNSIGNEDBYTE_TYPE = 2;

    /**
     * The instance of the unsignedByte type
     */
    public static final DataType UNSIGNEDBYTE = new DataType(UNSIGNEDBYTE_TYPE, "unsignedByte");

    /**
     * The short type
     */
    public static final int SHORT_TYPE = 3;

    /**
     * The instance of the short type
     */
    public static final DataType SHORT = new DataType(SHORT_TYPE, "short");

    /**
     * The int type
     */
    public static final int INT_TYPE = 4;

    /**
     * The instance of the int type
     */
    public static final DataType INT = new DataType(INT_TYPE, "int");

    /**
     * The long type
     */
    public static final int LONG_TYPE = 5;

    /**
     * The instance of the long type
     */
    public static final DataType LONG = new DataType(LONG_TYPE, "long");

    /**
     * The char type
     */
    public static final int CHAR_TYPE = 6;

    /**
     * The instance of the char type
     */
    public static final DataType CHAR = new DataType(CHAR_TYPE, "char");

    /**
     * The unicodeChar type
     */
    public static final int UNICODECHAR_TYPE = 7;

    /**
     * The instance of the unicodeChar type
     */
    public static final DataType UNICODECHAR = new DataType(UNICODECHAR_TYPE, "unicodeChar");

    /**
     * The float type
     */
    public static final int FLOAT_TYPE = 8;

    /**
     * The instance of the float type
     */
    public static final DataType FLOAT = new DataType(FLOAT_TYPE, "float");

    /**
     * The double type
     */
    public static final int DOUBLE_TYPE = 9;

    /**
     * The instance of the double type
     */
    public static final DataType DOUBLE = new DataType(DOUBLE_TYPE, "double");

    /**
     * The floatComplex type
     */
    public static final int FLOATCOMPLEX_TYPE = 10;

    /**
     * The instance of the floatComplex type
     */
    public static final DataType FLOATCOMPLEX = new DataType(FLOATCOMPLEX_TYPE, "floatComplex");

    /**
     * The doubleComplex type
     */
    public static final int DOUBLECOMPLEX_TYPE = 11;

    /**
     * The instance of the doubleComplex type
     */
    public static final DataType DOUBLECOMPLEX = new DataType(DOUBLECOMPLEX_TYPE, "doubleComplex");

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

    private DataType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.DataType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of DataType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this DataType
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
     * DataType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new DataType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.DataType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid DataType";
            throw new IllegalArgumentException(err);
        }
        return (DataType) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.DataType valueOf(java.lang.String) 

}
