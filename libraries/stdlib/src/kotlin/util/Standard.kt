package kotlin

/**
 * An exception is thrown to indicate that a method body that remains to be implemented.
 */
public class NotImplementedError(message: String = "An operation is not implemented.") : Error(message)

/**
 * Always throws an [NotImplementedError] stating that operation is not implemented.
 */
public fun TODO(): Nothing = throw NotImplementedError()

/**
 * Always throws an [NotImplementedError] stating that operation is not implemented.
 *
 * @param reason a string explaining why the implementation is missing.
 */
public fun TODO(reason: String): Nothing = throw NotImplementedError("An operation is not implemented: $reason")


/**
 * Creates a tuple of type [Pair] from this and [that].
 *
 * This can be useful for creating [Map] literals with less noise, for example:
 * @sample test.collections.MapTest.createUsingTo
 */
public fun <A, B> A.to(that: B): Pair<A, B> = Pair(this, that)

/**
 * Calls the specified function.
 */
public inline fun <T> run(f: () -> T): T = f()

/**
 * Execute f with given receiver
 */
public inline fun <T, R> with(receiver: T, f: T.() -> R): R = receiver.f()

/**
 * Converts receiver to body parameter
 */
public inline fun <T, R> T.let(f: (T) -> R): R = f(this)
