/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo.dataimport;

import java.awt.Cursor;
import java.io.File;

import javax.swing.SwingUtilities;

import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.globes.ElevationModel;
import gov.nasa.worldwind.globes.Globe;
import gov.nasa.worldwind.terrain.CompoundElevationModel;
import gov.nasa.worldwind.terrain.LocalElevationModel;
import worldwinddemo.ApplicationTemplate;
import worldwinddemo.util.ExampleUtil;

/**
 * Illustrates how to import elevation data into World Wind. This imports a GeoTIFF file containing elevation data and
 * creates an <code>{@link gov.nasa.worldwind.globes.ElevationModel}</code> for it.
 *
 * @author tag
 * @version $Id: ImportElevations.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class ImportElevations extends ApplicationTemplate
{
    // The data to import.
    protected static final String ELEVATIONS_PATH = "gov/nasa/worldwindx/examples/data/craterlake-elev-16bit-30m.tif";

    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            // Show the WAIT cursor because the import may take a while.
            this.setCursor(new Cursor(Cursor.WAIT_CURSOR));

            // Import the elevations on a thread other than the event-dispatch thread to avoid freezing the UI.
            Thread t = new Thread(new Runnable()
            {
                public void run()
                {
                    importElevations();

                    // Restore the cursor.
                    setCursor(Cursor.getDefaultCursor());
                }
            });

            t.start();
        }

        protected void importElevations()
        {
            try
            {
                // Download the data and save it in a temp file.
                File sourceFile = ExampleUtil.saveResourceToTempFile(ELEVATIONS_PATH, ".tif");

                // Create a local elevation model from the data.
                final LocalElevationModel elevationModel = new LocalElevationModel();
                elevationModel.addElevations(sourceFile);

                SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        // Get the WorldWindow's current elevation model.
                        Globe globe = AppFrame.this.getWwd().getModel().getGlobe();
                        ElevationModel currentElevationModel = globe.getElevationModel();

                        // Add the new elevation model to the globe.
                        if (currentElevationModel instanceof CompoundElevationModel)
                            ((CompoundElevationModel) currentElevationModel).addElevationModel(elevationModel);
                        else
                            globe.setElevationModel(elevationModel);

                        // Set the view to look at the imported elevations, although they might be hard to detect. To
                        // make them easier to detect, replace the globe's CompoundElevationModel with the new elevation
                        // model rather than adding it.
                        Sector modelSector = elevationModel.getSector();
                        ExampleUtil.goTo(getWwd(), modelSector);
                    }
                });
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Elevation Import", ImportElevations.AppFrame.class);
    }
}
