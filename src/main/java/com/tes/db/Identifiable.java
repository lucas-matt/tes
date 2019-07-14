package com.tes.db;

import java.util.UUID;

/**
 * Generic savable entity, to be used with the general repository
 */
public interface Identifiable {

    /**
     * @return the unique identifier of the entity
     */
    UUID getId();

    /**
     * Sets uuid of the entity
     * @param id
     */
    void setId(UUID id);

}
