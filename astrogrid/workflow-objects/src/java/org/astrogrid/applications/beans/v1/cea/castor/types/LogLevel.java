/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: LogLevel.java,v 1.14 2004/03/15 16:53:03 pah Exp $
 */

package org.astrogrid.applications.beans.v1.cea.castor.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Enumeration of possible message log levels
 * 
 * @version $Revision: 1.14 $ $Date: 2004/03/15 16:53:03 $
 */
public class LogLevel implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The info type
     */
    public static final int INFO_TYPE = 0;

    /**
     * The instance of the info type
     */
    public static final LogLevel INFO = new LogLevel(INFO_TYPE, "info");

    /**
     * The warn type
     */
    public static final int WARN_TYPE = 1;

    /**
     * The instance of the warn type
     */
    public static final LogLevel WARN = new LogLevel(WARN_TYPE, "warn");

    /**
     * The error type
     */
    public static final int ERROR_TYPE = 2;

    /**
     * The instance of the error type
     */
    public static final LogLevel ERROR = new LogLevel(ERROR_TYPE, "error");

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

    private LogLevel(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of LogLevel
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this LogLevel
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
        members.put("info", INFO);
        members.put("warn", WARN);
        members.put("error", ERROR);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * LogLevel
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new LogLevel based on the given
     * String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid LogLevel";
            throw new IllegalArgumentException(err);
        }
        return (LogLevel) obj;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.LogLevel valueOf(java.lang.String) 

}
