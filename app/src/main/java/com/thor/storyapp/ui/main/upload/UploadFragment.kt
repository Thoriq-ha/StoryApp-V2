package com.thor.storyapp.ui.main.upload

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.asLiveData
import androidx.navigation.fragment.findNavController
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.thor.storyapp.R
import com.thor.storyapp.data.preferences.datastore.DataStoreSession
import com.thor.storyapp.databinding.FragmentUploadBinding
import com.thor.storyapp.ui.main.CameraActivity
import com.thor.storyapp.utils.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.koin.android.ext.android.inject
import java.io.File


class UploadFragment : Fragment(R.layout.fragment_upload) {
    private val binding by viewBinding(FragmentUploadBinding::bind)
    private val viewModel: UploadViewModel by inject()
    private val session: DataStoreSession by inject()
    private var getFile: File? = null
    private var lat: String? = null
    private var lon: String? = null

    private lateinit var fusedLocation: FusedLocationProviderClient

    private val cameraPermReq =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val granted = permissions.entries.all {
                it.value
            }
            if (granted) {
                activity?.let {
                    val intent = Intent(it, CameraActivity::class.java)
                    launcherIntentCameraX.launch(intent)
                }
            } else {
                Toast.makeText(
                    requireContext(), "Belum  mendapatkan permission.", Toast.LENGTH_LONG
                ).show()
            }
        }

    private val mapPermReq =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                getMyLocation()
            }
        }

    companion object {
        const val CAMERA_X_RESULT = 200
        var PERMISSIONS = arrayOf(
            Manifest.permission.CAMERA,
//            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
//            Manifest.permission.MANAGE_EXTERNAL_STORAGE,


        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fusedLocation = LocationServices.getFusedLocationProviderClient(requireContext())
        binding.buttonCamera.setOnClickListener { startCameraX() }
        binding.buttonGallery.setOnClickListener { startGallery() }
        binding.buttonUpload.setOnClickListener {
            val caption = binding.captionText.text.toString()
            if (caption.isNotEmpty()) uploadImage(caption) else {
                Toast.makeText(
                    requireContext(), "Caption belum ditambahkan.", Toast.LENGTH_SHORT
                ).show()
            }
        }
        viewModel.stateList.observe(viewLifecycleOwner, observerStateList)
        session.userFlow.asLiveData().observe(viewLifecycleOwner) {
            if (it != null) {
                viewModel.setToken("Bearer ${it.token}")
            }
        }
        getMyLocation()
    }

    private val observerStateList = Observer<UploadState> {
        showLoading(it == UploadState.OnLoading)
        when (it) {
            is UploadState.OnSuccess -> {
                Toast.makeText(requireContext(), it.uploadResponse.message, Toast.LENGTH_SHORT)
                    .show()
                findNavController().navigate(UploadFragmentDirections.actionUploadFragmentToHomeFragment())
            }
            is UploadState.OnError -> {
                Toast.makeText(requireContext(), it.message, Toast.LENGTH_SHORT).show()
            }
            UploadState.OnLoading -> {}
        }
    }

    private fun uploadImage(caption: String) {
        if (getFile != null) {
            val file = reduceFileImage(getFile as File)
            val description =
                caption.toRequestBody("text/plain".toMediaType())
            val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            val imageMultipart: MultipartBody.Part = MultipartBody.Part.createFormData(
                "photo",
                file.name,
                requestImageFile
            )
            val lat =
                lat?.toRequestBody("text/plain".toMediaTypeOrNull())
            val lon =
                lon?.toRequestBody("text/plain".toMediaTypeOrNull())

            viewModel.uploadImage(imageMultipart, description, lat, lon)
        } else {
            Toast.makeText(requireContext(), "Gambar belum ditambahkan", Toast.LENGTH_LONG)
                .show()
        }
    }

    private fun startCameraX() {
        activity?.let {
            val intent = Intent(it, CameraActivity::class.java)

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                launcherIntentCameraX.launch(intent)
            }
            if (hasPermissions(activity as Context, PERMISSIONS)) {
                launcherIntentCameraX.launch(intent)
            } else {
                cameraPermReq.launch(
                    PERMISSIONS
                )
            }
        }
    }

    private val launcherIntentCameraX = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == CAMERA_X_RESULT) {
            val myFile = it.data?.getSerializableExtra("picture") as File
            val isBackCamera = it.data?.getBooleanExtra("isBackCamera", true) as Boolean
            val result = rotateBitmap(
                BitmapFactory.decodeFile(myFile.path),
                isBackCamera
            )
            var uriFile: Uri? = null
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                uriFile = saveImageInQ(result, requireContext())
            } else saveImageInLegacy(result, requireContext())
            val fileRotated = uriToFile(Uri.parse(uriFile.toString()), requireContext())
            getFile = fileRotated
            binding.imageView.setImageBitmap(result)
        }
    }


    private fun startGallery() {
        val intent = Intent()
        intent.action = Intent.ACTION_GET_CONTENT
        intent.type = "image/*"
        val chooser = Intent.createChooser(intent, "Choose a Picture")
        launcherIntentGallery.launch(chooser)
    }

    private val launcherIntentGallery = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == AppCompatActivity.RESULT_OK) {
            val selectedImg: Uri = result.data?.data as Uri
            val myFile = uriToFile(selectedImg, requireContext())
            getFile = myFile
            binding.imageView.setImageURI(selectedImg)
        }
    }

    private fun showLoading(isLoading: Boolean) {
        binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }

    private fun getMyLocation() {
        if (ContextCompat.checkSelfPermission(
                requireActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            fusedLocation.lastLocation.addOnSuccessListener { location ->
                if (location != null) {

                    lat = location.latitude.toString()
                    lon = location.longitude.toString()
                    } else {
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.active_your_location),
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        } else {
            mapPermReq.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        }
    }


}