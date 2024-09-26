package com.nexgencoders.mylibrary.shape

import com.nexgencoders.mylibrary.shape.ArrowPointerLocation

/**
 * The different kind of known Shapes.
 */
sealed interface ShapeType {

    object Brush : ShapeType
    object Oval : ShapeType
    object Rectangle : ShapeType
    object Line : ShapeType
    class Arrow(val pointerLocation: ArrowPointerLocation = ArrowPointerLocation.START) : ShapeType

}