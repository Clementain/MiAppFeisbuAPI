@file:Suppress("DEPRECATION")

package com.example.feisbo

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.ShareLinkContent
import com.facebook.share.widget.ShareDialog


class MainActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var botonprueba: Button
    private lateinit var botonposteo: Button
    private var shareDialog: ShareDialog? = null

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        /*val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired*/
        shareDialog = ShareDialog(this)


        callbackManager = CallbackManager.Factory.create()

        val EMAIL = "email"

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        botonprueba = findViewById<View>(R.id.btnP) as Button
        botonposteo = findViewById<View>(R.id.postButton) as Button
        listOf(EMAIL)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // App code
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(error: FacebookException) {
                // App code
            }
        })


        botonprueba.setOnClickListener {


            LoginManager.getInstance().logInWithReadPermissions(this, listOf("public_profile"))
        }
        botonposteo.setOnClickListener {
            val content: ShareLinkContent = ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com")).build()
            if (ShareDialog.canShow(ShareLinkContent::class.java)) {
                shareDialog!!.show(content)
            }
        }


        LoginManager.getInstance().retrieveLoginStatus(this, object : LoginStatusCallback {
            override fun onCompleted(accessToken: AccessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
            }

            override fun onFailure() {
                // No access token could be retrieved for the user
            }

            override fun onError(exception: Exception) {
                // An error occurred
            }
        })


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

}