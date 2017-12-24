package hu.ait.android.shoppinglist;

import android.graphics.Bitmap;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Spinner;

import io.realm.RealmModel;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;


/**
 * Created by alicetan on 11/5/17.
 */

public class Tobuy extends RealmObject {

    @PrimaryKey
    private String itemID;

    private int category;
    private String name;
    private String description;
    private String price;
    private boolean bought;


    public Tobuy() {

    }

    public Tobuy(int category, String name, String description, String price, boolean bought) {
        this.category = category;
        this.name = name;
        this.description = description;
        this.price = price;
        this.bought = bought;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public boolean isBought() {
        return bought;
    }

    public void setBought(boolean bought) {
        this.bought = bought;
    }

    public String getItemID() {
        return itemID;
    }
}
