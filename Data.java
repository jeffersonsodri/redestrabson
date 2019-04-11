package redes;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class Data {
	private int Tipo;
	private String exten;
	private byte[] bite;
	
	public Data(int Tipo, byte[] bite, String exten) {
		this.Tipo = Tipo;
		this.exten = exten;
		this.bite = bite;
	}

	public int getTipo() {
		return Tipo;
	}

	public void setTipo(int tipo) {
		Tipo = tipo;
	}

	public String getExten() {
		return exten;
	}

	public void setExten(String exten) {
		this.exten = exten;
	}

	public byte[] getBite() {
		return bite;
	}

	public void setBite(byte[] bite) {
		this.bite = bite;
	}
	
	public static byte[] readFileToByteArray(File file){
        FileInputStream fis = null;
        // Creating a byte array using the length of the file
        // file.length returns long which is cast to int
        byte[] bArray = new byte[(int) file.length()];
        try{
            fis = new FileInputStream(file);
            fis.read(bArray);
            fis.close();        
            
        }catch(IOException ioExp){
            ioExp.printStackTrace();
        }
        return bArray;
    }
}
