/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */
package worldwinddemo;


import java.io.IOException;
import java.util.logging.Level;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.avlist.AVList;
import gov.nasa.worldwind.exception.WWRuntimeException;
import gov.nasa.worldwind.formats.geojson.GeoJSONDoc;
import gov.nasa.worldwind.formats.geojson.GeoJSONFeature;
import gov.nasa.worldwind.formats.geojson.GeoJSONFeatureCollection;
import gov.nasa.worldwind.formats.geojson.GeoJSONGeometry;
import gov.nasa.worldwind.formats.geojson.GeoJSONGeometryCollection;
import gov.nasa.worldwind.formats.geojson.GeoJSONLineString;
import gov.nasa.worldwind.formats.geojson.GeoJSONMultiLineString;
import gov.nasa.worldwind.formats.geojson.GeoJSONMultiPoint;
import gov.nasa.worldwind.formats.geojson.GeoJSONMultiPolygon;
import gov.nasa.worldwind.formats.geojson.GeoJSONObject;
import gov.nasa.worldwind.formats.geojson.GeoJSONPoint;
import gov.nasa.worldwind.formats.geojson.GeoJSONPolygon;
import gov.nasa.worldwind.formats.geojson.GeoJSONPositionArray;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.Layer;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.Path;
import gov.nasa.worldwind.render.PointPlacemark;
import gov.nasa.worldwind.render.PointPlacemarkAttributes;
import gov.nasa.worldwind.render.Polygon;
import gov.nasa.worldwind.render.Renderable;
import gov.nasa.worldwind.render.ShapeAttributes;
import gov.nasa.worldwind.render.SurfacePolygon;
import gov.nasa.worldwind.render.SurfacePolyline;
import gov.nasa.worldwind.util.Logging;
import gov.nasa.worldwind.util.WWIO;
import gov.nasa.worldwind.util.WWUtil;
import worldwinddemo.util.RandomShapeAttributes;

/**
 * Utility class to load data from a GeoJSON source into a layer.
 *
 * @author dcollins
 * @version $Id: GeoJSONLoader.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class GeoJSONLoader
{
    protected static final RandomShapeAttributes randomAttrs = new RandomShapeAttributes();

    /** Create a new loader. */
    public GeoJSONLoader()
    {
    }

    /**
     * Parse a GeoJSON document and add it to a layer.
     *
     * @param docSource GeoJSON document. May be a file path {@link String}, {@link java.io.File}, {@link java.net.URL},
     *                  or {@link java.net.URI}.
     * @param layer     layer to receive the new Renderable.
     */
    public void addSourceGeometryToLayer(Object docSource, RenderableLayer layer)
    {
        if (WWUtil.isEmpty(docSource))
        {
            String message = Logging.getMessage("nullValue.SourceIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (layer == null)
        {
            String message = Logging.getMessage("nullValue.LayerIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        GeoJSONDoc doc = null;
        try
        {
            doc = new GeoJSONDoc(docSource);
            doc.parse();

            if (doc.getRootObject() instanceof GeoJSONObject)
            {
                this.addGeoJSONGeometryToLayer((GeoJSONObject) doc.getRootObject(), layer);
            }
            else if (doc.getRootObject() instanceof Object[])
            {
                for (Object o : (Object[]) doc.getRootObject())
                {
                    if (o instanceof GeoJSONObject)
                    {
                        this.addGeoJSONGeometryToLayer((GeoJSONObject) o, layer);
                    }
                    else
                    {
                        this.handleUnrecognizedObject(o);
                    }
                }
            }
            else
            {
                this.handleUnrecognizedObject(doc.getRootObject());
            }
        }
        catch (IOException e)
        {
            String message = Logging.getMessage("generic.ExceptionAttemptingToReadGeoJSON", docSource);
            Logging.logger().log(Level.SEVERE, message, e);
            throw new WWRuntimeException(message, e);
        }
        finally
        {
            WWIO.closeStream(doc, docSource.toString());
        }
    }
    /**
     * Create a layer from a GeoJSON document.
     *
     * @param object GeoJSON object to be added to the layer.
     * @param layer layer to receive the new GeoJSON renderable.
     */
    public void addGeoJSONGeometryToLayer(GeoJSONObject object, RenderableLayer layer)
    {
        if (object == null)
        {
            String message = Logging.getMessage("nullValue.ObjectIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (layer == null)
        {
            String message = Logging.getMessage("nullValue.LayerIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        if (object.isGeometry())
            this.addRenderableForGeometry(object.asGeometry(), layer, null);

        else if (object.isFeature())
            this.addRenderableForFeature(object.asFeature(), layer);

        else if (object.isFeatureCollection())
            this.addRenderableForFeatureCollection(object.asFeatureCollection(), layer);

        else
            this.handleUnrecognizedObject(object);
    }

    /**
     * Create a layer from a GeoJSON document.
     *
     * @param docSource GeoJSON document. May be a file path {@link String}, {@link java.io.File}, {@link java.net.URL},
     *                  or {@link java.net.URI}.
     *
     * @return the new layer.
     */
    public Layer createLayerFromSource(Object docSource)
    {
        if (WWUtil.isEmpty(docSource))
        {
            String message = Logging.getMessage("nullValue.SourceIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        RenderableLayer layer = new RenderableLayer();
        addSourceGeometryToLayer(docSource, layer);

        return layer;
    }

    /**
     * Create a layer from a GeoJSON object.
     *
     * @param object GeoJSON object to use to create a Renderable, which will be added to the new layer.
     *
     * @return the new layer.
     */
    public Layer createLayerFromGeoJSON(GeoJSONObject object)
    {
        if (object == null)
        {
            String message = Logging.getMessage("nullValue.ObjectIsNull");
            Logging.logger().severe(message);
            throw new IllegalArgumentException(message);
        }

        RenderableLayer layer = new RenderableLayer();
        addGeoJSONGeometryToLayer(object, layer);

        return layer;
    }

    protected void handleUnrecognizedObject(Object o)
    {
        Logging.logger().warning(Logging.getMessage("generic.UnrecognizedObjectType", o));
    }

    //**************************************************************//
    //********************  Geometry Conversion  *******************//
    //**************************************************************//

    protected void addRenderableForGeometry(GeoJSONGeometry geom, RenderableLayer layer, AVList properties)
    {
        if (geom.isPoint())
            this.addRenderableForPoint(geom.asPoint(), layer, properties);

        else if (geom.isMultiPoint())
            this.addRenderableForMultiPoint(geom.asMultiPoint(), layer, properties);

        else if (geom.isLineString())
            this.addRenderableForLineString(geom.asLineString(), layer, properties);

        else if (geom.isMultiLineString())
            this.addRenderableForMutiLineString(geom.asMultiLineString(), layer, properties);

        else if (geom.isPolygon())
            this.addRenderableForPolygon(geom.asPolygon(), layer, properties);

        else if (geom.isMultiPolygon())
            this.addRenderableForMultiPolygon(geom.asMultiPolygon(), layer, properties);

        else if (geom.isGeometryCollection())
            this.addRenderableForGeometryCollection(geom.asGeometryCollection(), layer, properties);

        else
            this.handleUnrecognizedObject(geom);
    }

    protected void addRenderableForGeometryCollection(GeoJSONGeometryCollection c, RenderableLayer layer,
        AVList properties)
    {
        if (c.getGeometries() == null || c.getGeometries().length == 0)
            return;

        for (GeoJSONGeometry geom : c.getGeometries())
        {
            this.addRenderableForGeometry(geom, layer, properties);
        }
    }

    protected void addRenderableForFeature(GeoJSONFeature feature, RenderableLayer layer)
    {
        if (feature.getGeometry() == null)
        {
            Logging.logger().warning(Logging.getMessage("nullValue.GeometryIsNull"));
            return;
        }

        this.addRenderableForGeometry(feature.getGeometry(), layer, feature.getProperties());
    }

    protected void addRenderableForFeatureCollection(GeoJSONFeatureCollection c, RenderableLayer layer)
    {
        if (c.getFeatures() != null && c.getFeatures().length == 0)
            return;

        for (GeoJSONFeature feat : c.getFeatures())
        {
            this.addRenderableForFeature(feat, layer);
        }
    }

    protected void addRenderableForPoint(GeoJSONPoint geom, RenderableLayer layer, AVList properties)
    {
        PointPlacemarkAttributes attrs = this.createPointAttributes(geom, layer);

        layer.addRenderable(this.createPoint(geom, geom.getPosition(), attrs, properties));
    }

    protected void addRenderableForMultiPoint(GeoJSONMultiPoint geom, RenderableLayer layer, AVList properties)
    {
        PointPlacemarkAttributes attrs = this.createPointAttributes(geom, layer);

        for (int i = 0; i < geom.getPointCount(); i++)
        {
            layer.addRenderable(this.createPoint(geom, geom.getPosition(i), attrs, properties));
        }
    }

    protected void addRenderableForLineString(GeoJSONLineString geom, RenderableLayer layer, AVList properties)
    {
        ShapeAttributes attrs = this.createPolylineAttributes(geom, layer);

        layer.addRenderable(this.createPolyline(geom, geom.getCoordinates(), attrs, properties));
    }

    protected void addRenderableForMutiLineString(GeoJSONMultiLineString geom, RenderableLayer layer, AVList properties)
    {
        ShapeAttributes attrs = this.createPolylineAttributes(geom, layer);

        for (GeoJSONPositionArray coords : geom.getCoordinates())
        {
            layer.addRenderable(this.createPolyline(geom, coords, attrs, properties));
        }
    }

    protected void addRenderableForPolygon(GeoJSONPolygon geom, RenderableLayer layer, AVList properties)
    {
        ShapeAttributes attrs = this.createPolygonAttributes(geom, layer);

        layer.addRenderable(this.createPolygon(geom, geom.getExteriorRing(), geom.getInteriorRings(), attrs,
            properties));
    }

    protected void addRenderableForMultiPolygon(GeoJSONMultiPolygon geom, RenderableLayer layer, AVList properties)
    {
        ShapeAttributes attrs = this.createPolygonAttributes(geom, layer);

        for (int i = 0; i < geom.getPolygonCount(); i++)
        {
            layer.addRenderable(
                this.createPolygon(geom, geom.getExteriorRing(i), geom.getInteriorRings(i), attrs, properties));
        }
    }

    //**************************************************************//
    //********************  Primitive Geometry Construction  *******//
    //**************************************************************//

    @SuppressWarnings( {"UnusedDeclaration"})
    protected Renderable createPoint(GeoJSONGeometry owner, Position pos, PointPlacemarkAttributes attrs,
        AVList properties)
    {
        PointPlacemark p = new PointPlacemark(pos);
        p.setAttributes(attrs);
        if (pos.getAltitude() != 0)
        {
            p.setAltitudeMode(WorldWind.ABSOLUTE);
            p.setLineEnabled(true);
        }
        else
        {
            p.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
        }

        if (properties != null)
            p.setValue(AVKey.PROPERTIES, properties);

        return p;
    }

    @SuppressWarnings( {"UnusedDeclaration"})
    protected Renderable createPolyline(GeoJSONGeometry owner, Iterable<? extends Position> positions,
        ShapeAttributes attrs, AVList properties)
    {
        if (positionsHaveNonzeroAltitude(positions))
        {
            Path p = new Path();
            p.setPositions(positions);
            p.setAltitudeMode(WorldWind.ABSOLUTE);
            p.setAttributes(attrs);

            if (properties != null)
                p.setValue(AVKey.PROPERTIES, properties);

            return p;
        }
        else
        {
            SurfacePolyline sp = new SurfacePolyline(attrs, positions);

            if (properties != null)
                sp.setValue(AVKey.PROPERTIES, properties);

            return sp;
        }
    }

    @SuppressWarnings( {"UnusedDeclaration"})
    protected Renderable createPolygon(GeoJSONGeometry owner, Iterable<? extends Position> outerBoundary,
        Iterable<? extends Position>[] innerBoundaries, ShapeAttributes attrs, AVList properties)
    {
        if (positionsHaveNonzeroAltitude(outerBoundary))
        {
            Polygon poly = new Polygon(outerBoundary);
            poly.setAttributes(attrs);

            if (innerBoundaries != null)
            {
                for (Iterable<? extends Position> iter : innerBoundaries)
                {
                    poly.addInnerBoundary(iter);
                }
            }

            if (properties != null)
                poly.setValue(AVKey.PROPERTIES, properties);

            return poly;
        }
        else
        {
            SurfacePolygon poly = new SurfacePolygon(attrs, outerBoundary);

            if (innerBoundaries != null)
            {
                for (Iterable<? extends Position> iter : innerBoundaries)
                {
                    poly.addInnerBoundary(iter);
                }
            }

            if (properties != null)
                poly.setValue(AVKey.PROPERTIES, properties);

            return poly;
        }
    }

    protected static boolean positionsHaveNonzeroAltitude(Iterable<? extends Position> positions)
    {
        for (Position pos : positions)
        {
            if (pos.getAltitude() != 0)
                return true;
        }

        return false;
    }

    //**************************************************************//
    //********************  Attribute Construction  ****************//
    //**************************************************************//

    @SuppressWarnings( {"UnusedDeclaration"})
    protected PointPlacemarkAttributes createPointAttributes(GeoJSONGeometry geom, Layer layer)
    {
        if (layer == null)
            return randomAttrs.nextPointAttributes();

        String key = this.getClass().getName() + ".PointAttributes";
        PointPlacemarkAttributes attrs = (PointPlacemarkAttributes) layer.getValue(key);
        if (attrs == null)
        {
            attrs = randomAttrs.nextPointAttributes();
            layer.setValue(key, attrs);
        }

        return attrs;
    }

    @SuppressWarnings( {"UnusedDeclaration"})
    protected ShapeAttributes createPolylineAttributes(GeoJSONGeometry geom, Layer layer)
    {
        if (layer == null)
            return randomAttrs.nextPolylineAttributes();

        String key = this.getClass().getName() + ".PolylineAttributes";
        ShapeAttributes attrs = (ShapeAttributes) layer.getValue(key);
        if (attrs == null)
        {
            attrs = randomAttrs.nextPolylineAttributes();
            layer.setValue(key, attrs);
        }

        return attrs;
    }

    @SuppressWarnings( {"UnusedDeclaration"})
    protected ShapeAttributes createPolygonAttributes(GeoJSONGeometry geom, Layer layer)
    {
        if (layer == null)
            return randomAttrs.nextPolygonAttributes();

        String key = this.getClass().getName() + ".PolygonAttributes";
        ShapeAttributes attrs = (ShapeAttributes) layer.getValue(key);
        if (attrs == null)
        {
            attrs = randomAttrs.nextPolygonAttributes();
            layer.setValue(key, attrs);
        }

        return attrs;
    }
}
