package tester;
import static org.junit.Assert.*;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import student.StudentArrayDeque;

public class TestArrayDequeEC {
    @Test
    public void randomizedTest() {
        ArrayDequeSolution<Integer> A = new ArrayDequeSolution<>();
        StudentArrayDeque<Integer> S = new StudentArrayDeque<>();
        int N = 5000;
        StringBuilder message = new StringBuilder();
        for (int i = 0; i < N; i++) {
            int method = StdRandom.uniform(0, 4);
            if (method == 0) {
                int value = StdRandom.uniform(0, 100);
                A.addFirst(value);
                S.addFirst(value);
                message.append("addFirst(").append(value).append(")\n");
            }
            if (method == 1) {
                int value = StdRandom.uniform(0, 100);
                A.addLast(value);
                S.addLast(value);
                message.append("addLast(").append(value).append(")\n");
            }
            if (method == 2) {
                if (A.isEmpty() && S.isEmpty()) continue;
                Integer n1 = A.removeFirst();
                Integer n2 = S.removeFirst();
                message.append("removeFirst()\n");
                assertEquals(String.valueOf(message),n1,n2);
            }
            if (method == 3) {
                if (A.isEmpty() && S.isEmpty()) continue;
                Integer n1 = A.removeLast();
                Integer n2 = S.removeLast();
                message.append("removeLast()\n");
                assertEquals(String.valueOf(message),n1,n2);
            }
        }

    }
}
