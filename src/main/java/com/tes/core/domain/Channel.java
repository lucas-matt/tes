package com.tes.core.domain;

import java.util.Set;

public enum Channel {

    SMS("mobile"),

    EMAIL("email"),

    PUSH(),

    PIDGEON("latitude", "longitude");

    private Set<String> requiredPropertySet;

    Channel(String... requiredPropertySet) {
        this.requiredPropertySet = Set.of(requiredPropertySet);
    }

    public boolean deliveryKeysOk(Set<String> keys) {
        return keys.containsAll(requiredPropertySet);
    }

}
