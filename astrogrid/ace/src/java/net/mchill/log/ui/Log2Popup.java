package net.mchill.log.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import net.mchill.log.*;
import java.text.SimpleDateFormat;

/**
 * A log handler that displays a pop up dialog box to show
 * log events. Defaults to filtering only info
 * messages and above.
 * <P>Borrows look & feel from JOptionPane.
 * <P>Can show icons, if given as ./images/<severity> from the
 * location of this class, or will take them from JOptionPane
 * if not supplied.
 * <P>To do:
 * <LI>Listen to resizing events, storing height for visible and invisible details
 * so it is 'remembered' between hiding/showing
 *
 * @Created       Dec 2001
 * @Last Update   Dec 2001
 *
 * @author       M Hill
 */

public class Log2Popup extends JDialog implements FilteredHandler, ActionListener
{
   //keep reference to even itself so we can display in different ways
   LogEvent event = null;

   FilteredHandlerAdaptor filterAdaptor = new FilteredHandlerAdaptor();

   JTextPane userMessageArea = new JTextPane();
   JLabel iconArea = new JLabel();
   JButton continueButton = new JButton("Continue");
   JButton detailButton = new JButton("Detail ->");
   JTextPane detailArea = new JTextPane();
   JComponent detailPanel;
   JPanel userPanel;
   JPanel buttonPanel;

   boolean detailsVisible = false;

   //flag indicating whethre initial positioning has been done - see locateBox()
   boolean doneInitialPos = false;

   int buttonHeight = 10;
   int textHeight = 20;
// private static final int HEIGHT = 130;
// private static final int EXPANDED_HEIGHT = 400;
// private static final int INITIAL_WIDTH = 400;

   private static final int MIN_WIDTH = 350;
   private static final int MIN_HEIGHT = 140;
   private static final int MIN_EXP_DIFF = 200;

   int width = 400;
   int expandedDiff = 400; //DIFFERENCE in size between normal height and expanded
   int height = 140;    //normal unexpanded height

   /** Constructor - pass in component to centralise on and use as
    * owner.  If it has no owner, a default frame is used - uses
    * JOptionPane to work it out. */
   public Log2Popup(Component parentComponent)
   {
      super(JOptionPane.getFrameForComponent(parentComponent));   //set to modal
      initComponents();
   }

   /** Constructor, uses default frame provided by JOptionPane to
    * be modal with */
   public Log2Popup()
   {
      super(JOptionPane.getRootFrame());
      initComponents();
   }

   private void initComponents()
   {

      //button panel
      buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      buttonPanel.add(detailButton);
      buttonPanel.add(continueButton);
      buttonPanel.setOpaque(false);
      //buttonHeight = (int) buttonPanel.getPreferredSize().getHeight();
      buttonHeight = 35;

      //userMessageArea.setOpaque(false); doing this can sometimes let the button show through when resizing, even if it is behind
      userMessageArea.setEditable(false);

      iconArea.setBorder(BorderFactory.createEmptyBorder(10,10,10,10));
      iconArea.setHorizontalAlignment(SwingConstants.CENTER);
      iconArea.setVerticalAlignment(SwingConstants.CENTER);
      iconArea.setOpaque(false);

      //vertically centralise text
      JPanel textAlignmentPanel = new JPanel();
      textAlignmentPanel.setLayout(new GridBagLayout());
      textAlignmentPanel.setOpaque(false);
      textAlignmentPanel.add(userMessageArea,
         new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0,
            GridBagConstraints.CENTER,
            GridBagConstraints.BOTH, new Insets(5,5,5,5), 0, 0));

      //area normally seen by user
      userPanel = new JPanel(new BorderLayout());
      userPanel.setBorder(BorderFactory.createEmptyBorder(5,5,0,5));
      userPanel.setLayout(new BorderLayout());
      userPanel.setOpaque(false);
      userPanel.add(iconArea, BorderLayout.WEST);
      userPanel.add(textAlignmentPanel, BorderLayout.CENTER);

      //detail area for stack dumps etc
      detailArea.setEditable(false);
      detailArea.setBackground(getBackground());
      //detailArea.setOpaque(false);
      detailPanel = new JScrollPane(detailArea);

      getContentPane().add(userPanel, BorderLayout.NORTH);
      getContentPane().add(detailPanel, BorderLayout.CENTER);
      getContentPane().add(buttonPanel, BorderLayout.SOUTH);

      //only display relatively important stuff
      addFilter(new SeverityFilter(Severity.INFO));

      //Esc & Enter keys press [Continue] button
      new EscEnterListener(this, continueButton, continueButton);

      //listen out for button presses
      continueButton.addActionListener(this);
      detailButton.addActionListener(this);

      //base look & feel on option pane.
      //probably a better way of doing this - eg using look & feel plug in?
      setBackground(UIManager.getColor("OptionPane.background"));
      userMessageArea.setForeground(UIManager.getColor("OptionPane.foreground"));
      userMessageArea.setFont(UIManager.getFont("OptionPane.font"));
      userMessageArea.setFont(userMessageArea.getFont().deriveFont(Font.BOLD));

      //enable processing component events (so we can trap sizing)
      enableEvents(AWTEvent.COMPONENT_EVENT_MASK);

      //organise location, etc
      showDetails(false);
      setModal(true);
      //locateBox(); defer it until visible
   }

   /**
    * Moves to a suitable location relative to the parent frame
    * Unfortunately 'setLocationRelativeTo()' is not available pre 1.4,
    * and getLocationOnScreen() requires the component to be visible.
    * Therefore, locatnig the box is deferred until setVisible(true)
    * is called, and it is located centrally to the owner only once,
    * so that users can move it to somewhere more convenient.
    */
   public void locateBox()
   {
      pack();

      //setLocationRelativeTo(getOwner()); not available pre 1.4

      /**
       * Center on owning component
       * (NB owning component must be visible at time of calling getLocationOnScreen)
       */
      Point ownerPos = getOwner().getLocationOnScreen();
      Dimension ownerSize = Toolkit.getDefaultToolkit().getScreenSize();
      setLocation(   (ownerPos.x + (ownerSize.width - getPreferredSize().width)/2),
                     (ownerPos.y + (ownerSize.height - getPreferredSize().height)/2));
       /**/
      /**
       * Center on screen
       *
      Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
      Dimension boxSize = getPreferredSize();
      setLocation(   (screenSize.width - boxSize.width) /2,
                     (screenSize.height - boxSize.height)/2);
       /**/
   }

   /** Works out size and sets it */
   private void sizeBox()
   {
      setSize(getPreferredSize());
   }

   /**
    * Override setVisible so that first time the box
    * is made visible it positions itself relative to
    * the owner
    */
   public void setVisible(boolean vis)
   {
      super.setVisible(vis);

      if (vis && !doneInitialPos)
         locateBox();
   }



   /**
    * Show or hide details panel
    */
   private void showDetails(boolean b)
   {
      detailsVisible = b;
      if (b)
      {
         detailPanel.show();
         detailButton.setText("<- Details");
      }
      else
      {
         detailPanel.hide();
         detailButton.setText("Details ->");
      }
      sizeBox();
      validate();
   }

   public Dimension getMinimumSize()
   {
      if (detailsVisible)
         return new Dimension(MIN_WIDTH,
                              (int) Math.max(textHeight, iconArea.getPreferredSize().getHeight())
                                    + buttonHeight
                                    + MIN_EXP_DIFF
                             );
      else
         return new Dimension(MIN_WIDTH,
                              (int) Math.max(textHeight, iconArea.getPreferredSize().getHeight())
                                    + buttonHeight
                             );
   }

   public Dimension getPreferredSize()
   {
      int width = (int) Math.max(MIN_WIDTH,
                                 iconArea.getPreferredSize().getWidth()
                                  + userMessageArea.getPreferredSize().getWidth());

      int height = (int) Math.max(MIN_HEIGHT,
                                  Math.max(iconArea.getPreferredSize().getHeight(),
                                           userMessageArea.getPreferredSize().getHeight())
                                   + buttonPanel.getHeight());
      if (detailsVisible)
      {
         width = (int) Math.max(width,
                                detailArea.getPreferredSize().getWidth());

         height = height + expandedDiff;
      }

      return new Dimension(width, height);
   }

   /**
    * Trap component events (move, resize, etc) - use xxx for switching on
    * this trap
    */
   public void processComponentEvent(ComponentEvent event)
   {
      if (event.getID() == event.COMPONENT_RESIZED)
      {
         //presumably now the size is that of the new size?
         if (detailsVisible)
            expandedDiff = getHeight() - height;
         else
            height = getHeight();

      }
      super.processComponentEvent(event);
   }

   /**
    * Button pressed
    */
   public void actionPerformed(ActionEvent anEvent)
   {
      if (anEvent.getSource() == continueButton)
      {
         hide();
      }
      else if (anEvent.getSource() == detailButton)
      {
         showDetails(!detailsVisible);
      }
   }

   /** Set background - pass on to subcomponents */
   public void setBackground(Color aColor)
   {
      super.setBackground(aColor);
      getContentPane().setBackground(aColor);
      detailArea.setBackground(aColor);
      userMessageArea.setBackground(aColor);
   }

   /**
    * Message received
    */
   public void messageLogged(LogEvent anEvent)
   {
      if (!filterAdaptor.keepEvent(anEvent))
         return;

      event = anEvent;

      //set colours corresponding to severity
      //iconArea.setBackground(event.getSeverity().getBackground());
      iconArea.setIcon(SeverityMedia.getIcon(event.getSeverity()));

      //set the date label based on the timestamp
      SimpleDateFormat formatter = new SimpleDateFormat ("dd/MM/yy HH:mm:ss");
      String dateString = formatter.format( event.getTimestamp() );

      //set box title
      setTitle(event.getSeverity().toString());

      //make up user text
      userMessageArea.setText(event.getText());

      //make up detail text for programming/debugging
      String detail = "";
      if (event.hasUsefulInfo())
         detail = event.getUsefulInfo()+"\n\n";

      detail = detail + event.getStackTrace();

      //if no detail, hide buttons & area as appropriate
      if (detail.length() == 0)
      {
         detailButton.setEnabled(false);
         detailButton.hide();
         showDetails(false);
      }
      else
      {
         detailButton.setEnabled(true);
         detailButton.show();
         detailArea.setText(detail);
         //move focus to Detail button so <space> key operates
         detailButton.requestFocus();
      }

      //sound?

      //size, adjust layout, etc
      validate(); //arrange, word wrap, etc
      textHeight = (int) userMessageArea.getPreferredSize().getHeight();
      height = (int) Math.max(userMessageArea.getPreferredSize().getHeight(),
                              iconArea.getPreferredSize().getHeight())
               + 25 + buttonHeight; //+title bar & (45) buttons
      sizeBox();

      //show box - modally, will stop here until dealt with
      show();
   }

   /**
    * Implementation of FilteredLogHandler, adds the given filter to allow/prevent certain
    * messages from appearing on the status line
    */
   public void addFilter(LogFilter aFilter)
   {
      filterAdaptor.addFilter(aFilter);
   }

   /**
    * Implementation of FilteredLogHandler, removes the given filter to allow/prevent certain
    * messages from appearing on the status line
    */
   public void removeFilter(LogFilter aFilter)
   {
      filterAdaptor.removeFilter(aFilter);
   }

   /**
    * Unit test - creates instance in a frame and runs it
    */
   public static void main(String[] args)
   {
      /**
      UIDefaults uid = UIManager.getLookAndFeelDefaults();

      java.util.Enumeration e = uid.keys();
      while (e.hasMoreElements())
      {
         Object key = e.nextElement();
         System.out.println(""+key+"= "+uid.get(key));
      }
       /**/
      JOptionPane.showConfirmDialog(null, "Just checking");


      Log2Popup pop = new Log2Popup();
      Log.addHandler(pop);
      Log.logError("This is a test bit of text\nWith a second line",new RuntimeException("A RuntimeException"));
      Log.logInfo("A single line message");
      Log.logError("A second test\nWith a second line",new RuntimeException("Another RuntimeException"));
      Log.logWarning("A warning test\nWith a second line");
      Log.logInfo("An information test\nWith a second line\nAnd a third line\nAnd a fourth\nAnd a fifth");
      System.exit(0);
   }

}
