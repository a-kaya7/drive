package net.drive.services.administration.drucken.aussensicht;

import java.io.IOException;
import java.util.UUID;

public interface IFahrschuelerVertragService {

	byte [] vertragErstellen(UUID fahrschuelerId) throws IOException;
}
