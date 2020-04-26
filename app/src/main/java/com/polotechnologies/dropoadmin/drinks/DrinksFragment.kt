package com.polotechnologies.dropoadmin.drinks

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.polotechnologies.dropoadmin.R
import com.polotechnologies.dropoadmin.dataModels.Product
import com.polotechnologies.dropoadmin.databinding.FragmentDrinksBinding

class DrinksFragment : Fragment() {

    private val REQUEST_IMAGE_GET = 1
    private lateinit var selectedImageUri : Uri

    private lateinit var mBinding: FragmentDrinksBinding
    private lateinit var mDatabase: FirebaseFirestore
    private lateinit var mStorage : FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        mBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_drinks, container, false)
        mDatabase = FirebaseFirestore.getInstance()
        mStorage = FirebaseStorage.getInstance()


        setOnClickListeners()
        return mBinding.root
    }

    private fun setOnClickListeners() {
        mBinding.imageDrink.setOnClickListener {
            selectImage()
        }

        mBinding.btnAddProduct.setOnClickListener {
            if(isUserDetailsValid()){
                uploadProductImage()
            }
        }
    }

    private fun selectImage() {
        val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
            type = "image/*"
        }
        if (intent.resolveActivity(requireActivity().packageManager) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE_GET && resultCode == Activity.RESULT_OK) {

            val fullPhotoUri: Uri = data!!.data!!
            selectedImageUri = fullPhotoUri
            Glide.with(requireContext().applicationContext)
                .load(selectedImageUri)
                .into(mBinding.imageDrink)

        }
    }

    private fun uploadProductImage() {
        val imageName = getImageName()
        val productImageRef = mStorage.reference.
            child("dropo_deliveries/products/wines_and_spirits/${imageName}.jpeg")


        val uploadTask = productImageRef.putFile(selectedImageUri)

        val urlTask = uploadTask.continueWithTask { task ->
            if (!task.isSuccessful) {
                task.exception?.let {
                    throw it
                }
            }
            productImageRef.downloadUrl
        }.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val downloadUri = task.result

                addProduct(downloadUri)
            }
        }
    }

    private fun getImageName(): String {
        val productName = mBinding.etLoginItemName.text.toString().toLowerCase()
        return productName.replace(" ", "_")
    }

    private fun addProduct(downloadUri: Uri?) {
        val productRef = mDatabase.collection("products").document()

        var productCategory  = mBinding.etLoginProductCategory.text.toString()
        if(productCategory == "w") productCategory = "wines_and_spirits"

        val itemCategory  = mBinding.etLoginItemCategory.text.toString()
        val itemName  = mBinding.etLoginItemName.text.toString()
        val itemPrice = mBinding.etLoginItemPrice.text.toString().toInt()
        val itemId = productRef.id

        val product = Product(
            productCategory,
            itemCategory,
            itemId,
            itemName,
            itemPrice,
            downloadUri.toString()
        )

        productRef.set(product).addOnSuccessListener {
            Toast.makeText(requireContext().applicationContext, "$itemName Added Successfully", Toast.LENGTH_SHORT).show()
            clearFields()
        }

    }

    private fun clearFields() {
        mBinding.etLoginProductCategory.setText("")
        mBinding.etLoginItemCategory.setText("")
        mBinding.etLoginItemName.setText("")
        mBinding.etLoginItemPrice.setText("")

    }

    private fun isUserDetailsValid(): Boolean {
        var isDetailsValid  = false

        if (mBinding.etLoginProductCategory.text!!.isEmpty()) {
            mBinding.etLoginProductCategory.error = "Product Category Required"
        }
        if (mBinding.etLoginItemCategory.text!!.isEmpty()) {
            mBinding.etLoginItemCategory.error = "Item Category Required"
        }
        if (mBinding.etLoginItemName.text!!.isEmpty()) {
            mBinding.etLoginItemName.error = "Item Name Required"
        }
        if (mBinding.etLoginItemPrice.text!!.isEmpty()) {
            mBinding.etLoginItemPrice.error = "item Price Required"
        }

        if (mBinding.etLoginProductCategory.text!!.isNotEmpty() &&
            mBinding.etLoginItemCategory.text!!.isNotEmpty() &&
            mBinding.etLoginItemName.text!!.isNotEmpty() &&
            mBinding.etLoginItemPrice.text!!.isNotEmpty()
        ) {

            isDetailsValid = true
        }

        return isDetailsValid

    }

}