// -*- mode: java; c-basic-offset: 2; -*-
// Copyright 2009-2011 Google, All Rights reserved
// Copyright 2011-2012 MIT, All rights reserved
// Released under the MIT License https://raw.github.com/mit-cml/app-inventor/master/mitlicense.txt

package com.google.appinventor.components.runtime.util;

import com.google.appinventor.components.runtime.Component;

import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.view.Gravity;
import android.widget.TextView;

import java.util.HashMap; //GlassInventor: fonts
import java.util.Map;
import android.content.Context;

/**
 * Helper methods for manipulating {@link TextView} objects.
 *
 */
public class TextViewUtil {

  private TextViewUtil() {
  }

  /**
   * TextView alignment setter.
   *
   * @param alignment  one of {@link Component#ALIGNMENT_NORMAL},
   *                   {@link Component#ALIGNMENT_CENTER} or
   *                   {@link Component#ALIGNMENT_OPPOSITE}
   * @param centerVertically whether the text should be centered vertically
   */
  public static void setAlignment(TextView textview, int alignment, boolean centerVertically) {
    int horizontalGravity;
    switch (alignment) {
      default:
        throw new IllegalArgumentException();

      case Component.ALIGNMENT_NORMAL:
        horizontalGravity = Gravity.LEFT;
        break;

      case Component.ALIGNMENT_CENTER:
        horizontalGravity = Gravity.CENTER_HORIZONTAL;
        break;

      case Component.ALIGNMENT_OPPOSITE:
        horizontalGravity = Gravity.RIGHT;
        break;
    }
    int verticalGravity = centerVertically ? Gravity.CENTER_VERTICAL : Gravity.TOP;
    textview.setGravity(horizontalGravity | verticalGravity);
    textview.invalidate();
  }

  /**
   * {@link TextView} background color setter.  Generally, the caller will
   * not pass {@link Component#COLOR_DEFAULT}, instead substituting in the
   * appropriate color.
   *
   * @param textview   text view instance
   * @param argb  background RGB color with alpha
   */
  public static void setBackgroundColor(TextView textview, int argb) {
    textview.setBackgroundColor(argb);
    textview.invalidate();
  }

  /**
   * Returns the enabled state a {@link TextView}.
   *
   * @param textview   text view instance
   * @return  {@code true} for enabled, {@code false} disabled
   */
  public static boolean isEnabled(TextView textview) {
    return textview.isEnabled();
  }

  /**
   * Enables a {@link TextView}.
   *
   * @param textview   text view instance
   * @param enabled  {@code true} for enabled, {@code false} disabled
   */
  public static void setEnabled(TextView textview, boolean enabled) {
    textview.setEnabled(enabled);
    textview.invalidate();
  }

  /**
   * Returns the font size for a {@link TextView}.
   *
   * @param textview   text view instance
   * @return  font size in pixel
   */
  public static float getFontSize(TextView textview) {
    return textview.getTextSize();
  }

  /**
   * Sets the font size for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param size  font size in pixel
   */
  public static void setFontSize(TextView textview, float size) {
    textview.setTextSize(size);
    textview.requestLayout();
  }

  /**
   * Sets the font typeface for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param typeface  one of @link Component#TYPEFACE_DEFAULT},
   *                  {@link Component#TYPEFACE_SERIF},
   *                  {@link Component#TYPEFACE_SANSSERIF} or
   *                  {@link Component#TYPEFACE_MONOSPACE}
   * @param bold true for bold, false for not bold
   * @param italic true for italic, false for not italic
   */
  public static void setFontTypeface(TextView textview, int typeface,
      boolean bold, boolean italic) {
    Typeface tf;
    boolean loadedFont = false;
    switch (typeface) {
      default:
        throw new IllegalArgumentException();

      case Component.TYPEFACE_DEFAULT:
        tf = Typeface.DEFAULT;
        break;

      case Component.TYPEFACE_SERIF:
        tf = Typeface.SERIF;
        break;

      case Component.TYPEFACE_SANSSERIF:
        tf = Typeface.SANS_SERIF;
        break;

      case Component.TYPEFACE_MONOSPACE:
        tf = Typeface.MONOSPACE;
        break;
      case Component.TYPEFACE_ROBOTO: //GlassInventor: Handle Roboto in plain, thin and light
      case Component.TYPEFACE_ROBOTO_LIGHT:
      case Component.TYPEFACE_ROBOTO_THIN:
        tf = loadFont(textview.getContext(), typeface, bold, italic);
        loadedFont = true;
        break;
    }

    int style = 0;
    if (bold) {
      style |= Typeface.BOLD;
    }
    if (italic) {
      style |= Typeface.ITALIC;
    }
    textview.setTypeface(loadedFont? tf: Typeface.create(tf, style));
    textview.requestLayout();
  }

  /**
   * Returns the text for a {@link TextView}.
   *
   * @param textview   text view instance
   * @return  text shown in text view
   */
  public static String getText(TextView textview) {
    return textview.getText().toString();
  }

  /**
   * Sets the text for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param text  new text to be shown
   */
  public static void setText(TextView textview, String text) {
    textview.setText(text);
    textview.requestLayout();
  }

  /**
   * Sets the text color for a {@link TextView}.
   *
   * @param textview   text view instance
   * @param argb  text RGB color with alpha
   */
  public static void setTextColor(TextView textview, int argb) {
    textview.setTextColor(argb);
    textview.invalidate();
  }

  public static void setTextColors(TextView textview, ColorStateList colorStateList) {
    textview.setTextColor(colorStateList);
  }

  private static Typeface loadFont(Context context, int fontType, boolean bold, boolean italic) {
    String fontPostfix = "";
    if (bold) {
      fontPostfix = "Bold";
    }
    if (italic) {
      fontPostfix += "Italic";
    }
    if (!bold && !italic && fontType == Component.TYPEFACE_ROBOTO) {
      fontPostfix = "Regular";
    }
    String fontName = null;
    switch(fontType) {
      case Component.TYPEFACE_ROBOTO: //GlassInventor: Handle Roboto in plain, thin and light
        fontName = "Roboto-";
        break;
      case Component.TYPEFACE_ROBOTO_LIGHT:
        fontName = "Roboto-Light";
        break;
      case Component.TYPEFACE_ROBOTO_THIN:
        fontName = "Roboto-Thin";
        break;
    }
    String fontPath = fontName + fontPostfix;
    Typeface retval = fontCache.get(fontPath);
    if (retval == null) {
      try {
        retval = Typeface.createFromAsset(context.getAssets(), "fonts/" + fontPath + ".ttf");
      } catch (Exception e) {
        retval = Typeface.DEFAULT;
        e.printStackTrace();
        System.err.println("Font name: " + "fonts/" + fontPath + ".ttf");
      }
      fontCache.put(fontPath, retval);
    }
    return retval;
  }
  private static Map<String, Typeface> fontCache = new HashMap<String, Typeface>();
}
