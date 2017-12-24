package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import hu.ait.android.shoppinglist.adapter.TobuyRecyclerAdapter;
import io.realm.Realm;
import io.realm.RealmResults;

public class MainActivity extends AppCompatActivity {

    public static final String KEY_TO_ID = "KEY_TO_ID";
    public static final int REQUEST_CODE_TO_EDIT = 1001;
    public static final int REQUEST_NEW_ITEM = 100;
    private int positionToEdit = -1;
    private TobuyRecyclerAdapter itemsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ((TobuyApplication) getApplication()).openRealm();

        RealmResults<Tobuy> allItems = getRealm().where(Tobuy.class).findAll();
        Tobuy itemsArray[] = new Tobuy[allItems.size()];
        List<Tobuy> itemsResult = new ArrayList<Tobuy>(Arrays.asList(allItems.toArray(itemsArray)));


        itemsAdapter = new TobuyRecyclerAdapter(itemsResult, this);

        RecyclerView recyclerViewItems = (RecyclerView) findViewById(R.id.recyclerTobuy);

        recyclerViewItems.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewItems.setAdapter(itemsAdapter);

        TobuyItemTouchHelperCallback itemHelperCallback = new TobuyItemTouchHelperCallback(itemsAdapter);
        ItemTouchHelper touchHelper = new ItemTouchHelper(itemHelperCallback);
        touchHelper.attachToRecyclerView(recyclerViewItems);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    public Realm getRealm() {
        return ((TobuyApplication) getApplication()).getRealmItems();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_addItem:
                createItemPage();
                break;
            case R.id.action_deleteAll:
                itemsAdapter.clearList();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void createItemPage() {
        Intent intentCreate = new Intent(MainActivity.this, CreateItem.class);
        startActivityForResult(intentCreate, REQUEST_NEW_ITEM);
    }

    public void openEditActivity(String tobuyID, int adapterPosition) {
        Intent intentEdit = new Intent(MainActivity.this, CreateItem.class);

        positionToEdit = adapterPosition;

        intentEdit.putExtra(KEY_TO_ID, tobuyID);
        startActivityForResult(intentEdit, REQUEST_CODE_TO_EDIT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (resultCode) {
            case RESULT_OK:
                String itemID = data.getStringExtra(
                        CreateItem.KEY_ITEM);

                Tobuy item = getRealm().where(Tobuy.class)
                        .equalTo(getString(R.string.itemID), itemID)
                        .findFirst();

                if (requestCode == REQUEST_NEW_ITEM) {
                    itemsAdapter.addItem(item);

                } else if (requestCode == REQUEST_CODE_TO_EDIT) {

                    itemsAdapter.updateItem(positionToEdit, item);
                }
                break;
            case RESULT_CANCELED:
                break;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((TobuyApplication) getApplication()).closeRealm();
    }

    public void deleteItem(Tobuy item) {
        getRealm().beginTransaction();
        item.deleteFromRealm();
        getRealm().commitTransaction();
    }

    public void changeCheck(boolean bought, Tobuy item) {
        getRealm().beginTransaction();
        item.setBought(bought);
        getRealm().commitTransaction();
    }

    public void deleteAll() {
        getRealm().beginTransaction();
        getRealm().delete(Tobuy.class);
        getRealm().commitTransaction();
    }

}
