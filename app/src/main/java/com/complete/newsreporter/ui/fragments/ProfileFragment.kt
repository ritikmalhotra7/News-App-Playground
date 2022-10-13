package com.complete.newsreporter.ui.fragments

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import coil.load
import com.complete.newsreporter.R
import com.complete.newsreporter.model.User
import com.complete.newsreporter.utils.Constants.dbReferenceUsers
import com.complete.newsreporter.utils.Constants.firebaseAuth
import com.complete.newsreporter.utils.Constants.storageReference
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_profile.*
import java.io.File

@AndroidEntryPoint
class ProfileFragment : Fragment(R.layout.fragment_profile) {


    private val REQUEST_CODE: Int = 100

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar.visibility = View.VISIBLE
        val localFile = File.createTempFile("temp","jpg")
        storageReference.reference.child(firebaseAuth.currentUser!!.uid.toString()).getFile(localFile).addOnSuccessListener {
            val bitMap = BitmapFactory.decodeFile(localFile.absolutePath)
            imageprofile.load(bitMap)
            progressBar.visibility = View.GONE
        }
        dbReferenceUsers.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for(snap in snapshot.children){
                    val user = snap.getValue(User::class.java)
                    Log.d("taget",user.toString())
                    if(user!!.uid == firebaseAuth.currentUser!!.uid){
                        nameprofile.text = user!!.name
                        detailprofile.text = user.phone?:user.email
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireContext(),"this",Toast.LENGTH_SHORT).show()
                Log.d("taget",error.toString())
            }

        })
        fabprofile.setOnClickListener {
            progressBar.visibility = View.VISIBLE
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE) {
            storageReference.reference.child(firebaseAuth.currentUser!!.uid.toString()).putFile(data?.data!!)
                .addOnCompleteListener {

                    val localFile = File.createTempFile("temp","jpg")
                    storageReference.reference.child(firebaseAuth.currentUser!!.uid.toString()).getFile(localFile).addOnSuccessListener {
                        val bitMap = BitmapFactory.decodeFile(localFile.absolutePath)
                        imageprofile.load(bitMap)
                    } }
        }else{
            progressBar.visibility = View.GONE
        }
    }

}