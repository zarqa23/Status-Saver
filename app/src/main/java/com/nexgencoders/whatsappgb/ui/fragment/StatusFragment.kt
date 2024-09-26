package com.nexgencoders.whatsappgb.ui.fragment

import android.Manifest
import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.storage.StorageManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity.ACTIVITY_SERVICE
import androidx.appcompat.app.AppCompatActivity.RESULT_OK
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import com.nexgencoders.whatsappgb.R
import com.nexgencoders.whatsappgb.databinding.FragmentStatusBinding
import com.nexgencoders.whatsappgb.ui.activity.HomeActivity
import com.nexgencoders.whatsappgb.ui.adapter.StatusViewPageAdapter
import com.nexgencoders.whatsappgb.utils.showStatusPermissionDialog
import java.util.Objects


class StatusFragment : Fragment() {
    private var _binding: FragmentStatusBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewPagerAdapter: StatusViewPageAdapter
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
                setViewPagerAdapter()
            }
        }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStatusBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context = requireContext().applicationContext
        initViewPager()
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
        binding.btnOpenDrwaer.setOnClickListener {
            (activity as? HomeActivity)?.openDrawer()
        }
    }

    private fun initViewPager() {
        viewPagerAdapter = StatusViewPageAdapter(requireActivity())
        binding.viewPager.adapter = viewPagerAdapter
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.customView = layoutInflater.inflate(R.layout.tab_custom_layout, null).apply {
                findViewById<ImageView>(R.id.tab_icon).setImageResource(
                    when (position) {
                        0 -> R.drawable.ic_image
                        1 -> R.drawable.ic_video
                        else -> 0
                    }
                )
                findViewById<TextView>(R.id.tab_text).text = when (position) {
                    0 -> "Images"
                    1 -> "Videos"
                    else -> null
                }
            }
        }.attach()
    }

    private fun setViewPagerAdapter() {
    }

    @RequiresApi(api = Build.VERSION_CODES.Q)
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
            } else {
                setViewPagerAdapter()
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

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}