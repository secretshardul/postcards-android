package com.secretshardul.android.postcards

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.ktx.messaging
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import timber.log.Timber

class HomeFragment : Fragment() {
    private val viewModel: HomeViewModel by viewModels()
    val adapter = PostcardAdapter()
    private lateinit var userDataListenerRegistration: ListenerRegistration

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val recyclerView: RecyclerView = view.findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter

        activity?.let { activity ->
            val usersCollection = Firebase.firestore.collection("users")
            val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
            var savedDocumentId = sharedPref.getString(MainActivity.DOCUMENT_ID_KEY, null)

            // Save token to Firestore, if not already done
            CoroutineScope(Dispatchers.IO).launch {
                if (savedDocumentId == null) {
                    try {
                        val token = Firebase.messaging.token.await()
                        Timber.d("Got token: $token")

                        val newDocumentId = usersCollection.document().id
                        Timber.d("Got ID: $newDocumentId")
                        usersCollection.document(newDocumentId).set(
                            hashMapOf(
                                "fcmToken" to token
                            )
                        ).await()

                        sharedPref.edit()
                            .putString(MainActivity.DOCUMENT_ID_KEY, newDocumentId)
                            .apply()

                        savedDocumentId = newDocumentId
                        addUserDataListener(view, usersCollection.document(savedDocumentId!!))

                    } catch (exception: Exception) {
                        Timber.e(exception)
                    }
                } else {
                    addUserDataListener(view, usersCollection.document(savedDocumentId!!))
                }

            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        if(::userDataListenerRegistration.isInitialized) {
            userDataListenerRegistration.remove()
        }
    }

    private fun addUserDataListener(view: View, userDataDocument: DocumentReference) {
        userDataListenerRegistration = userDataDocument.addSnapshotListener { value, error ->
                Timber.d("Got data: ${value?.data}")
                val userData: UserDataModel? = value?.toObject()
                if(userData != null) {
                    Timber.d("Postcards: ${userData.postcards}")
                    adapter.data = userData.postcards
                }
            }
    }

}
