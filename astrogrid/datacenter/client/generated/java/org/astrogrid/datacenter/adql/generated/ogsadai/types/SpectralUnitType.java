/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: SpectralUnitType.java,v 1.2 2004/03/26 16:03:34 eca Exp $
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
 * Class SpectralUnitType.
 * 
 * @version $Revision: 1.2 $ $Date: 2004/03/26 16:03:34 $
 */
public class SpectralUnitType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Hz type
     */
    public static final int HZ_TYPE = 0;

    /**
     * The instance of the Hz type
     */
    public static final SpectralUnitType HZ = new SpectralUnitType(HZ_TYPE, "Hz");

    /**
     * The kHz type
     */
    public static final int KHZ_TYPE = 1;

    /**
     * The instance of the kHz type
     */
    public static final SpectralUnitType KHZ = new SpectralUnitType(KHZ_TYPE, "kHz");

    /**
     * The MHz type
     */
    public static final int MHZ_TYPE = 2;

    /**
     * The instance of the MHz type
     */
    public static final SpectralUnitType MHZ = new SpectralUnitType(MHZ_TYPE, "MHz");

    /**
     * The GHz type
     */
    public static final int GHZ_TYPE = 3;

    /**
     * The instance of the GHz type
     */
    public static final SpectralUnitType GHZ = new SpectralUnitType(GHZ_TYPE, "GHz");

    /**
     * The m type
     */
    public static final int M_TYPE = 4;

    /**
     * The instance of the m type
     */
    public static final SpectralUnitType M = new SpectralUnitType(M_TYPE, "m");

    /**
     * The mm type
     */
    public static final int MM_TYPE = 5;

    /**
     * The instance of the mm type
     */
    public static final SpectralUnitType MM = new SpectralUnitType(MM_TYPE, "mm");

    /**
     * The micron type
     */
    public static final int MICRON_TYPE = 6;

    /**
     * The instance of the micron type
     */
    public static final SpectralUnitType MICRON = new SpectralUnitType(MICRON_TYPE, "micron");

    /**
     * The nm type
     */
    public static final int NM_TYPE = 7;

    /**
     * The instance of the nm type
     */
    public static final SpectralUnitType NM = new SpectralUnitType(NM_TYPE, "nm");

    /**
     * The A type
     */
    public static final int A_TYPE = 8;

    /**
     * The instance of the A type
     */
    public static final SpectralUnitType A = new SpectralUnitType(A_TYPE, "A");

    /**
     * The eV type
     */
    public static final int EV_TYPE = 9;

    /**
     * The instance of the eV type
     */
    public static final SpectralUnitType EV = new SpectralUnitType(EV_TYPE, "eV");

    /**
     * The keV type
     */
    public static final int KEV_TYPE = 10;

    /**
     * The instance of the keV type
     */
    public static final SpectralUnitType KEV = new SpectralUnitType(KEV_TYPE, "keV");

    /**
     * The MeV type
     */
    public static final int MEV_TYPE = 11;

    /**
     * The instance of the MeV type
     */
    public static final SpectralUnitType MEV = new SpectralUnitType(MEV_TYPE, "MeV");

    /**
     * The GeV type
     */
    public static final int GEV_TYPE = 12;

    /**
     * The instance of the GeV type
     */
    public static final SpectralUnitType GEV = new SpectralUnitType(GEV_TYPE, "GeV");

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

    private SpectralUnitType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of SpectralUnitType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this SpectralUnitType
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
        members.put("Hz", HZ);
        members.put("kHz", KHZ);
        members.put("MHz", MHZ);
        members.put("GHz", GHZ);
        members.put("m", M);
        members.put("mm", MM);
        members.put("micron", MICRON);
        members.put("nm", NM);
        members.put("A", A);
        members.put("eV", EV);
        members.put("keV", KEV);
        members.put("MeV", MEV);
        members.put("GeV", GEV);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * SpectralUnitType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new SpectralUnitType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid SpectralUnitType";
            throw new IllegalArgumentException(err);
        }
        return (SpectralUnitType) obj;
    } //-- org.astrogrid.datacenter.adql.generated.ogsadai.types.SpectralUnitType valueOf(java.lang.String) 

}
