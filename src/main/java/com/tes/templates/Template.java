package com.tes.templates;

import java.util.Map;

public interface Template {

    String apply(Map<String, Object> metadata);

}
