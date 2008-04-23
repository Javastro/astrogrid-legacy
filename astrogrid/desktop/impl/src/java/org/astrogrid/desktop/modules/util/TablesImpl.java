/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.util.Tables;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.common.FileManagerFault;
import org.astrogrid.scripting.table.StarTableBuilder;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.StoragePolicy;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.table.TableFormatException;
import uk.ac.starlink.util.DataSource;
import uk.ac.starlink.util.URLDataSource;

/** Implementation of the tables utility.
 * @author Noel Winstanley
 * @since Aug 25, 20061:31:28 AM
 * @TEST convertToFile
 */
public class TablesImpl implements Tables {

	public TablesImpl(FileSystemManager vfs) {
		//@todo - candidate for factoring out?
		this.builderFactory = new StarTableFactory(false);
		// configure to be more memory-efficient.
		this.builderFactory.setStoragePolicy(StoragePolicy.PREFER_DISK);
		this.writerFactory = new StarTableOutput();
		this.vfs = vfs;
	}
	
	protected final StarTableFactory builderFactory;
	protected final StarTableOutput writerFactory;
	protected final FileSystemManager vfs;
	
	public String convert(String input, String inFormat, String outFormat) throws InvalidArgumentException, ServiceException {       
		StarTableWriter tableWriter = createTableWriter(outFormat);
		DataSource ds = new StarTableBuilder.InlineDataSource(input);
		ByteArrayOutputStream os = null;
		try {
			StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();
		} catch (IOException e) {
			throw new ServiceException(e);
		} 
	}

	public void convertFiles(URI inLocation, String inFormat
			, URI outLocation, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException  {
		StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
		    DataSource ds = new URLDataSource(inLocation.toURL()); // as we're using vfs, this will work for myspace too.
			StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);
		} catch (MalformedURLException e) {
		    throw new InvalidArgumentException(e);
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// nevermind
				}
			}
		}		
	}
	
	
	public String convertFromFile(URI inLocation, String inFormat, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException {
		StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
		    DataSource ds = new URLDataSource(inLocation.toURL());
			StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();		
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {		
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// nevermind
				}
			}
		}				
	}
	
	public void convertToFile(String input, String inFormat, URI outLocation, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException {
		StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
	        DataSource ds = new StarTableBuilder.InlineDataSource(input);
			StarTable st = builderFactory.makeStarTable(ds, inFormat);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);		
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
		
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// nevermind
				}
			}
		}		
	}


	
	private OutputStream getOutputStream(URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
		try {
		    return vfs.resolveFile(u.toString()).getContent().getOutputStream();

	    } catch (IOException e) {
	        throw new ServiceException(e);
	    }
	}
	
	public String[] listOutputFormats() {
		return (String[])writerFactory.getKnownFormats().toArray(new String[]{});
	}
	
	public String[] listInputFormats() {
		return (String[])builderFactory.getKnownFormats().toArray(new String[]{});
	}
	
	// supporting methods.

    private StarTableWriter createTableWriter(String outFormat) throws InvalidArgumentException {
        try {
            return writerFactory.getHandler(outFormat);
        } catch (TableFormatException x) {
            throw new InvalidArgumentException(outFormat);
        }
    }

    private TableBuilder createTableBuilder(String inFormat) throws InvalidArgumentException {
        try {
            return builderFactory.getTableBuilder(inFormat);
        } catch (TableFormatException x) {
            throw new InvalidArgumentException(inFormat);
        }
    }
    

}
