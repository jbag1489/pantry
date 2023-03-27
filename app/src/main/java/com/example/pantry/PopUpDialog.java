package com.example.pantry;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import java.io.IOException;

public class PopUpDialog extends AppCompatDialogFragment {
    private TextView mNameTextView;
    private TextView mQuantityTextView;
    private ImageView mIngredientImageView;
    private ImageButton mPlusImageButton;
    private ImageButton mMinusImageButton;
    private PopUpDialogListener listener;
    private int quantityToChange;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_popup_dialog, null);

        Bundle recievedBundle = getArguments();
        String barcode = recievedBundle.getString("barcode","");
        PantryManager pm = new PantryManager(getActivity());
        Ingredient ingredient = pm.getIngredient(barcode);
        mNameTextView = (TextView) view.findViewById(R.id.nameTextView);
        mNameTextView.setText(ingredient.getName());

        mIngredientImageView = (ImageView) view.findViewById(R.id.ingredientImageView);
        ingredient.loadImageIntoImageView(getActivity(), mIngredientImageView);
        Bitmap ingredientImage = recievedBundle.getParcelable("Image");
        mIngredientImageView.setImageBitmap(ingredientImage);

        mPlusImageButton = (ImageButton) view.findViewById(R.id.imageButtonPlus);
        mMinusImageButton = (ImageButton) view.findViewById(R.id.imageButtonMinus);

        quantityToChange = 1;

        mQuantityTextView = (TextView) view.findViewById(R.id.textViewQuantity);
        mQuantityTextView.setText(Integer.toString(quantityToChange));

        mPlusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                quantityToChange = onPlusButtonClick();
                mQuantityTextView.setText(Integer.toString(quantityToChange));
            }
        });

        mMinusImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onMinusButtonClick();
                mQuantityTextView.setText(Integer.toString(quantityToChange));
            }
        });

        builder.setView(view)
                .setTitle("Ingredient Info")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //add code to cancel the add ingredient function
                    }
                })
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        listener.getQuantityToChange(quantityToChange);
                    }
                });
        return builder.create();
    }


    public int onPlusButtonClick(){
        quantityToChange = quantityToChange + 1;
        return  quantityToChange;
    }

    public int onMinusButtonClick(){
        quantityToChange = quantityToChange - 1;
        return quantityToChange;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (PopUpDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface PopUpDialogListener {
        void getQuantityToChange(int mQuantityToChange);
    }
}

