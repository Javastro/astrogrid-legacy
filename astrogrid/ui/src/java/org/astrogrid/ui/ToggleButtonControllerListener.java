package org.astrogrid.ui;

import java.util.*;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class ToggleButtonControllerListener implements ActionListener
{
  Vector components = new Vector();

  public void addComponent(JComponent aComponent)
  {
    components.add(aComponent);
  };

  public void actionPerformed(ActionEvent e)
  {
    boolean enabledState = false;
    int componentLoop = 0;

    if (e.getSource() instanceof JToggleButton) 
    {
      if ( ((JToggleButton) e.getSource()).isSelected() )
      {
        enabledState = true;
      };

      for (componentLoop = 0; componentLoop < components.size() ; componentLoop++ )
      {
        JComponent tempComponent = 
          (JComponent) components.elementAt(componentLoop);

        tempComponent.setEnabled(enabledState);
        tempComponent.invalidate();
      }
    }
  };
};
