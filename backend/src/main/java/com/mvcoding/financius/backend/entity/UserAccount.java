/*
 * Copyright (C) 2015 Mantas Varnagiris.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 */

package com.mvcoding.financius.backend.entity;

import com.google.api.server.spi.config.ApiResourceProperty;
import com.google.appengine.api.users.User;
import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Index;

import static com.mvcoding.financius.backend.OfyService.ofy;

@Entity
public class UserAccount extends BaseEntity {
    @Index @ApiResourceProperty(name = "email") private String email;
    @ApiResourceProperty(name = "googleId") private String googleId;

    public static UserAccount find(User user) {
        return ofy().load().type(UserAccount.class).filter("email", user.getEmail()).first().now();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getGoogleId() {
        return googleId;
    }

    public void setGoogleId(String googleId) {
        this.googleId = googleId;
    }
}

