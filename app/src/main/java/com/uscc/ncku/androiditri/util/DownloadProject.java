package com.uscc.ncku.androiditri.util;

import android.os.AsyncTask;

/**
 * Created by Oslo on 9/30/16.
 */
public class DownloadProject extends AsyncTask<Void, Void, Boolean> {
    private int project_id;
    private String downloadPath;

    public DownloadProject(int project_id) {
        this.project_id = project_id;
        downloadPath = getDownloadPath();
    }

    @Override
    protected Boolean doInBackground(Void... voids) {
        try {
            DownloadProjectWithId(project_id);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return null;
    }

    public String getDownloadPath() {
        String path = null;


        return path;
    }

    // do all downloading
    private void DownloadProjectWithId(int project_id) throws Exception{

    }

    private void DownloadAllField() {

    }


}
