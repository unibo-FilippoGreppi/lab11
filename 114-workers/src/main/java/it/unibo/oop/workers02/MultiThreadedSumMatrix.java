package it.unibo.oop.workers02;

import java.util.Objects;
import java.util.stream.IntStream;
/**
 * Implementation using streams.
 */
public class MultiThreadedSumMatrix implements SumMatrix {
    private final int nthread;

    /**
     * 
     * @param nthread
     *            no. of thread performing the sum.
     */
    public MultiThreadedSumMatrix(final int nthread) {
        Objects.requireNonNull(nthread);
        if (nthread <= 0) {
            throw new IllegalArgumentException("n must be a positive integer");
        }
        this.nthread = nthread;
    }

    /**
     * 
     */
    @Override
    public final double sum(final double[][] matrix) {
        Objects.requireNonNull(matrix);
        final int size = matrix.length % nthread + matrix.length / nthread;
        /*
         * Build a stream of workers
         */
        return IntStream
            .iterate(0, start -> start + size)
            .limit(nthread)
            .mapToObj(start -> new Worker(matrix, start, size))
            // Start them
            .peek(Thread::start)
            // Join them
            .peek(MultiThreadedSumMatrix::joinUninterruptibly)
            // Get their result and sum
           .mapToDouble(Worker::getResult)
           .sum();
    }

    private static class Worker extends Thread {
        private final double[][] matrix;
        private final int startpos;
        private final int nelem;
        private double res;

        /**
         * Build a new worker.
         * 
         * @param matrix
         *            the matrix to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final double[][] matrix, final int startpos, final int nelem) {
            super();
            this.matrix = matrix.clone();
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < matrix.length && i < startpos + nelem; i++) {
                for (int j = 0; j < matrix[i].length; j++) {
                    this.res = this.res + this.matrix[i][j];
                }
            }
        }

        /**
         * @return the sum of every element in the matrix.
         */
        public double getResult() {
            return this.res;
        }
    }

    @SuppressWarnings("PMD.AvoidPrintStackTrace")
    private static void joinUninterruptibly(final Thread target) {
        var joined = false;
        while (!joined) {
            try {
                target.join();
                joined = true;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
