package com.nexgencoders.whatsappgb.ui.fragment

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentScanCodeBinding
import com.nexgencoders.whatsappgb.ui.ads.MyInterstitialAds
import com.nexgencoders.whatsappgb.utils.Common.await
import com.nexgencoders.whatsappgb.utils.Common.getBitmapFromUri
import com.nexgencoders.whatsappgb.utils.showNotificationPermissionDialog
import com.nexgencoders.whatsappgb.utils.showWarning
import com.nexgencoders.whatsappgb.utils.toast
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class ScanCodeFragment : Fragment() {
    private lateinit var binding: FragmentScanCodeBinding
    private val CAMERA_PERMISSION_REQUEST_CODE = 1001
    private lateinit var permissionLauncher: ActivityResultLauncher<String>


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentScanCodeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        permissionLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                context?.toast("Camera permission is required")
            }
        }
        with(binding) {
            btnCamera.setOnClickListener {
                checkCameraPermission()
            }
            btnGallery.setOnClickListener {
                openGallery()
            }

            btnOk.setOnClickListener {
                checkCameraPermission()
            }
        }
    }

    private fun checkCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            else -> {
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        findNavController().navigate(R.id.action_qrCodeFragment_to_scannerFragment)
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_PICK)
        } else {
            context?.toast("No app available to handle gallery")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_PICK -> {
                    val imageUri: Uri = data?.data!!
                    scanImageForQRCode(requireContext(),imageUri)
                }
            }
        } else {
            context?.toast("Operation canceled")
        }
    }


    private fun scanImageForQRCode(context: Context, imageUri: Uri) {
        val bitmap = getBitmapFromUri(context, imageUri)
        if (bitmap == null) {
            context.toast("Could not load image")
            return
        }
        val image = InputImage.fromBitmap(bitmap, 0)
        val options = BarcodeScannerOptions.Builder()
            .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
            .build()
        val scanner = BarcodeScanning.getClient(options)
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val barcodes = scanner.process(image).await()
                withContext(Dispatchers.Main) {
                    if (barcodes.isNotEmpty()) {
                        val qrCodeValue = barcodes.first().rawValue
                        qrCodeValue?.let {
                            val action = QrCodeFragmentDirections.actionQrCodeFragmentToQrResultFragment(it,true)
                            findNavController().navigate(action)
                        }
                    } else {
                        requireContext().showWarning {
                                openGallery()
                        }
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    context.toast("Error scanning QR code")
                }
            }
        }
    }


    companion object {
        private const val REQUEST_IMAGE_PICK = 2

    }
}