/*
 * <cvs:source>$Source: /Users/pharriso/Work/ag/repo/git/astrogrid-mirror/astrogrid/filemanager/server/src/java/org/astrogrid/filemanager/server/Attic/FileManagerImpl.java,v $</cvs:source>
 * <cvs:author>$Author: jdt $</cvs:author>
 * <cvs:date>$Date: 2004/11/25 00:20:30 $</cvs:date>
 * <cvs:version>$Revision: 1.2 $</cvs:version>
 * <cvs:log>
 *   $Log: FileManagerImpl.java,v $
 *   Revision 1.2  2004/11/25 00:20:30  jdt
 *   Merge from dave-dev-200410061224-200411221626
 *
 *   Revision 1.1.2.2  2004/11/18 14:39:32  dave
 *   Added SOAP delegate, RemoteException decoding and test case.
 *
 *   Revision 1.1.2.1  2004/11/17 07:56:33  dave
 *   Added server mock and webapp build scripts ...
 *
 * </cvs:log>
 *
 */
package org.astrogrid.filemanager.server ;

import org.astrogrid.filemanager.common.FileManager;
import org.astrogrid.filemanager.common.FileManagerMock;
import org.astrogrid.filemanager.common.FileManagerConfig;
import org.astrogrid.filemanager.common.FileManagerConfigImpl;
import org.astrogrid.filemanager.common.ivorn.FileManagerIvornFactory;

import org.astrogrid.filestore.resolver.FileStoreDelegateResolver;
import org.astrogrid.filestore.resolver.FileStoreDelegateResolverImpl;

/**
 * The public interface for a file manager service.
 *
 */
public class FileManagerImpl
	extends FileManagerMock
	implements FileManager
	{

	/**
	 * Public constructor, using the default configuration, identifier factory and resolver.
	 * @param config The local file manager configuration.
	 * @param factory A factory for creating resource identifiers.
	 * @param resolver A resolver to locate filestores.
	 *
	 */
	public FileManagerImpl()
		{
		this(
			new FileManagerConfigImpl(),
			new FileManagerIvornFactory(),
			new FileStoreDelegateResolverImpl()
			);
		}

	/**
	 * Public constructor, using a custom configuration, identifier factory and resolver.
	 * @param config The local file manager configuration.
	 * @param factory A factory for creating resource identifiers.
	 * @param resolver A resolver to locate filestores.
	 *
	 */
	public FileManagerImpl(
		FileManagerConfig config,
		FileManagerIvornFactory factory,
		FileStoreDelegateResolver resolver
		)
		{
		super(
			config,
			factory,
			resolver
			);
		}
	}
