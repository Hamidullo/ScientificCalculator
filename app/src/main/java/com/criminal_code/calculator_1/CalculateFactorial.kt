package com.criminal_code.calculator_1

/**
 * Created by Anubhav on 14-03-2016.
 */
class CalculateFactorial internal constructor() {
    private var resSize = 1
    private val res = IntArray(MAX)

    fun getPercentage(n: Double,m: Double):Double{
        return ((n * m)/100).toDouble()
    }

    fun getfactorial(n: Int):Long{
        var factorial: Long = 1
        for (i in 1..n) {
            // factorial = factorial * i;
            factorial *= i.toLong()
        }
        return factorial
    }

    companion object {
        const val MAX = 1000
    }
}