/*$Id: SsapRetrieval.java,v 1.25 2008/11/04 14:35:48 nw Exp $
 * Created on 27-Jan-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import static org.apache.commons.lang.StringUtils.containsIgnoreCase;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Pattern;

import org.apache.commons.lang.ObjectUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.vfs.FileSystemException;
import org.astrogrid.acr.ivoa.Ssap;
import org.astrogrid.acr.ivoa.resource.Service;
import org.astrogrid.acr.ivoa.resource.SsapCapability;
import org.astrogrid.desktop.modules.system.ProgrammerError;
import org.astrogrid.desktop.modules.ui.MonitoringInputStream;
import org.astrogrid.desktop.modules.ui.dnd.VoDataFlavour;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.DescribedValue;
import uk.ac.starlink.table.StarTable;
import edu.berkeley.guir.prefuse.graph.DefaultTreeNode;
import edu.berkeley.guir.prefuse.graph.TreeNode;
/** Retreiver for SSAP service, v1.04
 * @TEST
 */
public class SsapRetrieval extends AbstractRetriever {
    public SsapRetrieval(final Service service,final SsapCapability cap,final URI acurl,final NodeSocket socket,final VizModel model,final Ssap ssap,final double ra,final double dec,final double raSize, final double decSize) {
        super(service,cap,socket,model,ra,dec);
        this.accessUrl = acurl;
        this.ssap = ssap;
        this.raSize= raSize;
        this.decSize = decSize;
    }
    protected final Ssap ssap;
    protected final URI accessUrl;
    protected final double raSize;
    protected final double decSize;
    @Override
    protected Object construct() throws Exception {
        reportProgress("Constructing query");        
        final URL ssapURL =  ssap.constructQueryS(accessUrl,ra,dec,raSize,decSize);
        final StringBuffer sb = new StringBuffer();
        sb.append("<html>Title: ").append(service.getTitle())
            .append("<br>ID: ").append(service.getId());
        final String subName = getSubName();
        if (subName != null && subName.trim().length() > 0) {
            sb.append(" - ").append(subName);
        }
//                if (service.getContent() != null) {
//               sb.append("<br>Description: <p>")
//                .append(service.getContent().getDescription()!= null 
//                			?   WordUtils.wrap(service.getContent().getDescription(),AstroScopeLauncherImpl.TOOLTIP_WRAP_LENGTH,"<br>",false) : "");
//            }
          //      sb.append("</html>"); 
        // build subtree for this service
                reportProgress("Querying service");
        final MonitoringInputStream monitorStream = MonitoringInputStream.create(this,ssapURL,MonitoringInputStream.ONE_KB );
        final TreeNode serviceNode = createServiceNode(ssapURL
                ,monitorStream.getSize()
                ,sb.toString());
        final InputSource source = new InputSource(monitorStream);
        final AstroscopeTableHandler th = new SsapTableHandler(serviceNode);
        parseTable(source, th); 
        return th;                 
    }
    //constants used with tablehandler.
    // the keys are UCDs - this makes it simpler to pass all metadata onto another app via plastic.
    
    // the Attribute keys are also used as constants for the UCDS to search for.
    // latest SSA spec has replaced useage of UCD by utype - but is poorly implemented at present
    // so keeping both systems at present.
    // only implemented a partial list of the utypes for now - even less than for the ucd.
    public static final String SPECTRA_URL_LEGACY_ATTRIBUTE = "DATA_LINK";
    public static final String SPECTRA_URL_UCD = "meta.ref.url";
    public static final String POS_UCD = "pos.eq";
    public static final String RA_UCD = "pos.eq.ra";
    public static final String DEC_UCD = "pos.eq.dec";
    public static final String SPECTRA_URL_UTYPE = "Access.Reference";
    public static final String SPECTRA_TITLE_ATTRIBUTE = "VOX:Image_Title";
    public static final String SPECTRA_TITLE_UCD = "meta.id"; // matches meta.id;meta.main and meta.id;meta.dataset
    public static final String SPECTRA_TITLE_UTYPE = "Target.Name";
    public static final String SPECTRA_AXES_ATTRIBUTE = "VOX:Spectrum_axes";
    public static final String SPECTRA_DIMEQ_ATTRIBUTE = "VOX:Spectrum_dimeq"; 
    public static final String SPECTRA_DIMEQ_UTYPE = "Dime.Q";
    public static final String SPECTRA_SCALEQ_ATTRIBUTE ="VOX:Spectrum_scaleq";
    public static final String SPECTRA_SCALEQ_UTYPE = "Scale.Q";
    public static final String SPECTRA_FORMAT_ATTRIBUTE = "VOX:Spectrum_Format";
    public static final String SPECTRA_FORMAT_UTYPE = "Access.Format";   
    //utype  Access.Size - estimated size - not seen in wild.
    public static final String SPECTRA_UNITS_ATTRIBUTE = "VOX:Spectrum_units";
    public static final String SPECTRA_START_TIME_ATTRIBUTE = "VOX:OBS_START_TIME";
    public static final String SPECTRA_START_TIME_UTYPE = "Char.TimeAxis.Coverage.Bounds.Start";
    public static final String SPECTRA_END_TIME_ATTRIBUTE = "VOX:OBS_END_TIME";
    public static final String SPECTRA_END_TIME_UTYPE = "Char.TimeAxis.Coverage.Bounds.Stop";
    public static final String SPECTRA_OBS_ID_ATTRIBUTE = "OBS_ID";
        
    //utype ssa:Query.LName - why the prefix??
    
    /** pattern that detects broken forms of mime type */
    public static final Pattern BUST_MIME = Pattern.compile("\\w+/\\w+;[^=]+");
    
    public class SsapTableHandler extends BasicTableHandler {


        public SsapTableHandler(final TreeNode serviceNode) {
            super(serviceNode);
        }
        int posCol = -1; // combined ra,dec col - grr.
        int urlCol = -1;
        int titleCol = -1;
        int formatCol = -1;
        int spectrumAxesCol = -1;
        int spectrumDimeqCol = -1;
        int spectrumScaleqCol = -1;
        int spectrumUnitsCol = -1;
        int endTimeCol = -1;
        int startTimeCol = -1;
        int obsIdCol = -1;
        
        private boolean skipNextTable = false;
        private boolean resultsTableParsed = false;
        

        @Override
        public void resource(final String name, final String id, final String type)
                throws SAXException {
            skipNextTable = ! "results".equals(type);
        }
        
        @Override
        public void info(final String name, final String value, final String content)
                throws SAXException {
            // assume we're inside the 'result' resource.
            if ("QUERY_STATUS".equals(name) && ! "OK".equalsIgnoreCase(value)) {
                throw new DalProtocolException(content != null ? content : value);
            }
        }
                
        @Override
        public void startTable(final StarTable starTable) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
                super.startTable(starTable);
        }
        @Override
        public void rowData(final Object[] row) throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
            }
           super.rowData(row);
            
        }
             
        @Override
        protected void startTableExtensionPoint(final int col, final ColumnInfo columnInfo) {            
            super.startTableExtensionPoint(col, columnInfo);
            final DescribedValue utypeDV = columnInfo.getAuxDatumByName("utype");
            final String ucd  = columnInfo.getUCD();

            if (columnInfo.isArray() 
                    &&  (ucd != null && containsIgnoreCase(ucd,POS_UCD))
                    ) { // could test utypes here too, but there's so many of em.
                posCol = col;
                return;
            }
            if (utypeDV != null) {
                final String utype = utypeDV.getValueAsString(300);
                if (StringUtils.isNotBlank(utype)) {
                    // use 'contains' as they sometimes seem to come with a 'ssa:' prefix
                    if (containsIgnoreCase(utype,SPECTRA_URL_UTYPE)) {
                        urlCol = col;
                    } else if (containsIgnoreCase(utype,SPECTRA_TITLE_UTYPE)) {
                        titleCol = col;
                    } else if (containsIgnoreCase(utype,SPECTRA_DIMEQ_UTYPE)) {
                        spectrumDimeqCol = col;
                    } else if (containsIgnoreCase(utype,SPECTRA_SCALEQ_UTYPE)) {
                        spectrumScaleqCol = col;
                    } else if (containsIgnoreCase(utype,SPECTRA_FORMAT_UTYPE)) {
                        formatCol = col;
                    }
                }
            }

            if (ucd != null) {
                if (containsIgnoreCase(ucd,SPECTRA_URL_LEGACY_ATTRIBUTE)
                        || containsIgnoreCase(ucd,SPECTRA_URL_UCD)) {
                    urlCol = col;
                } else if (containsIgnoreCase(ucd,RA_UCD)) {
                    raCol = col;
                } else if (containsIgnoreCase(ucd,DEC_UCD)) {
                    decCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_TITLE_ATTRIBUTE)
                        || containsIgnoreCase(ucd,SPECTRA_TITLE_UCD)) {
                    titleCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_FORMAT_ATTRIBUTE)) {
                    formatCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_AXES_ATTRIBUTE)) {
                    spectrumAxesCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_DIMEQ_ATTRIBUTE)) {
                    spectrumDimeqCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_SCALEQ_ATTRIBUTE)) {
                    spectrumScaleqCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_UNITS_ATTRIBUTE)){
                    spectrumUnitsCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_OBS_ID_ATTRIBUTE)) {
                    obsIdCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_START_TIME_ATTRIBUTE)) {
                    startTimeCol = col;
                } else if (containsIgnoreCase(ucd,SPECTRA_END_TIME_ATTRIBUTE)) {
                    endTimeCol= col;                    
                }
            }
        }
        

        
        @Override
        protected void rowDataExtensionPoint(final Object[] row, final TreeNode valNode) {
            try {
                final URL url = new URL(safeTrim(row[urlCol]));
                valNode.setAttribute(SPECTRA_URL_LEGACY_ATTRIBUTE,url.toString());

                String title;
                if (titleCol > -1) {
                    title = safeTrim(row[titleCol]);
                } else {
                    title  = "untitled";
                }
                valNode.setAttribute(SPECTRA_TITLE_ATTRIBUTE,title);
                
                String type;
                if (formatCol > -1) {
                    type = safeTrim(row[formatCol]);
                    if (type.equals("FITS")) { // make it a bit more standard.
                        type = VoDataFlavour.MIME_FITS_SPECTRUM;       
                    } else if (BUST_MIME.matcher(type).matches()) { // broken form of mime type, tidy up.
                        final String[] arr = StringUtils.split(type,";");
                        if (arr.length == 2 && ! arr[1].contains("votable")) { // special case - leave broken forms mentioning votable as-is.
                            type = arr[0];
                        }

                    }
                } else {
                    type="unknown";
                }
                valNode.setAttribute(SPECTRA_FORMAT_ATTRIBUTE ,type);

                valNode.setAttribute(LABEL_ATTRIBUTE,title + ", " + StringUtils.substringAfterLast(type,"/"));

                if (spectrumAxesCol > -1) {                   
                    valNode.setAttribute(SPECTRA_AXES_ATTRIBUTE,safeTrim(row[spectrumAxesCol]));
                }
                
                if (spectrumDimeqCol > -1) {                
                    valNode.setAttribute(SPECTRA_DIMEQ_ATTRIBUTE,safeTrim(row[spectrumDimeqCol]));                
                }
                
                if (spectrumScaleqCol > -1) {                 
                    final StrBuilder sb = new StrBuilder();
                    final Object o = row[spectrumScaleqCol];
                    // sometimes seems to be a string, sometimes a double array
                    if (o.getClass().isArray() && o.getClass().getComponentType() == Double.TYPE) {
                        final double[] is = (double[]) o;
                        for (int i = 0; i < is.length ; i++) {
                            sb.append(is[i]);
                            sb.append(" ");
                        }
                    } else  {
                        sb.append(safeTrim(o));
                    }
                    valNode.setAttribute(SPECTRA_SCALEQ_ATTRIBUTE,sb.toString());                
                }

                if (spectrumUnitsCol > -1) {
                    valNode.setAttribute(SPECTRA_UNITS_ATTRIBUTE,safeTrim(row[spectrumUnitsCol]));
                }

                // now build a file object of the results.
                try {
                    // construct a time, in order of preference.
                    Date parse = null;
                    if (endTimeCol != -1 ) {
                            //ObjectUtils.toString handles nulls by returning the empty string.
                            parse = dfA.parse(ObjectUtils.toString(row[endTimeCol]),new ParsePosition(0));
                            if (parse == null) {
                                parse = dfB.parse(ObjectUtils.toString(row[endTimeCol]),new ParsePosition(0));
                            }
                    }
                    if (parse == null && startTimeCol != -1) {
                        parse = dfA.parse(ObjectUtils.toString(row[startTimeCol]),new ParsePosition(0));
                        if (parse == null) {
                            parse = dfB.parse(ObjectUtils.toString(row[startTimeCol]),new ParsePosition(0));
                        }                       
                    }
                    if (parse == null) {
                        parse = new Date();
                    }
                    
                    final AstroscopeFileObject fileObject = model.createFileObject(url
                            ,AstroscopeFileObject.UNKNOWN_SIZE
                            ,parse.getTime()
                            ,StringUtils.containsIgnoreCase(type,"fits") ? VoDataFlavour.MIME_FITS_SPECTRUM : type
                    );
                    filenameBuilder.clear();
                    filenameBuilder.append(StringUtils.replace(title,"/","_"));
                    if (obsIdCol != -1) {
                        final Object o = row[obsIdCol];
                        if (o != null) {
                            filenameBuilder.append(" (")
                            .append(StringUtils.replace(o.toString(),"/","_"))
                            .append(")");
                        }
                    }
                    filenameBuilder.append(".");
                    String suffix = StringUtils.substringAfterLast(type,"/");
                    // work around for ESO
                    final int nonsense = StringUtils.indexOfAny(suffix,";, ?'\"");
                    if (nonsense != -1) {
                        suffix = suffix.substring(0,nonsense);
                    }
                    filenameBuilder.append(suffix);
                    model.addResultFor(SsapRetrieval.this,filenameBuilder.toString(),fileObject,(FileProducingTreeNode)valNode);                    
                } catch (final FileSystemException e) {
                    logger.warn(service.getId() + " : Unable to create result file object - skipping row",e);
                }
            }catch (final MalformedURLException e) {
                logger.warn(service.getId() + " : Unable to parse url in service response - skipping row",e);
            }
        }
        private final StrBuilder filenameBuilder = new StrBuilder(64);
        //3 different formats, for the various formats seen 'in the wild'.
        // "1998-01-31 11:26:00.792"
        // 1995-03-13 19:50:17
        //Aug 22 1995  8:25:23:150PM - non-standard - shan't parse.
        private final DateFormat dfA = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        private final DateFormat dfB = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                
    	@Override
        public DefaultTreeNode createValueNode() {
    	    return new FileProducingTreeNode();
   	}        
    	
    	@Override
    	protected String getRaFromRow(final Object[] row) {
            if (posCol != -1) {
                final Object o = row[posCol];
                if (o instanceof double[]){
                    return Double.toString(((double[])o)[0]);
                } else if(o instanceof float[]) {
                    return Float.toString(((float[])o)[0]);
                } else {
                    throw new ProgrammerError("Unexpected array type " + o.getClass().getName());
                }
            }    	    
    	    return super.getRaFromRow(row);
    	}
    	@Override
    	protected String getDecFromRow(final Object[] row) {
    	    if (posCol != -1) {
    	        final Object o = row[posCol];
    	        if (o instanceof double[]){
    	            return Double.toString(((double[])o)[1]);
    	        } else if(o instanceof float[]) {
    	            return Float.toString(((float[])o)[1]);
    	        } else {
    	            throw new ProgrammerError("Unexpected array type " + o.getClass().getName());
    	        }
    	    }
    	    //otherwise do this.
    	    return super.getDecFromRow(row);
    	    
    	}
    	@Override
        protected boolean omitRowFromTooltip(final int rowIndex) {
    	    return rowIndex == urlCol;
    	}
        
        @Override
        protected void isWorthProceeding() throws InsufficientMetadataException {
            // minimal subset of stuff.
            if (posCol == -1) { // check for ra and dec then
                super.isWorthProceeding();
            }
            if(urlCol == -1) {
                throw new InsufficientMetadataException("Access Reference column not detected");
            }
        
        }
        //make it hyper-strict - to make life easy for vospec
        /*
        protected boolean isWorthProceeding() {
            return super.isWorthProceeding() 
                && urlCol >= 0
                && titleCol >= 0
                && formatCol >= 0
                && spectrumAxesCol >= 0
                && spectrumDimeqCol >= 0
                && spectrumScaleqCol >= 0                
            ; 
                //@todo should I require units too?
        } */       
        
        @Override
        public void endTable() throws SAXException {
            if (skipNextTable || resultsTableParsed) {
                return;
                
            }
            resultsTableParsed = true;
                super.endTable();
                urlCol= -1;
                titleCol = -1;
                formatCol = -1;
                endTimeCol = -1;
                startTimeCol = -1;
                obsIdCol = -1;     
                posCol = -1;
            
        }
    }// end table handler class.
    @Override
    public String getServiceType() {
        return SSAP;
    }
    public static final String SSAP = "SSAP";
}

/* 
$Log: SsapRetrieval.java,v $
Revision 1.25  2008/11/04 14:35:48  nw
javadoc polishing

Revision 1.24  2008/08/20 09:34:36  nw
RESOLVED - bug 2813: SSAP services FITS files not really FITS files
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2813

Revision 1.23  2008/08/07 11:52:36  nw
RESOLVED - bug 2767: VOExplore searching eso-ssap
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2767

Revision 1.22  2008/08/06 18:54:47  nw
removed closing html tag.

Revision 1.21  2008/04/25 08:59:36  nw
extracted interface from retriever, to ease unit testing.

Revision 1.20  2008/04/23 11:17:53  nw
marked as needing test.

Revision 1.19  2008/03/30 18:02:54  nw
FIXED - bug 2689: upgrade to handle latest ssa standard.
http://www.astrogrid.org/bugzilla/show_bug.cgi?id=2689

Revision 1.18  2008/03/26 10:35:14  nw
checked configuraiton of html builder and strbuilder

Revision 1.17  2008/02/25 13:25:48  mbt
Add subnames to node tooltips as appropriate

Revision 1.16  2008/02/22 17:03:35  mbt
Merge from branch mbt-desktop-2562.
Basically, Retrievers rather than Services are now the objects (associated
with TreeNodes) which communicate with external servers to acquire results.
Since Registry v1.0 there may be multiple Retrievers (even of a given type)
per Service.

Revision 1.15.18.3  2008/02/22 15:18:30  mbt
Fix so that multiple capabilities of a single service are anchored at a single node representing that service, rather than direct from the primary node

Revision 1.15.18.2  2008/02/21 15:35:15  mbt
Now does multiple-capability-per-service for all known protocols

Revision 1.15.18.1  2008/02/21 11:06:09  mbt
First bash at 2562.  AstroScope now runs multiple cone searches per Service

Revision 1.15  2007/12/12 13:54:12  nw
astroscope upgrade, and minor changes for first beta release

Revision 1.14  2007/11/28 09:03:51  nw
Complete - task 256: Complete retriever bugfxes

Revision 1.13  2007/11/27 08:19:01  nw
integrate commons.io
progress tracking or reading from streams.

Revision 1.12  2007/11/26 14:44:45  nw
Complete - task 224: review configuration of all backgroiund workers

Revision 1.11  2007/09/21 16:35:15  nw
improved error reporting,
various code-review tweaks.

Revision 1.10  2007/05/10 19:35:22  nw
reqwork

Revision 1.9  2007/04/18 15:47:08  nw
tidied up voexplorer, removed front pane.

Revision 1.8  2007/03/08 17:43:56  nw
first draft of voexplorer

Revision 1.7  2007/02/06 18:56:10  nw
made response parsing resilient to whitespace in columns.

Revision 1.6  2007/01/29 10:43:49  nw
documentation fixes.

Revision 1.5  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.4  2006/08/15 09:59:58  nw
migrated from old to new registry models.

Revision 1.3  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.2.6.1  2006/04/14 02:45:00  nw
finished code.extruded plastic hub.

Revision 1.2  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer

Revision 1.1  2006/02/02 14:51:11  nw
components of astroscope, plus new ssap component.
 
*/
