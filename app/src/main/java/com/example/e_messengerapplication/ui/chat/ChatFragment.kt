package com.example.e_messengerapplication.ui.chat

import android.content.ContentUris
import android.media.MediaRecorder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.*
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.e_messengerapplication.AppStore
import com.example.e_messengerapplication.R
import com.example.e_messengerapplication.databinding.FragmentChatBinding
import com.example.e_messengerapplication.utils.Constant.TAG_MESSAGE
import com.google.android.material.bottomsheet.BottomSheetBehavior
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File
import javax.inject.Inject

@AndroidEntryPoint
class ChatFragment : Fragment() {

    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!

    private val viewModel: ChatViewModel by viewModels()

    @Inject
    lateinit var appStore: AppStore

    private lateinit var conversationId: String
    private lateinit var conversationName: String
    private lateinit var conversationAvatar: String

    private lateinit var imageBottomSheet: BottomSheetBehavior<LinearLayout>
    private lateinit var voiceBottomSheet: BottomSheetBehavior<LinearLayout>

    private var mediaRecorder: MediaRecorder? = null
    private var audioFile: File? = null
    private var isPaused = false
    private var secondsElapsed = 0
    private var recordTimer: Job? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        retrieveArguments()
        viewModel.fetchMessage(conversationId)
        setupUI()
    }

    private fun retrieveArguments() {
        conversationId = arguments?.getString("conversationId").orEmpty()
        conversationName = arguments?.getString("conversationName").orEmpty()
        conversationAvatar = arguments?.getString("conversationAvatar").orEmpty()
    }

    private fun setupUI() {
        setupBottomSheet()
        setupRecyclerView()
        setupSendMessageActions()
        setupImagePicker()
        setupVoiceBottomSheetControls()
        setupEditTextFocusBehavior()
        binding.tvDisplayName.text = conversationName
        binding.btnBack.setOnClickListener { findNavController().navigateUp() }
        val secureUrl = conversationAvatar.replace("http://", "https://")
        Glide.with(binding.root.context)
            .load(secureUrl)
            .into(binding.imageViewAvatar)
    }

    private fun setupRecyclerView() {
        val adapter = MessagesAdapter(appStore)
        binding.listMessages.apply {
            this.adapter = adapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        }

        lifecycleScope.launch {
            viewModel.messages.collectLatest { messages ->
                adapter.submitList(messages) {
                    if (messages.isNotEmpty()) {
                        binding.listMessages.scrollToPosition(messages.size - 1)
                    }
                }
            }
        }
    }

    private fun setupSendMessageActions() {
        binding.btnSendMessage.setOnClickListener {
            val message = binding.edtMessage.text.toString().trim()
            if (message.isNotEmpty()) {
                viewModel.sendMessage(conversationId, message)
                binding.edtMessage.text.clear()
                binding.edtMessage.clearFocus()
            }
        }
    }

    private fun setupBottomSheet() {
        imageBottomSheet = BottomSheetBehavior.from(binding.bottomSheetImage)
        voiceBottomSheet = BottomSheetBehavior.from(binding.bottomSheetVoice)
        imageBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        voiceBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN

        setupSpacerSync(imageBottomSheet)
        setupSpacerSync(voiceBottomSheet)

        binding.btnSendImage.setOnClickListener {
            toggleBottomSheet(imageBottomSheet)
        }

        binding.btnSendVoice.setOnClickListener {
            toggleBottomSheet(voiceBottomSheet)
        }
    }

    private fun toggleBottomSheet(target: BottomSheetBehavior<LinearLayout>) {
        if (target.state == BottomSheetBehavior.STATE_HIDDEN) {
            // Hide others
            if (target != imageBottomSheet) imageBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            if (target != voiceBottomSheet) voiceBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            target.state = BottomSheetBehavior.STATE_COLLAPSED
        } else {
            target.state = BottomSheetBehavior.STATE_HIDDEN
        }
    }

    private fun setupSpacerSync(sheet: BottomSheetBehavior<LinearLayout>) {
        sheet.addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
            override fun onStateChanged(bottomSheet: View, newState: Int) {
                if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                    binding.bottomSheetSpacer.layoutParams.height = 0
                    binding.bottomSheetSpacer.requestLayout()
                }
            }

            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                val peek = sheet.peekHeight
                val expanded = bottomSheet.height
                val offsetHeight = if (slideOffset >= 0) {
                    peek + (expanded - peek) * slideOffset
                } else {
                    peek * (1 + slideOffset)
                }.coerceAtLeast(0f)

                val newHeight = offsetHeight.toInt()
                if (binding.bottomSheetSpacer.layoutParams.height != newHeight) {
                    binding.bottomSheetSpacer.layoutParams.height = newHeight
                    binding.bottomSheetSpacer.requestLayout()
                }
            }
        })
    }

    private fun setupVoiceBottomSheetControls() {
        val btnStart = requireView().findViewById<Button>(R.id.btnStartRecord)
        val btnPause = requireView().findViewById<Button>(R.id.btnPauseRecord)
        val btnStop = requireView().findViewById<Button>(R.id.btnStopRecord)
        val tvTimer = requireView().findViewById<TextView>(R.id.tvRecordTimer)

        btnStart.setOnClickListener {
            startRecording()
            startTimer(tvTimer)
            btnStart.visibility = View.GONE
            btnPause.visibility = View.VISIBLE
            btnStop.visibility = View.VISIBLE
        }

        btnPause.setOnClickListener {
            if (!isPaused) {
                pauseRecording()
                btnPause.text = "Resume"
            } else {
                resumeRecording()
                btnPause.text = "Pause"
            }
            isPaused = !isPaused
        }

        btnStop.setOnClickListener {
            stopRecording()
            stopTimer()
            btnStart.visibility = View.VISIBLE
            btnPause.visibility = View.GONE
            btnStop.visibility = View.GONE
            tvTimer.text = "00:00"
        }
    }

    private fun startTimer(tv: TextView) {
        secondsElapsed = 0
        recordTimer = lifecycleScope.launch {
            while (true) {
                delay(1000)
                if (!isPaused) {
                    secondsElapsed++
                    val mins = secondsElapsed / 60
                    val secs = secondsElapsed % 60
                    tv.text = String.format("%02d:%02d", mins, secs)
                }
            }
        }
    }

    private fun stopTimer() {
        recordTimer?.cancel()
        recordTimer = null
    }

    private fun startRecording() {
        audioFile = File(requireContext().cacheDir, "voice_${System.currentTimeMillis()}.m4a")
        mediaRecorder = MediaRecorder().apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setOutputFile(audioFile!!.absolutePath)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            prepare()
            start()
        }
    }

    private fun pauseRecording() {
        mediaRecorder?.pause()
    }

    private fun resumeRecording() {
        mediaRecorder?.resume()
    }

    private fun stopRecording() {
        mediaRecorder?.apply {
            stop()
            release()
        }
        mediaRecorder = null

        // Gửi file
        audioFile?.let { file ->
            viewModel.sendAudio(conversationId, file)
        }
        voiceBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
    }


    private fun setupImagePicker() {
        val recyclerView = binding.root.findViewById<RecyclerView>(R.id.recyclerViewImages)
        val adapter = ImageAdapter { uri ->
            binding.layoutSendImage.visibility = View.VISIBLE

            binding.btnSelect.setOnClickListener {
                Log.d(TAG_MESSAGE, "Image selected: $uri")
                viewModel.sendImage(requireContext(), conversationId, uri)
                binding.layoutSendImage.visibility = View.GONE
                imageBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        binding.btnCancel.setOnClickListener {
            binding.layoutSendImage.visibility = View.GONE
            imageBottomSheet.state = BottomSheetBehavior.STATE_HIDDEN
        }

        lifecycleScope.launch(Dispatchers.IO) {
            val images = loadImagesFromMediaStore()
            withContext(Dispatchers.Main) {
                adapter.submitList(images)
            }
        }
    }

    private fun loadImagesFromMediaStore(): List<Uri> {
        val images = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        val maxImages = 100

        context?.contentResolver?.query(
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
            projection, null, null, sortOrder
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext() && images.size < maxImages) {
                val id = cursor.getLong(idColumn)
                val uri = ContentUris.withAppendedId(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id)
                images.add(uri)
            }
        }

        return images
    }

    private fun setupEditTextFocusBehavior() {
        binding.edtMessage.setOnFocusChangeListener { _, hasFocus ->
            with(binding.edtMessage.layoutParams as ConstraintLayout.LayoutParams) {
                matchConstraintPercentWidth = if (hasFocus) 0.75f else 0.6f
                horizontalBias = if (hasFocus) 0f else 0.6f
                binding.edtMessage.layoutParams = this
            }

            binding.btnSendImage.visibility = if (hasFocus) View.GONE else View.VISIBLE
            binding.btnSendVoice.visibility = if (hasFocus) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
