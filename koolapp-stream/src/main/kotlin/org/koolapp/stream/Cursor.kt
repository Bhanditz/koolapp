package org.koolapp.stream

import java.io.Closeable

/**
 * Represents the processing of a [[Stream]] by a [[Handler]]
 * which can be closed via the [[Closeable]] interface
 */
public trait Cursor: Closeable {
    fun isClosed(): Boolean
}