package com.androidiani.MarketEnabler.view;

import java.util.ArrayList;

import android.app.Activity;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;

import com.androidiani.MarketEnabler.R;

public class ContextMenuSample extends Activity {

	// ===========================================================
	// Final Fields
	// ===========================================================
	protected static final int CONTEXTMENU_DELETEITEM = 0;

	// ===========================================================
	// Fields
	// ===========================================================

	protected ListView mFavList;
	protected ArrayList<Favorite> fakeFavs = new ArrayList<Favorite>();

	// ===========================================================
	// "Constructors"
	// ===========================================================

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.listofsettings);

		/* Add some items to the list the listview will be showing. */
		fakeFavs.add(new Favorite("John", "nice guy"));
		fakeFavs.add(new Favorite("Yasmin", "hot girl"));
		fakeFavs.add(new Favorite("Jack", "cool guy"));

		this.mFavList = (ListView) this.findViewById(R.id.listset);
		initListView();
	}

	private void refreshFavListItems() {
		mFavList.setAdapter(new ArrayAdapter<Favorite>(this,
				android.R.layout.simple_list_item_1, fakeFavs));
	}

	private void initListView() {
		/* Loads the items to the ListView. */
		refreshFavListItems();

		/* Add Context-Menu listener to the ListView. */
		// mFavList.setOnItemLongClickListener();
		mFavList
				.setOnCreateContextMenuListener(new OnCreateContextMenuListener() {

					@Override
					public void onCreateContextMenu(ContextMenu menu, View v,
							ContextMenuInfo menuInfo) {
						// TODO Auto-generated method stub
						menu.setHeaderTitle("ContextMenu");
						menu.add(0, CONTEXTMENU_DELETEITEM, 0,
								"Delete this favorite!");

					}

				});
		/*
		 * mFavList.setOnItemLongClickListener( new
		 * AdapterView.OnItemLongClickListener (){
		 * 
		 * @Override public boolean onItemLongClick(AdapterView<?> av, View v,
		 * int pos, long id) { Log.i("Debug", "Log id "+id+" pos "+pos); return
		 * false; } });
		 */

	}

	// ===========================================================
	// Methods from SuperClass/Interfaces
	// ===========================================================

	@Override
	public boolean onContextItemSelected(MenuItem aItem) {
		AdapterContextMenuInfo menuInfo = (AdapterContextMenuInfo) aItem
				.getMenuInfo();

		/* Switch on the ID of the item, to get what the user selected. */
		switch (aItem.getItemId()) {
		case CONTEXTMENU_DELETEITEM:
			/* Get the selected item out of the Adapter by its position. */
			Favorite favContexted = (Favorite) mFavList.getAdapter().getItem(
					menuInfo.position);
			/* Remove it from the list. */
			fakeFavs.remove(favContexted);

			refreshFavListItems();
			return true; /* true means: "we handled the event". */
		}
		return false;
	}

	// ===========================================================
	// Inner and Anonymous Classes
	// ===========================================================

	/** Small class holding some basic */
	protected class Favorite {

		protected String name;
		protected String kindness;

		protected Favorite(String name, String kindness) {
			this.name = name;
			this.kindness = kindness;
		}

          /** The ListView is going to display the toString() return-value! */
		public String toString() {
			return name + " (" + kindness + ")";
		}

          public boolean equals(Object o) {
			return o instanceof Favorite
					&& ((Favorite) o).name.compareTo(name) == 0;
		}
	}
}