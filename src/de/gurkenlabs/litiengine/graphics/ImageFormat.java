package de.gurkenlabs.litiengine.graphics;

import java.util.ArrayList;

public enum ImageFormat {
  UNDEFINED, PNG, GIF, BMP, JPG;

  public static ImageFormat get(String imageFormat) {
    if (imageFormat == null || imageFormat.isEmpty()) {
      return UNDEFINED;
    }

    String stripedImageFormat = imageFormat;
    if (stripedImageFormat.startsWith(".")) {
      stripedImageFormat = imageFormat.substring(1);
    }

    for (ImageFormat val : values()) {
      if (stripedImageFormat.equalsIgnoreCase(val.toString())) {
        return val;
      }
    }

    return UNDEFINED;
  }

  public static String[] getAllExtensions() {
    ArrayList<String> arrList = new ArrayList<>();
    for (ImageFormat format : values()) {
      if (format != ImageFormat.UNDEFINED) {
        arrList.add(format.toString());
      }
    }

    return arrList.toArray(new String[arrList.size()]);
  }

  public String toExtension() {
    return "." + this.name().toLowerCase();
  }

  @Override
  public String toString() {
    return this.name().toLowerCase();
  }
}
