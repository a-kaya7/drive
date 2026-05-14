package net.drive.services.administration.drucken.innensicht;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import java.io.InputStream;

import java.util.UUID;
import java.util.stream.Collectors;

import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.apache.poi.xwpf.usermodel.XWPFRun;
import org.springframework.stereotype.Service;

import net.drive.config.LogicResource;
import net.drive.model.entities.fahrschueler.Fahrschueler;
import net.drive.repository.fahrschueler.IFahrschuelerRepository;
import net.drive.services.administration.drucken.aussensicht.IFahrschuelerVertragService;


@Service
public class FahrschuelerVertragService implements IFahrschuelerVertragService  {

	 private final IFahrschuelerRepository fahrschuelerRepo;
	 private final LogicResource logicResource;
	 
	 public FahrschuelerVertragService (IFahrschuelerRepository fahrschuelerRepo, LogicResource logicResource) {
		 this.fahrschuelerRepo = fahrschuelerRepo;
		 this.logicResource = logicResource;
	 }
	 
	@Override
	public byte[] vertragErstellen(UUID fahrschuelerId) throws IOException {
		if (fahrschuelerId == null) {
			throw new IllegalArgumentException(logicResource.getMessage("IDNull"));
		}
		  Fahrschueler fahrschueler = fahrschuelerRepo
	                .findByFahrschuelerId(fahrschuelerId)
	                .orElseThrow(() -> new IllegalArgumentException(
	                        logicResource.getMessage("FahrschuelerNichtGefunden")));

		  InputStream input = getClass().getClassLoader().getResourceAsStream("templates/ausbildungsvertrag-template.docx");
		  
		  // Daten auslesen und dokument objekt erzeugen
		  XWPFDocument dokument =  new XWPFDocument(input);
		  
		  
		  /*
		   * Document
                  ── Paragraph
                      ── Run → "Hallo "
                      ── Run → "Welt"
                      ── Run → "!"
		   * 
		   */
		  
		  for (XWPFParagraph p : dokument.getParagraphs()) {
			  
			  for(XWPFRun run : p.getRuns()) {
				  String text = run.getText(0); // der richtige Text in run lesen... 
				  
				  if(text != null) {
					  text = text.replace("${VORNAME}", fahrschueler.getVorname());
					  text = text.replace("${NACHNAME}", fahrschueler.getNachname());
					  text= text.replace("${MANDANT}", fahrschueler.getMandant().toString());
					  text = text.replace("${GEBURTSDATUM}", fahrschueler.getGeburtsdatum().toString());
					  String adresse = fahrschueler.getAdresse().getStrasse()
						        + " "
						        + fahrschueler.getAdresse().getPlz()
						        + ", "
						        + fahrschueler.getAdresse().getOrt()
						        + " "
						        + fahrschueler.getAdresse().getLand();
					  text = text.replace("${ADRESSE}", adresse);
					  text = text.replace("${TELEFONNUMMER}", fahrschueler.getTelefonnummer());
					  String klasse = fahrschueler.getFuehrerscheine().stream()
							  .map( k -> k.getFuehrerscheinKlasse())
							  .collect(Collectors.joining());
					  text = text.replace("${KLASSE}", klasse);
					  run.setText(text, 0);  // Aktualisiere den alten Text in Run mit dem neuen Text.

				  }
			  }
			 
		  }
		  ByteArrayOutputStream output = new ByteArrayOutputStream();
		  dokument.write(output);
		  dokument.close();
		  return output.toByteArray();

	}
}
