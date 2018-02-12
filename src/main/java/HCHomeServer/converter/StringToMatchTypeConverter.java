package HCHomeServer.converter;

import org.springframework.core.convert.converter.Converter;

import HCHomeServer.model.result.UserSearchManager.MatchType;

public class StringToMatchTypeConverter implements Converter<String, MatchType> {

	@Override
	public MatchType convert(String source) {
		return MatchType.create(source);
	}
	
}
