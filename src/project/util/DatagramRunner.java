package project.util;

import java.io.IOException;
import java.net.*;

public class DatagramRunner {

    public static void main(String[] args) throws IOException {
        var inetAddress = InetAddress.getByName("localhost"); // получаем IP-адрес по хосту
        try (var datagramSocket = new DatagramSocket()) {  // клиент по протоколу UDP
  //               ------> [buffer]<------
                        // buffer представляет собой массив, кто то записывает в него информацию, а кто-то считывает с него информацию
            var bytes = "Hello from UDP client".getBytes(); // размер массива байт должен быть одинаковый и на клиенте и на сервере
            DatagramPacket packet = new DatagramPacket(bytes, bytes.length,inetAddress,7777); // для отправки и получения сообщений использует - datagram пакеты, в которые также передаем массив байт и адрес и порт сервера
            datagramSocket.send(packet); // отправляет пакет
        }
    }
}
