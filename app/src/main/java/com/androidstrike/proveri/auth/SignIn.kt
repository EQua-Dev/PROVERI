package com.androidstrike.proveri.auth

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidstrike.proveri.utils.Common
import com.androidstrike.bias.utils.toast
import com.androidstrike.proveri.MainActivity
import com.androidstrike.proveri.R
import com.google.firebase.auth.FirebaseUser
import com.rengwuxian.materialedittext.MaterialEditText
import kotlinx.android.synthetic.main.fragment_sign_in.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class SignIn : Fragment() {

    lateinit var password: String
    lateinit var email: String
    lateinit var txtWelcomeNote: TextView
    lateinit var txtSignUp: TextView
    lateinit var txtForgotPassword: TextView
    lateinit var btnRegister: Button
    lateinit var etEmail: MaterialEditText
    lateinit var etPassword: MaterialEditText
    lateinit var pbLoading: LinearLayout


    var userName: String = "User"
    private var firebaseUser: FirebaseUser? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_in, container, false)

        txtWelcomeNote = view.findViewById(R.id.log_in_welcome_message)
        btnRegister = view.findViewById(R.id.log_in_btn_register)
        etEmail = view.findViewById(R.id.log_in_email)
        etPassword = view.findViewById(R.id.log_in_password)
        txtSignUp = view.findViewById(R.id.login_tv_signup)
        txtForgotPassword = view.findViewById(R.id.login_forgot_password)
        pbLoading = view.findViewById(R.id.pb_sign_in)

        //display user name if user has previously used app on device
//        if (isFirstTime())
//            txtWelcomeNote.text = "Welcome"
//        else
            txtWelcomeNote.text = "Welcome Back $userName"

        btnRegister.setOnClickListener {
            //first run validation on input
            email = etEmail.text.toString()
            password = etPassword.text.toString()

            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                etEmail.error = "Valid Email Required"
                etEmail.requestFocus()
            }
            if (password.isEmpty()) {
                etPassword.error = "Password Required"
                etPassword.requestFocus()
            } else //perform sign in
                signIn(email, password)
        }

        txtForgotPassword.setOnClickListener {
            //navigate to password reset screen
            findNavController().navigate(R.id.action_signIn_to_forgotPassword)
        }

        txtSignUp.setOnClickListener {
            //navigate to sign up screen
            findNavController().navigate(R.id.action_signIn_to_signUp)
        }

        return view
    }

    private fun signIn(email: String, password: String) {
        //implement sign in method
        pbLoading.visibility = View.VISIBLE
        try {
            email.let { Common.mAuth.signInWithEmailAndPassword(it, password) }
                .addOnCompleteListener { it ->
                    if (it.isSuccessful) {
                        //login success
                        pbLoading.visibility = View.GONE
                        val i = Intent(requireContext(), MainActivity::class.java)
                        startActivity(i)
                    } else {
                        activity?.toast(it.exception?.message.toString())
                    }
                }
        } catch (e: Exception) {
            pbLoading.visibility = View.GONE
            activity?.toast(e.message.toString())
            Log.d("Equa", "signIn: ${e.message.toString()}")
        }
    }


    //boolean shared pref to store whether user is using the app for the 1st time
    private fun isFirstTime(): Boolean {
        //get the from the shared preference: user name and return true if user has previously launched app on device
        val sharedPref =
            requireActivity().getSharedPreferences(Common.sharedPrefName, Context.MODE_PRIVATE)
        userName = sharedPref.getString(Common.userNamekey, "User").toString()
        return sharedPref.getBoolean(Common.firstTimeKey, true)

    }


}