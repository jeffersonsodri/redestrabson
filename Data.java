package redestrabson;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Data {
	private int tipo;
	private String exten;
	private byte[] bite;
	private String nome;
	
	/**
	 * Logger, usado para exceptions
	 */
	
	private static final Logger LOGGER = Logger.getLogger( Data.class.getName() );
	
	
	public Data(int tipo, byte[] bite, String exten, String nome) {
		this.tipo = tipo;
		this.exten = exten;
		this.bite = bite;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public int getTipo() {
		return tipo;
	}

	public void setTipo(int tipo) {
		this.tipo = tipo;
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
        	LOGGER.log( Level.SEVERE, ioExp.toString(), ioExp );
        }
        return bArray;
    }
}
