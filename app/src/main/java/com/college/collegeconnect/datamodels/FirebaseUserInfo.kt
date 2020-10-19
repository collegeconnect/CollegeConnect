package com.college.collegeconnect.datamodels

import android.content.Context
import android.util.Log
import com.college.collegeconnect.security.Security
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.MetadataChanges

object FirebaseUserInfo {
    private const val TAG = "UserInfoUpload"

    fun uploadUserInfo(user: User, ctx: Context) {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return
        FirebaseFirestore.getInstance()
                .collection("users")
                .document(firebaseUser.uid)
                .set(user.encrypt(ctx))
                .addOnSuccessListener { Log.d(TAG, "Account Created") }
                .addOnFailureListener { Log.e(TAG, "Uploading user info failed" + it.message) }
    }

    fun getUserInfo(ctx: Context, callback: (User) -> Unit): ListenerRegistration? {
        val firebaseUser = FirebaseAuth.getInstance().currentUser ?: return null
        return FirebaseFirestore.getInstance()
                .collection("users")
                .document(firebaseUser.uid)
                .addSnapshotListener(MetadataChanges.INCLUDE) { document, exception ->
                    if (exception != null) {
                        Log.e(TAG, exception.message ?: exception.code.name)
                    } else {
                        document?.let { callback(it.decrypt(ctx)) }
                    }
                }
    }

    private fun User.encrypt(ctx: Context): User = User(
            Security.encrypt(rollNo, ctx),
            Security.encrypt(email, ctx),
            Security.encrypt(name, ctx),
            Security.encrypt(branch, ctx),
            Security.encrypt(college, ctx)
    )

    private fun DocumentSnapshot.decrypt(ctx: Context): User = User(
            Security.decrypt(getString("rollNo").orEmpty(), ctx),
            Security.decrypt(getString("email").orEmpty(), ctx),
            Security.decrypt(getString("name").orEmpty(), ctx),
            Security.decrypt(getString("branch").orEmpty(), ctx),
            Security.decrypt(getString("college").orEmpty(), ctx)
    )
}