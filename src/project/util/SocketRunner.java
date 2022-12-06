package project.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.Socket;

public class SocketRunner {

    public static void main(String[] args) throws IOException {
        //     http - 80
        //     https - 443
        //     tcp
        //     socket - это наш клиент, мы будет передавать данные серверу, который указали в параметрах
        var inetAddress = Inet4Address.getByName("google.com"); // класс для получения IP-адреса по хосту
        try (var socket = new Socket(inetAddress, 80);
             var outputStream = new DataOutputStream(socket.getOutputStream());
             var inputStream = new DataInputStream(socket.getInputStream())) {
            outputStream.writeUTF("Hello world!"); // отправляем запрос на сервер
            var response = inputStream.readAllBytes(); // в этой строчке клиент(socket) будет ожидать пока сервер не ответит
            System.out.println(response.length);
        }
    }
}
