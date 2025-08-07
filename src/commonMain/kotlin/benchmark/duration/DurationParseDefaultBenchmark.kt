@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.*
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DurationParseDefaultBenchmark {
    @Param(
        // Successful cases
        "len3",
        "len5",
        "len8",
        "len13",
        "len21",
        "len34",
        "len55",
        "len89",

        // Overflow cases
        "double_overflow_small",
        "double_overflow_medium",
        "double_overflow_large",
        "double_overflow_extra_large",

        "leading_zeros_small",
        "leading_zeros_medium",
        "leading_zeros_large",
        "leading_zeros_extra_large"
    )
    lateinit var caseId: String

    private fun generateDoubleOverflowString(k: Int): String = "0.${"0123456789".repeat(k)}s"
    private fun generateLeadingZeros(k: Int): String = "${"0000000000".repeat(k)}1h"

    private val doubleOverflowSmall = generateDoubleOverflowString(2)
    private val doubleOverflowMedium = generateDoubleOverflowString(3)
    private val doubleOverflowLarge = generateDoubleOverflowString(8)
    private val doubleOverflowExtraLarge = generateDoubleOverflowString(21)

    private val leadingZerosSmall = generateLeadingZeros(2)
    private val leadingZerosMedium = generateLeadingZeros(3)
    private val leadingZerosLarge = generateLeadingZeros(8)
    private val leadingZerosExtraLarge = generateLeadingZeros(21)

    val input: String
        get() = when (caseId) {
            // Successful cases
            "len3" -> "10d"
            "len5" -> "1d12h"
            "len8" -> "-5d23h2m"
            "len13" -> "8d 31h 28m 6s"
            "len21" -> "15d 98m 451s 30.123ms"
            "len34" -> "100d 57h 12m 45s 28ms 3210.12345us"
            "len55" -> "8765d 151h 452m 1233s 9873ms 123451us 987653.12345678ns"
            "len89" -> "-(01257d  012395h 0087542m  000115874s 0871542ms  00951487us    000125845751.985487515ns)"

            // Overflow cases
            "double_overflow_small" -> doubleOverflowSmall
            "double_overflow_medium" -> doubleOverflowMedium
            "double_overflow_large" -> doubleOverflowLarge
            "double_overflow_extra_large" -> doubleOverflowExtraLarge

            "leading_zeros_small" -> leadingZerosSmall
            "leading_zeros_medium" -> leadingZerosMedium
            "leading_zeros_large" -> leadingZerosLarge
            "leading_zeros_extra_large" -> leadingZerosExtraLarge

            else -> error("Unhandled case-id: $caseId")
        }

    val durationWrapper = DurationWrapper(Duration.ZERO)

    @Benchmark
    fun parse(bh: Blackhole) {
        try {
            durationWrapper.duration = Duration.parse(input)
            bh.consume(durationWrapper)
        } catch (_: IllegalArgumentException) {
        }
    }
}