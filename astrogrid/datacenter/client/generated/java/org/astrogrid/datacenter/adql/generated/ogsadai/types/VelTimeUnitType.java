/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: VelTimeUnitType.java,v 1.2 2004/03/26 16:03:34 eca Exp $
 */

package org.astrogrid.datacenter.adql.generated.ogsadai.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class VelTimeUnitType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class VelTimeUnitType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The s type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the s type
     */
    public static final VelTimeUnitType VALUE_0 = new VelTimeUnitType(VALUE_0_TYPE, "s");

    /**
     * The h type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the h type
     */
    public static final VelTimeUnitType VALUE_1 = new VelTimeUnitType(VALUE_1_TYPE, "h");

    /**
     * The d type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the d type
     */
    public static final VelTimeUnitType VALUE_2 = new VelTimeUnitType(VALUE_2_TYPE, "d");

    /**
     * The a type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the a type
     */
    public static final VelTimeUnitType VALUE_3 = new VelTimeUnitType(VALUE_3_TYPE, "a");

    /**
     * The yr type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the yr type
     */
    public static final VelTimeUnitType VALUE_4 = new VelTimeUnitType(VALUE_4_TYPE, "yr");

    /**
     * The century type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the century type
     */
    public static final VelTimeUnitType VALUE_5 = new VelTimeUnitType(VALUE_5_TYPE, "century");

    /**
     * The  type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the  type
     */
    public static final VelTimeUnitType VALUE_6 = new VelTimeUnitType(VALUE_6_TYPE, "");

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

    private VelTimeUnitType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.VelTimeUnitType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of VelTimeUnitType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this VelTimeUnitType
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
        members.put("s", VALUE_0);
        members.put("h", VALUE_1);
        members.put("d", VALUE_2);
        members.put("a", VALUE_3);
        members.put("yr", VALUE_4);
        members.put("century", VALUE_5);
        members.put("", VALUE_6);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * VelTimeUnitType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new VelTimeUnitType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.VelTimeUnitType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid VelTimeUnitType";
            throw new IllegalArgumentException(err);
        }
        return (VelTimeUnitType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.VelTimeUnitType valueOf(java.lang.String) 

}
