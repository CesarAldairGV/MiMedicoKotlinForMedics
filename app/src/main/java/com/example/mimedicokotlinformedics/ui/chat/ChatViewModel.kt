package com.example.mimedicokotlinformedics.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinformedics.services.AuthService
import com.example.mimedicokotlinformedics.services.ConsultService
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authService: AuthService,
    private val consultService : ConsultService
) : ViewModel(){

    private val _messageState: MutableLiveData<Boolean> = MutableLiveData()
    val messageState: LiveData<Boolean> get() = _messageState

    private val _photoState: MutableLiveData<String> = MutableLiveData()
    val photoState: LiveData<String> get() = _photoState

    private val _consultData: MutableLiveData<ConsultData> = MutableLiveData()
    val consultData: LiveData<ConsultData> get() = _consultData

    fun getConsultData(consultId: String){
        viewModelScope.launch {
            _consultData.value = consultService.getConsultData(consultId)!!.toConsultData()
        }
    }

    fun sendMessage(consultId: String, message: String, photoUrl: String){
        viewModelScope.launch {
            consultService.sendMessage(consultId,message, photoUrl)
        }
    }

    fun getMedicPhoto(){
        viewModelScope.launch {
            val medic = authService.getCurrentMedicInfo()!!
            val photoUrl = medic["photoUrl",String::class.java]!!
            _photoState.value = photoUrl
        }
    }

    fun checkMessage(message: String){
        _messageState.value = message.isNotEmpty()
    }

    fun DocumentSnapshot.toConsultData() = ConsultData(
        medicId = this["medicId",String::class.java]!!,
        userId = this["userId",String::class.java]!!
    )
}