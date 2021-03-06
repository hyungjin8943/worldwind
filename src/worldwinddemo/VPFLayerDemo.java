/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.formats.vpf.VPFCoveragePanel;
import gov.nasa.worldwind.formats.vpf.VPFDatabase;
import gov.nasa.worldwind.formats.vpf.VPFDatabaseFilter;
import gov.nasa.worldwind.formats.vpf.VPFLayer;
import gov.nasa.worldwind.formats.vpf.VPFUtils;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWUtil;

/**
 * Illustrates how to import data from a Vector Product Format (VPF) database into World Wind. This uses <code>{@link
 * VPFLayer}</code> to display an imported VPF database, and uses <code>{@link VPFCoveragePanel}</code> to enable the
 * user to choose which shapes from the VPF database to display.
 * <p/>
 * To display VPF shapes with the appropriate color, style, and icon, applications must include the JAR file
 * <code>vpf-symbols.jar</code> in the Java class-path. If this JAR file is not in the Java class-path, VPFLayer outputs
 * the following message in the World Wind log: <code>WARNING: GeoSym style support is disabled</code>. In this case,
 * VPF shapes are displayed as gray outlines, and icons are displayed as a gray question mark.
 *
 * @author dcollins
 * @version $Id: VPFLayerDemo.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class VPFLayerDemo extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            this.makeControlPanel();
        }

        protected void addVPFLayer(File file)
        {
            VPFDatabase db = VPFUtils.readDatabase(file);
            VPFLayer layer = new VPFLayer(db);
            insertBeforePlacenames(this.getWwd(), layer);
            this.getLayerPanel().update(this.getWwd());
            this.openVPFCoveragePanel(db, layer);
        }

        protected void openVPFCoveragePanel(VPFDatabase db, VPFLayer layer)
        {
            VPFCoveragePanel panel = new VPFCoveragePanel(getWwd(), db);
            panel.setLayer(layer);
            JFrame frame = new JFrame(db.getName());
            frame.setResizable(true);
            frame.setAlwaysOnTop(true);
            frame.add(panel);
            frame.pack();
            WWUtil.alignComponent(this, frame, AVKey.CENTER);
            frame.setVisible(true);
        }

        protected void showOpenDialog()
        {
            JFileChooser fc = new JFileChooser(Configuration.getUserHomeDirectory());
            fc.addChoosableFileFilter(new VPFFileFilter());

            int retVal = fc.showOpenDialog(this);
            if (retVal != JFileChooser.APPROVE_OPTION)
                return;

            File file = fc.getSelectedFile();
            this.addVPFLayer(file);
        }

        protected void makeControlPanel()
        {
            JButton button = new JButton("Open VPF Database");
            button.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent actionEvent)
                {
                    showOpenDialog();
                }
            });

            Box box = Box.createHorizontalBox();
            box.setBorder(BorderFactory.createEmptyBorder(30, 30, 30, 30)); // top, left, bottom, right
            box.add(button);

            this.getLayerPanel().add(box, BorderLayout.SOUTH);
        }
    }

    public static class VPFFileFilter extends FileFilter
    {
        protected VPFDatabaseFilter filter;

        public VPFFileFilter()
        {
            this.filter = new VPFDatabaseFilter();
        }

        public boolean accept(File file)
        {
            if (file == null)
            {
                String message = Logging.getMessage("nullValue.FileIsNull");
                Logging.logger().severe(message);
                throw new IllegalArgumentException(message);
            }

            return file.isDirectory() || this.filter.accept(file);
        }

        public String getDescription()
        {
            return "VPF Databases (dht)";
        }
    }

    public static void main(String[] args)
    {
        start("World Wind VPF Shapes", AppFrame.class);
    }
}
