package com.tes.db;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface Repository<T extends Identifiable> {

    List<T> find(int skip, int limit);

    Optional<T> findById(UUID id);

    T save(T template);

    void delete(UUID id);

    Integer count();

    boolean exists(UUID id);
}
