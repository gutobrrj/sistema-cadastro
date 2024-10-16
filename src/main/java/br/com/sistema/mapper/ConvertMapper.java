package br.com.sistema.mapper;

import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;

public class ConvertMapper {

	// Inicializa a biblioteca de conversão ModelMapper
	private static ModelMapper mapper = new ModelMapper();
	
	// Converte uma entidade em um DTO e vice-versa
	public static <O, D> D parseObject(O origin, Class<D> destination) {
		return mapper.map(origin, destination);
	}
	
	// Converte uma Lista de entidades em uma lista de DTO e vice-versa
	public static <O, D> List<D> parseListObjects(List<O> origin, Class<D> destination) {
		List<D> destinationObjects = new ArrayList<D>();
		for (O o : origin) {
			destinationObjects.add(mapper.map(o, destination));
		}
		return destinationObjects;
	}
	
}
