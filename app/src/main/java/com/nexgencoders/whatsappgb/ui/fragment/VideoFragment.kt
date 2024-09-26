package com.nexgencoders.whatsappgb.ui.fragment

import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.Looper
import android.os.StrictMode
import android.os.StrictMode.VmPolicy
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.documentfile.provider.DocumentFile
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentVideoBinding
import com.nexgencoders.whatsappgb.model.Status
import com.nexgencoders.whatsappgb.ui.adapter.ImageAdapter
import com.nexgencoders.whatsappgb.ui.adapter.VideoAdapter
import com.nexgencoders.whatsappgb.utils.Common.GRID_COUNT
import com.nexgencoders.whatsappgb.utils.HomeConst.whatsappStatusDirectory
import com.nexgencoders.whatsappgb.utils.toast
import java.io.File
import java.util.Arrays
import java.util.concurrent.Executors

class VideoFragment : Fragment() {

    lateinit var binding: FragmentVideoBinding
    private val videoList: ArrayList<Status> = ArrayList()
    private var videoAdapter: VideoAdapter? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentVideoBinding.inflate(layoutInflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        } else {
            executeOld()
        }
    }

    private fun executeNew() {
        Executors.newSingleThreadExecutor().execute {
            val mainHandler = Handler(Looper.getMainLooper())
            val list = requireActivity().contentResolver.persistedUriPermissions
            videoList.clear()
            if (list.isEmpty()) {
                mainHandler.post {
                    showNoFilesFound()
                }
                return@execute
            }

            val file = DocumentFile.fromTreeUri(requireActivity(), list[0].uri)

            if (file == null) {
                // Handle null file
                mainHandler.post {
                    showNoFilesFound()
                }
                return@execute
            }

            val statusFiles = file.listFiles()

            if (statusFiles.isEmpty()) {
                // Handle empty directory
                mainHandler.post {
                    showNoFilesFound()
                }
                return@execute
            }

            // Filter for video files
            for (documentFile in statusFiles) {
                val status = Status(documentFile)
                if (status.isVideo) {
                    videoList.add(status)
                }
            }

            // Update UI on the main thread
            mainHandler.post {
                if (videoList.isEmpty()) {
                    showNoFilesFound()
                } else {
                    updateRecyclerView()
                }
                binding.swipeRefreshLayout.isRefreshing = false
            }
        }
    }

    // Updated executeOld() method
    private fun executeOld() {
        Executors.newSingleThreadExecutor().execute {
            val mainHandler = Handler(Looper.getMainLooper())
            val statusFiles: Array<File>? = whatsappStatusDirectory.listFiles()

            // Clear the video list before populating
            videoList.clear()

            if (statusFiles != null && statusFiles.isNotEmpty()) {
                Arrays.sort(statusFiles)
                for (file in statusFiles) {
                    val status = Status(file, file.name, file.absolutePath)
                    if (status.isVideo) {
                        videoList.add(status)
                    }
                }

                // Update UI on the main thread
                mainHandler.post {
                    if (videoList.isEmpty()) {
                        showNoFilesFound()
                    } else {
                        updateRecyclerView()
                    }
                }
            } else {
                // Handle empty or null statusFiles
                mainHandler.post {
                    showNoFilesFound()
                }
            }
            binding.swipeRefreshLayout.isRefreshing = false
        }
    }

    // Helper function to show "No Files Found" message and update UI
    private fun showNoFilesFound() {
        binding.progressBar.visibility = View.GONE
        binding.messageTextView.visibility = View.VISIBLE
        binding.messageTextView.setText(R.string.no_files_found)
        Toast.makeText(activity, getString(R.string.no_files_found), Toast.LENGTH_SHORT).show()
    }

    // Helper function to update RecyclerView with video list and hide progress bar
    private fun updateRecyclerView() {
        binding.messageTextView.visibility = View.GONE
        binding.messageTextView.text = ""
        videoAdapter = VideoAdapter(videoList, binding.container)
        binding.recyclerView.adapter = videoAdapter
        videoAdapter?.notifyItemRangeChanged(0, videoList.size)
        binding.progressBar.visibility = View.GONE
    }


}