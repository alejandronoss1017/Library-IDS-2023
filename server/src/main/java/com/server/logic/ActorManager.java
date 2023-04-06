package com.server.logic;

public class ActorManager {

    //  I was trying to implement the actors as a singleton, but I couldn't get it to work
    //  @Stivancho you can try to implement it as a singleton if you want, I think it's a good idea

    // R = ready.
    private static final ActorApplicant actorApplicant = new ActorApplicant(); // creates an instance of ActorApplicant
    private static final ActorRenewal actorRenewal = new ActorRenewal(); // creates an instance of ActorRenewal
    private static final ActorReturn actorReturn = new ActorReturn(); // creates an instance of ActorReturn

    static Thread threadApplicant = null; // creates a Thread variable for ActorApplicant and initializes it to null
    static Thread threadRenewal = null; // creates a Thread variable for ActorRenewal and initializes it to null
    static Thread threadReturn = null; // creates a Thread variable for ActorReturn and initializes it to null

    public static void setUpActors() {
        threadApplicant = new Thread(actorApplicant); // creates a new Thread for ActorApplicant, passing in the ActorApplicant instance
        threadRenewal = new Thread(actorRenewal); // creates a new Thread for ActorRenewal, passing in the ActorRenewal instance
        threadReturn = new Thread(actorReturn); // creates a new Thread for ActorReturn, passing in the ActorReturn instance

        threadApplicant.start(); // starts the Thread for ActorApplicant
        threadRenewal.start(); // starts the Thread for ActorRenewal
        threadReturn.start(); // starts the Thread for ActorReturn

        System.out.println("[Actor Manager]: Actors started"); // prints a message indicating that the actors have started
    }

    public static void stopActors() {
        if(threadApplicant != null && threadRenewal != null && threadReturn != null) { // checks if all the Threads are not null
            threadApplicant.interrupt(); // interrupts the Thread for ActorApplicant
            threadRenewal.interrupt(); // interrupts the Thread for ActorRenewal
            threadReturn.interrupt(); // interrupts the Thread for ActorReturn

            System.out.println("[Actor Manager]: Actors stopped"); // prints a message indicating that the actors have stopped
            return;
        }
        System.out.println("[Actor Manager]: Actors already stopped"); // prints a message indicating that the actors have already stopped
    }

    public static Boolean isReady() {
        if (actorRenewal.isStatus()) { // checks if ActorRenewal is ready
            return actorReturn.isStatus(); // returns whether ActorReturn is ready or not
        }
        return false; // returns false if ActorRenewal is not ready
    }
}
