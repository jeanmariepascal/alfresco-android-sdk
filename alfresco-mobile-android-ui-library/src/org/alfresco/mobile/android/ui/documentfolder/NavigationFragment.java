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
package org.alfresco.mobile.android.ui.documentfolder;

import java.util.ArrayList;
import java.util.List;

import org.alfresco.mobile.android.api.asynchronous.LoaderResult;
import org.alfresco.mobile.android.api.asynchronous.NodeChildrenLoader;
import org.alfresco.mobile.android.api.model.Folder;
import org.alfresco.mobile.android.api.model.ListingContext;
import org.alfresco.mobile.android.api.model.Node;
import org.alfresco.mobile.android.api.model.PagingResult;
import org.alfresco.mobile.android.api.model.Site;
import org.alfresco.mobile.android.ui.R;
import org.alfresco.mobile.android.ui.fragments.BaseListFragment;

import android.app.LoaderManager.LoaderCallbacks;
import android.content.Loader;
import android.os.Bundle;

/**
 * Displays a fragment list of document and folders.
 * 
 * @author Jean Marie Pascal
 */
public abstract class NavigationFragment extends BaseListFragment implements
        LoaderCallbacks<LoaderResult<PagingResult<Node>>>
{

    public static final String TAG = "BrowserFragment";

    public static final String ARGUMENT_FOLDER = "folder";

    public static final String ARGUMENT_SITE = "site";

    public static final String ARGUMENT_FOLDERPATH = "folderPath";

    // Browser Parameters
    protected Folder parentFolder;
    
    protected Site currentSiteParameter = null;
    protected String pathParameter = null;
    protected Folder folderParameter = null;
    
    private Boolean activateThumbnail = Boolean.FALSE;

    protected List<Node> selectedItems = new ArrayList<Node>(1);

    public NavigationFragment()
    {
        loaderId = NodeChildrenLoader.ID;
        callback = this;
        emptyListMessageId = R.string.empty_child;
    }

    public static Bundle createBundleArgs(Folder folder)
    {
        return createBundleArgs(folder, null, null);
    }

    public static Bundle createBundleArgs(String folderPath)
    {
        return createBundleArgs(null, folderPath, null);
    }

    public static Bundle createBundleArgs(Site site)
    {
        return createBundleArgs(null, null, site);
    }

    public static Bundle createBundleArgs(Folder parentFolder, String pathFolder, Site site)
    {
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_FOLDER, parentFolder);
        args.putSerializable(ARGUMENT_SITE, site);
        args.putSerializable(ARGUMENT_FOLDERPATH, pathFolder);
        return args;
    }

    @Override
    public Loader<LoaderResult<PagingResult<Node>>> onCreateLoader(int id, Bundle ba)
    {
        if (!hasmore)
        {
            setListShown(false);
        }

        // Case Init & case Reload
        bundle = (ba == null) ? getArguments() : ba;

        ListingContext lc = null, lcorigin = null;

        if (bundle != null)
        {
            folderParameter = (Folder) bundle.getSerializable(ARGUMENT_FOLDER);
            pathParameter = bundle.getString(ARGUMENT_FOLDERPATH);
            currentSiteParameter = (Site) bundle.getSerializable(ARGUMENT_SITE);
            lcorigin = (ListingContext) bundle.getSerializable(ARGUMENT_LISTING);
            lc = copyListing(lcorigin);
            loadState = bundle.getInt(LOAD_STATE);
        }

        calculateSkipCount(lc);

        NodeChildrenLoader loader = null;
        if (pathParameter != null)
        {
            title = (pathParameter.equals("/") ? "/" : pathParameter.substring(pathParameter.lastIndexOf("/") + 1, pathParameter.length()));
            loader = new NodeChildrenLoader(getActivity(), alfSession, pathParameter);
        }
        else if (currentSiteParameter != null && folderParameter == null)
        {
            title = currentSiteParameter.getTitle();
            loader = new NodeChildrenLoader(getActivity(), alfSession, currentSiteParameter);
        }
        else if (folderParameter != null)
        {
            title = folderParameter.getName();
            loader = new NodeChildrenLoader(getActivity(), alfSession, folderParameter);
        }

        if (loader != null)
        {
            loader.setListingContext(lc);
        }

        return loader;
    }

    protected void reload(Bundle b)
    {
        reload(b, loaderId, callback);
    }

    protected void refresh()
    {
        refresh(loaderId, callback);
    }

    @Override
    public void onLoadFinished(Loader<LoaderResult<PagingResult<Node>>> loader, LoaderResult<PagingResult<Node>> results)
    {
        if (loader instanceof NodeChildrenLoader)
        {
            parentFolder = ((NodeChildrenLoader) loader).getParentFolder();
        }

        if (adapter == null)
        {
            adapter = new NodeAdapter(getActivity(), alfSession, R.layout.sdk_list_row, new ArrayList<Node>(0),
                    selectedItems);
        }

        if (results.hasException())
        {
            onLoaderException(results.getException());
        }
        else
        {
            displayPagingData(results.getData(), loaderId, callback);
        }
        ((NodeAdapter) adapter).setActivateThumbnail(activateThumbnail);
    }

    @Override
    public void onLoaderReset(Loader<LoaderResult<PagingResult<Node>>> arg0)
    {
        // Nothing special
    }

    public Boolean hasActivateThumbnail()
    {
        return activateThumbnail;
    }

    public void setActivateThumbnail(Boolean activateThumbnail)
    {
        this.activateThumbnail = activateThumbnail;
    }
    
    public Folder getParent()
    {
        return parentFolder;
    }

}
