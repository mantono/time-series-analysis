package com.mantono.tsa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import java.time.Duration
import java.time.Instant

data class Data(override val timestamp: Instant, override val value: Int): DataPoint<Int>

class TrendsTest
{
    private val list1: List<DataPoint<Int>> = listOf(
        Data(Instant.now().minusSeconds(30), 15),
        Data(Instant.now().minusSeconds(20), 10),
        Data(Instant.now().minusSeconds(10), 8),
        Data(Instant.now().minusSeconds(0), 6)
    )

    @Test
    fun trendTestBySum()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(35))
        assertEquals(0.56, trend.bySum())
    }

    @Test
    fun trendTestBySumWithNotAllValues1()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(25))
        assertEquals(1.4, trend.bySum()) // (8 + 6) / 10
    }

    @Test
    fun trendTestBySumWithNotAllValues2()
    {
        val trend: Trend = trend(list1, Duration.ofSeconds(30), Instant.now().minusSeconds(2L))
        assertEquals(0.32, trend.bySum()) // 8 / (10 + 15)
    }
}