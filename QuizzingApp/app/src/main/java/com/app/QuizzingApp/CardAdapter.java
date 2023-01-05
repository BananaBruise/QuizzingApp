package com.app.QuizzingApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.QuizzingApp.databinding.CardBinding;

import java.util.ArrayList;
import java.util.List;

public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHolder>{
    List<Card> cardList;

    public CardAdapter(List<Card> cardList) {
        this.cardList = cardList;
    }

    @NonNull
    @Override
    public CardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardBinding binding = CardBinding.inflate(li);
        return new myViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CardAdapter.myViewHolder holder, int position) {
        Card cardItem = cardList.get(position);
        holder.binding.cardQuestionName.setText(cardItem.getQuestionName());
        holder.binding.cardQuestionDiff.setText(cardItem.getDifficulty());
        holder.binding.answer1.setText(cardItem.getAnswer1());
        holder.binding.answer2.setText(cardItem.getAnswer2());
        holder.binding.answer3.setText(cardItem.getAnswer3());
        holder.binding.answer4.setText(cardItem.getAnswer4());

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
