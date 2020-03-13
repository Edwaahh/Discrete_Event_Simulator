package cs2030.simulator;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> { 
    
    public int compare(Event e1, Event e2) {
        
        if(e1.getTime() > e2.getTime()) {
            return 1;
        } else if(e1.getTime() < e2.getTime()) {
            return -1;
        } else { //same time
            if(e1.getCustState() == 1) {
                return 1;
            } else {
                return -1;
            }
        }
    }		
}