package com.app.QuizzingApp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.QuizzingApp.databinding.CardBinding;

import java.util.List;

/**
 * When we create an adapter with a data set, it "adapts" the data so we can see it in the view.
 * viewHolder holds the view that will be recycled by the recycler view. Each time an activity is
 * created, each activity automatically has its own binding. To create the view holder, you also
 * need to supply the binding for the class the view holder is holding. To actually set the values
 * you can use the binding that was created to directly reference the xml elements.
 */
public class QuestionCardAdapter extends RecyclerView.Adapter<QuestionCardAdapter.myViewHolder>{
    List<Question> cardList;

    public QuestionCardAdapter(List<Question> cardList) {
        this.cardList = cardList;
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
//    @Override
//    public Course dequeue() throws NoSuchElementException {
//        // if the queue is empty, throw a NoSuchElementException
//        if (this.size == 0) {
//            throw new NoSuchElementException("This CourseQueue is empty");
//        }
//
//        Course toRemove = this.queue[0]; // instantiate the course to be removed as the first element in
//        // our array
//
//        // if there is only one element in the queue, just remove it, no need to percolateDown()
//        if (this.size == 1) {
//            this.queue[0] = null; // remove first element by setting first index to 0
//            this.size--; // decrement size
//        } else {
//            // put last node at root
//            this.queue[0] = this.queue[size - 1];
//            // delete the last node (it's now at root)
//            this.queue[size - 1] = null;
//            // decrement size
//            this.size--;
//            // percolate root node down
//            this.percolateDown(0);
//        }
//        return toRemove; // return the Course that was dequeued
//    }
//    protected void percolateDown(int index) throws IndexOutOfBoundsException {
//        if (index < 0 || index > size - 1) {
//            throw new IndexOutOfBoundsException("index is out of bounds");
//        } else {
//            // first, we want a reference to the node we are percolating down
//            Course percolatingDown = this.queue[index];
//            // we want to know where the left child is (if there is one) for the node at the given index
//            int indexLeftChild = 2 * index + 1; // this will be at 2n + 1
//
//            // we want to make sure that there is a left child (i.e. it is within the bounds of our array)
//            while (indexLeftChild < this.size) {
//                // now, we need to find the max value between the current node and its children
//                // we can start by assuming current node is the max value
//                Course max = percolatingDown;
//                // we also want to maintain the index of the max, which we'll start off at -1
//                int maxIndex = -1;
//                // loop through the current node's children, reassigning max if we find a greater value
//                for (int i = 0; i < 2 && indexLeftChild + i < this.size; i++) {
//                    // if the element at the current index is larger priority than our max, reset max
//                    if (this.queue[indexLeftChild + i].compareTo(max) > 0) {
//                        max = this.queue[indexLeftChild + i];
//                        maxIndex = indexLeftChild + i; // keep track of where current max is
//                    }
//                }
//                // if we WANT to percolate further down, then max must have been reset above (meaning
//                // original max was in the wrong spot). if this is not the case, we want to stop
//                if (max == percolatingDown) {
//                    return; // stop, since the node we were percolating down is larger than the children
//                    // don't swap duplicates
//                } else {
//                    // if max was reset, then max should go "above" the node we are percolating down
//                    // i.e. percolatingDown and max should switch places
//                    Course temp = this.queue[index];
//                    this.queue[index] = this.queue[maxIndex];
//                    this.queue[maxIndex] = temp;
//
//                    // now, the index of the node we are percolating is where maxIndex was
//                    index = maxIndex;
//                    indexLeftChild = 2 * index + 1; // now, determine the index of the left child of our
//                    // ONCE percolated node
//                }
//            }
//        }
//    }
}
