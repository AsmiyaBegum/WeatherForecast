package com.ab.weatherforecast.ui.mapscreen

import android.content.Context
import android.view.MotionEvent
import org.osmdroid.api.IGeoPoint
import org.osmdroid.views.MapView

class CustomMapView(context: Context?) : MapView(context) {
    private var onMapClickListener: OnMapClickListener? = null
    private val CLICK_THRESHOLD = 100 // Adjust as needed

    private var lastX = 0f
    private var lastY = 0f
    fun setOnMapClickListener(listener: OnMapClickListener?) {
        this.onMapClickListener = listener
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                lastX = event.x
                lastY = event.y
            }

            MotionEvent.ACTION_UP -> {
                val deltaX: Float = Math.abs(event.x - lastX)
                val deltaY: Float = Math.abs(event.y - lastY)
                if (deltaX < CLICK_THRESHOLD && deltaY < CLICK_THRESHOLD && onMapClickListener != null) {
                    // Convert screen coordinates to GeoPoint
                    val projection = projection
                    val geoPoint: IGeoPoint? = projection.fromPixels(event.x.toInt(), event.y.toInt())
                    // Notify the listener about the click event
                    onMapClickListener!!.onMapClick(geoPoint)
                    return true // Consume the event
                }
            }

            else -> {}
        }
        return super.onTouchEvent(event)
    }

    interface OnMapClickListener {
        fun onMapClick(geoPoint: IGeoPoint?)
    }
}
