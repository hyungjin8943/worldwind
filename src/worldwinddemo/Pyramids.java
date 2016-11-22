/*
 * Copyright (C) 2012 United States Government as represented by the Administrator of the
 * National Aeronautics and Space Administration.
 * All Rights Reserved.
 */

package worldwinddemo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import gov.nasa.worldwind.WorldWind;
import gov.nasa.worldwind.WorldWindow;
import gov.nasa.worldwind.avlist.AVKey;
import gov.nasa.worldwind.geom.Angle;
import gov.nasa.worldwind.geom.Position;
import gov.nasa.worldwind.layers.RenderableLayer;
import gov.nasa.worldwind.render.BasicShapeAttributes;
import gov.nasa.worldwind.render.Material;
import gov.nasa.worldwind.render.Pyramid;
import gov.nasa.worldwind.render.ShapeAttributes;

/**
 * Example of {@link Pyramid} usage. Shows examples of pyramids with various
 * orientations, materials, and textures applied.
 *
 * @author ccrick
 * @version $Id: Pyramids.java 1171 2013-02-11 21:45:02Z dcollins $
 */
public class Pyramids extends ApplicationTemplate {
	public static List<Input> dataList = new ArrayList<Input>();
	public static List<Pyramid> pyramidList = new ArrayList<Pyramid>();

	public static class AppFrame extends ApplicationTemplate.AppFrame {
		public AppFrame() {
			super(true, true, false);

			final RenderableLayer layer = new RenderableLayer();

			// Create and set an attribute bundle.
			ShapeAttributes attrs = new BasicShapeAttributes();
			attrs.setInteriorMaterial(Material.YELLOW);
			attrs.setInteriorOpacity(0.7);
			attrs.setEnableLighting(true);
			attrs.setOutlineMaterial(Material.RED);
			attrs.setOutlineWidth(2d);
			attrs.setDrawInterior(true);
			attrs.setDrawOutline(false);

			// Create and set an attribute bundle.
			ShapeAttributes attrs2 = new BasicShapeAttributes();
			attrs2.setInteriorMaterial(Material.PINK);
			attrs2.setInteriorOpacity(1);
			attrs2.setEnableLighting(true);
			attrs2.setOutlineMaterial(Material.WHITE);
			attrs2.setOutlineWidth(2d);
			attrs2.setDrawOutline(false);

			// ********* sample Pyramids *******************

			// Pyramid with equal axes, ABSOLUTE altitude mode
			/*
			 * Pyramid pyramid3 = new Pyramid( Position.fromDegrees(40, -120,
			 * 80000), 50000, 50000, 50000);
			 * pyramid3.setAltitudeMode(WorldWind.ABSOLUTE);
			 * pyramid3.setAttributes(attrs); pyramid3.setVisible(true);
			 * pyramid3.setValue(AVKey.DISPLAY_NAME,
			 * "Pyramid with equal axes, ABSOLUTE altitude mode"); //
			 * layer.addRenderable(pyramid3);
			 * 
			 * // Pyramid with equal axes, RELATIVE_TO_GROUND Pyramid pyramid4 =
			 * new Pyramid(Position.fromDegrees(37.5, -115, 50000), 50000,
			 * 50000, 50000);
			 * pyramid4.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			 * pyramid4.setAttributes(attrs); pyramid4.setVisible(true);
			 * pyramid4.setValue(AVKey.DISPLAY_NAME,
			 * "Pyramid with equal axes, RELATIVE_TO_GROUND altitude mode"); //
			 * layer.addRenderable(pyramid4);
			 * 
			 * // Pyramid with equal axes, CLAMP_TO_GROUND Pyramid pyramid5 =
			 * new Pyramid( Position.fromDegrees(35, -110, 50000), 50000, 50000,
			 * 50000); pyramid5.setAltitudeMode(WorldWind.CLAMP_TO_GROUND);
			 * pyramid5.setAttributes(attrs); pyramid5.setVisible(true);
			 * pyramid5.setValue(AVKey.DISPLAY_NAME,
			 * "Pyramid with equal axes, CLAMP_TO_GROUND altitude mode"); //
			 * layer.addRenderable(pyramid5);
			 * 
			 * // Pyramid with a texture, using Pyramid(position, height, width)
			 * // constructor Pyramid pyramid9 = new Pyramid(
			 * Position.fromDegrees(0, -90, 600000), 1200000, 1200000);
			 * pyramid9.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			 * pyramid9.setImageSources
			 * ("gov/nasa/worldwindx/examples/images/500px-Checkerboard_pattern.png"
			 * ); pyramid9.setAttributes(attrs); pyramid9.setVisible(true);
			 * pyramid9.setValue(AVKey.DISPLAY_NAME, "Pyramid with a texture");
			 * // layer.addRenderable(pyramid9);
			 * 
			 * // Scaled Pyramid with default orientation Pyramid pyramid = new
			 * Pyramid(Position.ZERO, 1000000, 500000, 100000);
			 * pyramid.setAltitudeMode(WorldWind.ABSOLUTE);
			 * pyramid.setAttributes(attrs); pyramid.setVisible(true);
			 * pyramid.setValue(AVKey.DISPLAY_NAME,
			 * "Scaled Pyramid with default orientation"); //
			 * layer.addRenderable(pyramid);
			 * 
			 * // Scaled Pyramid with a pre-set orientation Pyramid pyramid2 =
			 * new Pyramid(Position.fromDegrees(0, 30, 750000), 1000000, 500000,
			 * 100000, Angle.fromDegrees(90), Angle.fromDegrees(45),
			 * Angle.fromDegrees(30));
			 * pyramid2.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			 * pyramid2.setAttributes(attrs2); pyramid2.setVisible(true);
			 * pyramid2.setValue(AVKey.DISPLAY_NAME,
			 * "Scaled Pyramid with a pre-set orientation"); //
			 * layer.addRenderable(pyramid2);
			 * 
			 * // Scaled Pyramid with a pre-set orientation Pyramid pyramid6 =
			 * new Pyramid( Position.fromDegrees(30, 30, 750000), 1000000,
			 * 500000, 100000, Angle.fromDegrees(90), Angle.fromDegrees(45),
			 * Angle.fromDegrees(30));
			 * pyramid6.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			 * pyramid6.setImageSources
			 * ("gov/nasa/worldwindx/examples/images/500px-Checkerboard_pattern.png"
			 * ); pyramid6.setAttributes(attrs2); pyramid6.setVisible(true);
			 * pyramid6.setValue(AVKey.DISPLAY_NAME,
			 * "Scaled Pyramid with a pre-set orientation"); //
			 * layer.addRenderable(pyramid6);
			 * 
			 * // Scaled Pyramid with a pre-set orientation Pyramid pyramid7 =
			 * new Pyramid( Position.fromDegrees(60, 30, 750000), 1000000,
			 * 500000, 100000, Angle.fromDegrees(90), Angle.fromDegrees(45),
			 * Angle.fromDegrees(30));
			 * pyramid7.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			 * pyramid7.setAttributes(attrs2); pyramid7.setVisible(true);
			 * pyramid7.setValue(AVKey.DISPLAY_NAME,
			 * "Scaled Pyramid with a pre-set orientation"); //
			 * layer.addRenderable(pyramid7);
			 */

			// Scaled, oriented pyramid in 3rd "quadrant" (-X, -Y, -Z)

			for (Pyramid pyramid : pyramidList) {
				layer.addRenderable(pyramid);
			}

			final Timer timer = new Timer();
			timer.scheduleAtFixedRate(new TimerTask() {

				int index = 0;
				@Override
				public void run() {
					System.out.println("Every second index : " + index);

					pyramidList.get(index).setVisible(true);
					if (index != 0) {
						pyramidList.get(index - 1).setVisible(false);
					}

					
					if (index == pyramidList.size() - 1) {
						timer.cancel();
					}
					index++;
				}
			}, 0, 1000);

			// Add the layer to the model.
			insertBeforeCompass(getWwd(), layer);

			// Update layer panel
			this.getLayerPanel().update(this.getWwd());
		}
	}

	public static void main(String[] args) {
		File path = new File("output-3d-quatrilateral.csv");
		readCSVFile(path.getAbsolutePath());
		makePyramidList();
		ApplicationTemplate.start("World Wind Pyramids", AppFrame.class);
	}

	public static void readCSVFile(String csvFilePath) {
		try {
			File csvFile = new File(csvFilePath);

			BufferedReader br = new BufferedReader(new FileReader(csvFile));

			String line = "";
			while ((line = br.readLine()) != null) {
				String[] token = line.split(",", -1);
				dataList.add(new Input(Double.parseDouble(token[0]), Double
						.parseDouble(token[1]), Double.parseDouble(token[2]),
						Double.parseDouble(token[3]), Double
								.parseDouble(token[4]), Double
								.parseDouble(token[5]), Double
								.parseDouble(token[6]), Double
								.parseDouble(token[7]), Double
								.parseDouble(token[8]), Double
								.parseDouble(token[9]), Double
								.parseDouble(token[10]), Double
								.parseDouble(token[11]), Double
								.parseDouble(token[12]), Double
								.parseDouble(token[13])));
			}
			br.close();
			System.out.println(dataList.size());
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void makePyramidList() {
		ShapeAttributes attrs2 = new BasicShapeAttributes();
		attrs2.setInteriorMaterial(Material.YELLOW);
		attrs2.setInteriorOpacity(0.3);
		attrs2.setEnableLighting(true);
		attrs2.setOutlineMaterial(Material.WHITE);
		attrs2.setOutlineWidth(2d);
		attrs2.setDrawOutline(false);

		for (Input input : dataList) {
			double distance1 = distance(input.quadrilateral_pointA_latitude,
					input.quadrilateral_pointB_latitude,
					input.quadrilateral_pointA_longitude,
					input.quadrilateral_pointB_longitude, 0, 0);
			double distance2 = distance(input.quadrilateral_pointC_latitude,
					input.quadrilateral_pointD_latitude,
					input.quadrilateral_pointC_longitude,
					input.quadrilateral_pointD_longitude, 0, 0);

			Pyramid pyramid = new Pyramid(Position.fromDegrees(
					input.camera_latitude, input.camera_longitude,
					input.camera_height * 1000), distance1,
					input.camera_height * 1000, distance2,
					Angle.fromRadians(input.azimuth),
					Angle.fromRadians(input.pitch),
					Angle.fromRadians(input.roll));

			pyramid.setAltitudeMode(WorldWind.RELATIVE_TO_GROUND);
			pyramid.setAttributes(attrs2);
			pyramid.setVisible(false);
			pyramid.setValue(AVKey.DISPLAY_NAME,
					"Scaled, oriented Pyramid in the 3rd 'quadrant' (-X, -Y, -Z)");
			pyramidList.add(pyramid);
		}
	}

	public static class Input {
		private double camera_latitude;
		private double camera_longitude;
		private double camera_height;
		private double quadrilateral_pointA_latitude;
		private double quadrilateral_pointA_longitude;
		private double quadrilateral_pointB_latitude;
		private double quadrilateral_pointB_longitude;
		private double quadrilateral_pointC_latitude;
		private double quadrilateral_pointC_longitude;
		private double quadrilateral_pointD_latitude;
		private double quadrilateral_pointD_longitude;
		private double azimuth;
		private double pitch;
		private double roll;

		public Input(double camera_lat, double camera_long, double camera_h,
				double pointA_lat, double pointA_long, double pointB_lat,
				double pointB_long, double pointC_lat, double pointC_long,
				double pointD_lat, double pointD_long, double _azimuth,
				double _pitch, double _roll) {
			camera_latitude = camera_lat;
			camera_longitude = camera_long;
			camera_height = camera_h;
			quadrilateral_pointA_latitude = pointA_lat;
			quadrilateral_pointA_longitude = pointA_long;
			quadrilateral_pointB_latitude = pointB_lat;
			quadrilateral_pointB_longitude = pointB_long;
			quadrilateral_pointC_latitude = pointC_lat;
			quadrilateral_pointC_longitude = pointC_long;
			quadrilateral_pointD_latitude = pointD_lat;
			quadrilateral_pointD_longitude = pointD_long;
			azimuth = _azimuth;
			pitch = _pitch;
			roll = _roll;
		}
	}

	public static double distance(double lat1, double lat2, double lon1,
			double lon2, double el1, double el2) {

		final int R = 6371; // Radius of the earth

		Double latDistance = Math.toRadians(lat2 - lat1);
		Double lonDistance = Math.toRadians(lon2 - lon1);
		Double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
				+ Math.cos(Math.toRadians(lat1))
				* Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2)
				* Math.sin(lonDistance / 2);
		Double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
		double distance = R * c * 1000; // convert to meters

		double height = el1 - el2;

		distance = Math.pow(distance, 2) + Math.pow(height, 2);

		return Math.sqrt(distance);
	}
}
