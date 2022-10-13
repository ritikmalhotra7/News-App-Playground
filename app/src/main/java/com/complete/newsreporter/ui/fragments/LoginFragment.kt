package com.complete.newsreporter.ui.fragments


import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import com.complete.newsreporter.R
import com.complete.newsreporter.ui.activities.NewsActivity
import com.complete.newsreporter.utils.Constants.firebaseAuth
import com.google.firebase.auth.FirebaseAuth
import com.royrodriguez.transitionbutton.TransitionButton
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_login.*
@AndroidEntryPoint
class LoginFragment : Fragment(R.layout.fragment_login) {

    private lateinit var mAuth:FirebaseAuth

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAuth = firebaseAuth
        signUpButton.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_signUpFragment)
        }
             loginPhone.setOnClickListener {
            Navigation.findNavController(it).navigate(R.id.action_loginFragment_to_loginViaPhoneFragment)
        }
        loginButton.setOnClickListener {
            if (!TextUtils.isEmpty(email.text.toString()) && !TextUtils.isEmpty(password.text.toString())) {
                loginButton.startAnimation()
                verifyEmail(email.text.toString(), password.text.toString())
            }else {
                Toast.makeText(activity, "Please Enter Something", Toast.LENGTH_SHORT).show()
            }
        }
    }
    private fun login(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
            if (task.isSuccessful) {
                loginButton.stopAnimation(
                    TransitionButton.StopAnimationStyle.EXPAND,
                    TransitionButton.OnAnimationStopEndListener {
                        val intent = Intent(activity, NewsActivity::class.java)
                        activity?.finish()
                        startActivity(intent)

                    })

            } else {
                Toast.makeText(activity, " Authentication Failed:"+task.exception, Toast.LENGTH_SHORT).show()
                loginButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                Log.d("error",task.exception.toString())
            }
        }
    }
    private fun verifyEmail(email: String, password: String) {
        mAuth.signInWithEmailAndPassword(email, password).addOnSuccessListener {
            if (it.user!!.isEmailVerified) {
                login(email, password)
            } else {
                loginButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
                Toast.makeText(activity, "Please Verify", Toast.LENGTH_SHORT).show()
            }
            mAuth.signOut()
        }.addOnFailureListener {
            loginButton.stopAnimation(TransitionButton.StopAnimationStyle.SHAKE, null)
            Toast.makeText(activity, " Authentication Failed! May be User doesn't exist", Toast.LENGTH_SHORT).show()
            Log.d("error",it.toString())
        }
    }

    override fun onStart() {
        super.onStart()
        val mAuth1 = firebaseAuth.currentUser
        if (mAuth1 != null) {
            startActivity(Intent(activity, NewsActivity::class.java))
        } else {
            super.onStart()
        }
    }
}