package deque;

import edu.princeton.cs.algs4.StdRandom;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Created by hug.
 */


public class ArrayTest {
    @Test
    public void addThreeRemoveThree() {
        AListNoResizing<Integer> a = new AListNoResizing<>();
        ArrayDeque<Integer> b = new ArrayDeque<>();

        a.addLast(5); a.addLast(10); a.addLast(15);
        b.addLast(5); b.addLast(10); b.addLast(15);

        assertEquals(a.size(), b.size());

        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());
        assertEquals(a.removeLast(), b.removeLast());

    }

    @Test
    public void randomizedTest() {
        AListNoResizing<Integer> L = new AListNoResizing<>();
        ArrayDeque<Integer> B = new ArrayDeque<>();

        int N = 50000;
        for (int i = 0; i < N; i += 1) {
            int operationNumber = StdRandom.uniform(0, 3);
            if (operationNumber == 0) {
                // addLast
                int randVal = StdRandom.uniform(0, 100);
                L.addLast(randVal);
                B.addLast(randVal);
                System.out.println("addLast(" + randVal + ")");
            } else if (operationNumber == 1) {
                // size
                int size = L.size();
                int sizeB = B.size();
                assertEquals(size, sizeB);
                System.out.println("size: " + size);
            } else if (operationNumber == 2) {
                if (L.size() == 0 || B.size() == 0) {
                    continue;
                }
                int last = L.removeLast();
                int lastB = B.removeLast();
                assertEquals(last, lastB);
                System.out.println("removeLast: " + last);
            }
        }
    }
}
