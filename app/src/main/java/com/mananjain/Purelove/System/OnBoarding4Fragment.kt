package com.mananjain.Purelove.System

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mananjain.Purelove.R

class OnBoarding4Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_boarding4, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val prevbtn : Button = view.findViewById(R.id.prev_btn4)
        prevbtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                prevfragment()
            }
        })
    }
    private fun prevfragment() {
        fragmentManager!!.beginTransaction().replace(R.id.fl_onboarding, OnBoarding3Fragment()).commit()

    }
}