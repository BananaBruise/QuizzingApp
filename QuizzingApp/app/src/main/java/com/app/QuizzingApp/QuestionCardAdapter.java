package com.app.QuizzingApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.QuizzingApp.databinding.CardBinding;

import java.util.List;

/**
 * The adapter's job is to bind data to a view (onCreateViewHolder -> onBindViewHolder).
 * viewHolder holds the view that will be recycled by the recycler view. Each time an activity is
 * created, each activity automatically has its own binding. To create the view holder, you also
 * need to supply the binding for the class the view holder is holding. To actually set the values
 * you can use the binding that was created to directly reference the xml elements.
 */
public class QuestionCardAdapter extends RecyclerView.Adapter<QuestionCardAdapter.myViewHolder> {
    List<Question> cardList;    // list of Questions we are displaying


    /**
     * Constructor for our adapter
     * @param cardList the list of Questions we are displaying in our card stack
     */
    public QuestionCardAdapter(List<Question> cardList) {
        this.cardList = cardList; // assign to cardList

    }

    /**
     * Creates a viewHolder object for each card
     * @param parent parent view (AnswererDashboardActivity)
     * @param viewType
     * @return a viewHolder object for each card
     */
    @NonNull
    @Override
    public QuestionCardAdapter.myViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater li = (LayoutInflater) parent.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        CardBinding binding = CardBinding.inflate(li);  // inflate viewbinding on the parent (dashboard)
        return new myViewHolder(binding);
    }

    /**
     * Binds data to view
     * @param holder viewHolder corresponding to each card
     * @param position where we are in our list of cards
     */
    @Override
    public void onBindViewHolder(@NonNull QuestionCardAdapter.myViewHolder holder, int position) {
        // set all UI elements accordingly
        Question cardItem = cardList.get(position);
        holder.binding.progress.setMax(cardList.size());
        holder.binding.progress.setProgress(position + 1);
        holder.binding.cardQuestionNameTV.setText(cardItem.getName());
        holder.binding.cardQuestionDiffTV.setText("difficulty: " + cardItem.getDiff());
        holder.binding.answer1ET.setText(cardItem.getAnswers().get(0).getPrompt());
        holder.binding.answer2ET.setText(cardItem.getAnswers().get(1).getPrompt());
        holder.binding.answer3ET.setText(cardItem.getAnswers().get(2).getPrompt());
        holder.binding.answer4ET.setText(cardItem.getAnswers().get(3).getPrompt());


    }

    /**
     * Returns the number of elements in our list
     * @return the number of elements in our list
     */
    @Override
    public int getItemCount() {
        return cardList.size();
    }

    /**
     * ViewHolder class, used to create a viewHolder for each card (incorporates binding
     * so we can easily reference UI elements defined in xml)
     */
    public static class myViewHolder extends RecyclerView.ViewHolder {
        CardBinding binding;

        public myViewHolder(@NonNull CardBinding binding) {
            super(binding.getRoot());
            this.binding = binding; // note: ViewHolder incorporates binding
        }
    }

}
