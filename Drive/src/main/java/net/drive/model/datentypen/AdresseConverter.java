package net.drive.model.datentypen;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class AdresseConverter implements AttributeConverter<Adresse, String>{

	 private final ObjectMapper objectMapper = new ObjectMapper();

	    @Override
	    public String convertToDatabaseColumn(Adresse adresse) {
	        try {
	            return objectMapper.writeValueAsString(adresse);
	        } catch (JsonProcessingException e) {
	            throw new RuntimeException("", e);
	        }
	    }

	    @Override
	    public Adresse convertToEntityAttribute(String dbData) {
	        try {
	            return objectMapper.readValue(dbData, Adresse.class);
	        } catch (Exception e) {
	            throw new RuntimeException("", e);
	        }
	    }

}
