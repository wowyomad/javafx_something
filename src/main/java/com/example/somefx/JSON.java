package com.example.somefx;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;
import java.util.List;


public class JSON {
    public static void saveDataToFile(List<Book> list, String filename) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (Writer writer = new FileWriter(filename)) {
            gson.toJson(list, writer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static List readDataFromFile(String filename, Type dataType) throws FileNotFoundException {
        Type type = new TypeToken<List<Book>>(){}.getType();
        List list = null;
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(new TypeToken<List<Book>>(){}.getType(), new PersonListDeserializer())
                .create();
            File file = new File("data.json");
            if (file.exists()) {
                try (Reader reader = new FileReader(file)) {
                    list = gson.fromJson(reader, type);
                } catch (IOException e) {
                    throw new FileNotFoundException(filename + " wasn't opened");
                }
            }
        return list;
    }
}
