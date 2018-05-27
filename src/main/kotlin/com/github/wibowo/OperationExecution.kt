package com.github.wibowo

import com.google.common.collect.Lists

/**
 * A snapshot of operation execution. Having this snapshot allows us to rollback with exact
 * parameter used to perform the execution. E.g. for [Operation.Undo] or when we want to rollback
 * due to arithmetic exception.
 *
 * @param operation: Operation that was attempted
 * @param arguments: arguments required by the operation
 */
class OperationExecution(
        val operation: Operation,
        val arguments: List<RealNumber>) {

    /**
     * Result of the operation execution
     */
    val result: RealNumber

    init {
        this.result = operation.evaluate(this.arguments)
    }

    constructor(operation: Operation,
                arguments: RealNumber) : this(operation, Lists.newArrayList<RealNumber>(arguments)) {
    }

    override fun toString(): String {
        return result.toString()
    }
}
