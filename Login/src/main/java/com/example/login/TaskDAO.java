package com.example.login;

import com.example.login.ConnexioDB;
import com.example.login.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAO {
    public static List<Task> getTasks(int userId) {
        List<Task> tasks = new ArrayList<>();
        String query = "SELECT * FROM tasks WHERE user_id = ?";
        try (Connection conn = ConnexioDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                tasks.add(new Task(
                        rs.getInt("id"),
                        rs.getInt("user_id"),
                        rs.getString("description"),
                        rs.getDate("date").toLocalDate(),
                        rs.getBoolean("completed")));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving tasks", e);
        }
        return tasks;
    }

    public static void addTask(Task task) {
        String query = "INSERT INTO tasks (user_id, description, date, completed) VALUES (?, ?, ?, ?)";
        try (Connection conn = ConnexioDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, task.getUserId());
            stmt.setString(2, task.getDescription());
            stmt.setDate(3, Date.valueOf(task.getDate()));
            stmt.setBoolean(4, task.isCompleted());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error adding task", e);
        }
    }

    public static void deleteTask(int taskId) {
        String query = "DELETE FROM tasks WHERE id = ?";
        try (Connection conn = ConnexioDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, taskId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting task", e);
        }
    }

    public static void updateTask(Task task) {
        String query = "UPDATE tasks SET description = ?, date = ?, completed = ? WHERE id = ?";
        try (Connection conn = ConnexioDB.getConnexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, task.getDescription());
            stmt.setDate(2, Date.valueOf(task.getDate()));
            stmt.setBoolean(3, task.isCompleted());
            stmt.setInt(4, task.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error updating task", e);
        }
    }
}
