/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo;

import java.util.ArrayList;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.util.BasicDragger;
import gov.nasa.worldwind.util.WWUtil;

/**
 * Example of {@link Polygon} usage. Sets material, opacity and other attributes. Sets rotation and other properties.
 * Adds an image for texturing. Shows a dateline-spanning Polygon.
 *
 * @author tag
 * @version $Id: Polygons.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Polygons extends ApplicationTemplate
{
    public static class AppFrame extends ApplicationTemplate.AppFrame
    {
        public AppFrame()
        {
            super(true, true, false);

            // Add a dragger to enable shape dragging
            this.getWwd().addSelectListener(new BasicDragger(this.getWwd()));

            RenderableLayer layer = new RenderableLayer();

            // Create and set an attribute bundle.
            ShapeAttributes normalAttributes = new BasicShapeAttributes();
//            normalAttributes.setInteriorMaterial(Material.YELLOW);
            normalAttributes.setOutlineOpacity(1);
            normalAttributes.setInteriorOpacity(1);
            normalAttributes.setOutlineMaterial(Material.GREEN);
            normalAttributes.setOutlineWidth(2);
            normalAttributes.setDrawOutline(true);
            normalAttributes.setDrawInterior(false);
            normalAttributes.setEnableLighting(true);

            ShapeAttributes highlightAttributes = new BasicShapeAttributes(normalAttributes);
            highlightAttributes.setOutlineMaterial(Material.WHITE);
            highlightAttributes.setOutlineOpacity(1);

            
            // Create a polygon, set some of its properties and set its attributes.
           
           
            

           
//          ArrayList<Position> pathLocations = new ArrayList<Position>();
//          pathLocations.add(Position.fromDegrees(34, -118, 10));
//          pathLocations.add(Position.fromDegrees(34, -119, 10));
//          pathLocations.add(Position.fromDegrees(35, -119, 10));
//          pathLocations.add(Position.fromDegrees(34, -119, 10));
//          pathLocations.add(Position.fromDegrees(34, -118, 10));
//          pgon = new Polygon(pathLocations);
//          pgon.setValue(AVKey.DISPLAY_NAME, "Has an image");
//        normalAttributes = new BasicShapeAttributes(normalAttributes);
//        normalAttributes.setDrawInterior(true);
//        normalAttributes.setInteriorMaterial(Material.WHITE);
//        normalAttributes.setInteriorOpacity(1);
//        pgon.setAttributes(normalAttributes);
//        pgon.setHighlightAttributes(highlightAttributes);
//        float[] texCoords = new float[] {0, 0, 1, 0, 1, 1, 0, 1, 0, 0};
//        pgon.setTextureImageSource("images/32x32-icon-nasa.png", texCoords, 5);
//        layer.addRenderable(pgon);

            ArrayList<Position> pathLocations = new ArrayList<Position>();
            
            pathLocations.add(Position.fromDegrees(33.01845636, 140.6493453, 10));
            pathLocations.add(Position.fromDegrees(33.74608841, 136.6518606, 10));
            pathLocations.add(Position.fromDegrees(35.803221, 139.095267, 447.07538));
            Polygon pgon  = new Polygon(pathLocations);
            pgon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            pgon.setValue(AVKey.DISPLAY_NAME, "Has an image");
            normalAttributes = new BasicShapeAttributes(normalAttributes);
//            normalAttributes.setDrawInterior(true);
//            normalAttributes.setInteriorMaterial(Material.WHITE);
            normalAttributes.setInteriorOpacity(1);
            pgon.setAttributes(normalAttributes);
            pgon.setVisible(true);
//            pgon.setTextureImageSource("images/32x32-icon-nasa.png", texCoords, 5);
            layer.addRenderable(pgon);
            
            pathLocations.clear();
            pathLocations.add(Position.fromDegrees(33.74608841, 136.6518606, 10));
            pathLocations.add(Position.fromDegrees(37.53696157, 137.5501718, 10));
            pathLocations.add(Position.fromDegrees(35.803221, 139.095267, 447.07538));
            pgon = new Polygon(pathLocations);
            pgon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            pgon.setValue(AVKey.DISPLAY_NAME, "Has an image");
            normalAttributes = new BasicShapeAttributes(normalAttributes);
//            normalAttributes.setDrawInterior(true);
//            normalAttributes.setInteriorMaterial(Material.WHITE);
            normalAttributes.setInteriorOpacity(1);
            pgon.setAttributes(normalAttributes);
            pgon.setVisible(true);
//            pgon.setTextureImageSource("images/32x32-icon-nasa.png", texCoords, 5);
            layer.addRenderable(pgon);
            
            pathLocations.clear();
            pathLocations.add(Position.fromDegrees(37.53696157, 137.5501718, 10));
            pathLocations.add(Position.fromDegrees(36.8722113, 141.1523996, 10));
            pathLocations.add(Position.fromDegrees(35.803221, 139.095267, 447.07538));
            pgon = new Polygon(pathLocations);
            pgon.setValue(AVKey.DISPLAY_NAME, "Has an image");
            pgon.setVisible(true);
            pgon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            normalAttributes = new BasicShapeAttributes(normalAttributes);
//            normalAttributes.setDrawInterior(true);
//            normalAttributes.setInteriorMaterial(Material.WHITE);
            normalAttributes.setInteriorOpacity(1);
            pgon.setAttributes(normalAttributes);
            layer.addRenderable(pgon);
            
            
            pathLocations = new ArrayList<Position>();
            pathLocations.add(Position.fromDegrees(33.018456364,140.6493453, 0));
            pathLocations.add(Position.fromDegrees(33.74608841,136.6518606, 0));
            pathLocations.add(Position.fromDegrees(37.53696157,137.5501718, 0));
            pathLocations.add(Position.fromDegrees(36.8722113,141.1523996, 0));
            pathLocations.add(Position.fromDegrees(33.018456364,140.6493453, 0));
            pgon = new Polygon(pathLocations);
            pgon.setValue(AVKey.DISPLAY_NAME, "coverage area");
            normalAttributes = new BasicShapeAttributes(normalAttributes);
            normalAttributes.setDrawInterior(true);
            pgon.setAttributes(normalAttributes);
            pgon.setVisible(true);
            pgon.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
            
           layer.addRenderable(pgon);           

            // Add the layer to the model.
            insertBeforeCompass(getWwd(), layer);

            // Update layer panel
            this.getLayerPanel().update(this.getWwd());
        }
    }

    public static void main(String[] args)
    {
        ApplicationTemplate.start("World Wind Extruded Polygons", AppFrame.class);
    }
}
