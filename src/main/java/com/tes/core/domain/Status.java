package com.tes.core.domain;

/**
 * A message lifecycle is naturally asynchronous. These are the states that describe that workflow.
 */
public enum Status {

    /**
     * A message send is being processed
     */
    PENDING,

    /**
     * Message was successfully sent
     */
    SENT,

    /**
     * Message failed to be sent
     */
    FAILED

}
