/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: InvocationType.java,v 1.8 2004/04/05 14:36:12 KevinBenson Exp $
 */

package org.astrogrid.registry.beans.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class InvocationType.
 * 
 * @version $Revision: 1.8 $ $Date: 2004/04/05 14:36:12 $
 */
public class InvocationType implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Custom type
     */
    public static final int CUSTOM_TYPE = 0;

    /**
     * The instance of the Custom type
     */
    public static final InvocationType CUSTOM = new InvocationType(CUSTOM_TYPE, "Custom");

    /**
     * The Extended type
     */
    public static final int EXTENDED_TYPE = 1;

    /**
     * The instance of the Extended type
     */
    public static final InvocationType EXTENDED = new InvocationType(EXTENDED_TYPE, "Extended");

    /**
     * The WebService type
     */
    public static final int WEBSERVICE_TYPE = 2;

    /**
     * The instance of the WebService type
     */
    public static final InvocationType WEBSERVICE = new InvocationType(WEBSERVICE_TYPE, "WebService");

    /**
     * The WebBrowser type
     */
    public static final int WEBBROWSER_TYPE = 3;

    /**
     * The instance of the WebBrowser type
     */
    public static final InvocationType WEBBROWSER = new InvocationType(WEBBROWSER_TYPE, "WebBrowser");

    /**
     * The GLUService type
     */
    public static final int GLUSERVICE_TYPE = 4;

    /**
     * The instance of the GLUService type
     */
    public static final InvocationType GLUSERVICE = new InvocationType(GLUSERVICE_TYPE, "GLUService");

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

    private InvocationType(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.resource.types.InvocationType(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of InvocationType
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this InvocationType
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
        members.put("Custom", CUSTOM);
        members.put("Extended", EXTENDED);
        members.put("WebService", WEBSERVICE);
        members.put("WebBrowser", WEBBROWSER);
        members.put("GLUService", GLUSERVICE);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * InvocationType
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new InvocationType based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.resource.types.InvocationType valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid InvocationType";
            throw new IllegalArgumentException(err);
        }
        return (InvocationType) obj;
    } //-- org.astrogrid.registry.beans.resource.types.InvocationType valueOf(java.lang.String) 

}
