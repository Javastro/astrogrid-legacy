/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: ParameterTypes.java,v 1.36 2007/01/04 16:26:36 clq2 Exp $
 */

package org.astrogrid.applications.beans.v1.parameters.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * The list of possible parameter typesThe enumeration values
 * should have the namespace appended? I have removed them to make
 * castor generate nicer objects
 * 
 * @version $Revision: 1.36 $ $Date: 2007/01/04 16:26:36 $
 */
public class ParameterTypes implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The integer type
     */
    public static final int INTEGER_TYPE = 0;

    /**
     * The instance of the integer type
     */
    public static final ParameterTypes INTEGER = new ParameterTypes(INTEGER_TYPE, "integer");

    /**
     * The real type
     */
    public static final int REAL_TYPE = 1;

    /**
     * The instance of the real type
     */
    public static final ParameterTypes REAL = new ParameterTypes(REAL_TYPE, "real");

    /**
     * The complex type
     */
    public static final int COMPLEX_TYPE = 2;

    /**
     * The instance of the complex type
     */
    public static final ParameterTypes COMPLEX = new ParameterTypes(COMPLEX_TYPE, "complex");

    /**
     * The double type
     */
    public static final int DOUBLE_TYPE = 3;

    /**
     * The instance of the double type
     */
    public static final ParameterTypes DOUBLE = new ParameterTypes(DOUBLE_TYPE, "double");

    /**
     * The text type
     */
    public static final int TEXT_TYPE = 4;

    /**
     * The instance of the text type
     */
    public static final ParameterTypes TEXT = new ParameterTypes(TEXT_TYPE, "text");

    /**
     * The boolean type
     */
    public static final int BOOLEAN_TYPE = 5;

    /**
     * The instance of the boolean type
     */
    public static final ParameterTypes BOOLEAN = new ParameterTypes(BOOLEAN_TYPE, "boolean");

    /**
     * The anyURI type
     */
    public static final int ANYURI_TYPE = 6;

    /**
     * The instance of the anyURI type
     */
    public static final ParameterTypes ANYURI = new ParameterTypes(ANYURI_TYPE, "anyURI");

    /**
     * The anyXML type
     */
    public static final int ANYXML_TYPE = 7;

    /**
     * The instance of the anyXML type
     */
    public static final ParameterTypes ANYXML = new ParameterTypes(ANYXML_TYPE, "anyXML");

    /**
     * The VOTable type
     */
    public static final int VOTABLE_TYPE = 8;

    /**
     * The instance of the VOTable type
     */
    public static final ParameterTypes VOTABLE = new ParameterTypes(VOTABLE_TYPE, "VOTable");

    /**
     * The RA type
     */
    public static final int RA_TYPE = 9;

    /**
     * The instance of the RA type
     */
    public static final ParameterTypes RA = new ParameterTypes(RA_TYPE, "RA");

    /**
     * The Dec type
     */
    public static final int DEC_TYPE = 10;

    /**
     * The instance of the Dec type
     */
    public static final ParameterTypes DEC = new ParameterTypes(DEC_TYPE, "Dec");

    /**
     * The ADQL type
     */
    public static final int ADQL_TYPE = 11;

    /**
     * The instance of the ADQL type
     */
    public static final ParameterTypes ADQL = new ParameterTypes(ADQL_TYPE, "ADQL");

    /**
     * The binary type
     */
    public static final int BINARY_TYPE = 12;

    /**
     * The instance of the binary type
     */
    public static final ParameterTypes BINARY = new ParameterTypes(BINARY_TYPE, "binary");

    /**
     * The FITS type
     */
    public static final int FITS_TYPE = 13;

    /**
     * The instance of the FITS type
     */
    public static final ParameterTypes FITS = new ParameterTypes(FITS_TYPE, "FITS");

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

    private ParameterTypes(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of ParameterTypes
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this ParameterTypes
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
        members.put("integer", INTEGER);
        members.put("real", REAL);
        members.put("complex", COMPLEX);
        members.put("double", DOUBLE);
        members.put("text", TEXT);
        members.put("boolean", BOOLEAN);
        members.put("anyURI", ANYURI);
        members.put("anyXML", ANYXML);
        members.put("VOTable", VOTABLE);
        members.put("RA", RA);
        members.put("Dec", DEC);
        members.put("ADQL", ADQL);
        members.put("binary", BINARY);
        members.put("FITS", FITS);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this
     * ParameterTypes
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new ParameterTypes based on the
     * given String value.
     * 
     * @param string
     */
    public static org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid ParameterTypes";
            throw new IllegalArgumentException(err);
        }
        return (ParameterTypes) obj;
    } //-- org.astrogrid.applications.beans.v1.parameters.types.ParameterTypes valueOf(java.lang.String) 

}
