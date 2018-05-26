package com.github.wibowo

/**
 * A command consist of one or more tokens provided by user.
 * E.g.
 * 5 2 is a command consisted of two tokens, 5 and 2.
 *
 * This status indicates the overall execution status for this command (i.e. both tokens)
 */
enum class CommandExecutionStatus {
    Success,
    Failed
}
