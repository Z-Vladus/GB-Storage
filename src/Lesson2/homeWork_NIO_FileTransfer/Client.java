/*
* По этим урокам сделано не так много как хотелось бы, но всё же
* 1) прослушан вебинар, код написан самостоятельно (без копи-паста) с попыткой понять как работает Селектор(до конца не понял, очень много деталей) (доделать!)
* 2) прочитаны статьи с Хабра и IBM
*  https://www.ibm.com/developerworks/java/tutorials/j-nio/j-nio.html
*  https://habr.com/post/235585/
* 3)
*
*
* */

package Lesson2.homeWork_NIO_FileTransfer;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Client {


    public static void main(String[] args) {
        Path path = Paths.get("_CLNT_Files\\temp.txt");
        if (Files.exists(path)) {
            System.out.println("Файл существует, продолжаем");

        }

    }

}
