package com.mostrario;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import javafx.application.Application;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayInputStream;
import java.lang.reflect.Type;
import java.util.Base64;
import java.util.List;

public class App extends Application {

    private TableView<Product> tableView;

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Productos Agrícolas");

        tableView = new TableView<>();
        configurarTabla();

        try {
            // Obtener los productos y agregarlos a la tabla
            List<Product> productos = fetchProducts();
            if (!productos.isEmpty()) {
                tableView.getItems().addAll(productos);
            } else {
                mostrarError("No se encontraron productos para mostrar.");
            }
        } catch (Exception e) {
            mostrarError("Error al obtener los productos: " + e.getMessage());
        }

        // Ajustar el tamaño del TableView
        tableView.setPrefWidth(600);
        tableView.setPrefHeight(1000);

        // Crear un VBox con el fondo rojo y ajustarlo
        VBox innerVBox = new VBox(tableView);
        innerVBox.setAlignment(Pos.CENTER);
        innerVBox.setPadding(new Insets(20)); // Ajusta el espaciado interno alrededor del TableView
        innerVBox.setStyle("-fx-background-color: #1e1e2f;"); // Fondo oscuro alrededor del TableView

        // Crear un VBox para el fondo rojo
        VBox outerVBox = new VBox(innerVBox);
        outerVBox.setAlignment(Pos.CENTER);
        outerVBox.setStyle("-fx-background-color: #FF0000;"); // Fondo rojo
        outerVBox.setPadding(new Insets(6)); // Ajusta el espaciado del fondo rojo

        Scene scene = new Scene(outerVBox, 800, 600);
        scene.getStylesheets().add(getClass().getResource("/styles.css").toExternalForm()); // Aplicar el estilo
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private void configurarTabla() {
        TableColumn<Product, String> nombreCol = new TableColumn<>("Nombre");
        nombreCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getNombre()));

        TableColumn<Product, String> descripcionCol = new TableColumn<>("Descripción");
        descripcionCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getDescripcion()));

        TableColumn<Product, Double> precioCol = new TableColumn<>("Precio");
        precioCol.setCellValueFactory(param -> new SimpleObjectProperty<>(param.getValue().getPrecio()));

        TableColumn<Product, ImageView> fotoCol = new TableColumn<>("Foto");
        fotoCol.setCellValueFactory(param -> {
            String fotoBase64 = param.getValue().getFoto();
            if (fotoBase64 != null && !fotoBase64.isEmpty()) {
                byte[] fotoBytes = Base64.getDecoder().decode(fotoBase64);
                Image image = new Image(new ByteArrayInputStream(fotoBytes));
                ImageView imageView = new ImageView(image);
                imageView.setFitWidth(100);
                imageView.setFitHeight(100);
                return new SimpleObjectProperty<>(imageView);
            }
            return null;
        });

        tableView.getColumns().addAll(nombreCol, descripcionCol, precioCol, fotoCol);
    }

    private List<Product> fetchProducts() throws Exception {
        String API_URL = "https://e0f8-2804-6a84-1057-a800-ca9-9f82-f60a-2f7c.ngrok-free.app/agricola"; // Cambia esta URL según la API

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(API_URL);

            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    throw new RuntimeException("Error: La API respondió con código " + statusCode);
                }

                String json = EntityUtils.toString(response.getEntity());
                System.out.println("Response JSON: " + json);  // Imprime la respuesta JSON para verificación

                Gson gson = new Gson();
                Type listType = new TypeToken<List<Product>>() {}.getType();
                return gson.fromJson(json, listType);
            }
        }
    }

    private void mostrarError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText("Se produjo un error");
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
