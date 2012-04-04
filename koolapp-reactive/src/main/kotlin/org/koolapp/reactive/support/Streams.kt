package org.koolapp.reactive.support

import org.koolapp.reactive.*
import java.io.Closeable
import java.util.concurrent.Executor


/**
 * A [[Stream]] which invokes the given function on the handler
 */
class FunctionStream<T>(val fn: (Handler<T>) -> Unit): Stream<T>() {
    override fun subscribe(handler: Handler<T>): Closeable {
        (fn)(handler)
        return DefaultCloseable
    }
}

/**
 * Converts a collection into an [[Stream]]
 */
class StreamCollection<T>(val coll: java.lang.Iterable<T>, val executor: Executor) : Stream<T>() {
    public override fun subscribe(handler: Handler<T>): Closeable {
        val subscription = IteratorTask(coll.iterator(), handler)
        executor.execute(subscription)
        return subscription
    }

}

/**
 * Creates an [[Stream]] which transforms the handler using the given function
 */
class DelegateStream<T>(val delegate: Stream<T>, val fn: (Handler<T>) -> Handler<T>) : Stream<T>() {

    public override fun subscribe(handler: Handler<T>): Closeable {
        val result = (fn)(handler)
        return delegate.subscribe(result)
    }
}
/**
 * Creates an [[Stream]] which transforms the handler using the given function
 */
class MapStream<T,R>(val delegate: Stream<T>, val fn: (Handler<R>) -> Handler<T>) : Stream<R>() {

    public override fun subscribe(handler: Handler<R>): Closeable {
        val result = (fn)(handler)
        return delegate.subscribe(result)
    }
}