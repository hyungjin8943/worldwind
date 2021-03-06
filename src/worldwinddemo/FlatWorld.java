/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo;


import java.awt.BorderLayout;

import gov.nasa.worldwind.Configuration;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.globes.EarthFlat;
import gov.nasa.worldwind.layers.LayerList;
import gov.nasa.worldwind.layers.SkyColorLayer;
import gov.nasa.worldwind.layers.SkyGradientLayer;
import gov.nasa.worldwind.view.orbit.FlatOrbitView;

/**
 * Example of displaying a flat globe instead of a round globe. The flat globe displays elevation in the same way as the
 * round globe (mountains rise out of the globe). One advantage of using a flat globe is that a user can see the entire
 * globe at once. The globe can be configured with different map projections.
 *
 * @author Patrick Murris
 * @version $Id: FlatWorld.java 1171 2013-02-11 21:45:02Z dcollins $
 * @see gov.nasa.worldwind.globes.FlatGlobe
 * @see EarthFlat
 * @see FlatOrbitView
 */
public class FlatWorld extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            // Change atmosphere SkyGradientLayer for SkyColorLayer
            LayerList layers = this.getWwd().getModel().getLayers();
            for (int i = 0; i < layers.size(); i++)
            {
                if (layers.get(i) instanceof SkyGradientLayer)
                    layers.set(i, new SkyColorLayer());
            }
            this.getLayerPanel().update(this.getWwd());

            // Add flat world projection control panel
            this.getLayerPanel().add(new FlatWorldPanel(this.getWwd()), BorderLayout.SOUTH);
        }
    }

    public static void main(String[] args)
    {
        // Adjust configuration values before instantiation
        Configuration.setValue(AVKey.GLOBE_CLASS_NAME, EarthFlat.class.getName());
        Configuration.setValue(AVKey.VIEW_CLASS_NAME, FlatOrbitView.class.getName());
        ApplicationTemplate.start("World Wind Flat World", AppFrame.class);
    }
}
