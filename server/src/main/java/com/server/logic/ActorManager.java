package com.server.logic;

public class ActorManager {

    //  I was trying to implement the actors as a singleton, but I couldn't get it to work
    //  @Stivancho you can try to implement it as a singleton if you want, I think it's a good idea

    // R = ready.
    private static final ActorApplicant actorApplicant = new ActorApplicant();
    private static final ActorRenewal actorRenewal = new ActorRenewal();
    private static final ActorReturn actorReturn = new ActorReturn();

    static Thread threadApplicant = null;
    static Thread threadRenewal = null;
    static Thread threadReturn = null;

    public static void setUpActors() {
        threadApplicant = new Thread(actorApplicant);
        threadRenewal = new Thread(actorRenewal);
        threadReturn = new Thread(actorReturn);

        threadApplicant.start();
        threadRenewal.start();
        threadReturn.start();

        System.out.println("[Actor Manager]: Actors started");
    }

    public static void stopActors() {
        if(threadApplicant != null && threadRenewal != null && threadReturn != null) {
            threadApplicant.interrupt();
            threadRenewal.interrupt();
            threadReturn.interrupt();

            System.out.println("[Actor Manager]: Actors stopped");
            return;
        }
        System.out.println("[Actor Manager]: Actors already stopped");
    }

    public static Boolean isReady() {
        if (actorRenewal.isStatus()) {
            return actorReturn.isStatus();
        }
        return false;
    }
}
