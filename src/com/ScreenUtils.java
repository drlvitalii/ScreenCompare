package com;

import javax.imageio.ImageIO;


import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
* 1. Загрузить две картинки картинки
* 2. Разбить картинку на зоны (прямоугольники)
* 3. Создать массив соответствующий зонам картинки
* 4. Пройтись по  зонам  маленького массива и сравнить попиксельно каждую зону,
*       пометить зону если внутреннее содержимое отличается больше чем на указанный коефициент
* 5. Пройтись по маленькому помеченному массиву, промаркировать зоны
* 6. Пройтись по маркированным зонам, забрать крайние координаты для отрисовки прямоугольников
* 7. Сохранить результат работы в картинку.
 *
* */

public class ScreenUtils {
    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage imageResult;

    private static String absolutePath = "F:";
    private static String path1 = absolutePath + "\\image1.png";
    private static String path2 = absolutePath + "\\image2.png";
    private static String resultPath = absolutePath + "\\image3.png";

    public static final int NEAREST_PIXEL_GAP = 1;
    private static int zoneMarkerCounter = 1;
    private static double differenceKoef = 0.1;

    private static int xBlocksAmount = 40;
    private static int yBlocksAmount = 40;
    private static int subImageWidth;
    private static int subImageHeight;

    public static void main(String[] args) {

        try {
            image1 = downloadImage(path1);
            image2 = downloadImage(path2);
            imageResult = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);

            int[][] imageDifferenceArray = makeSubImageArrayWithDifferentCells(image1, image2);
//            printArray(imageDifferenceArray);
//            System.err.println("-----------------");
            zoneMarkerCounter = markDifferentSections(imageDifferenceArray, zoneMarkerCounter);
//            printArray(imageDifferenceArray);
            List<Zone> zoneList = new ArrayList<>();
            for (int i = 1; i < zoneMarkerCounter; i++) {
                Zone rec = getRectangleCoordinates(imageDifferenceArray, i);
                zoneList.add(rec);
            }
            drawRectangle(imageResult, zoneList);
            saveImage(imageResult, resultPath);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] makeSubImageArrayWithDifferentCells(BufferedImage image1, BufferedImage image2) {
        int[][] subImagesArray = new int[yBlocksAmount][xBlocksAmount];
        subImageWidth = image1.getWidth() / xBlocksAmount;
        subImageHeight = image1.getHeight() / yBlocksAmount;

//        System.out.println("width: " + image1.getWidth());
//        System.out.println("height: " + image1.getHeight());
//        System.out.println("subImageWidth: " + subImageWidth);
//        System.out.println("subImageHeight: " + subImageHeight);
//        System.out.println("xBlocksAmount: " + xBlocksAmount);
//        System.out.println("yBlocksAmount: " + yBlocksAmount);

        for (int y = 0; y < yBlocksAmount; y++) {
            for (int x = 0; x < xBlocksAmount; x++) {
                int subImage1[][] = getSubImageArray(image1.getSubimage(x * subImageWidth, y * subImageHeight, subImageWidth - 1, subImageHeight - 1));
                int subImage2[][] = getSubImageArray(image2.getSubimage(x * subImageWidth, y * subImageHeight, subImageWidth - 1, subImageHeight - 1));

                if (isSubImagesDifferent(subImage1, subImage2)) {
                    subImagesArray[y][x] = -1;
//                    System.out.println("  y-> " + (y) + " x-> " + (x));
                }
            }
        }
        return subImagesArray;
    }

    public static boolean isSubImagesDifferent(int[][] subImage1, int[][] subImage2) {
        for (int i = 0; i < subImage1.length; i++) {
            for (int j = 0; j < subImage1[0].length; j++) {
//                int diff = Math.abs(subImage1[i][j] - subImage2[i][j]);
//                if (diff / Math.abs(subImage1[i][j]) > differenceKoef) {
//                    return true;
//                }
                if (subImage1[i][j] != subImage2[i][j]) {
                    return true;
                }
            }
        }
        return false;
    }

    public static int[][] getSubImageArray(BufferedImage subImage) {
        int width = subImage.getWidth();
        int height = subImage.getHeight();
        int[][] subImageArray = new int[height][width];

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                subImageArray[row][col] = subImage.getRGB(col, row);
            }
        }
        return subImageArray;
    }

    public static Zone getRectangleCoordinates(int[][] ar, int region) {
        Zone reg = new Zone();
        reg.regionNumber = region;
        boolean isFirstCoordSet = true;
        for (int y = 0; y < ar.length; y++) {
            for (int x = 0; x < ar[y].length; x++) {
                if (ar[y][x] == reg.regionNumber) {
                    if (isFirstCoordSet) {
                        reg.startX = x * subImageWidth;
                        reg.startY = y * subImageHeight;
                        reg.endX = x * subImageWidth + (subImageWidth - 1);
                        reg.endY = y * subImageHeight + (subImageHeight - 1);
                        isFirstCoordSet = false;
                    }
                    int x1 = x * subImageWidth;
                    int y1 = y * subImageHeight;
                    reg.startX = (x1 < reg.startX ? x1 : reg.startX);
                    reg.startY = (y1 < reg.startY ? y1 : reg.startY);
                    reg.endX = (x1 + (subImageWidth - 1) > reg.endX ? x1 + (subImageWidth - 1) : reg.endX);
                    reg.endY = (y1 + (subImageHeight - 1) > reg.endY ? y1 + (subImageHeight - 1) : reg.endY);
                }
            }
        }
        return reg;
    }

    public static int markDifferentSections(int[][] array, int zoneMarker) {
        for (int y = 0; y < array.length; y++) {
            for (int x = 0; x < array[y].length; x++) {
                if (array[y][x] == -1) {
                    recur(array, y, x, zoneMarker);
                    zoneMarker++;
                }
            }
        }
        return zoneMarker;
    }

    public static void recur(int[][] array, int i, int j, int zoneMarker) {
        try {
            array[i][j] = zoneMarker;
            for (int y = i - NEAREST_PIXEL_GAP; y <= i + NEAREST_PIXEL_GAP; y++) {
                for (int x = j - NEAREST_PIXEL_GAP; x <= j + NEAREST_PIXEL_GAP; x++) {
                    if (array[y][x] == -1) {
                        recur(array, y, x, zoneMarker);
                    }
                }
            }
        } catch (IndexOutOfBoundsException ignored) {
        }
    }

    public static void printArray(int[][] ar) {

        for (int y = 0; y < ar.length; y++) {
            for (int x = 0; x < ar[y].length; x++) {
                if (ar[y][x] == -1) {
                    System.out.print("+");
                } else if (ar[y][x] == 0) {
                    System.out.print(ar[y][x]);
                } else {
                    System.out.print(ar[y][x]);
                }
            }
            System.out.println();
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
}
