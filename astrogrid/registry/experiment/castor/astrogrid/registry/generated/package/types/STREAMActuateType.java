/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: STREAMActuateType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class STREAMActuateType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class STREAMActuateType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The onLoad type
     */
    public static final int ONLOAD_TYPE = 0;

    /**
     * The instance of the onLoad type
     */
    public static final STREAMActuateType ONLOAD = new STREAMActuateType(ONLOAD_TYPE, "onLoad");

    /**
     * The onRequest type
     */
    public static final int ONREQUEST_TYPE = 1;

    /**
     * The instance of the onRequest type
     */
    public static final STREAMActuateType ONREQUEST = new STREAMActuateType(ONREQUEST_TYPE, "onRequest");

    /**
     * The other type
     */
    public static final int OTHER_TYPE = 2;

    /**
     * The instance of the other type
     */
    public static final STREAMActuateType OTHER = new STREAMActuateType(OTHER_TYPE, "other");

    /**
     * The none type
     */
    public static final int NONE_TYPE = 3;

    /**
     * The instance of the none type
     */
    public static final STREAMActuateType NONE = new STREAMActuateType(NONE_TYPE, "none");

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

    private STREAMActuateType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.generated.package.types.STREAMActuateType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of STREAMActuateType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this STREAMActuateType
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
        members.put("onLoad", ONLOAD);
        members.put("onRequest", ONREQUEST);
        members.put("other", OTHER);
        members.put("none", NONE);
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
     * STREAMActuateType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new STREAMActuateType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.generated.package.types.STREAMActuateType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid STREAMActuateType";
            throw new IllegalArgumentException(err);
        }
        return (STREAMActuateType) obj;
    } //-- org.astrogrid.registry.generated.package.types.STREAMActuateType valueOf(java.lang.String) 

}
