package edu.rosehulman.improse.myapplication.Bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.os.Handler
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
import java.util.*


const val NAME = "Improse"
const val READ = 0
const val WRITE = 1
const val DEVICE_NAME = 2

const val REQUEST_CONNECT= 3
const val REQUEST_ENABLE = 4

val APP_UUID = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")

class BluetoothFragment : Fragment() {

    private var btAdapter:BluetoothAdapter? = null
    private var btAvailible = true
    private var btms:BTMessageService? = null
    lateinit var outStringBuff:StringBuffer
    var connectedDeviceName = ""

    var receivedMessage: TextView? = null
    var sendText: EditText? = null
    var submitButton: Button? = null

    var deviceAddress = "";

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        btAdapter = BluetoothAdapter.getDefaultAdapter()

        if(btAdapter == null){
            btAvailible = false;
            Toast.makeText(activity, "Bluetooth unavailible", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val bluetoothView =  inflater.inflate(R.layout.bluetooth_layout, container,false)

        submitButton = bluetoothView.bluetooth_send
        sendText = bluetoothView.bluetooth_edittext
        receivedMessage = bluetoothView.message_box


        val childFragMan = getChildFragmentManager();
        val childFragTrans = childFragMan.beginTransaction();
        val allNames = ArrayList<String>()
        btAdapter!!.bondedDevices!!.forEach(){
            allNames.add(it.name)
        }
        val fragBT =  BTDataFragment(this, allNames);
        childFragTrans.add(R.id.bt_data, fragBT);
        childFragTrans.commit();

        return bluetoothView;
    }


    override fun onStart() {
        super.onStart()

        if (!btAdapter!!.isEnabled()) {
            val enableIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            startActivityForResult(enableIntent, REQUEST_ENABLE)

        } else if (btms == null) {
            setupChat()
        }
    }

    fun setupChat(){
        Log.d("Chat", "Setting up chat")

        // Initialize the send button with a listener that for click events
        submitButton!!.setOnClickListener(View.OnClickListener {
            // Send a message using content of the edit text widget
            val view = view
            if (null != view) {
                val message = sendText!!.text.toString()
                sendMessage(message)
            }
        })

        btms = BTMessageService(mesHand, activity as Activity)
        outStringBuff = StringBuffer("")

    }

    private val mesHand = object: Handler(){
        override fun handleMessage(msg: Message){
            when (msg.what){
                WRITE ->{
                    val writeBuff = msg.obj as ByteArray
                    val writeMsg = String(writeBuff)
                    Log.d("MESSAGEW", writeMsg)
                    Toast.makeText(activity, "Sent message: " + writeMsg, Toast.LENGTH_LONG).show()

                }
                READ ->{
                    val readBuff = msg.obj as ByteArray
                    val readMsg = String(readBuff)
                    Log.d("MESSAGER", readMsg)
                    Toast.makeText(activity, "Received message: " + readMsg, Toast.LENGTH_LONG).show()
                    updateTextBox(readMsg)

                }
                DEVICE_NAME -> {
                    connectedDeviceName = msg.data.getString("DevName")!!
                    Log.d("Connect", "Connected device " + connectedDeviceName)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when(requestCode){

            REQUEST_CONNECT -> {
                if(resultCode == Activity.RESULT_OK){
                    connectDevice(data!!)
                }
            }
            REQUEST_ENABLE -> {
                if(resultCode == Activity.RESULT_OK){
                    setupChat()
                }
                else{
                    Log.d("Bluetooth", "Not available")
                }
            }
        }
    }

    private fun connectDevice(data: Intent){
        val address= data.extras!!.getString("DevName")
        val device = btAdapter!!.getRemoteDevice(address)
        btms!!.connect(device)
    }

    private fun connectDevice(){

        if(deviceAddress.equals("")){
            return;
        }
        val device = btAdapter!!.getRemoteDevice(deviceAddress)
        btms!!.connect(device)
    }

    private fun sendMessage (msg: String){
        if (btms!!.getState() != btms!!.ConnectedValue()){
            Log.d("State", "Not connected to device")
        }

        if(msg.length > 0){
            val send = msg.toByteArray()
            btms!!.write(send)

            outStringBuff.setLength(0)
            sendText!!.setText(outStringBuff)
        }
    }

    fun setDevice(pos:Int){
        deviceAddress = btAdapter!!.bondedDevices.toTypedArray()[pos].address
        connectDevice()
    }

    fun updateTextBox(msg:String){
        activity!!.runOnUiThread(Runnable {
            receivedMessage!!.setText(msg)
        })
    }

}
