/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ExecutionPhase.java,v 1.17 2004/04/05 15:18:00 nw Exp $
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
 * Enumeration of possible phases of job execution
 * 
 * @version $Revision: 1.17 $ $Date: 2004/04/05 15:18:00 $
 */
public class ExecutionPhase implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The PENDING type
     */
    public static final int PENDING_TYPE = 0;

    /**
     * The instance of the PENDING type
     */
    public static final ExecutionPhase PENDING = new ExecutionPhase(PENDING_TYPE, "PENDING");

    /**
     * The INITIALIZING type
     */
    public static final int INITIALIZING_TYPE = 1;

    /**
     * The instance of the INITIALIZING type
     */
    public static final ExecutionPhase INITIALIZING = new ExecutionPhase(INITIALIZING_TYPE, "INITIALIZING");

    /**
     * The RUNNING type
     */
    public static final int RUNNING_TYPE = 2;

    /**
     * The instance of the RUNNING type
     */
    public static final ExecutionPhase RUNNING = new ExecutionPhase(RUNNING_TYPE, "RUNNING");

    /**
     * The COMPLETED type
     */
    public static final int COMPLETED_TYPE = 3;

    /**
     * The instance of the COMPLETED type
     */
    public static final ExecutionPhase COMPLETED = new ExecutionPhase(COMPLETED_TYPE, "COMPLETED");

    /**
     * The ERROR type
     */
    public static final int ERROR_TYPE = 4;

    /**
     * The instance of the ERROR type
     */
    public static final ExecutionPhase ERROR = new ExecutionPhase(ERROR_TYPE, "ERROR");

    /**
     * The UNKNOWN type
     */
    public static final int UNKNOWN_TYPE = 5;

    /**
     * The instance of the UNKNOWN type
     */
    public static final ExecutionPhase UNKNOWN = new ExecutionPhase(UNKNOWN_TYPE, "UNKNOWN");

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

    private ExecutionPhase(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ExecutionPhase
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ExecutionPhase
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
        members.put("PENDING", PENDING);
        members.put("INITIALIZING", INITIALIZING);
        members.put("RUNNING", RUNNING);
        members.put("COMPLETED", COMPLETED);
        members.put("ERROR", ERROR);
        members.put("UNKNOWN", UNKNOWN);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ExecutionPhase
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ExecutionPhase based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ExecutionPhase";
            throw new IllegalArgumentException(err);
        }
        return (ExecutionPhase) obj;
    } //-- org.astrogrid.applications.beans.v1.cea.castor.types.ExecutionPhase valueOf(java.lang.String) 

}
