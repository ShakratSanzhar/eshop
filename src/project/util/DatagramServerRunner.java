package project.util;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class DatagramServerRunner {

    public static void main(String[] args) throws IOException {
        // для реализации клиента или сервера используется один и тот же класс - DatagramSocket
        try (var datagramServer = new DatagramSocket(7777)) { // в случае сервера, в параметры передаем порт
            byte[] buffer = new byte[512]; // в этот баффер будут передаваться данные из нашего клиента
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            datagramServer.receive(packet); // получаем пакет от клиента, здесь сервер ожидает пока какой-нибудь клиент не отправит пакет

            System.out.println(new String(buffer)); // данные от клиента полученные нашим пакетом мы получаем из buffer
        }
    }
}
