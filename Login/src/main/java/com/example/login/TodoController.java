package com.example.login;


import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.time.LocalDate;
import java.util.List;

public class TodoController {

    @FXML
    private TextField taskInput;

    @FXML
    private DatePicker datePicker;

    @FXML
    private Button addButton;

    @FXML
    private ListView<Task> taskList;

    private User user;

    @FXML
    public void initialize() {
        taskList.setCellFactory(new Callback<>() {
            @Override
            public ListCell<Task> call(ListView<Task> param) {
                return new TaskListCell();
            }
        });
        addButton.setOnAction(event -> handleAddTask());
    }

    public void setUser(User user) {
        this.user = user;
        loadTasks();
    }

    private void loadTasks() {
        if (user == null) {
            throw new IllegalStateException("User is not set");
        }
        List<Task> tasks = TaskDAO.getTasks(user.getId());
        taskList.getItems().clear();
        taskList.getItems().addAll(tasks);
    }

    @FXML
    private void handleAddTask() {
        if (user == null) {
            throw new IllegalStateException("User is not set");
        }
        String taskDescription = taskInput.getText();
        LocalDate date = datePicker.getValue();
        if (taskDescription != null && !taskDescription.isEmpty() && date != null) {
            Task newTask = new Task(user.getId(), taskDescription, date, false);
            TaskDAO.addTask(newTask);
            taskList.getItems().add(newTask);
            taskInput.clear();
            datePicker.setValue(null);
        }
    }

    @FXML
    private void handleDeleteTask() {
        if (user == null) {
            throw new IllegalStateException("User is not set");
        }
        Task selectedTask = taskList.getSelectionModel().getSelectedItem();
        if (selectedTask != null) {
            TaskDAO.deleteTask(selectedTask.getId());
            taskList.getItems().remove(selectedTask);
        }
    }

    private class TaskListCell extends ListCell<Task> {
        private HBox content;
        private CheckBox checkBox;
        private Label label;
        private Button deleteButton;
        private Button editButton;

        public TaskListCell() {
            super();
            checkBox = new CheckBox();
            label = new Label();
            deleteButton = new Button("Delete");
            editButton = new Button("Edit");

            checkBox.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    task.setCompleted(checkBox.isSelected());
                    TaskDAO.updateTask(task);
                    updateItem(task, false);
                }
            });

            deleteButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    TaskDAO.deleteTask(task.getId());
                    getListView().getItems().remove(task);
                }
            });

            editButton.setOnAction(event -> {
                Task task = getItem();
                if (task != null) {
                    taskInput.setText(task.getDescription());
                    datePicker.setValue(task.getDate());
                    getListView().getItems().remove(task);
                }
            });

            HBox buttons = new HBox(editButton, deleteButton);
            content = new HBox(checkBox, label, buttons);
            content.setSpacing(10);
        }

        @Override
        protected void updateItem(Task task, boolean empty) {
            super.updateItem(task, empty);
            if (empty || task == null) {
                setText(null);
                setGraphic(null);
            } else {
                checkBox.setSelected(task.isCompleted());
                label.setText(task.getDescription() + " (Due: " + task.getDate() + ")");
                setGraphic(content);
            }
        }
    }
}
