/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: MathFunction.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class MathFunction.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class MathFunction implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ABS type
     */
    public static final int ABS_TYPE = 0;

    /**
     * The instance of the ABS type
     */
    public static final MathFunction ABS = new MathFunction(ABS_TYPE, "ABS");

    /**
     * The CEILING type
     */
    public static final int CEILING_TYPE = 1;

    /**
     * The instance of the CEILING type
     */
    public static final MathFunction CEILING = new MathFunction(CEILING_TYPE, "CEILING");

    /**
     * The DEGREES type
     */
    public static final int DEGREES_TYPE = 2;

    /**
     * The instance of the DEGREES type
     */
    public static final MathFunction DEGREES = new MathFunction(DEGREES_TYPE, "DEGREES");

    /**
     * The EXP type
     */
    public static final int EXP_TYPE = 3;

    /**
     * The instance of the EXP type
     */
    public static final MathFunction EXP = new MathFunction(EXP_TYPE, "EXP");

    /**
     * The FLOOR type
     */
    public static final int FLOOR_TYPE = 4;

    /**
     * The instance of the FLOOR type
     */
    public static final MathFunction FLOOR = new MathFunction(FLOOR_TYPE, "FLOOR");

    /**
     * The LOG type
     */
    public static final int LOG_TYPE = 5;

    /**
     * The instance of the LOG type
     */
    public static final MathFunction LOG = new MathFunction(LOG_TYPE, "LOG");

    /**
     * The PI type
     */
    public static final int PI_TYPE = 6;

    /**
     * The instance of the PI type
     */
    public static final MathFunction PI = new MathFunction(PI_TYPE, "PI");

    /**
     * The POWER type
     */
    public static final int POWER_TYPE = 7;

    /**
     * The instance of the POWER type
     */
    public static final MathFunction POWER = new MathFunction(POWER_TYPE, "POWER");

    /**
     * The RADIANS type
     */
    public static final int RADIANS_TYPE = 8;

    /**
     * The instance of the RADIANS type
     */
    public static final MathFunction RADIANS = new MathFunction(RADIANS_TYPE, "RADIANS");

    /**
     * The SQRT type
     */
    public static final int SQRT_TYPE = 9;

    /**
     * The instance of the SQRT type
     */
    public static final MathFunction SQRT = new MathFunction(SQRT_TYPE, "SQRT");

    /**
     * The SQUARE type
     */
    public static final int SQUARE_TYPE = 10;

    /**
     * The instance of the SQUARE type
     */
    public static final MathFunction SQUARE = new MathFunction(SQUARE_TYPE, "SQUARE");

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

    private MathFunction(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of MathFunction
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this MathFunction
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
        members.put("ABS", ABS);
        members.put("CEILING", CEILING);
        members.put("DEGREES", DEGREES);
        members.put("EXP", EXP);
        members.put("FLOOR", FLOOR);
        members.put("LOG", LOG);
        members.put("PI", PI);
        members.put("POWER", POWER);
        members.put("RADIANS", RADIANS);
        members.put("SQRT", SQRT);
        members.put("SQUARE", SQUARE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * MathFunction
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new MathFunction based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid MathFunction";
            throw new IllegalArgumentException(err);
        }
        return (MathFunction) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.MathFunction valueOf(java.lang.String) 

}
