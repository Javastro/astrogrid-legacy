/*$Id: VOSpecButton.java,v 1.5 2006/09/14 13:52:59 nw Exp $
 * Created on 03-Feb-2006
 *
 * Copyright (C) AstroGrid. All rights reserved.
 *
 * This software is published under the terms of the AstroGrid 
 * Software License version 1.2, a copy of which has been included 
 * with this distribution in the LICENSE.txt file.  
 *
**/
package org.astrogrid.desktop.modules.ui.scope;

import java.awt.Image;
import java.awt.event.ActionEvent;
import java.util.Iterator;

import javax.swing.ImageIcon;

import org.apache.commons.lang.StringUtils;
import org.astrogrid.desktop.icons.IconHelper;
import org.astrogrid.desktop.modules.ui.BackgroundWorker;
import org.astrogrid.desktop.modules.ui.UIComponent;

import edu.berkeley.guir.prefuse.event.FocusEvent;
import edu.berkeley.guir.prefuse.focus.FocusSet;
import edu.berkeley.guir.prefuse.graph.TreeNode;
import esavo.vospec.spectrum.Spectrum;
import esavo.vospec.spectrum.SpectrumSet;
import esavo.vospec.spectrum.Unit;
import esavo.vospec.standalone.VoSpec;
/** @todo unused. remove soon.
 * node consumer that displays selected spectra in vospec.
 some services aren't returning the right types - need to check type cast to double[][ in SsapRetrieval.
 * @author Noel Winstanley nw@jb.man.ac.uk 03-Feb-2006
 *
 */
public class VOSpecButton extends NodeConsumerButton {

  
    public VOSpecButton(FocusSet selectedNodes,UIComponent ui) {
        super("View spectra in VOSpec", "Launch VOSpec to display spectra", selectedNodes);
        ImageIcon orig = IconHelper.loadIcon("vologo.gif");
        ImageIcon scaled = new ImageIcon(orig.getImage().getScaledInstance(-1,50,Image.SCALE_SMOOTH));
        this.setIcon(scaled);
        this.ui= ui;
    }
    private final UIComponent ui;

    public void focusChanged(FocusEvent arg0) {
        for (Iterator i = selectedNodes.iterator(); i.hasNext(); ) {
            TreeNode tn = (TreeNode)i.next();
            String type = tn.getAttribute(Retriever.SERVICE_TYPE_ATTRIBUTE);
          if (type != null && type.equals( SsapRetrieval.SSAP)) {
              setEnabled(true);
              return;
          }
        }
        setEnabled(false);
    }
    
    private VoSpec vospec;
    private synchronized VoSpec getVoSpecInstance() {
        if (vospec == null){
            vospec = new VoSpec();
        }
        return vospec;
    }

    public void actionPerformed(ActionEvent e) {
        final VoSpec vospec = getVoSpecInstance();
        vospec.show(); // jesus advises that it's best to show first, populate after.
        
        (new BackgroundWorker(ui,"Displaying Spectra") {
            protected Object construct() throws Exception {
                // know we've got some spectra selected, otherwise this button wouldn't be enabled.
                SpectrumSet spectrumSet = new SpectrumSet();
                int ix = 0;
                for (Iterator i = selectedNodes.iterator(); i.hasNext();) {
                    final TreeNode tn = (TreeNode)i.next();
                    // find leaf nodes that are spectrum..
                    String type = tn.getAttribute(Retriever.SERVICE_TYPE_ATTRIBUTE);
                    if (type == null || ! type.equals(SsapRetrieval.SSAP) ||tn.getChildCount() > 0 ) {
                        continue;
                    }
                    Spectrum s = new Spectrum();
                    s.setUrl(tn.getAttribute(SsapRetrieval.SPECTRA_URL_ATTRIBUTE));
                    final String cols = tn.getAttribute(SsapRetrieval.SPECTRA_AXES_ATTRIBUTE).trim();
                    if (cols != null) { // may not be there.
                        String[] colNames = StringUtils.split(cols,' ');
                        if (colNames.length > 0) {
                            s.setWaveLengthColumnName(colNames[0]);
                        }
                        if (colNames.length > 1) {
                            s.setFluxColumnName(colNames[1]);
                        }
                    }
                    String dimeq = tn.getAttribute(SsapRetrieval.SPECTRA_DIMEQ_ATTRIBUTE).trim();
                    String scaleq = tn.getAttribute(SsapRetrieval.SPECTRA_SCALEQ_ATTRIBUTE).trim();
                    if (dimeq != null && scaleq != null) {
                        String[] dims = StringUtils.split(dimeq,' ');
                        String[] scales = StringUtils.split(scaleq,' ');
                        s.setUnits(new Unit(dims[0],scales[0],dims[1],scales[1]));
                    }
                    s.setTitle(tn.getAttribute(SsapRetrieval.SPECTRA_TITLE_ATTRIBUTE).trim());
                    s.setRa(tn.getAttribute(SsapRetrieval.RA_ATTRIBUTE).trim());
                    s.setDec(tn.getAttribute(SsapRetrieval.DEC_ATTRIBUTE).trim());
                    s.setFormat(tn.getAttribute(SsapRetrieval.SPECTRA_FORMAT_ATTRIBUTE).trim());
                    
                    spectrumSet.addSpectrum(ix++,s);
                }
                vospec.loadSpectrumSet("",spectrumSet);
                return null;
            }          
        }).start();

    }

}


/* 
$Log: VOSpecButton.java,v $
Revision 1.5  2006/09/14 13:52:59  nw
implemented plastic spectrum messaging.

Revision 1.4  2006/06/27 10:19:06  nw
doc change

Revision 1.3  2006/05/17 23:58:49  nw
improvements from jonathan and kevin's feedback.

Revision 1.2  2006/04/18 23:25:44  nw
merged asr development.

Revision 1.1.2.1  2006/04/14 02:45:01  nw
finished code.extruded plastic hub.

Revision 1.1  2006/02/24 15:26:53  nw
build framework for dynamically adding buttons

Revision 1.1  2006/02/09 15:40:01  nw
finished refactoring of astroscope.
added vospec viewer
 
*/