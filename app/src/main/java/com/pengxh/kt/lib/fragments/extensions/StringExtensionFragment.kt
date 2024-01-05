package com.pengxh.kt.lib.fragments.extensions

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import com.pengxh.kt.lib.databinding.FragmentStringExtensionBinding
import com.pengxh.kt.lite.base.KotlinBaseFragment
import com.pengxh.kt.lite.extensions.breakLine
import com.pengxh.kt.lite.extensions.createLogFile
import com.pengxh.kt.lite.extensions.dateToTimestamp
import com.pengxh.kt.lite.extensions.diffCurrentTime
import com.pengxh.kt.lite.extensions.formatToYearMonthDay
import com.pengxh.kt.lite.extensions.getHanYuPinyin
import com.pengxh.kt.lite.extensions.isChinese
import com.pengxh.kt.lite.extensions.isEarlierThenCurrent
import com.pengxh.kt.lite.extensions.isEmail
import com.pengxh.kt.lite.extensions.isLetterAndDigit
import com.pengxh.kt.lite.extensions.isNumber
import com.pengxh.kt.lite.extensions.isPhoneNumber
import com.pengxh.kt.lite.extensions.show
import com.pengxh.kt.lite.extensions.timestampToCompleteDate
import com.pengxh.kt.lite.extensions.writeToFile
import java.io.File
import java.util.Calendar

class StringExtensionFragment : KotlinBaseFragment<FragmentStringExtensionBinding>() {

    private val kTag = "StringExtensionFragment"
    private val calendar by lazy { Calendar.getInstance() }
    private lateinit var logFile: File

    override fun initViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentStringExtensionBinding {
        return FragmentStringExtensionBinding.inflate(inflater, container, false)
    }

    override fun setupTopBarLayout() {

    }

    override fun initOnCreate(savedInstanceState: Bundle?) {
        binding.timeView.text = System.currentTimeMillis().timestampToCompleteDate()

        binding.earlierResultView.text =
            "${binding.timeView.text.toString().isEarlierThenCurrent()}"

        binding.timePickerView.setIs24HourView(true)

        binding.currentTimeView.text = System.currentTimeMillis().timestampToCompleteDate()

        logFile = requireContext().createLogFile()
    }

    override fun observeRequestState() {

    }

    override fun initEvent() {
        binding.chineseButton.setOnClickListener {
            binding.chineseResultView.text = binding.chineseView.text.toString().getHanYuPinyin()
        }

        binding.breakLineButton.setOnClickListener {
            binding.breakLineResultView.text = binding.breakLineView.text.toString().breakLine(24)
        }

        binding.timeToTimestampButton.setOnClickListener {
            binding.timestampResultView.text =
                "${binding.timeView.text.toString().dateToTimestamp()}"
        }

        val month = (calendar.get(Calendar.MONTH) + 1).appendZero()
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH).appendZero()
        val date = "${calendar.get(Calendar.YEAR)}-${month}-${dayOfMonth}"

        var time: String? = null
        binding.timePickerView.setOnTimeChangedListener { _, hourOfDay, minute ->
            time = "${hourOfDay.appendZero()}:${minute.appendZero()}:00"
            binding.selectedTimeView.text = "所选时间：$date $time"
        }

        binding.timeDiffButton.setOnClickListener {
            if (time == null) {
                "请选择需要计算时间差的时间".show(requireContext())
                return@setOnClickListener
            }
            val s = "$date $time"
            binding.diffTimeView.text = "时间差：${s.diffCurrentTime()}小时"
        }

        binding.deleteHourMinuteSecondsButton.setOnClickListener {
            binding.currentDateView.text =
                binding.currentTimeView.text.toString().formatToYearMonthDay()
        }

        binding.judgeNumberButton.setOnClickListener {
            if (binding.numberInputView.text.isNullOrBlank()) {
                binding.numberResultView.text = false.toString()
            } else {
                binding.numberResultView.text =
                    binding.numberInputView.text.toString().isNumber().toString()
            }
        }

        binding.judgeNumberAndLetterButton.setOnClickListener {
            if (binding.numberAndLetterInputView.text.isNullOrBlank()) {
                binding.numberAndLetterResultView.text = false.toString()
            } else {
                binding.numberAndLetterResultView.text =
                    binding.numberAndLetterInputView.text.toString().isLetterAndDigit().toString()
            }
        }

        binding.judgeChineseButton.setOnClickListener {
            if (binding.chineseInputView.text.isNullOrBlank()) {
                binding.chineseCharacterResultView.text = false.toString()
            } else {
                binding.chineseCharacterResultView.text =
                    binding.chineseInputView.text.toString().isChinese().toString()
            }
        }

        binding.judgePhoneButton.setOnClickListener {
            if (binding.phoneInputView.text.isNullOrBlank()) {
                binding.phoneResultView.text = false.toString()
            } else {
                binding.phoneResultView.text =
                    binding.phoneInputView.text.toString().isPhoneNumber().toString()
            }
        }

        binding.judgeEmailButton.setOnClickListener {
            if (binding.emailInputView.text.isNullOrBlank()) {
                binding.emailResultView.text = false.toString()
            } else {
                binding.emailResultView.text =
                    binding.emailInputView.text.toString().isEmail().toString()
            }
        }

        binding.writeFileButton.setOnClickListener {
            for (i in 1..1000) {
                "${System.currentTimeMillis().timestampToCompleteDate()}".writeToFile(logFile)
            }
            binding.writeResultView.text = "文件写入成功，路径：${logFile.absolutePath}"
        }

        binding.showToastButton.setOnClickListener {
            "showToastButton".show(requireContext())
        }
    }

    private fun Int.appendZero(): String {
        return if (this < 10) {
            "0$this"
        } else {
            this.toString()
        }
    }
}