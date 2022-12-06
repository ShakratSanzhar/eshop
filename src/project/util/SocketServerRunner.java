package project.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

public class SocketServerRunner {

    public static void main(String[] args) throws IOException {
        try (var serverSocket = new ServerSocket(7777); // создается сервер с хостом той машины, на которой запускается Java-приложение
        var socket = serverSocket.accept(); // метод возвращает клиента(socket) который подключился к серверу => accept() ожидает пока первый клиент не подключится к нашему серверу
        var outputStream = new DataOutputStream(socket.getOutputStream()); // записываем response для клиента
        var inputStream = new DataInputStream(socket.getInputStream())) {  // принимаем request от клиента
            System.out.println("Client request: " + inputStream.readUTF());
            outputStream.writeUTF("Hello from server!"); // запишем в response строчку
        } // выйдя из блока try-with-resources мы закрываем соединение
    }
}
