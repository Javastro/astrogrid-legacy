/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TimeScaleType.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class TimeScaleType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class TimeScaleType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The TT type
     */
    public static final int TT_TYPE = 0;

    /**
     * The instance of the TT type
     */
    public static final TimeScaleType TT = new TimeScaleType(TT_TYPE, "TT");

    /**
     * The TDT type
     */
    public static final int TDT_TYPE = 1;

    /**
     * The instance of the TDT type
     */
    public static final TimeScaleType TDT = new TimeScaleType(TDT_TYPE, "TDT");

    /**
     * The ET type
     */
    public static final int ET_TYPE = 2;

    /**
     * The instance of the ET type
     */
    public static final TimeScaleType ET = new TimeScaleType(ET_TYPE, "ET");

    /**
     * The TDB type
     */
    public static final int TDB_TYPE = 3;

    /**
     * The instance of the TDB type
     */
    public static final TimeScaleType TDB = new TimeScaleType(TDB_TYPE, "TDB");

    /**
     * The TCG type
     */
    public static final int TCG_TYPE = 4;

    /**
     * The instance of the TCG type
     */
    public static final TimeScaleType TCG = new TimeScaleType(TCG_TYPE, "TCG");

    /**
     * The TCB type
     */
    public static final int TCB_TYPE = 5;

    /**
     * The instance of the TCB type
     */
    public static final TimeScaleType TCB = new TimeScaleType(TCB_TYPE, "TCB");

    /**
     * The TAI type
     */
    public static final int TAI_TYPE = 6;

    /**
     * The instance of the TAI type
     */
    public static final TimeScaleType TAI = new TimeScaleType(TAI_TYPE, "TAI");

    /**
     * The IAT type
     */
    public static final int IAT_TYPE = 7;

    /**
     * The instance of the IAT type
     */
    public static final TimeScaleType IAT = new TimeScaleType(IAT_TYPE, "IAT");

    /**
     * The UTC type
     */
    public static final int UTC_TYPE = 8;

    /**
     * The instance of the UTC type
     */
    public static final TimeScaleType UTC = new TimeScaleType(UTC_TYPE, "UTC");

    /**
     * The LST type
     */
    public static final int LST_TYPE = 9;

    /**
     * The instance of the LST type
     */
    public static final TimeScaleType LST = new TimeScaleType(LST_TYPE, "LST");

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

    private TimeScaleType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TimeScaleType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TimeScaleType
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
        members.put("TT", TT);
        members.put("TDT", TDT);
        members.put("ET", ET);
        members.put("TDB", TDB);
        members.put("TCG", TCG);
        members.put("TCB", TCB);
        members.put("TAI", TAI);
        members.put("IAT", IAT);
        members.put("UTC", UTC);
        members.put("LST", LST);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * TimeScaleType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TimeScaleType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TimeScaleType";
            throw new IllegalArgumentException(err);
        }
        return (TimeScaleType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TimeScaleType valueOf(java.lang.String) 

}
