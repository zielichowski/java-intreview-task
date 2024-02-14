package com.javacaptain.original;

import java.io.*;
import java.util.Optional;

/**
 * This class is thread safe. This class in not thread safe!
 */

/**
 * Name of the class suggests Facade design pattern, where in fact we are dealing with a kind of utility class
 * In fact I'm against that kind of design and I'll point out some common pitfalls
 */
public class ParserFacade {

    private static ParserFacade instance;

    /**
     * This is not thread safe. If you really need Singleton, use Enum with INSTANCE instead.
     * Example is NaturalOrderComparator from jdk8
     */
    public static ParserFacade getInstance() {
        if (instance == null) {
            instance = new ParserFacade();
        }
        return instance;
    }

    /**
     * There is no need to hold the instance File as your intention is to provide utility class
     * which manipulates file content. But if for some unknown for me reasons you have to use this
     * instance, make it private final.
     * Moreover, I think File is too specific and we should use at most a file path.
     */
    private File file;

    /**
     * Setters are evil. 99% of clients using our code will forget to fire this setter.
     * It's synchronized but with private final variable we do not have to worry about synchronization
     * because our variable is immutable.
     * Moreover as you construct Singleton you should be aware that once the file is set
     * it will be used everywhere in the system.
     */
    public synchronized void setFile(File f) {
        file = f;
    }

    /**
     * I'm sorry but I don't understand this getter. First of all, I suppose
     * none of the code clients would have any reason to use it. It should be deleted.
     */

    public synchronized Optional<File> getFile() {
        if (file != null) {
            return Optional.of(file);
        } else {
            /**
             * Also, this if else block is unnecessary. Advice for the future- if you want to
             * return Optional<T> and you expect null value use Optional.ofNullable().
             * Never ever return null!
             */
            return null;
        }
    }

    /**
     * Here we have the first behavior in out class. In fact we want to read file content
     * and present it as String. First, my design advice. Create a separate Interface to
     * expose our intention - read content from file. Secondly, put this code into one of the
     * implementation.
     */
    public String getContent() throws IOException {
        /** As I suggested at line 25 we should use a file path. Then, we could shorten
         * all this method code to a single line using java8:
         * String(Files.readAllBytes(Paths.get()), StandardCharsets.UTF_8)
         */
        FileInputStream i = new FileInputStream(file);
        String output = "";
        int data;
        /**
         * We should never concat Strings in loop. String is final, so each time we create
         * a new instance. It's super inefficient. Use StringBuilder instead.
         */
        while ((data = i.read()) > 0) output += (char) data;
        return output;
    }

    /**
     * The same comment as above.
     */

    public String getContentWithoutUnicode() throws IOException {
        FileInputStream i = new FileInputStream(file);
        String output = "";
        int data;
        while ((data = i.read()) > 0) if (data < 0x80) {
            output += (char) data;
        }
        return output;
    }

    /**
     * This is another behaviour in our class. Candidate for interface- FileWriter.
     * I suggest to use Files.write(Paths.get(), content.getBytes());
     */
    public void saveContent(String content) throws IOException {
        FileOutputStream o = new FileOutputStream(file);
        for (int i = 0; i < content.length(); i += 1) {
            o.write(content.charAt(i));
        }
    }
    /**
     * The last two technical comments. First: throwing checked exception- IOException.
     * I believe we should discuss it if our client has any chance to recover from
     * a situation where for example a file does not exist and if we should use checked exceptions at all.
     * Maybe it is better idea to throw custom RuntimeException.
     * Furthermore, if we do not use try catch with resource block we should
     * care about closing all the file streams.
     */
}