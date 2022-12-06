package project.util;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HttpServer {

    private final ExecutorService pool; // создаем пул для того чтобы обрабатывать много запросов от клиентов, а не ждать пока обработается один запрос клиента
    private final int port; // порт сервера, хост создастся автоматически исходя из хоста машины на которой запущен Java-код
    private boolean stopped; // нужно чтобы остановить обработку новых запросов от клиентов

    public HttpServer(int port, int poolSize) {
        this.port = port;
        this.pool= Executors.newFixedThreadPool(poolSize);
    }

    public void run() {
        try {
            var server = new ServerSocket(port); // создаем сам сервер
            while (!stopped) { // будем обрабатывать запросы пока не остановим
                var socket = server.accept(); // будем ждать в этой точке пока какой-нибудь клиент не отправит запрос
                System.out.println("Socket accepted");
               pool.submit(() -> processSocket(socket)); //обрабатываем запрос с помощью пула - т.е. отправляем в качестве задачи в пул обработку запроса клиента
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    private void processSocket(Socket socket) {
        try (socket; //мы должны закрыть socket когда его использовали, поэтому помещаем его в блок try-with-resources
             var inputStream = new DataInputStream(socket.getInputStream());  //считываем данные из request
             var outputStream = new DataOutputStream(socket.getOutputStream())) {  //записываем response для клиента
            // step 1 handle request
            System.out.println("Request: " + new String(inputStream.readNBytes(400))); // считываем с request только 400 байт

            // step 2 handle response
            var body = Files.readAllBytes(Path.of("resources", "example.html")); // в качестве body передаем файл html
            var headers = """ 
                    HTTP/1.1 200 OK
                    content-type: text/html
                    content-length: %s
                    """.formatted(body.length).getBytes(); // создаем headers, объединенную со стартовой строкой
            outputStream.write(headers); // записываем заголовки в response
            outputStream.write(System.lineSeparator().getBytes()); // заголовки должны быть отделены пустой строкой от тела
            outputStream.write(body); // записываем тело в response
        } catch (
                IOException e) { // мы загологируем исключение чтобы сервер не прервал свою работу из-за одного неправильного запроса
            // TODO: 2/27/21 log error message
            e.printStackTrace();
        }
    }

    public void setStopped(boolean stopped) {
        this.stopped = stopped;
    }
}
