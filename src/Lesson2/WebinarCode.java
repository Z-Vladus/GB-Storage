package Lesson2;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;

public class WebinarCode {

    public static void main(String[] args) {
        //создание пути до файла (абсолютный)
        Path path = Paths.get("C:\\temp\\file.txt");
        //создание пути до файла (относительный путь, считая от места откуда запустиласть программа)
        Path path1 = Path.of("users\\file.txt");
        // создание пути до файла (относительный путь, считая от места откуда запустиласть программа - точка указывает на тек. каталог)
        Path path2 = Path.of("./tmp/file");

        // класс Files работает с экземплярами Path
        // нужно оборачивать в try..catch и обрабатывать разные случаи ошибок работы с ФС
        try {
            Files.createDirectories(Path.of("dir"));
        }
        catch (FileAlreadyExistsException) {
            System.out.println("Директория уже существует");
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }

        // копирование папки (содержимого)
        try {
            Files.copy(path1,path2, StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // рекурсивный обход директории, переопределение методов
        Files.walkFileTree(Path.of("dir"), new FileVisitor<Path>() {
            @Override
            public FileVisitResult preVisitDirectory(Path dir, BasicFileAttributes attrs) throws IOException {
                // дошли до директории, но не вошли в неё.
                // например, можем проверить, какие дирекотрии пропускать.
                if dir.endsWith("skip.me"){
                    // идем к следующему файлу
                    return FileVisitResult.CONTINUE;
                };
                //return null;
            }
            // когда указатель на файле
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                //можем удалить
                Files.delete(file);
                // нужный файл нашли и удалили, выходим из рекурсивного обхода.
                return FileVisitResult.TERMINATE;
            }

            @Override
            public FileVisitResult visitFileFailed(Path file, IOException exc) throws IOException {
            //обработка оибок чтения объекта ФС
                return null;

            }

            @Override
            public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                // после выхода из директории. Полезно, когда надо удалить директорию, после её очистки от файлов.
                return null;
            }
        });

        //размеры файлов! осторожно, может переполнить память!
        // readAllBytes документация говорит что можно считать не более (размер int) - 8 байт.
        // поэтому нужно проверять размер файла перед считыванием Files.size();
        // большие файлы складываютсяв HEAP , сразу несколько не считываем.
        // в терминале команда java -XX:+PrintFlagsFinal -version - инфа о Джаве, в т.ч. и HEAP size.
        try {
            byte[] bytes = Files.readAllBytes(Path.of("file.txt"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // кастмно прочитать из файла 10 мегабайт
        ByteBuffer buf = ByteBuffer.allocate(1024*1024*10);
        FileChannel fileChannel = FileChannel.open(Path.of("temp.txt"));
        try {
            fileChannel.read(buf);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }


    }


