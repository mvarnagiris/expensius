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

package com.mvcoding.financius.backend.util;

import com.google.api.server.spi.response.BadRequestException;
import com.google.api.server.spi.response.ForbiddenException;
import com.google.api.server.spi.response.NotFoundException;
import com.google.appengine.api.oauth.OAuthRequestException;
import com.google.appengine.api.users.User;
import com.mvcoding.financius.backend.entity.UserAccount;
import com.mvcoding.financius.core.endpoints.body.Body;

import javax.annotation.Nullable;

import static com.google.common.base.Preconditions.checkNotNull;

public final class EndpointUtils {
    private static final String API_KEY = System.getProperty("gcm.api.key");

    private EndpointUtils() {
    }

    public static void validateBody(@Nullable Body body) throws BadRequestException {
        try {
            checkNotNull(body, "Body cannot be null.");
            body.validate();
        } catch (RuntimeException e) {
            throw new BadRequestException(e);
        }
    }

    public static void verifyAuthenticated(@Nullable User user) throws OAuthRequestException {
        if (user == null) {
            throw new OAuthRequestException("Requires authentication.");
        }
    }

    public static UserAccount getRequiredUserAccount(@Nullable User user) throws OAuthRequestException, NotFoundException {
        verifyAuthenticated(user);

        final UserAccount userAccount = UserAccount.find(user);
        if (userAccount == null) {
            throw new NotFoundException("User " + user.getEmail() + " is not registered");
        }

        return userAccount;
    }

    public static UserAccount getRequiredUserAccountAndVerifyPermissions(@Nullable User user) throws OAuthRequestException, NotFoundException, ForbiddenException {
        final UserAccount userAccount = getRequiredUserAccount(user);
        if (!userAccount.isPremium()) {
            throw new ForbiddenException("User does not have permission to call this API because it's not a premium account.");
        }

        return userAccount;
    }

    //    public static void sendGcm(Message message) throws IOException {
    //        final List<Device> devices = getAllDevices();
    //        if (devices.isEmpty()) {
    //            return;
    //        }
    //
    //        sendGcmMessage(devices, message);
    //    }
    //
    //    private static List<Device> getAllDevices() {
    //        final List<Device> devices = ofy().load().type(Device.class).list();
    //        if (devices == null || devices.isEmpty()) {
    //            return Collections.emptyList();
    //        }
    //
    //        return devices;
    //    }
    //
    //    private static void sendGcmMessage(List<Device> devices, Message message) throws IOException {
    //        final Sender sender = new Sender(API_KEY);
    //
    //        for (Device device : devices) {
    //            final Result result = sender.send(message, device.getRegistrationId(), 5);
    //            if (result.getMessageId() != null) {
    //                final String canonicalRegId = result.getCanonicalRegistrationId();
    //                if (canonicalRegId != null) {
    //                    // The registrationId changed
    //                    device.setRegistrationId(canonicalRegId);
    //                    ofy().save().entity(device).now();
    //                }
    //            } else {
    //                final String error = result.getErrorCodeName();
    //                if (error.equals(Constants.ERROR_NOT_REGISTERED)) {
    //                    // The device is no longer registered with Gcm
    //                    ofy().delete().entity(device).now();
    //                }
    //            }
    //        }
    //    }
}
