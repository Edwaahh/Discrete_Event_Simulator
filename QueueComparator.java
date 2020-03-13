package cs2030.simulator;

import java.util.Comparator;

public class QueueComparator implements Comparator<Server> {
    /**
     * Compares the queue size of waiters.
     */
    public int compare(Server s1, Server s2) {
        if (s1.getWaitListSize() > s2.getWaitListSize()) {
            return 1;
        } else if (s1.getWaitListSize() < s2.getWaitListSize()) {
            return -1;
        } else { //same size
            return s1.getID() - s2.getID();
        }
    }   
}