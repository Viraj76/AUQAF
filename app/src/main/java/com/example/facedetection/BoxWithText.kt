package com.example.facedetection

import android.graphics.Rect
import android.graphics.RectF

data class BoxWithText(
    var text: String? = null,
    var rect: Rect? = null
) {


//    fun BoxWithText(text: String?, rect: Rect?) {
//        this.text = text
//        this.rect = rect
//    }
//
//    fun BoxWithText(displayName: String?, boundingBox: RectF) {
//        text = displayName
//        rect = Rect()
//        boundingBox.round(rect!!)
//    }
}