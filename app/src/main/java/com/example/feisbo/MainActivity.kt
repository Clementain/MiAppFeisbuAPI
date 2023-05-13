@file:Suppress("DEPRECATION", "SameParameterValue")

package com.example.feisbo

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.facebook.AccessToken
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginResult
import com.facebook.login.widget.LoginButton
import com.facebook.share.model.SharePhoto
import com.facebook.share.model.SharePhotoContent
import com.facebook.share.model.ShareVideo
import com.facebook.share.model.ShareVideoContent
import com.facebook.share.widget.ShareDialog


class MainActivity : AppCompatActivity() {
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginButton: LoginButton
    private lateinit var btnF: Button
    private lateinit var btnV: Button

    private var shareDialog: ShareDialog? = null
    private val REQUEST_SELECT_PHOTO = 1
    private val REQUEST_SELECT_VIDEO = 2
    private val REQUEST_READ_STORAGE_PERMISSION = 10

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (isReadStoragePermissionGranted(this)) {
            return
        } else {
            requestReadStoragePermission(this, REQUEST_READ_STORAGE_PERMISSION)
        }
        shareDialog = ShareDialog(this)


        callbackManager = CallbackManager.Factory.create()

        val EMAIL = "email"

        loginButton = findViewById<View>(R.id.login_button) as LoginButton
        btnF = findViewById<View>(R.id.btnF) as Button
        btnV = findViewById<View>(R.id.btnV) as Button
        listOf(EMAIL)

        // Callback registration
        loginButton.registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(result: LoginResult) {

            }

            override fun onCancel() {

            }

            override fun onError(error: FacebookException) {

            }
        })





        btnF.setOnClickListener {
            val isLoggedIn =
                AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken()!!.isExpired
            if (isLoggedIn) {
                val photoPickerIntent = Intent(Intent.ACTION_PICK)
                photoPickerIntent.type = "image/*"
                startActivityForResult(photoPickerIntent, REQUEST_SELECT_PHOTO)
            } else {
                Toast.makeText(
                    applicationContext, "Inicia sesión primero", Toast.LENGTH_SHORT
                ).show()
            }

        }

        btnV.setOnClickListener {
            val isLoggedIn =
                AccessToken.getCurrentAccessToken() != null && !AccessToken.getCurrentAccessToken()!!.isExpired

            if (isLoggedIn) {
                val videoPickerIntent = Intent(Intent.ACTION_PICK)
                videoPickerIntent.type = "video/*"
                startActivityForResult(videoPickerIntent, REQUEST_SELECT_VIDEO)
            } else {
                Toast.makeText(
                    applicationContext, "Inicia sesión primero", Toast.LENGTH_SHORT
                ).show()
            }
        }


    }

    @Deprecated("Deprecated in Java")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_SELECT_PHOTO -> {
                    val selectedPhotoUri = data?.data
                    if (selectedPhotoUri != null) {
                        val photo = SharePhoto.Builder().setImageUrl(selectedPhotoUri).build()

                        val content = SharePhotoContent.Builder().addPhoto(photo).build()

                        if (ShareDialog.canShow(SharePhotoContent::class.java)) {
                            shareDialog!!.show(content)
                        }
                    }
                }

                REQUEST_SELECT_VIDEO -> {
                    val selectedVideoUri = data?.data
                    if (selectedVideoUri != null) {
                        val video = ShareVideo.Builder().setLocalUrl(selectedVideoUri).build()

                        val content = ShareVideoContent.Builder().setVideo(video).build()

                        if (ShareDialog.canShow(ShareVideoContent::class.java)) {
                            shareDialog!!.show(content)
                        }
                    }
                }
            }
        }
    }

    private fun isReadStoragePermissionGranted(context: Context): Boolean {
        val result = ContextCompat.checkSelfPermission(
            context, Manifest.permission.READ_EXTERNAL_STORAGE
        )
        return result == PackageManager.PERMISSION_GRANTED
    }

    private fun requestReadStoragePermission(activity: Activity, requestCode: Int) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), requestCode
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_STORAGE_PERMISSION) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                return
            } else {
                // Permiso denegado, maneja la situación en consecuencia
                Toast.makeText(
                    applicationContext,
                    "Necesitas permisos para poder usar esta función",
                    Toast.LENGTH_LONG
                ).show()

                finish()
            }
        }
    }


}