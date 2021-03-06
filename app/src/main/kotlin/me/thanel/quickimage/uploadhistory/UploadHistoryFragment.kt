package me.thanel.quickimage.uploadhistory

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.app.LoaderManager
import android.support.v4.content.CursorLoader
import android.support.v4.content.Loader
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.view.*
import kotlinx.android.synthetic.main.fragment_upload_history.*
import kotlinx.android.synthetic.main.fragment_upload_history.view.*
import me.thanel.quickimage.R
import me.thanel.quickimage.db.uploadhistory.UploadHistoryProvider
import me.thanel.quickimage.db.uploadhistory.UploadHistoryTable
import me.thanel.quickimage.extensions.createShareLinkIntent
import me.thanel.quickimage.uploadhistory.model.UploadHistoryItem

class UploadHistoryFragment : Fragment(), LoaderManager.LoaderCallbacks<Cursor>,
        UploadHistoryAdapter.Callback {
    private val adapter by lazy { UploadHistoryAdapter(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.fragment_upload_history, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_history) {
            AlertDialog.Builder(activity)
                    .setTitle(R.string.clear_history)
                    .setMessage(R.string.clear_history_info)
                    .setPositiveButton(R.string.clear_history, { _, _ ->
                        UploadHistoryTable.deleteAll(activity)
                    })
                    .setNegativeButton(R.string.cancel, null)
                    .show()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

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
        if (data == null || data.count == 0) {
            uploadHistoryRecycler.visibility = View.GONE
            emptyView.visibility = View.VISIBLE
        } else {
            uploadHistoryRecycler.visibility = View.VISIBLE
            emptyView.visibility = View.GONE
        }

        adapter.swapCursor(data)
    }

    override fun onLoaderReset(loader: Loader<Cursor>?) {
        adapter.swapCursor(null)
    }

    override fun onItemClick(item: UploadHistoryItem) {
        startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(item.link)))
    }

    override fun onShareItem(item: UploadHistoryItem) {
        startActivity(context.createShareLinkIntent(item.link))
    }

    companion object {
        private const val LOADER_ID_UPLOAD_HISTORY = 0
    }
}