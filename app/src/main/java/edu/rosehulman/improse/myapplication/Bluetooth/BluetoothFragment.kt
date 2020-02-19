package edu.rosehulman.improse.myapplication.Bluetooth

import android.app.Activity.RESULT_CANCELED
import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothServerSocket
import android.bluetooth.BluetoothSocket
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import edu.rosehulman.improse.myapplication.R
import kotlinx.android.synthetic.main.bluetooth_layout.view.*
import java.io.IOException
import java.io.InputStream
import java.io.OutputStream
import java.lang.Exception
import java.util.*
import kotlin.collections.ArrayList


const val BT_REQUEST = 1
const val NAME = "Improse"
const val READ = 0
const val WRITE = 1
val APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

class BluetoothFragment : Fragment(){

    var bluetoothAdapter:BluetoothAdapter? = null
    var bt_enabled = false;
    var receivedMessage:TextView? = null;
    var sendText:EditText? = null;
    var submitButton:Button? = null;
    var deviceNum = -1;
    var pairedDevices:Set<BluetoothDevice>? = null

    private var connectingThread:ConnectingThread? = null
    private var acceptingThread:AcceptingThread? = null


     private val receiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val action: String = intent.action!!
            when(action) {
                BluetoothDevice.ACTION_FOUND -> {
                    val device: BluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
                    val deviceName = device.name
                    val deviceHardwareAddress = device.address // MAC address
                    bluetoothAdapter!!.getRemoteDevice(deviceHardwareAddress)
                }
            }
        }
    }

    val mesHandler:Handler = object: Handler(Looper.getMainLooper()){
        override fun handleMessage(msg: Message) {
            when(msg.what){
                READ -> {
                    val readbuf = msg.obj as ByteArray
                    val string_recieved = String(readbuf)
                    Log.d("message",string_recieved)
                    receivedMessage!!.text = string_recieved
                }
                WRITE ->{
                    if (msg.obj != null) {
                        val connectedThread =
                            ConnectedThread(msg.obj as BluetoothSocket)
                        val toSend = sendText!!.text.toString()
                        Log.d("toSend", toSend)
                        connectedThread.write(toSend.toByteArray())
                    }
                    else{
                        Log.d("mesHand", "mes.obj was null")
                    }
                }
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bluetoothView =  inflater.inflate(R.layout.bluetooth_layout, container,false)

        submitButton = bluetoothView.bluetooth_send
        sendText = bluetoothView.bluetooth_edittext
        receivedMessage = bluetoothView.message_box

        initializeBT()

        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val allNames = ArrayList<String>()
        pairedDevices!!.forEach(){
           allNames.add(it.name)
        }
        val fragBT =  BTDataFragment(this, allNames);
        childFragTrans.add(R.id.bt_data, fragBT);
        childFragTrans.commit();

        val acceptingThread = AcceptingThread()
        acceptingThread.start()

        return bluetoothView;
    }

    fun initializeBT(){
        bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

        if(bluetoothAdapter == null){
            submitButton!!.setOnClickListener{
                Toast.makeText(context, "No Bluetooth connection", Toast.LENGTH_LONG).show()
            }
        }

        if(bluetoothAdapter?.isEnabled == false){
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableBtIntent, BT_REQUEST)
        }
        else{
            bt_enabled = true
            val filter = IntentFilter(BluetoothDevice.ACTION_FOUND)
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
            filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
            activity!!.registerReceiver(receiver, filter)

            val discoverableIntent: Intent = Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE).apply {
                putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 15)
            }
            startActivity(discoverableIntent)

            pairedDevices = bluetoothAdapter?.bondedDevices
            pairedDevices?.forEach { device ->
                val deviceName = device.name
                val deviceHardwareAddress = device.address // MAC address
                Log.d("Devices", deviceName + deviceHardwareAddress)
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if(requestCode == BT_REQUEST){
            if (resultCode == RESULT_OK){
                bt_enabled=true
            }
            else if(resultCode == RESULT_CANCELED){
                bt_enabled = false
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        activity!!.unregisterReceiver(receiver)
    }

    private inner class AcceptingThread : Thread() {

        private val mmServerSocket: BluetoothServerSocket? by lazy(LazyThreadSafetyMode.NONE) {
            bluetoothAdapter?.listenUsingInsecureRfcommWithServiceRecord(
                NAME,
                APP_UUID
            )
        }

        override fun run() {
            var loop = true
            while (loop) {
                val socket: BluetoothSocket? = try {
                    mmServerSocket?.accept()
                } catch (e: IOException) {
                    Log.d("accept", "Socket's accept() method failed", e)
                    loop = false
                    null
                }
                socket?.also {
                    manageMyConnectedSocket(it)
                    mmServerSocket?.close()
                    loop = false
                }
            }
        }
        fun cancel() {
            try {
                mmServerSocket?.close()
            } catch (e: IOException) {
                Log.e("close listen", "Could not close the connect socket", e)
            }
        }
    }

    private fun manageMyConnectedSocket(blueSock: BluetoothSocket) {

        submitButton!!.setOnClickListener(){
            val writemsg = mesHandler.obtainMessage(WRITE,blueSock)
            writemsg.sendToTarget()
            Log.d("message", "buttonClicked")
        }
    }

    private inner class ConnectingThread(device: BluetoothDevice) : Thread() {

        private val mmSocket: BluetoothSocket? by lazy(LazyThreadSafetyMode.NONE) {
            device.createInsecureRfcommSocketToServiceRecord(APP_UUID)
        }

         override fun run() {
            bluetoothAdapter?.cancelDiscovery()

            mmSocket?.use { socket ->
                try{
                socket.connect()
                Log.d("Connecting", "called socket.connect()")
                manageMyConnectedSocket(socket)}
                catch (e: Exception){
                    if(mmSocket != null){
                        try{
                            mmSocket?.close()
                        }catch (e1: IOException){

                        }
                    }
                }
            }
        }

        // Closes the client socket and causes the thread to finish.
        fun cancel() {
            try {
                mmSocket?.close()
            } catch (e: IOException) {
                Log.e("Accepting", "Could not close the client socket", e)
            }
        }
    }

    private inner class ConnectedThread(private val mmSocket: BluetoothSocket) : Thread() {

        private val mmInStream: InputStream = mmSocket.inputStream
        private val mmOutStream: OutputStream = mmSocket.outputStream
        private val mmBuffer: ByteArray = ByteArray(1024) // mmBuffer store for the stream

        override fun run() {
            var numBytes: Int // bytes returned from read()

            // Keep listening to the InputStream until an exception occurs.
            while (true) {
                // Read from the InputStream.
                numBytes = try {
                    mmInStream.read(mmBuffer)
                } catch (e: IOException) {
                    Log.d("ConnectedThread", "Input stream was disconnected", e)
                    break
                }

                // Send the obtained bytes to the UI activity.
                val readMsg = mesHandler.obtainMessage(
                    READ, numBytes, -1,
                    mmBuffer)
                readMsg.sendToTarget()
            }
        }

        // Call this from the main activity to send data to the remote device.
        fun write(bytes: ByteArray) {
            try {
                mmOutStream.write(bytes)
            } catch (e: IOException) {
                Log.e("WRITE", "Error occurred when sending data", e)

            }

//            // Share the sent message with the UI activity.
//            val writtenMsg = mesHandler.obtainMessage(
//                WRITE, -1, -1, mmBuffer)
//            writtenMsg.sendToTarget()
        }

        // Call this method from the main activity to shut down the connection.
        fun cancel() {
            try {
                mmSocket.close()
            } catch (e: IOException) {
                Log.e("Error", "Could not close the connect socket", e)
            }
        }
    }


    fun setDevice(pos:Int){
        //connectingThread?.cancel()
        deviceNum= pos;
        val device = bluetoothAdapter!!.bondedDevices.toTypedArray()[pos]
        connectingThread = ConnectingThread(device)
        connectingThread!!.start()
    }
}