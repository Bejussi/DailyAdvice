package com.bejussi.dailyadvice.core.util

interface Mapper<F, T> {
    suspend fun map(from: F): T
}