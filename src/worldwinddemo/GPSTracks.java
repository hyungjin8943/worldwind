/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import javax.xml.parsers.ParserConfigurationException;

import org.xml.sax.SAXException;

import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.event.SelectEvent;
import gov.nasa.worldwind.event.SelectListener;
import gov.nasa.worldwind.formats.gpx.GpxReader;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.MarkerLayer;
import gov.nasa.worldwind.pick.PickedObject;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.markers.BasicMarker;
import gov.nasa.worldwind.render.markers.BasicMarkerAttributes;
import gov.nasa.worldwind.render.markers.BasicMarkerShape;
import gov.nasa.worldwind.render.markers.Marker;
import gov.nasa.worldwind.util.WWIO;

/**
 * Demonstrates displaying GPS tracks using markers. When the view is far away from the data, only a few markers are
 * displayed. As the view zooms in, more markers appear to fill in the track data. When the mouse hovers over a marker
 * the coordinates of that point will be printed to stdout.
 *
 * @author tag
 * @version $Id: GPSTracks.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class GPSTracks extends ApplicationTemplate
{
    protected static final String TRACK_PATH = "gov/nasa/worldwindx/examples/data/tuolumne.gpx";

    protected static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            MarkerLayer layer = this.buildTracksLayer();
            insertBeforeCompass(this.getWwd(), layer);
            this.getLayerPanel().update(this.getWwd());

            this.getWwd().addSelectListener(new SelectListener()
            {
                public void selected(SelectEvent event)
                {
                    if (event.getTopObject() != null)
                    {
                        if (event.getTopPickedObject().getParentLayer() instanceof MarkerLayer)
                        {
                            PickedObject po = event.getTopPickedObject();
                            //noinspection RedundantCast
                            System.out.printf("Track position %s, %s, size = %f\n",
                                po.getValue(AVKey.PICKED_OBJECT_ID).toString(),
                                po.getPosition(), (Double) po.getValue(AVKey.PICKED_OBJECT_SIZE));
                        }
                    }
                }
            });
        }

        protected MarkerLayer buildTracksLayer()
        {
            try
            {
                GpxReader reader = new GpxReader();
                reader.readStream(WWIO.openFileOrResourceStream(TRACK_PATH, this.getClass()));
                Iterator<Position> positions = reader.getTrackPositionIterator();

                BasicMarkerAttributes attrs =
                    new BasicMarkerAttributes(Material.WHITE, BasicMarkerShape.SPHERE, 1d);

                ArrayList<Marker> markers = new ArrayList<Marker>();
                while (positions.hasNext())
                {
                    markers.add(new BasicMarker(positions.next(), attrs));
                }

                MarkerLayer layer = new MarkerLayer(markers);
                layer.setOverrideMarkerElevation(true);
                layer.setElevation(0);
                layer.setEnablePickSizeReturn(true);

                return layer;
            }
            catch (ParserConfigurationException e)
            {
                e.printStackTrace();
            }
            catch (SAXException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return null;
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Tracks", AppFrame.class);
    }
}
