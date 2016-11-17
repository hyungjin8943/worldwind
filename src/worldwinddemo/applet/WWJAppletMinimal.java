/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo.applet;

import java.awt.BorderLayout;

import javax.swing.JApplet;

import gov.nasa.worldwind.Model;
import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.awt.WorldWindowGLCanvas;
import gov.nasa.worldwind.util.StatusBar;

/**
 * Illustrates the simplest possible way to display a World Wind <code>{@link WorldWindow}</code> in a Java Applet. This
 * class extends <code>{@link JApplet}</code> and embeds a WorldWindowGLCanvas and a StatusBar in the Applet's content
 * pane.
 *
 * @author Patrick Murris
 * @version $Id: WWJAppletMinimal.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class WWJAppletMinimal extends JApplet
{
    public WWJAppletMinimal()
    {
    }

    public void init()
    {
        try
        {
            // Create World Window canvas.
            WorldWindowGLCanvas wwd = new WorldWindowGLCanvas();
            this.getContentPane().add(wwd, BorderLayout.CENTER);

            // Create the default model as defined in the current worldwind configuration file.
            wwd.setModel((Model) WorldWind.createConfigurationComponent(AVKey.MODEL_CLASS_NAME));

            // Add the status bar, and forward events to the status bar to provide the cursor position info.
            StatusBar statusBar = new StatusBar();
            statusBar.setEventSource(wwd);
            this.getContentPane().add(statusBar, BorderLayout.SOUTH);
        }
        catch (Throwable e)
        {
            e.printStackTrace();
        }
    }

    public void stop()
    {
        // Shut down World Wind when the browser stops this Applet.
        WorldWind.shutDown();
    }
}
