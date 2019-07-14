package com.tes.core.domain;

import java.util.Map;

/**
 * A specific instance of a template.
 */
public interface Template {

    /**
     * Apply the provided metadata to the template. Returns a string after having applied the metadata to the template
     * @param metadata - set of keys and values to apply to the template
     * @return processed result of the template
     */
    String apply(Map<String, Object> metadata);

}
