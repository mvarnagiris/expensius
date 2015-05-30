package com.mvcoding.financius.backend.entity;

import com.google.api.server.spi.config.ApiResourceProperty;
import com.googlecode.objectify.annotation.Entity;

@Entity
public class Tag extends BaseEntity {
    @ApiResourceProperty(name = "title") private String title;
    @ApiResourceProperty(name = "color") private int color;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
