package com.app.QuizzingApp;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * a quick-n-dirty priority queue that only supports heapsort i.e. no enqueue.
 *
 * This queue must be instantiated with a list already.
 */
public class QuestionPriorityQueue {

    ArrayList<Question> qList;

    public QuestionPriorityQueue(ArrayList<Question> questionList) {
        this.qList = new ArrayList<>(questionList);
        heapify(qList);
    }

    /**
     * Sorts a given array of questions by, in decreasing order of importance:
     * Order of ranking:
     *      * 1. if the user missed the question last time (higher priority)
     *      * 2. time the user spent answering the question (higher time = higher priority)
     *      * 3. difficulty of question (higher difficulty = higher priority)
     *
     * @return a sorted version of questions, in order from highest priority to lowest priority (max-heap)
     */
    public ArrayList<Question> heapSort() {
        int arraySize = qList.size();
        ArrayList<Question> temp = qList;

        ArrayList<Question> result = new ArrayList<>(); // we are sorting out-of-place

        // since list is heapified during constructor, all we need to do is dequeue each element
        for (int i = 0; i < arraySize; i++) {
            result.add(dequeue(this.qList));
        }

        this.qList = temp; // preserve the original list
        return result;  // sorted array
    }

    /**
     * Heapifies a given question ArrayList so it can be heap sorted
     * @param questions array we are heapifying
     */
    private void heapify(ArrayList<Question> questions) {
        // empty or 1 item heap is already a heap
        if (questions.isEmpty() || questions.size() == 1)
            return;

        // heapify starting from last internal node
        heapifyHelper((questions.size() - 2) / 2, questions);
    }

    /**
     * Recursive helper method to heapify a given array of questions
     * @param index index we are going to start percolateDown() at
     * @param questions array we are heapifying
     */
    private void heapifyHelper(int index, List<Question> questions) {
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

    /**
     * Removes and returns the next highest priority Question in the given array list
     * @param questions the given array list we are checking for the highest priority element
     * @return the next highest priority Question
     * @throws NoSuchElementException if the list is empty
     */
    public Question dequeue(List<Question> questions) throws NoSuchElementException {
        // if the list is empty, throw a NoSuchElementException
        if (questions.size() == 0) {
            throw new NoSuchElementException("This list is empty");
        }

        Question toRemove = questions.get(0); // instantiate the Question to be removed as the first
                                                // element in our array

        // if there is only one element in the queue, just remove it, no need to percolateDown()
        if (questions.size() == 1) {
            questions.remove(0); // remove first element
        } else {
            int lastNodeIdx = questions.size() - 1;
            // put last node at root
            questions.set(0, questions.get(lastNodeIdx));
            // delete the last node (it's now at root)
            questions.remove(lastNodeIdx);
            // percolate root node down
            percolateDown(0, questions);
        }
        return toRemove; // return the Question that was dequeued
    }

    /**
     * Percolates down a node starting at a given index
     * @param index the node we are percolating down from
     * @param questions the list we are percolating with respect to
     * @throws IndexOutOfBoundsException if given index is OOB for list
     */
    private void percolateDown(int index, List<Question> questions) throws IndexOutOfBoundsException {
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
