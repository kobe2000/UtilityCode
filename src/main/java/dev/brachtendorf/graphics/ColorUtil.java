package dev.brachtendorf.graphics;

import dev.brachtendorf.MathUtil;

import java.awt.*;

/**
 * @author Kilian
 *
 */
public class ColorUtil {

	// YCrCb constants

	// LUMA CONST
	public static final double LUMA_RED = 0.299d;
	public static final double LUMA_GREEN = 0.587d;
	public static final double LUMA_BLUE = 0.114d;

	// RedDif
	public static final double CR_RED = 0.5d;
	public static final double CR_GREEN = 0.418688d;
	public static final double CR_BLUE = 0.081312d;

	// BlueDif
	public static final double CB_RED = 0.168736d;
	public static final double CB_GREEN = 0.331264d;
	public static final double CB_BLUE = 0.5d;

	//

	//@formatter:off
	/**
	 * Convert a JavaFX color to it's awt sibling
	 * @param fxColor The fx color to convert
	 * @return The awt color
	 * @since 1.0.0 com.github.kilianB
	 */
	public static java.awt.Color fxToAwtColor(Color fxColor){
		return fxColor;
	}
	//@formatter:on

	//@formatter:off
	/**
	 * Convert a Java AWT color to it's JavaFX sibling
	 * @param awtColor The awt color to convert
	 * @return The fx color
	 * @since 1.0.0 com.github.kilianB
	 */
	public static Color awtToFxColor(java.awt.Color awtColor) {
		return awtColor;
	}
	//@formatter:on

	/**
	 * Convert an argb value to it's individual components in range of 0 - 255
	 *
	 * @param argb values as int
	 * @return [0] Alpha, [1] Red, [2] Green, [3] Blue
	 * @since 1.0.0 com.github.kilianB
	 *
	 */
	public static int[] argbToComponents(int argb) {
		return new int[]{argb >> 24 & 0xFF, argb >> 16 & 0xFF, argb >> 8 & 0xFF, argb & 0xFF};
	}

	/**
	 * Converts the components to a single int argb representation. The individual
	 * values are not range checked
	 *
	 * @param alpha in range of 0 - 255
	 * @param red   in range of 0 - 255
	 * @param green in range of 0 - 255
	 * @param blue  in range of 0 - 255
	 * @return a single int representing the argb value
	 * @since 1.0.0 com.github.kilianB
	 */
	public static int componentsToARGB(int alpha, int red, int green, int blue) {
		return (alpha << 24) | (red << 16) | (green << 8) | blue;
	}

	/**
	 * Convert an argb value (alpha 24,red 16, green 8, blue 0) into a java fx
	 * color.
	 *
	 * @param argb the argb color as an int
	 * @return The JavaFX Color
	 * @since 1.0.0 com.github.kilianB
	 */
	public static Color argbToFXColor(int argb) {
		int[] components = argbToComponents(argb);
		return new Color(components[1], components[2], components[3], components[0]);
	}

	/**
	 * Return the hexcode of a color
	 *
	 * @param color the color to convert
	 * @return a hex representation of the color
	 * @since 1.0.0 com.github.kilianB
	 */
	public static String fxToHex(Color color) {
		return String.format("#%02X%02X%02X", (int) (color.getRed() * 255), (int) (color.getGreen() * 255),
			(int) (color.getBlue() * 255));
	}

	// https://stackoverflow.com/a/2103608/3244464
	// https://www.compuphase.com/cmetric.htm

	/**
	 * Compute a distance metric of 2 colors. The distance of a color is greater the
	 * further away two colors are.
	 * <p>
	 *
	 * Identical colors will return a distance of 0.
	 *
	 * @param c1 The first color
	 * @param c2 The second color
	 * @return a double value indicating the distance of two colors
	 * @since 1.0.0 com.github.kilianB
	 */
	public static double distance(java.awt.Color c1, java.awt.Color c2) {
		double rmean = (c1.getRed() + c2.getRed()) / 2;
		int r = (int) (c1.getRed() - c2.getRed());
		int g = (int) (c1.getGreen() - c2.getGreen());
		int b = (int) (c1.getBlue() - c2.getBlue());
		double weightR = 2 + rmean / 256;
		double weightG = 4.0;
		double weightB = 2 + (255 - rmean) / 256;
		return Math.sqrt(weightR * r * r + weightG * g * g + weightB * b * b);
	}

	/**
	 * Get the Y (luma component) of the YCrCb color model
	 *
	 * @param c an awt color
	 * @return the luma component in the tange [0-255]
	 * @since 1.3.2 com.github.kilianB
	 */
	public static int getLuma(java.awt.Color c) {
		int luma = (int) Math.round(LUMA_RED * c.getRed() + LUMA_GREEN * c.getGreen() + LUMA_BLUE * c.getBlue());
		return luma > 255 ? 255 : luma;
	}

	/**
	 * Return either white or black depending on the supplied color to guarantee
	 * readability. The contrast color is assumed to be used as text overlay on top
	 * of the input color.
	 *
	 * @param input the color of the background
	 * @return the color (white or black) of the foreground whichever guarantees
	 *         more readability.
	 * @since 1.3.2 com.github.kilianB
	 */
	public static java.awt.Color getContrastColor(java.awt.Color input) {
		// Luminascense
		int y = getLuma(input);
		if (y > 140.25) {
			return java.awt.Color.BLACK;
		} else {
			return java.awt.Color.WHITE;
		}
	}

	public static class ColorPalette {

		/**
		 * Return a default palette from blue to orange using rgba interpolation
		 *
		 * @param numColors the number of colors present in the returned array
		 * @return A color array with
		 * @since 1.0.0 com.github.kilianB
		 */
		public static Color[] getPalette(int numColors) {
			return getPalette(numColors, Color.decode("#003f5c"), Color.decode("#ffa600"));
		}

		/**
		 * Create a color palette using rgba interpolation
		 *
		 * @param numColors  The number of colors present in the returned array
		 * @param startColor The color of the first index
		 * @param endColor   The color of the last index
		 * @return An array containing the interpolated colors
		 * @since 1.0.0 com.github.kilianB
		 */
		public static Color[] getPalette(int numColors, Color startColor, Color endColor) {

			Color[] cols = new Color[numColors];
			for (int i = 0; i < numColors; i++) {
				double factor = i / (double) numColors;
				cols[i] = interpolate(startColor, endColor, factor);
			}
			return cols;
		}

		private static Color interpolate(Color startColor, Color endColor, double factor) {
			if (factor < 0) return startColor;
			if (factor > 1) return endColor;
			return new Color(
				interpolate(startColor.getRed(), endColor.getRed(), factor),
				interpolate(startColor.getGreen(), endColor.getGreen(), factor),
				interpolate(startColor.getBlue(), endColor.getBlue(), factor)
			);
		}

		private static int interpolate(int start, int end, double factor) {
			return (int) (start + (end - start) * factor);
		}

		/**
		 * Create a color palette with the hue component being altered instead of the
		 * individual rgb components. {@link #getPalette(int)}.
		 *
		 * @param numColors The number of colors present in the returned array
		 * @return An array containing the interpolated colors
		 * @since 1.0.0 com.github.kilianB
		 */
		public static Color[] getPaletteHue(int numColors) {
			return getPaletteHue(numColors, Color.decode("#003f5c"), Color.decode("#ffa600"));
		}

		/**
		 * Create a color palette with the hue component being altered instead of the
		 * individual rgb components. {@link #getPalette(int)}.
		 *
		 * @param numColors  The number of colors present in the returned array
		 * @param startColor The color of the first index
		 * @param endColor   The color of the last index
		 * @return An array containing the interpolated colors
		 * @since 1.0.0 com.github.kilianB
		 */
		public static Color[] getPaletteHue(int numColors, Color startColor, Color endColor) {

			float[] startHsb = Color.RGBtoHSB(startColor.getRed(), startColor.getGreen(), startColor.getBlue(), null);
			float[] endHsb = new float[3];
			double hDelta = (endHsb[0] - startHsb[0]) / numColors;
			double sDelta = (endHsb[1] - startHsb[1]) / numColors;
			double bDelta = (endHsb[2] - startHsb[2]) / numColors;

			Color[] cols = new Color[numColors];
			for (int i = 0; i < numColors; i++) {

				double newSat = startHsb[1] + sDelta * i;
				double newBrightness = startHsb[2] + bDelta * i;

				// Wrap around
				if (newSat > 1) {
					newSat = MathUtil.getFractionalPart(newSat);
				} else if (newSat < 0) {
					newSat = 1 - newSat;
				}

				if (newBrightness > 1) {
					newBrightness = MathUtil.getFractionalPart(newBrightness);
				} else if (newBrightness < 0) {
					newBrightness = 1 - newBrightness;
				}
				cols[i] = new Color(Color.HSBtoRGB((float) (startHsb[0] + hDelta * i), (float) newSat, (float) newBrightness));
			}
			return cols;
		}
	}
}
