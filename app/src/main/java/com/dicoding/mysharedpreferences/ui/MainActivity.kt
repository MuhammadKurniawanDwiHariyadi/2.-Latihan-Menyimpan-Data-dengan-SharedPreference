package com.dicoding.mysharedpreferences.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.WindowManager
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.mysharedpreferences.R
import com.dicoding.mysharedpreferences.databinding.ActivityMainBinding
import com.dicoding.mysharedpreferences.model.UserModel
import com.dicoding.mysharedpreferences.util.UserPreference

class MainActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var binding: ActivityMainBinding

    private lateinit var mUserPreference: UserPreference // ketika object userPreference diciptakan
    /*
    Ketika Anda membuat obyek dari kelas UserPreference pada Activity berikutnya, maka obyek Shared Preferences akan diciptakan dan hanya diciptakan sekali. Jika sudah ada, obyek yang sudah ada yang akan dikembalikan. Semua itu Anda lakukan di konstruktor kelas UserPreference.
     */

    private var isPreferenceEmpty = false
    private lateinit var userModel: UserModel

    // result code merupakan hasil dari activity setelahnya (Intent dengan mengembalikan data)
    private val resultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            if (result.data != null && result.resultCode == FormUserPreferenceActivity.RESULT_CODE) {
                userModel =
                    result.data?.getParcelableExtra<UserModel>(FormUserPreferenceActivity.EXTRA_RESULT) as UserModel
                populateView(userModel)
                checkForm(userModel)
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
//        enableEdgeToEdge()
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)

        setContentView(binding.root)
//        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
//            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
//            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
//            insets
//        }


        supportActionBar?.title = "My User Preference" // menyetel title pada bar

        mUserPreference = UserPreference(this) // menginisialisasi object

        showExistingPreference() // memanggil preference yang sudah ada

        binding.BTSave.setOnClickListener(this) // aksi menekan tombol

    }

    private fun showExistingPreference() { // memanggil refrerence jika ada
        userModel = mUserPreference.getUser() // mengambil preference dimana terdapat return value yaitu UserModel yang telah di set
        populateView(userModel) // menampilkanya pada view dengan argument dari return value
        checkForm(userModel) // mengecek apakah userModel sudah ada isi atau belum
    }

    private fun populateView(userModel: UserModel) { // menampilkan data ke view jika ada dan jika tidak di set disini
        binding.TVName.text = userModel.name.toString().ifEmpty { "Tidak Ada" }
        binding.TVAge.text = userModel.age.toString().ifEmpty { "Tidak Ada" }
        binding.TVIsLoveMu.text = if (userModel.isLove) "Ya" else "Tidak"
        binding.TVEmail.text = userModel.email.toString().ifEmpty { "Tidak Ada" }
        binding.TVPhone.text = userModel.phoneNumber.toString().ifEmpty { "Tidak Ada" }
    }

    private fun checkForm(userModel: UserModel) { // pengecekan userModel
        when {
            userModel.name.toString().isNotEmpty() -> { // jika sudah ada isi
                binding.BTSave.text = getString(R.string.change) // merubah tombol menjadi rubah
                isPreferenceEmpty = false // mengeset variabel menjadi false
            }
            else -> { // jika belum ada isi
                binding.BTSave.text = getString(R.string.save) // merubah tombol menjadi simpan
                isPreferenceEmpty = true // mengeset variabel menjadi true
            }
        }
    }

    override fun onClick(v: View?) { // jika tombol di tekan
        if (v?.id == R.id.BTSave) {
            val intent = Intent(this@MainActivity, FormUserPreferenceActivity::class.java) // menginisialisasi untuk pindah Intent
            when {
                isPreferenceEmpty -> { // mengecek apakah true atau false dari UserModel yang sudah di isi atau belum
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM, // menyiapkan key
                        FormUserPreferenceActivity.TYPE_ADD // isi dari value jika data pada UserModel kosong
                    )
                    intent.putExtra("USER", userModel) // mengirim data UserModel
                }
                else -> {
                    intent.putExtra(
                        FormUserPreferenceActivity.EXTRA_TYPE_FORM, // menyiapkan key
                        FormUserPreferenceActivity.TYPE_EDIT // isi dari value jika ada pada UserModel ada
                    )
                    intent.putExtra("USER", userModel) // mengirim data UserModel
                }
            }
            resultLauncher.launch(intent) // Ini intent yang mengembalikan data, dengan launch dari ActivityResultLauncher hasil dari registerForActivityForResult
        }
    }

}