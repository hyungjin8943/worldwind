/*
 * Copyright (C) 2013 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo.layermanager;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.SwingUtilities;
import javax.swing.border.CompoundBorder;
import javax.swing.border.TitledBorder;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.RenderingEvent;
import gov.nasa.worldwind.event.RenderingListener;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.TiledImageLayer;

/**
 * Displays the available layers. Provides an interface to enable and disable them. Provides an interface to change
 * their relative order. Indicates when any portion of an image layer is actually rendered.
 *
 * @author tag
 * @version $Id: LayerManagerPanel.java 1179 2013-02-15 17:47:37Z tgaskins $
 */
public class LayerManagerPanel extends JPanel
{
    protected JPanel layerNamesPanel;
    protected List<LayerPanel> layerPanels = new ArrayList<LayerPanel>();

    public LayerManagerPanel(final WorldWindow wwd)
    {
        super(new BorderLayout(10, 10));

        this.layerNamesPanel = new JPanel(new GridLayout(0, 1, 0, 5));
        this.layerNamesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        // Must put the layer grid in a container to prevent the scroll pane from stretching vertical spacing.
        JPanel dummyPanel = new JPanel(new BorderLayout());
        dummyPanel.add(this.layerNamesPanel, BorderLayout.NORTH);

        // Put the layers panel in a scroll pane.
        JScrollPane scrollPane = new JScrollPane(dummyPanel);

        // Suppress the scroll pane's default border.
        scrollPane.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));

        // Add the scroll pane to a titled panel that will resize with the main window.
        JPanel titlePanel = new JPanel(new GridLayout(0, 1, 0, 10));
        titlePanel.setBorder(
            new CompoundBorder(BorderFactory.createEmptyBorder(9, 9, 9, 9), new TitledBorder("Layers")));
        titlePanel.setToolTipText("Layers to Show");
        titlePanel.add(scrollPane);
        titlePanel.setPreferredSize(new Dimension(200, 500));
        this.add(titlePanel, BorderLayout.CENTER);

        this.fill(wwd);

        // Register a rendering listener that updates the was-rendered state of each image layer.
        wwd.addRenderingListener(new RenderingListener()
        {
            @Override
            public void stageChanged(RenderingEvent event)
            {
                updateLayerActivity(wwd);
            }
        });

        // Add a property change listener that causes this layer panel to be updated whenever the layer list changes.
        wwd.getModel().getLayers().addPropertyChangeListener(new PropertyChangeListener()
        {
            @Override
            public void propertyChange(PropertyChangeEvent propertyChangeEvent)
            {
                if (propertyChangeEvent.getPropertyName().equals(AVKey.LAYERS))
                    SwingUtilities.invokeLater(new Runnable()
                    {
                        public void run()
                        {
                            update(wwd);
                        }
                    });
            }
        });
    }

    public void update(WorldWindow wwd)
    {
        // Repopulate this layer manager.

        this.fill(wwd);
    }

    protected void fill(WorldWindow wwd)
    {
        // Populate this layer manager with an entry for each layer in the WorldWindow's layer list.

        if (this.isUpToDate(wwd))
            return;

        // First remove all the existing entries.
        this.layerPanels.clear();
        this.layerNamesPanel.removeAll();

        // Fill the layers panel with the titles of all layers in the world window's current model.
        for (Layer layer : wwd.getModel().getLayers())
        {
            if (layer.getValue(AVKey.IGNORE) != null)
                continue;

            LayerPanel layerPanel = new LayerPanel(wwd, layer);
            this.layerNamesPanel.add(layerPanel);
            this.layerPanels.add(layerPanel);
        }

        this.updateLayerActivity(wwd);
    }

    protected boolean isUpToDate(WorldWindow wwd)
    {
        // Determines whether this layer manager's layer list is consistent with the specified WorldWindow's. Knowing
        // this prevents redundant updates.

        LayerList layerList = wwd.getModel().getLayers();

        if (this.layerPanels.size() != layerList.size())
            return false;

        for (int i = 0; i < layerList.size(); i++)
        {
            if (layerList.get(i) != this.layerPanels.get(i).getLayer())
                return false;
        }

        return true;
    }

    /**
     * Loops through this layer panel's layer/checkbox list and updates the checkbox font to indicate whether the
     * corresponding layer was just rendered. This method is called by a rendering listener -- see comment below.
     *
     * @param wwd the world window.
     */
    protected void updateLayerActivity(WorldWindow wwd)
    {
        for (LayerPanel layerPanel : this.layerPanels)
        {
            // The frame timestamp from the layer indicates the last frame in which it rendered something. If that
            // timestamp matches the current timestamp of the scene controller, then the layer rendered something
            // during the most recent frame. Note that this frame timestamp protocol is only in place by default
            // for TiledImageLayer and its subclasses. Applications could, however, implement it for the layers
            // they design.

            Long layerTimeStamp = (Long) layerPanel.getLayer().getValue(AVKey.FRAME_TIMESTAMP);
            Long frameTimeStamp = (Long) wwd.getSceneController().getValue(AVKey.FRAME_TIMESTAMP);

            if (layerTimeStamp != null && frameTimeStamp != null
                && layerTimeStamp.longValue() == frameTimeStamp.longValue())
            {
                // Set the font to bold if the layer was just rendered.
                layerPanel.setLayerNameFont(layerPanel.getLayerNameFont().deriveFont(Font.BOLD));
            }
            else if (layerPanel.getLayer() instanceof TiledImageLayer)
            {
                // Set the font to plain if the layer was not just rendered.
                layerPanel.setLayerNameFont(layerPanel.getLayerNameFont().deriveFont(Font.PLAIN));
            }
            else if (layerPanel.getLayer().isEnabled())
            {
                // Set enabled layer types other than TiledImageLayer to bold.
                layerPanel.setLayerNameFont(layerPanel.getLayerNameFont().deriveFont(Font.BOLD));
            }
            else if (!layerPanel.getLayer().isEnabled())
            {
                // Set disabled layer types other than TiledImageLayer to plain.
                layerPanel.setLayerNameFont(layerPanel.getLayerNameFont().deriveFont(Font.PLAIN));
            }
        }
    }
}