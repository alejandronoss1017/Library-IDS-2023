package com.server.logic;

import com.server.model.Actor;

public interface InitActors {

    public static void setUpActors() {
        Actor actorApplicant = new ActorApplicant();
        Actor actorRenewal = new ActorRenewal();
        Actor actorReturn = new ActorReturn();

        ActorInvoker.invoke(actorApplicant);
        ActorInvoker.invoke(actorRenewal);
        ActorInvoker.invoke(actorReturn);
    }
}
