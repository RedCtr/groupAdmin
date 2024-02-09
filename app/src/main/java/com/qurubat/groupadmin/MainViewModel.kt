package com.qurubat.groupadmin

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.qurubat.groupadmin.models.Group
import com.qurubat.groupadmin.utils.Mappers
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val firestore: FirebaseFirestore,
    private val storage: FirebaseStorage
) : ViewModel() {

    companion object {
        const val TAG = "MainViewModel"
    }

    private val _groupsData = MutableLiveData<List<Group>>()
    val groupsData: LiveData<List<Group>> = _groupsData

    val isTheDocDeleted = MutableLiveData<Boolean>()
    val isTheDocSaved = MutableLiveData<Boolean>()

    init {
        getUnpublishedGroups()
    }

    fun getUnpublishedGroups() {
        firestore
            .collection("usersGroup")
            .whereEqualTo("isTheGroupActive", false)
            .get()
            .addOnSuccessListener {
                val groups = mutableListOf<Group>()
                for (document in it) {
                    val group = Group(
                        document.getString("groupName") ?: "",
                        document.getString("groupImage") ?: "",
                        document.getString("groupLink") ?: "",
                        document.getString("genre") ?: "",
                        document.getString("countryImage") ?: "",
                        document.getString("countryName") ?: "",
                        document.getString("groupDescription") ?: "",
                        document.getString("groupImagePathName") ?: "",
                    )
                    group.id = document.id
                    groups.add(group)
                }

                _groupsData.value = groups
            }
    }

    fun removeDocAndStorageFromFirebase(docId: String, imagePathName: String) {
        isTheDocDeleted.value = false

        firestore
            .collection("usersGroup")
            .document(docId)
            .delete()
            .addOnSuccessListener {
                // Document deleted successfully
                // Now, delete associated images from Firebase Storage

//                storage
//                    .reference
//                    .child(imagePathName) // ex: /usersGroupImages/image_1695827113260.jpg
//                    .delete()
//                    .addOnSuccessListener {
//                        isTheDocDeleted.value = true
//                    }

                isTheDocDeleted.value = true
            }

    }

    fun saveDocToFirestore(group: Group) {
        isTheDocSaved.value = false

        firestore
            .collection("usersGroup")
            .document(group.id)
            .delete()

        firestore
            .collection("countries")
            .whereEqualTo(
                "countryName",
                getKeyByValue(Mappers.countryNameMapper, group.countryName)
            )
            .get()
            .addOnSuccessListener {
                if (!it.isEmpty) {
                    val myDoc = it.documents[0]

                    myDoc
                        .reference
                        .collection("groups")
                        .add(group)
                        .addOnSuccessListener {
                            isTheDocSaved.value = true
                        }
                }
            }
//        firestore
//            .collection("allGroups")
//            .add(group)
//            .addOnSuccessListener {
//                firestore
//                    .collection("countries")
//                    .whereEqualTo(
//                        "countryName",
//                        getKeyByValue(Mappers.countryNameMapper, group.countryName)
//                    )
//                    .get()
//                    .addOnSuccessListener {
//                        if (!it.isEmpty) {
//                            val myDoc = it.documents[0]
//
//                            myDoc
//                                .reference
//                                .collection("groups")
//                                .add(group)
//                                .addOnSuccessListener {
//                                    isTheDocSaved.value = true
//                                }
//                        }
//                    }
//
//            }
    }

    private fun getKeyByValue(map: Map<String, String>, countryName: String): String {
        for ((key, value) in map) {
            if (value == countryName) {
                return key
            }
        }
        return ""
    }
}