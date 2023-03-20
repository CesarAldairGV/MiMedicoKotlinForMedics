package com.example.mimedicokotlinformedics.ui.chat

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinfirebase.dto.Consult
import com.example.mimedicokotlinfirebase.dto.SendChatMessageRequest
import com.example.mimedicokotlinfirebase.services.ConsultService
import com.example.mimedicokotlinfirebase.services.MedicAuthService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ChatViewModel @Inject constructor(
    private val authService: MedicAuthService,
    private val consultService : ConsultService
) : ViewModel(){

    private val _messageState: MutableLiveData<Boolean> = MutableLiveData()
    val messageState: LiveData<Boolean> get() = _messageState

    private val _photoState: MutableLiveData<String> = MutableLiveData()
    val photoState: LiveData<String> get() = _photoState

    private val _consultData: MutableLiveData<ConsultData> = MutableLiveData()
    val consultData: LiveData<ConsultData> get() = _consultData

    fun sendMessage(consultId: String, message: String, photoUrl: String){
        viewModelScope.launch {
            val req = SendChatMessageRequest(
                consultId = consultId,
                message = message,
                medicPhotoUrl = photoUrl
            )
            consultService.sendMessage(req)
        }
    }

    fun getMedicPhoto(){
        viewModelScope.launch {
            val medic = authService.getCurrentMedicInfo()!!
            val photoUrl = medic.photoUrl
            _photoState.value = photoUrl
        }
    }

    fun checkMessage(message: String){
        _messageState.value = message.isNotEmpty()
    }

    fun getConsultData(consultId: String){
        viewModelScope.launch {
            _consultData.value = consultService.getConsultById(consultId)?.toConsultData()
        }
    }

    fun Consult.toConsultData() = ConsultData(
        consultId = this.consultId,
        title = this.title,
        body = this.body,
        imgUrl = this.imgUrl,
        userName = this.userName,
        userId = this.userId,
        medicId = this.userId
    )
}