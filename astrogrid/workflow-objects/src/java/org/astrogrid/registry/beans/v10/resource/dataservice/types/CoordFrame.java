/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: CoordFrame.java,v 1.2 2007/01/04 16:26:19 clq2 Exp $
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
 * Class CoordFrame.
 * 
 * @version $Revision: 1.2 $ $Date: 2007/01/04 16:26:19 $
 */
public class CoordFrame implements java.io.Serializable {


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
    public static final CoordFrame ICRS = new CoordFrame(ICRS_TYPE, "ICRS");

    /**
     * The FK5 type
     */
    public static final int FK5_TYPE = 1;

    /**
     * The instance of the FK5 type
     */
    public static final CoordFrame FK5 = new CoordFrame(FK5_TYPE, "FK5");

    /**
     * The FK4 type
     */
    public static final int FK4_TYPE = 2;

    /**
     * The instance of the FK4 type
     */
    public static final CoordFrame FK4 = new CoordFrame(FK4_TYPE, "FK4");

    /**
     * The ECL type
     */
    public static final int ECL_TYPE = 3;

    /**
     * The instance of the ECL type
     */
    public static final CoordFrame ECL = new CoordFrame(ECL_TYPE, "ECL");

    /**
     * The GAL type
     */
    public static final int GAL_TYPE = 4;

    /**
     * The instance of the GAL type
     */
    public static final CoordFrame GAL = new CoordFrame(GAL_TYPE, "GAL");

    /**
     * The SGAL type
     */
    public static final int SGAL_TYPE = 5;

    /**
     * The instance of the SGAL type
     */
    public static final CoordFrame SGAL = new CoordFrame(SGAL_TYPE, "SGAL");

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

    private CoordFrame(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of CoordFrame
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this CoordFrame
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
     * Method toStringReturns the String representation of this
     * CoordFrame
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new CoordFrame based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid CoordFrame";
            throw new IllegalArgumentException(err);
        }
        return (CoordFrame) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.dataservice.types.CoordFrame valueOf(java.lang.String) 

}
