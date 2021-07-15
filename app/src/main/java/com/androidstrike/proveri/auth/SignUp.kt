package com.androidstrike.proveri.auth

import android.content.Context
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.androidstrike.bias.utils.setOnSingleClickListener
import com.androidstrike.bias.utils.toast
import com.androidstrike.proveri.utils.Common
import com.androidstrike.proveri.R
import com.rengwuxian.materialedittext.MaterialEditText

class SignUp : Fragment(), AdapterView.OnItemSelectedListener  {

    private var emailAddress: String? = null
    lateinit var uName: String
    lateinit var email: String
    lateinit var password: String
    lateinit var confirmPassword: String
    lateinit var department: String
    lateinit var etUsername: MaterialEditText
    lateinit var etUserEmail: MaterialEditText
    lateinit var etUserPassword: MaterialEditText
    lateinit var etUserConfirmPassword: MaterialEditText
    lateinit var pbLoading: LinearLayout


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_sign_up, container, false)

        val signUpTxtLogin = view.findViewById<TextView>(R.id.signup_txt_login)
        val signUpButtonRegister = view.findViewById<Button>(R.id.sign_up_btn_register)
        etUsername = view.findViewById(R.id.sign_up_user_name)
        etUserEmail = view.findViewById(R.id.sign_up_email)
        etUserPassword = view.findViewById(R.id.sign_up_password)
        etUserConfirmPassword = view.findViewById(R.id.sign_up_confirm_password)
        pbLoading = view.findViewById(R.id.pb_sign_up)


        signUpTxtLogin.setOnClickListener {
            findNavController().navigate(R.id.action_signUp_to_signIn)
        }

        signUpButtonRegister.setOnSingleClickListener {
            uName = etUsername.text.toString().trim()
            email = etUserEmail.text.toString().trim()
            password = etUserPassword.text.toString().trim()
            confirmPassword = etUserConfirmPassword.text.toString().trim()

            validateInput()

        }

        return view
    }

    private fun validateInput() {

        //if user name field is empty
        if (uName.isEmpty()) {
            etUsername.error = "First Name Required"
            etUsername.requestFocus()
            return
        }
        // if the email and password fields are empty we display error messages
        if (email.isEmpty()) {
            etUserEmail.error = "Email Required"
            etUserEmail.requestFocus()
            return
        }

        //if the email pattern/format does not does match that as defined, we display error messages
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etUserEmail.error = "Valid Email Required"
            etUserEmail.requestFocus()
            return
        }
        //if the password contains less than 6 characters we display error message
        if (password.isEmpty() || password.length < 6) {
            etUserPassword.error = "6 char password required"
            etUserPassword.requestFocus()
            return
        }

        if (confirmPassword != password) {
            etUserConfirmPassword.error = "Does Not Match Password"
            etUserConfirmPassword.requestFocus()
            return
        }
//        if (!Common.PASSWORD_PATTERN.matcher(password).matches()) {
//            sign_up_password.error =
//                "Password too weak. Must Contain at least one uppercase, one lowercase, one number and one character"
//            sign_up_password.requestFocus()
//            return
//        }
        else {
            registerUser(email, password)
        }
    }

    private fun registerUser(email: String, password: String) {
        //  implement user sign up
        pbLoading.visibility = View.VISIBLE
        Common.mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    val newUserId = Common.mAuth.uid
                    activity?.toast(newUserId.toString())

//                    isFirstTime()
                    pbLoading.visibility = View.GONE
                    findNavController().navigate(R.id.action_signUp_to_signIn)
                } else {
                    it.exception?.message?.let {
                        pbLoading.visibility = View.GONE
                        activity?.toast(it)
                    }
                }
            }
    }

    //boolean shared pref to store whether user is using the app for the 1st time
    private fun isFirstTime() {
        val sharedPref =
            requireActivity().getSharedPreferences(Common.sharedPrefName, Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putBoolean(Common.firstTimeKey, false)
        editor.putString(Common.userNamekey, uName)
        editor.putString(Common.departmentKey, department)
        editor.apply()
    }


    override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

    }

    override fun onNothingSelected(parent: AdapterView<*>?) {

    }

}