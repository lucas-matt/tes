package com.tes.messages.broker;

import java.util.function.Consumer;
import java.util.function.Predicate;

public interface SelectiveConsumer<T> extends Predicate<T>, Consumer<T> {



}
