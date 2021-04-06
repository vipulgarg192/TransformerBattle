package com.app.transformerbattle.presenter.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.app.transformerbattle.domain.model.Transformer
import com.app.transformerbattle.network.model.TransformerDto
import com.app.transformerbattle.network.model.TransformerListDto
import com.app.transformerbattle.presenter.utils.TransformerEvents
import com.app.transformerbattle.domain.abstraction.AppRepository
import com.app.transformerbattle.repository.AppSharedPrefs
import com.app.transformerbattle.utils.Status
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject
constructor(private val appRepository: AppRepository, private val appSharedPrefs: AppSharedPrefs): ViewModel() {

    private var _result: MutableLiveData<Status<Any>> = MutableLiveData()

    val result: LiveData<Status<Any>>
        get() = _result

    private var _battleResult: MutableLiveData<Status<Any>> = MutableLiveData()

    val battleResult: LiveData<Status<Any>>
        get() = _battleResult

    private var _transformerList = MutableLiveData<Status<TransformerListDto>>()
    var transformerList: LiveData<Status<TransformerListDto>> = _transformerList

    fun resetModel() {
        _result.value = Status.DoNothing
        _battleResult.value = Status.DoNothing
        _transformerList.value = Status.DoNothing
    }

    //region $handle events from fragments
    fun onTriggerEvent(event: TransformerEvents){
        viewModelScope.launch {
            try {
                when(event){
                    is TransformerEvents.CreateTransformer -> createTransformer(event.transformer)  // this will create transformer
                    is TransformerEvents.GetTransformer -> getTransformer()
                    is TransformerEvents.LetBattleTransformer -> getBattleResult(event.autobots,event.decepticons)
                    is TransformerEvents.UpdateTransformer -> updateTransformer(event.transformer)
                    is TransformerEvents.RefreshBattle -> _battleResult.postValue(Status.Success(""))
                }
            }catch (e: Exception){
                Log.e("TAG", "launchJob: Exception: ${e}, ${e.cause}")
                e.printStackTrace()
            }
        }
    }
    //endregion


    private fun getBattleResult(autobots: TransformerDto, decepticons: TransformerDto) {
        battle(autobots,decepticons)
    }

    //region $battle function
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
    // endregion

    //region $WinnerByOverallRating
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
    // endregion

    //region $WinnerbySkill
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
    // endregion

    //region $WinnerByStrength
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
    //endregion

    // region $WinnerByCourage
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
    }// endregion


    // region $GetTransformer
    private suspend fun getTransformer() {
        try {
            _transformerList.postValue(Status.Loading)
            val transformerList = appRepository.getTransformerList(
                    token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}"
            )
            _transformerList.postValue(Status.Success(transformerList))
        }catch (ex: Exception){
            _result.postValue(Status.Error(ex))
        }
    }// endregion

    // region $CreateTransformer
    private suspend fun createTransformer(transformer: Transformer) {
        try {
            _result.postValue(Status.Loading)
            val result = appRepository.createTransformerAppRepo(
                    token =  "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
                    body = transformer
            )
            _result.postValue(Status.Success(result))
        }catch (e: Exception) {
            _result.postValue(Status.Error(e))
        }
    }
    // endregion

    // region $UpdateTransformer
    private suspend fun updateTransformer(transformer: Transformer) {
        try {
            _result.postValue(Status.Loading)
            val result = appRepository.createTransformerAppRepo(
                    token = "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
                    body = transformer
            )
            _result.postValue(Status.Success(result))
            appRepository.deleteTransformerAppRepo(
                    token = "Bearer ${appSharedPrefs.getStoredTag(AppSharedPrefs.tokenPref)}",
                    id = transformer.id.toString()
            )
        } catch (ioe: IOException) {
            _result.postValue(Status.Error(ioe))
        } catch (he: HttpException) {
            _result.postValue(Status.Error(he))
        }catch (ex: Exception){
            _result.postValue(Status.Error(ex))
        }
    }
    //endregion
}

