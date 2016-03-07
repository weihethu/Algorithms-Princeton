import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

public class Solver {

    private List<Board> steps;
    private boolean solvable;

    public Solver(Board initial) {
        if (initial == null) {
            throw new NullPointerException();
        }
        
        MinPQ<SearchNode> pq = new MinPQ<SearchNode>();
        pq.insert(new SearchNode(initial.clone()));

        MinPQ<SearchNode> pqNonReachable = new MinPQ<SearchNode>();
        pqNonReachable.insert(new SearchNode(initial.twin()));

        solvable = false;
        
        SearchNode last = null;
        while (true) {
            if (!pq.isEmpty()) {
                SearchNode current = pq.delMin();

                if (current.board.isGoal()) {
                    solvable = true;
                    last = current;
                    break;
                } else {
                    Iterable<Board> neighborBoards = current.board.neighbors();
                    
                    for (Board neighbor : neighborBoards) {
                        if (current.previous != null && current.previous.board.equals(neighbor)) {
                            continue;
                        }
                        pq.insert(new SearchNode(neighbor, current));
                    }
                }
            } else {
                solvable = true;
                break;
            }

            if (!pqNonReachable.isEmpty()) {
                SearchNode current = pqNonReachable.delMin();

                if (current.board.isGoal()) {
                    solvable = false;
                    break;
                } else {
                    Iterable<Board> neighborBoards = current.board.neighbors();
                    
                    for (Board neighbor : neighborBoards) {
                        if (current.previous != null && current.previous.board.equals(neighbor)) {
                            continue;
                        }
                        pqNonReachable.insert(new SearchNode(neighbor, current));
                    }
                }
            }
        }

        steps = new ArrayList<Board>();
        if (solvable) {
            while (last != null) {
                steps.add(last.board);
                last = last.previous;
            }
            Collections.reverse(steps);
        }
    }
    
    public boolean isSolvable() {
        return solvable;
    }
    
    public int moves() {
        if (isSolvable()) {
            return steps.size() - 1;
        } else {
            return -1;
        }
    }

    public Iterable<Board> solution() {
        if (isSolvable()) {
            return steps;
        } else {
            return null;
        }
    }

    public static void main(String[] args) {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}

class SearchNode implements Comparable<SearchNode> {
    public SearchNode previous;
    public int move;
    public Board board;

    public SearchNode(Board board) {
        this.board = board;
        this.move = 0;
        this.previous = null;
    }

    public SearchNode(Board board, SearchNode previous) {
        this.board = board;
        this.move = previous.move + 1;
        this.previous = previous;
    }

    @Override
    public int compareTo(SearchNode that) {
        int thisPriority = this.move + this.board.manhattan();
        int thatPriority = that.move + that.board.manhattan();

        return Integer.compare(thisPriority, thatPriority);
    }

}
