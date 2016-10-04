package sample;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ToggleButton;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class Main extends Application {
    private boolean ON = false;
    static SerialPort chosenPort;


    @Override
    public void start(Stage primaryStage) throws Exception{

        VBox root = new VBox();
        root.setStyle("-fx-background-color: lightsteelblue");
//        root.setId("pane");

        ComboBox<String> portList = new ComboBox<>();
        SerialPort[] portNames = SerialPort.getCommPorts();
        for (SerialPort portName : portNames) {
            portList.getItems().addAll(portName.getSystemPortName());
        }

        Button connect = new Button("Connect");

        Label label = new Label();
        label.setPrefSize(150,80);
        label.setText("LED ON/OFF");
        label.setTextFill(Color.GREEN);
        label.setAlignment(Pos.CENTER);

        Label label_sound = new Label("Sound ON/OFF");
        label_sound.setPrefSize(150, 80);
        label_sound.setAlignment(Pos.CENTER);

        DropShadow shadow_mus = new DropShadow();
        shadow_mus.setColor(Color.AQUAMARINE);
        shadow_mus.setRadius(100);

        DropShadow shadow_LED = new DropShadow();
        shadow_LED.setColor(Color.LIGHTGREEN);
        shadow_LED.setRadius(75);

        Image led = new Image("led.png");
        Button btnOn = new Button();
        btnOn.setGraphic(new ImageView(led));
        btnOn.setShape(new Circle(30));
        btnOn.setPrefSize(150, 150);
        btnOn.setDisable(true);
//        btnOn.setId("led");

        Image sound = new Image("sound.png");
        ToggleButton tBtMus = new ToggleButton();
        tBtMus.setShape(new Circle(30));
        tBtMus.setPrefSize(150, 150);
        tBtMus.setStyle("-fx-base: lightblue");
        tBtMus.setGraphic(new ImageView(sound));
        tBtMus.setDisable(true);
//        tBtMus.setId("sound");

        connect.setOnAction(event -> {
            if (connect.getText().equals("Connect")) {
                chosenPort = SerialPort.getCommPort(portList.getValue());
                if (chosenPort.openPort()) {
                    connect.setText("Disconnect");
                    portList.setDisable(true);
                    btnOn.setDisable(false);
                    tBtMus.setDisable(false);
                }
            } else {
                chosenPort.closePort();
                portList.setDisable(false);
                connect.setText("Connect");
                btnOn.setDisable(true);
                tBtMus.setDisable(true);
            }

        });

        tBtMus.setOnAction(event -> {
            if(tBtMus.isSelected()){
                tBtMus.setEffect(shadow_mus);
                byte[] buff = {50};
                chosenPort.writeBytes(buff, 1);
            }
            else {
                tBtMus.setEffect(null);
                byte[] buff = {51};
                chosenPort.writeBytes(buff, 1);
            }
        });

        btnOn.setOnAction(event -> {
            if (!ON) {
                ON = true;
                btnOn.setStyle("-fx-base: lightgreen");
                btnOn.setEffect(shadow_LED);
                byte[] buff = {'1'};
                chosenPort.writeBytes(buff, 1);
                System.out.println(buff[0]);
            } else {
                ON = false;
                btnOn.setStyle(null);
                btnOn.setEffect(null);
                byte[] buff = {'0'};
                chosenPort.writeBytes(buff, 1);
                System.out.println(buff[0]);
            }
        });


        root.setAlignment(Pos.CENTER);
        root.setSpacing(10);

        root.getChildren().addAll(portList, connect, btnOn, label, tBtMus, label_sound);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 400, 600);
//        scene.getStylesheets().addAll(this.getClass().getResource("sample/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}


