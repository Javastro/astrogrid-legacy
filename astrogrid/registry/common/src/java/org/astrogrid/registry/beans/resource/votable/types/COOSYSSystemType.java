/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: COOSYSSystemType.java,v 1.5 2004/03/11 14:08:05 KevinBenson Exp $
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
 * Class COOSYSSystemType.
 * 
 * @version $Revision: 1.5 $ $Date: 2004/03/11 14:08:05 $
 */
public class COOSYSSystemType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The eq_FK4 type
     */
    public static final int EQ_FK4_TYPE = 0;

    /**
     * The instance of the eq_FK4 type
     */
    public static final COOSYSSystemType EQ_FK4 = new COOSYSSystemType(EQ_FK4_TYPE, "eq_FK4");

    /**
     * The eq_FK5 type
     */
    public static final int EQ_FK5_TYPE = 1;

    /**
     * The instance of the eq_FK5 type
     */
    public static final COOSYSSystemType EQ_FK5 = new COOSYSSystemType(EQ_FK5_TYPE, "eq_FK5");

    /**
     * The ICRS type
     */
    public static final int ICRS_TYPE = 2;

    /**
     * The instance of the ICRS type
     */
    public static final COOSYSSystemType ICRS = new COOSYSSystemType(ICRS_TYPE, "ICRS");

    /**
     * The ecl_FK4 type
     */
    public static final int ECL_FK4_TYPE = 3;

    /**
     * The instance of the ecl_FK4 type
     */
    public static final COOSYSSystemType ECL_FK4 = new COOSYSSystemType(ECL_FK4_TYPE, "ecl_FK4");

    /**
     * The ecl_FK5 type
     */
    public static final int ECL_FK5_TYPE = 4;

    /**
     * The instance of the ecl_FK5 type
     */
    public static final COOSYSSystemType ECL_FK5 = new COOSYSSystemType(ECL_FK5_TYPE, "ecl_FK5");

    /**
     * The galactic type
     */
    public static final int GALACTIC_TYPE = 5;

    /**
     * The instance of the galactic type
     */
    public static final COOSYSSystemType GALACTIC = new COOSYSSystemType(GALACTIC_TYPE, "galactic");

    /**
     * The supergalactic type
     */
    public static final int SUPERGALACTIC_TYPE = 6;

    /**
     * The instance of the supergalactic type
     */
    public static final COOSYSSystemType SUPERGALACTIC = new COOSYSSystemType(SUPERGALACTIC_TYPE, "supergalactic");

    /**
     * The xy type
     */
    public static final int XY_TYPE = 7;

    /**
     * The instance of the xy type
     */
    public static final COOSYSSystemType XY = new COOSYSSystemType(XY_TYPE, "xy");

    /**
     * The barycentric type
     */
    public static final int BARYCENTRIC_TYPE = 8;

    /**
     * The instance of the barycentric type
     */
    public static final COOSYSSystemType BARYCENTRIC = new COOSYSSystemType(BARYCENTRIC_TYPE, "barycentric");

    /**
     * The geo_app type
     */
    public static final int GEO_APP_TYPE = 9;

    /**
     * The instance of the geo_app type
     */
    public static final COOSYSSystemType GEO_APP = new COOSYSSystemType(GEO_APP_TYPE, "geo_app");

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

    private COOSYSSystemType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.COOSYSSystemType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of COOSYSSystemType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this COOSYSSystemType
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
        members.put("eq_FK4", EQ_FK4);
        members.put("eq_FK5", EQ_FK5);
        members.put("ICRS", ICRS);
        members.put("ecl_FK4", ECL_FK4);
        members.put("ecl_FK5", ECL_FK5);
        members.put("galactic", GALACTIC);
        members.put("supergalactic", SUPERGALACTIC);
        members.put("xy", XY);
        members.put("barycentric", BARYCENTRIC);
        members.put("geo_app", GEO_APP);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * COOSYSSystemType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new COOSYSSystemType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.COOSYSSystemType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid COOSYSSystemType";
            throw new IllegalArgumentException(err);
        }
        return (COOSYSSystemType) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.COOSYSSystemType valueOf(java.lang.String) 

}
