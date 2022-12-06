package project.util;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.nio.file.Path;

import static java.net.http.HttpResponse.BodyHandlers;

public class HttpClientExample {

    public static void main(String[] args) throws IOException, InterruptedException {

        /*
        класс HttpClient - используется в качестве клиента к веб-серверу, этот класс необходим для отправки запросов с Body (POST,PUT,DELETE и тд)
        HttpClient является потокобезопасным => его можно создать только один раз на все приложение
        также HttpClient позволяет работать в асинхронном режиме, т.е. отправлять запрос и не дождавшись ответа получать CompletableFuture для использования ее в будущем
        */
        // создаем объект HttpClient, с помощью паттерна строитель (этот паттерн уже встроен в IDE), таким образом мы можем настраивать клиента как хотим
        var httpClient = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_1_1)
                .build();
        // создаем GET-запрос на гугл
        HttpRequest request = HttpRequest.newBuilder(URI.create("https://www.google.com"))
                .GET()
                .build();
        // создаем POST-запрос на гугл для записи в него файла
        HttpRequest request1 = HttpRequest.newBuilder(URI.create("https://www.google.com"))
                .POST(HttpRequest.BodyPublishers.ofFile(Path.of("path","to","file")))
                .build();
        // метод send() используется для отправки request на веб-сервер, в параметры кидаем сам request и как мы хотим обработать response(обработчик тела), возвращает response
        var response = httpClient.send(request, BodyHandlers.ofString());
        // выводим тело ответа и заголовки
        System.out.println(response.body());
        System.out.println(response.headers());
    }
}
