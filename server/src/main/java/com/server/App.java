package com.server;

import com.server.logic.ActorManager;
import com.server.logic.PublisherTest;
import com.server.model.LoadManager;

public class App {
    public static void main(String[] args) throws Exception {

        System.out.println("Initializing server...");

        LoadManager loadManager = new LoadManager();

        System.out.println("Server running on port 5555");

        Thread loadManagerThread = new Thread(loadManager);

        ActorManager.setUpActors();
        PublisherTest publisher = new PublisherTest();
        Thread publisherThread = new Thread(publisher);
        publisherThread.start();

        Thread.sleep(1000);

        publisherThread.interrupt();
        ActorManager.stopActors();

        /*
        // Applicant is not implemented yet
        // final Actor actorApplicant = new ActorApplicant();
        final ActorRenewal actorRenewal = new ActorRenewal();
        final ActorReturn actorReturn = new ActorReturn();

        // Thread actorApplicantThread = new Thread(actorApplicant);
        Thread actorRenewalThread = new Thread(actorRenewal);
        Thread actorReturnThread = new Thread(actorReturn);

        // Threads are started
        loadManagerThread.start();
        actorRenewalThread.start();
        actorReturnThread.start();
         */
    }
}
