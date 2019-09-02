package home;

//import com.enigma.dynamic.DynamicApp;

import com.enigma.platform.ObjectInputStreamWithLoader;
import com.enigma.platform.servr;
import com.teamellipsis.dynamic.DynamicApp;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ResourceBundle;

import static com.enigma.platform.ClassLoder.getclassloader;

public class Controller implements Initializable {
    private WebView browser;
    private DynamicApp dapp;
    private servr server;
    @FXML
    private VBox pnItems = null;
    @FXML
    private Button loadapp;

    @FXML
    private Button openapp;

    @FXML
    private Button closeapp;

    @FXML
    private Button btnMenus;

    @FXML
    private Button btnPackages;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOrders.setStyle("-fx-background-color : #464F67");
        pnlOrders.toFront();
        closeapp.setVisible(false);
        openapp.setVisible(false);
//        Node[] nodes = new Node[10];
//        for (int i = 0; i < nodes.length; i++) {
//            try {
//
//                final int j = i;
//                nodes[i] = FXMLLoader.load(getClass().getResource("Item.fxml"));
//
//                //give the items some effect
//
//                nodes[i].setOnMouseEntered(event -> {
//                    nodes[j].setStyle("-fx-background-color : #0A0E3F");
//                });
//                nodes[i].setOnMouseExited(event -> {
//                    nodes[j].setStyle("-fx-background-color : #02030A");
//                });
//                pnItems.getChildren().add(nodes[i]);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }

    }


    public void handleClicks(ActionEvent actionEvent) throws IOException, ClassNotFoundException {
        if (actionEvent.getSource() == closeapp) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Warning...");
            alert.setHeaderText("Do you want close the app ?");
//            alert.setContentText("");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
//                    System.out.println("Pressed OK.");
                    try {
                        close_app();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    pnItems.getChildren().removeAll(browser);
                    pnlCustomer.setStyle("-fx-background-color : #1620A1");
                    pnlCustomer.toFront();
                    closeapp.setVisible(false);
                }
            });

        }
        if (actionEvent.getSource() == btnMenus) {
//            final FileChooser fileChooser = new FileChooser();
//            File file = fileChooser.showOpenDialog();
//            if (file != null) {
//                openFile(file);
//            }
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == loadapp) {
            load_app();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success...");
            alert.setHeaderText("Appllication is loaded successfully");
            alert.setContentText("You can open application now !");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                    pnItems.getChildren().removeAll(browser);
                }
            });
            pnlOrders.setStyle("-fx-background-color : #464F67");
            pnlOrders.toFront();
            openapp.setVisible(true);
        }
        if(actionEvent.getSource()==openapp)
        {
//            pnlOrders.setStyle("-fx-background-color : #464F67");
//            pnlOrders.toFront();
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
            open_app();
            closeapp.setVisible(true);
        }
    }
    public void loadwebpage(){
        browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.setJavaScriptEnabled(true);
        try {
            File file = new File("resources/build/index.html");
            URL url = file.toURI().toURL();
            // file:/C:/test/a.html
            System.out.println("Local URL: " + url.toString());
            webEngine.load(url.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        pnItems.setPadding(new Insets(5));
        pnItems.setSpacing(5);
        pnItems.getChildren().addAll(browser);


    }
    public void load_app() throws IOException, ClassNotFoundException {
        InputStream fileIn1 = new FileInputStream("resources/todo.ser");
//        InputStream in = new ObjectInputStream(fileIn);
        ObjectInputStreamWithLoader obl = new ObjectInputStreamWithLoader(fileIn1,getclassloader());
        dapp = (DynamicApp) obl.readObject();
        obl.close();
//        String[] arr6 = {"0","test1","test_task","2001-2-21"};
//        dapp.execute(arr6);
//        dapp.execute(arr6);
    }
    public void open_app(){
        server = new servr(dapp);
        server.start();
        loadwebpage();
    }
    public void close_app() throws IOException, InterruptedException {
        if(server!=null){
            server.stop();
            System.out.println("close server");
        }
        save_app();
    }
    public void save_app(){
        if(dapp!=null){
            try {
                FileOutputStream fileOut = new FileOutputStream("resources/app.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(dapp);
                out.close();
                fileOut.close();
                System.out.printf("Serialized data is saved in /resources/app.ser");
            } catch (IOException i) {
                i.printStackTrace();
            }

        }

    }
}
