/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: WavebandType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
 */

package org.astrogrid.registry.generated.package.types;

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
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class WavebandType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Radio type
     */
    public static final int RADIO_TYPE = 0;

    /**
     * The instance of the Radio type
     */
    public static final WavebandType RADIO = new WavebandType(RADIO_TYPE, "Radio");

    /**
     * The Millimeter type
     */
    public static final int MILLIMETER_TYPE = 1;

    /**
     * The instance of the Millimeter type
     */
    public static final WavebandType MILLIMETER = new WavebandType(MILLIMETER_TYPE, "Millimeter");

    /**
     * The Infrared type
     */
    public static final int INFRARED_TYPE = 2;

    /**
     * The instance of the Infrared type
     */
    public static final WavebandType INFRARED = new WavebandType(INFRARED_TYPE, "Infrared");

    /**
     * The Optical type
     */
    public static final int OPTICAL_TYPE = 3;

    /**
     * The instance of the Optical type
     */
    public static final WavebandType OPTICAL = new WavebandType(OPTICAL_TYPE, "Optical");

    /**
     * The UV type
     */
    public static final int UV_TYPE = 4;

    /**
     * The instance of the UV type
     */
    public static final WavebandType UV = new WavebandType(UV_TYPE, "UV");

    /**
     * The EUV type
     */
    public static final int EUV_TYPE = 5;

    /**
     * The instance of the EUV type
     */
    public static final WavebandType EUV = new WavebandType(EUV_TYPE, "EUV");

    /**
     * The X-ray type
     */
    public static final int X_RAY_TYPE = 6;

    /**
     * The instance of the X-ray type
     */
    public static final WavebandType X_RAY = new WavebandType(X_RAY_TYPE, "X-ray");

    /**
     * The Gamma-ray type
     */
    public static final int GAMMA_RAY_TYPE = 7;

    /**
     * The instance of the Gamma-ray type
     */
    public static final WavebandType GAMMA_RAY = new WavebandType(GAMMA_RAY_TYPE, "Gamma-ray");

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
    } //-- org.astrogrid.registry.generated.package.types.WavebandType(int, java.lang.String)


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
        members.put("Radio", RADIO);
        members.put("Millimeter", MILLIMETER);
        members.put("Infrared", INFRARED);
        members.put("Optical", OPTICAL);
        members.put("UV", UV);
        members.put("EUV", EUV);
        members.put("X-ray", X_RAY);
        members.put("Gamma-ray", GAMMA_RAY);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method readResolve will be called during deserialization to
     * replace the deserialized object with the correct constant
     * instance. <br/>
     */
    private java.lang.Object readResolve()
    {
        return valueOf(this.stringValue);
    } //-- java.lang.Object readResolve() 

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
    public static org.astrogrid.registry.generated.package.types.WavebandType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid WavebandType";
            throw new IllegalArgumentException(err);
        }
        return (WavebandType) obj;
    } //-- org.astrogrid.registry.generated.package.types.WavebandType valueOf(java.lang.String) 

}
