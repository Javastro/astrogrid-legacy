package org.astrogrid.stapsearch.impl.cdaw;

import org.astrogrid.stapsearch.IStapSearch;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.io.PrintWriter;
import java.util.TimeZone;
import java.util.Calendar;
import java.text.SimpleDateFormat;
import java.io.BufferedWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Set;

import org.astrogrid.tools.votable.stap.v0_1.STAPMaker;
import org.astrogrid.config.Config;

import org.astrogrid.stapsearch.ws.cdaw.ViewDescription;
import org.astrogrid.stapsearch.ws.cdaw.DatasetDescription;
import org.astrogrid.stapsearch.ws.cdaw.ResultDescription;
import org.astrogrid.stapsearch.ws.cdaw.FileDescription;



import java.io.IOException;

public class SolarSearch implements IStapSearch {
    
    /**
     * conf - Config variable to access the configuration for the server normally
     * jndi to a config file.
     * @see org.astrogrid.config.Config
     */   
    public static Config conf = null;
    
    private HashMap noDuplMap = null;
    
    /**
     * Static to be used on the initiatian of this class for the config
     */   
    static {
       if(conf == null) {
          conf = org.astrogrid.config.SimpleConfig.getSingleton();
       }
    }       
    
    public SolarSearch() {
        noDuplMap = new HashMap();
    }
    
    private void printMap(Map info) {
        Set st = info.keySet();
        Object []keyarray = st.toArray();
        for(int i = 0;i < keyarray.length;i++) {
            System.out.println("key = " + keyarray[i] + " val = " + ((String [])info.get(keyarray[i]))[0]  );
        }//for
    }
    
    
    public void execute(Calendar startTime, Calendar endTime, Map info, PrintWriter output) throws IOException {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        
        String []missionName = new String[1];
        //printMap(info);
        if(!info.containsKey("mission")) {
            output.print("could not find mission");
        }
        String formatReq = null;
        if(info.containsKey("FORMAT")) {
            formatReq = ((String [])info.get("FORMAT"))[0];
        }
        
        missionName[0] = ((String [])info.get("mission"))[0];
        
        STAPMaker stapMaker = new STAPMaker();
        BufferedWriter out =
            new BufferedWriter( output );

        try {
        stapMaker.writeBeginVOTable(out,missionName[0]);
        
        
        Calendar startTimeCal = Calendar.getInstance();
        Calendar endTimeCal = Calendar.getInstance();
        
        org.astrogrid.stapsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub binding;
        try {
            binding = (org.astrogrid.stapsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                          new org.astrogrid.stapsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort();
        }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
            throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
        }
        
        ViewDescription[] value = null;        
        ViewDescription[] views = binding.getAllViewDescriptions();

        Calendar startTemp = Calendar.getInstance();
        Calendar endTemp = Calendar.getInstance();
        String tempFormat = null;
        for(int i = 0;i < views.length;i++) {
            //System.out.println("the title = " + views[i].getTitle() + " id = " + views[i].getId());
            if(views[i].isPublicAccess() && views[i].getId().equals("sp_phys")) {
               //System.out.println("getting mission groups from view = " + views[i].getTitle());
               try {
                   binding = (org.astrogrid.stapsearch.ws.cdaw.CoordinatedDataAnalysisSystemBindingStub)
                                 new org.astrogrid.stapsearch.ws.cdaw.CDASWSLocator().getCoordinatedDataAnalysisSystemPort(new URL(views[i].getEndpointAddress()));
               }
               catch (javax.xml.rpc.ServiceException jre) {
                   if(jre.getLinkedCause()!=null)
                       jre.getLinkedCause().printStackTrace();
                   throw new junit.framework.AssertionFailedError("JAX-RPC ServiceException caught: " + jre);
               }               
               
               String []missionGroups = binding.getAllMissionGroups();
               String [][]instruments = binding.getAllInstruments();
               /*
               System.out.println("instruments length = " + instruments.length);
               for(int r = 0;r < instruments.length;r++) {
                   System.out.println("short name = " + instruments[r][0] + " long name = " + instruments[r][1]);                   
               }
               String []instrumentType = binding.getAllInstrumentTypes();
               
               System.out.println("instrument types length = " + instrumentType.length);
               for(int r = 0;r < instrumentType.length;r++) {
                   System.out.println("instrument type = " + instrumentType[r]);                   
               } 
               */              
               for(int j = 0;j < missionGroups.length;j++) {
                  //System.out.println("comparing " + missionName[0].toUpperCase() + " with " + missionGroups[j].toUpperCase());
                  if(missionName[0].toUpperCase().equals(missionGroups[j].toUpperCase())) {
                     DatasetDescription []dsd = binding.getDatasets(missionName, new String[0]);
                     //noDuplMap.clear();
                     for(int k = 0;k < dsd.length;k++) {
                         if(!noDuplMap.containsKey(dsd[k].getId())) {
                             noDuplMap.put(dsd[k].getId(),null);
                                        if(startTime.before(dsd[k].getStartTime())) {
                                           System.out.println("WARNING: Your given start time is before the dataset start time; using dataset start time");
                                           startTemp = dsd[k].getStartTime();
                                        }else {
                                            startTemp = startTime;
                                        }
                                        if(endTime.after(dsd[k].getEndTime())) {
                                           System.out.println("WARNING: Your given end time is after the datasets end time; using the dataset end time");
                                           endTemp = dsd[k].getEndTime();
                                        }else {
                                            endTemp = endTime;
                                        }
                                        //System.out.println("startTemp = " + dateFormat.format(startTemp.getTime()) + " and endtemp = " + dateFormat.format(endTemp.getTime()));
                                        //String []dsURLS = binding.getDataUrls(dsd[k].getId(),startTemp,endTemp);
                                        //printDataSet(dsd[k]);
                                        FileDescription []fds = binding.getDataFiles(dsd[k].getId(),startTemp,endTemp);
                                        //printFileDescriptions(fds);
                                        if(fds != null && fds.length > 0) {
                                           //System.out.println("got this many dsURLS = " + dsURLS.length + " dsd k = " + k + " and label = " + dsd[k].getLabel());
                                           for(int m = 0;m < fds.length;m++) {
                                              //System.out.println("the url2 = " + dsURLS[m]);
                                              if(correctFormat(formatReq,fds[m].getName()) && !noDuplMap.containsKey(fds[m].getName() + dsd[k].getId() )) {
                                                  noDuplMap.put(fds[m].getName() + dsd[k].getId(),null);
                                                  stapMaker.setDataID(dsd[k].getLabel());
                                                  stapMaker.setTimeStart(dateFormat.format(fds[m].getStartTime().getTime()));
                                                  stapMaker.setTimeEnd(dateFormat.format(fds[m].getEndTime().getTime()));
                                                  stapMaker.setAccessReference(fds[m].getName());
                                                  stapMaker.setProvider("CDAW");
                                                  stapMaker.setDescription(dsd[k].getLabel());
                                                  stapMaker.setInstrumentID(dsd[k].getLabel());
                                                  if(dsd[k].getNotesUrl() != null && dsd[k].getNotesUrl().trim().length() > 0) {
                                                      stapMaker.setDescriptionURL(dsd[k].getNotesUrl());
                                                  }//if  
                
                                                  tempFormat = DEFAULT_FORMAT;
                                                  if(fds[m].getName().lastIndexOf('.') != -1) {
                                                      //System.out.println(" prop name = " + "format.ending." + dsURLS[m].substring(dsURLS[m].lastIndexOf('.')+1) );
                                                      tempFormat = conf.getString("format.ending." + fds[m].getName().substring(fds[m].getName().lastIndexOf('.')+1), tempFormat);
                                                      //System.out.println(" val from config = " + tempFormat);
                                                  }
                                                  stapMaker.setFormat(tempFormat);                                  
                                                  stapMaker.addRow();
                                              }
                                           }
                                           if(stapMaker.getRowCount() > 0) {
                                               stapMaker.writeTable(out);
                                               //output.flush();
                                           }
                                        }//if
                                     }//for
                                  }//if
                  }//if
               }//for
            }else {
               //System.out.println("Skipping analyzing missions from this view, no public access title = " + views[i].getTitle());
            }
        }//for
        }finally {
            stapMaker.writeEndVOTable(out);
        }
    }
    
    private void printFileDescriptions(FileDescription []fds) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
        "yyyy-MM-dd'T'HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));        
        for(int j = 0;j < fds.length;j++) {
            System.out.println(" j = " + j + " name = " + fds[j].getName() + " Start Time = " + 
                    dateFormat.format(fds[j].getStartTime().getTime()) + " end time = " + dateFormat.format(fds[j].getEndTime().getTime()) + " mime type = " + fds[j].getMimeType() + " mod time = " +  dateFormat.format(fds[j].getLastModified().getTime()) + " and length = " + fds[j].getLength());
        }
    }
    
    private static final String DEFAULT_FORMAT = "TIME_SERIES";
    
    private boolean correctFormat(String format, String accessRefExtension) {
        boolean timeSeries = false;
        boolean graphics = false;
        if(format == null || format.trim().length() == 0) {
            return true;
        }
        if(accessRefExtension.lastIndexOf('.') != -1)
            accessRefExtension = accessRefExtension.substring(accessRefExtension.lastIndexOf('.')+1);
        
        if((accessRefExtension.equalsIgnoreCase("fits") || accessRefExtension.equalsIgnoreCase("jpg") ||
            accessRefExtension.equalsIgnoreCase("gif") || accessRefExtension.equalsIgnoreCase("fts"))) {
            graphics = true;
        }
        
        if((accessRefExtension.equalsIgnoreCase("cdf") || accessRefExtension.equalsIgnoreCase("txt") ||
                 accessRefExtension.equalsIgnoreCase("vot"))) {
            timeSeries = true;
        }
        if(format.equalsIgnoreCase("TIME_SERIES") && timeSeries)
            return true;
        else if(format.equalsIgnoreCase("GRAPHICS") && graphics)
            return true;
        else if(!timeSeries && !graphics && format.equalsIgnoreCase(DEFAULT_FORMAT))
            return true;

        return false;
    }
    
    private static void printDataSet(DatasetDescription dsd) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        dateFormat.setTimeZone(TimeZone.getTimeZone("GMT+00:00"));
        System.out.println("Begin - printing DataSet Information ...");
        System.out.println("Label = " + dsd.getLabel() + " id = " + dsd.getId() + " pi Affiliate = " + dsd.getPiAffiliation() + " piName = " + dsd.getPiName() + " starttime = " + dateFormat.format(dsd.getStartTime().getTime()) + " and endTime = " + dateFormat.format(dsd.getEndTime().getTime()));
        System.out.println("End of printing out dataset info for label = " + dsd.getLabel());
     }    
    
}