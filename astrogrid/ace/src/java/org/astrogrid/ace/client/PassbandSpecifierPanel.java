/**
 * $Id: PassbandSpecifierPanel.java,v 1.1 2003/08/25 18:36:06 mch Exp $
 *
 * @author M Hill
 */

package org.astrogrid.ace.client;

import java.awt.*;
import javax.swing.*;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.astrogrid.intensity.Passband;
import org.astrogrid.ui.GridBagHelper;
import org.astrogrid.ui.JDoubleField;
import org.astrogrid.ui.JIntegerField;

/**
 * A UI panel so that the user can specify how the intensity is measured
 *
 */

public class PassbandSpecifierPanel extends JPanel implements ActionListener
{
   private ButtonGroup catalogButtonGp = new ButtonGroup();
   public JRadioButton ignoreFluxBtn = new JRadioButton("Ignore");
   public JRadioButton genericMagBtn = new JRadioButton("Generic Mag");
   public JRadioButton eisSingleBandBtn = new JRadioButton("EIS Single Mag");
   public JRadioButton eisCrossMatchedBtn = new JRadioButton("EIS X-Matched Mag");
   public JRadioButton physicalBtn = new JRadioButton("Physical Units");

   public JComboBox genericBandPicker = new JComboBox(new Object[] {
               Passband.U,
               Passband.B,
               Passband.V,
               Passband.R,
               Passband.I,
               Passband.J,
               Passband.H,
               Passband.K
         });

   public JComboBox eisSingleBandPicker = new JComboBox(new Object[] {
               Passband.U,
               Passband.B,
               Passband.V,
               Passband.R,
               Passband.I,
         });

   public JComboBox eisCrossBandPicker = new JComboBox(new Object[] {
               Passband.U,
               Passband.B,
               Passband.V,
               Passband.R,
               Passband.I,
               Passband.J,
               Passband.K
         });

   private JPanel blankPanel = new JPanel();
   private    JPanel genericPanel = new JPanel();
   private JPanel eisSinglePanel = new JPanel();
   private    JPanel eisCrossPanel = new JPanel();
   private    JPanel physicalPanel = new JPanel();
   private    CardLayout cardLayout = new CardLayout();
   private    JPanel detailPanel = new JPanel(cardLayout);

   public JDoubleField obsFreqField = new JDoubleField();
   public JDoubleField filterFwhmField = new JDoubleField();

   private static String PANELID_IGNORE = "I";
   private static String PANELID_GENERIC = "G";
   private static String PANELID_EISSINGLE = "S";
   private static String PANELID_EISCROSS = "C";
   private static String PANELID_PHYSICAL = "P";

   public PassbandSpecifierPanel()
   {
      super(new BorderLayout(0,10));
      initComponents();
   }

   private void initComponents()
   {
      JPanel buttonPanel = new JPanel(new GridLayout(0,2));
      add(buttonPanel, BorderLayout.WEST);

      buttonPanel.add(ignoreFluxBtn);
      catalogButtonGp.add(ignoreFluxBtn);
      ignoreFluxBtn.setToolTipText("Makes no changes to output columns");

      buttonPanel.add(genericMagBtn);
      catalogButtonGp.add(genericMagBtn);
      genericMagBtn.setToolTipText("Vega/Johnson Magnitudes");

      buttonPanel.add(eisSingleBandBtn);
      catalogButtonGp.add(eisSingleBandBtn);
      eisSingleBandBtn.setToolTipText("EIS Single Catalogue Magnitudes");

      buttonPanel.add(eisCrossMatchedBtn);
      catalogButtonGp.add(eisCrossMatchedBtn);
      eisCrossMatchedBtn.setToolTipText("EIS Crossmatched Catalogue Magnitudes");

      buttonPanel.add(physicalBtn);
      catalogButtonGp.add(physicalBtn);
      physicalBtn.setToolTipText("Janskys");

      add(detailPanel, BorderLayout.CENTER);

      detailPanel.add(blankPanel, PANELID_IGNORE);

      genericPanel.add(new JLabel("Generic Passband"));
      genericPanel.add(genericBandPicker);
      detailPanel.add(genericPanel, PANELID_GENERIC);

      eisSinglePanel.add(new JLabel("EIS (Single) Passband"));
      eisSinglePanel.add(eisSingleBandPicker);
      detailPanel.add(eisSinglePanel, PANELID_EISSINGLE);

      eisCrossPanel.add(new JLabel("EIS (Crossmatched) Passband"));
      eisCrossPanel.add(eisCrossBandPicker);
      detailPanel.add(eisCrossPanel, PANELID_EISCROSS);

      physicalPanel.setLayout(new GridBagLayout());
      GridBagConstraints constraints = new GridBagConstraints();
      constraints.insets = new Insets(2,4,2,4);
      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      physicalPanel.add(new JLabel("Obs Frequency", JLabel.RIGHT), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      physicalPanel.add(obsFreqField, constraints);
      obsFreqField.setToolTipText("Frequency objects were observed at. [OBS_FREQUENCY]");
      GridBagHelper.setControlConstraints(constraints);
      physicalPanel.add(new JLabel("Hz", JLabel.LEFT), constraints);

      constraints.gridy++;
      GridBagHelper.setLabelConstraints(constraints);
      physicalPanel.add(new JLabel("Filter FWHM", JLabel.RIGHT), constraints);
      GridBagHelper.setEntryConstraints(constraints);
      physicalPanel.add(filterFwhmField, constraints);
      filterFwhmField.setToolTipText("Filter passband width at half maximum [INST_FILTER_FWHM]");
      GridBagHelper.setControlConstraints(constraints);
      physicalPanel.add(new JLabel("Hz", JLabel.LEFT), constraints);

      JPanel northPanel = new JPanel(new BorderLayout());
      northPanel.add(physicalPanel, BorderLayout.NORTH);

      detailPanel.add(northPanel, PANELID_PHYSICAL);

      genericPanel.show();

      //add actions to buttons so they bring to the top the correct detail
      //panel
      ignoreFluxBtn.addActionListener(this);
      genericMagBtn.addActionListener(this);
      eisSingleBandBtn.addActionListener(this);
      eisCrossMatchedBtn.addActionListener(this);
      physicalBtn.addActionListener(this);

      ignoreFluxBtn.doClick(); //default
   }

   public void actionPerformed(ActionEvent event)
   {
      if (ignoreFluxBtn.isSelected())
         cardLayout.show(detailPanel, PANELID_IGNORE);
      else if (genericMagBtn.isSelected())
         cardLayout.show(detailPanel, PANELID_GENERIC);
      else if (eisSingleBandBtn.isSelected())
         cardLayout.show(detailPanel, PANELID_EISSINGLE);
      else if (eisCrossMatchedBtn.isSelected())
         cardLayout.show(detailPanel, PANELID_EISCROSS);
      else if (physicalBtn.isSelected())
         cardLayout.show(detailPanel, PANELID_PHYSICAL);
   }

   /**
    * Returns two strings: the first is the 'name' of the column, the
    * second is the ucd; if physical units are selected, two more are returned -
   * the obs frequency and filter fwhm
    */
   public String[] getIds()
   {
      if (ignoreFluxBtn.isSelected())
      {
         String[] ids = new String[2];
         ids[0]= "";
         ids[1] = "";
         return ids;
      }
      else if (genericMagBtn.isSelected())
      {
         String[] ids = new String[2];

         ids[0] = ((Passband) genericBandPicker.getSelectedItem()).getText();
         ids[1] = "PHOT_MAG_"+ids[0];
         return ids;
      }
      else if (eisSingleBandBtn.isSelected())
      {
         String[] ids = new String[2];

         ids[0] = "Mtot";
         ids[1] = "PHOT_INT_"+((Passband) eisSingleBandPicker.getSelectedItem()).getText();
         return ids;
      }
      else if (eisCrossMatchedBtn.isSelected())
      {
         String[] ids = new String[2];
         Passband pickedBand = ((Passband) eisCrossBandPicker.getSelectedItem());

         ids[0] = pickedBand.getText()+"mag";
         if ((pickedBand == Passband.R) || (pickedBand == Passband.I))
         {
            ids[1] = "PHOT_COUS_"+pickedBand.getText();
         }
         else
         {
            ids[1] = "PHOT_JHN_"+pickedBand.getText();
         }
         return ids;
      }
      else if (physicalBtn.isSelected())
      {
         String[] ids = new String[4];

         ids[0] = "Flux";
         ids[1] = "PHOT_FLUX_DENSITY";
         if (obsFreqField.getValue() == 0)
         {
            ids[2] = "";
         }
         else
         {
            ids[2] = ""+obsFreqField.getValue();
         }
         if (filterFwhmField.getValue() == 0)
         {
            ids[3] = "";
         }
         else
         {
            ids[3] = ""+filterFwhmField.getValue();
         }
         return ids;
      }
      else
      {
         throw new RuntimeException("No button selected");
      }
   }

   /**
    * Given Ids (say from a config file), works out what the user input
    * was and selects it
    */
   public void setFromIds(String[] ids)
   {
      if ((ids.length<2))  return; //old fashioned UCD from v0.2

      if (ids[1].length()==0)
      {
         ignoreFluxBtn.doClick();
         return;
      }

      String lastId1Ltr = ids[1].substring(ids[1].length()-1);

      if (ids[1].startsWith("PHOT_MAG"))
      {
         genericMagBtn.doClick();
         genericBandPicker.setSelectedItem(Passband.getFor(Passband.class, lastId1Ltr));
      }
      else if (ids[1].startsWith("PHOT_INT"))
      {
         eisSingleBandBtn.doClick();
         eisSingleBandPicker.setSelectedItem(Passband.getFor(Passband.class, lastId1Ltr));
      }
      else if (ids[1].startsWith("PHOT_JHN") || ids[1].startsWith("PHOT_COUS"))
      {
         eisCrossMatchedBtn.doClick();
         eisCrossBandPicker.setSelectedItem(Passband.getFor(Passband.class, lastId1Ltr));
      }
      else if (ids[1].startsWith("PHOT_FLUX"))
      {
         physicalBtn.doClick();
         if ((ids[2] != null)  && (ids[2].length() >0))
         {
            obsFreqField.setText(ids[2]);
         }
         if ((ids[3] != null)  && (ids[3].length() >0))
         {
            filterFwhmField.setText(ids[3]);
         }
      }
      else
      {
         throw new IllegalArgumentException("Could not interpret IDs: "+ids);
      }
   }


   /**
    * Rreturns the selected passband - throws exception if physical units
    * are selected, though could probably make up a passband instance
    * instead.  Need a gaussian passband for that.
    */
   public Passband getSelectedPassband()
   {
      if (genericMagBtn.isSelected())
      {
         return ((Passband) genericBandPicker.getSelectedItem());
      }
      else if (eisSingleBandBtn.isSelected())
      {
         return ((Passband) eisSingleBandPicker.getSelectedItem());
      }
      else if (eisCrossMatchedBtn.isSelected())
      {
         return ((Passband) eisCrossBandPicker.getSelectedItem());
      }
      else if (physicalBtn.isSelected())
      {
         throw new RuntimeException("No passband selected");
      }

      throw new RuntimeException("no button selected");

   }

   /**
    * Returns whether physical units were selected
    */
   public boolean isPhysicalSelected()
   {
      return physicalBtn.isSelected();
   }

   /**
    * Returns whether magnitude passbands were selected
    */
   public boolean isMagnitudeSelected()
   {
      return   genericMagBtn.isSelected() ||
               eisCrossMatchedBtn.isSelected() ||
               eisSingleBandBtn.isSelected();
   }
   /**
    * Returns the passband frequency entered by the user or given by the
    * selected 'standard' passband
    */
   public double getPassbandFreq()
   {
      if (isPhysicalSelected())
      {
         //get from user entry
         return obsFreqField.getValue();
      }
      else
      {
         //get from passband
         return getSelectedPassband().getCentralFrequency();
      }

   }

   /**
    * Returns the filter full-width at half-maximum, as enetered by the user
    * throws exception if physical units weren't selected
    */
   public double getFilterFwhm()
   {
      if (isPhysicalSelected())
      {
         return filterFwhmField.getValue();
      }
      else
      {
         //we have no fwhms for the standard passbands at the mo
         throw new UnsupportedOperationException("No passband selected");
      }

   }

   /**
    * test harness
    */

    public static void main(String[] args)
    {
    JDialog dialog = new JDialog( (Frame) null, "Intensity Test Panel", true);

    dialog.setSize(480,520);

      PassbandSpecifierPanel isp = new PassbandSpecifierPanel();

    dialog.getContentPane().add(isp);

    dialog.setVisible(true);

      String[] results = isp.getIds();

      System.out.println(results[0]+", "+results[1]);
      System.out.println("e_"+results[0]+", ERROR");

      if (isp.isPhysicalSelected())
      {
         System.out.println("In description:");
         System.out.println("OBS_FREQUENCY="+isp.getPassbandFreq());
         System.out.println("INST_FILTER_FWHM="+isp.getPassbandFreq());
      }
    };


}
/*
$Log: PassbandSpecifierPanel.java,v $
Revision 1.1  2003/08/25 18:36:06  mch
*** empty log message ***

Revision 1.4  2003/07/03 18:10:31  mch
Obs freq now double for large values, and fix for not overwriting obs freq with blank values from file

Revision 1.3  2003/07/02 19:19:59  mch
Fixes for ignore button

Revision 1.2  2003/06/26 19:25:59  mch
Added Hz labels

Revision 1.1  2003/06/26 19:14:59  mch
Passband stuff added

 */
