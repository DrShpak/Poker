package gui;

import java.io.IOException;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;

public class IOUtils {
    private static PipedInputStream in;
    private static PipedOutputStream out;
    private static Consumer<String> printer;

    public static void redirectIO(Consumer<String> printer) throws IOException {
        var o = new PipedOutputStream();

        in = new PipedInputStream(o);
        System.setOut(new java.io.PrintStream(o));

        out = new PipedOutputStream();
        System.setIn(new PipedInputStream(out));

        IOUtils.printer = printer;
    }

    public static String readString() throws IOException {
        var sb = new StringBuilder();
        do {
            var bytes = new byte[10000];
            var count = in.read(bytes, 0, 10000);
            sb.append(new String(bytes, 0, count, StandardCharsets.UTF_8));
        } while (in.available() > 0);
        printer.accept(sb.toString());
        return sb.toString();
    }

    public static void writeString(String str) throws IOException {
        var toWrite = str.concat("\n");
        printer.accept(toWrite);
        out.write(toWrite.getBytes());
    }
}
