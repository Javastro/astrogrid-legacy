/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: PosUnitType.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class PosUnitType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class PosUnitType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The deg type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the deg type
     */
    public static final PosUnitType VALUE_0 = new PosUnitType(VALUE_0_TYPE, "deg");

    /**
     * The rad type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the rad type
     */
    public static final PosUnitType VALUE_1 = new PosUnitType(VALUE_1_TYPE, "rad");

    /**
     * The h type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the h type
     */
    public static final PosUnitType VALUE_2 = new PosUnitType(VALUE_2_TYPE, "h");

    /**
     * The arcmin type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the arcmin type
     */
    public static final PosUnitType VALUE_3 = new PosUnitType(VALUE_3_TYPE, "arcmin");

    /**
     * The arcsec type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the arcsec type
     */
    public static final PosUnitType VALUE_4 = new PosUnitType(VALUE_4_TYPE, "arcsec");

    /**
     * The m type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the m type
     */
    public static final PosUnitType VALUE_5 = new PosUnitType(VALUE_5_TYPE, "m");

    /**
     * The km type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the km type
     */
    public static final PosUnitType VALUE_6 = new PosUnitType(VALUE_6_TYPE, "km");

    /**
     * The mm type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the mm type
     */
    public static final PosUnitType VALUE_7 = new PosUnitType(VALUE_7_TYPE, "mm");

    /**
     * The au type
     */
    public static final int VALUE_8_TYPE = 8;

    /**
     * The instance of the au type
     */
    public static final PosUnitType VALUE_8 = new PosUnitType(VALUE_8_TYPE, "au");

    /**
     * The pc type
     */
    public static final int VALUE_9_TYPE = 9;

    /**
     * The instance of the pc type
     */
    public static final PosUnitType VALUE_9 = new PosUnitType(VALUE_9_TYPE, "pc");

    /**
     * The kpc type
     */
    public static final int VALUE_10_TYPE = 10;

    /**
     * The instance of the kpc type
     */
    public static final PosUnitType VALUE_10 = new PosUnitType(VALUE_10_TYPE, "kpc");

    /**
     * The Mpc type
     */
    public static final int VALUE_11_TYPE = 11;

    /**
     * The instance of the Mpc type
     */
    public static final PosUnitType VALUE_11 = new PosUnitType(VALUE_11_TYPE, "Mpc");

    /**
     * The lyr type
     */
    public static final int VALUE_12_TYPE = 12;

    /**
     * The instance of the lyr type
     */
    public static final PosUnitType VALUE_12 = new PosUnitType(VALUE_12_TYPE, "lyr");

    /**
     * The  type
     */
    public static final int VALUE_13_TYPE = 13;

    /**
     * The instance of the  type
     */
    public static final PosUnitType VALUE_13 = new PosUnitType(VALUE_13_TYPE, "");

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

    private PosUnitType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of PosUnitType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this PosUnitType
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
        members.put("deg", VALUE_0);
        members.put("rad", VALUE_1);
        members.put("h", VALUE_2);
        members.put("arcmin", VALUE_3);
        members.put("arcsec", VALUE_4);
        members.put("m", VALUE_5);
        members.put("km", VALUE_6);
        members.put("mm", VALUE_7);
        members.put("au", VALUE_8);
        members.put("pc", VALUE_9);
        members.put("kpc", VALUE_10);
        members.put("Mpc", VALUE_11);
        members.put("lyr", VALUE_12);
        members.put("", VALUE_13);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * PosUnitType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new PosUnitType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid PosUnitType";
            throw new IllegalArgumentException(err);
        }
        return (PosUnitType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.PosUnitType valueOf(java.lang.String) 

}
