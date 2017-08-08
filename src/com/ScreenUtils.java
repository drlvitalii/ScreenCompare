package com;

import javax.imageio.ImageIO;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class ScreenUtils {
    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage imageResult;

    private static String absolutePath = "F:";
    private static String path1 = absolutePath + "\\fur1.png";
    private static String path2 = absolutePath + "\\fur2.png";
    private static String resultPath = absolutePath + "\\fur3.png";

    public static final int NEAREST_PIXEL_GAP = 3;
    private static int zoneMarkerCounter = 1;

    public static void main(String[] args) {
        ScreenUtils sc = new ScreenUtils();
        try {
            sc.image1 = downloadImage(path1);
            sc.image2 = downloadImage(path2);
            sc.imageResult = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
            int[][] imageDifferenceArray = makeArrayWithDifferentCells(sc.image1, sc.image2);

            zoneMarkerCounter = markDifferentSections(imageDifferenceArray, zoneMarkerCounter);
            List<Zone> zoneList = new ArrayList<>();
            for (int i = 1; i < zoneMarkerCounter; i++) {
                Zone rec = getRectangeCoordinates(imageDifferenceArray, i);
                zoneList.add(rec);
            }
            drawRectangle(imageResult, zoneList);
            saveImage(imageResult, resultPath);


        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawRectangle(BufferedImage resultImage, List<Zone> zoneList) {
        Graphics2D g2 = resultImage.createGraphics();
        g2.drawImage(image2, null, null);
        g2.setColor(Color.RED);
        for (Zone rec : zoneList) {
            g2.drawRect(rec.startX, rec.startY, rec.endX - rec.startX, rec.endY - rec.startY);
        }

    }

    public static BufferedImage downloadImage(String filename) throws IOException {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(filename));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return img;

    }

    public static void saveImage(BufferedImage bimg, String filename) {
        try {
            File outPutFile = new File(filename);
            ImageIO.write(bimg, "png", outPutFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] makeArrayWithDifferentCells(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();
        int resultArray[][] = new int[height][width];

        for (int i = 0; i < height - 2; i++) {
            for (int j = 0; j < width - 2; j++) {
                if (image1.getRGB(j, i) == image2.getRGB(j, i)) {
                    resultArray[i][j] = 0;
                } else {
                    resultArray[i][j] = -1;
                }
            }
        }
        return resultArray;
    }

    public static Zone getRectangeCoordinates(int[][] ar, int region) {
        Zone reg = new Zone();
        reg.regionNumber = region;
        boolean isFirstCoordSet = true;
        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == reg.regionNumber) {
                    if (isFirstCoordSet) {
                        reg.startX = j;
                        reg.startY = i;
                        reg.endX = j;
                        reg.endY = i;
                        isFirstCoordSet = false;
                    }
                    reg.startX = (j < reg.startX ? j : reg.startX);
                    reg.startY = (i < reg.startY ? i : reg.startY);
                    reg.endX = (j > reg.endX ? j : reg.endX);
                    reg.endY = (i > reg.endY ? i : reg.endY);
                }
            }
        }
        return reg;
    }

    public static int markDifferentSections(int[][] array, int zoneMarker) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == -1) {
                    recur(array, i, j, zoneMarker);
                    zoneMarker++;
                }
            }
        }
        return zoneMarker;
    }

    public static void recur(int[][] array, int i, int j, int zoneMarker) {
        try {
            array[i][j] = zoneMarker;
            for (int k = i - NEAREST_PIXEL_GAP; k <= i + NEAREST_PIXEL_GAP; k++) {
                for (int l = j - NEAREST_PIXEL_GAP; l <= j + NEAREST_PIXEL_GAP; l++) {
                    if (array[k][l] == -1) {
                        recur(array, k, l, zoneMarker);
                    }
                }
            }
            return;
        } catch (IndexOutOfBoundsException e) {

        }
    }

    public static void printArray(int[][] ar) {

        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == -1) {
                    System.out.print("+");
                } else if (ar[i][j] == 0) {
                    System.out.print(ar[i][j]);
                } else {
                    System.out.print(ar[i][j]);
                }
            }
            System.out.println();
        }
    }
}
