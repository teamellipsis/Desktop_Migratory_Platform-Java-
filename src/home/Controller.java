package home;

//import com.enigma.dynamic.DynamicApp;

import com.enigma.platform.ObjectInputStreamWithLoader;
import com.enigma.platform.servr;
import com.teamellipsis.dynamic.DynamicApp;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;

import static com.enigma.platform.ClassLoder.getclassloader;

public class Controller implements Initializable {
    private WebView browser;
    private DynamicApp dapp;
    private servr server;
    static  servr sserver;
    private String appPath;
    private Desktop desktop = Desktop.getDesktop();
    @FXML
    private VBox pnItems = null;

    @FXML
    private VBox fillist = null;
    @FXML
    private Button loadapp;

    @FXML
    private Button btnGet;

    @FXML
    private Button closeapp;

    @FXML
    private Button btnSave;

    @FXML
    private Button btnSend;

    @FXML
    private Button btnChangeDir;

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
        btnSave.setVisible(false);
        btnSend.setVisible(false);

//        openapp.setVisible(false);
        String file_name = "/home/shehan/Desktop/txt/filepath.txt";
        File f = new File(file_name);
        if(!f.exists()){
            try {
                f.createNewFile();
                choosefile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
//        File file = new File(file_name);
//        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter(file_name))) {
//            String fileContent = "This is a sample text.";
//            bufferedWriter.write(fileContent);
//        } catch (IOException e) {
//
//        }
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(file_name))) {
            String line = bufferedReader.readLine();
            System.out.println(line);
            File appdir = new File(line);
            if(!appdir.exists()){
                System.out.println("app folder is not exist");
                choosefile();
//                try {
//                    f.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }else{
                appPath=line;
                getfiles();
            }
//            System.out.println(line);
        } catch (FileNotFoundException e) {
            // exception handling
        } catch (IOException e) {
            // exception handling
        }

//        choosefile();
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
        System.out.println(actionEvent.getSource());
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
                    showApplicationList();
                }
            });

        }
        if (actionEvent.getSource() == btnSave) {
            save_app();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success...");
            alert.setHeaderText("Appllication is saved successfully");
            alert.setContentText("You can send application now !");
            alert.showAndWait().ifPresent(rs -> {
                if (rs == ButtonType.OK) {
                    System.out.println("Pressed OK.");
                }
            });
        }
        if (actionEvent.getSource() == loadapp) {
            showApplicationList();

        }
        if(actionEvent.getSource()==btnSend)
        {
//            sendalert();
        }
        if(actionEvent.getSource()==btnGet)
        {
            try {
                sendalert();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(actionEvent.getSource()==btnChangeDir)
        {

            System.out.println("changegir");
            choosefile();
//
        }
    }
    public void showApplicationList(){
        try {
            close_server();
        } catch (InterruptedException | IOException e) {
            e.printStackTrace();
        }
        btnChangeDir.setVisible(true);
        btnGet.setVisible(true);
        closeapp.setVisible(false);
        btnSave.setVisible(false);
        btnSend.setVisible(false);
        pnlOrders.setStyle("-fx-background-color : #464F67");
        pnlOrders.toFront();
        getfiles();
    }
    public void loadobject() throws IOException, ClassNotFoundException {
        load_app();
//        Alert alert = new Alert(Alert.AlertType.INFORMATION);
//        alert.setTitle("Success...");
//        alert.setHeaderText("Appllication is loaded successfully");
//        alert.setContentText("You can open application now !");
//        alert.showAndWait().ifPresent(rs -> {
//            if (rs == ButtonType.OK) {
//                System.out.println("Pressed OK.");
//                pnItems.getChildren().removeAll(browser);
//            }
//        });
        pnlOrders.setStyle("-fx-background-color : #464F67");
        pnlOrders.toFront();
        getfiles();
//        openapp.setVisible(true);
    }

    public void showapp(){
        pnlOverview.setStyle("-fx-background-color : #02030A");
        pnlOverview.toFront();
        open_app();
        closeapp.setVisible(true);
        btnSave.setVisible(true);
        btnSend.setVisible(true);
        btnChangeDir.setVisible(false);
        btnGet.setVisible(false);
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
        pnItems.getChildren().clear();
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
        sserver=server;
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
    public static void close_server() throws IOException, InterruptedException {
        if(sserver!=null){
            sserver.stop();
        }

    }

    public void getfiles(){
        ListView<String> list = new ListView<String>();
        File dir = new File(appPath);
        File[] fileNames = dir.listFiles();

        ObservableList<String> data = FXCollections.observableArrayList();
        HashMap<String, String> args= new HashMap<String, String>();

        for(File file : fileNames){
            data.add(file.getName());
            args.put(file.getName(),file.getAbsolutePath());
        }
        list.setItems(data);

//        list.setOnMouseMoved(e->{
//            System.out.println("ssasasassasasasa");;
//        });
        list.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<String>() {
                    public void changed(ObservableValue<? extends String> ov,
                                        String old_val, String new_val) {

                        System.out.println(args.get(new_val));
                        String selectedItm = detaildialog(new_val);
                        if(selectedItm.equals("Select Option")){
                            System.out.println("not selected");
                        }else if(selectedItm.equals("Open App")){
                            System.out.println("open selected");
                            try {
                                loadobject();
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }finally {
                                showapp();
                            }
                        }else if(selectedItm.equals("Send App")){

                        }else if(selectedItm.equals("Save App")){

                        }else if(selectedItm.equals("Close App")){

                        }
                        else{

                        }

//                        label.setText(new_val);
//                        label.setTextFill(Color.web(new_val));
                    }
                });

        fillist.getChildren().clear();
        fillist.setPadding(new Insets(5));
        fillist.setSpacing(5);
        fillist.getChildren().addAll(list);

    }

    public void choosefile(){
        final DirectoryChooser fileChooser = new DirectoryChooser();
//        final FileChooser fileChooser = new FileChooser();
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        File file = fileChooser.showDialog(stage);
        stage.show();
        if (file != null) {
            stage.close();
            openFile(file);

        }
        stage.close();
    }

    private void loadStage(String fxml) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxml));
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.getIcons().add(new Image("/home/icons/icon.png"));
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void openFile(File file) {
        System.out.println(file.getAbsolutePath());
        appPath=file.getAbsolutePath();

        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("/home/shehan/Desktop/txt/filepath.txt"))) {
            String fileContent = file.getAbsolutePath();
            bufferedWriter.write(fileContent);
        } catch (IOException e) {

        }finally {
            getfiles();
        }
    }

    String detaildialog(String filename){
        Stage s = new Stage();
        TilePane r = new TilePane();
        s.setTitle("creating choice dialog");
        Button b = new Button("click");
        String days[] = { "Select Option","Open App","Send App","Save App","Close App" };
        Label l = new Label(days[1] + " selected");
        ChoiceDialog d = new ChoiceDialog(days[0], days);
        d.setHeaderText("Choose Action for :"+ filename);
        d.setContentText("Select an option to continue");

        Optional<String> result = d.showAndWait();
        String selecteditem = "Select Option";
        if ( result.isPresent() )
        {
            System.out.println(selecteditem);
            selecteditem =d.getSelectedItem().toString();
        }
//        d.showAndWait().ifPresent(rs -> {
//            System.out.println(rs+"sasasasas" );
//            if (rs == ButtonType.OK) {
//                System.out.println("Pressed OK.");
//                pnItems.getChildren().removeAll(browser);
//            }
//        });


        Scene sc = new Scene(r, 200, 200);
        s.setScene(sc);
        s.show();
        getfiles();
        s.close();
    return selecteditem;
    }

    public void sendalert() throws Exception {

//        try {
//            String ip= InetAddress.getLocalHost().getHostAddress();
//            System.out.println(ip);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }





        ServerSocket serverSocket = null;
        boolean serveropen=false;
        int port = 0;
        try {
            serverSocket = new ServerSocket(0);
            port = serverSocket.getLocalPort();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Waiting for clients....");
            alert.setHeaderText("ip : "+getipv4()+"\n  port : "+port);
            alert.setContentText("waiting.. ");
            ButtonType buttonTypeOne = new ButtonType("Wait", ButtonBar.ButtonData.OK_DONE);
            ButtonType buttonTypeCancel = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);

            alert.getButtonTypes().setAll(buttonTypeOne, buttonTypeCancel);

            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(result.get());
            if (result.get() == buttonTypeOne){
                alert.close();
                serverSocket.close();

            } else {
                // ... user chose CANCEL or closed the dialog
            }
        } catch (IOException ex) {
            System.out.println("Can't setup server on this port number. ");
        }

        Socket socket = null;

        while(true){
            try {
                socket = serverSocket.accept();
            } catch (IOException ex) {
                System.out.println("Can't accept client connection. ");
            }
            if(socket!=null){
                BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                String message1 = input.readLine();
                System.out.println(message1);
                File file = new File("/home/shehan/Desktop/fyp/fileserver/src/main/java/WelcomeCourse.mp4");
                long length = file.length();

                if(message1.equals("get file")){
                    System.out.println("shehan");
                    byte[] bytes = new byte[(int)length];
                    InputStream fin = new FileInputStream(file);
                    BufferedInputStream bis1 = new BufferedInputStream(fin);
                    bis1.read(bytes, 0, bytes.length);

                    DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                    dOut.writeInt(bytes.length); // write length of the message
                    dOut.write(bytes);
                    System.out.println("Done.");

                    socket.close();
                    serverSocket.close();
                    break;
                }else{
                    PrintWriter output= new PrintWriter(socket.getOutputStream());
                    output.println(Integer.toString((int) file.length()));
                    output.flush();
                    System.out.println("Done.");

                    BufferedReader input11 = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    String message = input11.readLine();
                    System.out.println(message);

                    String filename = file.getName();
                    output.println(filename);
                    output.flush();
                    System.out.println("Done.");

                    socket.close();

                }
            }

        }


    }

    public String getipv4(){
        String ip ="";
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iface = interfaces.nextElement();
                // filters out 127.0.0.1 and inactive interfaces
                if (iface.isLoopback() || !iface.isUp())
                    continue;

                Enumeration<InetAddress> addresses = iface.getInetAddresses();
                while(addresses.hasMoreElements()) {
                    InetAddress addr = addresses.nextElement();

                    // *EDIT*
                    if (addr instanceof Inet6Address) continue;

                    ip = addr.getHostAddress();
                    System.out.println(ip);
                }
            }
        } catch (SocketException e) {
            throw new RuntimeException(e);
        }
        return ip;
    }


}
