package bg.fmi.popcornpals.dto;

import bg.fmi.popcornpals.util.RegexPattern;
import bg.fmi.popcornpals.util.StringSize;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudioDTO {

    @NotNull(message = "Studio: Name cannot be null")
    @NotBlank(message = "Studio: Name cannot be blank")
    @Size(max = StringSize.STUDIO_NAME_MAX, message = "Studio: Name must be no more than " +  StringSize.STUDIO_NAME_MAX + " characters")
    @Pattern(regexp = RegexPattern.STUDIO_NAME, message = "Studio: Name can contain only letters, digits and spaces")
    private String name;

    @Size(max = StringSize.DESCRIPTION_MAX, message = "Studio: Description must be no more than " +  StringSize.DESCRIPTION_MAX + " characters")
    @Pattern(regexp = RegexPattern.DESCRIPTION, message = "Studio: Invalid symbols in description")
    private String description;

    private LocalDate foundingDate;

    public StudioDTO(String name) {
        this.name = name;
    }
}
