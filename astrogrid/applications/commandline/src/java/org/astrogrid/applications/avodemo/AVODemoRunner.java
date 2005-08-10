/*
 * $Id: AVODemoRunner.java,v 1.7 2005/08/10 14:45:37 clq2 Exp $
 * 
 * Created on 23-Jan-2004 by Paul Harrison (pah@jb.man.ac.uk)
 *
 * Copyright 2004 AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
 */

package org.astrogrid.applications.avodemo;

import org.astrogrid.applications.beans.v1.parameters.ParameterValue;
import org.astrogrid.community.beans.v1.Account;
import org.astrogrid.community.beans.v1.Credentials;
import org.astrogrid.community.beans.v1.Group;
import org.astrogrid.filemanager.client.FileManagerClient;
import org.astrogrid.filemanager.client.FileManagerClientFactory;
import org.astrogrid.filemanager.client.FileManagerNode;
import org.astrogrid.io.Piper;
import org.astrogrid.jes.types.v1.cea.axis.JobIdentifierType;
import org.astrogrid.store.Ivorn;
import org.astrogrid.workflow.beans.v1.Input;
import org.astrogrid.workflow.beans.v1.Output;
import org.astrogrid.workflow.beans.v1.Sequence;
import org.astrogrid.workflow.beans.v1.Step;
import org.astrogrid.workflow.beans.v1.Tool;
import org.astrogrid.workflow.beans.v1.Workflow;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

import javax.mail.MessagingException;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;

/**
 * @author Paul Harrison (pah@jb.man.ac.uk)
 * @version $Name:  $
 * @since iteration4.1
 * @deprecated this was a quick fix for the demo - not the way to do things nowadays...
 * @bugzilla 
 */
public class AVODemoRunner implements Runnable {

private JobIdentifierType jobstepid = new JobIdentifierType();
   org.astrogrid.community.User fulluser =
      new org.astrogrid.community.User(AVODemoConstants.ACCOUNT, AVODemoConstants.GROUP, AVODemoConstants.TOKEN);
   private final String MYSPACEBASECONT = "ivo://"+AVODemoConstants.MYSPACE +"#" + fulluser.getUserId() + "/";
   private String toAddress = "pah@jb.man.ac.uk";
   private final String fromAddress = "pah@jb.man.ac.uk";
   private Credentials credentials = new Credentials();
   // TODO need to fill these in with something...

   private InternetAddress ccdests[];

   private String myspaceBaseRef;
   private String sector = "sect23";
   private String hemi = "s";
   private String myspacebase;
   private String username;
   static private org.apache.commons.logging.Log logger =
      org.apache.commons.logging.LogFactory.getLog(AVODemoRunner.class);
private String workflowIvorn;
   
   /**
    * 
    */
   public AVODemoRunner() {

      myspacebase = MYSPACEBASECONT;
      try {
         ccdests = new InternetAddress[] { new InternetAddress("pah@jb.man.ac.uk")};
      }
      catch (AddressException e) {
         // TODO Auto-generated catch block
         e.printStackTrace();
      }

   }
   public static void main(String[] args) {

      AVODemoRunner demo = new AVODemoRunner();
      demo.runit();

   }

   public void runit() {

      Thread runthread = new Thread(this);
      runthread.start();

   }

   public void createWorkflow() throws Exception {
      Group group = null;
      FileManagerClient myspacedel = (new FileManagerClientFactory()).login(new Ivorn(fulluser.getAccount()),fulluser.getToken()); //unsure whether this is correct.
      //VoSpaceClient myspacedel = new VoSpaceClient(fulluser);
      StringBuffer msbr = new StringBuffer("ivo://");
      msbr.append(AVODemoConstants.MYSPACE);
      msbr.append("#");
      msbr.append(username);
      msbr.append("/");
      myspacebase = msbr.toString(); // take the base from here...
      msbr.append(sector);
      msbr.append(hemi);
      myspaceBaseRef = msbr.toString();
      FileManagerNode root;
      try {
          root = myspacedel.createFolder(new Ivorn(myspaceBaseRef));
         //myspacedel.newFolder(new Ivorn(myspaceBaseRef));
      }
      catch (Exception e2) {
         // TODO Auto-generated catch block
         e2.printStackTrace();
         //must be there already..
         root = myspacedel.node(new Ivorn(myspaceBaseRef));
      }
      msbr.append("/");
      myspaceBaseRef = msbr.toString();

      Workflow workflow = new Workflow();
      workflow.setName("AVODEMO-" + sector + hemi);
      workflow.setDescription(
         "Workflow involving multi-band sextractor run followed by a data federation and hyperz run");
         Credentials cred = new Credentials();
         Account acc = new Account();
         acc.setName(username); //FIXME this is a quick hack to get the username into the right place.
         acc.setCommunity(fulluser.getCommunity());
         
        group = new Group();
        group.setName("any");
        group.setCommunity(AVODemoConstants.COMMUNITY);
        cred.setAccount(acc);
        cred.setGroup(group);
        cred.setSecurityToken("notoken");
        workflow.setCredentials(cred);
       
      Sequence sequence = new Sequence();
      workflow.setSequence(sequence);
      Step step = new Step();
      populateSExtractorStep(step, "b");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "v");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "i");
      sequence.addActivity(step);

      step = new Step();
      populateSExtractorStep(step, "z");
      sequence.addActivity(step);

      step = new Step();
      populateDftStep(step);
      sequence.addActivity(step);

      step = new Step();
      populateHyperZStep(step);
      sequence.addActivity(step);
      
     

      StringWriter outstr = new StringWriter();

      workflow.marshal(outstr);
      byte[] bytes = outstr.toString().getBytes();
      workflowIvorn = myspacebase + "workflow/" + workflow.getName();
      //myspacedel.putBytes(bytes, 0, bytes.length, new Ivorn(workflowIvorn), false);
      FileManagerNode output = root.addFile(workflow.getName());
      OutputStream os = output.writeContent();
      InputStream is = new ByteArrayInputStream(bytes);
      Piper.pipe(is,os);
      is.close();
      os.close();
 
   }
   private void runworkflow() throws Exception {
       FileManagerClient myspacedel = (new FileManagerClientFactory()).login(new Ivorn(fulluser.getAccount()),fulluser.getToken()); //unsure whether this is correct.       
      //VoSpaceClient myspacedel = new VoSpaceClient(fulluser);
      myspaceBaseRef = myspacebase + sector + hemi;
      FileManagerNode folder = myspacedel.createFolder(new Ivorn(myspaceBaseRef));

      String exid;
      exid = runSExtractor("b");
      waitForCompletion(exid);
      exid = runSExtractor("v");
      waitForCompletion(exid);
      exid = runSExtractor("i");
      waitForCompletion(exid);
      exid = runSExtractor("z");
      waitForCompletion(exid);
      exid = runDft();
      waitForCompletion(exid);
      exid = runHyperZ();
      waitForCompletion(exid);
      String urlout = null;
      
      FileManagerNode fout = folder.getChild("hyperzout");
      Reader in = new InputStreamReader(fout.readContent());
      Writer out = new StringWriter();
      Piper.pipe(in,out);
      
      //    StoreFile fout = myspacedel.getFile(new Ivorn(
       //     myspaceBaseRef + "hyperzout"));
            
      mailresult(out.toString());
   }

   private String runHyperZ() {
       throw new UnsupportedOperationException("direct running not supported anymore");
   }

   private void populateHyperZStep(Step step) {
      Input input = new Input();
      Output output = new Output();
      String applicationid = AVODemoConstants.HYPERZ;
      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setValue("file:///home/applications/demo/hyperz/zphot.param");
      param.setIndirect(true);

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("input_catalog");

      param.setValue(myspaceBaseRef + "merged");
      param.setIndirect(true);

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("output_catalog");

      param.setValue(myspaceBaseRef + "hyperzout");
      param.setIndirect(true);
      step.setTool(tool);
      step.setName("hyperz");

   }

   private String runDft()  {
       throw new UnsupportedOperationException("direct running not supported anymore");
  }

   private void populateDftStep(Step step) {
      Output output = new Output();
      Input input = new Input();
      String applicationid = AVODemoConstants.DFT;

      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("targets");

      param.setValue(myspaceBaseRef + "sexout_z");
      param.setIndirect(true);

      param = new ParameterValue();
      param.setName("matches");
      input.addParameter(param);
      param.setValue(myspaceBaseRef + "sexout_b");
      param.setIndirect(true);

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setIndirect(true);
      param.setValue(myspaceBaseRef + "sexout_v");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("matches");

      param.setIndirect(true);
      param.setValue(myspaceBaseRef + "sexout_i");

      param = new ParameterValue();
      param.setName("merged_output");
      output.addParameter(param);
      param.setIndirect(true);
      param.setValue(myspaceBaseRef + "merged");
      step.setTool(tool);
      step.setName("dft");
   }

   private String runSExtractor(String band)  {
       throw new UnsupportedOperationException("direct running not supported anymore");

   }

   private void populateSExtractorStep(Step step, String band) {
      Output output = new Output();
      Input input = new Input();
      String applicationid = AVODemoConstants.SEXTRACTOR;
      Tool tool = new Tool();
      tool.setName(applicationid);
      tool.setInput(input);
      tool.setOutput(output);
      tool.setInterface("Simple");
      ParameterValue param = new ParameterValue();
      input.addParameter(param);
      param.setName("DetectionImage");

      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/data/GOODS/h_"
            + hemi
            + "z_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PhotoImage");

      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/data/GOODS/h_"
            + hemi
            + band
            + "_"
            + sector
            + "_v1.0_drz_img.fits");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("config_file");

      param.setIndirect(true);
      param.setValue(
         "file:///home/applications/demo/h_goods_" + hemi + band + "_r1.0z_phot_sex.txt");

      param = new ParameterValue();
      input.addParameter(param);
      param.setName("PARAMETERS_NAME");

      param.setIndirect(true);
      param.setValue("file:///home/applications/demo/std.param");

      param = new ParameterValue();
      output.addParameter(param);
      param.setName("CATALOG_NAME");

      param.setIndirect(true);
      param.setValue(myspaceBaseRef + "sexout_" + band);
      
      param = new ParameterValue();
      input.addParameter(param);
      param.setName("FILTER_NAME");
      param.setValue("file:///home/applications/demo/h_goods_r1.0z_detect_conv.txt");
      param.setIndirect(true);


      step.setTool(tool);
      step.setName("sextractor - " + band);

   }

   private void waitForCompletion(String executionId) {

       //not doing this any more
//      if (executionId != null) {
//         try {
//            MessageType runStatus = controller.queryExecutionStatus(executionId);
//            try {
//               while (runStatus.getPhase() != ExecutionPhase.COMPLETED) {
//                  Thread.sleep(20000);
//                  runStatus = controller.queryExecutionStatus(executionId);
//
//               }
//
//            }
//            catch (InterruptedException e) {
//               // TODO Auto-generated catch block
//               e.printStackTrace();
//            }
//         }
//         catch (CEADelegateException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//         }
//      }
//      else {
//         logger.error("the executionid is null - application error");
//      }

   }
   private void mailresult(String url) throws MessagingException {
       //this is not really necessary any longer....
//      Session session = config.mailSessionInstance();
//      Message message = new MimeMessage(session);
//
//      InternetAddress dests[] = new InternetAddress[] { new InternetAddress(toAddress)};
//      message.setFrom(new InternetAddress(fromAddress));
//      message.setRecipients(Message.RecipientType.TO, dests);
//      message.addRecipients(Message.RecipientType.BCC, ccdests);
//      message.setSubject("AVO Demo Results for " + sector + " " + hemi);
//      if (url != null) {
//
//         message.setContent(url, "text/plain");
//      }
//      else {
//         message.setContent("problem running job", "text/plain");
//      }
//
//      Transport.send(message);
   }

   /**
    * @return
    */
   public String getHemi() {
      return hemi;
   }

   /**
    * @return
    */
   public String getSector() {
      return sector;
   }

   /**
    * @return
    */
   public String getToAddress() {
      return toAddress;
   }

   /**
    * @param string
    */
   public void setHemi(String string) {
      hemi = string;
   }

   /**
    * @param string
    */
   public void setSector(String string) {
      sector = string;
   }

   /**
    * @param string
    */
   public void setToAddress(String string) {
      toAddress = string;
   }

   /* (non-Javadoc)
    * @see java.lang.Runnable#run()
    */
   public void run() {
      try {
         runworkflow();
      }
      catch (Exception e) {
         throw new RuntimeException("problem running the job", e);
      }
   }

   /**
    * @return
    */
   public String getMyspacebase() {
      return myspacebase;
   }

   /**
    * @param string
    */
   public void setMyspacebase(String string) {
      myspacebase = string;
   }

   /**
    * @return
    */
   public String getUsername() {
      return username;
   }

   /**
    * @param string
    */
   public void setUsername(String string) {
      username = string;
   }

public String getWorkflowIvorn() {
    return workflowIvorn;
}
}
