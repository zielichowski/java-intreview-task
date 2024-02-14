package com.javacaptain.refactored;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

sealed interface FileReader {
    String readFileContent(String filePath);

    final class SimpleFileReader implements FileReader {
        @Override
        public String readFileContent(String filePath) {
            try {
                return Files.readString(Paths.get(filePath));
            } catch (IOException e) {
                throw new RuntimeException("This should be more specific exception");
            }
        }
    }

    final class WithoutUnicodeFileReader implements FileReader {
        @Override
        public String readFileContent(String filePath) {
            try (FileInputStream inputStream = new FileInputStream(filePath)) {
                StringBuilder stringBuilder = new StringBuilder();
                int data;
                while ((data = inputStream.read()) > 0) if (data < 0x80) {
                    stringBuilder.append((char) data);
                }
                return stringBuilder.toString();
            } catch (IOException e) {
                throw new RuntimeException("This should be more specific exception");
            }
        }
    }

}



