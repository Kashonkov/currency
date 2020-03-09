package com.exmpale.estensions

import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat

/**
 * @author Kashonkov Nikita
 */

private val decFormat = DecimalFormat().apply {
    setMaximumFractionDigits(2);
    setMinimumFractionDigits(2);
}

fun Double.toFormatedString(): String{
    val bd = BigDecimal(this)
    bd.setScale(2, RoundingMode.FLOOR)
    return  decFormat.format(bd)
}