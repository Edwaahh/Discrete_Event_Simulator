import cs2030.simulator.Store;
import cs2030.simulator.Customer;
import cs2030.simulator.Event;
import cs2030.simulator.Server;
import cs2030.simulator.RandomGenerator;
import cs2030.simulator.GreedyCustomer;
import cs2030.simulator.CustomerGenerator;
import cs2030.simulator.*;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;

public class Main {

  /**
   * The main method for Discrete Event Simulator plus. Reads data from file and
   * then run a simulation based on the input data using Store Print() method.
   *
   * @param args 10 arguments, 
   *Input to the program comprises (in order of presentation):
   * an int value denoting the base seed for the RandomGenerator object;
   * an int value representing the number of servers
   * an int value representing the number of self-checkout counters, Nself
   * an int value for the maximum queue length, Qmax
   * an int representing the number of customers (or the number of arrival events) to simulate
   * a positive double parameter for the arrival rate, λ
   * a positive double parameter for the service rate, μ
   * a positive double parameter for the resting rate, ρ
   * a double parameter for the probability of resting, Pr
   * a double parameter for the probability of a greedy customer occurring, Pgfirst an integer specifying the base seed value of
   *
   */
    public static void main(String[] args) {

        //RandomGenerator generator;

        Scanner sc = new Scanner(System.in);

        int seedValue = sc.nextInt();

        //scan the number of servers
        int numServers = sc.nextInt();

        int selfie = sc.nextInt();

        int maxQ = sc.nextInt();

        int numCustomers = sc.nextInt();

        double arrRate = sc.nextDouble();

        double serviceRate = sc.nextDouble();

        double restRate = sc.nextDouble();

        double pRest = sc.nextDouble();

        double pGreed = sc.nextDouble();

        Customer.constructGenerator(seedValue, arrRate, serviceRate, restRate);

        //initialise a scheduler with the right amt of waiters
        Store store = new Store(numServers, selfie, restRate, pRest);

        //update max queue size
        store.updateQSize(maxQ);

        //time to be stacked on for each new customer after rand gen
        double newTime = 0;

        //storing the arrivals inside schedule first before parsing
        for (int i = 0; i < numCustomers; i++) {

            if (i == 0) {
                //Customer cust = (CustomerGenerator.generator.genCustomerType() < pGreed) ? new Customer(1, 0, 1,0) : new GreedyCustomer(1,0,1,0);

                //store.addEvent(cust);

                Customer cust = CustomerGenerator.customerGenerator(1, 0, 1, 0, pGreed);
                store.addEvent(cust);
            } else {
                //Customer cust = (CustomerGenerator.generator.genCustomerType() < pGreed) ? new Customer(i+1, 1, 1): new GreedyCustomer(i+1,1,0);
                //newTime = cust.getTime();
                //Customer newCust = cust.changeTime(newTime);

                //store.addEvent(cust);
                Customer cust = CustomerGenerator.customerGenerator(i+1, 1, 0, pGreed);
                newTime += cust.getTime();

                Customer newCust = cust.changeTime(newTime);
                store.addEvent(newCust);

            }
        }

        store.print();

        store.printStats();
    }
}
