package com.app.QuizzingApp;

import android.app.Activity;
import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.QuizzingApp.databinding.ActivityAnswererDashboardBinding;
import com.app.QuizzingApp.databinding.CardBinding;
import com.yuyakaido.android.cardstackview.CardStackView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * When we create an adapter with a data set, it "adapts" the data so we can see it in the view.
 * viewHolder holds the view that will be recycled by the recycler view. Each time an activity is
 * created, each activity automatically has its own binding. To create the view holder, you also
 * need to supply the binding for the class the view holder is holding. To actually set the values
 * you can use the binding that was created to directly reference the xml elements.
 */
public class QuestionCardAdapter extends RecyclerView.Adapter<QuestionCardAdapter.myViewHolder> {
    List<Question> cardList;


    public QuestionCardAdapter(List<Question> cardList) {
        this.cardList = cardList; // assign to cardList

    }

    @NonNull
    @Override
    public QuestionCardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardBinding binding = CardBinding.inflate(li);  // inflate viewbinding on the parent (dashboard)
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionCardAdapter.myViewHolder holder, int position) {
        Question cardItem = cardList.get(position);
        holder.binding.progress.setMax(cardList.size());
        holder.binding.progress.setProgress(position + 1);
        holder.binding.cardQuestionNameTV.setText(cardItem.getName());
        holder.binding.cardQuestionDiffTV.setText("difficulty: " + cardItem.getDiff());
        holder.binding.answer1ET.setText(cardItem.getAnswers().get(0).getPrompt());
        holder.binding.answer2ET.setText(cardItem.getAnswers().get(1).getPrompt());
        holder.binding.answer3ET.setText(cardItem.getAnswers().get(2).getPrompt());
        holder.binding.answer4ET.setText(cardItem.getAnswers().get(3).getPrompt());


        // timer

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public static class myViewHolder extends RecyclerView.ViewHolder {
        CardBinding binding;

        public myViewHolder(@NonNull CardBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

}
