package com.iman.story_akhir1.ui.add

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import com.iman.story_akhir1.R
import com.iman.story_akhir1.com.CamUtility
import com.iman.story_akhir1.com.SharedPreferenceProvider
import com.iman.story_akhir1.core.Resource
import com.iman.story_akhir1.databinding.ActivityAddBinding
import com.iman.story_akhir1.ui.custom.CustomTextInput
import com.iman.story_akhir1.ui.home.HomeActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.*

class AddActivity : AppCompatActivity(), LocationListener {
    private lateinit var binding : ActivityAddBinding
    private val viewModel: AddViewModel by viewModel()
    private var getFile: File? = null
    private lateinit var currentPhotoPath: String
    private var lat: Float = -6.857053F
    private var lon: Float = 107.53229F
    private lateinit var locationManager: LocationManager

    private val launcherIntentCamera = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = BitmapFactory.decodeFile(myFile.path)
            binding.ivStory.setImageBitmap(result)
        }
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, this)
            getFile = myFile
            binding.ivStory.setImageURI(selectedImg)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = "Add Story"
        }

            with(binding) {
            getLocation()

            btCamera.setOnClickListener {
                openCamera()
            }

            btnGallery.setOnClickListener {
                openGallery()
            }

            etDescription.globalChange()

            btnSubmit.setOnClickListener {
                val token = SharedPreferenceProvider(applicationContext).getToken()!!
                val file = reduceFileImage(getFile as File)
                val description = etDescription.text.toString()
                val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                    "photo",
                    file.name,

                    requestImageFile
                )
                viewModel.setStoryParam(token, imageMultipart, description, lat, lon)
                viewModel.postStory.observe(this@AddActivity) {
                    when (it) {
                        is Resource.Loading -> {
                            viewLoading.root.visibility = View.VISIBLE
                        }
                        is Resource.Success -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(
                                this@AddActivity,
                                resources.getString(R.string.success_add_new_story),
                                Toast.LENGTH_SHORT
                            ).show()
                            Intent(this@AddActivity, HomeActivity::class.java)
                                .apply {
                                    finish()
                                    startActivity(this)
                                }
                        }
                        is Resource.Error -> {
                            viewLoading.root.visibility = View.GONE
                            Toast.makeText(this@AddActivity, it.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                    }
                }
            }
        }
    }

    fun reduceFileImage(file: File): File {
        val bitmap = BitmapFactory.decodeFile(file.path)
        var compressQuality = 100
        var streamLength: Int
        do {
            val bmpStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, bmpStream)
            val bmpPicByteArray = bmpStream.toByteArray()
            streamLength = bmpPicByteArray.size
            compressQuality -= 5
        } while (streamLength > 1000000)
        bitmap.compress(Bitmap.CompressFormat.JPEG, compressQuality, FileOutputStream(file))
        return file
    }

    private fun openGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        intent.resolveActivity(packageManager)
        CamUtility.createCustomTempFile(application).also {
            val photoURI: Uri = FileProvider.getUriForFile(
                this,
                "com.iman.story_akhir1",
                it
            )
            currentPhotoPath = it.absolutePath
            intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
            launcherIntentCamera.launch(intent)

        }
    }

    private fun uriToFile(selectedImg: Uri, context: Context): File {
        val contentResolver: ContentResolver = context.contentResolver
        val myFile = CamUtility.createCustomTempFile(context)

        val inputStream = contentResolver.openInputStream(selectedImg) as InputStream
        val outputStream: OutputStream = FileOutputStream(myFile)
        val buf = ByteArray(1024)
        var len: Int
        while (inputStream.read(buf).also { len = it } > 0) outputStream.write(buf, 0, len)
        outputStream.close()
        inputStream.close()

        return myFile
    }


    private fun CustomTextInput.globalChange() {
        this.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                with(binding) {
                    btnSubmit.isEnabled = etDescription.isValid == true && getFile != null
                }
            }

        })
    }

    private fun getLocation() {
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 2)
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 5f, this)
    }

    override fun onLocationChanged(location: Location) {
        lon = location.longitude.toFloat()
        lat = location.latitude.toFloat()
    }

}