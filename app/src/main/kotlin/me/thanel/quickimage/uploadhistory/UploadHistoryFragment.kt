package me.thanel.quickimage.uploadhistory

import android.content.Intent
import android.database.Cursor
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.fragment_upload_history.view.*
import me.thanel.quickimage.R
import me.thanel.quickimage.db.uploadhistory.UploadHistoryProvider

class UploadHistoryFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>,
        UploadHistoryContract.View {
    private val presenter by lazy { UploadHistoryPresenter(this) }
    private val adapter by lazy { UploadHistoryAdapter(this) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_upload_history, container, false).apply {
            uploadHistoryRecycler.adapter = adapter
            uploadHistoryRecycler.layoutManager = LinearLayoutManager(activity)
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        loaderManager.initLoader(LOADER_ID_UPLOAD_HISTORY, null, this)
    }

    override fun onCreateLoader(id: Int, args: Bundle?) =
            CursorLoader(activity, UploadHistoryProvider.CONTENT_URI, null, null, null, null)

    override fun onLoadFinished(loader: Loader<Cursor>?, data: Cursor?) {
        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter.swapCursor(null)
    }

    override fun onItemClick(view: View) {
        if (view.id == R.id.rowHistory) {
            presenter.viewImage(view.tag as String)
        }
    }

    override fun onStartActivity(intent: Intent) {
        startActivity(intent)
    }

    companion object {
        private const val LOADER_ID_UPLOAD_HISTORY = 0
    }
}