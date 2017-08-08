package com;


public class TempMain {
    private static int ROWS = 7;
    private static int COLUMNS = 7;

    public static void main(String[] args) {

        int[][] arrray = createArray(ROWS, COLUMNS);
        printArray(arrray);

        ScreenUtils.markDifferentSections(arrray, 3);
        System.out.println(ScreenUtils.getCoordinates(arrray,1));
            System.out.println("--------");
        printArray(arrray);
        System.out.println("--------");
//        getRegion(ar, 3);
    }



    private static void printArray(int[][] ar) {

        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == -1) {
                    System.out.print(ar[i][j]);
//                    System.out.print("+");
                } else if (ar[i][j] == 0) {
                    System.out.print(ar[i][j]);
                } else {
                    System.out.print(ar[i][j]);
                }
            }
            System.out.println();
        }
    }

    public static int[][] createArray(int a, int b) {
        int[][] ar = new int[a][b];
        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (i == 0) {
                    if (j == 3) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 1) {
                    if (j > 0 && j < 6) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 2) {
                    if (j > 1 && j < 6) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 3) {
                    if (j > 2 && j < 5) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 5) {
                    if (j > 2) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 6) {
                    if (j > 3) {
                        ar[i][j] = -1;

                    } else {
                        ar[i][j] = 0;
                    }
                } else {
                    ar[i][j] = 0;
                }
            }
        }
        return ar;
    }




}
