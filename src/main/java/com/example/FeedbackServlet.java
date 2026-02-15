package com.example;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class FeedbackServlet extends HttpServlet {

    // Имитация БД (храним в памяти)
    // static не обязателен, если сервлет синглтон, но для надежности можно оставить
    private final static List<String> feedbacks = new CopyOnWriteArrayList<>();

    // Метод GET: Просто отображает список отзывов

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html;charset=UTF-8");
        PrintWriter out = resp.getWriter();

        out.println("<html><body>");
        out.println("<h1>Список отзывов</h1>");

        if (feedbacks.isEmpty()) {
            out.println("<p>Отзывов пока нет.");
        } else {
            out.println("<ul>");
            for (String feedback : feedbacks) {
                out.println("<li>" + feedback + "</li>");
            }
            out.println("</ul>");
        }

        out.println("<a href='index.html'>Вернуться назад</a>");
        out.println("</body></html>");
    }

    // Метод POST: Принимает данные и сохраняет их
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. Правильная кодировка (иначе будут "краказяблики - P?P?`P")
        req.setCharacterEncoding("UTF-8");

        // 2. Извлечение данных из формы по атрибуту name
        String name = req.getParameter("username");
        String message = req.getParameter("message");

        // 3. Валидация
        if (name == null || name.trim().isEmpty()) {
            name = "Аноним";
        }

        // 4. Сохраняем "в базу"
        String record = "<strong>" + name + ":</strong> " + message;
        feedbacks.add(record);

        // 5. Ответ пользователю
        // Вариант 1: Показать страницу подтверждения
        // Вариант 2: Перенаправить на список
        resp.sendRedirect("feedback");
    }
}
