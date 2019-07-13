package com.tes.health;

import akka.actor.typed.ActorSystem;
import com.codahale.metrics.health.HealthCheck;

/**
 * Stub while we have no real external dependencies or internal systems
 * of which to check the health
 */
public class AkkaHealthCheck extends HealthCheck {

    private ActorSystem system;

    public AkkaHealthCheck(ActorSystem system) {
        this.system = system;
    }

    @Override
    protected Result check() {
        return Result.healthy(String.format("Uptime %s seconds", system.uptime()));
    }

}
