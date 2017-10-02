package com.mantono.tsa

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class CollectionOperationsTest
{
    @Test
    fun testSum()
    {
        assertEquals(15.0, listOf(1, 2, 3, 4, 5).sum())
    }

    @Test
    fun testSumWithNegativeValues()
    {
        assertEquals(5.0, listOf(1, 2, 3, 4, -5).sum())
    }

    @Test
    fun testSumWithEmptyInput()
    {
        assertEquals(0.0, emptyList<Double>().sum())
    }

    @Test
    fun testAverage()
    {
        assertEquals(3.0, listOf(1, 2, 3, 4, 5).average())
    }

    @Test
    fun testAverageWithEmptyInput()
    {
        assertEquals(Double.NaN, emptyList<Double>().average())
    }

    @Test
    fun testMedianWithOddAmountOfValues()
    {
        assertEquals(3.0, listOf(1, 2, 3, 4, 5).median())
    }

    @Test
    fun testMedianWithEvenAmountOfValues()
    {
        assertEquals(2.5, listOf(1, 2, 3, 4).median())
    }

    @Test
    fun testMedianUnsorted()
    {
        assertEquals(2.5, listOf(1, 6, 0, 2, 4, 3).median())
    }

    @Test
    fun testMedianWithOneValue()
    {
        assertEquals(5.0, listOf(5).median())
    }

    @Test
    fun testMedianWithEmptyInput()
    {
        assertEquals(Double.NaN, emptyList<Double>().median())
    }
}