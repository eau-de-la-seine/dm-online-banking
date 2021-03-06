package fr.ekinci.tutorialspringsecurityjwt.security.models;

import fr.ekinci.tutorialspringsecurityjwt.commons.models.Profile;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @author Gokan EKINCI
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Session {
	private String guid;
	private List<String> roles;
	private Boolean passwordExpired;
	private Profile profile;
}
