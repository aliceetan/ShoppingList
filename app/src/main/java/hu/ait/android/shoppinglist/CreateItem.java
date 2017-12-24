package hu.ait.android.shoppinglist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.UUID;

import io.realm.Realm;

/**
 * Created by alicetan on 11/5/17.
 */

public class CreateItem extends AppCompatActivity {

    public static final String KEY_ITEM = "KEY_ITEM";
    private EditText etItem;
    private Spinner spnrDropDown;
    private EditText etPrice;
    private EditText etDescription;
    private CheckBox cbPurchased;
    private Tobuy itemToBuy = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);

        setupUI();


        if (getIntent().getSerializableExtra(MainActivity.KEY_TO_ID) != null) {
            initEdit();
        } else {
            initCreate();
        }

    }

    private void initCreate() {

        getRealm().beginTransaction();
        itemToBuy = getRealm().createObject(Tobuy.class, UUID.randomUUID().toString());
        getRealm().commitTransaction();
    }

    private void initEdit() {
        String itemID = getIntent().getStringExtra(MainActivity.KEY_TO_ID);
        itemToBuy = getRealm().where(Tobuy.class)
                .equalTo(getString(R.string.itemID), itemID)
                .findFirst();
        etItem.setText(itemToBuy.getName());
        spnrDropDown.setSelection(itemToBuy.getCategory());
        etPrice.setText("" + itemToBuy.getPrice());
        etDescription.setText(itemToBuy.getDescription());
        cbPurchased.setChecked(itemToBuy.isBought());
    }

    private void setupUI() {
        etItem = (EditText) findViewById(R.id.etItem);
        spnrDropDown = (Spinner) findViewById(R.id.spnrDropDown);
        etPrice = (EditText) findViewById(R.id.etPrice);
        etDescription = (EditText) findViewById(R.id.etDescription);
        cbPurchased = (CheckBox) findViewById(R.id.cbPurchased);
        Button btnAddRow = (Button) findViewById(R.id.btnAddRow);
        btnAddRow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveItem();
            }
        });
    }


    public Realm getRealm() {
        return ((TobuyApplication) getApplication()).getRealmItems();
    }

    private void saveItem() {

        getRealm().beginTransaction();

        itemToBuy.setBought(cbPurchased.isChecked());
        itemToBuy.setCategory(spnrDropDown.getSelectedItemPosition());
        itemToBuy.setDescription(etDescription.getText().toString());
        itemToBuy.setName(etItem.getText().toString());
        itemToBuy.setPrice(etPrice.getText().toString());

        getRealm().commitTransaction();

        Intent intentResult = new Intent();
        intentResult.putExtra(KEY_ITEM, itemToBuy.getItemID());
        setResult(RESULT_OK, intentResult);
        finish();
    }

}
