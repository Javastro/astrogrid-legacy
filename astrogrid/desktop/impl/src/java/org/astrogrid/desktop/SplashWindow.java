/*
 * @(#)SplashWindow.java  2.2  2005-04-03
 *
 * Copyright (c) 2003-2005 Werner Randelshofer
 * Staldenmattweg 2, Immensee, CH-6405, Switzerland.
 * All rights reserved.
 *
 * This software is in the public domain.
 */

package org.astrogrid.desktop;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.geom.AffineTransform;
import java.net.URL;

/** 
 * A Splash window.
 *  <p>
 * Usage: MyApplication is your application class. Create a Workbench class which
 * opens the splash window, invokes the main method of your Application class,
 * and disposes the splash window afterwards.
 * Please note that we want to keep the Workbench class and the SplashWindow class
 * as small as possible. The less code and the less classes must be loaded into
 * the JVM to open the splash screen, the faster it will appear.
 * <pre>
 * class Workbench {
 *    public static void main(String[] args) {
 *         SplashWindow.splash(Startup.class.getResource("splash.gif"));
 *         MyApplication.main(args);
 *         SplashWindow.disposeSplash();
 *    }
 * }
 * </pre>
 *Cribbed from http://www.randelshofer.ch/oop/javasplash/javasplash.html
 * @author  Werner Randelshofer
 * @version 2.1 2005-04-03 Revised.
 */
public class SplashWindow extends Window {
    /**
     * The current instance of the splash window.
     * (Singleton design pattern).
     */
    private static SplashWindow instance;
    
    /**
     * The splash image which is displayed on the splash window.
     */
    private Image image;
    
    /**
     * the label used to report progress
     */
    private String label ="";    
    private Font labelFont = Font.decode("Dialog")
        .deriveFont(AffineTransform.getScaleInstance(0.9,0.9));
    /**
     * This attribute indicates whether the method
     * paint(Graphics) has been called at least once since the
     * construction of this window.<br>
     * This attribute is used to notify method splash(Image)
     * that the window has been drawn at least once
     * by the AWT event dispatcher thread.<br>
     * This attribute acts like a latch. Once set to true,
     * it will never be changed back to false again.
     *
     * @see #paint
     * @see #splash
     */
    boolean paintCalled = false;
    
    /**
     * Creates a new instance.
     * @param parent the parent of the window.
     * @param image the splash image.
     */
    private SplashWindow(Frame parent, Image image) {
        super(parent);
        this.image = image;        
        // Load the image
        MediaTracker mt = new MediaTracker(this);
        mt.addImage(image,0);
        try {
            mt.waitForID(0);
        } catch(InterruptedException ie){
        	// do nothing.
        }
        
        imgWidth = image.getWidth(this);
        imgHeight = image.getHeight(this);
        setSize(imgWidth, imgHeight);
        Dimension screenDim = Toolkit.getDefaultToolkit().getScreenSize();
        setLocation(
        (screenDim.width - imgWidth) / 2,
        (screenDim.height - imgHeight) / 2
        );
        
        // Users shall be able to close the splash window by
        // clicking on its display area. This mouse listener
        // listens for mouse clicks and disposes the splash window.
        MouseAdapter disposeOnClick = new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                // Note: To avoid that method splash hangs, we
                // must set paintCalled to true and call notifyAll.
                // This is necessary because the mouse click may
                // occur before the contents of the window
                // has been painted.
                synchronized(SplashWindow.this) {
                    SplashWindow.this.paintCalled = true;
                    SplashWindow.this.notifyAll();
                }
                dispose();
            }
        };
        addMouseListener(disposeOnClick);
    }
    
    /**
     * @param label the label to set
     */
    private void setLabel(String label) {
        this.label = label;
        Graphics g = getGraphics();
        if (g != null ) { // if null, label is not shown
            drawLabel(g);
        }
    }
    
    private void drawLabel(Graphics g) {
        // clear
        g.setColor(Color.WHITE);
        int sz = labelFont.getSize();
        g.fillRect(indent,imgHeight - (sz +8) ,imgWidth - (indent + 8) ,sz + 2);
       // draw.
        g.setFont(labelFont);
        g.setColor(Color.BLACK);
        g.drawString(this.label,indent,imgHeight - 8);
    }
    
    /** x of baseline of leftmost character */
    private final int indent = 10;

    /** dimensions of the image */
    private final int imgWidth;
   
    private final int imgHeight;

    /**
     * Updates the display area of the window.
     */
    @Override
    public void update(Graphics g) {
        // Note: Since the paint method is going to draw an
        // image that covers the complete area of the component we
        // do not fill the component with its background color
        // here. This avoids flickering.
        paint(g);
    }
    /**
     * Paints the image on the window.
     */
    @Override
    public void paint(Graphics g) {
        g.drawImage(image, 0, 0, this);
        drawLabel(g);
        // Notify method splash that the window
        // has been painted.
        // Note: To improve performance we do not enter
        // the synchronized block unless we have to.
        if (! paintCalled) {
            paintCalled = true;
            synchronized (this) { notifyAll(); }
        }
    }
    
    /**
     * Open's a splash window using the specified image.
     * @param image The splash image.
     */
    public static void splash(Image image) {
        if (!GraphicsEnvironment.isHeadless() && instance == null && image != null) {
            Frame f = new Frame();
            
            // Create the splash image
            instance = new SplashWindow(f, image);
            reportProgress( "Launching VO Desktop...");
            // Show the window.
            //instance.show();
            instance.setVisible(true);
            // Note: To make sure the user gets a chance to see the
            // splash window we wait until its paint method has been
            // called at least once by the AWT event dispatcher thread.
            // If more than one processor is available, we don't wait,
            // and maximize CPU throughput instead.
            if (! EventQueue.isDispatchThread() 
            && Runtime.getRuntime().availableProcessors() == 1) {
                synchronized (instance) {
                    while (! instance.paintCalled) {
                        try { instance.wait(); } catch (InterruptedException e) {
                        	// do nothing
                        }
                    }
                }
            }
        }
    }
    /**
     * Open's a splash window using the specified image.
     * @param imageURL The url of the splash image.
     */
    public static void splash(URL imageURL) {
        if (imageURL != null) {
            splash(Toolkit.getDefaultToolkit().createImage(imageURL));
        }
    }
    
    /**
     * Closes the splash window.
     */
    public static void disposeSplash() {
        if (instance != null) {
            instance.getOwner().dispose();
            instance = null;
        }
    }
    
    /**
     * Invokes the main method of the provided class name.
     * @param args the command line arguments
     */
    public static void invokeMain(String className, String[] args) {
        try {
            Class.forName(className)
            .getMethod("main", new Class[] {String[].class})
            .invoke(null, new Object[] {args});
        } catch (Exception e) {
            InternalError error = new InternalError("Failed to invoke main method");
            error.initCause(e);
            throw error;
        }
    }
    
    // additions for progress reporting
    
    public static void reportProgress(String msg) {
        if (instance != null) {
            instance.setLabel(msg);
        }
    }

}
