package com.complete.newsreporter.ui.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.complete.newsreporter.R
import com.complete.newsreporter.databinding.DialogViewBinding
import com.complete.newsreporter.databinding.FragmentLoginViaPhoneBinding
import com.complete.newsreporter.model.User
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.utils.Constants.dbReferenceUsers
import com.complete.newsreporter.utils.Constants.firebaseAuth
import com.google.firebase.FirebaseException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login_via_phone.*
import java.util.concurrent.TimeUnit
@AndroidEntryPoint
class LoginViaPhoneFragment : Fragment(R.layout.fragment_login_via_phone) {
    private var _binding: FragmentLoginViaPhoneBinding? = null

    lateinit var storedVerificationId: String
    private lateinit var resendToken: PhoneAuthProvider.ForceResendingToken
    private lateinit var callbacks: PhoneAuthProvider.OnVerificationStateChangedCallbacks
    private lateinit var mProgressDialog: Dialog

    private lateinit var name: String


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        hide()
        otp.isEnabled = false
        verify.isEnabled = false
        resend.isEnabled = false
        loginBtn.setOnClickListener {
            verify.isEnabled = true
            otp.isEnabled = true
            loginBtn.isEnabled = false
            phoneNumber.isEnabled = false
            resend.isEnabled = true
            show()
            login()
        }
        resend.setOnClickListener {
            verify.isEnabled = false
            otp.isEnabled = false
            loginBtn.isEnabled = true
            phoneNumber.isEnabled = true
            resend.isEnabled = false
        }
        verify.setOnClickListener {
            show()
            val otp = otp.text.toString().trim()
            if (otp.isNotEmpty()) {
                val credential: PhoneAuthCredential = PhoneAuthProvider.getCredential(
                    storedVerificationId, otp
                )
                signInWithPhoneAuthCredential(credential)
            } else {
                hide()
                Toast.makeText(activity, "Enter OTP", Toast.LENGTH_SHORT).show()
            }
        }

        callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
            }

            override fun onVerificationFailed(e: FirebaseException) {
                verify.isEnabled = false
                otp.isEnabled = false
                loginBtn.isEnabled = true
                phoneNumber.isEnabled = true
                resend.isEnabled = true
                hide()
                Toast.makeText(context, e.toString(), Toast.LENGTH_LONG).show()
                Log.d("tagetfailed", e.toString())
            }

            override fun onCodeSent(
                verificationId: String,
                token: PhoneAuthProvider.ForceResendingToken
            ) {
                hide()
                Log.d("TAG", "onCodeSent:$verificationId")
                storedVerificationId = verificationId
                resendToken = token
                Toast.makeText(activity, "Code Sent", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    hide()
                    val phone = phoneNumber.text.toString().trim()
                    try {
                        val b = DialogViewBinding.inflate(layoutInflater)
                        val mBuilder = AlertDialog.Builder(requireContext())
                            .setView(b.root)
                            .setTitle("Login Form")
                            .setNegativeButton(
                                "Cancel"
                            ) { dialog, which ->
                                dialog?.cancel()
                                firebaseAuth.signOut()
                                verify.isEnabled = false
                                otp.isEnabled = false
                                loginBtn.isEnabled = true
                                phoneNumber.isEnabled = true
                                resend.isEnabled = false
                                Toast.makeText(
                                    activity,
                                    "This OTP is not valid now!",
                                    Toast.LENGTH_LONG
                                ).show()
                            }
                            .setPositiveButton("Continue") { dialog, which ->
                                name = b.yourname.text.toString().trim()
                                addUserToDB(name, phone, firebaseAuth.currentUser?.uid)
                                val intent = Intent(activity, NewsActivity::class.java)
                                startActivity(intent)
                                Toast.makeText(activity, "Welcome $name", Toast.LENGTH_SHORT).show()
                                dialog?.dismiss()
                            }
                        mBuilder.show()
                        hide()
                    } catch (e: Exception) {
                        hide()
                        Log.d("taget", e.toString())
                    }


                } else {
                    hide()
                    Log.d("taget", task.exception.toString())
                    Toast.makeText(
                        activity, "Authentication failed.",
                        Toast.LENGTH_SHORT
                    ).show()

                }
            }
    }

    private fun login() {
        var number = phoneNumber.text.toString().trim()

        if (number.isNotEmpty()) {
            number = "+91$number"
            sendVerificationCode(number)


        } else {
            Toast.makeText(activity, "Enter mobile number", Toast.LENGTH_SHORT).show()
            verify.isEnabled = false
            otp.isEnabled = false
            loginBtn.isEnabled = true
            phoneNumber.isEnabled = true
            resend.isEnabled = false

        }
    }

    private fun sendVerificationCode(number: String) {
        val options = PhoneAuthOptions.newBuilder(firebaseAuth)
            .setPhoneNumber(number) // Phone number to verify
            .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
            .setActivity(requireActivity()) // Activity (for callback binding)
            .setCallbacks(callbacks) // OnVerificationStateChangedCallbacks
            .build()
        PhoneAuthProvider.verifyPhoneNumber(options)
    }

    private fun addUserToDB(name: String, phone: String, uid: String?) {
        var u = User(name, phone, null, uid)
        dbReferenceUsers.child(uid!!).setValue(u).addOnSuccessListener {
            Log.d("taget", u.toString())
        }
        Log.d("tag", u.toString())

    }

    fun show() {
        cl.visibility = View.VISIBLE
    }

    fun hide() {
        cl.visibility = View.INVISIBLE
    }

}