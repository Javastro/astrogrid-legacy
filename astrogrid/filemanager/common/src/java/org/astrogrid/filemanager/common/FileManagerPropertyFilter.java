/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerPropertyFilter.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.4 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerPropertyFilter.java,v $
 *   Revision 1.4  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.3.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.3  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.4  2004/12/08 01:56:04  dave
 *   Added filestore location to move ...
 *
 *   Revision 1.1.2.3  2004/11/05 02:23:45  dave
 *   Refactored identifiers are properties ...
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

import org.astrogrid.filestore.common.file.FileProperty ;
import org.astrogrid.filestore.common.file.PropertyFilter ;

/**
 * This filter excludes properties that should be set by the local FileManager service.
 * This prevents external entities from changing the local properties.
 *
 *
 */
public class FileManagerPropertyFilter
    implements PropertyFilter
    {
    /**
     * Public constructor.
     *
     */
    public FileManagerPropertyFilter()
        {
        }

    /**
     * Filter a property.
     * @param property The property to filter.
     * @return The filtered property value, or null if the whole property has been filtered out.
     *
     */
    public FileProperty filter(FileProperty property)
        {
        if (FileManagerProperties.MANAGER_RESOURCE_IVORN.equals(property.getName()))
            {
            return null ;
            }
        if (FileManagerProperties.MANAGER_PARENT_IVORN.equals(property.getName()))
            {
            return null ;
            }
        if (FileManagerProperties.MANAGER_RESOURCE_TYPE.equals(property.getName()))
            {
            return null ;
            }
        if (FileManagerProperties.MANAGER_RESOURCE_NAME.equals(property.getName()))
            {
            return null ;
            }
        //
        // Return the original property.
        return property ;
        }
    }

