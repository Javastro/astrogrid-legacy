/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ApplicationKindType.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
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
 * The type of the underlying application - commandline, http-get
 * etc
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:00 $
 */
public class ApplicationKindType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The cmdline type
     */
    public static final int CMDLINE_TYPE = 0;

    /**
     * The instance of the cmdline type
     */
    public static final ApplicationKindType CMDLINE = new ApplicationKindType(CMDLINE_TYPE, "cmdline");

    /**
     * The http type
     */
    public static final int HTTP_TYPE = 1;

    /**
     * The instance of the http type
     */
    public static final ApplicationKindType HTTP = new ApplicationKindType(HTTP_TYPE, "http");

    /**
     * The javaclass type
     */
    public static final int JAVACLASS_TYPE = 2;

    /**
     * The instance of the javaclass type
     */
    public static final ApplicationKindType JAVACLASS = new ApplicationKindType(JAVACLASS_TYPE, "javaclass");

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

    private ApplicationKindType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.types.ApplicationKindType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ApplicationKindType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ApplicationKindType
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
        members.put("cmdline", CMDLINE);
        members.put("http", HTTP);
        members.put("javaclass", JAVACLASS);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ApplicationKindType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ApplicationKindType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.types.ApplicationKindType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ApplicationKindType";
            throw new IllegalArgumentException(err);
        }
        return (ApplicationKindType) obj;
    } //-- org.astrogrid.applications.beans.v1.types.ApplicationKindType valueOf(java.lang.String) 

}
