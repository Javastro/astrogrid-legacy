/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: WavebandType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
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
 * Class WavebandType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class WavebandType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Radio type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the Radio type
     */
    public static final WavebandType VALUE_0 = new WavebandType(VALUE_0_TYPE, "Radio");

    /**
     * The Millimeter type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the Millimeter type
     */
    public static final WavebandType VALUE_1 = new WavebandType(VALUE_1_TYPE, "Millimeter");

    /**
     * The Infrared type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the Infrared type
     */
    public static final WavebandType VALUE_2 = new WavebandType(VALUE_2_TYPE, "Infrared");

    /**
     * The Optical type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the Optical type
     */
    public static final WavebandType VALUE_3 = new WavebandType(VALUE_3_TYPE, "Optical");

    /**
     * The UV type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the UV type
     */
    public static final WavebandType VALUE_4 = new WavebandType(VALUE_4_TYPE, "UV");

    /**
     * The EUV type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the EUV type
     */
    public static final WavebandType VALUE_5 = new WavebandType(VALUE_5_TYPE, "EUV");

    /**
     * The X-ray type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the X-ray type
     */
    public static final WavebandType VALUE_6 = new WavebandType(VALUE_6_TYPE, "X-ray");

    /**
     * The Gamma-ray type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the Gamma-ray type
     */
    public static final WavebandType VALUE_7 = new WavebandType(VALUE_7_TYPE, "Gamma-ray");

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

    private WavebandType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.WavebandType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of WavebandType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this WavebandType
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
        members.put("Radio", VALUE_0);
        members.put("Millimeter", VALUE_1);
        members.put("Infrared", VALUE_2);
        members.put("Optical", VALUE_3);
        members.put("UV", VALUE_4);
        members.put("EUV", VALUE_5);
        members.put("X-ray", VALUE_6);
        members.put("Gamma-ray", VALUE_7);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * WavebandType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new WavebandType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.dataservice.types.WavebandType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid WavebandType";
            throw new IllegalArgumentException(err);
        }
        return (WavebandType) obj;
    } //-- org.astrogrid.registry.beans.resource.dataservice.types.WavebandType valueOf(java.lang.String) 

}
