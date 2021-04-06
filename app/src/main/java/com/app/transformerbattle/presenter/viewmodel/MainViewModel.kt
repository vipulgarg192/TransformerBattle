package com.app.transformerbattle.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerListDto
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.repository.AppRepository
import com.app.transformerbattle.repository.AppSharedPrefs
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(private val appRepository: AppRepository, private val appSharedPrefs: AppSharedPrefs): ViewModel() {

    companion object{
        private const val TAG = "MainViewModel"
    }
    private var _loading : MutableLiveData<Boolean> = MutableLiveData()
    val loading : LiveData<Boolean>
        get() = _loading

    private var _result: MutableLiveData<Status<Any>> = MutableLiveData()

    val result: LiveData<Status<Any>>
        get() = _result

    private var _battleResult: MutableLiveData<Status<Any>> = MutableLiveData()
    val battleResult: LiveData<Status<Any>>
        get() = _battleResult

    private var _transformerList = MutableLiveData<TransformerListDto>()
    var transformerList: LiveData<TransformerListDto> = _transformerList

    private var _autobotsList = MutableLiveData<TransformerListDto>()
    var autobotsList: LiveData<TransformerListDto> = _transformerList

    var decepticonsList: LiveData<TransformerListDto> = _transformerList




    fun onTriggerEvent(event: TransformerEvents){
        viewModelScope.launch {
            try {
                when(event){
                    is TransformerEvents.CreateTransformer -> createTransformer(event.transformer)  // this will create transformer
                    is TransformerEvents.GetTransformer -> getTransformer()
                    is TransformerEvents.LetBattleTransformer -> getBattleResult(event.autobots,event.decepticons)
                }
            }catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }

    private fun getBattleResult(autobots: TransformerDto, decepticons: TransformerDto) {
        battle(autobots,decepticons)
    }

    private fun battle(autobots: TransformerDto, decepticons: TransformerDto){

        if (autobots.name.equals(decepticons.name)){
            _battleResult.postValue(Status.Success("1 Battle \n Match Tie"))
            return
        }else if (autobots.name.equals("Optimus Prime") && decepticons.name.equals("Predaking")){
            _battleResult.postValue(Status.Success("1 Battle  \n Match Tie"))
            return
        }else if(autobots.name.equals("Optimus Prime")){
            _battleResult.postValue(Status.Success("1 Battle \n Winning team (Autobots) ${autobots.name}  \n losing team (Decepticons) ${decepticons.name}"))
            return
        }else if (decepticons.name.equals("Predaking")){
            _battleResult.postValue(Status.Success("1 Battle \n Winning team (Decepticons) ${decepticons.name}  \n losing team (Autobots) ${autobots.name}"))
            return
        }

        val winnerOfCourage = checkCourageIsBiggerby4(autobots,decepticons)
        val winnerOfStrength = checkStrengthIsBiggerby3(autobots,decepticons)
        val winnerOfSkill= checkSkillIsBiggerby3(autobots,decepticons)
        val winnerOfOverall = checkOverallRating(autobots,decepticons)


        if ((winnerOfCourage !=null) && (winnerOfSkill !=null) && (!winnerOfCourage.equals(winnerOfSkill))){
            if (winnerOfCourage.equals(autobots)){
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Autobots) ${autobots.name}  \n losing team (Decepticons) ${decepticons.name}"))
                return
            }else if (winnerOfCourage.equals(decepticons)){
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Decepticons) ${decepticons.name}  \n losing team (Autobots) ${autobots.name}"))
                return
            }
        }

        if (winnerOfStrength!=null){
            if (winnerOfStrength.equals(autobots)){
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Autobots) ${autobots.name}  \n losing team (Decepticons) ${decepticons.name}"))
                return
            }else{
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Decepticons) ${decepticons.name}  \n losing team (Autobots) ${autobots.name}"))
                return
            }
        }

        if (winnerOfOverall!=null){
            if (winnerOfOverall.equals(autobots)){
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Autobots) ${autobots.name}  \n losing team (Decepticons) ${decepticons.name}"))
                return
            }else{
                _battleResult.postValue(Status.Success("1 Battle \n Winning team (Decepticons) ${decepticons.name}  \n losing team (Autobots) ${autobots.name}"))
                return
            }
        }else{
            _battleResult.postValue(Status.Success("1 Battle \n Match Tie"))
            return
        }

    }

    private fun checkOverallRating(autobots: TransformerDto, decepticons: TransformerDto): TransformerDto? {
        val autobotTotalValue = autobots.rank + autobots.strength +
                autobots.courage + autobots.endurance +
                autobots.firepower + autobots.firepower+
                autobots.speed+ autobots.skill

        val decepticonTotalValue = decepticons.rank + decepticons.strength +
                decepticons.courage + decepticons.endurance +
                decepticons.firepower + decepticons.firepower+
                decepticons.speed+ decepticons.skill

        return if (autobotTotalValue>decepticonTotalValue){
            autobots
        }else if (decepticonTotalValue>autobotTotalValue){
            decepticons
        }else{
            null
        }
    }

    private fun checkSkillIsBiggerby3(autobots: TransformerDto, decepticons: TransformerDto): TransformerDto? {
        return if ((autobots.skill - decepticons.skill >= 3) || (decepticons.skill - autobots.skill >= 3)) {
            if (autobots.skill > decepticons.skill) {
                autobots
            } else {
                decepticons
            }
        }else{
            null
        }
    }

    private fun checkStrengthIsBiggerby3(autobots: TransformerDto, decepticons: TransformerDto): TransformerDto? {
        return if ((autobots.strength - decepticons.strength >= 3) || (decepticons.strength - autobots.strength >= 3)) {
            if (autobots.strength > decepticons.strength) {
                autobots
            } else {
                decepticons
            }
        }else{
            null
        }
    }

    private fun checkCourageIsBiggerby4(autobots: TransformerDto, decepticons: TransformerDto) : TransformerDto? {
        return if ((autobots.courage - decepticons.courage >= 4) || (decepticons.courage - autobots.courage >= 4)) {
            if (autobots.courage > decepticons.courage) {
                autobots
            } else {
                decepticons
            }
        }else{
            null
        }
    }

    private fun checkForSpecialtransformer() {
        TODO("Not yet implemented")
    }

    private fun getDecepticons() {
//        decepticonsList = transformerList.filter { transformerListDto ->
//            transformerListDto?.transformer?.all {
//                it.team.contains("d")
//            }
//        }
    }

    private fun getAutobots() {

//        autobotsList = transformerList.filter { transformerListDto ->
//            transformerListDto?.transformer?.all {
//                    it.team.contains("A")
//            }
//        }

    }

    private suspend fun getTransformer() {
        _loading.postValue(true)
        val transformerList = appRepository.getTransformerList(
            token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}"
        )
        Log.e("TAG", "getTransformer: ${transformerList} ")

        _transformerList.postValue(transformerList)
        _loading.postValue(false)
    }

    private suspend fun createTransformer(transformer: Transformer) {
        _result.postValue(Status.Loading)
        val result = appRepository.createTransformerAppRepo(
            token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
            contentType = "application/json",
            body = transformer
        )
        _result.postValue(Status.Success(result))
//        _loading.postValue(false)
    }
}

