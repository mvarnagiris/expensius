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

package com.mvcoding.financius.backend.endpoint;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiNamespace;

@Api(name = "financius",
        version = "v1",
        namespace = @ApiNamespace(ownerDomain = "backend.locl.com", ownerName = "backend.locl.com", packagePath = "")//,
        //        scopes = {Constants.EMAIL_SCOPE},
        //        clientIds = {Constant.API_EXPLORER_CLIENT_ID, Constants.WEB_CLIENT_ID, Constants.ANDROID_CLIENT_ID_CUSTOMER,
        //                     Constants.ANDROID_CLIENT_ID_CUSTOMER_DEBUG, Constants.ANDROID_CLIENT_ID_BUSINESS,
        //                     Constants.ANDROID_CLIENT_ID_BUSINESS_DEBUG, Constants.IOS_CLIENT_ID},
        //        audiences = {Constants.ANDROID_AUDIENCE})
)
public abstract class Endpoint {
}
