package com.euphony.streaming.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "DTO for playlist creation and update")
public class PlaylistRequestDTO {

    @Schema(description = "Name of the playlist", example = "My Rock Playlist")
    @NotBlank(message = "Playlist name cannot be empty")
    @Size(min = 3, max = 255, message = "Name must be between 3 and 255 characters")
    private String name;

    @Schema(description = "Description of the playlist", example = "Collection of my favorite songs")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Schema(description = "Indicates if the playlist is public", example = "false", defaultValue = "false")
    @NotNull(message = "Must specify if the playlist is public or private")
    @Builder.Default
    private Boolean isPublic = false;

    @Schema(description = "Cover image URL", example = "https://example.com/image.jpg")
    @Size(max = 255, message = "Image URL cannot exceed 255 characters")
    @Pattern(regexp = "^(https?://.*|)$", message = "URL must start with http:// or https:// or be empty")
    private String coverImage;

    @Schema(description = "Owner user ID", example = "123e4567-e89b-12d3-a456-426614174000")
    @NotNull(message = "User ID cannot be null")
    private UUID userId;
}