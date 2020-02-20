package edu.rosehulman.improse.myapplication.Bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import edu.rosehulman.improse.myapplication.MainActivity
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.util.*

const val NO_STATE = 0
const val LISTENING = 1
const val CONNECTING = 2
const val CONNECTED = 3

class BTMessageService(val handler: Handler, val activity: Activity) {

    lateinit var btAdapter:BluetoothAdapter
    var currentState:Int = 0
    var newState:Int = 0

    private var acceptThread: AcceptThread? = null
    private var connectThread: ConnectThread? = null
    private var connectedThread: ConnectedThread? = null

    val INSECURE_NAME = "IMPROSE"
    val INSECURE_UUID = UUID.fromString("8ce255c0-200a-11e0-ac64-0800200c9a66")

    init{
        btAdapter = BluetoothAdapter.getDefaultAdapter()
        currentState = NO_STATE
        newState = currentState
    }

    fun getState():Int{return this.currentState}

    @Synchronized fun start(){
        Log.d("Start", "Starting")

        if(connectThread != null){
            connectThread!!.cancel()
            connectThread = null
        }

        if (connectedThread != null){
            connectedThread!!.cancel()
            connectedThread = null;
        }

        if (acceptThread == null){
            acceptThread = AcceptThread()
            acceptThread!!.start()
        }

    }

    @Synchronized fun connect(device: BluetoothDevice){
        Log.d("Connecting", "Attempting to connect to " + device.name)

        if (currentState == CONNECTING){
            if (connectThread != null){
                connectThread!!.cancel()
                connectThread = null
            }
        }

        if (connectedThread != null){
            connectedThread!!.cancel()
            connectedThread = null
        }

        connectThread = ConnectThread(device)
        connectThread!!.start()

    }

    @Synchronized fun connected(blueSock: BluetoothSocket, device: BluetoothDevice){
        Log.d("Connected","connection successful")

        if(connectThread != null){
            connectThread!!.cancel()
            connectThread = null
        }

        if(connectedThread != null){
            connectedThread!!.cancel()
            connectedThread = null
        }

        if (acceptThread != null){
            acceptThread!!.cancel()
            acceptThread = null
        }

        connectedThread = ConnectedThread(blueSock)
        connectedThread!!.start()

        val msg = handler.obtainMessage(DEVICE_NAME)
        val bundle = Bundle()
        bundle.putString("DevName", device.name)
        msg.data = bundle
        handler.sendMessage(msg)
    }

    @Synchronized fun stop(){

        if(connectThread != null){
            connectThread!!.cancel()
            connectThread = null
        }

        if(connectedThread != null){
            connectedThread!!.cancel()
            connectedThread = null
        }

        if (acceptThread != null){
            acceptThread!!.cancel()
            acceptThread = null
        }

        currentState = NO_STATE

    }

    fun write(out: ByteArray){

        var r: ConnectedThread? = null

        synchronized(this){
            if(currentState != CONNECTED){
                return
            }
            r = connectedThread
        }
        r!!.write(out)
    }

    private fun connectionFailed(){
        Log.d("failed", "Connection failed")

        currentState = NO_STATE
        this.start()
    }

    private fun connectionLost(){
        Log.d("lost", "Connection lost")
        currentState = NO_STATE
        this.start()
    }

    fun ConnectedValue():Int{
        return CONNECTED
    }

    private inner class AcceptThread(): Thread() {

        private var mmServerSocket: BluetoothServerSocket? = null

        init {
            try {
                mmServerSocket =
                    btAdapter.listenUsingRfcommWithServiceRecord(INSECURE_NAME, INSECURE_UUID)
            } catch (e: IOException) {
                Log.d("ERROR", "listen() failed")
                mmServerSocket = null
            }
            currentState = LISTENING
        }

        override fun run() {
            Log.d("RUN", "Running on AcceptThread")
            setName("AcceptThread")

            var blueSocket: BluetoothSocket? = null

            while (currentState != CONNECTED) {
                try {
                    blueSocket = mmServerSocket!!.accept()
                } catch (e2: IOException) {
                    Log.e("Error", "accept error")
                    break;
                }

                if (blueSocket != null) {
                    synchronized(this) {
                        when (currentState) {
                            LISTENING, CONNECTING -> {
                                connected(blueSocket, blueSocket.remoteDevice)
                            }
                            NO_STATE, CONNECTED -> {
                                try {
                                    blueSocket.close()
                                } catch (e3: IOException) {
                                    Log.e("Error", "Could not close unwated socket")
                                }
                            }
                            else -> {
                            }
                        }
                    }
                }

            }

        }

        fun cancel() {
            try {
                mmServerSocket!!.close()
            }catch (e4: IOException){
                Log.e("Error", "accept close failed")
            }

        }
    }

    private inner class ConnectThread(val device: BluetoothDevice): Thread(){

        private var mmSocket: BluetoothSocket? = null
        init {
            try {
                mmSocket =
                    device.createInsecureRfcommSocketToServiceRecord(INSECURE_UUID)
            } catch (e: IOException) {
                Log.d("ERROR", "create() failed")
                mmSocket = null
            }
            currentState = CONNECTED

        }

        override fun run() {
            Log.d("RUN", "Running on ConnectThread")
            setName("ConnectThread")

            btAdapter.cancelDiscovery()

            try{
                mmSocket!!.connect()
            }catch (e: IOException){
                try{
                    mmSocket!!.close()
                }catch (e2: IOException){
                    Log.e("Close", "Could not close")
                }
                connectionFailed()
                return
            }

            synchronized(this){
                connectThread = null
            }

            connected(mmSocket!!, device)

        }

        fun cancel() {
            try {
                mmSocket!!.close()
            }catch (e4: IOException){
                Log.e("Error", "connect close failed")
            }

        }
    }

    public inner class ConnectedThread(val blueSock: BluetoothSocket):Thread(){

        var inStream:InputStream? = null
        var outStream: OutputStream? = null

        init{
            Log.d("Connected", "Creating ConnectedThread")

            try{
                inStream = blueSock.inputStream
                outStream = blueSock.outputStream
            }catch (e:IOException){
                Log.e("Error", "Socket did not give streams")
                inStream = null
                outStream = null
            }
            currentState = CONNECTED

        }

        override fun run(){
            Log.d("Connect", "Beginning Connection")
            activity.runOnUiThread(Runnable {
                Toast.makeText(activity, "Messages can be sent when both parties see this message", Toast.LENGTH_SHORT).show()
            })
            val buffer = ByteArray(1024)
            var bytes = 0;

            while (currentState == CONNECTED){
                try{
                    bytes = inStream!!.read(buffer)
                    handler.obtainMessage(READ, bytes, -1, buffer).sendToTarget()
                }catch (e2: IOException){
                    Log.e("Error", "Disconnected")
                    connectionLost()
                    break
                }
            }
        }

        fun write(buff: ByteArray){
            try{
                outStream!!.write(buff)
                handler.obtainMessage(WRITE, -1,-1, buff).sendToTarget()
            }catch (e3: IOException){
                Log.e("Error", "exception while writing")
            }
        }

        fun cancel(){
            try{
                blueSock.close()
            }catch (e4:IOException){
                Log.e("Error", "connect close failed")
            }
        }
    }
}
