package net.drive.services.fahrschueler.aussensicht;

import java.util.List;
import net.drive.model.dto.fahrschueler.FahrschuelerListDTO;

public interface IFahrschuelerListService {
 List<FahrschuelerListDTO> getAllFahrschueler();
 
 List<FahrschuelerListDTO> getFahrschuelerBenchmark(
         String klasse,
         Integer ageMax,
         Boolean bezahlt,
         String status
 );
}
