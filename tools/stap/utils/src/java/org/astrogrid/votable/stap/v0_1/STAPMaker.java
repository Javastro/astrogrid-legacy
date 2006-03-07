package org.astrogrid.tools.votable.stap.v0_1;

import uk.ac.starlink.table.ColumnInfo;
import uk.ac.starlink.table.RowListStarTable;
import uk.ac.starlink.votable.VOSerializer;
import uk.ac.starlink.votable.DataFormat;
import uk.ac.starlink.votable.VOTableWriter;

import java.io.BufferedWriter;
import java.io.Writer;
import java.io.IOException;

public class STAPMaker {
    

    private Object []values = null;
    private RowListStarTable astro = null;
    
    public STAPMaker() {
        ColumnInfo []defValues;
        defValues = new ColumnInfo[8];
        defValues[0] = new ColumnInfo("ACCESS_URL",String.class,"Url pointing to data file");
        defValues[0].setUCD("VOX:AccessReference");
        defValues[1] = new ColumnInfo("PROVIDER",String.class,"The archive (STAP service) providing the data");
        defValues[1].setUCD("meta.curation");
        defValues[2] = new ColumnInfo("TIME_START",String.class,"Start time of data in the file  ");
        defValues[2].setUCD("time.obs.start");
        defValues[3] = new ColumnInfo("TIME_END",String.class,"End time of data in the file  ");
        defValues[3].setUCD("time.obs.end");
        defValues[4] = new ColumnInfo("DATA_ID",String.class,"Short description of the measurement");
        defValues[4].setUCD("meta.title");        
        defValues[5] = new ColumnInfo("INSTRUMENT_ID",String.class,"A string specifiying the mission and instrument");
        defValues[5].setUCD("INST_ID");        
        defValues[6] = new ColumnInfo("DESCRIPTION",String.class,"A string containing supplemental information on the data file");
        defValues[6].setUCD("meta");
        defValues[7] = new ColumnInfo("DESCRIPTION_URL",String.class,"A URL pointing to information on the data product");
        defValues[7].setUCD("meta.ref.url");
        astro = new RowListStarTable( defValues );
        values = new Object[8];
    }
    
    public void setAccessReference(String accessReference) {
        values[0] = accessReference;
    }
    
    public void setProvider(String provider) {
        values[1] = provider;
    }
    
    public void setTimeStart(String timeStart) {
        values[2] = timeStart;
    }
    
    public void setTimeEnd(String timeEnd) {
        values[3] = timeEnd;
    }
    
    public void setDataID(String dataID) {
        values[4] = dataID; //label
    }
    
    public void setInstrumentID(String instrumentID) {
        values[5] = instrumentID;
    }
    
    public void setDescription(String description) {
        values[6] = description;
    }
    
    public void setDescriptionURL(String descriptionURL) {
        values[7] = descriptionURL;
    }
    
    public void addRow() {
        astro.addRow( values );
    }
    
    public void writeBeginVOTable(BufferedWriter out, String description) throws IOException {
         out.write( "<VOTABLE version='1.1'>\n" );
         out.write( "<RESOURCE>\n" );
         out.write( "<DESCRIPTION>" + description + "</DESCRIPTION>\n" );
    }
    
    public void writeTable(BufferedWriter out) throws IOException {
        VOSerializer vos = VOSerializer.makeSerializer( DataFormat.TABLEDATA, astro );
        vos.writeInlineTableElement(out);
        astro.clearRows();
        vos = null;
    }
    
    public void writeEndVOTable(BufferedWriter out) throws IOException {
        out.write( "</RESOURCE>\n" );
        out.write( "</VOTABLE>\n" );
        out.close();
    }
    
}