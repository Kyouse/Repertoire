package com.reclycer.repertoire.ui.login

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.reclycer.repertoire.R
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import android.content.Intent
import android.util.Log
import android.view.View
import com.bumptech.glide.Glide
import com.google.android.gms.tasks.Task
import com.google.android.gms.common.api.ApiException
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : AppCompatActivity(){

    object Constants {
        const val REQUEST_CODE_GOOGLE_SIGNIN = 1
    }

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        Glide.with(this)
                .load(R.drawable.backgroung)
                .centerCrop()
                .into(background);

        ViewModelProviders.of(this).get(LoginViewModel::class.java)
        var gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()

        var mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
        signInButton.setSize(SignInButton.SIZE_WIDE)
        signInButton.setOnClickListener {
            val signInIntent = mGoogleSignInClient.signInIntent
            startActivityForResult(signInIntent, Constants.REQUEST_CODE_GOOGLE_SIGNIN)
        }

    }

    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == Constants.REQUEST_CODE_GOOGLE_SIGNIN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            loginViewModel.onAuthResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account = completedTask.getResult(ApiException::class.java)

            // Signed in successfully, show authenticated UI.
            updateUI(account)

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("LoginActivity", "signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }

    }


    override fun onStart() {
        super.onStart()

        val account = GoogleSignIn.getLastSignedInAccount(this)
        updateUI(account)
    }

    private fun updateUI(account: GoogleSignInAccount?) {
        if(account == null){
            displayName.visibility = View.INVISIBLE
            sign_in_button.visibility = View.VISIBLE
        }
        else{
            sign_in_button.visibility = View.INVISIBLE
            displayName.visibility = View.VISIBLE
            displayName.setText(account.displayName)
        }
    }

}