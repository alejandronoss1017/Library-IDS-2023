package com.server.logic;

import com.server.model.Actor;

public class ActorInvoker {
    public static void invoke(Actor type) {
        new Thread(type::execute).start();
    }
}
