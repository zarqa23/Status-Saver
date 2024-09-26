package com.nexgencoders.whatsappgb.ui.fragment

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.storage.StorageManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.ACTIVITY_SERVICE
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentImageBinding
import com.nexgencoders.whatsappgb.model.Status
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter
import com.nexgencoders.whatsappgb.utils.Common.GRID_COUNT
import com.nexgencoders.whatsappgb.utils.PrefHelper
import com.nexgencoders.whatsappgb.utils.gone
import com.nexgencoders.whatsappgb.utils.showStatusPermissionDialog
import com.nexgencoders.whatsappgb.utils.toast
import com.nexgencoders.whatsappgb.utils.visible
import java.io.File
import java.util.Objects
import java.util.concurrent.Executors

class ImageFragment : Fragment() {
    private lateinit var binding: FragmentImageBinding
    private val imagesList: ArrayList<Status> = ArrayList<Status>()
    private var imageAdapter: ImageAdapter? = null
    private var type:String=""
    private var context: Context? = null

    private val REQUEST_PERMISSIONS: Int = 1234

    private val PERMISSIONS: Array<String> = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )

    private var activityResultLauncher: ActivityResultLauncher<Intent> =
        registerForActivityResult<Intent, ActivityResult>(
            ActivityResultContracts.StartActivityForResult()
        ) { result: ActivityResult ->
            if (result.resultCode == RESULT_OK) {
                val data = checkNotNull(result.data)
                context?.contentResolver?.takePersistableUriPermission(
                    data.data!!,
                    Intent.FLAG_GRANT_READ_URI_PERMISSION or
                            Intent.FLAG_GRANT_WRITE_URI_PERMISSION
                )
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentImageBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        type = arguments?.getString("type") ?: ""
        if (type == "edit"){
            if (arePermissionDenied()) {
                requireContext().showStatusPermissionDialog {
                    if (it) {
                        findNavController().navigateUp()
                    } else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            requestPermissionQ()
                        } else {
                            requestPermissions(PERMISSIONS, REQUEST_PERMISSIONS)
                        }
                    }
                }
            }
            binding.toolBar.visible()
            binding.btnBack.setOnClickListener {
                findNavController().navigateUp()
            }
        }
        else{
            binding.toolBar.gone()
        }
        binding.swipeRefreshLayout.setColorSchemeColors(
            ContextCompat.getColor(requireActivity(), android.R.color.holo_orange_dark),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_green_dark),
            ContextCompat.getColor(requireActivity(), R.color.colorPrimary),
            ContextCompat.getColor(requireActivity(), android.R.color.holo_blue_dark)
        )

        binding.swipeRefreshLayout.setOnRefreshListener(OnRefreshListener { this.getStatus() })

        binding.recyclerView.setHasFixedSize(true)
        binding.recyclerView.setLayoutManager(GridLayoutManager(activity, GRID_COUNT))
        getStatus()
    }

    private fun getStatus() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            executeNew()
        } else{
            executeOld()
        }
    }

    private fun executeOld() {
        Executors.newSingleThreadExecutor().execute {
            val mainHandler = Handler(Looper.getMainLooper())
            val whatsappStatusDirectory = File(
                Environment.getExternalStorageDirectory().toString() +
                        File.separator + "WhatsApp/Media/.Statuses"
            )
            val whatsappBusinessStatusDirectory = File(
                Environment.getExternalStorageDirectory().toString() +
                        File.separator + "WhatsApp Business/Media/.Statuses"
            )
            val statusFiles = mutableListOf<File>()
            if (whatsappStatusDirectory.exists()) {
                statusFiles.addAll(whatsappStatusDirectory.listFiles()?.toList() ?: emptyList())
            }
            if (whatsappBusinessStatusDirectory.exists()) {
                statusFiles.addAll(whatsappBusinessStatusDirectory.listFiles()?.toList() ?: emptyList())
            }

            imagesList.clear()

            if (statusFiles.isNotEmpty()) {
                statusFiles.sortBy { it.lastModified() }

                for (file in statusFiles) {
                    if (file.name.contains(".nomedia")) continue
                    val status = Status(file, file.name, file.absolutePath)
                    if (!status.isVideo && status.title!!.endsWith(".jpg")) {
                        imagesList.add(status)
                    }
                }

                mainHandler.post {
                    if (imagesList.size <= 0) {
                        binding.messageTextView.visibility = View.VISIBLE
                        binding.messageTextView.text = getString(R.string.status_txt)
                    } else {
                        binding.messageTextView.visibility = View.GONE
                        binding.messageTextView.text = ""
                    }
                    imageAdapter = ImageAdapter(imagesList, type)
                    binding.recyclerView.adapter = imageAdapter
                    imageAdapter!!.notifyItemRangeChanged(0, imagesList.size)
                    binding.progressBar.visibility = View.GONE
                }
            } else {
                mainHandler.post {
                    binding.progressBar.visibility = View.GONE
                    binding.messageTextView.visibility = View.VISIBLE
                    binding.messageTextView.text = getString(R.string.status_txt)
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    private fun executeNew() {
        Executors.newSingleThreadExecutor().execute {
            val mainHandler = Handler(Looper.getMainLooper())

            // Get the list of persisted URI permissions (to check for WhatsApp or WhatsApp Business access)
            val uriPermissions = requireActivity().contentResolver.persistedUriPermissions

            imagesList.clear()

            if (uriPermissions.isEmpty()) {
                mainHandler.post {
                    binding.progressBar.visibility = View.GONE
                    binding.messageTextView.visibility = View.VISIBLE
                    binding.messageTextView.text = getString(R.string.status_txt)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                return@execute
            }

            // Iterate through persisted URIs to check for WhatsApp and WhatsApp Business directories
            val statusFilesList = mutableListOf<DocumentFile>()

            for (permission in uriPermissions) {
                val file = DocumentFile.fromTreeUri(requireActivity(), permission.uri)
                if (file != null) {
                    statusFilesList.addAll(file.listFiles().toList())
                }
            }

            if (statusFilesList.isEmpty()) {
                // No files found in both WhatsApp and WhatsApp Business
                mainHandler.post {
                    binding.progressBar.visibility = View.GONE
                    binding.messageTextView.visibility = View.VISIBLE
                    binding.messageTextView.text = getString(R.string.status_txt)
                    binding.swipeRefreshLayout.isRefreshing = false
                }
                return@execute
            }

            // Filter and add status files (ignoring .nomedia and non-images/videos)
            for (documentFile in statusFilesList) {
                val fileName = documentFile.name ?: continue
                if (fileName.contains(".nomedia")) continue

                val status = Status(documentFile)

                if (!status.isVideo) { // You can add logic to check if it's an image or a video
                    imagesList.add(status)
                }
            }

            mainHandler.post {
                if (imagesList.isEmpty()) {
                    binding.messageTextView.visibility = View.VISIBLE
                    binding.messageTextView.text = getString(R.string.status_txt)
                } else {
                    binding.messageTextView.visibility = View.GONE
                    binding.messageTextView.text = ""
                }
                imageAdapter = ImageAdapter(imagesList, type)
                binding.recyclerView.adapter = imageAdapter
                imageAdapter?.notifyItemRangeChanged(0, imagesList.size)
                binding.progressBar.visibility = View.GONE
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }



    @RequiresApi(Build.VERSION_CODES.Q)
    private fun requestPermissionQ() {
        val sm = context?.getSystemService(Context.STORAGE_SERVICE) as StorageManager
        val intent = sm.primaryStorageVolume.createOpenDocumentTreeIntent()
        val startDir = "Android%2Fmedia%2Fcom.whatsapp%2FWhatsApp%2FMedia%2FWhatsApp%2FStatuses"
        val uri = intent.getParcelableExtra<Uri>("android.provider.extra.INITIAL_URI")
        val scheme = uri.toString().replace("/root/", "/document/") + "%3A$startDir"

        intent.putExtra("android.provider.extra.INITIAL_URI", Uri.parse(scheme))
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION or Intent.FLAG_GRANT_WRITE_URI_PERMISSION or Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION)

        activityResultLauncher.launch(intent)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_PERMISSIONS && grantResults.isNotEmpty()) {
            if (arePermissionDenied()) {
                (Objects.requireNonNull(requireContext().getSystemService(ACTIVITY_SERVICE)) as ActivityManager).clearApplicationUserData()
                requireActivity().recreate()
            }
        }
    }


    private fun arePermissionDenied(): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            return requireActivity().contentResolver.persistedUriPermissions.size <= 0
        }
        for (permissions: String in PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(
                    requireContext(),
                    permissions
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return true
            }
        }
        return false
    }

}