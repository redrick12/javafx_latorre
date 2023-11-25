import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class PaymentScene extends Application {

    private static double totalPrice;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {

        totalPrice = 100.0;

        primaryStage.setTitle("Forma de Pagamento");

        Text totalText = new Text("Total: R$ " + String.format("%.2f", totalPrice));
        totalText.setFont(Font.font(18));
        totalText.setFill(Color.WHITE);

        Scene paymentScene = createPaymentScene(primaryStage, totalText);
        primaryStage.setScene(paymentScene);
        primaryStage.show();
    }

    public static Scene createPaymentScene(Stage primaryStage, Text totalText) {
        VBox paymentVBox = new VBox(20);
        paymentVBox.setAlignment(Pos.CENTER);
        paymentVBox.setStyle("-fx-background-color: #336699;"); // Adicione o estilo de fundo azul

        Text paymentTitle = new Text("Forma de Pagamento");
        paymentTitle.setFont(Font.font(18));
        paymentTitle.setFill(Color.BLUE);

        ToggleGroup paymentToggleGroup = new ToggleGroup();

        RadioButton debitRadioButton = createPaymentRadioButton("Débito", paymentToggleGroup);
        RadioButton creditRadioButton = createPaymentRadioButton("Crédito", paymentToggleGroup);
        RadioButton pixRadioButton = createPaymentRadioButton("PIX", paymentToggleGroup);

        Button confirmPaymentButton = new Button("Confirmar Pagamento");

        confirmPaymentButton.setOnAction(event -> {
            String selectedPayment = getSelectedPayment(paymentToggleGroup);
            System.out.println("Forma de pagamento selecionada: " + selectedPayment);
    
            showProcessingScreen(primaryStage, totalText);
        });

        Rectangle totalRectangle = new Rectangle(300, 100);
        totalRectangle.setFill(Color.BLACK);
        totalRectangle.setStroke(Color.BLUE);
        totalRectangle.setStrokeWidth(2);

        StackPane totalStackPane = new StackPane(totalRectangle, totalText);

        paymentVBox.getChildren().addAll(paymentTitle, debitRadioButton, creditRadioButton, pixRadioButton,
                totalStackPane, confirmPaymentButton);

        return new Scene(paymentVBox, primaryStage.getWidth(), primaryStage.getHeight());
    }

    private static void showProcessingScreen(Stage primaryStage, Text totalText) {
        TelaProcessamentoPagamento telaProcessamento = new TelaProcessamentoPagamento();
        telaProcessamento.start(primaryStage);
    }

    private static RadioButton createPaymentRadioButton(String text, ToggleGroup toggleGroup) {
        RadioButton radioButton = new RadioButton(text);
        radioButton.setStyle("-fx-text-fill: white;");
        radioButton.setToggleGroup(toggleGroup);
        return radioButton;
    }

    private static String getSelectedPayment(ToggleGroup toggleGroup) {
        RadioButton selectedRadioButton = (RadioButton) toggleGroup.getSelectedToggle();
        if (selectedRadioButton != null) {
            return selectedRadioButton.getText();
        } else {
            return "Nenhuma opção selecionada";
        }
    }
}