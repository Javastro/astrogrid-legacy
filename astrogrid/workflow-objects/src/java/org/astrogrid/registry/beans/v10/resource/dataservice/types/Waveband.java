/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Waveband.java,v 1.2 2007/01/04 16:26:20 clq2 Exp $
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
 * Class Waveband.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:20 $
 */
public class Waveband implements java.io.Serializable {


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
    public static final Waveband VALUE_0 = new Waveband(VALUE_0_TYPE, "Radio");

    /**
     * The Millimeter type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the Millimeter type
     */
    public static final Waveband VALUE_1 = new Waveband(VALUE_1_TYPE, "Millimeter");

    /**
     * The Infrared type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the Infrared type
     */
    public static final Waveband VALUE_2 = new Waveband(VALUE_2_TYPE, "Infrared");

    /**
     * The Optical type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the Optical type
     */
    public static final Waveband VALUE_3 = new Waveband(VALUE_3_TYPE, "Optical");

    /**
     * The UV type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the UV type
     */
    public static final Waveband VALUE_4 = new Waveband(VALUE_4_TYPE, "UV");

    /**
     * The EUV type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the EUV type
     */
    public static final Waveband VALUE_5 = new Waveband(VALUE_5_TYPE, "EUV");

    /**
     * The X-ray type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the X-ray type
     */
    public static final Waveband VALUE_6 = new Waveband(VALUE_6_TYPE, "X-ray");

    /**
     * The Gamma-ray type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the Gamma-ray type
     */
    public static final Waveband VALUE_7 = new Waveband(VALUE_7_TYPE, "Gamma-ray");

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

    private Waveband(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of Waveband
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this Waveband
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
     * Waveband
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new Waveband based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Waveband";
            throw new IllegalArgumentException(err);
        }
        return (Waveband) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.Waveband valueOf(java.lang.String) 

}
