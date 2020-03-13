package cs2030.simulator;

/**
 * The Store class encapsulates information and methods pertaining to the events
 * happening in simulation.
 *
 */

import java.util.Comparator;
import java.util.ArrayList;
import java.util.PriorityQueue;

public class Store{
	/** The sequence of events in s simulation. */
	private PriorityQueue<Event> events;
	/** The ArrayList of servers in the Store. */
	private ArrayList<Server> servers;
	/** The maximum size of each queue. */
	private int qSize;

	/** The servers in the Store.*/
	private Server server;
	/** The resting rate of the servers in the Store.*/
	private double restRate;
	/** The probability of resting of the servers in the Store.*/
	private double pRest;
	/** The queue for the self check out counter.*/
	private ArrayList<Customer> selfCheckOutQ = new ArrayList<>();;
	/** Counter to keep track of total waiting time.*/
	double totalWaitingTime = 0.000;
	/** Counter to keep track of total customers served.*/
	int numServed = 0;
	/** Counter to keep track of total customers left.*/
	int numLeft = 0;


  /**
   * Create and initalize a Store.
   * The {@code n} of the numbers of servers in the store is set.
   * @param selfCheckout The number of self check out servers.
   * @param restRate The resting rate of human servers.
   * @param pRest The probability of a human server gouing to rest after serving
   */
	public Store(int n, int selfCheckOut, double restRate, double pRest){
		events = new PriorityQueue<Event>(new EventComparator());
		servers = new ArrayList<Server>();

		for(int i = 0 ; i < n ; i ++){ //human servers
			server = new Server(0.000, i+1, 0);
			servers.add(server);
		}
		this.restRate = restRate;
		this.pRest = pRest;

		for(int j = 0; j < selfCheckOut ; j++){ //machine servers
			SelfCheckOut selfie = new SelfCheckOut(0.000, n + j+1);
			servers.add(selfie);
		}
	}

   /**
    * Get the sequence of events.
    * @return the PQ of events.
    */
	public PriorityQueue<Event> getEvents(){
		return events;
	}
   /**
    * Get the ArrayList of servers.
    * @return the ArrayList of servers.
    */
	public ArrayList<Server> getServers(){
		return servers;
	}

   /**
    * Add events to the event PQ.
    */
	public void addEvent(Customer customer){
		Event event = new Event(customer);
		events.add(event);
	}
	
   /**
    * Update the maximum queue size of each simulation.
    */	
	public void updateQSize(int n){
		this.qSize = n;
	}

   /**
    * To get the event of a server after they are done serving a customer.
    * @param server The server that just finished serving a customer.
    */	
	private void schedule(Server server) {
        if (server.getState() == 1) {
            double time = Customer.generator.genRestPeriod();
            double timeBack = server.getTime() + time;
            server.updateTime(timeBack); //time server is free
            Server newServer = server.makeBack(timeBack); 
            servers.set(server.getID() - 1, newServer); //replace old server
            Event event = new Event(newServer); //back to work
            events.add(event);
        } else { //waiter status == 2, when he is back
            Server curr = servers.get(server.getID() - 1);
            
            if (curr.getWaitListSize() != 0) {
                double currTime = curr.getTime();
                //a customer
                Customer customer = curr.getCust(); 
                //to get the curr waiting time
                double customerWaitingTime = currTime - customer.getTime();
                //add to the total waiting time
                totalWaitingTime += customerWaitingTime;
                Customer newCustomer = customer.changeTime(currTime);
                Customer finalCustomer = newCustomer.makeServed(server.getID());
                Event event = new Event(finalCustomer);
                events.add(event); //Serve next
                curr.removeCust();
            }
        }
    } 
    


   /**
    * To get the event of a normal customer between states
    * @param c The customer that is being served.
    */	
	private void schedule(Customer c){

		if(c instanceof GreedyCustomer){
			schedule((GreedyCustomer) c);
		} else if (c.getState() == 1){ //customer arrives
			boolean flag = true;

			if(flag){

				if(servers.size() == 0){ //no servers
					Customer customer = c.makeLeave();
					Event event = new Event(customer);
					events.add(event);
				}

				for(int i = 0; i < servers.size(); i ++){

					Server currentServer = servers.get(i);
					if(currentServer.isAvailable(c.getTime())){ //finds a free servers
						Customer customer = c.makeServed(i+1);

						if(currentServer instanceof SelfCheckOut){
							customer.selfCheckOut(); //check if using self check out
							currentServer.setArray(selfCheckOutQ);
							//Event event = new Event(customer);
							//events.add(event);
						}
						
						Event event = new Event(customer); //serve
						events.add(event);
						flag = true;
						break;
					} else {
						flag = false; //no free servers
					}
				}
				if(!flag){

					boolean flag2 = true;

					if(flag2){
						for(int j = 0 ; j < servers.size(); j++){
							Server currentServer = servers.get(j);
							if(currentServer instanceof SelfCheckOut){
								currentServer.setArray(selfCheckOutQ);
							}
							//check to see if serverQ < qSize, if yes then customer waits
							if(currentServer.getWaitListSize() < this.qSize){
								Customer customer = c.makeWait(j+1);

								if(currentServer instanceof SelfCheckOut){
									customer.selfCheckOut(); //check if using self-checkout
									//currentServer.setArray(selfCheckOutQ);
								}
								
								Event event = new Event(customer);
								events.add(event);
								currentServer.addWait(customer);
								flag2 = true;
								break;
							} else {
								flag2 = false; //no servers and no Qs available
							}
						}

						if(!flag2){
							Customer customer = c.makeLeave();
							Event event = new Event(customer);
							events.add(event);
						}
					}
				}

			}
		} else if(c.getState() == 2){ //Customer done
			numServed++;
			Customer customer = c.makeDone(c.getServer());
			Server currentServer = this.servers.get(c.getServer() - 1);
			currentServer.updateTime(customer.getTime());
			
			if (currentServer instanceof SelfCheckOut){
				customer.selfCheckOut(); //check if self-checkout
				currentServer.setArray(selfCheckOutQ);
			} 

			Event event = new Event(customer);
			events.add(event);
		
		} else if (c.getState() == 3){ //Customer leaves
			numLeft++;
		
		} else if (c.getState() == 4){ //Customer done

			Server currentServer = servers.get(c.getServer() - 1);
			int serverID = c.getServer();

			if(!(currentServer instanceof SelfCheckOut)){
				if(Customer.generator.genRandomRest() < this.pRest){
					Server newServers = currentServer.makeRest();
					Event event = new Event(newServers);
					events.add(event);
			} 
				else if (currentServer.getWaitListSize() != 0){
					double t = c.getTime();
					Customer nextCustomer = currentServer.getCust();
					
					double waitingTime = t - nextCustomer.getTime();
					totalWaitingTime += waitingTime;
					
					Customer newCustomer = nextCustomer.changeTime(t);
					currentServer.updateTime(t);
					
					Customer finalCustomer = newCustomer.makeServed(serverID);
					Event event = new Event(finalCustomer);
					events.add(event);
					currentServer.removeCust();
				}
			} else if (currentServer.getWaitListSize() != 0){
					double t = c.getTime();
					Customer nextCustomer = currentServer.getCust();
					
					double waitingTime = t - nextCustomer.getTime();
					totalWaitingTime += waitingTime;
					
					Customer newCustomer = nextCustomer.changeTime(t);
					currentServer.updateTime(t);
					
					Customer finalCustomer = newCustomer.makeServed(serverID);
					finalCustomer.selfCheckOut();
					Event event = new Event(finalCustomer);
					events.add(event);
					currentServer.removeCust();
				}
			}
		}


   /**
    * To get the event of a GreedyCustomer between states
    * @param c The GreedyCustomer that is being served.
    */
	private void schedule(GreedyCustomer c){
		if(c.getState() == 1){
			boolean flag = true;

			if(flag){

				if(servers.size() == 0){ //no servers
					Customer customer = c.makeLeave();
					Event event = new Event(customer);
					events.add(event);
				}

				for(int i = 0; i < servers.size(); i ++){

					Server currentServer = servers.get(i);
					
					if(currentServer.isAvailable(c.getTime())){ //finds a free servers
						Customer customer = c.makeServed(i+1);

						if(currentServer instanceof SelfCheckOut){
							customer.selfCheckOut(); //check if using self check out
							currentServer.setArray(selfCheckOutQ);
							//Event event = new Event(customer);
							//events.add(event);
						}
						
						Event event = new Event(customer); //serve
						events.add(event);
						flag = true;
						break;
					} else {
						flag = false; //no free servers
					}
				}
				if(!flag){

					PriorityQueue<Server> fastQ = new PriorityQueue<>(new QueueComparator());

					for(int j = 0; j < servers.size(); j++){
						if(servers.get(j).getWaitListSize()< this.qSize){
							fastQ.add(servers.get(j));
						}
					}

					boolean flag2 = true;

					if(fastQ.size() == 0){
						flag2 = false;
					}

					if(flag2){

						Server fastest = fastQ.peek();

						Customer newCustomer = c.makeWait(fastest.getID());

						if(fastest instanceof SelfCheckOut){
							fastest.setArray(selfCheckOutQ);
							newCustomer.selfCheckOut();
						}

						Event event = new Event(newCustomer);
						events.add(event);
						fastest.addWait(newCustomer);
					} else {
						
						flag2 = false;
					}

					if(!flag2){
						Customer customer = c.makeLeave();
						Event event = new Event(customer);
						events.add(event);
						
					}
				}

			}
		} else if(c.getState() == 2){ //Customer done
			numServed++;
			Customer customer = c.makeDone(c.getServer());
			Server currentServer = this.servers.get(c.getServer() - 1);
			currentServer.updateTime(customer.getTime());
			
			if (currentServer instanceof SelfCheckOut){
				customer.selfCheckOut(); //check if self-checkout
				currentServer.setArray(selfCheckOutQ);
			} 

			Event event = new Event(customer);
			events.add(event);
		
		} else if (c.getState() == 3){ //Customer leaves
			numLeft++;
		
		} else if (c.getState() == 4){ //Customer done

			Server currentServer = servers.get(c.getServer() - 1);
			int serverID = c.getServer();

			if(!(currentServer instanceof SelfCheckOut)){
				if(Customer.generator.genRandomRest() < this.pRest){
					Server newServers = currentServer.makeRest();
					Event event = new Event(newServers);
					events.add(event);
			} 
				else if (currentServer.getWaitListSize() != 0){
					double t = c.getTime();
					Customer nextCustomer = currentServer.getCust();
					
					double waitingTime = t - nextCustomer.getTime();
					totalWaitingTime += waitingTime;
					
					Customer newCustomer = nextCustomer.changeTime(t);
					currentServer.updateTime(t);
					
					Customer finalCustomer = newCustomer.makeServed(serverID);
					Event event = new Event(finalCustomer);
					events.add(event);
					currentServer.removeCust();
				}
			} else if (currentServer.getWaitListSize() != 0){
					double t = c.getTime();
					Customer nextCustomer = currentServer.getCust();
					
					double waitingTime = t - nextCustomer.getTime();
					totalWaitingTime += waitingTime;
					
					Customer newCustomer = nextCustomer.changeTime(t);
					currentServer.updateTime(t);
					
					Customer finalCustomer = newCustomer.makeServed(serverID);
					finalCustomer.selfCheckOut();
					Event event = new Event(finalCustomer);
					events.add(event);
					currentServer.removeCust();
				}
			}
		}
		
	

   /**
    * To print out all the events in the events PQ
    * after each change in state for customers
    * and servers. 
    */

    public void print() {
        while(events.size() != 0) {
            if (events.peek().getType() == 1) {
                System.out.println(events.peek().getCust());
                Customer customer = events.poll().getCust();
                schedule(customer);
            } else if (events.peek().getType() == 2) {
            	//System.out.println(events.peek().getServer());
                Server server = events.poll().getServer();
                schedule(server);
            }
        }
    }	


   /**
    * To print out the simulation statistcs.
    * @param c The customer that is being served.
    */
    public void printStats() {
        double finalWaitingTime;
        
        if (numServed == 0) {
            finalWaitingTime = 0.000;
        } else {
            finalWaitingTime = totalWaitingTime / numServed;
        }

        System.out.println("[" + String.format("%.3f", finalWaitingTime) + " " + numServed + " " + numLeft + "]");
    }	
}


