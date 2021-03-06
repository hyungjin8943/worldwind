/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JPanel;

import worldwinddemo.util.SectorSelector;

/**
 * Demonstrates how to use the {@link gov.nasa.worldwindx.examples.util.SectorSelector} utility.
 *
 * @author tag
 * @version $Id: SectorSelection.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class SectorSelection extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        private SectorSelector selector;

        public AppFrame()
        {
            super(true, true, false);

            this.selector = new SectorSelector(getWwd());
            this.selector.setInteriorColor(new Color(1f, 1f, 1f, 0.1f));
            this.selector.setBorderColor(new Color(1f, 0f, 0f, 0.5f));
            this.selector.setBorderWidth(3);

            // Set up a button to enable and disable region selection.
            JButton btn = new JButton(new EnableSelectorAction());
            btn.setToolTipText("Press Start then press and drag button 1 on globe");

            JPanel p = new JPanel(new BorderLayout(5, 5));
            p.add(btn, BorderLayout.CENTER);

            this.getLayerPanel().add(p, BorderLayout.SOUTH);

            // Listen for changes to the sector selector's region. Could also just wait until the user finishes
            // and query the result using selector.getSector().
            this.selector.addPropertyChangeListener(SectorSelector.SECTOR_PROPERTY, new PropertyChangeListener()
            {
                public void propertyChange(PropertyChangeEvent evt)
                {
//                    Sector sector = (Sector) evt.getNewValue();
//                    System.out.println(sector != null ? sector : "no sector");
                }
            });
        }

        private class EnableSelectorAction extends AbstractAction
        {
            public EnableSelectorAction()
            {
                super("Start");
            }

            public void actionPerformed(ActionEvent e)
            {
                ((JButton) e.getSource()).setAction(new DisableSelectorAction());
                selector.enable();
            }
        }

        private class DisableSelectorAction extends AbstractAction
        {
            public DisableSelectorAction()
            {
                super("Stop");
            }

            public void actionPerformed(ActionEvent e)
            {
                selector.disable();
                ((JButton) e.getSource()).setAction(new EnableSelectorAction());
            }
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("Sector Selection", AppFrame.class);
    }
}
