package org.astrogrid.desktop.modules.ivoa;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.modules.ivoa.DALImpl.BasicErrorChecker;
import org.astrogrid.desktop.modules.ui.actions.CopyAsCommand;
import org.astrogrid.desktop.modules.ui.scope.SiapRetrieval;
import org.astrogrid.desktop.modules.ui.scope.SsapRetrieval;
import org.astrogrid.desktop.modules.ui.scope.StapRetrieval;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;

/** Dal votable handler that saves linked data to disk
 * 
 *  written to handle special cases peculiar to Siap and Ssap
 *  */
public class DatasetSaver extends BasicErrorChecker {
    
    /** sets a subset of rows to save */
	public void setSubset(final List<Integer> rows) {
		subset = true;
		this.rows = rows;
	}

	private  boolean subset;
	private List<Integer> rows;
	private int currentRow;
	private final List<CopyAsCommand> result = new ArrayList<CopyAsCommand>();
	boolean skipNextTable = false;
	boolean resultsTableParsed = false;		
	private int urlIx = -1;
	private int formatIx = -1;
	int colCount;
    private boolean onlyParseResultTable = true;
	
	/** after the parse, this returns the list of commands that will
	 * copy  required
	 * urls to and destinations to copy to.
	 * 
	 */
	public List<CopyAsCommand> getResult() {
		return result;
	}
	
    @Override
    public void resource(final String name, final String id, final String type)
            throws SAXException {
        skipNextTable = ! "results".equalsIgnoreCase(type);
        
    }		
	
    public void startTable(final StarTable t) throws SAXException {
        if (skipThisTable()) {
            return;
        }		    
        checkStarTableErrors(t);
        colCount = t.getColumnCount();
        for (int col = 0; col < colCount; col++) {
            final ColumnInfo nfo = t.getColumnInfo(col);
            final String ucd = nfo.getUCD();
            if (SiapRetrieval.IMG_ACCREF_UCD.equalsIgnoreCase(ucd)
                    || SiapRetrieval.LEGACY_IMG_ACCREF_UCD.equalsIgnoreCase(ucd)  
                    || SsapRetrieval.SPECTRA_URL_UCD.equalsIgnoreCase(ucd)
                    || StapRetrieval.IMG_ACCREF_UCD.equalsIgnoreCase(ucd)
            ) {
                if (urlIx == -1) { // assume the first column that matches is the most important - the accref
                    urlIx = col;
                }
            } else if (
                    SiapRetrieval.IMG_FORMAT_UCD.equalsIgnoreCase(ucd)
                    || SsapRetrieval.SPECTRA_FORMAT_ATTRIBUTE.equalsIgnoreCase(ucd)
                    || StapRetrieval.IMG_FORMAT_UCD.equalsIgnoreCase(ucd)
            ) {
                formatIx = col;
            }
            final DescribedValue utypeDV = nfo.getAuxDatumByName("utype");
            if (utypeDV != null) {
                final String utype = utypeDV.getValueAsString(300);
                if (StringUtils.containsIgnoreCase(utype,SsapRetrieval.SPECTRA_URL_UTYPE)) {
                    urlIx = col;
                } else if (StringUtils.containsIgnoreCase(utype,SsapRetrieval.SPECTRA_FORMAT_UTYPE)) {
                    formatIx = col;
                }
            }
        }
    }
	public void endTable() throws SAXException {
        if (skipThisTable()) {
            return;
        }
        resultsTableParsed = true;
	}
    /** determines whether to skip the current table.
     * @return
     */
    private boolean skipThisTable() {
        return onlyParseResultTable && (skipNextTable || resultsTableParsed);
    }


	
	public void rowData(final Object[] cells) throws SAXException {
        if (skipThisTable() || urlIx == -1) {
            return;
        }		    
		if (!subset || rows.contains(Integer.valueOf(currentRow))) {
				String format = null;
				if (formatIx > -1) {
				    final String rawFormat = cells[formatIx].toString();
				    if (rawFormat.contains("/")) {
				        format = "." + StringUtils.substringAfterLast(rawFormat,"/");
				    } else {
				        format = "." + rawFormat.toLowerCase();
				    }
				}
				try {
				    final CopyAsCommand c = new CopyAsCommand(
				            new URL(cells[urlIx].toString())
				            ,"data-" + currentRow + (format == null ? "" : format)
				            );
				    result.add(c);

				} catch (final MalformedURLException e) {
					DALImpl.logger.warn("Failed to construct url",e); // @todo find a way to report this to client..
				}
		}
		currentRow++;
	}
    /** sets whether only the table marked as 'results' should be parsed.
     * defaults to true
     * @param b
     */
	
    public void setLookForResultTable(final boolean b) {
        onlyParseResultTable = b;
    }
}