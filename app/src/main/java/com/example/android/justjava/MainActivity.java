package com.example.android.justjava;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.NumberFormat;

/**
 * This app displays an order form to order coffee.
 */
public class MainActivity extends AppCompatActivity {

    int quantity = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * This method is called when the plus button is clicked.
     */
    public void increment(View view) {

        if (quantity == 100){
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_max_coffee), Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity + 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the minus button is clicked.
     */
    public void decrement(View view) {

        if (quantity == 1){
            // Show an error message as a toast
            Toast.makeText(this, getString(R.string.toast_min_coffee), Toast.LENGTH_SHORT).show();
            // Exit this method early because there's nothing left to do
            return;
        }
        quantity = quantity - 1;
        displayQuantity(quantity);
    }

    /**
     * This method is called when the order button is clicked.
     */
    public void submitOrder(View view) {
        // Find costumer's name
        EditText text = (EditText) findViewById(R.id.name_field); //gets you the contents of edit text
        String name = text.getText().toString();

        // Figure out if the costumer wants whipped cream
        CheckBox whippedCreamCheckBox = findViewById(R.id.whipped_cream_checkbox);
        boolean hasWhippedCream = whippedCreamCheckBox.isChecked();

        // Figure out if the costumer wants chocolate
        CheckBox chocolateCheckBox = findViewById(R.id.chocolate_checkbox);
        boolean hasChocolate = chocolateCheckBox.isChecked();

        int price = calculatePrice(hasWhippedCream, hasChocolate);
        String priceMessage = createOrderSummary(name, price, hasWhippedCream, hasChocolate);

        Intent intent = new Intent(Intent.ACTION_SENDTO);
        intent.setData(Uri.parse("mailto:justjava@justjavacoffee.com")); // only email apps should handle this
        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.order_summary_email_subject, name));
        intent.putExtra(Intent.EXTRA_TEXT, priceMessage);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

    /**
     * Calculates the price of the order.
     * @param addWhipCream if the user wants whipped cream topping
     * @param addChocolate if the user wants chocolate topping
     * @return total price
     */
    private int calculatePrice(boolean addWhipCream, boolean addChocolate) {
        // Price of 1 cup of coffee
        int basePrice = 5;

        // add $1 if costumer wants whipped cream
        if (addWhipCream) {
            basePrice = basePrice + 1;
        }

        // add $2 if costumer wants chocolate
        if (addChocolate) {
            basePrice = basePrice + 2;
        }

        // Calculate the total order price
        return quantity * basePrice;
    }


    /**
     * Creates the order summary.
     * @param name of the costumer
     * @param price of the order
     * @param addWhippedCream if the user wants whipped cream topping
     * @param addChocolate if the user wants chocolate topping
     * @return text summary
     */
    private String createOrderSummary(String name, int price, boolean addWhippedCream, boolean addChocolate) {
        String priceMessage = getString(R.string.order_summary_name, name);
        priceMessage = priceMessage + "\n" + getString(R.string.order_summary_whipped_cream, addWhippedCream);
        priceMessage = priceMessage + "\n" + getString(R.string.order_summary_chocolate, addChocolate);
        priceMessage = priceMessage + "\n" + getString(R.string.order_summary_quantity, quantity);
        priceMessage = priceMessage + "\n" + getString(R.string.order_summary_price,
                NumberFormat.getCurrencyInstance().format(price));
        priceMessage = priceMessage + "\n" + getString(R.string.thank_you);
        return priceMessage;
    }

    /**
     * This method displays the given quantity value on the screen.
     */
    private void displayQuantity(int numberOfCoffees) {
        TextView quantityTextView = (TextView) findViewById(R.id.quantity_text_view);
        quantityTextView.setText("" + numberOfCoffees);
    }

}