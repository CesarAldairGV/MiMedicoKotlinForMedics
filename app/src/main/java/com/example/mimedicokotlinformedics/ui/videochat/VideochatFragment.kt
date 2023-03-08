package com.example.mimedicokotlinformedics.ui.videochat

import android.os.Bundle
import android.support.annotation.NonNull
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.PermissionRequest
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import com.example.mimedicokotlinformedics.R
import com.example.mimedicokotlinformedics.databinding.FragmentVideochatBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*

class VideochatFragment : Fragment() {

    private lateinit var peerUsername: String
    private lateinit var myUsername: String
    private var isPeerConnected = false
    private val firebaseRef: FirebaseDatabase = FirebaseDatabase.getInstance()
    private var isAudio = true
    private var isVideo = true
    private var uniqueId: String? = null

    private var _binding: FragmentVideochatBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentVideochatBinding.inflate(inflater, container, false)

        peerUsername = arguments?.getString("peerUsername")!!
        myUsername = arguments?.getString("myUsername")!!

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
            Toast.makeText(requireContext(), "User not connected!", Toast.LENGTH_LONG).show()
            return
        }
        firebaseRef.getReference("videochat").child(peerUsername).child("incoming").setValue(myUsername)
        firebaseRef.getReference("videochat").child(peerUsername).child("isAvailable").addValueEventListener(object: ValueEventListener {

            override fun onDataChange(@NonNull snapshot : DataSnapshot) {
                if(snapshot.value == null) return;
                if(snapshot.value.toString() == "true"){
                    listenForConnId()
                }
            }

            override fun onCancelled(@NonNull error : DatabaseError) {

            }
        })
    }

    private fun listenForConnId(){
        firebaseRef.getReference("videochat").child(peerUsername).child("connId").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot : DataSnapshot) {
                if(snapshot.value == null) return
                switchControls()
                callJavascriptFunction("javascript:startCall(\""+ snapshot.value.toString() + "\")");
            }

            override fun onCancelled(@NonNull error : DatabaseError) {
            }
        })
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
        firebaseRef.getReference("videochat").child(myUsername).child("incoming").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(@NonNull snapshot : DataSnapshot) {
                if(snapshot.value != null){
                    onCallRequest(snapshot.value.toString())
                }
            }

            override fun onCancelled(@NonNull error : DatabaseError) {
            }
        })
    }

    private fun onCallRequest(caller: String){
        if(caller == null) return
        binding.callLayout.visibility = View.VISIBLE;
        binding.incomingCallTxt.text = caller + "is calling"

        binding.acceptBtn.setOnClickListener{
            firebaseRef.getReference("videochat").child(myUsername).child("connId").setValue(uniqueId)
            firebaseRef.getReference("videochat").child(myUsername).child("isAvailable").setValue(true)

            binding.callLayout.visibility = View.GONE
            switchControls()
        }

        binding.rejectBtn.setOnClickListener{
            firebaseRef.getReference("videochat").child(myUsername).child("incoming").setValue(null)
            binding.callLayout.visibility = View.GONE
        }
    }

    private fun switchControls(){
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
        firebaseRef.getReference("videochat").child(myUsername).setValue(null)
        binding.webView.loadUrl("about:blank")
        super.onDestroy()
    }

}