package com.nexgencoders.mylibrary.shape

import android.graphics.Paint
import com.nexgencoders.mylibrary.shape.AbstractShape

/**
 * Simple data class to be put in an ordered Stack
 */
open class ShapeAndPaint(
    val shape: AbstractShape,
    val paint: Paint
)