/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterTypes.java,v 1.1 2004/02/20 18:36:39 nw Exp $
 */

package org.astrogrid.applications.beans.v1.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The list of possible parameter types
 * 
 * @version $Revision: 1.1 $ $Date: 2004/02/20 18:36:39 $
 */
public class ParameterTypes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The xs:integer type
     */
    public static final int VALUE_0_TYPE = 0;

    /**
     * The instance of the xs:integer type
     */
    public static final ParameterTypes VALUE_0 = new ParameterTypes(VALUE_0_TYPE, "xs:integer");

    /**
     * The xs:real type
     */
    public static final int VALUE_1_TYPE = 1;

    /**
     * The instance of the xs:real type
     */
    public static final ParameterTypes VALUE_1 = new ParameterTypes(VALUE_1_TYPE, "xs:real");

    /**
     * The agpd:MySpace_FileReference type
     */
    public static final int VALUE_2_TYPE = 2;

    /**
     * The instance of the agpd:MySpace_FileReference type
     */
    public static final ParameterTypes VALUE_2 = new ParameterTypes(VALUE_2_TYPE, "agpd:MySpace_FileReference");

    /**
     * The agpd:FileReference type
     */
    public static final int VALUE_3_TYPE = 3;

    /**
     * The instance of the agpd:FileReference type
     */
    public static final ParameterTypes VALUE_3 = new ParameterTypes(VALUE_3_TYPE, "agpd:FileReference");

    /**
     * The agpd:MySpace_VOTableReference type
     */
    public static final int VALUE_4_TYPE = 4;

    /**
     * The instance of the agpd:MySpace_VOTableReference type
     */
    public static final ParameterTypes VALUE_4 = new ParameterTypes(VALUE_4_TYPE, "agpd:MySpace_VOTableReference");

    /**
     * The xs:double type
     */
    public static final int VALUE_5_TYPE = 5;

    /**
     * The instance of the xs:double type
     */
    public static final ParameterTypes VALUE_5 = new ParameterTypes(VALUE_5_TYPE, "xs:double");

    /**
     * The xs:string type
     */
    public static final int VALUE_6_TYPE = 6;

    /**
     * The instance of the xs:string type
     */
    public static final ParameterTypes VALUE_6 = new ParameterTypes(VALUE_6_TYPE, "xs:string");

    /**
     * The agpd:RA type
     */
    public static final int VALUE_7_TYPE = 7;

    /**
     * The instance of the agpd:RA type
     */
    public static final ParameterTypes VALUE_7 = new ParameterTypes(VALUE_7_TYPE, "agpd:RA");

    /**
     * The agpd:Dec type
     */
    public static final int VALUE_8_TYPE = 8;

    /**
     * The instance of the agpd:Dec type
     */
    public static final ParameterTypes VALUE_8 = new ParameterTypes(VALUE_8_TYPE, "agpd:Dec");

    /**
     * The agpd:ADQL type
     */
    public static final int VALUE_9_TYPE = 9;

    /**
     * The instance of the agpd:ADQL type
     */
    public static final ParameterTypes VALUE_9 = new ParameterTypes(VALUE_9_TYPE, "agpd:ADQL");

    /**
     * The xs:boolean type
     */
    public static final int VALUE_10_TYPE = 10;

    /**
     * The instance of the xs:boolean type
     */
    public static final ParameterTypes VALUE_10 = new ParameterTypes(VALUE_10_TYPE, "xs:boolean");

    /**
     * The xs:anyURI type
     */
    public static final int VALUE_11_TYPE = 11;

    /**
     * The instance of the xs:anyURI type
     */
    public static final ParameterTypes VALUE_11 = new ParameterTypes(VALUE_11_TYPE, "xs:anyURI");

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

    private ParameterTypes(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.types.ParameterTypes(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ParameterTypes
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ParameterTypes
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
        members.put("xs:integer", VALUE_0);
        members.put("xs:real", VALUE_1);
        members.put("agpd:MySpace_FileReference", VALUE_2);
        members.put("agpd:FileReference", VALUE_3);
        members.put("agpd:MySpace_VOTableReference", VALUE_4);
        members.put("xs:double", VALUE_5);
        members.put("xs:string", VALUE_6);
        members.put("agpd:RA", VALUE_7);
        members.put("agpd:Dec", VALUE_8);
        members.put("agpd:ADQL", VALUE_9);
        members.put("xs:boolean", VALUE_10);
        members.put("xs:anyURI", VALUE_11);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ParameterTypes
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ParameterTypes based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.types.ParameterTypes valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ParameterTypes";
            throw new IllegalArgumentException(err);
        }
        return (ParameterTypes) obj;
    } //-- org.astrogrid.applications.beans.v1.types.ParameterTypes valueOf(java.lang.String) 

}
