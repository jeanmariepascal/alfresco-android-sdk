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
package org.alfresco.mobile.android.samples.ui.tags;

import org.alfresco.mobile.android.api.model.Node;
import org.alfresco.mobile.android.samples.R;
import org.alfresco.mobile.android.samples.utils.SessionUtils;
import org.alfresco.mobile.android.samples.utils.UIUtils;
import org.alfresco.mobile.android.ui.tag.TagsNodeFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TagsListNodeFragment extends TagsNodeFragment
{
    
    public static final String TAG = "TagsListNodeFragment";

    public TagsListNodeFragment()
    {
    }

    public static TagsListNodeFragment newInstance(Node node)
    {
        TagsListNodeFragment bf = new TagsListNodeFragment();
        bf.setArguments(createBundleArgs(node));
        return bf;
    }
    
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        alfSession = SessionUtils.getsession(getActivity());
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    
    @Override
    public void onStart()
    {
        UIUtils.setFragmentTitle(getActivity(), getText(R.string.document_tags_header).toString());
        super.onStart();
    }

   
}

