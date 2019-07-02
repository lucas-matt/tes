package com.tes.core.domain;

import java.util.Map;

public interface Template {

    String apply(Map<String, Object> metadata);

}
