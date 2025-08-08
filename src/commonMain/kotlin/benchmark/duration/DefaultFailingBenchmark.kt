@file:Suppress("unused")

package benchmark.duration

import kotlinx.benchmark.Benchmark
import kotlinx.benchmark.Blackhole
import kotlinx.benchmark.Param
import kotlinx.benchmark.Scope
import kotlinx.benchmark.State
import kotlin.time.Duration

@State(Scope.Benchmark)
open class DefaultFailingBenchmark {
    @Param(
        "empty",
        "def_wrong_order",
        "def_bad_fraction",
        "def_overflow"
    )
    lateinit var caseId: String

    val input: String
        get() = when (caseId) {
            "empty" -> ""
            "def_wrong_order" -> "1d 54s 123m"
            "def_bad_fraction" -> "123.456.789d"
            "def_overflow" -> "12345678901234567890ns"

            else -> error("Unhandled case-id: $caseId")
        }

    @Benchmark
    fun parse(bh: Blackhole) {
        try {
            bh.consume(Duration.parse(input))
        } catch (_: IllegalArgumentException) {
        }
    }

    @Benchmark
    fun parseOrNull(bh: Blackhole) {
        bh.consume(Duration.parseOrNull(input))
    }
}