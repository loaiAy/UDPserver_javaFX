import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class clientMainController {
	Alert alert = new Alert(Alert.AlertType.ERROR);

    @FXML
    private TextField textField;

    @FXML
    private TextArea textA;
    
    @FXML
    private Button Ok;
    
    @FXML
    public void initialize() {
    	textA.setText("please enter capital city name to display weather for the three next days : \n");
    	textA.appendText("[ 1.jerusalem , 2.london , 3.paris , 4.moscow ]");
    }

    @FXML
    void OkPpressed(ActionEvent event) {
    	String city = textField.getText(); 

    	//check if the data correct , if yes , then we initializing new client socket and sending to sendtoserver method
    	//that handles writing the data to the server and afterwards we calling the getfromserver method to receive the 
    	//information of the weather of the given city 
    	if(city.equals("jerusalem") || city.equals("london") || city.equals("paris") || city.equals("moscow")) {
    		textA.clear();
			DatagramSocket clientSocket = null;
			try {
				clientSocket = new DatagramSocket();
				InetAddress IPAddress = InetAddress.getByName("localhost");

				sendToServer(clientSocket, IPAddress);
				getFromServer(clientSocket);

			} catch (Exception e) { 
			} finally { clientSocket.close(); }
		}
		else {
			error();
		}
	}

	public void sendToServer(DatagramSocket socket, InetAddress ip) {
		String city = textField.getText();
		textField.setText("");
		
		//generating the name of the capital into packet
		DatagramPacket packet = new DatagramPacket(city.getBytes(), city.getBytes().length, ip, 9876);
		try {     
			//sending the name of the city to the server
			socket.send(packet);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}  


	public void getFromServer(DatagramSocket socket) {
		byte[] receiveData = new byte[1024];
		DatagramPacket packet = new DatagramPacket(receiveData, receiveData.length);	
		try {
			//receive the asked data from the server
			socket.receive(packet);  
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = packet.getData();
		int len = packet.getLength();
		String modifiedSentence = new String(data).substring(0, len);
		textA.setText(modifiedSentence);
	}
	
	private void error() {
		alert.setTitle("error");
    	alert.setHeaderText("error : invalid capital city name");
    	alert.setContentText("please insert again ");
    	alert.showAndWait();		
	}
}
