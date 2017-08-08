package com;


public class TempMain {
    private static int ROWS = 20;
    private static int COLUMNS = 50;
    private static int counter = 1;


    public static void main(String[] args) {

        int[][] arrray = createArray(ROWS, COLUMNS);
        printArray(arrray);

        counter = ScreenUtils.markDifferentSections(arrray, counter);

        System.out.println("--------");
        printArray(arrray);
        for (int i = 1; i < counter; i++) {
            System.out.println(ScreenUtils.getCoordinates(arrray, i));
        }
        System.out.println("--------");
    }


    private static void printArray(int[][] ar) {

        for (int i = 0; i < ar.length; i++) {
            for (int j = 0; j < ar[i].length; j++) {
                if (ar[i][j] == -1) {
//                    System.out.print(ar[i][j]);
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
                } else if (i == 1 || i == 2) {
                    if (j > 0 && j < 6) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 3 || i == 4) {
                    if (j > 1 && j < 6) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 6) {
                    if (j > 2 && j < 5) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 7) {
                    if (j > 4) {
                        ar[i][j] = -1;
                    } else {
                        ar[i][j] = 0;
                    }
                } else if (i == 8) {
                    if (j > 6) {
                        ar[i][j] = -1;

                    } else {
                        ar[i][j] = 0;
                    }

                } else if (i == 11 || i == 12) {
                    if (j > 6 && j < 30) {
                        ar[i][j] = -1;

                    } else {
                        ar[i][j] = 0;
                    }

                } else if (i == 13 || i == 14) {
                    if (j > 6 && j < 20) {
                        ar[i][j] = -1;

                    } else {
                        ar[i][j] = 0;
                    }

                } else if (i == 17 || i == 18 || i == 19) {
                    if (j > 15 && j < 40) {
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
