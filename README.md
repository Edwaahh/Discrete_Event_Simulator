# Discrete_Event_Simulator


This program attempts to create a simulator of a store using Object Oriented Programming(OOP), it models the complex behavior of shops, servers (staff in the shop), and customers (normal and greedy). 

Specically,
  1) FIFO (first-in-first-out) queues for customers with a given maximum capacity
        SERVER_REST that simulates a server taking a rest, and
        SERVER_BACK that simulates a server returning back from rest
  2) Two types of servers,
        human servers who may rest after serving a customer, and
        self-checkout counters that never rest
  3) Two types of customers
        typical customers that joins the first queue (scanning from server 1 onwards) that is still not full, and
        greedy customers that joins the queue with the fewest waiting customers
        
Customer Queues
        
Each human server has a queue of customers to allow multiple customers to queue up. A customer that chooses to join a queue joins at the tail. When a server is done serving a customer, it serves the next waiting customer at the head of the queue. Hence, the queue should be a first-in-first-out (FIFO) structure. The self-checkout counters, however, have only a single shared queue.

Taking a Rest

The human servers are allowed to take occasional breaks. When a server finishes serving a customer, there is a probability Pr that the server takes a rest for a random amount of time T. During the break, the server does not serve the next waiting customer. Upon returning from the break, the server serves the next customer in the queue immediately.

Self-Checkout

To reduce waiting time, self-checkout counters have been set-up. These self-checkout counters are automated and never need to rest. Customers queue up for the self-checkout counter in the same way as human servers. There is one shared queue for all self-checkout counter.

Customers' Choice of Queue

As before, when a customer arrives, he or she first scans through the servers (in order, from 1 to k) to see if there is an idle server (i.e. not serving a customer and not resting). If there is one, the customer will go to the server to be served. Otherwise, a typical customer just chooses the first queue (while scanning from servers 1 to k) that is still not full to join. However, other than the typical customer, a greedy customer is introduced that always chooses the queue with the fewest customers to join. In the case of a tie, the customer breaks the tie by choosing the first one while scanning from servers 1 to k.

If a customer cannot find any queue to join, it will leave the shop.

Randomized Arrival and Service Time

Simulation of different arrival and service times is achieved via random number generation. A random number generator is an entity that generates one random number after another. Since it is not possible to generate a truly random number algorithmically, pseudo random number generation is adopted instead. A pseudo-random number generator can be initialized with a seed, such that the same seed always produces the same sequence of (seemingly random) numbers.

Although, Java provides a class java.util.Random, an alternative RandomGenerator class that is more suitable for discrete event simulation is provided for you that encapsulates different random number generators for use in our simulator. Each random number generator generates a different stream of random numbers. The constructor for RandomGenerator takes in three parameters:

    -int seed is the base seed. Each random number generator uses its own seed that is derived from this base seed;
    -double lambda is the arrival rate, λ;
    -double mu is the service rate, μ.

The inter-arrival time is usually modeled as an exponential random variable, characterized by a single parameter λ denoting the arrival rate. The genInterArrivalTime() method of the class RandomGenerator is used for this purpose. Specifically,

    -start the simulation by generating the first customer arrival event with timestamp 0
    -if there are still more customers to simulate, generate the next arrival event with a timestamp of T + now, where T is generated with the method genInterArrivalTime();

The service time is modeled as an exponential random variable, characterized by a single parameter, service rate μ. The method genServiceTime() from the class RandomGenerator can be used to generate the service time. Specifically,

    -each time a customer is being served, a DONE event is generated and scheduled;
    -the DONE event generated will have a timestamp of T + now, where T is generated with the method genServiceTime().

The program will also keep track of the following statistics:

the average waiting time for customers who have been served
the number of customers served
the number of customers who left without being served

To run this program, 
  1) Ensure that the folder "cs2030", which contains "simulator", is in the same directory as your working directory. Within "simulator" should contain the various java classes of the java files
  2) run "Main.java" file.
  3) The input to the program comprises (in order of presentation):

      1. an int value denoting the base seed for the RandomGenerator object;
      2. an int value representing the number of servers in the shop
      3. an int value representing the number of self-checkout counters, Nself
      4. an int value for the maximum queue length for each server, Qmax
      5. an int representing the number of customers to simulate
      6. a positive double parameter for the arrival rate, λ
      7. a positive double parameter for the service rate, μ
      8. a positive double parameter for the resting rate, ρ
      9. a double parameter for the probability of resting, Pr
