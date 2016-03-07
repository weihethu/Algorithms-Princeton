import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;


public class Board implements Cloneable {
    private int[][] blocks;
    private int xBlank;
    private int yBlank;
    
    public Board(int[][] blocks) {
        if (blocks == null || blocks.length <= 1 || blocks[0].length <= 1 || blocks.length != blocks[0].length) {
            throw new IllegalArgumentException();
        }
        this.blocks = new int[blocks.length][blocks[0].length];
        
        for (int i = 0; i < dimension(); i++) {
            this.blocks[i] = Arrays.copyOf(blocks[i], dimension());
        }
        
        for (int y = 0; y < dimension(); y++) {
            for (int x = 0; x < dimension(); x++) {
                if (blocks[y][x] == 0) {
                    yBlank = y;
                    xBlank = x;
                }
            }
        }
    }
    
    public int dimension() {
        return this.blocks.length;
    }
    
    public int hamming() {
        int result = 0;
        for (int y = 0; y < dimension(); y++) {
            for (int x = 0; x < dimension(); x++) {
                if (blocks[y][x] != 0 && (blocks[y][x] != y * dimension() + x + 1)) {
                    result++;
                }
            }
        }
        return result;
    }
    
    public int manhattan() {
        int result = 0;
        for (int y = 0; y < dimension(); y++) {
            for (int x = 0; x < dimension(); x++) {
                if (blocks[y][x] != 0) {
                    int xInGoal = (blocks[y][x] - 1) % dimension();
                    int yInGoal = (blocks[y][x] - 1) / dimension();
                    
                    result += (Math.abs(xInGoal - x) + Math.abs(yInGoal - y));
                }
            }
        }
        return result;
    }
    
    public boolean isGoal() {
        return hamming() == 0;
    }
    
    public Board twin() {
        boolean firstBlock = true;
        
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        
        for (int y = 0; y < dimension(); y++) {
            for (int x = 0; x < dimension(); x++) {
                if (y == yBlank && x == xBlank) {
                    continue;
                } else {
                    if (firstBlock) {
                        x1 = x;
                        y1 = y;
                        firstBlock = false;
                    } else {
                        x2 = x;
                        y2 = y;
                        break;
                    }
                }
            }
        }
        
        swap(x1, y1, x2, y2);
        Board twin = new Board(this.blocks);
        swap(x1, y1, x2, y2);
        return twin;
    }
    
    private void swap(int x1, int y1, int x2, int y2) {        
        int tmp = this.blocks[y1][x1];
        this.blocks[y1][x1] = this.blocks[y2][x2];
        this.blocks[y2][x2] = tmp;
    }
    
    public boolean equals(Object y) {
        if (y == null) {
            return false;
        }
        if (y == this) {
            return true;
        }
        if (!(y instanceof Board)) {
            return false;
        }
        
        Board yCasted = (Board)y;
        if (this.dimension() != yCasted.dimension()) {
            return false;
        }
        
        return this.toString().equals(yCasted.toString());
    }
    
    public Iterable<Board> neighbors() {
        int[] dx = {-1, 1, 0, 0};
        int[] dy = {0, 0, -1, 1};
        
        Set<Board> neighborsResults = new HashSet<Board>();
        
        for (int i = 0; i < dx.length; i++) {
            int xMove = xBlank + dx[i];
            int yMove = yBlank + dy[i];
            
            if (withinBoard(xMove, yMove)) {
                swap(xBlank, yBlank, xMove, yMove);
                neighborsResults.add(new Board(blocks));
                swap(xBlank, yBlank, xMove, yMove);
            }
        }
        
        return neighborsResults;
    }
    
    public String toString() {
        StringBuilder sb = new StringBuilder();
        
        sb.append(dimension());
        sb.append('\n');
        for (int y = 0; y < dimension(); y++) {
            sb.append(' ');
            for (int x = 0; x < dimension(); x++) {
                if (x > 0) {
                    sb.append("  ");
                }
                sb.append(this.blocks[y][x]);
            }
            sb.append('\n');
        }
        sb.append('\n');
        
        return sb.toString();
    }
    
    private boolean withinBoard(int x, int y) {
        return x >= 0 && x < dimension() && y >= 0 && y < dimension();
    }
    
    public static void main(String[] args) {
        
    }

    public Board clone() {
        return new Board(this.blocks);
    }
}
