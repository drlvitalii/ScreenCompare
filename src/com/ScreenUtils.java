package com;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;


public class ScreenUtils {
    private static BufferedImage image1;
    private static BufferedImage image2;
    private static BufferedImage imageResult;

    private static String path1 = "F:\\image1.png";
    private static String path2 = "F:\\image2.png";
    private static String path3 = "F:\\image3.png";

    private static int SECTION_MARKER = 1;

    public static void main(String[] args) {
        ScreenUtils sc = new ScreenUtils();
        try {
            sc.image1 = downloadImage(path1);
            sc.image2 = downloadImage(path2);
            sc.imageResult = new BufferedImage(image1.getWidth(), image1.getHeight(), BufferedImage.TYPE_INT_RGB);
            int[][] imageDifferenceArray = makeArrayWithDifferentCells(sc.image1, sc.image2);
            int[][] imageDifferenceMarkedArray = markDifferentSections(imageDifferenceArray, SECTION_MARKER);
            Region rec1 = getCoordinates(imageDifferenceMarkedArray, 4);
            drawRectangle(imageResult, rec1);
            saveImage(imageResult, path3);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void drawRectangle(BufferedImage resultImage, Region rec) {
        Graphics2D g2 = resultImage.createGraphics();
        g2.drawImage(image2, null, null);
        g2.setColor(Color.RED);
        g2.drawRect(rec.startX, rec.startY, rec.endX - rec.startY, rec.endY - rec.startY);
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
            File outputfile = new File(filename);
            ImageIO.write(bimg, "png", outputfile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static int[][] makeArrayWithDifferentCells(BufferedImage image1, BufferedImage image2) {
        int width = image1.getWidth();
        int height = image1.getHeight();
        int resultArray[][] = new int[width][height];

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                if (image1.getRGB(i, j) == image2.getRGB(i, j)) {
                    resultArray[i][j] = 0;
//                    System.out.print("0");
                } else {
                    resultArray[i][j] = -1;
//                    System.err.print("+");
                }
            }
//            System.out.println();
        }
        return resultArray;
    }

    public static Region getCoordinates(int[][] ar, int region) {
        Region reg = new Region();
        reg.regionNumber = region;
        boolean isSetCoord = true;
        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == reg.regionNumber) {
                    if (isSetCoord) {
                        reg.startX = j;
                        reg.startY = i;
                        reg.endX = j;
                        reg.endY = i;
                        isSetCoord = false;
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

    public static int[][] markDifferentSections(int[][] array, int count) {
        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[i].length; j++) {
                if (array[i][j] == -1) {
                    array = recur(array, i, j, count);
                    count++;
                }
            }
        }
        return array;
    }

    public static int[][] recur(int[][] array, int i, int j, int count) {
        try {
            for (int k = i; k < i + 2; k++) {
                for (int l = j + 1; l < j + 2; l++) {
                    if (array[k][l] == -1 ) {
                        recur(array, k, l, count);
                        array[k][l] = count;
                    }
                }
            }
        } catch (IndexOutOfBoundsException e) {
        }
        return array;
    }
}
