package com.example.mimedicokotlinformedics.ui.videochat

import android.os.Bundle
import android.support.annotation.NonNull
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.mimedicokotlinformedics.R
import com.example.mimedicokotlinformedics.databinding.FragmentVideochatBinding
import com.google.firebase.Timestamp
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.HashMap


class VideochatFragment : Fragment() {

    private lateinit var peerId: String
    private lateinit var myId: String
    private lateinit var consultId: String


    private var isPeerConnected = false

    private val firebaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()

    private var isAudio = true
    private var isVideo = true

    private var newData = false

    private lateinit var uniqueId: String
    private lateinit var callId: String

    private var _binding: FragmentVideochatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideochatBinding.inflate(inflater, container, false)

        peerId = arguments?.getString("peerUsername")!!
        myId = arguments?.getString("myUsername")!!
        consultId = arguments?.getString("consultId")!!

        binding.callBtn.setOnClickListener{
            sendCallRequest()
        }

        binding.toggleAudioBtn.setOnClickListener{
            isAudio = !isAudio
            callJavascriptFunction("javascript:toggleAudio(\"$isAudio\")");
            binding.toggleAudioBtn
                .setImageResource(if (isAudio) R.drawable.ic_baseline_mic_24 else R.drawable.ic_baseline_mic_off_24)
        }

        binding.toggleVideoBtn.setOnClickListener{
            isVideo = !isVideo;
            callJavascriptFunction("javascript:toggleVideo(\"$isVideo\")");
            binding.toggleVideoBtn
                .setImageResource(if(isVideo) R.drawable.ic_baseline_videocam_24 else R.drawable.ic_baseline_videocam_off_24)
        }

        setUpWebView()

        return binding.root
    }

    private fun sendCallRequest(){
        if(!isPeerConnected){
            Toast.makeText(requireContext(), "User not connected!",Toast.LENGTH_LONG).show()
            return
        }

        callId = UUID.randomUUID().toString()

        val videochatData = VideochatData(
            uniqueId = null,
            timestamp = Timestamp.now().seconds)


        firebaseRef.getReference("videochat")
            .child(consultId)
            .child(peerId)
            .child(callId)
            .setValue(videochatData)


        firebaseRef.getReference("videochat")
            .child(consultId)
            .child(peerId)
            .child(callId)
            .addValueEventListener(object: ValueEventListener {

                override fun onDataChange(@NonNull snapshot : DataSnapshot) {
                    val value = snapshot.value as Map<String, Any>
                    Log.d("ViodechatFragment", value.toString())
                    if(value.get("uniqueId") != null){
                        Log.d("ViodechatFragment", value.toString())
                        listenForConnId(value.get("uniqueId") as String)
                    }
                }

                override fun onCancelled(@NonNull error : DatabaseError) {
                }
            })
    }

    private fun listenForConnId(uniqueId: String){
        switchControls()
        callJavascriptFunction("javascript:startCall(\"$uniqueId\")");
    }

    fun setUpWebView(){
        binding.webView.webChromeClient = object : WebChromeClient(){
            override fun onPermissionRequest(request : PermissionRequest) {
                if(request != null) request.grant(request.resources)
            }
        }

        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.mediaPlaybackRequiresUserGesture = false
        binding.webView.addJavascriptInterface( JavascriptInterface(this@VideochatFragment),"Android")
        binding.webView.settings.domStorageEnabled = true
        WebView.setWebContentsDebuggingEnabled(true)

        loadVideoCall()
    }

    private fun loadVideoCall(){
        val path = "file:android_asset/call.html"
        binding.webView.loadUrl(path)
        binding.webView.webViewClient = object : WebViewClient() {
            override fun onPageFinished(view : WebView, url : String) {
                initializePeer()
            }
        }
    }

    private fun initializePeer(){
        uniqueId = UUID.randomUUID().toString()
        callJavascriptFunction("javascript:init(\"$uniqueId\")")
        firebaseRef.getReference("videochat")
            .child(consultId)
            .child(myId)
            .orderByChild("timestamp")
            .limitToLast(1)
            .addChildEventListener(object : ChildEventListener{
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                    if(!newData){
                        newData = true
                        return
                    }
                    Log.d("VideochatFragment",snapshot.key!!)
                    if(snapshot.value != null){
                        onCallRequest(snapshot.key!!)
                    }
                }

                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onChildRemoved(snapshot: DataSnapshot) {

                }

                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

                }

                override fun onCancelled(error: DatabaseError) {

                }

            })
    }

    private fun onCallRequest(key: String){
        binding.callLayout.visibility = View.VISIBLE;
        binding.incomingCallTxt.text = peerId + "is calling"

        binding.acceptBtn.setOnClickListener{
            firebaseRef.getReference("videochat")
                .child(consultId)
                .child(myId)
                .child(key)
                .child("uniqueId").setValue(uniqueId)

            switchControls()
        }

        binding.rejectBtn.setOnClickListener{
            binding.callLayout.visibility = View.GONE
        }
    }

    private fun switchControls(){
        binding.callLayout.visibility = View.GONE
        binding.inputLayout.visibility = View.GONE
        binding.callControlLayout.visibility = View.VISIBLE
    }

    fun onPeerConnected(){
        isPeerConnected = true
    }

    private fun callJavascriptFunction(functionString: String){
        binding.webView.post{
            binding.webView.evaluateJavascript(functionString, null)
        }
    }

    override fun onDestroy() {
        binding.webView.loadUrl("about:blank")
        super.onDestroy()
    }

}