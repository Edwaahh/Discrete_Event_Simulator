package cs2030.simulator;

import java.util.ArrayList;

/**
 * The Server class keeps track of who is the customer being served (if any)
 * and who is the customer waiting to be served (if any).
 */
public class Server{
	/** The timestamp of this server. */
	private double time;

	/** The unique ID of this server. */
	private int ID;

	/** The ArrayList representing the queue of this server.*/
	private ArrayList<Customer> waitList;
	
	/** The state of this server. 0:initial 1: resting 2: back to work. */
	int state; //0 initial, 1 resting, 2 back to work

  /**
   * Creates a server and initalizes it with a unique id.
   * @param time The timestamp og the server.
   * @param ID The ID of ther server.
   * @param state The state of the server,
   */
	Server(double time, int ID, int state){
		this.time = time;
		this.ID = ID;
		waitList = new ArrayList<Customer>();
		this.state = state;
	}

  /**
   * Change this server's state to resting.
   * @return A new server with state = 1.
   */
	public Server makeRest(){
		Server newServer = new Server(this.time, this.ID, 1);
		newServer.setArray(this.waitList);
		return newServer; 
	}

  /**
   * Change this server's state to resting.
   * @return A new server with state = 2.
   */
	public Server makeBack(double timeBack){
		Server newServer = new Server(timeBack, this.ID, 2);
		newServer.setArray(this.waitList);
		return newServer;
	}

  /**
   * Get state of server
   * @return servers state.
   */
	public int getState(){
		return this.state;
	}

  /**
   * Set the server's queue.
   * @param arr The ArrayList to set as queue.
   * @return A new server with ArrayList arr.
   */
	public void setArray(ArrayList<Customer> arr){
		this.waitList = arr;
	}

  /**
   * get the server's queue.
   * @return A server's wait list.
   */
	public ArrayList<Customer> getWaitList(){
		return this.waitList;
	}

  /**
   * get the server's ID.
   * @return A server's ID.
   */
	public int getID(){
		return this.ID;
	}

  /**
   * Add customer to server's queue.
   * @param customer The customer to add to the queue.
   */
	public void addWait(Customer customer){
		waitList.add(customer);
	}

   /**
    *Get Customer from server's queue.
    */
	public Customer getCust(){
		return waitList.get(0);
	}

   /**
    * Remove Customer from server's queue.
    */
	public void removeCust(){
		waitList.remove(0);
	}

   /**
    *Get the size of server's queue.
    * @return size of queue.
    */
	public int getWaitListSize(){
		return waitList.size();
	}

   /**
    *Get the time stamp of server.
    * @return time stamp of server.
    */	
	public double getTime(){
		return this.time;
	}

   /**
    * Check if server is free to serve.
    * @return true if free.
    */	
	public boolean isAvailable(double t){
		return t >= this.time;
	}

   /**
    * Update the time of server
    * @param The time to update to
    */
	public void updateTime(double t){
		this.time = t;
	}
  /**
   * Return a string representation of this server.
   * @return A string S followed by the ID of the server
   */
	@Override
	public String toString() {
		if (this.state == 1) {
			String temp = String.format("%.3f", this.time) + " server " + this.ID + " rest";
			return temp;
		} else {
			String temp = String.format("%.3f", this.time) + " server " + this.ID + " back";
			return temp;
		}
	}

}

