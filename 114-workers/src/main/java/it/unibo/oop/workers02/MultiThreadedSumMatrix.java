package it.unibo.oop.workers02;

import java.util.List;
import java.util.Objects;
import java.util.stream.DoubleStream;

public class MultiThreadedSumMatrix implements SumMatrix {
    private final int nthread;

    public MultiThreadedSumMatrix(final int nthread) {
        Objects.requireNonNull(nthread);
        if (nthread <= 0) {
            throw new IllegalArgumentException("n must be a positive integer");
        }
        this.nthread = nthread;
    }

    @Override
    public double sum(double[][] matrix) {
        

        return 0;
    }


    private static final class Worker extends Thread {
        private final List<Integer> list;
        private final int startpos;
        private final int nelem;
        private long res;

        /**
         * Build a new worker.
         * 
         * @param list
         *            the list to sum
         * @param startpos
         *            the initial position for this worker
         * @param nelem
         *            the no. of elems to sum up for this worker
         */
        Worker(final List<Integer> list, final int startpos, final int nelem) {
            super();
            this.list = list;
            this.startpos = startpos;
            this.nelem = nelem;
        }

        @Override
        @SuppressWarnings("PMD.SystemPrintln")
        public void run() {
            System.out.println("Working from position " + startpos + " to position " + (startpos + nelem - 1));
            for (int i = startpos; i < list.size() && i < startpos + nelem; i++) {
                this.res += this.list.get(i);
            }
        }

        /**
         * Returns the result of summing up the integers within the list.
         * 
         * @return the sum of every element in the array
         */
        public long getResult() {
            return this.res; 
        }

    }
}
