/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AngleUnitType.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class AngleUnitType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class AngleUnitType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The deg type
     */
    public static final int DEG_TYPE = 0;

    /**
     * The instance of the deg type
     */
    public static final AngleUnitType DEG = new AngleUnitType(DEG_TYPE, "deg");

    /**
     * The rad type
     */
    public static final int RAD_TYPE = 1;

    /**
     * The instance of the rad type
     */
    public static final AngleUnitType RAD = new AngleUnitType(RAD_TYPE, "rad");

    /**
     * The h type
     */
    public static final int H_TYPE = 2;

    /**
     * The instance of the h type
     */
    public static final AngleUnitType H = new AngleUnitType(H_TYPE, "h");

    /**
     * The arcmin type
     */
    public static final int ARCMIN_TYPE = 3;

    /**
     * The instance of the arcmin type
     */
    public static final AngleUnitType ARCMIN = new AngleUnitType(ARCMIN_TYPE, "arcmin");

    /**
     * The arcsec type
     */
    public static final int ARCSEC_TYPE = 4;

    /**
     * The instance of the arcsec type
     */
    public static final AngleUnitType ARCSEC = new AngleUnitType(ARCSEC_TYPE, "arcsec");

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

    private AngleUnitType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AngleUnitType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AngleUnitType
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
        members.put("deg", DEG);
        members.put("rad", RAD);
        members.put("h", H);
        members.put("arcmin", ARCMIN);
        members.put("arcsec", ARCSEC);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AngleUnitType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AngleUnitType based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AngleUnitType";
            throw new IllegalArgumentException(err);
        }
        return (AngleUnitType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.AngleUnitType valueOf(java.lang.String) 

}
