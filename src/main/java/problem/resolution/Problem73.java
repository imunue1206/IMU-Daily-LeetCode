package problem.resolution;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Problem73 extends LeetCodeProblem{

    /**
     * 给定一个 m x n 的矩阵，如果一个元素为 0 ，则将其所在行和列的所有元素都设为 0 。请使用 原地 算法。
     */
    public void setZeroes(int[][] matrix) {

        // 标记，分为x轴, 因为y轴可以在标记过程中处理置零
        int xLen = matrix[0].length;
        int yLen = matrix.length;

        boolean[] xZeroIndex = new boolean[xLen];

        for (int y = 0; y < yLen; y++) {

            boolean currentRowContainsZero = false;

            for (int x = 0; x < xLen; x++) {
                if (matrix[y][x] == 0) {
                    currentRowContainsZero = true;
                    xZeroIndex[x] = true;
                }
            }

            if (currentRowContainsZero) {
                Arrays.fill(matrix[y], 0);
            }
        }

        // 列置空
        for (int y = 0; y < yLen; y++) {
            if (matrix[y][0] == 0) continue;
            for (int x = 0; x < xLen; x++) {
                if (xZeroIndex[x]) matrix[y][x] = 0;
            }
        }
    }

    @Override
    public void run() {
        // 测试用例1：普通矩阵
        testMatrix(new int[][]{
                {1, 1, 1},
                {1, 0, 1},
                {1, 1, 1}
        }, "单零元素矩阵");

        // 测试用例2：多零矩阵
        testMatrix(new int[][]{
                {0, 1, 2, 0},
                {3, 4, 5, 2},
                {1, 3, 1, 5}
        }, "多零元素矩阵");

        // 测试用例3：边缘零值
        testMatrix(new int[][]{
                {1, 0, 1},
                {1, 1, 1},
                {0, 1, 1}
        }, "边缘零值矩阵");

        // 测试用例4：全零矩阵
        testMatrix(new int[][]{
                {0, 0},
                {0, 0}
        }, "全零矩阵");

        // 测试用例5：单行矩阵
        testMatrix(new int[][]{
                {1, 0, 3}
        }, "单行矩阵");
    }

    private void testMatrix(int[][] matrix, String caseName) {
        System.out.println("=== " + caseName + " ===");
        System.out.println("原始矩阵:");
        printMatrix(matrix);

        setZeroes(matrix);

        System.out.println("处理后矩阵:");
        printMatrix(matrix);
        System.out.println();
    }

    private void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int num : row) {
                System.out.print(num + " ");
            }
            System.out.println();
        }
    }

    @Override
    protected String getTitle() {
        return "矩阵置零";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
