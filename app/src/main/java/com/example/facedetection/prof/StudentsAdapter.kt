package com.example.facedetection.prof

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.facedetection.databinding.ItemViewBinding

class StudentsAdapter:RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {
    private var studentList = ArrayList<StudentModel>()
    fun setStudentList(studentList: ArrayList<StudentModel>){
        this.studentList = studentList
        notifyDataSetChanged()
    }
    class StudentsViewHolder(val binding : ItemViewBinding):ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        return StudentsViewHolder(ItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        val name = studentList[position]
        holder.binding.userName.text = name.studentName
    }

    override fun getItemCount(): Int {
        return studentList.size
    }


}