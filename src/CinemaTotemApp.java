  import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.Glow;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.*;

public class CinemaTotemApp extends Application {

    private final double PRICE_PER_SEAT = 20.0; 
    private Text totalText = new Text("Total: R$ 0,00");

    private static double totalPrice = 0.0;

        public static void main(String[] args) {
        launch(args);
        
        
    }
    


    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Totem de Cinema");

        Scene mainScene = createMainScene(primaryStage);
        primaryStage.setScene(mainScene);
        primaryStage.show();
    }


    private Scene createMainScene(Stage primaryStage) {
        HBox mainHBox = new HBox(20);
        mainHBox.setAlignment(Pos.CENTER);

        for (int i = 0; i < 6; i++) {
            mainHBox.getChildren().add(createMovieRectangle(primaryStage));
        }

        ScrollPane scrollPane = new ScrollPane(mainHBox);
        scrollPane.setFitToHeight(true);
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setStyle("-fx-background-color: transparent;");

        VBox root = new VBox(20);
        root.setAlignment(Pos.CENTER);
        root.setStyle("-fx-background-color: #336699;"); 
        root.getChildren().addAll(createSideMessage("Arraste para o lado ->"), scrollPane, totalText);


        totalText = new Text("Total: R$ 0,00");
        totalText.setFont(Font.font(18));
        totalText.setFill(Color.BLUE);

        return new Scene(root, 1000, 600);
    }
    private void updateTotalRectangle(double newTotalPrice, Text totalText) {
        totalPrice = newTotalPrice;
        totalText.setText(String.format("Total: R$ %.2f", totalPrice));
    }
    
    private VBox createMovieRectangle(Stage primaryStage) {
        VBox movieBox = new VBox(20);
        movieBox.setAlignment(Pos.CENTER);

        Rectangle imageRectangle = new Rectangle(200, 300);
        imageRectangle.setFill(Color.LIGHTGRAY);

        Rectangle ratingSquare = new Rectangle(30, 30, Color.GREEN);

        Label movieNameLabel = new Label(generateRandomMovieName());
        movieNameLabel.setStyle("-fx-font-size: 18pt;");

        VBox movieDetailsVBox = new VBox(10);
        movieDetailsVBox.setAlignment(Pos.CENTER_LEFT);
        movieDetailsVBox.getChildren().addAll(
                new Label("Título: " + movieNameLabel.getText()),
                imageRectangle,
                ratingSquare,
                new Label("Diretor: " + generateRandomDirector()),
                new Label("Gênero: " + generateRandomGenre())
        );

        movieBox.getChildren().addAll(movieDetailsVBox);

        movieBox.setOnMouseClicked(event -> {
            String movieName = movieNameLabel.getText();
            Rectangle movieRectangle = new Rectangle(200, 300);
            movieRectangle.setFill(Color.LIGHTGRAY);

            showScheduleScreen(primaryStage, movieName, movieDetailsVBox, movieRectangle);
        });

        return movieBox;
    }

    private void showScheduleScreen(Stage primaryStage, String movieName, VBox movieDetailsVBox, Rectangle movieRectangle) {
        VBox scheduleVBox = new VBox(20);
        scheduleVBox.setAlignment(Pos.CENTER);

        HBox movieInfoBox = new HBox(20);
        movieInfoBox.setAlignment(Pos.CENTER);

        VBox moviePosterVBox = new VBox(20);
        moviePosterVBox.setAlignment(Pos.CENTER_LEFT);
        moviePosterVBox.getChildren().add(movieRectangle);

        Label movieNameLabel = new Label(movieName);
        movieNameLabel.setStyle("-fx-font-size: 18pt;");
        movieInfoBox.getChildren().addAll(moviePosterVBox, movieNameLabel);

        scheduleVBox.getChildren().add(movieInfoBox);

        Text synopsisTitle = new Text("Sinopse");
        synopsisTitle.setFont(Font.font(18));
        Text synopsisText = new Text(generateRandomSynopsis());
        synopsisText.setFont(Font.font(14));
        synopsisText.setFill(Color.BLACK);

        VBox synopsisBox = new VBox(10);
        synopsisBox.setAlignment(Pos.CENTER_LEFT);
        synopsisBox.getChildren().addAll(synopsisTitle, synopsisText);

        VBox timeVBox = new VBox(10);
        timeVBox.setAlignment(Pos.CENTER_RIGHT);

        for (String time : Arrays.asList("10:00 AM", "2:00 PM", "5:00 PM", "8:00 PM")) {
            StackPane timeStackPane = createTimeRectangle(time, movieName, primaryStage);
            timeVBox.getChildren().add(timeStackPane);
        }

        HBox mainHBox = new HBox(20);
        mainHBox.getChildren().addAll(movieDetailsVBox, timeVBox);

        Button backButton = new Button("Voltar");
        backButton.setOnAction(event -> {
            primaryStage.setScene(createMainScene(primaryStage));
        });

        scheduleVBox.getChildren().addAll(mainHBox, synopsisBox, backButton);

        Button snackBarButton = new Button("Acompanhamentos");
        snackBarButton.setOnAction(event -> {
            primaryStage.setScene(DiscountScene.createDiscountScene(primaryStage, totalText, totalPrice));


        });
        

        scheduleVBox.getChildren().addAll(snackBarButton);

        Scene scheduleScene = new Scene(scheduleVBox, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(scheduleScene);
    }

    private StackPane createTimeRectangle(String time, String movieName, Stage primaryStage) {
        Rectangle timeRectangle = new Rectangle(120, 60);
        timeRectangle.setFill(Color.LIGHTBLUE);

        Label timeLabel = new Label(time);
        timeLabel.setTextFill(Color.BLACK);

        StackPane timeStackPane = new StackPane(timeRectangle, timeLabel);
        timeStackPane.setOnMouseClicked(event -> showSeatSelectionScreen(primaryStage, movieName, time));

        return timeStackPane;
    }

    private void showSeatSelectionScreen(Stage primaryStage, String movieName, String time) {
        VBox seatSelectionVBox = new VBox(20);
        seatSelectionVBox.setAlignment(Pos.CENTER);
        seatSelectionVBox.setPadding(new Insets(20));
    
        Text seatSelectionTitle = new Text("Escolha de Assentos");
        seatSelectionTitle.setFont(Font.font(18));
        seatSelectionTitle.setFill(Color.BLACK);
    
        GridPane seatGrid = createSeatGrid(movieName);
        Text selectedSeatsText = new Text("Assentos Selecionados: ");
        
        Button confirmButton = new Button("Confirmar");
        Button backButton = new Button("Voltar");
    
        seatSelectionVBox.getChildren().addAll(seatSelectionTitle, seatGrid, selectedSeatsText, confirmButton, backButton);
    
        backButton.setOnAction(event -> {
            showScheduleScreen(primaryStage, movieName, new VBox(), new Rectangle(200, 300));
        });
    
        confirmButton.setOnAction(event -> {
            double totalPrice = calculateTotalPrice();
            updateTotalRectangle(totalPrice, totalText);
            primaryStage.setScene(SnackBarScene.createSnackBarScene(primaryStage, totalText, totalPrice));
        });
        
    
        seatGrid.getChildren().forEach(node -> {
            if (node instanceof Button) {
                Button seatButton = (Button) node;
                seatButton.setOnAction(event -> {
                    handleSeatSelection(seatButton, movieName);
                    updateTotalPriceText(totalText);
                    updateSelectedSeatsText(selectedSeatsText);
                });
            }
        });
    
        Scene seatSelectionScene = new Scene(seatSelectionVBox, primaryStage.getWidth(), primaryStage.getHeight());
        primaryStage.setScene(seatSelectionScene);
    }
    
    private void updateSelectedSeatsText(Text selectedSeatsText) {
        String seats = String.join(", ", selectedSeats);
        selectedSeatsText.setText("Assentos Selecionados: " + seats);
    }

    private GridPane createSeatGrid(String movieName) {
        GridPane seatGrid = new GridPane();
        seatGrid.setAlignment(Pos.CENTER);
        seatGrid.setHgap(10);
        seatGrid.setVgap(10);

        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 10; j++) {
                Button seatButton = new Button((char) ('A' + i) + Integer.toString(j + 1));
                seatButton.setMinSize(40, 40);
                seatButton.setStyle("-fx-background-color: yellow;");
                seatGrid.add(seatButton, i, j);
            }
        }

        return seatGrid;
    }

    private void handleSeatSelection(Button seatButton, String movieName) {
        String seatName = seatButton.getText();

        if (selectedSeats.contains(seatName)) {
            selectedSeats.remove(seatName);
            seatButton.setStyle("-fx-background-color: yellow;");
        } else {
            selectedSeats.add(seatName);
            seatButton.setStyle("-fx-background-color: grey;");
        }
    }

    private void updateTotalPriceText(Text totalPriceText) {
        double totalPrice = calculateTotalPrice();
        totalPriceText.setText(String.format("Total: R$ %.2f", totalPrice));
    }

    private double calculateTotalPrice() {
        double totalPrice = selectedSeats.size() * PRICE_PER_SEAT;
        return totalPrice;
    }

    private String generateRandomMovieName() {
        String[] movieNames = {"Aventura Espacial", "O Segredo do Abismo", "Trama Fatal", "Em Busca do Destino", "No Limite da Honra", "Cidade dos Sonhos"};
        Random random = new Random();
        return movieNames[random.nextInt(movieNames.length)];
    }

    private String generateRandomSynopsis() {
        String[] synopses = {
                "Uma história emocionante de aventuras espaciais.",
                "Um drama envolvente com reviravoltas surpreendentes.",
                "Uma trama misteriosa cheia de suspense.",
                "Uma jornada épica em busca do destino."
        };
        Random random = new Random();
        return synopses[random.nextInt(synopses.length)];
    }

    private String generateRandomDirector() {
        String[] directors = {"Diretor 1", "Diretor 2", "Diretor 3", "Diretor 4", "Diretor 5"};
        Random random = new Random();
        return directors[random.nextInt(directors.length)];
    }

    private String generateRandomGenre() {
        String[] genres = {"Ação", "Comédia", "Drama", "Ficção Científica", "Suspense", "Romance"};
        Random random = new Random();
        return genres[random.nextInt(genres.length)];
    }

    private Text createSideMessage(String message) {
        Text text = new Text(message);
        text.setFont(Font.font(18));
        text.setFill(Color.WHITE);
        text.setEffect(new Glow(0.8));

        return text;
    }

    private Set<String> selectedSeats = new HashSet<>();
}

class SnackBarScene {

    public static Scene createSnackBarScene(Stage primaryStage, Text totalText, double totalPrice) {
    HBox snackBarBox = new HBox(10);
    snackBarBox.setAlignment(Pos.CENTER);
    snackBarBox.setStyle("-fx-background-color: #336699;"); 

    Button popcornButton = createSnackBarButton("Pipoca", primaryStage, totalText, totalPrice);
    Button cokeButton = createSnackBarButton("Coca-Cola", primaryStage, totalText, totalPrice);

    snackBarBox.getChildren().addAll(popcornButton, cokeButton);

    return new Scene(snackBarBox, primaryStage.getWidth(), primaryStage.getHeight());
}

private static Button createSnackBarButton(String text, Stage primaryStage, Text totalText, double totalPrice) {
    Button button = new Button(text);
    button.setStyle("-fx-background-color: lightgray;");
    button.setMinSize(150, 60);

    button.setOnAction(event -> {
        System.out.println("Você selecionou " + text + "!");
        primaryStage.setScene(DiscountScene.createDiscountScene(primaryStage, totalText, totalPrice));
    });

    return button;
}
}

class DiscountScene {

          private static double originalTotal; 
    private static double currentTotal;

    public static Scene createDiscountScene(Stage primaryStage, Text totalText, double totalPrice) { {
        VBox discountVBox = new VBox(20);
        discountVBox.setAlignment(Pos.CENTER);
        discountVBox.setStyle("-fx-background-color: #336699;");

        Text discountTitle = new Text("DESCONTOS ESPECIAIS(OPCIONAL)");
        discountTitle.setFont(Font.font(18));
        discountTitle.setFill(Color.BLUE);

        HBox discountButtons = new HBox(20);
        discountButtons.setAlignment(Pos.CENTER);

        
        totalText.setFont(Font.font(18));
        totalText.setFill(Color.BLUE);

        originalTotal = totalPrice;
        currentTotal = totalPrice;

        List<Button> discountButtonsList = new ArrayList<>();

Button studentButton = createDiscountButton("Estudante", 15, totalText, primaryStage, discountButtonsList);
discountButtonsList.add(studentButton);

Button seniorButton = createDiscountButton("Idoso", 20, totalText, primaryStage, discountButtonsList);
discountButtonsList.add(seniorButton);

Button disabilityButton = createDiscountButton("Deficiente", 20, totalText, primaryStage, discountButtonsList);
discountButtonsList.add(disabilityButton);

Button partnerButton = createDiscountButton("Parceiro Comercial", 25, totalText, primaryStage, discountButtonsList);
discountButtonsList.add(partnerButton);

        discountButtons.getChildren().addAll(studentButton, seniorButton, disabilityButton, partnerButton);

        Rectangle totalRectangle = new Rectangle(300, 100);
        totalRectangle.setFill(Color.BLACK);
        totalRectangle.setStroke(Color.BLUE);
        totalRectangle.setStrokeWidth(2);

        StackPane totalStackPane = new StackPane(totalRectangle, totalText);

        Button confirmDiscountButton = new Button("Confirmar Desconto");
        confirmDiscountButton.setOnAction(event -> {
           primaryStage.setScene(PaymentScene.createPaymentScene(primaryStage, totalText));



        });

        discountVBox.getChildren().addAll(discountTitle, discountButtons, totalStackPane, confirmDiscountButton);

        return new Scene(discountVBox, primaryStage.getWidth(), primaryStage.getHeight());
    }
}
private static Button createDiscountButton(String text, int discountPercentage, Text totalText, Stage primaryStage, List<Button> otherButtons) {
    Button button = new Button(text + "\n(" + discountPercentage + "%)");
    button.setStyle("-fx-background-color: lightgray;");
    button.setMinSize(150, 60);

    button.setOnAction(event -> {
        for (Button otherButton : otherButtons) {
            if (otherButton != button) {
                otherButton.setStyle("-fx-background-color: lightgray;");
            }
        }

        boolean isSelected = button.getStyle().contains("-fx-background-color: lightgreen;");
        if (isSelected) {
            button.setStyle("-fx-background-color: lightgray;");
            updateTotalRectangle(originalTotal, totalText);
        } else {
            button.setStyle("-fx-background-color: lightgreen;");
            double discountedTotal = applyDiscount(originalTotal, discountPercentage);
            updateTotalRectangle(discountedTotal, totalText);
        }
        System.out.println("Desconto aplicado para " + text + ": " + discountPercentage + "%");
    });

    return button;
}



    private static void updateTotalRectangle(double discountedTotal, Text totalText) {
        currentTotal = discountedTotal;
        totalText.setText("Total: R$ " + String.format("%.2f", currentTotal));
    }

    private static double applyDiscount(double originalTotal, int discountPercentage) {
        double discountFactor = 1 - (discountPercentage / 100.0);
        return originalTotal * discountFactor;
    }
}

    class TelaProcessamentoPagamento extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        primaryStage.setTitle("Processando Pagamento");

      
        Label label = new Label("Processando pagamento...");

       
        StackPane root = new StackPane();
        root.getChildren().add(label);

        
        Scene scene = new Scene(root, 500, 300);

       
        primaryStage.setScene(scene);

        
        primaryStage.show();
    }

    @Override
    public String toString() {
        return "TelaProcessamentoPagamento []";
    }
}