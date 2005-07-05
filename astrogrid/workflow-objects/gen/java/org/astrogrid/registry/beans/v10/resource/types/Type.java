/*
 * This class was automatically generated with 
 * <a href="http://www.castor.org">Castor 0.9.4.3</a>, using an XML
 * Schema.
 * $Id: Type.java,v 1.2 2005/07/05 08:27:00 clq2 Exp $
 */

package org.astrogrid.registry.beans.v10.resource.types;

  //---------------------------------/
 //- Imported classes and packages -/
//---------------------------------/

import java.io.Serializable;
import java.util.Enumeration;
import java.util.Hashtable;
import org.exolab.castor.xml.Marshaller;
import org.exolab.castor.xml.Unmarshaller;

/**
 * Class Type.
 * 
 * @version $Revision: 1.2 $ $Date: 2005/07/05 08:27:00 $
 */
public class Type implements java.io.Serializable {


      //--------------------------/
     //- Class/Member Variables -/
    //--------------------------/

    /**
     * The Other type
     */
    public static final int OTHER_TYPE = 0;

    /**
     * The instance of the Other type
     */
    public static final Type OTHER = new Type(OTHER_TYPE, "Other");

    /**
     * The Archive type
     */
    public static final int ARCHIVE_TYPE = 1;

    /**
     * The instance of the Archive type
     */
    public static final Type ARCHIVE = new Type(ARCHIVE_TYPE, "Archive");

    /**
     * The Bibliography type
     */
    public static final int BIBLIOGRAPHY_TYPE = 2;

    /**
     * The instance of the Bibliography type
     */
    public static final Type BIBLIOGRAPHY = new Type(BIBLIOGRAPHY_TYPE, "Bibliography");

    /**
     * The Catalog type
     */
    public static final int CATALOG_TYPE = 3;

    /**
     * The instance of the Catalog type
     */
    public static final Type CATALOG = new Type(CATALOG_TYPE, "Catalog");

    /**
     * The Journal type
     */
    public static final int JOURNAL_TYPE = 4;

    /**
     * The instance of the Journal type
     */
    public static final Type JOURNAL = new Type(JOURNAL_TYPE, "Journal");

    /**
     * The Library type
     */
    public static final int LIBRARY_TYPE = 5;

    /**
     * The instance of the Library type
     */
    public static final Type LIBRARY = new Type(LIBRARY_TYPE, "Library");

    /**
     * The Simulation type
     */
    public static final int SIMULATION_TYPE = 6;

    /**
     * The instance of the Simulation type
     */
    public static final Type SIMULATION = new Type(SIMULATION_TYPE, "Simulation");

    /**
     * The Survey type
     */
    public static final int SURVEY_TYPE = 7;

    /**
     * The instance of the Survey type
     */
    public static final Type SURVEY = new Type(SURVEY_TYPE, "Survey");

    /**
     * The Transformation type
     */
    public static final int TRANSFORMATION_TYPE = 8;

    /**
     * The instance of the Transformation type
     */
    public static final Type TRANSFORMATION = new Type(TRANSFORMATION_TYPE, "Transformation");

    /**
     * The Education type
     */
    public static final int EDUCATION_TYPE = 9;

    /**
     * The instance of the Education type
     */
    public static final Type EDUCATION = new Type(EDUCATION_TYPE, "Education");

    /**
     * The Outreach type
     */
    public static final int OUTREACH_TYPE = 10;

    /**
     * The instance of the Outreach type
     */
    public static final Type OUTREACH = new Type(OUTREACH_TYPE, "Outreach");

    /**
     * The EPOResource type
     */
    public static final int EPORESOURCE_TYPE = 11;

    /**
     * The instance of the EPOResource type
     */
    public static final Type EPORESOURCE = new Type(EPORESOURCE_TYPE, "EPOResource");

    /**
     * The Animation type
     */
    public static final int ANIMATION_TYPE = 12;

    /**
     * The instance of the Animation type
     */
    public static final Type ANIMATION = new Type(ANIMATION_TYPE, "Animation");

    /**
     * The Artwork type
     */
    public static final int ARTWORK_TYPE = 13;

    /**
     * The instance of the Artwork type
     */
    public static final Type ARTWORK = new Type(ARTWORK_TYPE, "Artwork");

    /**
     * The Background type
     */
    public static final int BACKGROUND_TYPE = 14;

    /**
     * The instance of the Background type
     */
    public static final Type BACKGROUND = new Type(BACKGROUND_TYPE, "Background");

    /**
     * The BasicData type
     */
    public static final int BASICDATA_TYPE = 15;

    /**
     * The instance of the BasicData type
     */
    public static final Type BASICDATA = new Type(BASICDATA_TYPE, "BasicData");

    /**
     * The Historical type
     */
    public static final int HISTORICAL_TYPE = 16;

    /**
     * The instance of the Historical type
     */
    public static final Type HISTORICAL = new Type(HISTORICAL_TYPE, "Historical");

    /**
     * The Photographic type
     */
    public static final int PHOTOGRAPHIC_TYPE = 17;

    /**
     * The instance of the Photographic type
     */
    public static final Type PHOTOGRAPHIC = new Type(PHOTOGRAPHIC_TYPE, "Photographic");

    /**
     * The Press type
     */
    public static final int PRESS_TYPE = 18;

    /**
     * The instance of the Press type
     */
    public static final Type PRESS = new Type(PRESS_TYPE, "Press");

    /**
     * The Organisation type
     */
    public static final int ORGANISATION_TYPE = 19;

    /**
     * The instance of the Organisation type
     */
    public static final Type ORGANISATION = new Type(ORGANISATION_TYPE, "Organisation");

    /**
     * The Project type
     */
    public static final int PROJECT_TYPE = 20;

    /**
     * The instance of the Project type
     */
    public static final Type PROJECT = new Type(PROJECT_TYPE, "Project");

    /**
     * The Registry type
     */
    public static final int REGISTRY_TYPE = 21;

    /**
     * The instance of the Registry type
     */
    public static final Type REGISTRY = new Type(REGISTRY_TYPE, "Registry");

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

    private Type(int type, java.lang.String value) {
        super();
        this.type = type;
        this.stringValue = value;
    } //-- org.astrogrid.registry.beans.v10.resource.types.Type(int, java.lang.String)


      //-----------/
     //- Methods -/
    //-----------/

    /**
     * Method enumerateReturns an enumeration of all possible
     * instances of Type
     */
    public static java.util.Enumeration enumerate()
    {
        return _memberTable.elements();
    } //-- java.util.Enumeration enumerate() 

    /**
     * Method getTypeReturns the type of this Type
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
        members.put("Other", OTHER);
        members.put("Archive", ARCHIVE);
        members.put("Bibliography", BIBLIOGRAPHY);
        members.put("Catalog", CATALOG);
        members.put("Journal", JOURNAL);
        members.put("Library", LIBRARY);
        members.put("Simulation", SIMULATION);
        members.put("Survey", SURVEY);
        members.put("Transformation", TRANSFORMATION);
        members.put("Education", EDUCATION);
        members.put("Outreach", OUTREACH);
        members.put("EPOResource", EPORESOURCE);
        members.put("Animation", ANIMATION);
        members.put("Artwork", ARTWORK);
        members.put("Background", BACKGROUND);
        members.put("BasicData", BASICDATA);
        members.put("Historical", HISTORICAL);
        members.put("Photographic", PHOTOGRAPHIC);
        members.put("Press", PRESS);
        members.put("Organisation", ORGANISATION);
        members.put("Project", PROJECT);
        members.put("Registry", REGISTRY);
        return members;
    } //-- java.util.Hashtable init() 

    /**
     * Method toStringReturns the String representation of this Type
     */
    public java.lang.String toString()
    {
        return this.stringValue;
    } //-- java.lang.String toString() 

    /**
     * Method valueOfReturns a new Type based on the given String
     * value.
     * 
     * @param string
     */
    public static org.astrogrid.registry.beans.v10.resource.types.Type valueOf(java.lang.String string)
    {
        java.lang.Object obj = null;
        if (string != null) obj = _memberTable.get(string);
        if (obj == null) {
            String err = "'" + string + "' is not a valid Type";
            throw new IllegalArgumentException(err);
        }
        return (Type) obj;
    } //-- org.astrogrid.registry.beans.v10.resource.types.Type valueOf(java.lang.String) 

}
