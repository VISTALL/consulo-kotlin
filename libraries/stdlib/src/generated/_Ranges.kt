package kotlin

//
// NOTE THIS FILE IS AUTO-GENERATED by the GenerateStandardLib.kt
// See: https://github.com/JetBrains/kotlin/tree/master/libraries/stdlib
//

import kotlin.platform.*
import java.util.*

import java.util.Collections // TODO: it's temporary while we have java.util.Collections in js

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun IntRange.contains(item: Byte): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun LongRange.contains(item: Byte): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ShortRange.contains(item: Byte): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun DoubleRange.contains(item: Byte): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun FloatRange.contains(item: Byte): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Byte item is not supported and should not be used.")
public fun CharRange.contains(item: Byte): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Int and Char item is not supported and should not be used.")
public fun IntRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Long and Char item is not supported and should not be used.")
public fun LongRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Byte and Char item is not supported and should not be used.")
public fun ByteRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Short and Char item is not supported and should not be used.")
public fun ShortRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Double and Char item is not supported and should not be used.")
public fun DoubleRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

deprecated("The 'contains' operation for range of Float and Char item is not supported and should not be used.")
public fun FloatRange.contains(item: Char): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun IntRange.contains(item: Double): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun LongRange.contains(item: Double): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ByteRange.contains(item: Double): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ShortRange.contains(item: Double): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun FloatRange.contains(item: Double): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Double item is not supported and should not be used.")
public fun CharRange.contains(item: Double): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun IntRange.contains(item: Float): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun LongRange.contains(item: Float): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ByteRange.contains(item: Float): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ShortRange.contains(item: Float): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun DoubleRange.contains(item: Float): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Float item is not supported and should not be used.")
public fun CharRange.contains(item: Float): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun LongRange.contains(item: Int): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ByteRange.contains(item: Int): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ShortRange.contains(item: Int): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun DoubleRange.contains(item: Int): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun FloatRange.contains(item: Int): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Int item is not supported and should not be used.")
public fun CharRange.contains(item: Int): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun IntRange.contains(item: Long): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ByteRange.contains(item: Long): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ShortRange.contains(item: Long): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun DoubleRange.contains(item: Long): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun FloatRange.contains(item: Long): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Long item is not supported and should not be used.")
public fun CharRange.contains(item: Long): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun IntRange.contains(item: Short): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun LongRange.contains(item: Short): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun ByteRange.contains(item: Short): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun DoubleRange.contains(item: Short): Boolean {
    return start <= item && item <= end
}

/**
 * Checks if the specified [item] belongs to this range.
 */
public fun FloatRange.contains(item: Short): Boolean {
    return start <= item && item <= end
}

deprecated("The 'contains' operation for range of Char and Short item is not supported and should not be used.")
public fun CharRange.contains(item: Short): Nothing {
    throw UnsupportedOperationException()
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Byte): IntProgression {
    return IntProgression(this, to.toInt(), -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Byte): LongProgression {
    return LongProgression(this, to.toLong(), -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Byte): ByteProgression {
    return ByteProgression(this, to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Byte): ShortProgression {
    return ShortProgression(this, to.toShort(), -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Byte): DoubleProgression {
    return DoubleProgression(this, to.toDouble(), -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Byte): FloatProgression {
    return FloatProgression(this, to.toFloat(), -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Char.downTo(to: Char): CharProgression {
    return CharProgression(this, to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this.toDouble(), to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this.toDouble(), to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this.toDouble(), to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this.toDouble(), to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this, to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Double): DoubleProgression {
    return DoubleProgression(this.toDouble(), to, -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Float): FloatProgression {
    return FloatProgression(this.toFloat(), to, -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Float): FloatProgression {
    return FloatProgression(this.toFloat(), to, -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Float): FloatProgression {
    return FloatProgression(this.toFloat(), to, -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Float): FloatProgression {
    return FloatProgression(this.toFloat(), to, -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Float): DoubleProgression {
    return DoubleProgression(this, to.toDouble(), -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Float): FloatProgression {
    return FloatProgression(this, to, -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Int): IntProgression {
    return IntProgression(this, to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Int): LongProgression {
    return LongProgression(this, to.toLong(), -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Int): IntProgression {
    return IntProgression(this.toInt(), to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Int): IntProgression {
    return IntProgression(this.toInt(), to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Int): DoubleProgression {
    return DoubleProgression(this, to.toDouble(), -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Int): FloatProgression {
    return FloatProgression(this, to.toFloat(), -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Long): LongProgression {
    return LongProgression(this.toLong(), to, -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Long): LongProgression {
    return LongProgression(this, to, -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Long): LongProgression {
    return LongProgression(this.toLong(), to, -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Long): LongProgression {
    return LongProgression(this.toLong(), to, -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Long): DoubleProgression {
    return DoubleProgression(this, to.toDouble(), -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Long): FloatProgression {
    return FloatProgression(this, to.toFloat(), -1.0F)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Int.downTo(to: Short): IntProgression {
    return IntProgression(this, to.toInt(), -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Long.downTo(to: Short): LongProgression {
    return LongProgression(this, to.toLong(), -1L)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Byte.downTo(to: Short): ShortProgression {
    return ShortProgression(this.toShort(), to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Short.downTo(to: Short): ShortProgression {
    return ShortProgression(this, to, -1)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Double.downTo(to: Short): DoubleProgression {
    return DoubleProgression(this, to.toDouble(), -1.0)
}

/**
 * Returns a progression from this value down to the specified [to] value with the increment -1.
 * The [to] value has to be less than this value.
 */
public fun Float.downTo(to: Short): FloatProgression {
    return FloatProgression(this, to.toFloat(), -1.0F)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun ByteProgression.reversed(): ByteProgression {
    return ByteProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun CharProgression.reversed(): CharProgression {
    return CharProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun DoubleProgression.reversed(): DoubleProgression {
    return DoubleProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun FloatProgression.reversed(): FloatProgression {
    return FloatProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun IntProgression.reversed(): IntProgression {
    return IntProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun LongProgression.reversed(): LongProgression {
    return LongProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over the same range in the opposite direction with the same step.
 */
public fun ShortProgression.reversed(): ShortProgression {
    return ShortProgression(end, start, -increment)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun ByteRange.reversed(): ByteProgression {
    return ByteProgression(end, start, -1)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun CharRange.reversed(): CharProgression {
    return CharProgression(end, start, -1)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun DoubleRange.reversed(): DoubleProgression {
    return DoubleProgression(end, start, -1.0)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun FloatRange.reversed(): FloatProgression {
    return FloatProgression(end, start, -1.0f)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun IntRange.reversed(): IntProgression {
    return IntProgression(end, start, -1)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun LongRange.reversed(): LongProgression {
    return LongProgression(end, start, -1L)
}

/**
 * Returns a progression that goes over this range in reverse direction.
 */
public fun ShortRange.reversed(): ShortProgression {
    return ShortProgression(end, start, -1)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun ByteProgression.step(step: Int): ByteProgression {
    checkStepIsPositive(step > 0, step)
    return ByteProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun CharProgression.step(step: Int): CharProgression {
    checkStepIsPositive(step > 0, step)
    return CharProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun DoubleProgression.step(step: Double): DoubleProgression {
    checkStepIsPositive(step > 0, step)
    return DoubleProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun FloatProgression.step(step: Float): FloatProgression {
    checkStepIsPositive(step > 0, step)
    return FloatProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun IntProgression.step(step: Int): IntProgression {
    checkStepIsPositive(step > 0, step)
    return IntProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun LongProgression.step(step: Long): LongProgression {
    checkStepIsPositive(step > 0, step)
    return LongProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over the same range with the given step.
 */
public fun ShortProgression.step(step: Int): ShortProgression {
    checkStepIsPositive(step > 0, step)
    return ShortProgression(start, end, if (increment > 0) step else -step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun ByteRange.step(step: Int): ByteProgression {
    checkStepIsPositive(step > 0, step)
    return ByteProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun CharRange.step(step: Int): CharProgression {
    checkStepIsPositive(step > 0, step)
    return CharProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun DoubleRange.step(step: Double): DoubleProgression {
    if (step.isNaN()) throw IllegalArgumentException("Step must not be NaN.")
    checkStepIsPositive(step > 0, step)
    return DoubleProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun FloatRange.step(step: Float): FloatProgression {
    if (step.isNaN()) throw IllegalArgumentException("Step must not be NaN.")
    checkStepIsPositive(step > 0, step)
    return FloatProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun IntRange.step(step: Int): IntProgression {
    checkStepIsPositive(step > 0, step)
    return IntProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun LongRange.step(step: Long): LongProgression {
    checkStepIsPositive(step > 0, step)
    return LongProgression(start, end, step)
}

/**
 * Returns a progression that goes over this range with given step.
 */
public fun ShortRange.step(step: Int): ShortProgression {
    checkStepIsPositive(step > 0, step)
    return ShortProgression(start, end, step)
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Int.until(to: Byte): IntRange {
    return this .. (to.toInt() - 1).toInt()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Long.until(to: Byte): LongRange {
    return this .. (to.toLong() - 1).toLong()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Byte.MIN_VALUE].
 */
public fun Byte.until(to: Byte): ByteRange {
    val to_  = (to - 1).toByte()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Short.until(to: Byte): ShortRange {
    return this .. (to.toShort() - 1).toShort()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Char.MIN_VALUE].
 */
public fun Char.until(to: Char): CharRange {
    val to_  = (to.toInt() - 1).toChar()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Int.MIN_VALUE].
 */
public fun Int.until(to: Int): IntRange {
    val to_  = (to.toLong() - 1).toInt()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Long.until(to: Int): LongRange {
    return this .. (to.toLong() - 1).toLong()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Int.MIN_VALUE].
 */
public fun Byte.until(to: Int): IntRange {
    val to_  = (to.toLong() - 1).toInt()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toInt() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Int.MIN_VALUE].
 */
public fun Short.until(to: Int): IntRange {
    val to_  = (to.toLong() - 1).toInt()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toInt() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Long.MIN_VALUE].
 */
public fun Int.until(to: Long): LongRange {
    val to_  = (to - 1).toLong()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toLong() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Long.MIN_VALUE].
 */
public fun Long.until(to: Long): LongRange {
    val to_  = (to - 1).toLong()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Long.MIN_VALUE].
 */
public fun Byte.until(to: Long): LongRange {
    val to_  = (to - 1).toLong()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toLong() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Long.MIN_VALUE].
 */
public fun Short.until(to: Long): LongRange {
    val to_  = (to - 1).toLong()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toLong() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Int.until(to: Short): IntRange {
    return this .. (to.toInt() - 1).toInt()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 */
public fun Long.until(to: Short): LongRange {
    return this .. (to.toLong() - 1).toLong()
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Short.MIN_VALUE].
 */
public fun Byte.until(to: Short): ShortRange {
    val to_  = (to - 1).toShort()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this.toShort() .. to_
}

/**
 * Returns a range from this value up to but excluding the specified [to] value.
 * The [to] value must be greater than [Short.MIN_VALUE].
 */
public fun Short.until(to: Short): ShortRange {
    val to_  = (to - 1).toShort()
    if (to_ > to) throw IllegalArgumentException("The to argument value '$to' was too small.")
    return this .. to_
}

