package com.mananjain.Purelove.User

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.google.firebase.database.*
import com.mananjain.Purelove.Databases.MatchData
import com.mananjain.Purelove.Databases.sessionManager
import com.mananjain.Purelove.R
import java.util.*

class UserMatchFragment : Fragment() {

    private lateinit var mobileno: String
    private lateinit var builder: AlertDialog.Builder
    private lateinit var dialog: AlertDialog
    private var pmatchleft: Int = 0
    private var nmatchleft: Int = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_user_match, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        try {
            builder = AlertDialog.Builder(context!!)
            builder.setCancelable(false)
            builder.setView(R.layout.progress_bar_layout)
            dialog = builder.create()
            dialog.show()
            lateinit var snapshot: DataSnapshot
            val matchinglayout: RelativeLayout = view.findViewById(R.id.matchingrelativelayout)
            matchinglayout.visibility = View.GONE
            spinnerAdapter()
            val session = sessionManager(context!!)
            mobileno = session.getLoginContact()
            val checkUser =
                FirebaseDatabase.getInstance().getReference("Users").child("Accounts")
                    .child(mobileno)
            checkUser.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshotlocal: DataSnapshot) {
                    snapshot = snapshotlocal
                    ismatchching(snapshot)
                    if (addvalues(snapshot)) {
                        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                        builder.setMessage("You do not have matches").setCancelable(true)
                        builder.setPositiveButton("Buy Now") { _: DialogInterface, _: Int ->
                            val fragmentTransaction: FragmentTransaction =
                                fragmentManager!!.beginTransaction()
                            fragmentTransaction.replace(R.id.FLfragment, UserBuyFragment()).commit()
                        }
                        builder.setNegativeButton("Discard") { _: DialogInterface, _: Int -> }
                        builder.show()
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                }
            })

            val matchbtn: Button = view.findViewById(R.id.matchbtn)
            matchbtn.setOnClickListener {
                if(validatefields()) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                    builder.setMessage("Which match Do you want use?").setCancelable(true)
                    builder.setPositiveButton("Priority") { _: DialogInterface, _: Int ->
                        prioritymatch(snapshot)
                    }
                    builder.setNegativeButton("Normal") { _: DialogInterface, _: Int ->
                        normalmatch(snapshot)
                    }
                    builder.show()
                }
            }

            val cancel_match: Button = view.findViewById(R.id.cancel_match_btn)
            cancel_match.setOnClickListener {
                val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                builder.setTitle("Do you want to Cancel the match?")
                builder.setMessage("You will loose 1 match").setCancelable(true)
                builder.setPositiveButton("Stop matching") { _: DialogInterface, _: Int ->
                    cancelmatch(snapshot)
                }
                builder.setNegativeButton("Discard") { _: DialogInterface, _: Int -> }
                builder.show()
            }

        } catch (e: Exception) {
            Toast.makeText(context!!, "Oops! something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun validatefields(): Boolean {
        val genderspinner: Spinner = view!!.findViewById(R.id.genderspinner)
        val agespinner: Spinner = view!!.findViewById(R.id.agespinner)
        val relationspinner: Spinner = view!!.findViewById(R.id.relationspinner)
        val locationspinner: Spinner = view!!.findViewById(R.id.locationspinner)
        when {
            genderspinner.selectedItem == null -> {
                Toast.makeText(context,"Gender Not Selected",Toast.LENGTH_SHORT).show()
                return false
            }
            agespinner.selectedItem == null -> {
                Toast.makeText(context,"Age Not Selected",Toast.LENGTH_SHORT).show()
                return false
            }
            relationspinner.selectedItem == null -> {
                Toast.makeText(context,"Relation Not Selected",Toast.LENGTH_SHORT).show()
                return false
            }
            locationspinner.selectedItem == null -> {
                Toast.makeText(context,"Location not selected",Toast.LENGTH_SHORT).show()
                return false
            }
            else -> {
                return true
            }
        }
    }

    private fun ismatchching(snapshot: DataSnapshot) {
        try {

            val matchinglayout: RelativeLayout = view!!.findViewById(R.id.matchingrelativelayout)
            val matchlayout: LinearLayout = view!!.findViewById(R.id.matchlinearLayout)
            if (snapshot.child("matchingStatus").value.toString() == "Not Matching") {
                matchinglayout.visibility = View.GONE
                matchlayout.visibility = View.VISIBLE
                spinnerAdapter()
            } else {
                matchlayout.visibility = View.GONE
                addmatchingfields(snapshot)
                matchinglayout.visibility = View.VISIBLE
            }
            dialog.dismiss()

        } catch (e: Exception) {
            Toast.makeText(context!!, "Oops! something went wrong", Toast.LENGTH_SHORT).show()
        }
    }

    private fun addvalues(snapshot: DataSnapshot): Boolean {
        val pmatch: TextView = view!!.findViewById(R.id.prioritymatchtextview)
        val nmatch: TextView = view!!.findViewById(R.id.normalmatchtextview)
        pmatch.text = snapshot.child("priorityMatch").value.toString()
        nmatch.text = snapshot.child("normalMatch").value.toString()
        pmatchleft = snapshot.child("priorityMatch").value.toString().toInt()
        nmatchleft = snapshot.child("normalMatch").value.toString().toInt()
        return ((pmatch.text.toString().toInt() == 0) && (nmatch.text.toString().toInt() == 0))
    }

    private fun spinnerAdapter() {
        val genderspinner: Spinner = view!!.findViewById(R.id.genderspinner)
        val adaptergender: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context!!,
            R.array.gender,
            android.R.layout.simple_spinner_item
        )
        adaptergender.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        genderspinner.adapter = adaptergender
        genderspinner.setSelection(0)

        val agespinner: Spinner = view!!.findViewById(R.id.agespinner)
        val adapterage: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context!!,
            R.array.age,
            android.R.layout.simple_spinner_item
        )
        adapterage.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        agespinner.adapter = adapterage
        agespinner.setSelection(0)

        val relationspinner: Spinner = view!!.findViewById(R.id.relationspinner)
        val adapterrelation: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context!!,
            R.array.relation,
            android.R.layout.simple_spinner_item
        )
        adapterrelation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        relationspinner.adapter = adapterrelation
        relationspinner.setSelection(0)

        val locationspinner: Spinner = view!!.findViewById(R.id.locationspinner)
        val adapterlocation: ArrayAdapter<CharSequence> = ArrayAdapter.createFromResource(
            context!!,
            R.array.location,
            android.R.layout.simple_spinner_item
        )
        adapterlocation.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        locationspinner.adapter = adapterlocation
        locationspinner.setSelection(0)
    }

    private fun addmatchingfields(snapshot: DataSnapshot) {

        val age: TextView = view!!.findViewById(R.id.matchingagetxt)
        val gender: TextView = view!!.findViewById(R.id.matchinggendertxt)
        val location: TextView = view!!.findViewById(R.id.matchinglocationtxt)
        val relation: TextView = view!!.findViewById(R.id.matchingrelationtxt)
        age.text = snapshot.child("rage").value.toString()
        gender.text = snapshot.child("rgender").value.toString()
        if (snapshot.child("rlocation").value.toString() == "Your City") {
            location.text = snapshot.child("city").value.toString()
        } else {
            location.text = snapshot.child("rlocation").value.toString()
        }
        relation.text = snapshot.child("rrelation").value.toString()
    }

    private fun prioritymatch(snapshot: DataSnapshot) {
        if (snapshot.child("priorityMatch").value.toString().toInt() == 0) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            builder.setMessage("You do not have Priority matches").setCancelable(true)
            builder.setPositiveButton("Buy Now") { _: DialogInterface, _: Int ->
                val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.replace(R.id.FLfragment, UserBuyFragment()).commit()
            }
            builder.setNegativeButton("Match with Normal  ") { _: DialogInterface, _: Int ->
                normalmatch(snapshot)
            }
            builder.show()
        } else {
            checkmatching(snapshot, "Priority")
        }
    }

    private fun normalmatch(snapshot: DataSnapshot) {
        if (snapshot.child("normalMatch").value.toString().toInt() == 0) {
            val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
            builder.setMessage("You do not have Normal matches").setCancelable(true)
            builder.setPositiveButton("Buy Now") { _: DialogInterface, _: Int ->
                val fragmentTransaction: FragmentTransaction = fragmentManager!!.beginTransaction()
                fragmentTransaction.replace(R.id.FLfragment, UserBuyFragment()).commit()
            }
            builder.setNegativeButton("Match with Priority  ") { _: DialogInterface, _: Int ->
                prioritymatch(snapshot)
            }
            builder.show()
        } else {
            checkmatching(snapshot, "Normal")
        }
    }

    private fun checkmatching(mydata: DataSnapshot, matching: String) {
        val gender: Spinner = view!!.findViewById(R.id.genderspinner)
        val age: Spinner = view!!.findViewById(R.id.agespinner)
        val relation: Spinner = view!!.findViewById(R.id.relationspinner)
        val location: Spinner = view!!.findViewById(R.id.locationspinner)

        updatematchingstatus(
            matching,
            gender.selectedItem.toString(),
            location.selectedItem.toString(),
            age.selectedItem.toString(),
            relation.selectedItem.toString()
        )

        lateinit var checkUser: DatabaseReference
        val myconnections: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child("Connections")
                .child(mobileno)

        myconnections.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(connections: DataSnapshot) {


                checkUser = if (location.selectedItem.toString() == "Anywhere") {
                    FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                        .child("Priority")
                        .child(location.selectedItem.toString())
                        .child(gender.selectedItem.toString())
                        .child(relation.selectedItem.toString()).child(age.selectedItem.toString())
                } else {
                    FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                        .child("Priority")
                        .child(location.selectedItem.toString())
                        .child(mydata.child("city").value.toString())
                        .child(gender.selectedItem.toString())
                        .child(relation.selectedItem.toString())
                        .child(age.selectedItem.toString())
                }
                checkUser.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshotlocal: DataSnapshot) {
                        for (data in snapshotlocal.children) {
                            if (data.child("rgender").value.toString() == mydata.child("gender").value.toString() && !(connections.hasChild(
                                    data.key.toString()
                                ))
                            ) {
                                val myage = agecalc(mydata.child("dob").value.toString())
                                when {
                                    myage in 18..21 && data.child("rage").value
                                        .toString() == "18–21" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 22..25 && data.child("rage").value
                                        .toString() == "22–25" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 26..29 && data.child("rage").value
                                        .toString() == "26–29" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 30..35 && data.child("rage").value
                                        .toString() == "30–35" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 36..42 && data.child("rage").value
                                        .toString() == "36–42" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 43..50 && data.child("rage").value
                                        .toString() == "43–50" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage > 50 && data.child("rage").value
                                        .toString() == "50+" -> {
                                        matchwith(data)
                                        return
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }
                })



                checkUser = if (location.selectedItem.toString() == "Anywhere") {
                    FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                        .child("Normal")
                        .child(location.selectedItem.toString())
                        .child(gender.selectedItem.toString())
                        .child(relation.selectedItem.toString()).child(age.selectedItem.toString())
                } else {
                    FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                        .child("Normal")
                        .child(location.selectedItem.toString())
                        .child(mydata.child("city").value.toString())
                        .child(gender.selectedItem.toString())
                        .child(relation.selectedItem.toString())
                        .child(age.selectedItem.toString())
                }
                checkUser.addValueEventListener(object : ValueEventListener {
                    override fun onDataChange(snapshotlocal: DataSnapshot) {
                        for (data in snapshotlocal.children) {
                            if (data.child("rgender").value.toString() == mydata.child("gender").value.toString() && !(connections.hasChild(
                                    data.key.toString()
                                ))
                            ) {
                                val myage = agecalc(mydata.child("dob").value.toString())
                                when {
                                    myage in 18..21 && data.child("rage").value
                                        .toString() == "18–21" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 22..25 && data.child("rage").value
                                        .toString() == "22–25" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 26..29 && data.child("rage").value
                                        .toString() == "26–29" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 30..35 && data.child("rage").value
                                        .toString() == "30–35" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 36..42 && data.child("rage").value
                                        .toString() == "36–42" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage in 43..50 && data.child("rage").value
                                        .toString() == "43–50" -> {
                                        matchwith(data)
                                        return
                                    }
                                    myage > 50 && data.child("rage").value
                                        .toString() == "50+" -> {
                                        matchwith(data)
                                        return
                                    }
                                }
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
                    }
                })


                if (matching == "Priority") {
                    if (location.selectedItem.toString() == "Anywhere") {
                        checkUser =
                            FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                                .child("Priority")
                                .child(location.selectedItem.toString())
                                .child(mydata.child("gender").value.toString())
                                .child(relation.selectedItem.toString())
                                .child(getagerange(agecalc(mydata.child("dob").value.toString())))
                        val database =
                            MatchData(
                                gender.selectedItem.toString(),
                                location.selectedItem.toString(),
                                relation.selectedItem.toString(),
                                age.selectedItem.toString(),
                                mobileno,
                                matching
                            )
                        checkUser.child(mobileno).setValue(database)
                        return
                    } else {
                        checkUser =
                            FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                                .child("Priority")
                                .child(location.selectedItem.toString())
                                .child(mydata.child("city").value.toString())
                                .child(mydata.child("gender").value.toString())
                                .child(relation.selectedItem.toString())
                                .child(getagerange(agecalc(mydata.child("dob").value.toString())))
                        val database =
                            MatchData(
                                gender.selectedItem.toString(),
                                location.selectedItem.toString(),
                                relation.selectedItem.toString(),
                                age.selectedItem.toString(),
                                mobileno,
                                matching
                            )
                        checkUser.child(mobileno).setValue(database)
                        return
                    }
                } else {
                    if (location.selectedItem.toString() == "Anywhere") {
                        checkUser =
                            FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                                .child("Normal")
                                .child(location.selectedItem.toString())
                                .child(mydata.child("gender").value.toString())
                                .child(relation.selectedItem.toString())
                                .child(getagerange(agecalc(mydata.child("dob").value.toString())))
                        val database =
                            MatchData(
                                gender.selectedItem.toString(),
                                location.selectedItem.toString(),
                                relation.selectedItem.toString(),
                                age.selectedItem.toString(),
                                mobileno,
                                matching
                            )
                        checkUser.child(mobileno).setValue(database)
                        return
                    } else {
                        checkUser =
                            FirebaseDatabase.getInstance().getReference("Users").child("Matching")
                                .child("Normal")
                                .child(location.selectedItem.toString())
                                .child(mydata.child("city").value.toString())
                                .child(mydata.child("gender").value.toString())
                                .child(relation.selectedItem.toString())
                                .child(getagerange(agecalc(mydata.child("dob").value.toString())))
                        val database =
                            MatchData(
                                gender.selectedItem.toString(),
                                location.selectedItem.toString(),
                                relation.selectedItem.toString(),
                                age.selectedItem.toString(),
                                mobileno,
                                matching
                            )
                        checkUser.child(mobileno).setValue(database)
                        return
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getagerange(age: Int): String {

        lateinit var agerange: String
        when {
            age >= 18 && age <= 21 -> {
                agerange = "18–21"
            }
            age >= 22 && age <= 25 -> {
                agerange = "22–25"
            }
            age >= 26 && age <= 29 -> {
                agerange = "26–29"
            }
            age >= 30 && age <= 35 -> {
                agerange = "30–35"
            }
            age >= 36 && age <= 42 -> {
                agerange = "36–42"
            }
            age >= 43 && age <= 50 -> {
                agerange = "43–50"
            }
            age > 50 -> {
                agerange = "50+"
            }
        }
        return agerange
    }

    private fun agecalc(dob: String): Int {

        var currentyear = Calendar.getInstance().get(Calendar.YEAR)
        var currentmonth = Calendar.getInstance().get(Calendar.MONTH)
        var currentdate = Calendar.getInstance().get(Calendar.DATE)
        val userdate: Int = when {
            dob[2] == '/' -> {
                dob.substring(0, 1)
            }
            else -> {
                dob[0].toString()
            }
        }.toInt()

        val usermonth: Int = when {
            dob[2] == '/' && dob[4] == '/' -> {
                dob[3].toString()
            }
            dob[2] == '/' && dob[5] == '/' -> {
                dob.substring(3, 4)
            }
            dob[1] == '/' && dob[3] == '/' -> {
                dob[2].toString()
            }
            else ->
                dob.substring(2, 3)
        }.toInt()
        val useryear: Int = dob.substring(dob.length - 4).toInt()
        val month = arrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)

        if (userdate > currentdate) {
            currentdate += month[usermonth - 1]
            currentmonth = currentmonth - 1
        }

        if (usermonth > currentmonth) {
            currentyear = currentyear - 1
            currentmonth += 12
        }

        return (currentyear - useryear)

    }

    private fun updatematchingstatus(
        str: String,
        gender: String,
        location: String,
        age: String,
        relation: String
    ) {
        dialog.show()
        val databasereferance: DatabaseReference =
            FirebaseDatabase.getInstance().getReference("Users").child("Accounts").child(mobileno)

        databasereferance.child("matchingStatus").setValue("Matching with $str")
        databasereferance.child("rgender").setValue(gender)
        databasereferance.child("rlocation").setValue(location)
        databasereferance.child("rage").setValue(age)
        databasereferance.child("rrelation").setValue(relation)
        if (str == "Priority") {
            databasereferance.child("priorityMatch").setValue(pmatchleft - 1).toString()
        } else {
            databasereferance.child("normalMatch").setValue(nmatchleft - 1).toString()
        }

        databasereferance.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                ismatchching(snapshot)
                if (addvalues(snapshot)) {
                    val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
                    builder.setMessage("You do not have matches").setCancelable(true)
                    builder.setPositiveButton("Buy Now") { dialogInterface: DialogInterface, i: Int ->
                        val fragmentTransaction: FragmentTransaction =
                            fragmentManager!!.beginTransaction()
                        fragmentTransaction.replace(R.id.FLfragment, UserBuyFragment()).commit()
                    }
                    builder.setNegativeButton("Discard") { dialogInterface: DialogInterface, i: Int -> }
                    builder.show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun cancelmatch(snapshot: DataSnapshot) {
        val match =
            if (snapshot.child("matchingStatus").value.toString() == "Matching with Priority") {
                "Priority"
            } else {
                "Normal"
            }
        var checkUser: DatabaseReference =
            if (snapshot.child("rlocation").value.toString() == "Anywhere") {
                FirebaseDatabase.getInstance().getReference("Users").child("Matching").child(match)
                    .child(snapshot.child("rlocation").value.toString())
                    .child(snapshot.child("gender").value.toString())
                    .child(snapshot.child("rrelation").value.toString())
                    .child(getagerange(agecalc(snapshot.child("dob").value.toString())))
                    .child(snapshot.child("phone").value.toString())
            } else {
                FirebaseDatabase.getInstance().getReference("Users").child("Matching").child(match)
                    .child(snapshot.child("rlocation").value.toString())
                    .child(snapshot.child("city").value.toString())
                    .child(snapshot.child("gender").value.toString())
                    .child(snapshot.child("rrelation").value.toString())
                    .child(getagerange(agecalc(snapshot.child("dob").value.toString())))
                    .child(snapshot.child("phone").value.toString())
            }
        checkUser.setValue(null)
        checkUser =
            FirebaseDatabase.getInstance().getReference("Users").child("Accounts")
                .child(snapshot.child("phone").value.toString())
        checkUser.child("matchingStatus").setValue("Not Matching")
        checkUser.child("rgender").setValue(null)
        checkUser.child("rlocation").setValue(null)
        checkUser.child("rrelation").setValue(null)
        checkUser.child("rage").setValue(null)
        checkUser.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshotnew: DataSnapshot) {
                ismatchching(snapshotnew)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

    private fun matchwith(data: DataSnapshot) {

        val key:String = FirebaseDatabase.getInstance().reference.child("Users").child("Chat").push().key.toString()

        var databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child("Connections")
                .child(mobileno).child(data.key.toString()).child("ChatId")
        databaseReference.setValue(key)
        databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child("Connections")
                .child(data.key.toString()).child(mobileno).child("ChatId")
        databaseReference.setValue(key)
        removeMatchingStatus(data.child("mobileno").value.toString())
        removeMatchingStatus(mobileno)
    }

    private fun removeMatchingStatus(number: String) {
        var databaseReference =
            FirebaseDatabase.getInstance().reference.child("Users").child("Accounts").child(number)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                cancelmatch(snapshot)
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, error.message, Toast.LENGTH_SHORT).show()
            }
        })

    }

}