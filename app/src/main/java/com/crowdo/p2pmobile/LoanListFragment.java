package com.crowdo.p2pmobile;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Loader;
import android.os.Bundle;
import android.database.Cursor;

/**
 * Created by cwdsg05 on 8/11/16.
 */

public class LoanListFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{


    @Override
    public Loader<Cursor> onCreateLoader(int i, Bundle bundle) {
        return null;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {

    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }
}
