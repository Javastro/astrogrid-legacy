/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: RESOURCETypeType.java,v 1.8 2004/04/05 14:36:12 KevinBenson Exp $
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
 * Class RESOURCETypeType.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:12 $
 */
public class RESOURCETypeType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The results type
     */
    public static final int RESULTS_TYPE = 0;

    /**
     * The instance of the results type
     */
    public static final RESOURCETypeType RESULTS = new RESOURCETypeType(RESULTS_TYPE, "results");

    /**
     * The meta type
     */
    public static final int META_TYPE = 1;

    /**
     * The instance of the meta type
     */
    public static final RESOURCETypeType META = new RESOURCETypeType(META_TYPE, "meta");

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

    private RESOURCETypeType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of RESOURCETypeType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this RESOURCETypeType
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
        members.put("results", RESULTS);
        members.put("meta", META);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * RESOURCETypeType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new RESOURCETypeType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid RESOURCETypeType";
            throw new IllegalArgumentException(err);
        }
        return (RESOURCETypeType) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.RESOURCETypeType valueOf(java.lang.String) 

}
