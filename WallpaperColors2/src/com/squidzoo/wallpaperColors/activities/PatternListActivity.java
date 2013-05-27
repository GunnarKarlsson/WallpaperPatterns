package com.squidzoo.wallpaperColors.activities;

import android.os.Handler;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.squidzoo.wallpaperColors.R;
import com.squidzoo.wallpaperColors.tasks.GetPatternsTask;
import com.squidzoo.wallpaperColors.types.ItemType;

public class PatternListActivity extends ItemListActivity {

	
	@Override
	protected void setType(){
		setItemType(ItemType.PATTERN);
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.layout.pattern_list_activity_options, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.option_menu_item_new_patterns:
			new GetPatternsTask(new Handler(this)).execute(getNewUrl());
			break;
		case R.id.option_menu_item_top_patterns:
			new GetPatternsTask(new Handler(this)).execute(getTopUrl());
			break;
		}
		return true;
	}
}
