package com.tes.core.domain;

import java.util.Set;

/**
 * Available channels down which messages can be sent
 */
public enum Channel {

    SMS("mobile"),

    EMAIL("email"),

    PUSH(),

    PIDGEON("latitude", "longitude");

    /**
     * Metadata properties required for channel to operate
     */
    private Set<String> requiredPropertySet;

    Channel(String... requiredPropertySet) {
        this.requiredPropertySet = Set.of(requiredPropertySet);
    }

    /**
     * Returns true if provided keys satisfy those required by the channel
     * @param keys - set of keys to be compared
     * @return true if satisfied
     */
    public boolean deliveryKeysOk(Set<String> keys) {
        return keys.containsAll(requiredPropertySet);
    }

}
