package com.server.logic;

import com.server.model.Actor;

public class InitActors {


    //  I was trying to implement the actors as a singleton, but I couldn't get it to work
    //  @Stivancho you can try to implement it as a singleton if you want, I think it's a good idea
    private final Actor actorApplicant = new ActorApplicant();
    private final Actor actorRenewal = new ActorRenewal();
    private final Actor actorReturn = new ActorReturn();

    public static void setUpActors() {

        // actorApplicant.execute();
        // actorRenewal.execute();
        // actorReturn.execute();
    }
}
