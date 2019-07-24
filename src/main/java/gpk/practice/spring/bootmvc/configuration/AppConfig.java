package gpk.practice.spring.bootmvc.configuration;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;


@Configuration
public class AppConfig {
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean(name="messageDateTimeFormatter")
    public DateTimeFormatter messageDatetimeFormatter() {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.SHORT)
                        .withLocale(Locale.US)
                        .withZone(ZoneId.systemDefault());
    }

}
