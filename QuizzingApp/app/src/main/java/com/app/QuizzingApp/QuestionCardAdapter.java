package com.app.QuizzingApp;

import android.content.Context;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.QuizzingApp.databinding.CardBinding;

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

    // timer
    long totalSeconds = 30;
    long intervalSeconds = 1;
    SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("mm:ss");
    CountDownTimer cut;


    public QuestionCardAdapter(List<Question> cardList) {
        // heapsort incoming cardlist and assign to CardList
        this.cardList = cardList;
        // TODO - enable for heapsort
        //this.cardList = heapSort(cardList); // assign to cardList
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


        cut = new CountDownTimer(totalSeconds * 1000, intervalSeconds * 1000) {

            public void onTick(long millisUntilFinished) {
                holder.binding.timerTV.setText(mSimpleDateFormat.format((totalSeconds * 1000 - millisUntilFinished) / 1000));
            }

            public void onFinish() {
                holder.binding.timerTV.setText(mSimpleDateFormat.format(0));

            }

        };
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

    // HEAP CODE (dequeue() and percolateDown())
    private ArrayList<Question> heapSort(List<Question> questions) {
        // heapify
        heapify(questions);
        // sort
        ArrayList<Question> result = new ArrayList<>(questions.size()); // copy
        for (int i = 0; i < result.size(); i++) {
            result.add(dequeue(questions));
        }

        return result;
    }

    public void heapify(List<Question> questions) {
        // empty or 1 item heap is already a heap
        if (questions.isEmpty() || questions.size() == 1)
            return;

        // heapify starting from last internal node
        this.heapifyHelper((questions.size() - 2) / 2, questions);
    }

    protected void heapifyHelper(int index, List<Question> questions) {
        // internal node index is valid all the way back to root
        if (index >= 0) {
            // percolate down to make heap subtree valid
            percolateDown(index, questions);
            // heapify the previous internal node
            heapifyHelper(index - 1, questions);
        } else {
            return; // index less than root (0); stops heapify
        }
    }


    public Question dequeue(List<Question> questions) throws NoSuchElementException {
        // if the queue is empty, throw a NoSuchElementException
        if (questions.size() == 0) {
            throw new NoSuchElementException("This Queue is empty");
        }

        Question toRemove = questions.get(0); // instantiate the course to be removed as the first element in out array

        // if there is only one element in the queue, just remove it, no need to percolateDown()
        if (questions.size() == 1) {
            questions.remove(0); // remove first element by setting first index to
        } else {
            int lastNodeIdx = questions.size() - 1;
            // put last node at root
            questions.set(0, questions.get(lastNodeIdx));
            // delete the last node (it's now at root)
            questions.remove(lastNodeIdx);
            // percolate root node down
            this.percolateDown(0, questions);
        }
        return toRemove; // return the Course that was dequeued
    }

    protected void percolateDown(int index, List<Question> questions) throws IndexOutOfBoundsException {
        if (index < 0 || index > questions.size() - 1) {
            throw new IndexOutOfBoundsException("index is out of bounds");
        } else {
            // first, we want a reference to the node we are percolating down
            Question percolatingDown = questions.get(index);
            // we want to know where the left child is (if there is one) for the node at the given index
            int indexLeftChild = 2 * index + 1; // this will be at 2n + 1

            // we want to make sure that there is a left child (i.e. it is within the bounds of our array)
            while (indexLeftChild < questions.size()) {
                // now, we need to find the max value between the current node and its children
                // we can start by assuming current node is the max value
                Question max = percolatingDown;
                // we also want to maintain the index of the max, which we'll start off at -1
                int maxIndex = -1;
                // loop through the current node's children, reassigning max if we find a greater value
                for (int i = 0; i < 2 && indexLeftChild + i < questions.size(); i++) {
                    // if the element at the current index is larger priority than our max, reset max
                    if (questions.get(indexLeftChild + i).compareTo(max) > 0) {
                        max = questions.get(indexLeftChild + i);
                        maxIndex = indexLeftChild + i; // keep track of where current max is
                    }
                }
                // if we WANT to percolate further down, then max must have been reset above (meaning
                // original max was in the wrong spot). if this is not the case, we want to stop
                if (max == percolatingDown) {
                    return; // stop, since the node we were percolating down is larger than the children
                    // don't swap duplicates
                } else {
                    // if max was reset, then max should go "above" the node we are percolating down
                    // i.e. percolatingDown and max should switch places
                    Question temp = questions.get(index);
                    questions.set(index, questions.get(maxIndex));
                    questions.set(maxIndex, temp);

                    // now, the index of the node we are percolating is where maxIndex was
                    index = maxIndex;
                    indexLeftChild = 2 * index + 1; // now, determine the index of the left child of our
                    // ONCE percolated node
                }
            }
        }
    }
}
