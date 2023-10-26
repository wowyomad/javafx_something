package com.example.somefx;

import com.google.gson.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;


public class Book implements Serializable {

    private transient StringProperty bookName;
    private transient StringProperty bookAuthor;
    private transient StringProperty description;

    // Define regular fields to store the property values
    public String nameValue;
    public String authorValue;
    public String descriptionValue;

    public Book() {
        this.bookName = new SimpleStringProperty();
        this.bookAuthor = new SimpleStringProperty();
        this.description = new SimpleStringProperty();
    }

    public Book(String name, String surname, String description) {
        this.nameValue = name;
        this.authorValue = surname;
        this.descriptionValue = description;
        this.bookName = new SimpleStringProperty(nameValue);
        this.bookAuthor = new SimpleStringProperty(authorValue);
        this.description = new SimpleStringProperty(descriptionValue);
    }

    public String getBookName() {
        return bookName.get();
    }

    public void setBookName(String bookName) {
        this.bookName.set(bookName);
        this.nameValue = bookName;
    }

    public StringProperty bookNameProperty() {
        return bookName;
    }

    // Getter and Setter for surname
    public String getBookAuthor() {
        return bookAuthor.get();
    }

    public void setBookAuthor(String bookAuthor) {
        this.bookAuthor.set(bookAuthor);
        this.authorValue = bookAuthor;
    }

    public StringProperty bookAuthorProperty() {
        return bookAuthor;
    }

    // Getter and Setter for description
    public String getDescription() {
        return description.get();
    }

    public void setDescription(String description) {
        this.description.set(description);
        this.descriptionValue = description;
    }

    public StringProperty descriptionProperty() {
        return description;
    }
}

class PersonListDeserializer implements JsonDeserializer<List<Book>> {

    @Override
    public List<Book> deserialize(JsonElement json, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        List<Book> bookList = new ArrayList<>();
        JsonArray jsonArray = json.getAsJsonArray();

        for (JsonElement element : jsonArray) {
            JsonObject jsonObject = element.getAsJsonObject();
            String nameValue = jsonObject.get("nameValue").getAsString();
            String surnameValue = jsonObject.get("authorValue").getAsString();
            String descriptionValue = jsonObject.get("descriptionValue").getAsString();


            Book book = new Book(nameValue, surnameValue, descriptionValue);
            bookList.add(book);
        }

        return bookList;
    }
}

