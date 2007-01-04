/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ScriptingLanguage.java,v 1.8 2007/01/04 16:26:23 clq2 Exp $
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
 * Scripting language
 * 
 * @version $Revision: 1.8 $ $Date: 2007/01/04 16:26:23 $
 */
public class ScriptingLanguage implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The xslt type
     */
    public static final int XSLT_TYPE = 0;

    /**
     * The instance of the xslt type
     */
    public static final ScriptingLanguage XSLT = new ScriptingLanguage(XSLT_TYPE, "xslt");

    /**
     * The javascript type
     */
    public static final int JAVASCRIPT_TYPE = 1;

    /**
     * The instance of the javascript type
     */
    public static final ScriptingLanguage JAVASCRIPT = new ScriptingLanguage(JAVASCRIPT_TYPE, "javascript");

    /**
     * The groovy type
     */
    public static final int GROOVY_TYPE = 2;

    /**
     * The instance of the groovy type
     */
    public static final ScriptingLanguage GROOVY = new ScriptingLanguage(GROOVY_TYPE, "groovy");

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

    private ScriptingLanguage(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.types.ScriptingLanguage(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ScriptingLanguage
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ScriptingLanguage
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
        members.put("xslt", XSLT);
        members.put("javascript", JAVASCRIPT);
        members.put("groovy", GROOVY);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ScriptingLanguage
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ScriptingLanguage based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.types.ScriptingLanguage valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ScriptingLanguage";
            throw new IllegalArgumentException(err);
        }
        return (ScriptingLanguage) obj;
    } //-- org.astrogrid.applications.beans.v1.types.ScriptingLanguage valueOf(java.lang.String) 

}
