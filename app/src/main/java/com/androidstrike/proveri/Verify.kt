package com.androidstrike.proveri

import android.app.Dialog
import android.content.Context
import android.os.Build
import android.os.Bundle
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.androidstrike.proveri.data.ChecksViewModel
import com.androidstrike.proveri.data.Product
import com.androidstrike.proveri.database.DBModel
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_verify.*
import kotlinx.android.synthetic.main.fragment_verify.view.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext


class Verify : Fragment() {

    private val productsCollectionRef = Firebase.firestore.collection("products")
    private lateinit var mChecksViewModel: ChecksViewModel
    lateinit var capturedImageView: ImageView

    val REQUEST_CODE = 200



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_verify, container, false)

        mChecksViewModel = ViewModelProvider(this).get(ChecksViewModel::class.java)

        capturedImageView = view.iv_captured_image

        view.btnVerify.setOnClickListener {
            retrievePersons()
        }
        return view
    }



    private fun retrievePersons() = CoroutineScope(Dispatchers.IO).launch {

        val productCode = et_product_code.text.toString().trim()
        et_product_code.setText(" ")
        try {
            val querySnapshot = productsCollectionRef
                .whereEqualTo("batch_no", productCode)
                .get().await()

            if (querySnapshot.isEmpty) {
                Log.d("EQUA", "retrievePersons: retrieved but empty")
                withContext(Dispatchers.Main) {
                    val vibrator: Vibrator? =
                        requireContext().getSystemService(Context.VIBRATOR_SERVICE) as Vibrator?
                    val vibrationEffect: VibrationEffect
//                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION)

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        vibrationEffect =
                            VibrationEffect.createPredefined(VibrationEffect.EFFECT_HEAVY_CLICK)
                        vibrator?.cancel()

                        vibrator?.vibrate(vibrationEffect)
                    } else if (Build.VERSION.SDK_INT >= 26) {
                        vibrator?.vibrate(
                            VibrationEffect.createOneShot(
                                700,
                                VibrationEffect.DEFAULT_AMPLITUDE
                            )
                        )
                    } else {
                        vibrator?.vibrate(700)
                    }
                    showNotValid(productCode)
                }
            } else {
                Log.d("EQUA", "retrievePersons: retrieved and good")
                for (document in querySnapshot) {
                    Log.d("EQUA", "retrievePersons: $document")
                    val product = document.toObject(Product::class.java)
                    Log.d("EQUA", "retrievePersons: written to object...i guess")
//                val prodId = productsCollectionRef.document(document.id).toString()

//
//                    data?.forEach {
//                    Log.d("EQUA", "retrievePersons: $it")
//                }
                    withContext(Dispatchers.Main) {
                        showVerification(product, document)
                    }
                    //todo ...complete code here to implement recycler view
                }
            }
//            Toast.makeText(context, "Good!", Toast.LENGTH_SHORT).show()
        } catch (e: Exception) {
            withContext(Dispatchers.Main) {
                Toast.makeText(context, e.message, Toast.LENGTH_SHORT).show()
                Log.d("ERROR", "retrievePersons: ${e.message}")
            }
        }
    }


    private fun showNotValid(productCode: String) {
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        Log.d("EQUA", "showVerification: arrived here too")


        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_custom_design)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_product_invalid)

//        Log.d("EQUA", "showVerification: arrived here too")

        Log.d("EQUA", "showVerification: ${productCode.toString()}")

        val invalidTxt = dialog.findViewById<TextView>(R.id.txt_not_valid)
        val btnOkay = dialog.findViewById<Button>(R.id.btn_not_okay)
        val ivSaved = dialog.findViewById<ImageView>(R.id.iv_save_invalid)
        val ivProd = dialog.findViewById<ImageView>(R.id.prod_img_invalid)
        val etProdReview = dialog.findViewById<EditText>(R.id.et_product_review)



        ivProd.setImageResource(R.drawable.ic_baseline_cancel_24)
        invalidTxt.append("The Product with\n\n batch number: $productCode\n\n is NOT valid!!")

        val batchNumberDB = productCode.toString()
        val productNameDB = "N/A"
        val mfgNameDB = "N/A"
        val prodDateDB = "N/A"
        val expDateDB = "N/A"
        val review = etProdReview.text.toString()
        val isValid = false

        ivSaved.setOnClickListener {

            insertCheckToDB(batchNumberDB,productNameDB,mfgNameDB,prodDateDB,expDateDB,review,isValid)
            ivSaved.setImageResource(R.drawable.ic_bookmark_added_24)
            findNavController().navigate(R.id.action_verify_to_history)
            dialog.dismiss()
        }

        btnOkay.setOnClickListener {
            insertCheckToDB(batchNumberDB,productNameDB,mfgNameDB,prodDateDB,expDateDB,review,isValid)

            findNavController().navigate(R.id.action_verify_to_history)

            dialog.dismiss()
            Toast.makeText(context, "Not Valid!!", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    private fun showVerification(product: Product, document: QueryDocumentSnapshot) {

//        val product = Product()
        Log.d("EQUA", "showVerification: reached here")
        val dialog = Dialog(requireActivity())
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)

        Log.d("EQUA", "showVerification: arrived here too")


        dialog.window?.setBackgroundDrawableResource(R.drawable.dialog_custom_design)
        dialog.setCancelable(false)
        dialog.setContentView(R.layout.custom_product_detail)

//        Log.d("EQUA", "showVerification: arrived here too")

        Log.d("EQUA", "showVerification: ${product.prod_name.toString()}")

        val productName = dialog.findViewById<TextView>(R.id.product_name)
        val mfgName = dialog.findViewById<TextView>(R.id.mfg_name)
        val prodDate = dialog.findViewById<TextView>(R.id.prod_date)
        val expDate = dialog.findViewById<TextView>(R.id.exp_date)
        val batchNo = dialog.findViewById<TextView>(R.id.btch_no)
        val btnOkay = dialog.findViewById<Button>(R.id.btn_okay)
        val ivSaved = dialog.findViewById<ImageView>(R.id.iv_save)
        val ivProd = dialog.findViewById<ImageView>(R.id.prod_img)
        val etProductReview = dialog.findViewById<EditText>(R.id.et_valid_product_review)


        productName.text = product.prod_name.toString()
        mfgName.text = product.mfg_name.toString()
        prodDate.text = product.prod_date.toString()
        expDate.text = product.exp_date.toString()
        batchNo.text = product.batch_no.toString()

        val batchNumberDB = product.batch_no.toString()
        val productNameDB = product.prod_name.toString()
        val mfgNameDB = product.mfg_name.toString()
        val prodDateDB = product.prod_date.toString()
        val expDateDB = product.exp_date.toString()
        val review = etProductReview.text.toString().trim()
        val isValid = true

        ivProd.setImageResource(R.drawable.ic_baseline_verified_24)

        ivSaved.setOnClickListener {
            insertCheckToDB(
                batchNumberDB,
                productNameDB,
                mfgNameDB,
                prodDateDB,
                expDateDB,
                review,
                isValid
            )
            ivSaved.setImageResource(R.drawable.ic_bookmark_added_24)
            findNavController().navigate(R.id.action_verify_to_history)
            dialog.dismiss()
        }

        btnOkay.setOnClickListener {
            insertCheckToDB(
                batchNumberDB,
                productNameDB,
                mfgNameDB,
                prodDateDB,
                expDateDB,
                review,
                isValid
            )
            findNavController().navigate(R.id.action_verify_to_history)
            dialog.dismiss()
            Toast.makeText(context, "Valid!!", Toast.LENGTH_SHORT).show()
        }
        dialog.show()
    }

    private fun insertCheckToDB(
        batchNumberDB: String,
        productNameDB: String,
        mfgNameDB: String,
        prodDateDB: String,
        expDateDB: String,
        review: String,
        isValid: Boolean
    ) {
        val check = DBModel(batchNumberDB,productNameDB,mfgNameDB,prodDateDB,expDateDB,review,isValid)
        mChecksViewModel.insertCheck(check)
        Toast.makeText(requireContext(), "Saved!", Toast.LENGTH_SHORT).show()
    }


}