package cs2030.simulator;

/**
 * The Event class stores information of each event that has occured for both
 * server and customer whenever there is a change in state of the server or customer.
 */
public class Event{

	/** The timestamp of this event. */ 
	private double time;

	/** The Customer in this event.*/ 
	private Customer customer;

	/** Flag to differentiate customer event from server event */ 
	private int type;

	/** The Server in the event */ 
	private Server server;


  /**
   * Create and initalize a new customer event.
   * @param customer The customer in the event
   */
	public Event(Customer customer){
		this.time = customer.getTime();
		this.customer = customer;
		this.type = 1;
	}
  /**
   * Create and initalize a new sever event.
   * @param server The server in the event
   */
	public Event(Server server){
		this.time = server.getTime();
		this.server = server;
		this.type = 2;
	}

  /**
   * Return the type of this event
   * @return int type of event, 1 for customer, 2 for server.
   */
	public int getType(){
		return this.type;
	}

  /**
   * Return the state of the customer in the event.
   * @return state of this customer.
   */
	public int getCustState(){
		return this.customer.getState();
	}
  /**
   * Return the Customer in the event
   * @return The customer in the event.
   */
	public Customer getCust(){
		return this.customer;
	}
  /**
   * Return the timestamp of this event.
   * @return The time stamp of the event.
   */
	public double getTime(){
		return this.time;
	}
  /**
   * Return the Server in this event.
   * @return The Server in the event.
   */
	public Server getServer(){
		return this.server;
	}
}