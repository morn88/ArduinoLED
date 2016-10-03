package sample;

import com.fazecast.jSerialComm.SerialPort;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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

    @Override
    public void start(Stage primaryStage) throws Exception{

        SerialPort portName = SerialPort.getCommPort("COM3");

        VBox root = new VBox();
        root.setStyle("-fx-background-color: lightsteelblue");
//        root.setId("pane");


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
//        btnOn.setId("led");

        Image sound = new Image("sound.png");
        ToggleButton tBtMus = new ToggleButton();
        tBtMus.setShape(new Circle(30));
        tBtMus.setPrefSize(150, 150);
        tBtMus.setStyle("-fx-base: lightblue");
        tBtMus.setGraphic(new ImageView(sound));
//        tBtMus.setId("sound");

        tBtMus.setOnAction(event -> {
            if(tBtMus.isSelected()){
                tBtMus.setEffect(shadow_mus);
                portName.openPort();
                byte[] buff = {50};
                portName.writeBytes(buff, 1);
            }
            else {
                tBtMus.setEffect(null);
                portName.openPort();
                byte[] buff = {51};
                portName.writeBytes(buff, 1);
            }
        });

        btnOn.setOnAction(event -> {
            portName.openPort();
            if (!ON) {
                ON = true;
                btnOn.setStyle("-fx-base: lightgreen");
                btnOn.setEffect(shadow_LED);
                byte[] buff = {'1'};
                portName.writeBytes(buff, 1);
                System.out.println(buff[0]);
            } else {
                ON = false;
                btnOn.setStyle(null);
                btnOn.setEffect(null);
                byte[] buff = {'0'};
                portName.writeBytes(buff, 1);
                System.out.println(buff[0]);
            }
        });


        root.setAlignment(Pos.CENTER);

        root.getChildren().addAll(btnOn, label, tBtMus, label_sound);
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 400, 550);
//        scene.getStylesheets().addAll(this.getClass().getResource("sample/style.css").toExternalForm());
        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {

        launch(args);
    }
}


