/**
 * 
 */
package org.astrogrid.desktop.modules.util;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URI;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileSystemManager;
import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.util.Tables;
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

	public TablesImpl(final FileSystemManager vfs) {
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
	
	public String convert(final String input, final String inFormat, final String outFormat) throws InvalidArgumentException, ServiceException {       
		final StarTableWriter tableWriter = createTableWriter(outFormat);
		final DataSource ds = new StarTableBuilder.InlineDataSource(input);
		ByteArrayOutputStream os = null;
		try {
			final StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();
		} catch (final IOException e) {
			throw new ServiceException(e);
		} 
	}

	public void convertFiles(final URI inLocation, final String inFormat
			, final URI outLocation, final String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException  {
		final StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
		    final DataSource ds = new URLDataSource(inLocation.toURL()); // as we're using vfs, this will work for myspace too.
			final StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);
		} catch (final MalformedURLException e) {
		    throw new InvalidArgumentException(e);
		} catch (final IOException e) {
			throw new ServiceException(e);
		} finally {
		    IOUtils.closeQuietly(os);
		}		
	}
	
	
	public String convertFromFile(final URI inLocation, final String inFormat, final String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException {
		final StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
		    final DataSource ds = new URLDataSource(inLocation.toURL());
			final StarTable st = builderFactory.makeStarTable(ds,inFormat);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();		
		} catch (final IOException e) {
			throw new ServiceException(e);
		} finally {		
			IOUtils.closeQuietly(os);
		}				
	}
	
	public void convertToFile(final String input, final String inFormat, final URI outLocation, final String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException {
		final StarTableWriter tableWriter = createTableWriter(outFormat);
		OutputStream os = null;
		try {
	        final DataSource ds = new StarTableBuilder.InlineDataSource(input);
			final StarTable st = builderFactory.makeStarTable(ds, inFormat);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);		
		} catch (final IOException e) {
			throw new ServiceException(e);
		} finally {
		IOUtils.closeQuietly(os);
		}		
	}


	
	private OutputStream getOutputStream(final URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
		try {
		    return vfs.resolveFile(u.toString()).getContent().getOutputStream();

	    } catch (final IOException e) {
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

    private StarTableWriter createTableWriter(final String outFormat) throws InvalidArgumentException {
        try {
            return writerFactory.getHandler(outFormat);
        } catch (final TableFormatException x) {
            throw new InvalidArgumentException(outFormat);
        }
    }

    private TableBuilder createTableBuilder(final String inFormat) throws InvalidArgumentException {
        try {
            return builderFactory.getTableBuilder(inFormat);
        } catch (final TableFormatException x) {
            throw new InvalidArgumentException(inFormat);
        }
    }
    

}
