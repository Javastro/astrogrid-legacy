/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/common/src/java/org/astrogrid/filemanager/common/Attic/FileManagerResourceFilter.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2005/01/13 17:23:15 $</cvs:date>
 * <cvs:version>$Revision: 1.3 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerResourceFilter.java,v $
 *   Revision 1.3  2005/01/13 17:23:15  jdt
 *   merges from dave-dev-200412201250
 *
 *   Revision 1.2.4.1  2005/01/12 13:16:27  dave
 *   Changed tabs to spaces ...
 *
 *   Revision 1.2  2004/12/16 17:25:49  jdt
 *   merge from dave-dev-200410061224-200412161312
 *
 *   Revision 1.1.2.1  2004/12/11 05:59:18  dave
 *   Added internal copy for nodes ...
 *   Added local copy for data ...
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
import org.astrogrid.filestore.common.file.FileStorePropertyFilter ;

/**
 * This filter excludes the FileManager and filestore resource properties.
 *
 *
 */
public class FileManagerResourceFilter
    extends FileStorePropertyFilter
    implements PropertyFilter
    {
    /**
     * Public constructor.
     *
     */
    public FileManagerResourceFilter()
        {
        super();
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
        //
        // Filter the filestore properties.
        return super.filter(
            property
            ) ;
        }
    }

