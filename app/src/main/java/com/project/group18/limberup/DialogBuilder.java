package com.project.group18.limberup;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.widget.Toast;

public class DialogBuilder  extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle(R.string.pick_device)
                .setItems(R.array.device_type, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // The 'which' argument contains the index position
                        // of the selected item
                        if(which == 0)
                        {

                            ((UserProfileActivity)getActivity()).dispatchTakePictureIntent();
                        }else if(which == 1)
                        {
                            ((UserProfileActivity)getActivity()).chooseImage();
                        }
                        else {
                            Toast.makeText(getActivity(), "There is no such choice available", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        return builder.create();
    }
}
