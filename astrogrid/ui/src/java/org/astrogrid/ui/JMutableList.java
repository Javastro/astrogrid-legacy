package org.astrogrid.ui;

import javax.swing.*;

public class JMutableList extends JList
{
  public JMutableList() {
    super(new DefaultListModel());
  };

  public DefaultListModel getContents() {
    return (DefaultListModel) getModel();
  };
}
