package home;

//import com.enigma.dynamic.DynamicApp;

import com.enigma.platform.ObjectInputStreamWithLoader;
import com.enigma.platform.servr;
import com.teamellipsis.dynamic.DynamicApp;
import javafx.application.Platform;
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
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.stage.*;
import javafx.util.Pair;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import static com.enigma.platform.ClassLoder.getclassloader;

public class Controller implements Initializable {
    private WebView browser;
    private DynamicApp dapp;
    private servr server;
    static  servr sserver;
    private String appPath;
    private String appsDir;
    private String sentDir;
    private String todoPath;
    PrintWriter output;
    String messagefilelength;
    String filename;
    private Desktop desktop = Desktop.getDesktop();
    private int port;
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
    @FXML
    private Label lblIP;
    @FXML
    private Label lblPort;
    @FXML
    private Label lblApplication;
    @FXML
    private Label lblStatus;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        pnlOrders.setStyle("-fx-background-color : #464F67");
        pnlOrders.toFront();
        closeapp.setVisible(false);
        btnSave.setVisible(false);
        btnSend.setVisible(false);

//        openapp.setVisible(false);
        String file_name = "resources/filepath.txt";
        File f = new File(file_name);
        if(!f.exists()){
            try {
                f.createNewFile();
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Need Action");
                alert.setHeaderText("Application path is not set");
                alert.setContentText("Please select a directiory for application");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                        choosefile();
                    }
                });

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
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Need Action");
                alert.setHeaderText("Application path is not set");
                alert.setContentText("Please select a directiory for application");
                alert.showAndWait().ifPresent(rs -> {
                    if (rs == ButtonType.OK) {
                        System.out.println("Pressed OK.");
                        choosefile();
                    }else{
                        System.out.println("not sets aapp");
                    }
                });
//                choosefile();
//                try {
//                    f.createNewFile();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }else{
                appPath=line;
                appsDir=appPath+"/apps";
                sentDir=appPath+"/sentapps";
                File appsDirectory = new File(appPath+"/apps");
                File sentApps = new File(appPath+"/sentapps");
                if (! appsDirectory.exists()){
                    appsDirectory.mkdir();
                    appsDir=appPath+"/apps";
                }
                if (! sentApps.exists()){
                    sentApps.mkdir();
                    sentDir=appPath+"/sentapps";
                }
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
                getapp();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if(actionEvent.getSource()==btnChangeDir)
        {

//            zipfile(new File("/home/shehan/Desktop/ziptest/todoapp"),new File("/home/shehan/Desktop/ziptest/test.zip"));
//            System.out.println("changegir");
            choosefile();
//            unzip(new File("/home/shehan/Desktop/ziptest/test.zip"),new File("/home/shehan/Desktop/ziptest/extract"));
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
    public void loadobject(String path) throws IOException, ClassNotFoundException {
        load_app(path);
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

    public void showapp(String path){
        pnlOverview.setStyle("-fx-background-color : #02030A");
        pnlOverview.toFront();
        open_app(path);
        closeapp.setVisible(true);
        btnSave.setVisible(true);
        btnSend.setVisible(true);
        btnChangeDir.setVisible(false);
        btnGet.setVisible(false);
    }

    public void loadwebpage(String path){
        browser = new WebView();
        final WebEngine webEngine = browser.getEngine();
        webEngine.setJavaScriptEnabled(true);
        try {
            File file = new File(path+"/build/index.html");
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
    public void load_app(String path) throws IOException, ClassNotFoundException {
        InputStream fileIn1 = new FileInputStream(path+"/app.ser");
//        InputStream in = new ObjectInputStream(fileIn);
        ObjectInputStreamWithLoader obl = new ObjectInputStreamWithLoader(fileIn1,getclassloader(path));
        dapp = (DynamicApp) obl.readObject();
        obl.close();
//        String[] arr6 = {"0","test1","test_task","2001-2-21"};
//        dapp.execute(arr6);
//        dapp.execute(arr6);
    }
    public void open_app(String path){
        server = new servr(dapp);
        sserver=server;
        server.start();
        loadwebpage(path);
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
                FileOutputStream fileOut = new FileOutputStream(todoPath+"/app.ser");
                ObjectOutputStream out = new ObjectOutputStream(fileOut);
                out.writeObject(dapp);
                out.close();
                fileOut.close();
                System.out.printf("Serialized data is saved in "+todoPath+"/app.ser");
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
        System.out.println(appsDir);
        File dir = new File(appsDir);

        File[] fileNames = dir.listFiles();

        ObservableList<String> data = FXCollections.observableArrayList();
        HashMap<String, String> args= new HashMap<String, String>();

        for(File file : fileNames){
            if(file.isDirectory()){
                data.add(file.getName());
                args.put(file.getName(),file.getAbsolutePath());
            }

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
                        String todopath = args.get(new_val);
                        todoPath= args.get(new_val);
                        String selectedItm = detaildialog(new_val);
                        if(selectedItm.equals("Select Option")){
                            System.out.println("not selected");
                        }else if(selectedItm.equals("Open App")){
                            try {
                                loadobject(todopath);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }finally {
                                showapp(todopath);
                            }
                        }else if(selectedItm.equals("Send App")){
                            try {
                                zipfile(new File(todopath),new File(sentDir+"/"+new_val+".zip"));
                                sendfile(todopath);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else if(selectedItm.equals("Reset")){

                        }else if(selectedItm.equals("Delete")){

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
        File appsDirectory = new File(file+"/apps");
        File sentApps = new File(file+"/sentapps");
        if (! appsDirectory.exists()){
            appsDirectory.mkdir();
            appsDir=file+"/apps";
        }
        if (! sentApps.exists()){
            sentApps.mkdir();
            sentDir=file+"/sentapps";
        }
        try(BufferedWriter bufferedWriter = new BufferedWriter(new FileWriter("resources/filepath.txt"))) {
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
        String days[] = { "Select Option","Open App","Send App","Reset","Delete"};
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

    public void sendfile(String filename) throws Exception {
        File sendtodo =  new File(filename);
        String fileName = sendtodo.getName();;
        int pos = fileName.lastIndexOf(".");
        if (pos > 0 && pos < (fileName.length() - 1)) { // If '.' is not the first or last character.
            fileName = fileName.substring(0, pos);
        }
        System.out.println(fileName);
        String filesending = sentDir+"/"+fileName+".zip";
        pnlMenus.setStyle("-fx-background-color : #464F67");
        pnlMenus.toFront();
        lblIP.setText(getipv4());
        lblApplication.setText(fileName+".zip");
        lblPort.setText("8000");
//        try {
//            String ip= InetAddress.getLocalHost().getHostAddress();
//            System.out.println(ip);
//        } catch (UnknownHostException e) {
//            e.printStackTrace();
//        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {


                try {

                ServerSocket serverSocket = null;
                boolean serveropen=false;
                int port = 0;
                try {
                    serverSocket = new ServerSocket(0);
                    serveropen=true;
                    port = serverSocket.getLocalPort();

                    System.out.println(port);
                    int finalPort = port;
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            lblPort.setText(Integer.toString(finalPort));
                        }
                    });


                } catch (IOException ex) {
                    System.out.println("Can't setup server on this port number. ");
                }

                Socket socket = null;

                while(serveropen){
                    System.out.println("server open");
                    try {
                        socket = serverSocket.accept();
                        Platform.runLater(new Runnable() {
                            @Override
                            public void run() {
                                lblStatus.setText("Client is Connected..");
                            }
                        });
//
                    } catch (IOException ex) {
                        System.out.println("Can't accept client connection. ");
                    }
                    if(socket!=null){
                        BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                        String message1 = input.readLine();
                        System.out.println(message1);
                        File file = new File(filesending);
                        long length = file.length();

                        if(message1.equals("get file")){
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblStatus.setText("Sending file..");
                                }
                            });
                            System.out.println("shehan");
                            byte[] bytes = new byte[(int)length];
                            InputStream fin = new FileInputStream(file);
                            BufferedInputStream bis1 = new BufferedInputStream(fin);
                            bis1.read(bytes, 0, bytes.length);

                            DataOutputStream dOut = new DataOutputStream(socket.getOutputStream());
                            dOut.writeInt(bytes.length); // write length of the message
                            dOut.write(bytes);
                            Platform.runLater(new Runnable() {
                                @Override
                                public void run() {
                                    lblStatus.setText("Done");
                                }
                            });
//


                            socket.close();
                            serverSocket.close();
                            System.out.println("server closed..........");
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
            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();

            }finally{
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success...");
                            alert.setHeaderText("File is sent successfully");
                            alert.setContentText("You can open application now !");
                            alert.showAndWait().ifPresent(rs -> {
                                if (rs == ButtonType.OK) {
                                    showApplicationList();
                                }
                            });
                        }
                    });


                }

            }
        });
        thread.start();




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

    public void packageapp(){

    }

    public  void getapp(){
        // Create the custom dialog.
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Connect to remote device ");
        dialog.setHeaderText("Enter ip and port ...");
        dialog.setContentText("Select an option to continue");
        // Set the button types.
        ButtonType loginButtonType = new ButtonType("OK", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

        GridPane gridPane = new GridPane();
        gridPane.setHgap(10);
        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 150, 10, 10));

        TextField from = new TextField();

        from.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.matches("[0-9.]*")) {
                    if(newValue!=""){
                        try{
                            int value = Integer.parseInt(newValue);
                        }catch(NumberFormatException e){

                        }

                    }

                } else {
                    from.setText(oldValue);
                }
            }
        });
        from.setPromptText("Ip");
        TextField to = new TextField();
        to.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue,
                                String newValue) {
                if (newValue.matches("[0-9]*")) {
                    if(newValue!=""){
                        try{
                            int value = Integer.parseInt(newValue);
                        }catch(NumberFormatException e){

                        }

                    }

                } else {
                    to.setText(oldValue);
                }
            }
        });
        to.setPromptText("Port");
        gridPane.add(new Label("Ip :"), 0, 0);
        gridPane.add(from, 1, 0);
        gridPane.add(new Label("Port:"), 2, 0);
        gridPane.add(to, 3, 0);

        dialog.getDialogPane().setContent(gridPane);

        // Request focus on the username field by default.
        Platform.runLater(() -> from.requestFocus());

        // Convert the result to a username-password-pair when the login button is clicked.
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == loginButtonType) {
                return new Pair<>(from.getText(), to.getText());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(pair -> {

            System.out.println("From=" + from.getText() + ", To=" + to.getText());

        String ip = from.getText();
        int port = Integer.parseInt(to.getText());

        Thread thread = new Thread(new Runnable() {
            Socket sock = null;
            @Override
            public void run() {
                try {

                    sock = new Socket(ip, port);
                    System.out.println("Connecting...");
                    BufferedReader input = new BufferedReader(new InputStreamReader(sock.getInputStream()));
//                    while (input.readLine()!= null) {
//                        messagefilelength = input.readLine();
//                        System.out.println(messagefilelength);

                    output = new PrintWriter(sock.getOutputStream());
                    output.println("send me file");
                    output.flush();
//                    }
                    messagefilelength = input.readLine();

//                    System.out.println(messagefilelength);
                    output = new PrintWriter(sock.getOutputStream());
                    output.println("send me file");
                    output.flush();
                    filename = input.readLine();

                } catch (UnknownHostException e) {
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    Platform.runLater(new Runnable() {
                        @Override
                        public void run() {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Success...");
                            alert.setHeaderText("File is sent successfully");
                            alert.setContentText(filename + messagefilelength);
                            alert.showAndWait().ifPresent(rs -> {
                                if (rs == ButtonType.OK) {
//                                    showApplicationList();
                                }
                            });

                            Thread thread = new Thread(new Runnable() {
                                Socket sock = null;

                                @Override
                                public void run() {
                                    try {

                                        getfile(ip ,port);

                                    } catch (UnknownHostException e) {
                                        e.printStackTrace();
                                    } catch (FileNotFoundException e) {
                                        e.printStackTrace();
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    } finally {
                                        Platform.runLater(new Runnable() {
                                            @Override
                                            public void run() {
                                                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                                                alert.setTitle("Success...");
                                                alert.setHeaderText("File is sent successfully");
                                                alert.setContentText("file is saved on app path");
                                                alert.showAndWait().ifPresent(rs -> {
                                                    if (rs == ButtonType.OK) {
//                                    showApplicationList();
                                                    }
                                                });
                                            }});
                                        if (sock != null) {
                                            try {
                                                sock.close();
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            });
                            thread.start();
                        }
                    });

                    if (sock != null) {
                        try {
                            sock.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });

        thread.start();
        });
    }









    public void writeByte(byte[] bytes) {

//        File file = Environment.getExternalStorageDirectory();
        File save = new File(sentDir+"/"+filename);

        try {
            OutputStream out = new FileOutputStream(save.getAbsolutePath());
            out.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            try {
                unzip(save,new File(appsDir));
            } catch (IOException e) {
                e.printStackTrace();
            }finally {
                Platform.runLater(new Runnable() {
                                      @Override
                                      public void run() {
                                          showApplicationList();
                                      }
                                  });

            }
        }


    }

    public void getfile(String ip , int port) throws IOException {
        Socket sock = new Socket(ip, port);
        System.out.println("Connecting...");
        PrintWriter output = new PrintWriter(sock.getOutputStream());
        output.println("get file");
        output.flush();

        DataInputStream dIn = new DataInputStream(sock.getInputStream());   // read length of incoming message
        int length = dIn.readInt();
        if(length>0) {
            byte[] message = new byte[length];
            byte[] message1=new byte[length];
            dIn.readFully(message1, 0, message1.length);
            System.out.println(message1);
//            messagees=message1;
            writeByte(message1);


        }


    }

    public void zipfile(File src ,File dest){
        try {
            FileOutputStream fileOut =new  FileOutputStream(dest);
            ZipOutputStream zipOut = new  ZipOutputStream(new BufferedOutputStream(fileOut));
            if(src.isDirectory()){
                zipSubFolder(zipOut, src, src.getParent().length());
                zipOut.close();
            }else{
                zipOut.close();
            }
        } catch (Exception e) {
        e.printStackTrace();

         }
    }
    public void zipSubFolder(ZipOutputStream zipOut,File dir, int basePathLength) throws IOException{
        int BUFFER_SIZE = 2048;
        File[] fileList = dir.listFiles();
        BufferedInputStream bufferedInputStream= null;
        for (File file : fileList) {
            if (file.isDirectory()) {
                if (file.listFiles().length!=0) {
                    zipSubFolder(zipOut, file, basePathLength);

                }else{
                    String relativePath = file.getPath().substring(basePathLength).substring(1) + "/";
                    ZipEntry entry = new ZipEntry(relativePath);
                    entry.setTime(file.lastModified());
                    zipOut.putNextEntry(entry);
                }
            }else{

                byte[] data =new byte[BUFFER_SIZE];
                String relativePath = file.getAbsolutePath().substring(basePathLength).substring(1);
                FileInputStream fileInputStream =new  FileInputStream(file);
                bufferedInputStream = new BufferedInputStream(fileInputStream, BUFFER_SIZE);
                ZipEntry entry = new ZipEntry(relativePath);
                entry.setTime(file.lastModified());
                zipOut.putNextEntry(entry);
                int  count = 0;
                while(count!=-1){
                    zipOut.write(data, 0, count);
                    count = bufferedInputStream.read(data, 0, BUFFER_SIZE);

                }
                bufferedInputStream.close();


            }
        }
    }

    public void unzip(File zipFile, File targetDirectory) throws IOException{
        int BUFFER_SIZE = 2048;
        Boolean unzipSuccess = true;
        ZipInputStream zipInputStream = new ZipInputStream(new BufferedInputStream(new FileInputStream(zipFile)));
        try {
            ZipEntry zipEntry=null;
            byte[] buffer = new byte[BUFFER_SIZE] ;
            while ((zipEntry=zipInputStream.getNextEntry())!=null) {
               // zipEntry = zipInputStream.getNextEntry();
                File file = new File(targetDirectory, zipEntry.getName());
                File dir=null;
                if (zipEntry.isDirectory()){
                    dir=file;
                }else{
                    dir=file.getParentFile();
                }
                if (!dir.isDirectory() && !dir.mkdirs()) {
                    throw new FileNotFoundException("Failed to ensure directory: " + dir.getAbsolutePath());
                }
                if (zipEntry.isDirectory()) {
                    continue;
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                int  count = 0;
                while(count!=-1){
                    fileOutputStream.write(buffer, 0, count);
                    count = zipInputStream.read(buffer);
                }
                fileOutputStream.close();

            }
        }catch (Exception e) {
            e.printStackTrace();
            unzipSuccess = false;
        } finally {
            zipInputStream.close();
        }

    }

}
