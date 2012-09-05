/*******************************************************************************
 * Copyright (C) 2005-2012 Alfresco Software Limited.
 * 
 * This file is part of the Alfresco Mobile SDK.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *  
 *  http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 ******************************************************************************/
package org.alfresco.mobile.android.api.session;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.alfresco.mobile.android.api.session.impl.AbstractAlfrescoSessionImpl;
import org.alfresco.mobile.android.api.session.impl.CloudSessionImpl;

/**
 * DRAFT Represents a connection to the Alfresco Cloud repository as a specific
 * user.
 * 
 * @author Jean Marie Pascal
 */
public abstract class CloudSession extends AbstractAlfrescoSessionImpl
{

    // ///////////////////////////////////////////////
    // CLOUD SPECIFIC
    // ///////////////////////////////////////////////
    /** Define a different cloud network. */
    public static final String CLOUD_NETWORK_ID = "org.alfresco.mobile.bindings.api.cloud.network.id";

    /**
     * Registers a new user for an account on the Alfresco in the cloud server.
     * 
     * @param firstName
     * @param lastName
     * @param emailAddress
     * @param password
     * @param apiKey
     * @return
     */
    public static CloudSignupRequest signup(String firstName, String lastName, String emailAddress, String password,
            String apiKey)
    {
        return CloudSessionImpl.signup(firstName, lastName, emailAddress, password, apiKey);
    }

    /**
     * Determines whether an account represented by a previous signup request
     * has been verified yet.
     * 
     * @param signupRequest
     * @return
     */
    public static boolean isAccountVerified(CloudSignupRequest signupRequest)
    {
        return CloudSessionImpl.checkAccount(signupRequest);
    }

    /**
     * Connects the given user to the Alfresco in the cloud server in the
     * context of the users home network.
     * 
     * @param emailAddress
     * @param password
     * @param apiKey
     * @param parameters
     * @return
     */
    public static CloudSession connect(String emailAddress, String password, String apiKey,
            Map<String, Serializable> parameters)
    {
        return new CloudSessionImpl(emailAddress, password, parameters);
    }

    /**
     * Connects the given user to the Alfresco in the cloud server in the
     * context of the given network.
     * 
     * @param emailAddress
     * @param password
     * @param apiKey
     * @param networkId
     * @param parameters
     * @return
     */
    public static CloudSession connect(String emailAddress, String password, String apiKey, String networkId,
            Map<String, Serializable> parameters)
    {
        Map<String, Serializable> tmpParameters = parameters;
        if (tmpParameters == null)
        {
            tmpParameters = new HashMap<String, Serializable>();
        }
        tmpParameters.put(CLOUD_NETWORK_ID, networkId);
        return new CloudSessionImpl(emailAddress, password, tmpParameters);
    }

    /**
     * Returns the list of networks the current user has access to.
     * 
     * @return
     */
    public abstract List<CloudNetwork> getNetworks();

    /**
     * Returns the current network for the session.
     * 
     * @return
     */
    public abstract CloudNetwork getNetwork();

}
