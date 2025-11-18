package problem.resolution;

import java.util.ArrayList;
import java.util.List;

public class Problem54 extends LeetCodeProblem{

    /**
     * 给你一个 m 行 n 列的矩阵 matrix ，请按照 顺时针螺旋顺序 ，返回矩阵中的所有元素。
     */
    public List<Integer> spiralOrder(int[][] matrix) {
        int xLen = matrix[0].length;
        int yLen = matrix.length;
        int size = xLen * yLen;

        List<Integer> res = new ArrayList<>();

        int xIndexStart = 0, xIndexEnd = xLen - 1;
        int yIndexStart = 1, yIndexEnd = yLen - 1;

        boolean isReverse = false;

        while (res.size() < size) {

            if (isReverse) {
                int debugCount = 1;
                for (int x = xIndexStart; x <= xIndexEnd && x > 0; x--) {
                    int v = matrix[yIndexStart + 1][x];
                    res.add(v);
                    debugCount++;
                }

                for (int y = yIndexStart; y <= yIndexEnd && y > 0; y--) {
                    res.add(matrix[y][xIndexEnd]);
                }

                int newXIndexEnd = xIndexStart;
                int newXIndexStart = xIndexEnd + 1;
                xIndexEnd = newXIndexEnd;
                xIndexStart = newXIndexStart;

                int newYIndexEnd = yIndexStart;
                yIndexStart = yIndexEnd - 1;
                yIndexEnd = newYIndexEnd;
                isReverse = false;
            } else {

                for (int x = xIndexStart; x <= xIndexEnd && x < xLen; x++) {
                    if (yIndexStart == 1) res.add(matrix[0][x]);
                    else res.add(matrix[yIndexStart][x]);
                }

                for (int y = yIndexStart; y <= yIndexEnd && y < yLen; y++) {
                    res.add(matrix[y][xIndexEnd]);
                }

                int newXIndexEnd = xIndexStart;
                xIndexStart = xIndexEnd - 1;
                xIndexEnd = newXIndexEnd;

                int newYIndexStart = yIndexEnd - 1;
                yIndexEnd = yIndexStart;
                yIndexStart = newYIndexStart;
                isReverse = true;
            }
        }

        return res;
    }

    @Override
    public void run() {

        testMatrix(new int[][]{
                {1,2,3,4},
                {5,6,7,8},
                {9,10,11,12}
        }, "多零元素矩阵");

    }

    private void testMatrix(int[][] matrix, String caseName) {
        System.out.println("=== " + caseName + " ===");
        System.out.println("原始矩阵:");
        printMatrix(matrix);

        System.out.println(spiralOrder(matrix));
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
        return "螺旋矩阵";
    }

    @Override
    protected Difficulty getDifficulty() {
        return Difficulty.MEDIUM;
    }
}
