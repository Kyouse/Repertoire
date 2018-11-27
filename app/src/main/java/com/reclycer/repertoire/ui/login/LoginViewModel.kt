package com.reclycer.repertoire.ui.login

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.MutableLiveData
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.reclycer.repertoire.RepertoireApp
import com.reclycer.repertoire.data.DataManager
import com.reclycer.repertoire.data.DatabaseManager
import io.reactivex.android.schedulers.AndroidSchedulers

class LoginViewModel(application: Application): AndroidViewModel(application) {


    val repertoireApp : RepertoireApp
        get()= getApplication<RepertoireApp>()
    private val databaseManager : DatabaseManager
        get() = repertoireApp.databaseManager
    private val dataManager : DataManager
        get() = repertoireApp.dataManager
    val currentLoginStatus = MutableLiveData<LoginStatus>()

    fun onAuthResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
//            updateUI(account)
            currentLoginStatus.value = LoginStatus.Running
            dataManager.sendAccount(account)
                    .observeOn(AndroidSchedulers.mainThread()).subscribe({

                    },{

                    })

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
//            updateUI(null)
            currentLoginStatus.value = LoginStatus.Error
        }

    }

    sealed class LoginStatus{
        object Init: LoginStatus()
        object Running : LoginStatus()
        sealed class Error{

        }
        class Success(displayName : String){

        }
    }

}