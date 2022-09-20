/*
*
Ответить на вопросы (письменно):
Как организовать клиент-серверное взаимодействие?
* в общем случае, серверная часть слушает определённый сокет и далее в потоках обрабатывает запросы.
* файлы можно хранить в БД, а можно в ФС сервера.
Как и в каком виде передавать файлы?
* способов много. наиболее распространённые способы - чтение из потоков ввода-вывода, сегментация передачи данных.
Как пересылать большие файлы?
* сегментировано, и далее каким то образом соединять после передачи.
Как пересылать служебные команды?
* тоже способов может быть много. например, перед передаче данных. Или использовать отдельный сокет как в протоколе FTP.
Что хранить в базе данных?
* в общем случае - путь, имя файла, права доступа, и сами данные (если система поддерживает)
Как передавать структуру каталогов/файлов?
* в служебных командах, или в виде строки
Какую библиотеку использовать для сетевого взаимодействия: java.io, java.nio, Netty?
* каждый способ подойдёт, .io простой, но блокирующий, .nio новее, возможностей больше. Netty - сложнее, современный фреймворк,
* который исполользуется в боевых проектах.
* */


package main;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Server server = new Server(4445);
        new Thread(server).start();

        Scanner scanner = new Scanner(System.in);
        while (scanner.nextLine().equals("stop")) {
            server.stopServer();
        }

    }
}
