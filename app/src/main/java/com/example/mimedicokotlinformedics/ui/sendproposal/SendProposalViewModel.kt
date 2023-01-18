package com.example.mimedicokotlinformedics.ui.sendproposal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinformedics.services.PetitionService
import com.example.mimedicokotlinformedics.services.ProposalService
import com.google.firebase.firestore.DocumentSnapshot
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendProposalViewModel @Inject constructor(
    private val proposalService: ProposalService,
    private val petitionService: PetitionService
) : ViewModel() {

    private val _formState: MutableLiveData<SendProposalFormState> = MutableLiveData()
    val formState: LiveData<SendProposalFormState> get() = _formState

    private val _petitionInfo: MutableLiveData<SendProposalPetitionInfo> = MutableLiveData()
    val petitionInfo: LiveData<SendProposalPetitionInfo> get() = _petitionInfo

    private val _result: MutableLiveData<Boolean> = MutableLiveData()
    val result: LiveData<Boolean> get() = _result

    fun loadInfo(petitionId: String){
        viewModelScope.launch {
            _petitionInfo.value =
                petitionService.getPetitionById(petitionId)!!.toSendProposalPetitionInfo()
        }
    }

    private fun checkMessage(message: String): Boolean{
        return message != null && message.length > 20
    }

    fun checkData(message: String){
        var messageErr: Int? = null
        var isDataValid = false
        if(!checkMessage(message)){
            messageErr = 1
        }
        if(messageErr == null){
            isDataValid = true
        }
        _formState.value = SendProposalFormState(messageErr,isDataValid)
    }

    fun sendProposal(petitionId: String, message: String){
        viewModelScope.launch {
            proposalService.sendProposal(petitionId, message)
            _result.value = true
        }
    }

    private fun DocumentSnapshot.toSendProposalPetitionInfo() = SendProposalPetitionInfo(
        subject = this["subject",String::class.java]!!,
        body = this["body",String::class.java]!!,
        date = this["date",String::class.java]!!,
        username = this["name",String::class.java]!!,
        imgUrl = this["urlPhoto",String::class.java],
    )
}