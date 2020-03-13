package cs2030.simulator;

/**
 * The GreedyCustomer class encapsulates information and methods pertaining to a
 * GreedyCustomer in a simulation.
 *
 */

public class GreedyCustomer extends Customer {
	public GreedyCustomer(int ID, int state, int servedBy){
		super(ID,state,servedBy);
	}

	public GreedyCustomer(int ID, double time, int state, int servedBy){
		super(ID, time, state, servedBy);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */	
	@Override
	public GreedyCustomer makeServed(int serverID) {
        return new GreedyCustomer(this.ID, this.time, 2, serverID); 
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	@Override
    public GreedyCustomer makeLeave() {
		return new GreedyCustomer(this.ID, this.time, 3, this.servedBy);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	@Override
	public GreedyCustomer makeDone(int serverID) {
        return new GreedyCustomer(this.ID, this.time + generator.genServiceTime(), 4, serverID);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	@Override
	public GreedyCustomer makeWait(int serverID) {		
        return new GreedyCustomer(this.ID, this.time, 5, serverID);
    }

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
    @Override
    public GreedyCustomer changeTime(double time) {//for waiting case
		return new GreedyCustomer(this.ID, time, this.state, this.servedBy);
	}

  /**
   * Return a string representation of this customer.
   * @return The id of the customer
   */
	@Override
	public String toString(){
		if(this.selfCheckOut == 0){
			String temp;
			if (this.state == 1){
				temp = "(greedy) arrives";
			} else if (this.state == 2){
				temp = "(greedy) served by server " + Integer.toString(this.servedBy);
			} else if (this.state == 4){
				temp = "(greedy) done serving by server " + Integer.toString(this.servedBy);
			} else if (this.state == 5){
				temp = "(greedy) waits to be served by server " + Integer.toString(this.servedBy);
			}else{
				temp = "(greedy) leaves" ;
			} 
			return String.format("%.3f",this.getTime()) + " " + this.ID + temp;

		} else {
			String temp;
			if (this.state == 1){
				temp = "(greedy) arrives";
			} else if (this.state == 2){
				temp = "(greedy) served by self-check " + Integer.toString(this.servedBy);
			} else if (this.state == 4){
				temp = "(greedy) done serving by self-check " + Integer.toString(this.servedBy);
			} else if (this.state == 5){
				temp = "(greedy) waits to be served by self-check " + Integer.toString(this.servedBy);
			}else{
				temp = "(greedy) leaves" ;
			}
			return String.format("%.3f",this.getTime()) + " " + this.ID  + temp; 
		}
	}

}