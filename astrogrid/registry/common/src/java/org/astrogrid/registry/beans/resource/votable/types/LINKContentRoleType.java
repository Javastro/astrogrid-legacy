/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: LINKContentRoleType.java,v 1.3 2004/03/05 09:52:02 KevinBenson Exp $
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
 * Class LINKContentRoleType.
 * 
 * @version $Revision: 1.3 $ $Date: 2004/03/05 09:52:02 $
 */
public class LINKContentRoleType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The query type
     */
    public static final int QUERY_TYPE = 0;

    /**
     * The instance of the query type
     */
    public static final LINKContentRoleType QUERY = new LINKContentRoleType(QUERY_TYPE, "query");

    /**
     * The hints type
     */
    public static final int HINTS_TYPE = 1;

    /**
     * The instance of the hints type
     */
    public static final LINKContentRoleType HINTS = new LINKContentRoleType(HINTS_TYPE, "hints");

    /**
     * The doc type
     */
    public static final int DOC_TYPE = 2;

    /**
     * The instance of the doc type
     */
    public static final LINKContentRoleType DOC = new LINKContentRoleType(DOC_TYPE, "doc");

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

    private LINKContentRoleType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of LINKContentRoleType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this LINKContentRoleType
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
        members.put("query", QUERY);
        members.put("hints", HINTS);
        members.put("doc", DOC);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * LINKContentRoleType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new LINKContentRoleType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LINKContentRoleType";
            throw new IllegalArgumentException(err);
        }
        return (LINKContentRoleType) obj;
    } //-- org.astrogrid.registry.beans.resource.votable.types.LINKContentRoleType valueOf(java.lang.String) 

}
