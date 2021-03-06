package com.czar.catalogs.xmlToJson;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationIntrospector;
import com.fasterxml.jackson.module.jsonSchema.factories.SchemaFactoryWrapper;

@Service("jaxbToJsonGeneratorService")
public class JaxbToJsonGenerator implements JaxbToJsonGeneratorService {

	private ObjectMapper createJaxbObjectMapper() {
		final ObjectMapper mapper = new ObjectMapper();
		final TypeFactory typeFactory = TypeFactory.defaultInstance();
		final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(typeFactory);
		mapper.getDeserializationConfig().with(introspector);
		mapper.getSerializationConfig().with(introspector);
		return mapper;
	}

	public String writeJson(String fullyQualifiedClassName) {
			
		final ObjectMapper mapper = new ObjectMapper();
		final TypeFactory typeFactory = TypeFactory.defaultInstance();
		final AnnotationIntrospector introspector = new JaxbAnnotationIntrospector(typeFactory);
		mapper.getDeserializationConfig().with(introspector);
		mapper.getSerializationConfig().with(introspector);
		final SchemaFactoryWrapper visitor = new SchemaFactoryWrapper();
		//final ObjectMapper mapper = new ObjectMapper();
		try {
			mapper.acceptJsonFormatVisitor(mapper.constructType(Class.forName(fullyQualifiedClassName)), visitor);
			final com.fasterxml.jackson.module.jsonSchema.JsonSchema jsonSchema = visitor.finalSchema();
			System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonSchema));
			return mapper.writeValueAsString(jsonSchema);
		} catch (ClassNotFoundException cnfEx) {
			System.err.println("Unable to find class " + fullyQualifiedClassName);
			return "";
		} catch (JsonMappingException jsonEx) {
			System.err.println("Unable to map JSON: " + jsonEx);
			return "";
		} catch (JsonProcessingException jsonEx) {
			System.err.println("Unable to process JSON: " + jsonEx);
			return "";
		}	
	}
	
	
//	 public static void main(final String[] arguments)
//	   {
//	      final JaxbToJsonGenerator instance = new JaxbToJsonGenerator();
//	     // final String fullyQualifiedClassName = arguments[0];
//	      final ObjectMapper objectMapper = instance.createJaxbObjectMapper();
//	      instance.writeJson(objectMapper, "mx.gob.sat.sitio_internet.cfd.catalogos.CUsoCFDI");
//	   }

	
}
