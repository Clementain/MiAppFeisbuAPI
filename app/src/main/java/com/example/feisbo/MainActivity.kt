package com.example.feisbo

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.GraphRequest
import com.facebook.HttpMethod
import com.facebook.LoginStatusCallback
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var botonprueba: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        botonprueba = findViewById<View>(R.id.btnP) as Button
        val textView: TextView = findViewById(R.id.publicacion)
        val postButton: Button = findViewById(R.id.postButton)

        textView.visibility = View.GONE
        postButton.visibility = View.GONE


        // Otorgar permisos al loginButton
        loginButton.setPermissions(listOf("public_profile", "email", "publish_actions").toString())

        // Otorgar permisos al botonprueba
        botonprueba.setOnClickListener {
            LoginManager.getInstance().logInWithReadPermissions(
                this, listOf("public_profile", "email", "publish_actions")
            )
        }

        postButton.setOnClickListener {
            val message = textView.text.toString()

            val parameters = Bundle()
            parameters.putString("message", message)

            GraphRequest(AccessToken.getCurrentAccessToken(),
                "/me/feed",
                parameters,
                HttpMethod.POST,
                { response ->
                    val graphResponse = response.jsonObject
                    if (graphResponse!!.has("error")) {
                        val errorMessage = graphResponse.getJSONObject("error").getString("message")
                        Toast.makeText(
                            this@MainActivity, "Error al publicar: $errorMessage", Toast.LENGTH_LONG
                        ).show()
                    } else {
                        Toast.makeText(this@MainActivity, "Publicación exitosa", Toast.LENGTH_SHORT)
                            .show()
                    }
                }).executeAsync()
        }


        // Resto del código...
        val accessToken = AccessToken.getCurrentAccessToken()
        val isLoggedIn = accessToken != null && !accessToken.isExpired

        if(isLoggedIn){
            Toast.makeText(this@MainActivity, "Ya te has logueado", Toast.LENGTH_SHORT)
                .show()
        }

        callbackManager = CallbackManager.Factory.create()

        val EMAIL = "email"
        listOf(EMAIL)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {
                // App code
                textView.visibility = View.VISIBLE
                postButton.visibility = View.VISIBLE
            }

            override fun onCancel() {
                // App code
            }

            override fun onError(error: FacebookException) {
                // App code
            }
        })

        LoginManager.getInstance().retrieveLoginStatus(this, object : LoginStatusCallback {
            override fun onCompleted(accessToken: AccessToken) {
                // User was previously logged in, can log them in directly here.
                // If this callback is called, a popup notification appears that says
                // "Logged in as <User Name>"
                textView.visibility = View.VISIBLE
                postButton.visibility = View.VISIBLE
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
