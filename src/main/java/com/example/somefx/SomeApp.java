package com.example.somefx;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;


import java.io.FileNotFoundException;


import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;


public class SomeApp extends Application {
    private List<Book> bookList = new ArrayList<>();
    private ObservableList<Book> filteredList = FXCollections.observableArrayList();

    private ChoiceBox<String> searchInChoice;
    private ChoiceBox<String> searchScopeChoice;
    private TextField searchField;
    private TextArea resultTextArea;

    private CheckBox checkBoxSearchName;
    private CheckBox checkBoxSearchAuthor;
    private CheckBox checkBoxSearchDescription;

    private TableView<Book> personTableView;

    @Override
    public void start(Stage stage) throws Exception {
        bookList = null;
        try {
            bookList = JSON.readDataFromFile("data.json", Book.class);
        } catch (FileNotFoundException e) {
            bookList = new ArrayList<>();
        }
        if (bookList == null) bookList = new ArrayList<>();
        stage.setTitle("Book Management App");

        searchInChoice = new ChoiceBox<>();
        searchScopeChoice = new ChoiceBox<>();

        resultTextArea = new TextArea();

        checkBoxSearchName = new CheckBox("Search in Name");
        checkBoxSearchAuthor = new CheckBox("Search in Author");
        checkBoxSearchDescription = new CheckBox("Search in Description");

        searchField = new TextField();
        searchField.setPromptText("Search value");


        Button addButton = new Button("Add Person");
        Button editButton = new Button("Edit Person");

        VBox root = new VBox();

        // Right-click context menu for the TableView
        ContextMenu contextMenu = new ContextMenu();
        MenuItem deleteMenuItem = new MenuItem("Delete");
        contextMenu.getItems().add(deleteMenuItem);
        MenuItem editMenuItem = new MenuItem("Edit");
        contextMenu.getItems().add(editMenuItem);

        personTableView = new TableView<>();
        personTableView.setItems(filteredList);
        personTableView.setContextMenu(contextMenu);


        // Create columns with appropriate headers
        TableColumn<Book, String> nameColumn = new TableColumn<>("Name");
        TableColumn<Book, String> authorColumn = new TableColumn<>("Author");
        TableColumn<Book, String> descriptionColumn = new TableColumn<>("Description");

        nameColumn.setCellValueFactory(cellData -> cellData.getValue().bookNameProperty());
        authorColumn.setCellValueFactory(cellData -> cellData.getValue().bookAuthorProperty());
        descriptionColumn.setCellValueFactory(cellData -> cellData.getValue().descriptionProperty());

        personTableView.getColumns().addAll(nameColumn, authorColumn, descriptionColumn);

        addButton.setOnAction(event -> {
            openAddBookWindow();
        });

        // Handle context menu item for delete
        deleteMenuItem.setOnAction(event -> {
            Book selectedBook = personTableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                bookList.remove(selectedBook);
                filteredList.remove(selectedBook);
            }
        });

        editMenuItem.setOnAction(event -> {
            Book selectedBook = personTableView.getSelectionModel().getSelectedItem();
            if (selectedBook != null) {
                openEditPersonWindow(selectedBook);
                updateSearchResults();
            }
        });

        // Handle sorting when column headers are pressed
        nameColumn.setSortType(TableColumn.SortType.ASCENDING);
        authorColumn.setSortType(TableColumn.SortType.ASCENDING);
        descriptionColumn.setSortType(TableColumn.SortType.ASCENDING);
        personTableView.getSortOrder().add(nameColumn);

        searchInChoice.setOnAction(event -> updateSearchResults());
        searchField.setOnAction(event -> updateSearchResults());
        checkBoxSearchName.setOnAction(event -> updateSearchResults());
        checkBoxSearchAuthor.setOnAction(event -> updateSearchResults());
        checkBoxSearchDescription.setOnAction(event -> updateSearchResults());

        VBox searchCriteria = new VBox(
                searchField,
                checkBoxSearchName,
                checkBoxSearchAuthor,
                checkBoxSearchDescription
        );

        root.getChildren().addAll(
                personTableView,
                resultTextArea,
                searchCriteria,
                addButton
        );

        Scene scene = new Scene(root, 600, 400);
        stage.setScene(scene);
        stage.setOnCloseRequest(event -> {
            JSON.saveDataToFile(bookList, "data.json");
        });

        updateSearchResults();
        stage.show();
    }

    private void updateSearchResults() {
        filteredList.clear();

        for (Book book : bookList) {
            if(passesFilter(book))
                filteredList.add(book);
        }
        System.out.println(filteredList);
    }

    // Define openAddPersonWindow and openEditPersonWindow methods

    public static void main(String[] args) {
        launch(args);
    }


    private void addBook(String name, String author, String description) {
        Book newBook = new Book(name, author, description);
        bookList.add(newBook);

//        searchInChoice.getItems().add(newPerson.getName()); // Update the ChoiceBox with names, for example.
//        searchScopeChoice.getItems().add(newPerson.getDescription()); // Update the ChoiceBox with descriptions.
    }

    private void editBook(int index, String name, String author, String description) {
        if (index >= 0 && index < bookList.size()) {
            Book updatedBook = bookList.get(index);
            updatedBook.setBookName(name);
            updatedBook.setBookAuthor(author);
            updatedBook.setDescription(description);



        }
    }

    private void deleteBook(int index) {
        if (index >= 0 && index < bookList.size()) {
            bookList.remove(index);

        }
    }

    private void openAddBookWindow() {
        // Create a new stage for adding a person
        Stage addPersonStage = new Stage();
        addPersonStage.setTitle("Add Book");

        // Create UI components for adding a person
        TextField nameField = new TextField();
        nameField.setPromptText("Enter name");
        TextField surnameField = new TextField();
        surnameField.setPromptText("Enter author");
        TextField descriptionField = new TextField();
        descriptionField.setPromptText("Enter description");
        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String description = descriptionField.getText();
            addBook(name, surname, description);
            updateSearchResults();
            addPersonStage.close();
        });

        VBox addBookLayout = new VBox(nameField, surnameField, descriptionField, saveButton);
        Scene addBookScene = new Scene(addBookLayout, 300, 200);
        addPersonStage.setScene(addBookScene);
        addPersonStage.show();
    }

    private void openEditPersonWindow(Book selectedBook) {
        Stage editPersonStage = new Stage();
        editPersonStage.setTitle("Edit Person");

        int selectedIndex = personTableView.getSelectionModel().getSelectedIndex();
        TextField nameField = new TextField(selectedBook.getBookName());
        nameField.setPromptText("Enter name");
        TextField surnameField = new TextField(selectedBook.getBookAuthor());
        surnameField.setPromptText("Enter surname");
        TextField descriptionField = new TextField(selectedBook.getDescription());
        descriptionField.setPromptText("Enter description");
        Button saveButton = new Button("Save");

        saveButton.setOnAction(event -> {
            String name = nameField.getText();
            String surname = surnameField.getText();
            String description = descriptionField.getText();
            editBook(selectedIndex, name, surname, description);
        });

        // Create layout for editing a person
        VBox editPersonLayout = new VBox(nameField, surnameField, descriptionField, saveButton);
        Scene editPersonScene = new Scene(editPersonLayout, 300, 200);
        editPersonStage.setScene(editPersonScene);
        editPersonStage.show();
    }

    private boolean passesFilter(Book book) {
        if (!checkBoxSearchAuthor.isSelected() && !checkBoxSearchDescription.isSelected() && !checkBoxSearchName.isSelected())
            return true;

        String searchValue = searchField.getText().toLowerCase();

        boolean nameCheck = !checkBoxSearchName.isSelected() || book.getBookName().toLowerCase().contains(searchValue);
        boolean authorCheck = !checkBoxSearchAuthor.isSelected() || book.getBookAuthor().toLowerCase().contains(searchValue);
        boolean descriptionCheck = !checkBoxSearchDescription.isSelected() || book.getDescription().toLowerCase().contains(searchValue);

        return (checkBoxSearchName.isSelected() && nameCheck) ||
                (checkBoxSearchAuthor.isSelected() && authorCheck) ||
                (checkBoxSearchDescription.isSelected() && descriptionCheck);
    }
}


