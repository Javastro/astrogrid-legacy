/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: CoordFrameType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class CoordFrameType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class CoordFrameType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ICRS type
     */
    public static final int ICRS_TYPE = 0;

    /**
     * The instance of the ICRS type
     */
    public static final CoordFrameType ICRS = new CoordFrameType(ICRS_TYPE, "ICRS");

    /**
     * The FK5 type
     */
    public static final int FK5_TYPE = 1;

    /**
     * The instance of the FK5 type
     */
    public static final CoordFrameType FK5 = new CoordFrameType(FK5_TYPE, "FK5");

    /**
     * The FK4 type
     */
    public static final int FK4_TYPE = 2;

    /**
     * The instance of the FK4 type
     */
    public static final CoordFrameType FK4 = new CoordFrameType(FK4_TYPE, "FK4");

    /**
     * The ECL type
     */
    public static final int ECL_TYPE = 3;

    /**
     * The instance of the ECL type
     */
    public static final CoordFrameType ECL = new CoordFrameType(ECL_TYPE, "ECL");

    /**
     * The GAL type
     */
    public static final int GAL_TYPE = 4;

    /**
     * The instance of the GAL type
     */
    public static final CoordFrameType GAL = new CoordFrameType(GAL_TYPE, "GAL");

    /**
     * The SGAL type
     */
    public static final int SGAL_TYPE = 5;

    /**
     * The instance of the SGAL type
     */
    public static final CoordFrameType SGAL = new CoordFrameType(SGAL_TYPE, "SGAL");

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

    private CoordFrameType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.generated.package.types.CoordFrameType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CoordFrameType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CoordFrameType
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
        members.put("ICRS", ICRS);
        members.put("FK5", FK5);
        members.put("FK4", FK4);
        members.put("ECL", ECL);
        members.put("GAL", GAL);
        members.put("SGAL", SGAL);
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
     * CoordFrameType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CoordFrameType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.generated.package.types.CoordFrameType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CoordFrameType";
            throw new IllegalArgumentException(err);
        }
        return (CoordFrameType) obj;
    } //-- org.astrogrid.registry.generated.package.types.CoordFrameType valueOf(java.lang.String) 

}
