package cs2030.simulator;
import cs2030.simulator.RandomGenerator;

/**
 * The Customer class encapsulates information and methods pertaining to a
 * Customer in a simulation.
 *
 */
public class Customer{
	
	/** The unique ID of this customer. */
	protected int ID;
	/** The timestamp of this customer. */ 
	protected double time;
	/** The state of this customer.
	 * 1:ARRIVED 2:SERVED 3:LEAVE 4:DONE 5:WAIT
	 */
	protected int state;
	/** The Server ID of this customer. */
	protected int servedBy;
    public static RandomGenerator generator;
    protected int selfCheckOut; //0 - normal , 1 - self checkout

    
  /**
   * Create and initalize a new customer.
   * The {@code id} of the customer is set.
   * @param state The state of the customer.
   * @param servedBy the Server ID of the customer.
   */
    public Customer(int ID, int state, int servedBy) {
		this.ID = ID;
		this.time = generator.genInterArrivalTime();
		this.state = state;
		this.servedBy = servedBy;
		this.selfCheckOut = 0;
	}
	
  /**
   * Alternative to create and initalize a new customer.
   * The {@code id} of the customer is set.
   * @param state The state of the customer.
   * @param time The time this customer arrived in the simulation.
   * @param servedBy the Server ID of the customer.
   */
	public Customer(int ID, double time, int state, int servedBy) {
		this.ID = ID;
		this.time = time;
		this.state = state;
		this.servedBy = servedBy;
		this.selfCheckOut = 0;
	}

	/**Flag to set Self checkout*/
	public void selfCheckOut(){
		this.selfCheckOut = 1;
	}

    public static void constructGenerator(int seed, double arrRate, double serviceRate, double restRate) {
        generator = new RandomGenerator(seed, arrRate, serviceRate, restRate);
    }
  /**
   * Return the ID of this customer.
   * @return ID of this customer.
   */
	public int getID() {
		return this.ID;
	}

  /**
   * Return the waiting time of this customer.
   * @return The waiting time of this customer.
   */
	public double getTime() {
		return this.time;
	}

	/**
   * Change the timestamp of this customer.
   * @return The timestamp of this customer.
   */
	public Customer changeTime(double time) { //for waiting case
		return new Customer(this.ID, time, this.state, this.servedBy);
	}
  /**
   * Return the state of this customer.
   * @return The state of this customer.
   */	
	public int getState() {
		return this.state;
	}

  /**
   * Return the server ID of this customer.
   * @return The ServerID of this customer.
   */
	public int getServer() {
		return this.servedBy;
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	public Customer makeServed(int serverID) {
        return new Customer(this.ID, this.time, 2 , serverID);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
    public Customer makeLeave() {
		return new Customer(this.ID, this.time, 3, this.servedBy);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	public Customer makeDone(int serverID) {
        return new Customer(this.ID, this.time + generator.genServiceTime(), 4, serverID);
	}

  /**
   * Change the state of this customer.
   * @return The new state of this customer.
   */
	public Customer makeWait(int serverID) {		
        return new Customer(this.ID, this.time, 5, serverID);
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
				temp = "arrives";
			} else if (this.state == 2){
				temp = "served by server " + Integer.toString(this.servedBy);
			} else if (this.state == 4){
				temp = "done serving by server " + Integer.toString(this.servedBy);
			} else if (this.state == 5){
				temp = "waits to be served by server " + Integer.toString(this.servedBy);
			}else{
				temp = "leaves" ;
			} 
			return String.format("%.3f",this.getTime()) + " " + this.ID + " " + temp;

		} else {
			String temp;
			if (this.state == 1){
				temp = "arrives";
			} else if (this.state == 2){
				temp = "served by self-check " + Integer.toString(this.servedBy);
			} else if (this.state == 4){
				temp = "done serving by self-check " + Integer.toString(this.servedBy);
			} else if (this.state == 5){
				temp = "waits to be served by self-check " + Integer.toString(this.servedBy);
			}else{
				temp = "leaves" ;
			}
			return String.format("%.3f",this.getTime()) + " " + this.ID + " " + temp; 
		}
	}
}
