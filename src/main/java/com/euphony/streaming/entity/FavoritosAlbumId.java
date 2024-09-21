package com.euphony.streaming.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FavoritosAlbumId implements Serializable {

    private Long usuario;
    private Long album;
}
