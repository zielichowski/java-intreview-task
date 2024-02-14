package com.javacaptain.refactored;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

sealed interface FileWriter {
    void saveFileContent(String content, String filePath);

    final class SimpleFileWriter implements FileWriter {
        @Override
        public void saveFileContent(String content, String filePath) {
            try {
                Files.write(Paths.get(filePath), content.getBytes());
            } catch (IOException e) {
                throw new RuntimeException("This should be more specific exception");
            }
        }
    }
}

