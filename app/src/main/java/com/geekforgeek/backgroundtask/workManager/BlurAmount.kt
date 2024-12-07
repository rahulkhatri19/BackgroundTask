package com.geekforgeek.backgroundtask.workManager

import androidx.annotation.StringRes

data class BlurAmount(
    @StringRes
    val amountText:Int,
    val amountSequence:Int
)
