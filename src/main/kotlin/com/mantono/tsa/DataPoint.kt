package com.mantono.tsa

import java.time.Instant

interface DataPoint<T> where T: Number
{
    val timestamp: Instant
    val value: T
}