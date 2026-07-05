package net.drive.services.engagement.aussensicht;

import java.util.UUID;

import net.drive.model.entities.fahrschueler.Fahrschueler;

public interface IEngagementService {

	Fahrschueler getEngagement (UUID fahrschulerId);
}
