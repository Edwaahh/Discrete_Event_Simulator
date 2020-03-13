package cs2030.simulator;
import cs2030.simulator.RandomGenerator;

public abstract class CustomerGenerator{

    public static Customer customerGenerator(int ID, double time, int state, int servedBy, double pGreedy){
    	if(Customer.generator.genCustomerType() < pGreedy){
    		GreedyCustomer c = new GreedyCustomer(ID, time, state, servedBy);
    		return c;
    	} else {
    		return new Customer(ID, time, state, servedBy);
    	}
    }

    //random time
    public static Customer customerGenerator(int ID, int state, int servedBy, double pGreedy){
    	if(Customer.generator.genCustomerType() < pGreedy){
    		GreedyCustomer c = new GreedyCustomer(ID, state, servedBy);
    		return c;
    	} else {
    		return new Customer(ID, state, servedBy);
    	}
    }


}