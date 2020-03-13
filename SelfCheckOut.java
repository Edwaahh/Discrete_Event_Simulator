package cs2030.simulator;

/**
 * The SelfCheckOut class which is a subclass of servers
 * Keeps track of the customer being served by selfCheckOut servers (if any)
 * and who is the customer waiting to be served (if any).
 */
public class SelfCheckOut extends Server{
	SelfCheckOut(double time, int ID){
		super(time,ID,0);
	}
}