package com.dicoding.mysharedpreferences.ui

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mysharedpreferences.R
import com.dicoding.mysharedpreferences.databinding.ActivityFormUserPreferenceBinding
import com.dicoding.mysharedpreferences.model.UserModel
import com.dicoding.mysharedpreferences.util.UserPreference

class FormUserPreferenceActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityFormUserPreferenceBinding

    private lateinit var userModel: UserModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFormUserPreferenceBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        setContentView(binding.root)
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }

        binding.BTSave.setOnClickListener(this)

        userModel = intent.getParcelableExtra<UserModel>("USER") as UserModel
        val formType = intent.getIntExtra(EXTRA_TYPE_FORM, 0)

        var actionBarTitle = ""
        var btnTitle = ""

        when (formType) { // akan mengecek data yang dikirim dari Intent sebelumnya
            TYPE_ADD -> {
                actionBarTitle = "Tambah Baru"
                btnTitle = "Simpan"
            }
            TYPE_EDIT -> {
                actionBarTitle = "Ubah"
                btnTitle = "Update"
                showPreferenceInForm()
            }
        }

        supportActionBar?.title = actionBarTitle
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        binding.BTSave.text = btnTitle
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.BTSave -> {
                val name = binding.ETName.text.toString().trim()
                val email = binding.ETEmail.text.toString().trim()
                val age = binding.ETAge.text.toString().trim()
                val phoneNo = binding.ETPhone.text.toString().trim()
                val isLoveMU = binding.RGLoveMu.checkedRadioButtonId == R.id.RBYes
                when{
                    name.isEmpty() -> binding.ETName.error = FIELD_REQUIRED
                    email.isEmpty() -> binding.ETEmail.error = FIELD_REQUIRED
                    !isValidEmail(email) -> binding.ETEmail.error = FIELD_IS_NOT_VALID
                    age.isEmpty() -> binding.ETAge.error = FIELD_REQUIRED
                    phoneNo.isEmpty() -> binding.ETPhone.error = FIELD_REQUIRED
                    !TextUtils.isDigitsOnly(phoneNo) -> binding.ETPhone.error = FIELD_DIGIT_ONLY
                }

                saveUser(name, email, age, phoneNo, isLoveMU)

                // memberikan result untuk di terima pada activity sebelumnya
                val resultIntent = Intent()
                resultIntent.putExtra(EXTRA_RESULT, userModel)
                setResult(RESULT_CODE, resultIntent)

                finish()
            }

        }
    }

    private fun saveUser(name: String, email: String, age: String, phoneNo: String, isLoveMU: Boolean) {
        val userPreference = UserPreference(this)

        userModel.name = name
        userModel.email = email
        userModel.age = Integer.parseInt(age)
        userModel.phoneNumber = phoneNo
        userModel.isLove = isLoveMU

        userPreference.setUser(userModel)
        Toast.makeText(this, "Data tersimpan", Toast.LENGTH_SHORT).show()
    }

    private fun isValidEmail(email: CharSequence): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches() // mengecek email yang valid
    }

    private fun showPreferenceInForm() {
        binding.ETName.setText(userModel.name)
        binding.ETEmail.setText(userModel.email)
        binding.ETAge.setText(userModel.age.toString())
        binding.ETPhone.setText(userModel.phoneNumber)
        if (userModel.isLove) {
            binding.RBYes.isChecked = true
        } else {
            binding.RBNo.isChecked = true
        }
    }


    companion object {
        const val EXTRA_TYPE_FORM = "extra_type_form"
        const val EXTRA_RESULT = "extra_result"
        const val RESULT_CODE = 101
        const val TYPE_ADD = 1
        const val TYPE_EDIT = 2
        private const val FIELD_REQUIRED = "Field tidak boleh kosong"
        private const val FIELD_DIGIT_ONLY = "Hanya boleh terisi numerik"
        private const val FIELD_IS_NOT_VALID = "Email tidak valid"
    }
}