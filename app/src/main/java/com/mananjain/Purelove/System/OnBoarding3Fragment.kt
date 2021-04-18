package com.mananjain.Purelove.System

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.mananjain.Purelove.R

class OnBoarding3Fragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_on_boarding3, container, false)
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val nextbtn : Button = view.findViewById(R.id.next_btn3)
        val prevbtn : Button = view.findViewById(R.id.prev_btn3)
        nextbtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                nextfragment()
            }
        })
        prevbtn.setOnClickListener(object : View.OnClickListener{
            override fun onClick(v: View?) {
                prevfragment()
            }
        })
    }

    private fun nextfragment() {
        fragmentManager!!.beginTransaction().replace(R.id.fl_onboarding, OnBoarding4Fragment()).commit()

    }
    private fun prevfragment() {
        fragmentManager!!.beginTransaction().replace(R.id.fl_onboarding, OnBoarding2Fragment()).commit()

    }
}