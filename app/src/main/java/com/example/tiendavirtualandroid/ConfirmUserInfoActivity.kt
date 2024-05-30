package com.example.tiendavirtualandroid

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database

class ConfirmUserInfoActivity : AppCompatActivity() {

    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var address: EditText
    private lateinit var city: EditText
    private lateinit var btnConfirmPurchase: Button
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var productList: ArrayList<Producto>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_confirm_user_info)

        auth = Firebase.auth
        database = Firebase.database.reference
        val currentUser = auth.currentUser

        name = findViewById(R.id.username)
        email = findViewById(R.id.email)
        phone = findViewById(R.id.phone)
        address = findViewById(R.id.address)
        city = findViewById(R.id.city)
        btnConfirmPurchase = findViewById(R.id.btnConfirmDelivery)
        productList = intent.getSerializableExtra("productArrayList") as ArrayList<Producto>
        btnConfirmPurchase.setOnClickListener {

            val name = name.text.toString()
            val email = email.text.toString()
            val phone = phone.text.toString()
            val city = city.text.toString()
            val address = address.text.toString()
            if (name.isEmpty() || email.isEmpty() || city.isEmpty() || address.isEmpty()) {
                Toast.makeText(this, "Todos los campos son obligatorios", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            val infoDelivery = InformationDelivery(
                name = name,
                email = email,
                phone = phone,
                address = address,
                city = city
            )
            database.child("deliveryInformation").child(currentUser?.uid!!).setValue(infoDelivery)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Pedido realizado", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, PurchaseCompleateActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "E", Toast.LENGTH_LONG).show()

                    }
                }

            database.child("deliveryInformation").child(currentUser?.uid!!).child("products")
                .setValue(productList)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Toast.makeText(this, "Pedido realizado", Toast.LENGTH_LONG).show()
                        val intent = Intent(this, PurchaseCompleateActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(this, "E", Toast.LENGTH_LONG).show()

                    }
                }
            database.child("shoppingCart").child(currentUser?.uid!!).removeValue()
        }
    }
}