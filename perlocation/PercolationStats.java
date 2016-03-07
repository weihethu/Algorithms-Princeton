import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

    private double[] thresholdSamples;
    private int T;

    public PercolationStats(int N, int T) {
        if (N <= 0 || T <= 0) {
            throw new IllegalArgumentException();
        }

        this.T = T;
        thresholdSamples = new double[T];

        for (int experimentIndex = 0; experimentIndex < T; experimentIndex++) {
            Percolation percolation = new Percolation(N);

            int openCnt = 0;
            while (true) {
                while (true) {
                    int rowIndex = StdRandom.uniform(N) + 1;
                    int colIndex = StdRandom.uniform(N) + 1;

                    if (!percolation.isOpen(rowIndex, colIndex)) {
                        percolation.open(rowIndex, colIndex);
                        openCnt++;
                        break;
                    }
                }

                if (percolation.percolates()) {
                    thresholdSamples[experimentIndex] = (double) openCnt
                            / (N * N);
                    break;
                }
            }
        }
    }

    public double mean() {
        return StdStats.mean(thresholdSamples);
    }

    public double stddev() {
        return StdStats.stddev(thresholdSamples);
    }

    public double confidenceLo() {
        return mean() - 1.96 * stddev() / Math.sqrt(T);
    }

    public double confidenceHi() {
        return mean() + 1.96 * stddev() / Math.sqrt(T);
    }

    public static void main(String[] args) {
        PercolationStats stats = new PercolationStats(
                Integer.parseInt(args[0]), Integer.parseInt(args[1]));

        System.out.println("mean                    = " + stats.mean());
        System.out.println("stddev                  = " + stats.stddev());
        System.out.println("95% confidence interval = " + stats.confidenceLo()
                + ", " + stats.confidenceHi());
    }
}
