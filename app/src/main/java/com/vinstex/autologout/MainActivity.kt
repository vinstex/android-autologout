package com.vinstex.autologout

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.widget.Toast
import androidx.core.view.GestureDetectorCompat

private const val DEBUG_TAG = "Gestures"

class MainActivity :
    AppCompatActivity(),
    GestureDetector.OnGestureListener {

    private lateinit var ourDetector: GestureDetectorCompat
    private var timeset: Long = 10000
    private var noOfClicks: Int = 0
    private var isActive: Boolean = false


    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Instantiating the gesture detector
        /*We pass in the application context and an implementation of
         GestureDetector.OnGestureListener as the arguments*/
        ourDetector = GestureDetectorCompat(this, this)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        return if (ourDetector.onTouchEvent(event)) {
            true
        } else {
            super.onTouchEvent(event)
        }
    }

    override fun onDown(event: MotionEvent): Boolean {
        startDetection()
        return true
    }

    override fun onFling(
        event1: MotionEvent,
        event2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean {
        startDetection()
        return true
    }

    override fun onLongPress(event: MotionEvent) {
        startDetection()
    }

    override fun onScroll(
        event1: MotionEvent,
        event2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        startDetection()
        return true
    }

    override fun onShowPress(event: MotionEvent) {
        startDetection()
    }

    override fun onSingleTapUp(event: MotionEvent): Boolean {
        startDetection()
        return true
    }

    //a method to start detection
    fun startDetection() {
        //set the user's active status to true then increase the number of clicks by 1
        isActive = true
        noOfClicks++

        //dont listen more than once
        //simply do nothing :)
        if (noOfClicks > 1) {

        } else if (noOfClicks == 1) {
            startListener()
        }
    }

    fun startListener() {
        //check the user's activeness after a specified time in milliseconds
        Handler(Looper.getMainLooper()).postDelayed({
            //displayDialog()
            checkActiveness()
        }, timeset)
    }

    fun checkActiveness() {
        /*set the active status to false deliberately
        and wait for 5 seconds to see if it will change to true
        */
        isActive = false
        Handler(Looper.getMainLooper()).postDelayed({

            displayDialog()
        }, 5000)
    }

    fun displayDialog() {
        //if the user is still inactive, show the dialog
        /*reset the number of clicks to zero to start
        the detection  incase the dialog is dismissed*/
        if (isActive == false) {
            noOfClicks = 0
            val displayDialog = AlertDialog.Builder(this)
            displayDialog.apply {
                setTitle("Autologout")
                setMessage("It seems you have you have been away for a while. Would you like us to sign you out?")
                setPositiveButton("Yes, Sign out") { _, _ ->

                }
                setNegativeButton("Keep me in") { _, _ ->

                }
            }.create().show()
        } else {
            /*if active, set the clicks to zero also
            to allow the detection to start as the user clicks/taps the screen
             */
            noOfClicks = 0
        }
    }
}