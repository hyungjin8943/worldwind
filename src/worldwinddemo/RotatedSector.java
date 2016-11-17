/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Timer;

import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.SurfaceQuad;

/**
 * Illustrates rotating a {@link gov.nasa.worldwind.geom.Sector} from standard position. A <code>Sector</code> is
 * created, its width and height computed, and a {@link gov.nasa.worldwind.render.SurfaceQuad} created from the
 * <code>sector's</code> centroid and the computed width and height. The <code>SurfaceQuad's</code> heading is then set
 * to the desired rotation angle.
 *
 * @author tag
 * @version $Id: RotatedSector.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class RotatedSector extends ApplicationTemplate
{
    private static final Sector sector = Sector.fromDegrees(45, 47, -123, -122);

    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            try
            {
                // Create the Quad from a Sector
                Globe globe = this.getWwd().getModel().getGlobe();
                double radius = globe.getRadiusAt(sector.getCentroid());
                double quadWidth = sector.getDeltaLonRadians() * radius;
                double quadHeight = sector.getDeltaLatRadians() * radius;
                final SurfaceQuad quad = new SurfaceQuad(sector.getCentroid(), quadWidth, quadHeight, Angle.ZERO);

                // Create the layer to hold it
                final RenderableLayer layer = new RenderableLayer();
                layer.setName("Rotating Sector");
                layer.addRenderable(quad);

                // Add the layer to the model and update the ApplicationTemplate's layer manager
                insertBeforeCompass(this.getWwd(), layer);
                this.getLayerPanel().update(this.getWwd());

                // Rotate the quad continuously
                Timer timer = new Timer(50, new ActionListener()
                {
                    public void actionPerformed(ActionEvent actionEvent)
                    {
                        // Increment the current heading if the layer is visible
                        if (layer.isEnabled())
                        {
                            quad.setHeading(Angle.fromDegrees((quad.getHeading().getDegrees() + 1) % 360));
                            getWwd().redraw();
                        }
                    }
                });
                timer.start();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("Rotated Sector", AppFrame.class);
    }
}