/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerProperties.java,v $</cvs:source>
 * <cvs:author>$Author: clq2 $</cvs:author>
 * <cvs:date>$Date: 2005/01/28 10:43:58 $</cvs:date>
 * <cvs:version>$Revision: 1.5 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerProperties.java,v $
 *   Revision 1.5  2005/01/28 10:43:58  clq2
 *   dave_dev_200501141257 (filemanager)
 *
 *   Revision 1.4.2.1  2005/01/18 14:52:48  dave
 *   Added node create and modify dates ..
 *
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.2  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3.4.1  2004/12/24 02:05:05  dave
 *   Refactored exception handling, removing IdentifierException from the public API ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.12  2004/12/06 13:29:02  dave
 *   Added initial code for move location ....
 *
 *   Revision 1.1.2.11  2004/12/03 13:27:52  dave
 *   Core of internal move is in place ....
 *
 *   Revision 1.1.2.10  2004/12/02 19:11:54  dave
 *   Added move name and parent to manager ...
 *
 *   Revision 1.1.2.9  2004/11/29 18:05:07  dave
 *   Refactored methods names ....
 *   Added stubs for delete, copy and move.
 *
 *   Revision 1.1.2.8  2004/11/11 17:53:10  dave
 *   Removed Node interface from the server side ....
 *
 *   Revision 1.1.2.7  2004/11/10 17:00:11  dave
 *   Moving the manager API towards property based rather than node based ...
 *
 *   Revision 1.1.2.6  2004/11/06 20:03:17  dave
 *   Implemented ImportInit and ExportInit using properties
 *
 *   Revision 1.1.2.5  2004/11/05 05:58:31  dave
 *   Refactored the properties handling in importInitEx() ..
 *
 *   Revision 1.1.2.4  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
 *
 *   Revision 1.1.2.3  2004/11/04 02:33:38  dave
 *   Refactored test to include multiple filestores ...
 *
 *   Revision 1.1.2.2  2004/11/02 23:40:08  dave
 *   Fixed typos and bugs ...
 *
 *   Revision 1.1.2.1  2004/11/02 23:21:22  dave
 *   Added FileManagerProperties and filter ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.common ;

import java.util.Date;
import java.util.Iterator;
import java.text.ParseException ;

import org.astrogrid.store.Ivorn ;

import org.astrogrid.filemanager.common.ivorn.FileManagerIvornParser ;
import org.astrogrid.filemanager.common.exception.FileManagerIdentifierException ;

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.FileProperties ;
import org.astrogrid.filestore.common.file.PropertyFilter ;
import org.astrogrid.filestore.common.FileStoreDateFormat;

/**
 * A wrapper for the FileStore properties to include the FileManager properties.
 *
 */
public class FileManagerProperties
    extends FileProperties
    {

    /**
     * The property key for the resource (node) ivorn.
     *
     */
    public static final String MANAGER_RESOURCE_IVORN  = "org.astrogrid.filemanager.resource.ivorn" ;

    /**
     * The property key for the parent (node) ivorn.
     *
     */
    public static final String MANAGER_PARENT_IVORN  = "org.astrogrid.filemanager.parent.ivorn" ;

    /**
     * The property key for the resource (node) type.
     *
     */
    public static final String MANAGER_RESOURCE_TYPE  = "org.astrogrid.filemanager.resource.type" ;

    /**
     * The property key for the resource (node) name.
     *
     */
    public static final String MANAGER_RESOURCE_NAME  = "org.astrogrid.filemanager.resource.name" ;

    /**
     * The property key for the store service ivorn.
     *
     */
    public static final String MANAGER_LOCATION_IVORN  = "org.astrogrid.filemanager.location.ivorn" ;

    /**
     * Type name for a data node.
     *
     */
    public static final String DATA_NODE_TYPE = "org.astrogrid.filemanager.node.type.data" ;

    /**
     * Type name for a container node.
     *
     */
    public static final String CONTAINER_NODE_TYPE = "org.astrogrid.filemanager.node.type.container" ;

    /**
     * The property key for the node create date.
     *
     */
    public static final String NODE_CREATED_DATE_PROPERTY  = "org.astrogrid.filemanager.created.date" ;

    /**
     * The property key for the node modify date.
     *
     */
    public static final String NODE_MODIFIED_DATE_PROPERTY  = "org.astrogrid.filemanager.modified.date" ;

    /**
     * Public constructor.
     *
     */
    public FileManagerProperties()
        {
        super() ;
        }

    /**
     * Public constructor from an array of values.
     * @param array An array of property values.
     *
     */
    public FileManagerProperties(FileProperty[] array)
        {
        super(array);
        }

    /**
     * Public constructor from another set of properties.
     * @param that The other set of properties.
     *
     */
    public FileManagerProperties(FileProperties that)
        {
        super(that) ;
        }

    /**
     * Set a property - automagically updates the modified date at the same time.
     * @param key The property key (name).
     * @param value The property value.
     *
     */
    public void setProperty(String key, String value)
        {
        //
        // Set the property.
        super.setProperty(
            key,
            value
            );
        //
        // Update the modified date.
        this.modified();
        }

    /**
     * Set the node create date.
     *
     */
    protected void created()
        {
        //
        // Create our ISO date formatter.
        FileStoreDateFormat formatter = new FileStoreDateFormat() ;
        //
        // Store the date property as an ISO string.
        this.setProperty(
            NODE_CREATED_DATE_PROPERTY,
            formatter.format(
                new Date()
                )
            );
        }

    /**
     * Set the node modified date.
     *
     */
    public void modified()
        {
        //
        // Create our ISO date formatter.
        FileStoreDateFormat formatter = new FileStoreDateFormat() ;
        //
        // Store the date property as an ISO string.
        super.setProperty(
            NODE_MODIFIED_DATE_PROPERTY,
            formatter.format(
                new Date()
                )
            );
        }

    /**
     * Get the resource ivorn.
     * @throws FileManagerIdentifierException if the resource identifier is invalid.
     *
     */
    public Ivorn getManagerResourceIvorn()
        throws FileManagerIdentifierException
        {
        String string = this.getProperty(
            MANAGER_RESOURCE_IVORN
            ) ;
        if (null != string)
            {
            try {
                return new Ivorn(
                    string
                    ) ;
                }
            catch (Exception ouch)
                {
                throw new FileManagerIdentifierException(
                    string
                    );
                }
            }
        else {
            return null ;
            }
        }

    /**
     * Set the resource ivorn.
     *
     */
    public void setManagerResourceIvorn(Ivorn ivorn)
        {
        if (null != ivorn)
            {
            this.setManagerResourceIvorn(
                ivorn.toString()
                ) ;
            }
        else {
            this.setProperty(
                MANAGER_RESOURCE_IVORN,
                null
                ) ;
            }
        }

    /**
     * Set the resource ivorn.
     *
     */
    public void setManagerResourceIvorn(String string)
        {
        if (null != string)
            {
            this.setProperty(
                MANAGER_RESOURCE_IVORN,
                string
                ) ;
            }
        else {
            this.setProperty(
                MANAGER_RESOURCE_IVORN,
                null
                ) ;
            }
        }

    /**
     * Get the service ivorn.
     * @throws FileManagerIdentifierException if the resource identifier is invalid.
     *
     */
    public Ivorn getManagerServiceIvorn()
        throws FileManagerIdentifierException
        {
        String string = this.getProperty(
            MANAGER_RESOURCE_IVORN
            ) ;
        if (null != string)
            {
            return new FileManagerIvornParser(
                string
                ).getServiceIvorn() ;
            }
        else {
            return null ;
            }
        }

    /**
     * Get the resource identifier.
     * @throws FileManagerIdentifierException if the resource identifier is invalid.
     *
     */
    public String getManagerResourceIdent()
        throws FileManagerIdentifierException
        {
        String string = this.getProperty(
            MANAGER_RESOURCE_IVORN
            ) ;
        if (null != string)
            {
            return new FileManagerIvornParser(
                string
                ).getResourceIdent() ;
            }
        else {
            return null ;
            }
        }

    /**
     * Get the parent resource ivorn.
     * @throws FileManagerIdentifierException if the resource identifier is invalid.
     *
     */
    public Ivorn getManagerParentIvorn()
        throws FileManagerIdentifierException
        {
        String string = this.getProperty(
            MANAGER_PARENT_IVORN
            ) ;
        if (null != string)
            {
            try {
                return new Ivorn(
                    string
                    ) ;
                }
            catch (Exception ouch)
                {
                throw new FileManagerIdentifierException(
                    string
                    );
                }
            }
        else {
            return null ;
            }
        }

    /**
     * Set the parent resource ivorn.
     * @param ivorn The parent resource ivorn.
     *
     */
    public void setManagerParentIvorn(Ivorn ivorn)
        {
        if (null != ivorn)
            {
            this.setProperty(
                MANAGER_PARENT_IVORN,
                ivorn.toString()
                ) ;
            }
        else {
            this.setProperty(
                MANAGER_PARENT_IVORN,
                null
                ) ;
            }
        }

    /**
     * Set the parent resource ivorn.
     * @param ivorn The parent resource ivorn.
     *
     */
    public void setManagerParentIvorn(String string)
        {
        this.setProperty(
            MANAGER_PARENT_IVORN,
            string
            ) ;
        }

    /**
     * Get the resource name.
     *
     */
    public String getManagerResourceName()
        {
        return this.getProperty(
            MANAGER_RESOURCE_NAME
            ) ;
        }

    /**
     * Set the resource (node) name.
     *
     */
    public void setManagerResourceName(String name)
        {
        this.setProperty(
            MANAGER_RESOURCE_NAME,
            name
            ) ;
        }

    /**
     * Get the resource (node) type.
     *
     */
    public String getManagerResourceType()
        {
        return this.getProperty(
            MANAGER_RESOURCE_TYPE
            ) ;
        }

    /**
     * Set the resource (node) type.
     *
     */
    public void setManagerResourceType(String type)
        {
        this.setProperty(
            MANAGER_RESOURCE_TYPE,
            type
            ) ;
        }

    /**
     * Get the filestore storage location.
     *
     */
    public Ivorn getManagerLocationIvorn()
        throws FileManagerIdentifierException
        {
        String string = this.getProperty(
            MANAGER_LOCATION_IVORN
            ) ;
        if (null != string)
            {
            try {
                return new Ivorn(
                    string
                    ) ;
                }
            catch (Exception ouch)
                {
                throw new FileManagerIdentifierException(
                    string
                    );
                }
            }
        else {
            return null ;
            }
        }

    /**
     * Set the filestore storage location.
     * @param ivorn The filestore location.
     *
     */
    public void setManagerLocationIvorn(Ivorn ivorn)
        {
        if (null != ivorn)
            {
            this.setProperty(
                MANAGER_LOCATION_IVORN,
                ivorn.toString()
                ) ;
            }
        else {
            this.setProperty(
                MANAGER_LOCATION_IVORN,
                null
                ) ;
            }
        }

    /**
     * Create a new set of properties containing the differences between this set and another.
     * This compares the properties in the target set and only transfers those which have
     * different values to those in the current set.
     * @param that The other set to compare with.
     * @return A new set containing only the properties that have been changed.
     *
     */
    public FileManagerProperties difference(FileManagerProperties that)
        {
        if (null == that)
            {
            throw new IllegalArgumentException(
                "Null properties"
                );
            }
        //
        // Create a new set of properties.
        FileManagerProperties results = new FileManagerProperties();
        //
        // Get an iterator for the other properties.
        Iterator iter = that.getProperties().keySet().iterator();
        //
        // Check each of the other properties.
        while (iter.hasNext())
            {
            String key   = (String) iter.next();
            String current = this.getProperty(key);
            String changed = that.getProperty(key);
            //
            // Skip the node create and modify dates ....
            if (NODE_CREATED_DATE_PROPERTY.equals(key))
                {
                continue ;
                }
            if (NODE_MODIFIED_DATE_PROPERTY.equals(key))
                {
                continue ;
                }
            //
            // If the changed property is not null.
            if (null != changed)
                {
                //
                // If our property is null.
                if (null == current)
                    {
                    results.setProperty(
                        key,
                        changed
                        );
                    }
                //
                // If our property is not null.
                else {
                    //
                    // If values are different.
                    if (!current.equals(changed))
                        {
                        results.setProperty(
                            key,
                            changed
                            );
                        }
                    }
                }
            }
        //
        // Return the new set of properties.
        return results ;
        }

    /**
     * Get the node create date.
     * @return The node create date.
     *
     */
    public Date getNodeCreateDate()
        {
        //
        // Create our ISO date format.
        FileStoreDateFormat formatter = new FileStoreDateFormat() ;
        //
        // Parse the date property.
        try {
            return formatter.parse(
                this.getProperty(
                    NODE_CREATED_DATE_PROPERTY
                    )
                );
            }
        catch (ParseException ouch)
            {
            return null ;
            }
        }

    /**
     * Get the node modified date.
     * @return The node modified date.
     *
     */
    public Date getNodeModifyDate()
        {
        //
        // Create our ISO date format.
        FileStoreDateFormat formatter = new FileStoreDateFormat() ;
        //
        // Parse the date property.
        try {
            return formatter.parse(
                this.getProperty(
                    NODE_MODIFIED_DATE_PROPERTY
                    )
                );
            }
        catch (ParseException ouch)
            {
            return null ;
            }
        }
    }

