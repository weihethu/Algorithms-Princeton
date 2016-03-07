import java.util.Arrays;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private static int[] di = { 0, 0, -1, 1 };
    private static int[] dj = { 1, -1, 0, 0 };

    private WeightedQuickUnionUF uf = null;
    private WeightedQuickUnionUF ufBackWash = null;
    private int N;
    private int virtualStartIndex;
    private int virtualEndIndex;
    private boolean[][] sitesOpen;

    public Percolation(int N) {
        if (N <= 0) {
            throw new IllegalArgumentException();
        }

        this.N = N;
        uf = new WeightedQuickUnionUF(2 + N * N);
        ufBackWash = new WeightedQuickUnionUF(1 + N * N);
        virtualStartIndex = 0;
        virtualEndIndex = N * N + 1;

        sitesOpen = new boolean[N][N];
        for (int i = 0; i < N; i++) {
            Arrays.fill(sitesOpen[i], false);
        }
    }

    public void open(int i, int j) {
        throwExceptionOnInvalidPositionParameters(i, j);
        if (sitesOpen[i - 1][j - 1]) {
            return;
        }

        sitesOpen[i - 1][j - 1] = true;

        int position = positionToIndex(i, j);
        for (int index = 0; index < di.length; index++) {
            int i1 = i + di[index];
            int j1 = j + dj[index];

            if (checkValidPositionParameters(i1, j1) && isOpen(i1, j1)) {
                int position1 = positionToIndex(i1, j1);
                uf.union(position, position1);
                ufBackWash.union(position, position1);
            }
        }

        if (i == 1) {
            uf.union(virtualStartIndex, position);
            ufBackWash.union(virtualStartIndex, position);
        }

        if (i == N) {
            uf.union(virtualEndIndex, position);
        }
    }

    public boolean isOpen(int i, int j) {
        throwExceptionOnInvalidPositionParameters(i, j);
        return sitesOpen[i - 1][j - 1];
    }

    public boolean isFull(int i, int j) {
        throwExceptionOnInvalidPositionParameters(i, j);
        return isOpen(i, j)
                && ufBackWash.connected(positionToIndex(i, j),
                        virtualStartIndex);
    }

    public boolean percolates() {
        return uf.connected(virtualStartIndex, virtualEndIndex);
    }

    private boolean checkValidPositionParameters(int i, int j) {
        return i >= 1 && i <= N && j >= 1 && j <= N;
    }

    private void throwExceptionOnInvalidPositionParameters(int i, int j) {
        if (!checkValidPositionParameters(i, j)) {
            throw new IndexOutOfBoundsException();
        }
    }

    private int positionToIndex(int i, int j) {
        return N * (i - 1) + j;
    }
}
