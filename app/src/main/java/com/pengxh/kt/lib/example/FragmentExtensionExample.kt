package com.pengxh.kt.lib.example

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.pengxh.kt.lib.databinding.FragmentExampleFragmentExtensionBinding
import com.pengxh.kt.lite.extensions.bindView

class FragmentExtensionExample : Fragment() {

    //绑定布局，替换setContentView，省去findViewById操作
    private val binding by bindView<FragmentExampleFragmentExtensionBinding>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.rootView.setBackgroundColor(Color.BLUE)
        return binding.root
    }
}