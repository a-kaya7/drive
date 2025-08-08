package net.drive.model.datentypen;

import java.io.Serializable;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Adresse implements Serializable{
	private static final long serialVersionUID = 1L;

	  private String strasse;
	  private String plz;
	  private String ort;
	  private String land;

}
