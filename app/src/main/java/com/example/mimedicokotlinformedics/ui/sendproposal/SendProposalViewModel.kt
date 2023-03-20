package com.example.mimedicokotlinformedics.ui.sendproposal

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mimedicokotlinfirebase.dto.CreateProposalRequest
import com.example.mimedicokotlinfirebase.dto.Petition
import com.example.mimedicokotlinfirebase.services.MedicAuthService
import com.example.mimedicokotlinfirebase.services.PetitionService
import com.example.mimedicokotlinfirebase.services.ProposalService
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SendProposalViewModel @Inject constructor(
    private val proposalService: ProposalService,
    private val petitionService: PetitionService,
    private val medicAuthService: MedicAuthService
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
            val req = CreateProposalRequest(
                petitionId = petitionId,
                message = message,
                medicId = medicAuthService.getCurrentMedicId()!!
            )
            proposalService.createProposal(req)
            _result.value = true
        }
    }

    private fun Petition.toSendProposalPetitionInfo() = SendProposalPetitionInfo(
        subject = this.title,
        body = this.body,
        date = this.date,
        username = this.userName,
        imgUrl = this.imgUrl,
    )
}