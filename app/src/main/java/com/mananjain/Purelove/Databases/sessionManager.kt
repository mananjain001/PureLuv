package com.mananjain.Purelove.Databases

import android.content.Context

class sessionManager(context:Context) {
    val PREFERANCE_NAME = "SharedPreferance"
    val PREFERANCE_CONTACT = "LOGIN_CONTACT"
    val PREFERANCE_PASSWORD = "LOGIN_PASSWORD"
    val REMEMBER_ME = false
    val LOGIN_STATUS = false
    val ON_BOARDING_STATUS = 100


    val preference = context.getSharedPreferences(PREFERANCE_NAME,Context.MODE_PRIVATE)!!

    fun getLoginContact():String{
        return preference.getString(PREFERANCE_CONTACT,PREFERANCE_CONTACT).toString()
    }
    fun getLoginPassword():String{
        return preference.getString(PREFERANCE_PASSWORD,PREFERANCE_PASSWORD).toString()
    }
    fun setLoginContact(str:String){
        val editor = preference.edit()
        editor.putString(PREFERANCE_CONTACT,str)
        editor.apply()
    }
    fun setLoginPassword(str:String){
        val editor = preference.edit()
        editor.putString(PREFERANCE_PASSWORD,str)
        editor.apply()
    }
    fun removeLoginContact(){
        val editor = preference.edit()
        editor.putString(PREFERANCE_CONTACT,"LOGIN_CONTACT")
        editor.apply()
    }
    fun removeLoginPassword(){
        val editor = preference.edit()
        editor.putString(PREFERANCE_CONTACT,"LOGIN_PASSWORD")
        editor.apply()
    }
    fun getRemembermeStatus():Boolean{
        return preference.getBoolean(REMEMBER_ME.toString(),REMEMBER_ME)
    }
    fun setRemembermeStatus(){
        val editor = preference.edit()
        editor.putBoolean(REMEMBER_ME.toString(),true)
        editor.apply()
    }
    fun removeLoginStatus(){
        val editor = preference.edit()
        editor.putBoolean(LOGIN_STATUS.toString(),false)
        editor.apply()
    }
    fun getLoginStatus():Boolean{
        return preference.getBoolean(LOGIN_STATUS.toString(),LOGIN_STATUS)
    }
    fun setLoginStatus(){
        val editor = preference.edit()
        editor.putBoolean(LOGIN_STATUS.toString(),true)
        editor.apply()
    }
    fun setOnBoardingStatus(){
        val editor = preference.edit()
        editor.putInt(ON_BOARDING_STATUS.toString(),404)
        editor.apply()
    }
    fun getOnBoardingStatus():Int{
        return preference.getInt(ON_BOARDING_STATUS.toString(),ON_BOARDING_STATUS)
    }

}