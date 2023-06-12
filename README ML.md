# Bangkit capstone project - ML (Recommender System Model)


### Summary
StarWord is a model to classify the **relevant** and **sentiment** of user's input to get the relevant or irrelevant and including negative, positive, or advice sentiment. By using this model, user feedback is used to improve further as metrics from relevant and sentiment that feedback is needed.

This repository mainly consists of 2 files:
1. `tf_native_modelling_relevant.ipynb` that to check and process the 0 (irrelevant) or 1 (relevant), if the label is relevant then will be processed into sentiment
3. `tf_native_modelling_sentiment.ipynb` that for further processing of the relevant labels to process included in sentiment 0 (negative), 1 (positive), and 2 (advice)

## 1. tf_native_modelling_relevant.ipynb
### A. How To Make The Model?
1. Load the dataset from the dataset folder from `dataset/dataset_full_preprocessed.csv` using pandas
2. Check the number of missing values in each column by using `df.isna().sum()`
3. Because there is no missing value, data remains as it is

### B. Data Preparation for Modelling
1. Count the label 0 (irrelevant) and 1 (relevant) in the column **relevant**
2. Split and shuffle the feature and the label (X and y) into 2 different usages. In this scope, the feature is divided as the train set and test set with the proportion of `90% train set and the 10% as the test set`
3. Define `vocab_size, embedding_dim, max_length, trunc_type, padding_type, and oov_tok` that will be used for modeling

### C. Modelling Process
1. Define the model. In this scope, the model uses `Embedding, GlobalAveragePooling1D, and Dropout` with 3 layers of Keras which is a Dense layer. The activation function used in the model is `ReLu` for the first 2 layers. The last activation function is `sigmoid` to do binary-class classification
2. Define the `callbacks` that are going to be used, callback is used to get the notification that the accuracy has reached the threshold of the accuracy (in this scope is 0.91). This callback is used to flag the **good** model and differ it from the others
3. Compile the model. The loss function used in this model is the `binary_crossentropy` (this loss is optimized for binary-class classification). To fit this model better, `Adaptive Moment Estimation (Adam)` is used for optimization and `accuracy` as the metric
5. Fit the model with the `epoch of 50` and test set as validation data, for more detail can be seen in the following picture

      <img src="https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/blob/ml-development/image/model_relevant.png" width="400" />

### D. Evaluation
1. Plot the accuracy of the model in the epoch. The plot is for the accuracy of both the train set and the test set. After plotting, check the graph. Does the accuracy continue to increase or keep decreasing and if it continues to increase, then the model is well-fit 
               
      <img src="https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/blob/ml-development/image/acc_relevant.png" width="375" />

2. Plot the loss of the model in the epoch. As the opposite of accuracy, the plot is for loss of both the train set and the test set should decrease in each epoch. After plotting, check the graph. Does the loss continue to decrease or keep increasing and if it continues to decrease, then the model is well-fit                         
               <img src="https://github.com/Yousei-kun/StarWord-NLP-FeedbackValidator/blob/ml-development/image/loss_relevant.png" width="375" />
               
3. Table of relevant in accuracy and loss

   | Accuracy | Val_accuracy | Loss   | Val_loss | Model    |
   | -------- | ------------ | ------ | -------- | -------- |
   | 0.9709   | 0.9007       | 0.0926 | 0.3206   | Good fit |
   
