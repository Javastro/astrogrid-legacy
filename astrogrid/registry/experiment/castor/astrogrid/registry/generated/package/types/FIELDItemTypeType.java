/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.5.3</a>, using an XML
 * Schema.
 * $Id: FIELDItemTypeType.java,v 1.1 2004/03/03 03:40:58 pcontrer Exp $
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
 * Class FIELDItemTypeType.
 * 
 * @version $Revision: 1.1 $ $Date: 2004/03/03 03:40:58 $
 */
public class FIELDItemTypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The hidden type
     */
    public static final int HIDDEN_TYPE = 0;

    /**
     * The instance of the hidden type
     */
    public static final FIELDItemTypeType HIDDEN = new FIELDItemTypeType(HIDDEN_TYPE, "hidden");

    /**
     * The no_query type
     */
    public static final int NO_QUERY_TYPE = 1;

    /**
     * The instance of the no_query type
     */
    public static final FIELDItemTypeType NO_QUERY = new FIELDItemTypeType(NO_QUERY_TYPE, "no_query");

    /**
     * The trigger type
     */
    public static final int TRIGGER_TYPE = 2;

    /**
     * The instance of the trigger type
     */
    public static final FIELDItemTypeType TRIGGER = new FIELDItemTypeType(TRIGGER_TYPE, "trigger");

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

    private FIELDItemTypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.generated.package.types.FIELDItemTypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of FIELDItemTypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this FIELDItemTypeType
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
        members.put("hidden", HIDDEN);
        members.put("no_query", NO_QUERY);
        members.put("trigger", TRIGGER);
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
     * FIELDItemTypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new FIELDItemTypeType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.generated.package.types.FIELDItemTypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid FIELDItemTypeType";
            throw new IllegalArgumentException(err);
        }
        return (FIELDItemTypeType) obj;
    } //-- org.astrogrid.registry.generated.package.types.FIELDItemTypeType valueOf(java.lang.String) 

}
