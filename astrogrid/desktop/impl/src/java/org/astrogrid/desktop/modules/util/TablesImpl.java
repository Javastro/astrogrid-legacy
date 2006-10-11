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
import java.net.URI;

import org.astrogrid.acr.InvalidArgumentException;
import org.astrogrid.acr.NotFoundException;
import org.astrogrid.acr.SecurityException;
import org.astrogrid.acr.ServiceException;
import org.astrogrid.acr.util.Tables;
import org.astrogrid.desktop.modules.ag.MyspaceInternal;
import org.astrogrid.filemanager.common.FileManagerFault;

import uk.ac.starlink.table.StarTable;
import uk.ac.starlink.table.StarTableFactory;
import uk.ac.starlink.table.StarTableOutput;
import uk.ac.starlink.table.StarTableWriter;
import uk.ac.starlink.table.StoragePolicy;
import uk.ac.starlink.table.TableBuilder;
import uk.ac.starlink.table.TableFormatException;

/** Implementation of the tables utility.
 * @author Noel Winstanley
 * @since Aug 25, 20061:31:28 AM
 */
public class TablesImpl implements Tables {

	public TablesImpl(MyspaceInternal vos) {
		//@todo - candidate for factoring out?
		this.builderFactory = new StarTableFactory(false);
		// configure to be more memory-efficient.
		this.builderFactory.setStoragePolicy(StoragePolicy.PREFER_DISK);
		this.writerFactory = new StarTableOutput();
		this.vos = vos;
	}
	
	protected final StarTableFactory builderFactory;
	protected final StarTableOutput writerFactory;
	protected final MyspaceInternal vos;
	
	public String convert(String input, String inFormat, String outFormat) throws InvalidArgumentException, ServiceException {
		TableBuilder tableBuilder;
		StarTableWriter tableWriter;
		try {
			tableBuilder = builderFactory.getTableBuilder(inFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(inFormat);
		}
		try {
			tableWriter = writerFactory.getHandler(outFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(outFormat);
		}
		InputStream is = null;
		ByteArrayOutputStream os = null;
		try {
			is = new ByteArrayInputStream(input.getBytes());
			StarTable st = builderFactory.makeStarTable(is,tableBuilder);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// nevermind
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// nevermind
				}
			}
		}
	}
	
	public void convertFiles(URI inLocation, String inFormat
			, URI outLocation, String outFormat) throws InvalidArgumentException, ServiceException, NotFoundException, SecurityException  {
		TableBuilder tableBuilder;
		StarTableWriter tableWriter;
		try {
			tableBuilder = builderFactory.getTableBuilder(inFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(inFormat);
		}
		try {
			tableWriter = writerFactory.getHandler(outFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(outFormat);
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			is = getInputStream(inLocation);
			StarTable st = builderFactory.makeStarTable(is,tableBuilder);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// nevermind
				}
			}
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
		TableBuilder tableBuilder;
		StarTableWriter tableWriter;
		try {
			tableBuilder = builderFactory.getTableBuilder(inFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(inFormat);
		}
		try {
			tableWriter = writerFactory.getHandler(outFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(outFormat);
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			is = getInputStream(inLocation);
			StarTable st = builderFactory.makeStarTable(is,tableBuilder);
			os = new ByteArrayOutputStream();
			writerFactory.writeStarTable(st,os,tableWriter);
			os.close();
			return os.toString();		
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// nevermind
				}
			}
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
		TableBuilder tableBuilder;
		StarTableWriter tableWriter;
		try {
			tableBuilder = builderFactory.getTableBuilder(inFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(inFormat);
		}
		try {
			tableWriter = writerFactory.getHandler(outFormat);
		} catch (TableFormatException x) {
			throw new InvalidArgumentException(outFormat);
		}
		InputStream is = null;
		OutputStream os = null;
		try {
			is = new ByteArrayInputStream(input.getBytes());
			StarTable st = builderFactory.makeStarTable(is,tableBuilder);
			os = getOutputStream(outLocation);
			writerFactory.writeStarTable(st,os,tableWriter);		
		} catch (IOException e) {
			throw new ServiceException(e);
		} finally {
			if (is != null) {
				try {
					is.close();
				} catch (IOException e) {
					// nevermind
				}
			}
			if (os != null) {
				try {
					os.close();
				} catch (IOException e) {
					// nevermind
				}
			}
		}		
	}
	
	// myspace componet could all do this itself - but access forces login, which is a bit of a pain if you're just reading and 
	// writing to local disk. - so test first, and only pass the ivo ones on to it.
	//@todo replace with a decent infrastucture based on URLProctocolHandlers
	private InputStream getInputStream(URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
		try {
		if (u.getScheme().equals("ivo")) {
				return vos.getInputStream(u);
		} else if (u.getScheme().equals("file")) {
            return new FileInputStream(new File(u));
        } else {
            return u.toURL().openStream();
        }
    } catch (FileNotFoundException e) {
        throw new NotFoundException(e);
    } catch (FileManagerFault e) {
        throw new ServiceException(e);
    } catch (IOException e) {
        throw new ServiceException(e);
    }
	}
	
	private OutputStream getOutputStream(URI u) throws InvalidArgumentException, NotFoundException, SecurityException, ServiceException {
		try {
		if (u.getScheme().equals("ivo")) {
			return vos.getOutputStream(u);
	} else if (u.getScheme().equals("file")) {
        return new FileOutputStream(new File(u));
    } else {
        return u.toURL().openConnection().getOutputStream();                      
    }      
   } catch (FileNotFoundException e) {
       throw new NotFoundException(e);
   } catch (FileManagerFault e) {
       throw new ServiceException(e);
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

}
