package project.util;

import java.io.IOException;
import java.net.URL;

public class UrlExample {

    public static void main(String[] args) throws IOException {
        // URL - класс для работы по протоколу HTTP
        // по-умолчанию URL работает только с методом GET
        var url = new URL("file:/Users/sanzharshakrat/IdeaProjects/eshop/src/project/util/DatagramRunner.java"); // также URL мы можем использовать для того чтобы получить какую-либо информацию из наших файлов
        var urlConnection = url.openConnection(); // представляем его также в виде urlConnection

        System.out.println(new String(urlConnection.getInputStream().readAllBytes())); // считываем информацию с файла побайтно и выводим ее в виде строки в консоль
    }

    private static void checkGoogle() throws IOException {
        var url = new URL("https://www.google.com"); // указываем протокол и домен в параметрах
        var urlConnection=url.openConnection(); // открываем соединение к нашему серверу, который мы указали в качестве конструктора в объекте URL
        urlConnection.setDoOutput(true); // выставляем переменную doOutput в true для того чтобы работать с другими HTTP-методами
        // urlConnection может возвращать разные данные
        try (var outputStream = urlConnection.getOutputStream()) { // записываем в request к серверу
        }

        System.out.println();
    }
}
