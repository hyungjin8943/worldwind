/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo.util;

import java.io.File;
import java.io.InputStream;
import java.nio.ByteBuffer;

import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Box;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.geom.Sector;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;

/**
 * A collection of static utility methods used by the example programs.
 *
 * @author tag
 * @version $Id: ExampleUtil.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class ExampleUtil
{
    /**
     * Unzips the sole entry in the specified zip file, and saves it in a temporary directory, and returns a File to the
     * temporary location.
     *
     * @param path   the path to the source file.
     * @param suffix the suffix to give the temp file.
     *
     * @return a {@link File} for the temp file.
     *
     * @throws IllegalArgumentException if the <code>path</code> is <code>null</code> or empty.
     */
    public static File unzipAndSaveToTempFile(String path, String suffix)
    {
        if (WWUtil.isEmpty(path))
        {
            String message = Logging.getMessage("nullValue.PathIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        InputStream stream = null;

        try
        {
            stream = WWIO.openStream(path);

            ByteBuffer buffer = WWIO.readStreamToBuffer(stream);
            File file = WWIO.saveBufferToTempFile(buffer, WWIO.getFilename(path));

            buffer = WWIO.readZipEntryToBuffer(file, null);
            return WWIO.saveBufferToTempFile(buffer, suffix);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            WWIO.closeStream(stream, path);
        }

        return null;
    }

    /**
     * Saves the file at the specified path in a temporary directory and returns a File to the temporary location.  The
     * path may be one of the following: <ul> <li>{@link java.io.InputStream}</li> <li>{@link java.net.URL}</li>
     * <li>absolute {@link java.net.URI}</li> <li>{@link java.io.File}</li> <li>{@link String} containing a valid URL
     * description or a file or resource name available on the classpath.</li> </ul>
     *
     * @param path   the path to the source file.
     * @param suffix the suffix to give the temp file.
     *
     * @return a {@link File} for the temp file.
     *
     * @throws IllegalArgumentException if the <code>path</code> is <code>null</code> or empty.
     */
    public static File saveResourceToTempFile(String path, String suffix)
    {
        if (WWUtil.isEmpty(path))
        {
            String message = Logging.getMessage("nullValue.PathIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        InputStream stream = null;
        try
        {
            stream = WWIO.openStream(path);

            ByteBuffer buffer = WWIO.readStreamToBuffer(stream);
            return WWIO.saveBufferToTempFile(buffer, suffix);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        finally
        {
            WWIO.closeStream(stream, path);
        }

        return null;
    }

    /**
     * Causes the View attached to the specified WorldWindow to animate to the specified sector. The View starts
     * animating at its current location and stops when the sector fills the window.
     *
     * @param wwd    the WorldWindow who's View animates.
     * @param sector the sector to go to.
     *
     * @throws IllegalArgumentException if either the <code>wwd</code> or the <code>sector</code> are
     *                                  <code>null</code>.
     */
    public static void goTo(WorldWindow wwd, Sector sector)
    {
        if (wwd == null)
        {
            String message = Logging.getMessage("nullValue.WorldWindow");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (sector == null)
        {
            String message = Logging.getMessage("nullValue.SectorIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        // Create a bounding box for the specified sector in order to estimate its size in model coordinates.
        Box extent = Sector.computeBoundingBox(wwd.getModel().getGlobe(),
            wwd.getSceneController().getVerticalExaggeration(), sector);

        // Estimate the distance between the center position and the eye position that is necessary to cause the sector to
        // fill a viewport with the specified field of view. Note that we change the distance between the center and eye
        // position here, and leave the field of view constant.
        Angle fov = wwd.getView().getFieldOfView();
        double zoom = extent.getRadius() / fov.cosHalfAngle() / fov.tanHalfAngle();

        // Configure OrbitView to look at the center of the sector from our estimated distance. This causes OrbitView to
        // animate to the specified position over several seconds. To affect this change immediately use the following:
        // ((OrbitView) wwd.getView()).setCenterPosition(new Position(sector.getCentroid(), 0d));
        // ((OrbitView) wwd.getView()).setZoom(zoom);
        wwd.getView().goTo(new Position(sector.getCentroid(), 0d), zoom);
    }
}
