/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: AllOrDistinct.java,v 1.6 2003/11/21 17:30:19 nw Exp $
 */

package org.astrogrid.datacenter.adql.generated.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class AllOrDistinct.
 * 
 * @version $Revision: 1.6 $ $Date: 2003/11/21 17:30:19 $
 */
public class AllOrDistinct implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The ALL type
     */
    public static final int ALL_TYPE = 0;

    /**
     * The instance of the ALL type
     */
    public static final AllOrDistinct ALL = new AllOrDistinct(ALL_TYPE, "ALL");

    /**
     * The DISTINCT type
     */
    public static final int DISTINCT_TYPE = 1;

    /**
     * The instance of the DISTINCT type
     */
    public static final AllOrDistinct DISTINCT = new AllOrDistinct(DISTINCT_TYPE, "DISTINCT");

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

    private AllOrDistinct(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.datacenter.adql.generated.types.AllOrDistinct(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of AllOrDistinct
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this AllOrDistinct
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
        members.put("ALL", ALL);
        members.put("DISTINCT", DISTINCT);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * AllOrDistinct
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new AllOrDistinct based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.datacenter.adql.generated.types.AllOrDistinct valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid AllOrDistinct";
            throw new IllegalArgumentException(err);
        }
        return (AllOrDistinct) obj;
    } //-- org.astrogrid.datacenter.adql.generated.types.AllOrDistinct valueOf(java.lang.String) 

}
