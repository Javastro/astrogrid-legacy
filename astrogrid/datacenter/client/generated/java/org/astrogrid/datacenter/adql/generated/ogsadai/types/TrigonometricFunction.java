/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: TrigonometricFunction.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class TrigonometricFunction.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class TrigonometricFunction implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The SIN type
     */
    public static final int SIN_TYPE = 0;

    /**
     * The instance of the SIN type
     */
    public static final TrigonometricFunction SIN = new TrigonometricFunction(SIN_TYPE, "SIN");

    /**
     * The COS type
     */
    public static final int COS_TYPE = 1;

    /**
     * The instance of the COS type
     */
    public static final TrigonometricFunction COS = new TrigonometricFunction(COS_TYPE, "COS");

    /**
     * The TAN type
     */
    public static final int TAN_TYPE = 2;

    /**
     * The instance of the TAN type
     */
    public static final TrigonometricFunction TAN = new TrigonometricFunction(TAN_TYPE, "TAN");

    /**
     * The COT type
     */
    public static final int COT_TYPE = 3;

    /**
     * The instance of the COT type
     */
    public static final TrigonometricFunction COT = new TrigonometricFunction(COT_TYPE, "COT");

    /**
     * The ASIN type
     */
    public static final int ASIN_TYPE = 4;

    /**
     * The instance of the ASIN type
     */
    public static final TrigonometricFunction ASIN = new TrigonometricFunction(ASIN_TYPE, "ASIN");

    /**
     * The ACOS type
     */
    public static final int ACOS_TYPE = 5;

    /**
     * The instance of the ACOS type
     */
    public static final TrigonometricFunction ACOS = new TrigonometricFunction(ACOS_TYPE, "ACOS");

    /**
     * The ATAN type
     */
    public static final int ATAN_TYPE = 6;

    /**
     * The instance of the ATAN type
     */
    public static final TrigonometricFunction ATAN = new TrigonometricFunction(ATAN_TYPE, "ATAN");

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

    private TrigonometricFunction(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of TrigonometricFunction
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this TrigonometricFunction
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
        members.put("SIN", SIN);
        members.put("COS", COS);
        members.put("TAN", TAN);
        members.put("COT", COT);
        members.put("ASIN", ASIN);
        members.put("ACOS", ACOS);
        members.put("ATAN", ATAN);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * TrigonometricFunction
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new TrigonometricFunction based on
     * the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid TrigonometricFunction";
            throw new IllegalArgumentException(err);
        }
        return (TrigonometricFunction) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.TrigonometricFunction valueOf(java.lang.String) 

}
