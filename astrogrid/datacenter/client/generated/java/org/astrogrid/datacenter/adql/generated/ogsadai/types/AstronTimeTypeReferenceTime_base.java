/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AstronTimeTypeReferenceTime_base.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class AstronTimeTypeReferenceTime_base.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class AstronTimeTypeReferenceTime_base implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ISO8601 type
     */
    public static final int ISO8601_TYPE = 0;

    /**
     * The instance of the ISO8601 type
     */
    public static final AstronTimeTypeReferenceTime_base ISO8601 = new AstronTimeTypeReferenceTime_base(ISO8601_TYPE, "ISO8601");

    /**
     * The JD type
     */
    public static final int JD_TYPE = 1;

    /**
     * The instance of the JD type
     */
    public static final AstronTimeTypeReferenceTime_base JD = new AstronTimeTypeReferenceTime_base(JD_TYPE, "JD");

    /**
     * The MJD type
     */
    public static final int MJD_TYPE = 2;

    /**
     * The instance of the MJD type
     */
    public static final AstronTimeTypeReferenceTime_base MJD = new AstronTimeTypeReferenceTime_base(MJD_TYPE, "MJD");

    /**
     * The relative type
     */
    public static final int RELATIVE_TYPE = 3;

    /**
     * The instance of the relative type
     */
    public static final AstronTimeTypeReferenceTime_base RELATIVE = new AstronTimeTypeReferenceTime_base(RELATIVE_TYPE, "relative");

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

    private AstronTimeTypeReferenceTime_base(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AstronTimeTypeReferenceTime_base
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this
     * AstronTimeTypeReferenceTime_base
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
        members.put("ISO8601", ISO8601);
        members.put("JD", JD);
        members.put("MJD", MJD);
        members.put("relative", RELATIVE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AstronTimeTypeReferenceTime_base
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AstronTimeTypeReferenceTime_base
     * based on the given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AstronTimeTypeReferenceTime_base";
            throw new IllegalArgumentException(err);
        }
        return (AstronTimeTypeReferenceTime_base) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AstronTimeTypeReferenceTime_base valueOf(java.lang.String) 

}
