package com.qurubat.groupadmin

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.qurubat.groupadmin.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), ButtonViewClicked {

    private lateinit var viewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding =
            ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this)[MainViewModel::class.java]

        val groupAdapter = GroupAdapter(this)
        binding.groupRecyclerview.apply {
            adapter = groupAdapter
            setHasFixedSize(true)
        }

        viewModel.groupsData.observe(this) {
            binding.emptyView.visibility = if (it.isNullOrEmpty()) View.VISIBLE else View.GONE
            binding.loading.visibility = View.GONE
            groupAdapter.submitList(it)
        }

        viewModel.isTheDocSaved.observe(this) {
            binding.loading.visibility = if (it) View.GONE else View.VISIBLE
            binding.loadingShimmer.visibility = if (it) View.GONE else View.VISIBLE
            if (it) {
                showSnackBar("تم حفظ المجموعة بنجاح", false)
                viewModel.getUnpublishedGroups()
            }
        }

        viewModel.isTheDocDeleted.observe(this) {
            binding.loading.visibility = if (it) View.GONE else View.VISIBLE
            binding.loadingShimmer.visibility = if (it) View.GONE else View.VISIBLE

            if (it) {
                showSnackBar("تم حذف المجموعة بنجاح", true)
                viewModel.getUnpublishedGroups()
            }

        }


    }

    override fun saveToFireStore(position: Int) {
        val group = viewModel.groupsData.value!![position]
        viewModel.saveDocToFirestore(group)

    }

    override fun removeFromFireStore(position: Int) {
        val group = viewModel.groupsData.value!![position]
        viewModel.removeDocAndStorageFromFirebase(group.id, group.groupImagePathName)


    }

    private fun showSnackBar(message: String, isRemove: Boolean) {
        val snackbar = Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT)
        val color =
            if (isRemove) resources.getColor(R.color.gnt_red) else resources.getColor(R.color.green)

        snackbar
            .setBackgroundTint(color)
            .show()
    }
}